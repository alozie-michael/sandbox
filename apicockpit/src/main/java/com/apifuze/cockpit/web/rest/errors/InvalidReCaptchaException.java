package com.apifuze.cockpit.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidReCaptchaException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidReCaptchaException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect re-captcha response", Status.BAD_REQUEST);
    }
}
