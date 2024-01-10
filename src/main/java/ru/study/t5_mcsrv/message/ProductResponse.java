package ru.study.t5_mcsrv.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Ответ на запрос создания экземпляра Продукта
 */
@Data
public class ProductResponse {
    @JsonIgnore
    private HttpStatus status;

    /** Идентификатор экземпляра продукта */
    private String instanceId;
    /** Массив идентификаторов продуктовых регистров */
    private List<String> registerId;
    /** Массив доп. соглашений */
    private List<String> supplementaryAgreementId;

    public static ProductResponse getInternalErrorResponse(String errorText) {
        return getStatusResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorText);
    }

    public static ProductResponse getBadResponse(String errorText) {
        return getStatusResponse(HttpStatus.BAD_REQUEST, errorText);
    }

    public static ProductResponse getNotFoundResponse(String errorText) {
        return getStatusResponse(HttpStatus.NOT_FOUND, errorText);
    }

    public static ProductResponse getOkResponse(String instanceId) {
        return getStatusResponse(HttpStatus.OK, instanceId);
    }

    private static ProductResponse getStatusResponse(HttpStatus status, String instanceId) {
        ProductResponse response = new ProductResponse();
        response.setInstanceId(instanceId);
        response.setStatus(status);
        return response;
    }
}