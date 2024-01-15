package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductResponseTest {
    private final static String INSTANCE_ID = "instance_id";
    @Test
    @DisplayName("Успех - метод setTextResponse")
    public void success_setTextResponse() {
        ProductResponse response = new ProductResponse();
        response.setTextResponse(INSTANCE_ID);
        Assertions.assertEquals(INSTANCE_ID, response.getInstanceId());
    }
}
