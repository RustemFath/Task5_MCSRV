package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductRegisterResponseTest {
    private final static String REGISTER_ID = "account_id";
    @Test
    @DisplayName("Успех - метод setTextResponse")
    public void success_setTextResponse() {
        ProductRegisterResponse response = new ProductRegisterResponse();
        response.setTextResponse(REGISTER_ID);
        Assertions.assertEquals(REGISTER_ID, response.getRegisterId());
    }
}
