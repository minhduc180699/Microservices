package com.saltlux.deepsignal.web.api.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@Getter
@Setter
public class BadRequestException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;
    private final String errorKey;

    public BadRequestException(URI type, String errorKey) {
        super(type, null, Status.BAD_REQUEST, null, null, null, getAlertParameters(errorKey));
        this.errorKey = errorKey;
    }

    private static Map<String, Object> getAlertParameters(String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", errorKey);
        return parameters;
    }

    public BadRequestException(URI type, String errorKey, Status status) {
        super(type, null, status, null, null, null, getAlertParameters(errorKey));
        this.errorKey = errorKey;
    }

    public BadRequestException(String errorKey) {
        //        super(null, null, Status.BAD_REQUEST, null, null, null, getAlertParameters(errorKey));
        this.errorKey = errorKey;
    }
}
