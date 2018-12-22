package com.apifuze.cockpit.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ResourceAuthorizationException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ResourceAuthorizationException() {
        super(ErrorConstants.INVALID_AUTH, "Resource Authorization Exaction", Status.BAD_REQUEST);
    }
}
