package com.saltlux.deepsignal.web.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneCodeDTO {

    private String countryCode;
    private String phoneNumber;
    private String code;
    private Integer type;
    private String oldPhoneNumber;
    private String login;
}
