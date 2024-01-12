package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

public class ProductResponseTest {
    private static final String INSTANCE_ID = "instance_id";

    @DisplayName("Успешное создание ответов")
    @ParameterizedTest(name = "Создание {arguments}")
    @EnumSource(value = HttpStatus.class, mode = EnumSource.Mode.INCLUDE,
            names = {"BAD_REQUEST", "OK", "INTERNAL_SERVER_ERROR", "NOT_FOUND"})
    public void success_createResponse(HttpStatus status) {
        ProductResponse response = getResponse(status);

        Assertions.assertEquals(status, response.getStatus());
        Assertions.assertEquals(INSTANCE_ID, response.getInstanceId());
    }

    private ProductResponse getResponse(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> ProductResponse.getBadResponse(INSTANCE_ID);
            case OK -> ProductResponse.getOkResponse(INSTANCE_ID);
            case INTERNAL_SERVER_ERROR -> ProductResponse.getInternalErrorResponse(INSTANCE_ID);
            case NOT_FOUND -> ProductResponse.getNotFoundResponse(INSTANCE_ID);
            default -> null;
        };
    }
}
