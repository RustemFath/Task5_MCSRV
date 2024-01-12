package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

public class ProductRegisterResponseTest {
    private static final String ACCOUNT_ID = "account_id";

    @DisplayName("Успешное создание ответов")
    @ParameterizedTest(name = "Создание {arguments}")
    @EnumSource(value = HttpStatus.class, mode = EnumSource.Mode.INCLUDE,
            names = {"BAD_REQUEST", "OK", "INTERNAL_SERVER_ERROR", "NOT_FOUND"})
    public void success_createResponse(HttpStatus status) {
        ProductRegisterResponse response = getResponse(status);

        Assertions.assertEquals(status, response.getStatus());
        Assertions.assertEquals(ACCOUNT_ID, response.getAccountId());
    }

    private ProductRegisterResponse getResponse(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> ProductRegisterResponse.getBadResponse(ACCOUNT_ID);
            case OK -> ProductRegisterResponse.getOkResponse(ACCOUNT_ID);
            case INTERNAL_SERVER_ERROR -> ProductRegisterResponse.getInternalErrorResponse(ACCOUNT_ID);
            case NOT_FOUND -> ProductRegisterResponse.getNotFoundResponse(ACCOUNT_ID);
            default -> null;
        };
    }
}
