package ru.study.t5_mcsrv.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import ru.study.t5_mcsrv.entity.Product;
import ru.study.t5_mcsrv.entity.ProductClass;
import ru.study.t5_mcsrv.entity.ProductRegisterType;
import ru.study.t5_mcsrv.mapping.ProductRequestMap;
import ru.study.t5_mcsrv.mapping.ProductRequestToProductMapper;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.message.ProductResponse;
import ru.study.t5_mcsrv.repo.ProductClassRepository;
import ru.study.t5_mcsrv.repo.ProductRegisterTypeRepository;
import ru.study.t5_mcsrv.repo.ProductRepository;

import java.util.Collections;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {
    private final ProductService productService;
    private final ProductRepository productRepoMock;
    private final ProductClassRepository productClassRepoMock;
    private final ProductRegisterTypeRepository productRegisterTypeRepoMock;
    private final ProductRequestToProductMapper productRequestToProductMapperMock;
    private final ProductRegisterService productRegisterServiceMock;

    private static final String CLIENT_ACCOUNT_TYPE = "Клиентский";

    public ProductServiceTest() {
        productService = new ProductService();

        productRepoMock = Mockito.mock(ProductRepository.class);
        productClassRepoMock = Mockito.mock(ProductClassRepository.class);
        productRegisterTypeRepoMock = Mockito.mock(ProductRegisterTypeRepository.class);
        productRequestToProductMapperMock = Mockito.mock(ProductRequestToProductMapper.class);
        productRegisterServiceMock = Mockito.mock(ProductRegisterService.class);

        productService.setProductRepo(productRepoMock);
        productService.setProductClassRepo(productClassRepoMock);
        productService.setProductRegisterTypeRepo(productRegisterTypeRepoMock);
        productService.setProductRequestToProductMapper(productRequestToProductMapperMock);
        productService.setProductRegisterService(productRegisterServiceMock);
    }

    @BeforeEach
    void initEach() {
        Mockito.reset(
                productRepoMock,
                productClassRepoMock,
                productRegisterTypeRepoMock,
                productRequestToProductMapperMock
        );
    }

    @Test
    @DisplayName("Ошибка - проверка отсутствия запроса")
    public void error_validateNullRequest() {
        final ProductResponse response = productService.validateRequest(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("Ошибка - проверка заполненности обязательных полей запроса")
    public void error_validateEmptyRequest() {
        final ProductResponse response = productService.validateRequest(new ProductRequest());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("Успех - проверка корректного запроса")
    public void success_validateCorrectRequest() {
        final ProductRequest request = new ProductRequest();
        request.setProductType("productType");
        request.setProductCode("productCode");
        request.setRegisterType("registerType");
        request.setMdmCode("mdmCode");
        request.setContractNumber("contractNumber");
        request.setContractDate(new Date());
        request.setPriority(1L);
        request.setBranchCode("branchCode");
        request.setIsoCurrencyCode("iso");
        request.setUrgencyCode("00");

        Assertions.assertNull(productService.validateRequest(request));
    }

    @Test
    @DisplayName("Ошибка - при создании ЭП обнаружены дубли")
    public void error_createProductExistsDublicate() {
        final ProductRequest request = new ProductRequest();
        request.setContractNumber("CN001");

        final Product product = new Product();
        product.setId(333L);

        Mockito.when(productRepoMock.findProductsByNumber(request.getContractNumber()))
                .thenReturn(Collections.singletonList(product));

        final ProductResponse response = productService.createProduct(request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    @DisplayName("Ошибка - при создании ЭП не найден КодПродукта")
    public void error_createProductNotFoundProductClass() {
        final ProductRequest request = new ProductRequest();
        request.setContractNumber("CN001");
        request.setProductCode("productCode");

        Mockito.when(productRepoMock.findProductsByNumber(request.getContractNumber()))
                .thenReturn(Collections.emptyList());
        Mockito.when(productClassRepoMock.findProductClassByValue(request.getProductCode())).thenReturn(null);

        final ProductResponse response = productService.createProduct(request);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    @DisplayName("Ошибка - при создании ЭП не найден Список ТипРегистра")
    public void error_createProductNotFoundRegisterTypeList() {
        final ProductRequest request = new ProductRequest();
        request.setContractNumber("CN001");
        request.setProductCode("productCode");

        final ProductClass productClass = new ProductClass();
        productClass.setValue("pc_code");

        Mockito.when(productRepoMock.findProductsByNumber(request.getContractNumber()))
                .thenReturn(Collections.emptyList());
        Mockito.when(productClassRepoMock.findProductClassByValue(request.getProductCode())).thenReturn(productClass);
        Mockito.when(productRegisterTypeRepoMock
                        .findProductRegisterTypesByProductClassCodeAndAccountType(request.getProductCode(), CLIENT_ACCOUNT_TYPE))
                .thenReturn(Collections.emptyList());

        final ProductResponse response = productService.createProduct(request);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    @DisplayName("Успех - создание ЭП")
    public void success_createProduct() {
        final ProductRequest request = new ProductRequest();
        request.setContractNumber("CN001");
        request.setProductCode("productCode");

        final ProductClass productClass = new ProductClass();
        productClass.setValue("pc_code");

        final Product product = new Product();
        product.setId(900L);

        Mockito.when(productRepoMock.findProductsByNumber(request.getContractNumber()))
                .thenReturn(Collections.emptyList());
        Mockito.when(productClassRepoMock.findProductClassByValue(request.getProductCode())).thenReturn(productClass);
        Mockito.when(productRegisterTypeRepoMock
                        .findProductRegisterTypesByProductClassCodeAndAccountType(request.getProductCode(), CLIENT_ACCOUNT_TYPE))
                .thenReturn(Collections.singletonList(new ProductRegisterType()));
        Mockito.when(productRequestToProductMapperMock.map(Mockito.any(ProductRequestMap.class)))
                .thenReturn(product);
        Mockito.when(productRepoMock.save(product)).thenReturn(product);
        Mockito.when(productRegisterServiceMock.createProductRegisters(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        final ProductResponse response = productService.createProduct(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }
}
