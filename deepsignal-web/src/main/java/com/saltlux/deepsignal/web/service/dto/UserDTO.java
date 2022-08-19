package com.saltlux.deepsignal.web.service.dto;

import com.saltlux.deepsignal.web.domain.User;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Getter
@Setter
public class UserDTO {

    private String id;

    @NotNull
    private String login;

    private String imageUrl;

    private String fullName;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
        this.fullName =
            StringUtils.isNotBlank(user.getFirstName()) && StringUtils.isNotBlank(user.getLastName())
                ? user.getFirstName() + " " + user.getLastName()
                : user.getLogin();
    }

    public UserDTO(String login) {
        this.login = login;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDTO{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            "}";
    }
}
