package com.saltlux.deepsignal.web.api.validator;

import com.twilio.exception.ApiException;
import com.twilio.rest.lookups.v1.PhoneNumber;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, Object> {

    private String countryCode;
    private String phoneNumber;

    public void initialize(ValidPhoneNumber constraintAnnotation) {
        this.countryCode = constraintAnnotation.countryCode();
        this.phoneNumber = constraintAnnotation.phoneNumber();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String countryCodeValue = new BeanWrapperImpl(value).getPropertyValue(countryCode).toString();
        String phoneNumberValue = new BeanWrapperImpl(value).getPropertyValue(phoneNumber).toString();
        return validate(countryCodeValue, phoneNumberValue);
    }

    public boolean validate(String countryCode, String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[\\s()-]", "");
        try {
            PhoneNumber.fetcher(new com.twilio.type.PhoneNumber(phoneNumber)).setCountryCode(countryCode).fetch();
        } catch (ApiException e) {
            return false;
        }
        return true;
    }
}
