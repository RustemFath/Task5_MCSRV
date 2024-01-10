package ru.study.t5_mcsrv.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Ответ на запрос создания Продуктового регистра
 */
@Data
public class ProductRegisterResponse {
    @JsonIgnore
    private HttpStatus status;

    /** Идентификатор продуктового регистра */
    private String accountId;

    public static ProductRegisterResponse getInternalErrorResponse(String errorText) {
        return getStatusResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorText);
    }

    public static ProductRegisterResponse getBadResponse(String errorText) {
        return getStatusResponse(HttpStatus.BAD_REQUEST, errorText);
    }

    public static ProductRegisterResponse getNotFoundResponse(String errorText) {
        return getStatusResponse(HttpStatus.NOT_FOUND, errorText);
    }

    public static ProductRegisterResponse getOkResponse(String accountId) {
        return getStatusResponse(HttpStatus.OK, accountId);
    }

    private static ProductRegisterResponse getStatusResponse(HttpStatus status, String accountId) {
        ProductRegisterResponse response = new ProductRegisterResponse();
        response.accountId = accountId;
        response.status = status;
        return response;
    }
}
