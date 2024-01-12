package ru.study.t5_mcsrv.utils;

import org.springframework.http.HttpStatus;
import ru.study.t5_mcsrv.message.StatusResponse;

public class ResponseMaker {
    public static <T extends StatusResponse> T getInternalErrorResponse(T response, String errorText) {
        return getStatusResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, errorText);
    }

    public static <T extends StatusResponse> T getBadResponse(T response, String errorText) {
        return getStatusResponse(HttpStatus.BAD_REQUEST, response, errorText);
    }

    public static <T extends StatusResponse> T getNotFoundResponse(T response, String errorText) {
        return getStatusResponse(HttpStatus.NOT_FOUND, response, errorText);
    }

    public static  <T extends StatusResponse> T getOkResponse(T response, String instanceId) {
        return getStatusResponse(HttpStatus.OK, response, instanceId);
    }

    private static <T extends StatusResponse> T getStatusResponse(HttpStatus status, T response, String text) {
        response.setTextResponse(text);
        response.setStatus(status);
        return response;
    }
}
