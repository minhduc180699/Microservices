package com.saltlux.deepsignal.web.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.api.errors.BadRequestAlertException;
import com.saltlux.deepsignal.web.api.errors.EmailAlreadyUsedException;
import com.saltlux.deepsignal.web.api.errors.LoginAlreadyUsedException;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.AuthoritiesConstants;
import com.saltlux.deepsignal.web.service.MailService;
import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.service.dto.AdminUserDTO;
import com.saltlux.deepsignal.web.service.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "User Management", description = "The user management API - only allowed for the administrators")
public class UserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey")
    );

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public UserResource(UserService userService, UserRepository userRepository, MailService mailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /admin/users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(summary = "Creates a new user", tags = { "User Management" })
    public ResponseEntity<User> createUser(@Valid @RequestBody AdminUserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity
                .created(new URI("/api/admin/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /admin/users} : Updates an existing User.
     *
     * @param strUserDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(summary = "Updates an existing User", tags = { "User Management" })
    @UserActivity(activityName = Constants.UserActivities.UPDATE_USER_INFO)
    public ResponseEntity<AdminUserDTO> updateUser(
        @Valid @RequestPart(value = "user") String strUserDTO,
        @RequestPart(value = "file", required = false) MultipartFile file,
        @RequestPart(value = "connectomeId", required = false) String connectomeId
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        AdminUserDTO userDTO = objectMapper.readValue(strUserDTO, AdminUserDTO.class);
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = null;
        if (userDTO.getEmail() != null && userDTO.getEmail() != "") existingUser =
            userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser != null && existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser != null && existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<AdminUserDTO> updatedUser = userService.updateUser(userDTO, file);

        return ResponseUtil.wrapOrNotFound(
            updatedUser,
            HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin())
        );
    }

    @PutMapping("/users/management")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(summary = "Updates an existing User without multipart file", tags = { "User Management" })
    @UserActivity(activityName = Constants.UserActivities.UPDATE_USER_INFO)
    public ResponseEntity<?> updateUser(@RequestBody AdminUserDTO adminUserDTO) throws JsonProcessingException {
        Optional<User> user = userRepository.findOneByLogin(adminUserDTO.getLogin());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("User doesn't exist");
        }
        if (!user.get().getId().equals(adminUserDTO.getId())) {
            return ResponseEntity.badRequest().body("User info is incorrect");
        } else {
            return ResponseEntity.ok().body(userService.managementUser(adminUserDTO));
        }
    }

    @PutMapping("/users/updateLastLogin")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(summary = "Updates an existing User", tags = { "User Management" })
    public ResponseEntity<AdminUserDTO> updateUserLastLogin(@RequestParam("login") String login, @RequestParam("ip") String ip) {
        Optional<AdminUserDTO> updatedUser = userService.updateUser(login, ip);
        return ResponseUtil.wrapOrNotFound(updatedUser, HeaderUtil.createAlert(applicationName, "userManagement.updated", login));
    }

    /**
     * {@code GET /admin/users} : get all users with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(summary = "Get all users with all the details", tags = { "User Management" })
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(Pageable pageable) {
        log.debug("REST request to get all User for an admin");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<AdminUserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code GET /admin/users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(summary = "Get the \"login\" user", tags = { "User Management" })
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(userService.getUserWithAuthoritiesByLogin(login).map(AdminUserDTO::new));
    }

    /**
     * {@code DELETE /admin/users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PutMapping("/users/{login}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(summary = "delete the \"login\" User", tags = { "User Management" })
    public ResponseEntity<AdminUserDTO> deleteUser(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.debug("REST request to delete User: {}", login);
        Optional<AdminUserDTO> adminUserDTO = userService.updateUser(login, 1);
        return ResponseUtil.wrapOrNotFound(adminUserDTO, HeaderUtil.createAlert(applicationName, "userManagement.deleted", login));
    }

    @GetMapping("/users/searchEmail")
    @Operation(summary = "search email", tags = { "User Management" })
    public ResponseEntity<?> searchEmail(@RequestParam("key") String key) {
        if (StringUtils.isEmpty(key)) {
            return ResponseEntity.badRequest().body("Key is null");
        }
        return ResponseEntity.ok().body(userService.searchEmail(key));
    }

    @GetMapping("/users/search")
    @Operation(summary = "search user by phone or email", tags = { "User Management" })
    public ResponseEntity<?> searchUser(
        @RequestParam("keyword") String keyword,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page
    ) {
        if (StringUtils.isEmpty(keyword)) {
            return ResponseEntity.badRequest().body("keyword is null");
        }
        return ResponseEntity.ok().body(userService.searchUser(keyword, page));
    }

    @GetMapping("users/info")
    @Operation(summary = "get user info", tags = { "User Management" })
    public ResponseEntity<?> getUserInfo(@RequestParam("login") String login) {
        if (StringUtils.isEmpty(login)) {
            return ResponseEntity.badRequest().body("param is null");
        }
        if (userService.findUser(login) == null) {
            return ResponseEntity.badRequest().body("Can not find user");
        } else {
            return ResponseEntity.ok().body(userService.findUser(login));
        }
    }

    @DeleteMapping("users/delete")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(summary = "delete user", tags = { "User Management" })
    public ResponseEntity<?> deleteUserByAdmin(@RequestParam("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return ResponseEntity.badRequest().body("param is null");
        }
        if (!userService.deleteUserByAdmin(id)) {
            return ResponseEntity.badRequest().body("An error occurred, please try again later");
        } else {
            return ResponseEntity.ok().body("Delete successfully");
        }
    }
}
