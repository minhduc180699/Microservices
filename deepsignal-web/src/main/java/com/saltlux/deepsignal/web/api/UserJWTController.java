package com.saltlux.deepsignal.web.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltlux.deepsignal.web.api.vm.LoginVM;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.domain.Connectome;
import com.saltlux.deepsignal.web.domain.User;
import com.saltlux.deepsignal.web.domain.UserDevice;
import com.saltlux.deepsignal.web.repository.ConnectomeRepository;
import com.saltlux.deepsignal.web.repository.UserDeviceRepository;
import com.saltlux.deepsignal.web.repository.UserRepository;
import com.saltlux.deepsignal.web.security.jwt.JWTFilter;
import com.saltlux.deepsignal.web.security.jwt.TokenProvider;
import com.saltlux.deepsignal.web.service.IUserDevice;
import com.saltlux.deepsignal.web.service.MailService;
import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.service.dto.AccountDTO;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import com.saltlux.deepsignal.web.util.ConnectAdapterApi;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RMapCache;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "User JWT Management", description = "The public user management API")
@Log4j2
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;
    private final UserRepository userRepository;
    private final ConnectomeRepository connectomeRepository;
    private String loginClone = null;
    private boolean isWebSocketConnected = false;
    private Timer timer;
    private TimerTask timerTask;
    private Integer countTimer = 0;
    private ConnectAdapterApi connectAdapterApi;
    private String connectomeId = null;
    private final IUserDevice iUserDevice;
    private final UserDeviceRepository userDeviceRepository;
    private final MailService mailService;
    private final UserService userService;

    @Autowired
    private RMapCache<String, String> mapToken;

    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        ConnectomeRepository connectomeRepository,
        RabbitTemplate rabbitTemplate,
        ApplicationProperties applicationProperties,
        UserRepository userRepository,
        ConnectAdapterApi connectAdapterApi,
        IUserDevice iUserDevice,
        UserDeviceRepository userDeviceRepository,
        MailService mailService,
        UserService userService
    ) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
        this.userRepository = userRepository;
        this.connectomeRepository = connectomeRepository;
        this.connectAdapterApi = connectAdapterApi;
        this.iUserDevice = iUserDevice;
        this.userDeviceRepository = userDeviceRepository;
        this.mailService = mailService;
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate login user", tags = { "User JWT Management" })
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        Optional<User> user = userRepository.findOneByLogin(loginVM.getUsername());
        if (!user.isPresent()) {
            return new ResponseEntity<>(new JWTToken(null, null), HttpStatus.BAD_REQUEST);
        }
        String secretKey = "";
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(loginVM.getDeviceId())) {
            Optional<UserDevice> userDevice = userDeviceRepository.findUserDeviceByDeviceIdAndUser_Id(
                loginVM.getDeviceId(),
                user.get().getId()
            );
            if (!userDevice.isPresent()) {
                try {
                    secretKey = iUserDevice.saveUserDevice(loginVM.getUsername(), loginVM.getDeviceId());
                } catch (Exception e) {
                    log.error(
                        "Can not save deviceId: " +
                        loginVM.getDeviceId() +
                        " with userName: " +
                        loginVM.getUsername() +
                        " because of: " +
                        e.getMessage()
                    );
                }
            } else {
                secretKey = userDevice.get().getSecretKey();
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        this.pushToQueueLoginTime(loginVM.getUsername(), loginVM.getLanguage());
        mapToken.put(jwt, jwt);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt, secretKey), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh the authentication token for the current user", tags = { "User JWT Management" })
    public ResponseEntity<JWTToken> authorize(@RequestHeader("Authorization") String bearerToken, @RequestParam boolean isCheckResetTk) {
        try {
            String jwt = "";
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                jwt = bearerToken.substring(7);
            }
            String newJwt = tokenProvider.refreshToken(jwt, isCheckResetTk);
            if (newJwt != null) {
                mapToken.put(newJwt, newJwt);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + newJwt);

                return new ResponseEntity<>(new JWTToken(newJwt, ""), httpHeaders, HttpStatus.OK);
            } else {
                return ResponseEntity.internalServerError().body(null);
            }
        } catch (Exception ex) {
            return new ResponseEntity(new ApiResponse(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/public/getToken")
    @Operation(summary = "Get JWT by secretKey", tags = { "User JWT Management" })
    public ResponseEntity<?> getSecretKey(@RequestParam("secretKey") String secretKey) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(secretKey)) {
            return ResponseEntity.badRequest().body("secretKey is null!");
        }
        Optional<UserDevice> userDeviceOptional = userDeviceRepository.findUserDeviceBySecretKey(secretKey);
        if (userDeviceOptional.isPresent()) {
            User user = userDeviceOptional.get().getUser();
            String codeConfirm = mailService.codeConfirm();
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setLogin(user.getLogin());
            accountDTO.setEmailCode(codeConfirm);
            userService.registerEmail(accountDTO);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getLogin(), codeConfirm);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, true);
            this.pushToQueueLoginTime(user.getLogin(), user.getLangKey());
            mapToken.put(jwt, jwt);
            return ResponseEntity.ok().body(new JWTToken(jwt, secretKey));
        }
        return ResponseEntity.badRequest().body("Can not find user by secretKey");
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        private String secretKey;

        JWTToken(String idToken, String secretKey) {
            this.idToken = idToken;
            this.secretKey = secretKey;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("secret_key")
        String getSecretKey() {
            return secretKey;
        }

        void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        this.isWebSocketConnected = true;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        this.isWebSocketConnected = false;
    }

    @GetMapping("/clearTimer/{login}")
    @Operation(summary = "Clear timer run when user logged in", tags = { "User JWT Management" })
    public void clearTimer(@PathVariable String login, HttpServletRequest httpServletRequest) {
        String loginName = httpServletRequest.getUserPrincipal().getName();
        log.info("Clearing timer by login: {}", login);
        if (login.equals(loginName)) {
            if (null != this.timer) {
                this.timer.cancel();
            }
            if (null != this.timerTask) {
                this.timerTask.cancel();
            }
        }
    }

    //    private void callExternalAPI() {
    //        try {
    //            if (!StringUtil.isNullOrEmpty(connectomeId)) {
    //                String uri = applicationProperties.getExternalApi().getDeepsignalIssueTracking() + "/trackingStartByConnectome";
    //                Map<String, Object> params = new HashMap<>();
    //                params.put("connectomeId", connectomeId);
    //                connectAdapterApi.getDataFromAdapterApi(uri, params, HttpMethod.POST);
    //            }
    //        } catch (Exception e) {
    //            log.error(e.getMessage());
    //        }
    //    }

    private void pushToQueueLoginTime(String login, String language) {
        try {
            this.timer = new Timer();
            this.loginClone = login;
            // every 5 minutes, push connectome authenticated to RabbitMQ
            this.timerTask =
                new TimerTask() {
                    @Override
                    public void run() {
                        String loggedIn = StringUtil.isNullOrEmpty(login) ? loginClone : login;
                        String lang = StringUtil.isNullOrEmpty(language) ? "en" : language;
                        Optional<User> user = userRepository.findOneByLoginAndActivated(loggedIn, 1);
                        if (user.isPresent()) {
                            log.info("Push message after login from user: {}", user.get().getLogin());
                            List<Connectome> connectomes = connectomeRepository.findByUser_Id(user.get().getId());
                            if (connectomes.size() > 0) {
                                connectomeId = connectomes.get(0).getConnectomeId();
                                long date = new Date().getTime();
                                rabbitTemplate.convertAndSend(
                                    applicationProperties.getRabbitConfig().getWebLogging().getExchangeName(),
                                    applicationProperties.getRabbitConfig().getWebLogging().getRouterKey(),
                                    new WebLogging(connectomeId, Long.toString(date), lang)
                                );
                            }
                        }
                        // when user logout or close browser, clear timer
                        if (!isWebSocketConnected && countTimer > 0) {
                            timer.cancel();
                            if (null != timerTask) {
                                timerTask.cancel();
                            }
                        }
                        countTimer++;
                    }
                };
            timer.schedule(timerTask, 0, 300000);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebLogging {

        private String connectomeId;
        private String loginTime;
        private String language;
    }
}
