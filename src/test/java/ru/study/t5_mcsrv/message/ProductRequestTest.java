package ru.study.t5_mcsrv.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Date;
import java.util.List;

public class ProductRequestTest {
    private ProductRequest request;

    private enum FieldEnumCP {
        PRODUCT_TYPE("productType"),
        PRODUCT_CODE("productCode"),
        REGISTER_TYPE("registerType"),
        MDM_CODE("mdmCode"),
        CONTRACT_NUMBER("contractNumber"),
        CONTRACT_DATE("contractDate"),
        PRIORITY("priority"),
        BRANCH_CODE("branchCode"),
        ISO_CURRENCY_CODE("isoCurrencyCode"),
        URGENCY_CODE("urgencyCode");

        FieldEnumCP(String name) {
            this.name = name;
        }
        private final String name;
    }

    private enum FieldEnumCA {
        CONTRACT_ID("contractId"),
        INSTANCE_AGREEMENT("instanceAgreement");

        FieldEnumCA(String name) {
            this.name = name;
        }
        private final String name;
    }

    @BeforeEach
    void initEach() {
        request = new ProductRequest();
    }
    @Test
    @DisplayName("Успех - проверка заполненности обязательных полей при создании ЭП")
    public void success_validateCreateProduct() {
        setRequestForCreateProduct();

        Assertions.assertTrue(request.isValidate());
        Assertions.assertNull(request.getFailField());
    }

    @DisplayName("Ошибка - пустое обязательное поле при создании ЭП")
    @ParameterizedTest(name = "поле {arguments}")
    @EnumSource(value = FieldEnumCP.class)
    public void error_nullFieldForCP(FieldEnumCP field) {
        setRequestForCreateProduct();
        resetFieldCP(field);

        Assertions.assertFalse(request.isValidate());
        Assertions.assertEquals(field.name, request.getFailField());
    }

    @Test
    @DisplayName("Успех - проверка заполненности обязательных полей при создании доп соглашения")
    public void success_validateCreateAgreement() {
        setRequestForCreateAgreement();

        Assertions.assertTrue(request.isValidate());
        Assertions.assertNull(request.getFailField());
    }

    @DisplayName("Ошибка - пустое обязательное поле при создании доп соглашения")
    @ParameterizedTest(name = "поле {arguments}")
    @EnumSource(value = FieldEnumCA.class)
    public void error_nullFieldForCA(FieldEnumCA field) {
        setRequestForCreateAgreement();
        resetFieldCA(field);

        Assertions.assertFalse(request.isValidate());
        Assertions.assertEquals(field.name, request.getFailField());
    }

    private void setRequestForCreateProduct() {
        request.setProductType("productType");
        request.setProductCode("productCode");
        request.setRegisterType("registerType");
        request.setMdmCode("mdmCode");
        request.setContractNumber("contactNumber");
        request.setContractDate(new Date());
        request.setPriority(5L);
        request.setBranchCode("branchCode");
        request.setIsoCurrencyCode("ISO");
        request.setUrgencyCode("00");
    }

    private void resetFieldCP(FieldEnumCP field) {
        switch (field) {
            case PRODUCT_CODE -> request.setProductCode(null);
            case PRODUCT_TYPE -> request.setProductType(null);
            case REGISTER_TYPE -> request.setRegisterType(null);
            case MDM_CODE -> request.setMdmCode(null);
            case CONTRACT_NUMBER -> request.setContractNumber(null);
            case CONTRACT_DATE -> request.setContractDate(null);
            case PRIORITY -> request.setPriority(null);
            case BRANCH_CODE -> request.setBranchCode(null);
            case ISO_CURRENCY_CODE -> request.setIsoCurrencyCode(null);
            case URGENCY_CODE -> request.setUrgencyCode(null);
        }
    }

    private void setRequestForCreateAgreement() {
        request.setInstanceId(101L);
        request.setContractId(102L);

        InstanceAgreement agreement = new InstanceAgreement();
        agreement.setNumber("number");
        agreement.setOpeningDate(new Date());
        List<InstanceAgreement> instanceAgreements = List.of(agreement);
        request.setInstanceAgreements(instanceAgreements);
    }

    private void resetFieldCA(FieldEnumCA field) {
        switch (field) {
            case CONTRACT_ID -> request.setContractId(null);
            case INSTANCE_AGREEMENT -> request.setInstanceAgreements(null);
        }
    }
}
