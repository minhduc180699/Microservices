package com.saltlux.deepsignal.web.service;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.service.dto.AccountDTO;
import com.saltlux.deepsignal.web.service.dto.PhoneCodeDTO;

public interface IPhoneService {
    PhoneCodeDTO sendSMS(PhoneCodeDTO phoneCodeDTO);

    Constants.ErrorCode verifyCode(AccountDTO accountDTO);
}
