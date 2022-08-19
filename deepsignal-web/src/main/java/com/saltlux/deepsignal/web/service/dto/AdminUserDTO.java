package com.saltlux.deepsignal.web.service.dto;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Authority;
import com.saltlux.deepsignal.web.domain.User;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A DTO representing a user, with his authorities.
 */
public class AdminUserDTO {

    private String id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    //    @Size(max = 60)
    //    private String passwordHash;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 7)
    private String phoneCountry;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 256)
    private String imageUrl;

    private Integer activated;

    @Size(min = 2, max = 10)
    private String langKey;

    @Size(max = 20)
    private String activationKey;

    @Size(max = 20)
    private String resetKey;

    private String createdBy;

    private Instant createdDate;

    private Instant resetDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Integer termOfService;

    private Integer receiveNews;

    private Integer loginFailedCount;

    private Instant lockedOn;

    private Instant lastLogin;

    private String ip;

    private Integer deleted;

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getLoginFailedCount() {
        return loginFailedCount;
    }

    public void setLoginFailedCount(Integer loginFailedCount) {
        this.loginFailedCount = loginFailedCount;
    }

    public Instant getLockedOn() {
        return lockedOn;
    }

    public void setLockedOn(Instant lockedOn) {
        this.lockedOn = lockedOn;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private Set<String> authorities;

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneCountry = user.getPhoneCountry();
        this.phoneNumber = user.getPhoneNumber();
        this.activated = user.getActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.activationKey = user.getActivationKey();
        this.resetKey = user.getResetKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.resetDate = user.getResetDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.termOfService = user.getTermOfService();
        this.receiveNews = user.getReceiveNews();
        this.loginFailedCount = user.getLoginFailedCount();
        this.lockedOn = user.getLockedOn();
        this.lastLogin = user.getLastLogin();
        this.ip = user.getIp();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
        this.deleted = user.getDeleted();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getActivated() {
        return activated;
    }

    public void setActivated(Integer activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getPhoneCountry() {
        return phoneCountry;
    }

    public void setPhoneCountry(String phoneCountry) {
        this.phoneCountry = phoneCountry;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public Integer getTermOfService() {
        return termOfService;
    }

    public void setTermOfService(Integer termOfService) {
        this.termOfService = termOfService;
    }

    public Integer getReceiveNews() {
        return receiveNews;
    }

    public void setReceiveNews(Integer receiveNews) {
        this.receiveNews = receiveNews;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminUserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            "}";
    }
}
