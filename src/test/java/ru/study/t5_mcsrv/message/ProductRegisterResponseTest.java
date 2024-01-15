package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductRegisterResponseTest {
    private final static String ACCOUNT_ID = "account_id";
    @Test
    @DisplayName("Успех - метод setTextResponse")
    public void success_setTextResponse() {
        ProductRegisterResponse response = new ProductRegisterResponse();
        response.setTextResponse(ACCOUNT_ID);
        Assertions.assertEquals(ACCOUNT_ID, response.getAccountId());
    }
}
