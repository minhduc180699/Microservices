package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Authority;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.exception.*;
import com.saltlux.deepsignal.web.repository.*;
import com.saltlux.deepsignal.web.security.AuthoritiesConstants;
import com.saltlux.deepsignal.web.security.SecurityUtils;
import com.saltlux.deepsignal.web.service.dto.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    @Autowired
    private ConnectomeRepository connectomeRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    @Autowired
    ApplicationProperties applicationProperties;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.setActivated(1);
                    user.setActivationKey(null);
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                }
            );
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(user -> user.getActivated() == 1)
            .map(
                user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(1);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    @Transactional
    public User registerAccount(AccountDTO accountDTO) {
        Optional<User> user = userRepository.findOneByLogin(accountDTO.getPhoneNumber());
        if (!user.isPresent()) {
            throw new AccountUnconfirmException();
        }

        if (user.isPresent() && user.get().getActivated().equals(1)) {
            throw new AccountAlreadyExistException();
        }
        User existedUser = user.get();
        // new user is  active
        existedUser.setActivated(1);
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        existedUser.setAuthorities(authorities);
        //        existedUser.setPurposes(accountDTO.getPurposeSet());
        userRepository.save(existedUser);
        // save connectome
        Connectome connectomeNew = new Connectome();
        connectomeNew.setConnectomeName(accountDTO.getNameConnectome());
        connectomeNew.setUser(existedUser);
        connectomeNew.setConnectomeJob(accountDTO.getNameConnectome());
        connectomeRepository.save(connectomeNew);
        //        //save purpose
        //        UserPurposeDetail userPurposeDetail = new UserPurposeDetail();
        //        userPurposeDetail.setUser(existedUser);
        //        userPurposeDetail.setPurpose(accountDTO.getPurpose());
        //        userPurposeDetailRepository.save(userPurposeDetail);
        this.clearUserCaches(existedUser);
        log.debug("Created Information for User: {}", existedUser);
        return existedUser;
    }

    @Transactional
    public User registerUserByPhone(AccountDTO accountDTO) {
        Optional<User> existedUser = userRepository.findOneByLogin(accountDTO.getLogin());
        Optional<User> existedUserEditPhone = userRepository.findOneByLogin(accountDTO.getLogin());
        String encryptedPassword = passwordEncoder.encode(accountDTO.getCode());
        if (existedUserEditPhone.isPresent()) {
            User userEditPhone = existedUserEditPhone.get();
            userEditPhone.setPassword(encryptedPassword);
            userEditPhone.setPhoneNumber(accountDTO.getPhoneNumber());
            if (userEditPhone.getTimeZone() == null && accountDTO.getTimeZone() != null) userEditPhone.setTimeZone(
                accountDTO.getTimeZone()
            );
            userRepository.save(userEditPhone);
            this.clearUserCaches(userEditPhone);
            return userEditPhone;
        }
        if (existedUser.isPresent()) {
            User user = existedUser.get();
            user.setPassword(encryptedPassword);
            if (user.getTimeZone() == null && accountDTO.getTimeZone() != null) user.setTimeZone(accountDTO.getTimeZone());
            userRepository.save(user);
            this.clearUserCaches(user);
            return user;
        }
        User newUser = new User();
        newUser.setLogin(accountDTO.getPhoneNumber());
        newUser.setPhoneNumber(accountDTO.getPhoneNumber());
        newUser.setPhoneCountry(accountDTO.getCountryCode());
        newUser.setPassword(encryptedPassword);
        newUser.setActivated(0);
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        newUser.setReceiveNews(accountDTO.getReceiveNews());
        newUser.setTermOfService(accountDTO.getTermOfService());
        newUser.setLastName(accountDTO.getLastName());
        newUser.setDeleted(accountDTO.getDeleted());
        if (StringUtils.isEmpty(accountDTO.getLangKey())) {
            newUser.setLangKey("en");
        } else {
            newUser.setLangKey(accountDTO.getLangKey());
        }
        if (accountDTO.getAuthorities() == null || accountDTO.getAuthorities().size() > 0) {
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
            newUser.setAuthorities(authorities);
        } else {
            newUser.setAuthorities(accountDTO.getAuthorities());
        }
        if (accountDTO.getTimeZone() != null) newUser.setTimeZone(accountDTO.getTimeZone());
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    @Transactional
    public User registerEmail(AccountDTO accountDTO) {
        Optional<User> existedUser = userRepository.findOneByLogin(accountDTO.getLogin());
        String encryptedPassword = passwordEncoder.encode(accountDTO.getEmailCode());
        if (existedUser.isPresent()) {
            User user = existedUser.get();
            user.setPassword(encryptedPassword);
            if (user.getTimeZone() == null && accountDTO.getTimeZone() != null) user.setTimeZone(accountDTO.getTimeZone());
            userRepository.save(user);
            this.clearUserCaches(user);
            return user;
        }
        User newUser = new User();
        newUser.setLogin(accountDTO.getEmail());
        newUser.setEmail(accountDTO.getEmail());
        //        newUser.setPhoneCountry(phoneCodeDTO.getCountryCode());
        newUser.setPassword(encryptedPassword);
        newUser.setActivated(0);
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        newUser.setReceiveNews(accountDTO.getReceiveNews());
        newUser.setTermOfService(accountDTO.getTermOfService());
        newUser.setLastName(accountDTO.getLastName());
        newUser.setDeleted(accountDTO.getDeleted());
        if (StringUtils.isEmpty(accountDTO.getLangKey())) {
            newUser.setLangKey("en");
        } else {
            newUser.setLangKey(accountDTO.getLangKey());
        }
        if (accountDTO.getAuthorities() == null || accountDTO.getAuthorities().size() > 0) {
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
            newUser.setAuthorities(authorities);
        } else {
            newUser.setAuthorities(accountDTO.getAuthorities());
        }
        if (accountDTO.getTimeZone() != null) newUser.setTimeZone(accountDTO.getTimeZone());
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated() == 1) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(1);
        user.setDeleted(0);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.getActivated());
                    user.setLangKey(userDTO.getLangKey());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO
                        .getAuthorities()
                        .stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    public boolean saveFile(MultipartFile file, File uploadFolder) {
        try {
            if (file == null) return false;

            String fileName = file.getOriginalFilename();
            File rs = new File(uploadFolder, fileName);
            if (rs.exists()) {
                rs.delete();
            }
            file.transferTo(rs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO, MultipartFile file) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (checkValidString(userDTO.getEmail())) user.setEmail(userDTO.getEmail().toLowerCase()); else user.setEmail(null);
                    if (checkValidString(userDTO.getPhoneCountry())) user.setPhoneCountry(userDTO.getPhoneCountry());
                    if (checkValidString(userDTO.getPhoneNumber())) user.setPhoneNumber(userDTO.getPhoneNumber()); else user.setPhoneNumber(
                        null
                    );

                    String UPLOAD_FOLDER = applicationProperties.getFilesUpload().getLocation();
                    String[] PATHS = UPLOAD_FOLDER.split("/");
                    String PATH = PATHS[PATHS.length - 1];
                    String pathUpload = UPLOAD_FOLDER + "/avatar";
                    File folderUpload = new File(pathUpload);
                    try {
                        if (!folderUpload.exists()) {
                            folderUpload.mkdirs();
                        }
                    } catch (Exception se) {
                        log.debug("can't create directory" + se.getMessage());
                    }
                    boolean isSaveFile = saveFile(file, folderUpload);
                    if (isSaveFile) {
                        user.setImageUrl("/" + PATH + "/avatar/" + file.getOriginalFilename());
                    }
                    user.setActivated(userDTO.getActivated());
                    user.setLangKey(userDTO.getLangKey());
                    if (userDTO.getAuthorities() != null) {
                        Set<Authority> managedAuthorities = user.getAuthorities();
                        managedAuthorities.clear();
                        userDTO
                            .getAuthorities()
                            .stream()
                            .map(authorityRepository::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(managedAuthorities::add);
                    }
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    @Transactional
    public String managementUser(AdminUserDTO adminUserDTO) {
        User user = userRepository.findById(adminUserDTO.getId()).get();
        user.setLogin(adminUserDTO.getLogin());
        user.setFirstName(adminUserDTO.getFirstName());
        user.setLastName(adminUserDTO.getLastName());
        user.setActivated(adminUserDTO.getActivated());
        user.setLangKey(adminUserDTO.getLangKey());
        if (adminUserDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            adminUserDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(authorities::add);
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        return user.getLogin();
    }

    public Optional<AdminUserDTO> updateUser(String login, String ip) {
        return Optional
            .of(userRepository.findOneByLogin(login))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    user.setLastLogin(Instant.now());
                    user.setIp(ip);
                    user.setLoginFailedCount(0);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    public Optional<AdminUserDTO> updateUser(String login, Integer deleted) {
        return Optional
            .of(userRepository.findOneByLogin(login))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    user.setDeleted(deleted);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setLangKey(langKey);
                    user.setImageUrl(imageUrl);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                }
            );
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);
                }
            );
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                }
            );
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public void deleteTemporaryAccount() {
        userRepository
            .findAllByActivatedAndCreatedDateBefore(3, Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting account temporary user {}", user.getLogin());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                }
            );
    }

    // save state of term-of-service and receive-news-letter
    public boolean userReceive(AccountDTO accountDTO) {
        Optional<User> optionalUser = userRepository.findOneByLogin(accountDTO.getPhoneNumber());
        Optional<User> optionalUser1 = userRepository.findOneByLogin(accountDTO.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            //            user.setTermOfService(accountDTO.getTermOfService());
            //            user.setReceiveNews(accountDTO.getReceiveNews());
            //            if (user.getActivated() != 1) {
            //                user.setActivated(1);
            //            }
            try {
                userRepository.save(user);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (optionalUser1.isPresent()) {
            User user = optionalUser1.get();
            //            if (user.getActivated() != 1) {
            //                user.setActivated(1);
            //            }
            try {
                userRepository.save(user);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    // save purposeSet, authorities
    public boolean registerUser(AccountDTO accountDTO) {
        Optional<User> user = userRepository.findOneByLogin(accountDTO.getPhoneNumber());
        if (!user.isPresent()) {
            user = userRepository.findOneByLogin(accountDTO.getEmail());
        }
        if (!user.isPresent()) {
            log.debug("User is not present!");
            return false;
        }
        if (user.isPresent() && user.get().getActivated() != 1) {
            throw new AccountAlreadyExistException();
        }

        User user1 = user.get();
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        user1.setAuthorities(authorities);
        //        List<Purpose> purposeList = new ArrayList<>();
        //        for (Object o : accountDTO.getPurposeSet()) {
        //            String purposeName = (String) o;
        //            if (purposeRepository.findOneByPurposeName(purposeName).isPresent()) {
        //                Purpose purpose = purposeRepository.findOneByPurposeName(purposeName).get();
        //                purposeList.add(purpose);
        //            }
        //        }
        try {
            for (String o : accountDTO.getPurposeSet()) {
                //                Purpose purpose = (Purpose) o;
                userRepository.saveUserPurposeDetail(user1.getId(), o);
            }
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    // create folder and save file
    public String saveFileUpload(String connectomeId, MultipartFile file) {
        //  D:/Project/DeepSignal/download/
        String FOLDER = applicationProperties.getFilesUpload().getLocation();
        String[] PATHS = FOLDER.split("/");
        String PATH = PATHS[PATHS.length - 1]; //download

        Connectome connectome = connectomeRepository.findConnectomeByConnectomeId(connectomeId).get();
        if (Objects.isNull(connectome)) {
            return null;
        }

        try {
            Date date = new Date();
            SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy_MM_dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH_mm_ss");
            String day = sdfDay.format(date);
            String time = sdfTime.format(date);
            String pathName = FOLDER + connectomeId;

            File dir = new File(pathName);
            if (!dir.exists() || dir.isFile()) {
                dir.mkdir();
            }

            String fileName = day + "." + time + "." + file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(dir.getAbsolutePath() + "/" + fileName);
            Files.write(path, bytes);

            String pathDisplay = "/" + PATH + "/" + connectomeId + "/" + fileName;
            connectome.setImageUrl(pathDisplay);
            connectomeRepository.save(connectome);
            return pathDisplay;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AdminUserDTO findUser(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        } else {
            try {
                User user = userRepository.findOneByLogin(username).get();
                AdminUserDTO adminUserDTO = new AdminUserDTO();
                BeanUtils.copyProperties(user, adminUserDTO);
                Set<String> authoritySet = new HashSet<>();
                for (Authority authority : user.getAuthorities()) {
                    authoritySet.add(authority.getName());
                }
                adminUserDTO.setAuthorities(authoritySet);
                return adminUserDTO;
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Transactional
    public boolean deleteUserByAdmin(String id) {
        if (userRepository.deleteUserById(id) > 0) {
            return true;
        } else {
            return false;
        }
    }

    //find user by email
    public boolean checkEmailExisted(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        return userRepository.existsUserByEmail(email);
    }

    public String checkUsernameExisted(String userName) {
        Optional<User> user = userRepository.findOneByLogin(userName);
        if (!user.isPresent()) {
            user = userRepository.findOneByEmail(userName);
            if (!user.isPresent()) {
                user = userRepository.findOneByPhoneNumber(userName);
                if (!user.isPresent()) {
                    return null;
                }
            }
        }
        if (user.get().getDeleted() == 1) {
            return null;
        }
        return user.get().getLogin();
    }

    //find userById
    public User findUserById(String id) {
        try {
            return userRepository.findById(id).get();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Optional<AdminUserDTO> updateFailedAttemptLoginToLockAccount(String login) {
        return Optional
            .of(userRepository.findOneByLogin(login))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    if (user.getActivated() != 0) {
                        int failedLogin = user.getLoginFailedCount() + 1;
                        if (user.getLoginFailedCount() > (applicationProperties.getSecurity().getLogin().getMaxFailedLogin() - 2)) {
                            TimerTask unlockAccount = new TimerTask() {
                                @Override
                                public void run() {
                                    user.setActivated(1);
                                    user.setLoginFailedCount(0);
                                    user.setLockedOn(null);
                                    userRepository.save(user);
                                }
                            };
                            user.setActivated(0);
                            Long lockTimeDuration = applicationProperties.getSecurity().getLogin().getLockTimeDuration();
                            Instant lockedOn = Instant.now().plusMillis(lockTimeDuration);
                            user.setLockedOn(lockedOn);
                            Timer timer = new Timer("Timer");
                            timer.schedule(unlockAccount, lockTimeDuration);
                        }
                        user.setLoginFailedCount(failedLogin);
                        this.clearUserCaches(user);
                        log.debug("Changed Information for User: {}", user);
                    }
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    public List<User> searchEmail(String key) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "email"));
        try {
            Page<User> userPage = userRepository.findByEmailContains(key, pageable);
            return userPage.getContent();
        } catch (Exception e) {
            return null;
        }
    }

    public UserManagementDTO searchUser(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(Sort.Direction.ASC, "login"));
        try {
            UserManagementDTO userManagementDTO = new UserManagementDTO();
            userManagementDTO.setCount(userRepository.getCountSearchUser(keyword).size());
            Page<AdminUserDTO> userPage = userRepository.searchUser(keyword, pageable).map(AdminUserDTO::new);
            userManagementDTO.setUserManagement(userPage.getContent());
            return userManagementDTO;
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    public User getCurrentUser() {
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        if (!user.isPresent()) {
            throw new com.saltlux.deepsignal.web.api.errors.BadRequestException("User not exist!");
        }
        return user.get();
    }

    boolean checkValidString(String str) {
        return str != "" && str != null;
    }
}
