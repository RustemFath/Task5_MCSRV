package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class InstanceAgreementTest {
    private InstanceAgreement instanceAgreement;

    @BeforeEach
    void initEach() {
        instanceAgreement = new InstanceAgreement();
    }
    @Test
    @DisplayName("Успех - проверка заполненности обязательных полей")
    public void success_validate() {
        instanceAgreement.setNumber("10000");
        instanceAgreement.setOpeningDate(new Date());

        Assertions.assertTrue(instanceAgreement.isValidate());
        Assertions.assertNull(instanceAgreement.getFailField());
    }

    @Test
    @DisplayName("Ошибка - пустое обязательное поле Number")
    public void error_emptyNumber() {
        instanceAgreement.setOpeningDate(new Date());

        Assertions.assertFalse(instanceAgreement.isValidate());
        Assertions.assertEquals(instanceAgreement.getFailField(), "number");
    }

    @Test
    @DisplayName("Ошибка - пустое обязательное поле OpeningDate")
    public void error_emptyOpeningDate() {
        instanceAgreement.setNumber("100");

        Assertions.assertFalse(instanceAgreement.isValidate());
        Assertions.assertEquals(instanceAgreement.getFailField(), "openingDate");
    }
}
