package com.saltlux.deepsignal.web.api;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.exception.AccountAlreadyNotExistException;
import com.saltlux.deepsignal.web.service.IPhoneService;
import com.saltlux.deepsignal.web.service.UserService;
import com.saltlux.deepsignal.web.service.dto.AccountDTO;
import com.saltlux.deepsignal.web.service.dto.AdminUserDTO;
import com.saltlux.deepsignal.web.service.dto.ApiResponse;
import com.saltlux.deepsignal.web.service.dto.PhoneCodeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/phone")
@Tag(name = "Phone Management", description = "The phone management API")
public class PhoneController {

    @Autowired
    private IPhoneService IPhoneService;

    @Autowired
    private UserService userService;

    private final String PATTERN = "^\\+[1-9]{1}[0-9]{3,14}$";

    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    /**
     * {@code POST  /send} : Send a code to verify phone number.
     *
     * @param phoneCodeDTO the information for send SMS
     * @return
     */
    @PostMapping("/send")
    @Operation(summary = "Send a code to verify phone number", tags = { "Phone Management" })
    public ResponseEntity<?> sendCode(@Valid @RequestBody PhoneCodeDTO phoneCodeDTO) {
        if (StringUtils.isBlank(phoneCodeDTO.getCountryCode())) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_VERIFY_COUNTRY_CODE_BLANK.description,
                    Constants.ErrorCode.DEEPSINAL_VERIFY_COUNTRY_CODE_BLANK.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }
        if (StringUtils.isBlank(phoneCodeDTO.getPhoneNumber())) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_VERIFY_PHONE_BLANK.description,
                    Constants.ErrorCode.DEEPSINAL_VERIFY_PHONE_BLANK.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }

        if (!validateFormatPhoneNumber(phoneCodeDTO.getPhoneNumber())) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_VERIFY_PHONE_INCORRECT_FORMAT.description,
                    Constants.ErrorCode.DEEPSINAL_VERIFY_PHONE_INCORRECT_FORMAT.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }

        if (!isPhoneNumber(phoneCodeDTO.getCountryCode(), phoneCodeDTO.getPhoneNumber())) {
            return new ResponseEntity(
                new ApiResponse(
                    false,
                    Constants.ErrorCode.DEEPSINAL_VERIFY_PHONE_INCORRECT.description,
                    Constants.ErrorCode.DEEPSINAL_VERIFY_PHONE_INCORRECT.code
                ),
                HttpStatus.BAD_REQUEST
            );
        }

        try {
            PhoneCodeDTO codeDTO = IPhoneService.sendSMS(phoneCodeDTO);
            if (codeDTO == null) {
                return new ResponseEntity(
                    new ApiResponse(
                        false,
                        Constants.ErrorCode.DEEPSINAL_VERIFY_SEND_CODE_ERROR.description,
                        Constants.ErrorCode.DEEPSINAL_VERIFY_SEND_CODE_ERROR.code
                    ),
                    HttpStatus.BAD_REQUEST
                );
            }
            return ResponseEntity.ok().body(codeDTO);
        } catch (AccountAlreadyNotExistException e) {
            return new ResponseEntity(
                new ApiResponse(false, Constants.ErrorCode.DEEPSINAL_REGISTER_ACCOUNT_NOT_EXISTED.description),
                HttpStatus.NOT_FOUND
            );
        }
    }

    /**
     * {@code POST  /verify} : Verify phone number.
     *
     * @param phoneCodeDTO the information for verify code
     * @return
     */
    @PostMapping("/verify")
    @Operation(summary = "Verify phone number", tags = { "Phone Management" })
    public ResponseEntity<?> verifyCode(@RequestBody AccountDTO accountDTO) {
        Constants.ErrorCode errorCode = IPhoneService.verifyCode(accountDTO);
        if (errorCode != null) {
            Optional<AdminUserDTO> adminUserDTO = userService.updateFailedAttemptLoginToLockAccount(accountDTO.getLogin());
            return new ResponseEntity(
                new ApiResponse(false, errorCode.description, errorCode.code, adminUserDTO.get().getLoginFailedCount()),
                HttpStatus.BAD_REQUEST
            );
        }
        userService.registerUserByPhone(accountDTO);
        return ResponseEntity.ok().body(null);
    }

    /**
     * check phone number of a country
     * @param countryCode
     * @param phoneNumber
     * @return
     */
    private Boolean isPhoneNumber(String countryCode, String phoneNumber) {
        // For test environment with phone number is: 5571981265131
        if (Constants.PHONE_NUMBER_TEST.equals(phoneNumber)) return true;

        Boolean checkValid = null;
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, countryCode);
            checkValid = phoneNumberUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            checkValid = false;
        }
        return checkValid;
    }

    /**
     * validate format for phone number
     *
     * @param phoneNumber
     * @return
     */
    private Boolean validateFormatPhoneNumber(String phoneNumber) {
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "IT");
            phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            return false;
        }
        return true;
    }
}
