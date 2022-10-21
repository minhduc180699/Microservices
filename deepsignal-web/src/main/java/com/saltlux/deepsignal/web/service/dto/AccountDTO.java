package com.saltlux.deepsignal.web.service.dto;

import com.saltlux.deepsignal.web.domain.Authority;
import com.saltlux.deepsignal.web.domain.Purpose;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AccountDTO extends PhoneCodeDTO {

    private Set<String> purposeSet;
    private String nameConnectome;
    private Integer termOfService;
    private Integer receiveNews;
    private String email;
    private String emailCode;
    private String lastName;
    private Integer deleted;
    private Set<Authority> authorities;
    private String langKey;
    private String timeZone;
    private String deviceId;
    //    private MultipartFile file;
}
