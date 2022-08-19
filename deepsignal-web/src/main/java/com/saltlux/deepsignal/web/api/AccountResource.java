package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.api.errors.EmailAlreadyUsedException;
import com.saltlux.deepsignal.web.api.errors.InvalidPasswordException;
import com.saltlux.deepsignal.web.api.errors.LoginAlreadyUsedException;
import com.saltlux.deepsignal.web.api.vm.KeyAndPasswordVM;
import com.saltlux.deepsignal.web.api.vm.ManagedUserVM;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.exception.AccountAlreadyExistException;
import com.saltlux.deepsignal.web.exception.AccountUnconfirmException;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.MailService;
import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.service.dto.*;
import com.saltlux.deepsignal.web.service.impl.ConnectomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.SimpleFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import liquibase.pro.packaged.D;
import liquibase.pro.packaged.U;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Account Management", description = "The account management API")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    ConnectomeService connectomeService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    //    @PostMapping("/register")
    ////    @ResponseStatus(HttpStatus.CREATED)
    ////    @Operation(summary = "Register the user", tags = { "Account Management" }, security = @SecurityRequirement(name = "basicAuth"))
    ////    public void registerAccount(@Valid @RequestBody z managedUserVM) {
    ////        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
    ////            throw new InvalidPasswordException();
    ////        }
    ////        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
    ////        mailService.sendActivationEmail(user);
    ////    }

    /**
     * {@code POST  /account} : register the current user information.
     *
     * @param accountDTO the current user information
     * @return
     */
    //    @PostMapping("/register")
    //    @ResponseStatus(HttpStatus.CREATED)
    //    @Operation(summary = "Register current user", tags = { "Account Management" })
    //    public ResponseEntity<?> registerAccount(@Valid @RequestBody AccountDTO accountDTO) {
    //        if (Objects.isNull(accountDTO.getPurposeSet())) {
    //            return new ResponseEntity(
    //                new ApiResponse(
    //                    false,
    //                    Constants.ErrorCode.DEEPSINAL_REGISTER_PURPOSE_NULL.description,
    //                    Constants.ErrorCode.DEEPSINAL_REGISTER_PURPOSE_NULL.code
    //                ),
    //                HttpStatus.BAD_REQUEST
    //            );
    //        }
    //
    //        if (StringUtils.isEmpty(accountDTO.getNameConnectome())) {
    //            return new ResponseEntity(
    //                new ApiResponse(
    //                    false,
    //                    Constants.ErrorCode.DEEPSINAL_REGISTER_CONNECTOME_EMPTY.description,
    //                    Constants.ErrorCode.DEEPSINAL_REGISTER_CONNECTOME_EMPTY.code
    //                ),
    //                HttpStatus.BAD_REQUEST
    //            );
    //        }
    //
    //        try {
    //            userService.registerAccount(accountDTO);
    //            return ResponseEntity.ok().body(null);
    //        } catch (AccountUnconfirmException au) {
    //            return new ResponseEntity(
    //                new ApiResponse(false, Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_UNCONFIRM.description),
    //                HttpStatus.FORBIDDEN
    //            );
    //        } catch (AccountAlreadyExistException ax) {
    //            return new ResponseEntity(
    //                new ApiResponse(false, Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_EXISTED.description),
    //                HttpStatus.CONFLICT
    //            );
    //        }
    //    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    @Operation(summary = "Activate the registered user", tags = { "Account Management" })
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    @Operation(summary = "Check the current user is authenticated", tags = { "Account Management" })
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    @Operation(summary = "Get the current user information", tags = { "Account Management" })
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException          {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    @Operation(summary = "Update the current user information", tags = { "Account Management" })
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    @Operation(summary = "Changes the current user's password", tags = { "Account Management" })
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    @Operation(summary = "Send an email to reset the password of the user", tags = { "Account Management" })
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException         {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    @Operation(summary = "Finish to reset the password of the user", tags = { "Account Management" })
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    //send email code
    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmail(@RequestBody AccountDTO accountDTO) {
        String codeConfirm = codeConfirm();

        if (!mailService.mapEmailCode(accountDTO.getEmail(), codeConfirm)) {
            return new ResponseEntity(new ApiResponse(false, "Can't create code"), HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = userRepository.findOneByEmail(accountDTO.getEmail());
        User user = new User();
        if (!userOptional.isPresent()) {
            //            return new ResponseEntity<>(new ApiResponse(false, "User does not exist!"), HttpStatus.BAD_REQUEST);
            user.setEmail(accountDTO.getEmail());
            user.setLogin(accountDTO.getEmail());
            user.setLangKey(accountDTO.getLangKey());
        } else {
            user = userOptional.get();
        }
        //        String subject = "Email from DeepSignal team";
        //        String content = "<p>Your verify code is: <strong>" + codeConfirm + "</strong>. The code is only valid for 5 minutes!</p>";
        try {
            mailService.sendActivationEmail(user, codeConfirm);
            //            mailService.sendEmail(accountDTO.getEmail(), subject, content, false, true);
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("email/noticeActivateAccount")
    @Operation(summary = "Send email to notice that activate account", tags = { "Account Management" })
    public ResponseEntity<?> noticeActivateAccount(@RequestParam("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return ResponseEntity.badRequest().body("Id is null");
        }
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.badRequest().body("User doesn't exist");
        }
        if (user.getActivated() == 1) {
            try {
                mailService.sendEmailNoticeActivation(user);
            } catch (Exception e) {
                log.error(e.toString());
                return ResponseEntity.badRequest().body("Send mail failed: " + e.toString());
            }
        }
        return ResponseEntity.ok().body("Send mail successfully!");
    }

    //verify email code
    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody AccountDTO accountDTO) {
        if (!mailService.verifyCodeEmail(accountDTO)) {
            Optional<AdminUserDTO> adminUserDTO = userService.updateFailedAttemptLoginToLockAccount(accountDTO.getLogin());
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSIGNAL_VERIFY_EMAIL_CODE_INCORRECT.description,
                    Constants.ErrorCode.DEEPSIGNAL_VERIFY_EMAIL_CODE_INCORRECT.code,
                    adminUserDTO.get().getLoginFailedCount()
                ),
                HttpStatus.BAD_REQUEST
            );
        } else {
            userService.registerEmail(accountDTO);
            return ResponseEntity.ok().body(accountDTO.getEmail());
        }
    }

    @GetMapping("/public/checkEmailExisted/{email}")
    public ResponseEntity<?> checkEmailIsExisted(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.checkEmailExisted(email));
    }

    @GetMapping("/public/checkUsernameExisted/{username}")
    public ResponseEntity<?> checkUsernameExisted(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.checkUsernameExisted(username));
    }

    //Receive newsLetters and messages
    @PostMapping("/public/receive")
    public ResponseEntity<?> receive(@RequestBody AccountDTO accountDTO) {
        if (StringUtils.isEmpty(accountDTO.getPhoneNumber()) && StringUtils.isEmpty(accountDTO.getEmail())) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_NOT_EXISTED.description,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_NOT_EXISTED.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        if (userService.userReceive(accountDTO)) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.badRequest().body(null);
    }

    //Get purposes and register
    @PostMapping("/register")
    public ResponseEntity<?> getPurposes(@RequestBody AccountDTO accountDTO) {
        if (accountDTO.getPurposeSet().size() == 0) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_PURPOSE_NULL.description,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_PURPOSE_NULL.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        if (!userService.registerUser(accountDTO)) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_UNCONFIRM.description,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_UNCONFIRM.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        return ResponseEntity.ok().body(null);
    }

    //save connectome
    @PostMapping("/public/connectome")
    public ResponseEntity<?> saveConnectome(@RequestBody AccountDTO accountDTO) {
        if (accountDTO.getNameConnectome() == null || accountDTO.getNameConnectome() == "") {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_CONNECTOME_EMPTY.description,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_CONNECTOME_EMPTY.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        User user = new User();
        if (!StringUtils.isEmpty(accountDTO.getLogin())) {
            user = userRepository.findOneByLogin(accountDTO.getLogin()).get();
        } else if (!StringUtils.isEmpty(accountDTO.getPhoneNumber())) {
            user = userRepository.findOneByLogin(accountDTO.getPhoneNumber()).get();
        } else {
            user = userRepository.findOneByLogin(accountDTO.getEmail()).get();
        }
        Connectome connectome = new Connectome();
        connectome.setConnectomeName(accountDTO.getNameConnectome());
        connectome.setConnectomeJob(accountDTO.getNameConnectome());
        connectome.setUser(user);
        try {
            connectomeService.save(connectome);
            return ResponseEntity.ok().body(connectome.getConnectomeId());
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    //upload connectome image profile
    @PostMapping("/public/upload")
    public ResponseEntity<?> uploadConnectomeImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam("connectomeId") String connectomeId
    ) {
        String path = userService.saveFileUpload(connectomeId, file);
        if (path == null) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSIGNAL_UPLOAD_CONNECTOME_IMAGE_PROFILE_FALSE.description,
                    Constants.ErrorCode.DEEPSIGNAL_UPLOAD_CONNECTOME_IMAGE_PROFILE_FALSE.code
                ),
                HttpStatus.BAD_REQUEST
            );
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

    //get user by id
    @GetMapping("/public/getUser")
    public ResponseEntity<?> getUserByPhoneNumber(@RequestParam("id") String id) {
        if (id == null || id == "") {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.description,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        User user = userService.findUserById(id);
        if (user == null) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED.description,
                    Constants.ErrorCode.DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED.code
                ),
                HttpStatus.BAD_REQUEST
            );
        } else {
            return ResponseEntity.ok().body(user);
        }
    }

    @GetMapping("/public/checkUserIsExisted")
    public ResponseEntity<?> checkUserIsExisted(@RequestParam("phoneNumber") String phoneNumber) {
        if (userService.findUser(phoneNumber) != null) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_EXISTED.description,
                    Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_EXISTED.code
                ),
                HttpStatus.BAD_REQUEST
            );
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

    @GetMapping("/getUserById")
    public ResponseEntity<?> getUserById(@RequestParam("id") String id) {
        if (StringUtils.isEmpty(id)) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.description,
                    Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        User user = userService.findUserById(id);
        if (Objects.isNull(user)) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED.description,
                    Constants.ErrorCode.DEEPSIGNAL_CHECK_USER_ID_NOT_EXXISTED.code
                ),
                HttpStatus.BAD_REQUEST
            );
        } else {
            return ResponseEntity.ok().body(user);
        }
    }

    private String codeConfirm() {
        StringBuilder codeConfirm = new StringBuilder();
        for (int i = 0; i < applicationProperties.getTwilio().getLengthCode(); i++) {
            int random = ThreadLocalRandom.current().nextInt(0, 10);
            codeConfirm.append(random);
        }
        return codeConfirm.toString();
    }
}
