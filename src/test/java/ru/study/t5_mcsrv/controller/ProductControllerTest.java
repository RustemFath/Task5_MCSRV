package ru.study.t5_mcsrv.controller;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.message.ProductResponse;
import ru.study.t5_mcsrv.utils.ResponseMaker;
import ru.study.t5_mcsrv.service.AgreementService;
import ru.study.t5_mcsrv.service.ProductService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerTest {

    private final ProductController productController;
    private final ProductService productServiceMock;
    private final AgreementService agreementServiceMock;

    public ProductControllerTest() {
        productController = new ProductController();

        productServiceMock = Mockito.mock(ProductService.class);
        agreementServiceMock = Mockito.mock(AgreementService.class);

        productController.setProductService(productServiceMock);
        productController.setAgreementService(agreementServiceMock);
    }

    @BeforeEach
    void initEach() {
        Mockito.reset(productServiceMock, agreementServiceMock);
    }

    @Test
    @DisplayName("Ошибка - получение пустого запроса на создание продукта")
    public void error_emptyRequest() {
        final ProductRequest request = new ProductRequest();

        Mockito.when(productServiceMock.validateRequest(request))
                .thenReturn(ResponseMaker.getBadResponse(new ProductResponse(), "BAD"));

        final ResponseEntity<ProductResponse> response = productController.postRequest(request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Ошибка - получение null запроса на создание продукта")
    public void error_nullRequest() {
        Mockito.when(productServiceMock.validateRequest(null))
                .thenReturn(ResponseMaker.getBadResponse(new ProductResponse(), "BAD"));

        final ResponseEntity<ProductResponse> response = productController.postRequest(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Успех - создание нового продукта")
    public void success_createProduct() {
        final ProductRequest request = new ProductRequest();

        Mockito.when(productServiceMock.validateRequest(request)).thenReturn(null);
        Mockito.when(productServiceMock.createProduct(request))
                .thenReturn(ResponseMaker.getOkResponse(new ProductResponse(), "1"));

        final ResponseEntity<ProductResponse> response = productController.postRequest(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Успех - создание нового доп соглашения")
    public void success_createAgreement() {
        final ProductRequest request = new ProductRequest();
        request.setInstanceId(1L);

        Mockito.when(productServiceMock.validateRequest(request)).thenReturn(null);
        Mockito.when(agreementServiceMock.createAgreement(request))
                .thenReturn(ResponseMaker.getOkResponse(new ProductResponse(), "1"));

        final ResponseEntity<ProductResponse> response = productController.postRequest(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Ошибка - возникновение исключения")
    public void error_generateThrow() {
        Mockito.when(productServiceMock.validateRequest(null)).thenThrow(IllegalArgumentException.class);

        final ResponseEntity<ProductResponse> response = productController.postRequest(null);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
