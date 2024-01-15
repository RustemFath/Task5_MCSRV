package ru.study.t5_mcsrv.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.study.t5_mcsrv.message.InstanceAgreement;

import java.util.Date;

public class RequestValidatorTest {
    private final RequestValidator validator = new InstanceAgreement();

    @Test
    @DisplayName("Проверка значения типа String на null")
    public void success_nullStringValue() {
        String str = null;
        Assertions.assertTrue(RequestValidator.isFailField(str, () -> validator.setFailField("string_null")));
        Assertions.assertEquals("string_null", validator.getFailField());
    }

    @Test
    @DisplayName("Проверка значения типа Date на null")
    public void success_nullDateValue() {
        Date date = null;
        Assertions.assertTrue(RequestValidator.isFailField(date, () -> validator.setFailField("date_null")));
        Assertions.assertEquals("date_null", validator.getFailField());
    }

    @Test
    @DisplayName("Проверка значения типа Long на null")
    public void success_nullLongValue() {
        Long l = null;
        Assertions.assertTrue(RequestValidator.isFailField(l, () -> validator.setFailField("long_null")));
        Assertions.assertEquals("long_null", validator.getFailField());
    }

    @Test
    @DisplayName("Проверка значения типа String на пустоту")
    public void success_emptyStringValue() {
        String str = "";
        Assertions.assertTrue(RequestValidator.isFailField(str, () -> validator.setFailField("string_empty")));
        Assertions.assertEquals("string_empty", validator.getFailField());
    }

    @Test
    @DisplayName("Проверка заполненного значения типа String")
    public void success_notNullStringValue() {
        String str = "0";
        Assertions.assertFalse(RequestValidator.isFailField(str, () -> validator.setFailField("string_val")));
        Assertions.assertNotEquals("string_val", validator.getFailField());
    }

    @Test
    @DisplayName("Проверка заполненного значения типа Date")
    public void success_notNullDateValue() {
        Date date = new Date();
        Assertions.assertFalse(RequestValidator.isFailField(date, () -> validator.setFailField("date_val")));
        Assertions.assertNotEquals("date_val", validator.getFailField());
    }

    @Test
    @DisplayName("Проверка заполненного значения типа Long")
    public void success_notNullLongValue() {
        Long l = 1L;
        Assertions.assertFalse(RequestValidator.isFailField(l, () -> validator.setFailField("long_val")));
        Assertions.assertNotEquals("long_val", validator.getFailField());
    }
}
