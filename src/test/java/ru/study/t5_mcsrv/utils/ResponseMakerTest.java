package ru.study.t5_mcsrv.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import ru.study.t5_mcsrv.message.ProductResponse;

public class ResponseMakerTest {
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
            case BAD_REQUEST -> ResponseMaker.getBadResponse(new ProductResponse(), INSTANCE_ID);
            case OK -> ResponseMaker.getOkResponse(new ProductResponse(), INSTANCE_ID);
            case INTERNAL_SERVER_ERROR -> ResponseMaker.getInternalErrorResponse(new ProductResponse(), INSTANCE_ID);
            case NOT_FOUND -> ResponseMaker.getNotFoundResponse(new ProductResponse(), INSTANCE_ID);
            default -> null;
        };
    }
}
