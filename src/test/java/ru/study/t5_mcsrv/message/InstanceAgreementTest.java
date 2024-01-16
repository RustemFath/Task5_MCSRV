package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Date;

public class InstanceAgreementTest {
    private InstanceAgreement instanceAgreement;

    private enum FieldEnum {
        NUMBER("number"), OPENING_DATE("openingDate");

        FieldEnum(String name) {
            this.name = name;
        }

        private final String name;
    }

    @BeforeEach
    void initEach() {
        instanceAgreement = new InstanceAgreement();
        setRequest();
    }

    @Test
    @DisplayName("Успех - проверка заполненности обязательных полей")
    public void success_validate() {
        Assertions.assertTrue(instanceAgreement.isValidate());
        Assertions.assertNull(instanceAgreement.getFailField());
    }

    @DisplayName("Ошибка - пустое обязательное поле")
    @ParameterizedTest(name = "поле {arguments}")
    @EnumSource(value = FieldEnum.class)
    public void error_nullField(FieldEnum field) {
        resetField(field);

        Assertions.assertFalse(instanceAgreement.isValidate());
        Assertions.assertEquals(field.name, instanceAgreement.getFailField());
    }

    private void setRequest() {
        instanceAgreement.setNumber("10000");
        instanceAgreement.setOpeningDate(new Date());
    }

    private void resetField(FieldEnum field) {
        switch (field) {
            case NUMBER -> instanceAgreement.setNumber(null);
            case OPENING_DATE -> instanceAgreement.setOpeningDate(null);
        }
    }
}
