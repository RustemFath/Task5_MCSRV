package ru.study.t5_mcsrv;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.study.t5_mcsrv.controller.ProductRegisterController;
import ru.study.t5_mcsrv.message.ProductRegisterRequest;
import ru.study.t5_mcsrv.message.ProductRegisterResponse;
import ru.study.t5_mcsrv.service.ProductRegisterService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRegisterControllerTest {
    private final ProductRegisterController productRegisterController;
    private final ProductRegisterService productRegisterServiceMock;

    public ProductRegisterControllerTest() {
        productRegisterController = new ProductRegisterController();
        productRegisterServiceMock = Mockito.mock(ProductRegisterService.class);
        productRegisterController.setProductRegisterService(productRegisterServiceMock);
    }

    @BeforeEach
    void initEach() {
        Mockito.reset(productRegisterServiceMock);
    }

    @Test
    @DisplayName("Ошибка - получение пустого запроса на создание продуктового регистра")
    public void error_emptyRequest() {
        final ProductRegisterRequest request = new ProductRegisterRequest();

        Mockito.when(productRegisterServiceMock.validateRequest(request))
                .thenReturn(ProductRegisterResponse.getBadResponse("BAD"));

        final ResponseEntity<ProductRegisterResponse> response = productRegisterController.postRequest(request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Ошибка - получение null запроса на создание продуктового регистра")
    public void error_nullRequest() {
        Mockito.when(productRegisterServiceMock.validateRequest(null))
                .thenReturn(ProductRegisterResponse.getBadResponse("BAD"));

        final ResponseEntity<ProductRegisterResponse> response = productRegisterController.postRequest(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Успех - создание нового продуктового регистра")
    public void success_createProductRegister() {
        final ProductRegisterRequest request = new ProductRegisterRequest();

        Mockito.when(productRegisterServiceMock.validateRequest(request)).thenReturn(null);
        Mockito.when(productRegisterServiceMock.createProductRegister(request))
                .thenReturn(ProductRegisterResponse.getOkResponse("1"));

        final ResponseEntity<ProductRegisterResponse> response = productRegisterController.postRequest(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Ошибка - возникновение исключения")
    public void error_generateThrow() {
        Mockito.when(productRegisterServiceMock.validateRequest(null)).thenThrow(IllegalArgumentException.class);

        final ResponseEntity<ProductRegisterResponse> response = productRegisterController.postRequest(null);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
