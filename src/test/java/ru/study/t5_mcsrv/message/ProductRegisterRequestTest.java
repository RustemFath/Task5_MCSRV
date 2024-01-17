package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class ProductRegisterRequestTest {
    private ProductRegisterRequest request;

    private enum FieldEnum {
        INSTANCE_ID("instanceId"),
        REGISTER_TYPE_CODE("registerTypeCode"),
        ACCOUNT_TYPE("accountType"),
        CURRENCY_CODE("currencyCode"),
        PRIORITY_CODE("priorityCode");

        FieldEnum(String name) {
            this.name = name;
        }

        private final String name;
    }

    @BeforeEach
    void initEach() {
        request = new ProductRegisterRequest();
        setRequest();
    }

    @Test
    @DisplayName("Успех - проверка заполненности обязательных полей")
    public void success_validate() {
        Assertions.assertTrue(request.isValidate());
        Assertions.assertNull(request.getFailField());
    }

    @DisplayName("Ошибка - пустое обязательное поле")
    @ParameterizedTest(name = "поле {arguments}")
    @EnumSource(value = FieldEnum.class)
    public void error_nullField(FieldEnum field) {
        resetField(field);

        Assertions.assertFalse(request.isValidate());
        Assertions.assertEquals(field.name, request.getFailField());
    }

    private void setRequest() {
        request.setInstanceId(10000L);
        request.setRegisterTypeCode("reg_type_code");
        request.setAccountType("account_type");
        request.setCurrencyCode("iso");
        request.setPriorityCode("00");
    }

    private void resetField(FieldEnum field) {
        switch (field) {
            case INSTANCE_ID -> request.setInstanceId(null);
            case REGISTER_TYPE_CODE -> request.setRegisterTypeCode(null);
            case ACCOUNT_TYPE -> request.setAccountType(null);
            case CURRENCY_CODE -> request.setCurrencyCode(null);
            case PRIORITY_CODE -> request.setPriorityCode(null);
        }
    }
}
