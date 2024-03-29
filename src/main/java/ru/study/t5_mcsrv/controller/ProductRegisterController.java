package ru.study.t5_mcsrv.controller;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.study.t5_mcsrv.message.ProductRegisterRequest;
import ru.study.t5_mcsrv.message.ProductRegisterResponse;
import ru.study.t5_mcsrv.utils.ResponseMaker;
import ru.study.t5_mcsrv.service.ProductRegisterService;

@RestController
@Slf4j
@Setter
@RequestMapping(path = "${rest.account-endpoint}", produces = "application/json")
public class ProductRegisterController {
    @Autowired
    private ProductRegisterService productRegisterService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProductRegisterResponse> postRequest(@RequestBody ProductRegisterRequest request) {
        log.info("get post request - {}", request);
        try {
            // Проверка Request
            ProductRegisterResponse response = productRegisterService.validateRequest(request);
            if (response != null) {
                return getResponseEntity(response);
            }

            // Создание нового продуктового регистра
            return getResponseEntity(productRegisterService.createProductRegister(request));
        }
        catch (Exception e) {
            log.info(e.toString());
            return getResponseEntity(
                    ResponseMaker.getInternalErrorResponse(new ProductRegisterResponse(), e.toString()));
        }
    }

    private ResponseEntity<ProductRegisterResponse> getResponseEntity(ProductRegisterResponse response) {
        log.info("send response - {}", response.getStatus());
        return new ResponseEntity<>(response, response.getStatus());
    }
}
