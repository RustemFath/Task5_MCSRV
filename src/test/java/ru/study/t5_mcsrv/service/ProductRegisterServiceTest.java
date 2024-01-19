package ru.study.t5_mcsrv.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import ru.study.t5_mcsrv.entity.*;
import ru.study.t5_mcsrv.mapping.ProductRegRequestToProductRegMapper;
import ru.study.t5_mcsrv.message.ProductRegisterRequest;
import ru.study.t5_mcsrv.message.ProductRegisterResponse;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.repo.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRegisterServiceTest {
    private final ProductRegisterService productRegisterService;
    private final ProductRegisterRepository productRegisterRepoMock;
    private final ProductRepository productRepoMock;
    private final ProductClassRepository productClassRepoMock;
    private final ProductRegisterTypeRepository productRegisterTypeRepoMock;
    private final AccountPoolRepository accountPoolRepoMock;
    private final ProductRegRequestToProductRegMapper productRegRequestToProductRegMapperMock;
    private final AccountNumberRepository accountNumberRepoMock;

    public ProductRegisterServiceTest() {
        productRegisterService = new ProductRegisterService();

        productRegisterRepoMock = Mockito.mock(ProductRegisterRepository.class);
        productRepoMock = Mockito.mock(ProductRepository.class);
        productClassRepoMock = Mockito.mock(ProductClassRepository.class);
        productRegisterTypeRepoMock = Mockito.mock(ProductRegisterTypeRepository.class);
        accountPoolRepoMock = Mockito.mock(AccountPoolRepository.class);
        productRegRequestToProductRegMapperMock = Mockito.mock(ProductRegRequestToProductRegMapper.class);
        accountNumberRepoMock = Mockito.mock(AccountNumberRepository.class);

        productRegisterService.setProductRegisterRepo(productRegisterRepoMock);
        productRegisterService.setProductRepo(productRepoMock);
        productRegisterService.setProductClassRepo(productClassRepoMock);
        productRegisterService.setProductRegisterTypeRepo(productRegisterTypeRepoMock);
        productRegisterService.setAccountPoolRepo(accountPoolRepoMock);
        productRegisterService.setProductRegRequestToProductRegMapper(productRegRequestToProductRegMapperMock);
        productRegisterService.setAccountNumberRepo(accountNumberRepoMock);
    }

    @BeforeEach
    void initEach() {
        Mockito.reset(
                productRegisterRepoMock,
                productRepoMock,
                productClassRepoMock,
                productRegisterTypeRepoMock,
                accountPoolRepoMock,
                productRegRequestToProductRegMapperMock,
                accountNumberRepoMock
        );
    }

    @Test
    @DisplayName("Ошибка - проверка отсутствия запроса")
    public void error_validateNullRequest() {
        final ProductRegisterResponse response = productRegisterService.validateRequest(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("Ошибка - проверка заполненности обязательных полей запроса")
    public void error_validateEmptyRequest() {
        ProductRegisterRequest request = new ProductRegisterRequest();

        final ProductRegisterResponse response = productRegisterService.validateRequest(request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("Успех - проверка корректного запроса")
    public void success_validateCorrectRequest() {
        ProductRegisterRequest request = new ProductRegisterRequest();
        request.setInstanceId(1L);
        request.setRegisterTypeCode("reg_type_code");
        request.setAccountType("account_type");
        request.setCurrencyCode("iso");
        request.setPriorityCode("00");

        Assertions.assertNull(productRegisterService.validateRequest(request));
    }

    @Test
    @DisplayName("Ошибка - при создании ПР обнаружены дубли")
    public void error_createPRExistsDublicate() {
        ProductRegisterRequest request = new ProductRegisterRequest();
        request.setInstanceId(1L);
        request.setRegisterTypeCode("reg_type_code");
        request.setAccountType("account_type");
        request.setCurrencyCode("iso");
        request.setPriorityCode("00");

        ProductRegister productRegister = new ProductRegister();
        productRegister.setProductId(100L);

        Mockito.when(productRegisterRepoMock
                        .findProductRegistersByProductIdAndType(request.getInstanceId(), request.getRegisterTypeCode()))
                .thenReturn(Collections.singletonList(productRegister));

        final ProductRegisterResponse response = productRegisterService.createProductRegister(request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("Ошибка - при создании ПР не найден ЭП")
    public void error_createPRNotFoundProduct() {
        ProductRegisterRequest request = new ProductRegisterRequest();
        request.setInstanceId(1L);
        request.setRegisterTypeCode("reg_type_code");
        request.setAccountType("account_type");
        request.setCurrencyCode("iso");
        request.setPriorityCode("00");

        Mockito.when(productRegisterRepoMock
                        .findProductRegistersByProductIdAndType(request.getInstanceId(), request.getRegisterTypeCode()))
                .thenReturn(Collections.emptyList());
        Mockito.when(productRepoMock.findById(request.getInstanceId())).thenReturn(Optional.empty());

        final ProductRegisterResponse response = productRegisterService.createProductRegister(request);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    @DisplayName("Ошибка - при создании ПР не найден КодПродукта")
    public void error_createPRNotFoundProductClass() {
        ProductRegisterRequest request = new ProductRegisterRequest();
        request.setInstanceId(1L);
        request.setRegisterTypeCode("reg_type_code");
        request.setAccountType("account_type");
        request.setCurrencyCode("iso");
        request.setPriorityCode("00");

        Product product = new Product();
        product.setProductCodeId(55L);

        Mockito.when(productRegisterRepoMock
                        .findProductRegistersByProductIdAndType(request.getInstanceId(), request.getRegisterTypeCode()))
                .thenReturn(Collections.emptyList());
        Mockito.when(productRepoMock.findById(request.getInstanceId())).thenReturn(Optional.of(product));
        Mockito.when(productClassRepoMock.findProductClassByInternalId(product.getProductCodeId()))
                .thenReturn(null);

        final ProductRegisterResponse response = productRegisterService.createProductRegister(request);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    @DisplayName("Ошибка - при создании ПР не найден Список ТипРегистра")
    public void error_createPRNotFoundRegisterTypeList() {
        ProductRegisterRequest request = new ProductRegisterRequest();
        request.setInstanceId(1L);
        request.setRegisterTypeCode("reg_type_code");
        request.setAccountType("account_type");
        request.setCurrencyCode("iso");
        request.setPriorityCode("00");

        Product product = new Product();
        product.setProductCodeId(55L);

        ProductClass productClass = new ProductClass();
        productClass.setValue("pc_code");

        Mockito.when(productRegisterRepoMock
                        .findProductRegistersByProductIdAndType(request.getInstanceId(), request.getRegisterTypeCode()))
                .thenReturn(Collections.emptyList());
        Mockito.when(productRepoMock.findById(request.getInstanceId())).thenReturn(Optional.of(product));
        Mockito.when(productClassRepoMock.findProductClassByInternalId(product.getProductCodeId()))
                .thenReturn(productClass);
        Mockito.when(productRegisterTypeRepoMock.findProductRegisterTypesByProductClassCode(productClass.getValue()))
                .thenReturn(Collections.emptyList());

        final ProductRegisterResponse response = productRegisterService.createProductRegister(request);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    @DisplayName("Ошибка - при создании ПР не найден КодПродукта для ТипРегистра")
    public void error_createPRNotFoundRegisterTypeCodeInRegisterTypeList() {
        ProductRegisterRequest request = new ProductRegisterRequest();
        request.setInstanceId(1L);
        request.setRegisterTypeCode("reg_type_code");
        request.setAccountType("account_type");
        request.setCurrencyCode("iso");
        request.setPriorityCode("00");

        Product product = new Product();
        product.setProductCodeId(55L);

        ProductClass productClass = new ProductClass();
        productClass.setValue("pc_code");

        ProductRegisterType productRegisterType = new ProductRegisterType();
        productRegisterType.setValue("rt_code");

        Mockito.when(productRegisterRepoMock
                        .findProductRegistersByProductIdAndType(request.getInstanceId(), request.getRegisterTypeCode()))
                .thenReturn(Collections.emptyList());
        Mockito.when(productRepoMock.findById(request.getInstanceId())).thenReturn(Optional.of(product));
        Mockito.when(productClassRepoMock.findProductClassByInternalId(product.getProductCodeId()))
                .thenReturn(productClass);
        Mockito.when(productRegisterTypeRepoMock.findProductRegisterTypesByProductClassCode(productClass.getValue()))
                .thenReturn(Collections.singletonList(productRegisterType));

        final ProductRegisterResponse response = productRegisterService.createProductRegister(request);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    @DisplayName("Успех - создание ПР")
    public void success_createPR() {
        ProductRegisterRequest request = new ProductRegisterRequest();
        request.setInstanceId(1L);
        request.setRegisterTypeCode("reg_type_code");
        request.setAccountType("account_type");
        request.setCurrencyCode("iso");
        request.setPriorityCode("00");

        Product product = new Product();
        product.setProductCodeId(55L);

        ProductClass productClass = new ProductClass();
        productClass.setValue("pc_code");

        ProductRegisterType productRegisterType = new ProductRegisterType();
        productRegisterType.setValue("reg_type_code");

        ProductRegister productRegister = new ProductRegister();
        productRegister.setId(777L);

        Mockito.when(productRegisterRepoMock
                        .findProductRegistersByProductIdAndType(request.getInstanceId(), request.getRegisterTypeCode()))
                .thenReturn(Collections.emptyList());
        Mockito.when(productRepoMock.findById(request.getInstanceId())).thenReturn(Optional.of(product));
        Mockito.when(productClassRepoMock.findProductClassByInternalId(product.getProductCodeId()))
                .thenReturn(productClass);
        Mockito.when(productRegisterTypeRepoMock.findProductRegisterTypesByProductClassCode(productClass.getValue()))
                .thenReturn(Collections.singletonList(productRegisterType));
        Mockito.when(accountPoolRepoMock.findAccountPoolByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
                        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()
                ))
                .thenReturn(null);
        Mockito.when(productRegRequestToProductRegMapperMock.map(Mockito.any())).thenReturn(productRegister);
        Mockito.when(productRegisterRepoMock.save(productRegister)).thenReturn(productRegister);

        final ProductRegisterResponse response = productRegisterService.createProductRegister(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    @DisplayName("Успех - создание массива ПР")
    public void success_createPRArray() {
        final List<ProductRegisterType> types = Collections.singletonList(new ProductRegisterType());
        final ProductRequest request = new ProductRequest();
        request.setBranchCode("branchCode");
        request.setIsoCurrencyCode("iso");
        request.setMdmCode("mdmCode");
        request.setUrgencyCode("urgencyCode");
        request.setRegisterType("regType");

        final ProductRegister productRegister = new ProductRegister();
        productRegister.setId(123L);

        final AccountPool accountPool = new AccountPool();
        accountPool.setId(56L);
        final AccountNumber accountNumber = new AccountNumber();
        accountNumber.setId(99L);

        Mockito.when(accountPoolRepoMock.findAccountPoolByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
                        request.getBranchCode(),
                        request.getIsoCurrencyCode(),
                        request.getMdmCode(),
                        request.getUrgencyCode(),
                        request.getRegisterType()
                ))
                .thenReturn(accountPool);
        Mockito.when(accountNumberRepoMock.findAccountNumbersByAccountPoolIdOrderById(accountPool.getId()))
                        .thenReturn(Collections.singletonList(accountNumber));
        Mockito.when(productRegisterRepoMock.save(Mockito.any(ProductRegister.class))).thenReturn(productRegister);

        final List<Long> responseList = productRegisterService.createProductRegisters(types, request);
        Assertions.assertEquals(1, responseList.size());
        Assertions.assertEquals(123, responseList.get(0));
    }

}
