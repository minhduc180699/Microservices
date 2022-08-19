package com.saltlux.deepsignal.web.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class MailParamsDTO {

    String confirmCode;
    String Day;
    List<String> emails;
    String message;
    String linkShare;
    String dayShare;
}
