package ru.study.t5_mcsrv.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.study.t5_mcsrv.message.ProductRegisterRequest;
import ru.study.t5_mcsrv.message.ProductRegisterResponse;

@RestController
@Slf4j
@RequestMapping(path = "${rest.account-endpoint}", produces = "application/json")
public class ProductRegisterController {
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProductRegisterResponse> postRequest(@RequestBody ProductRegisterRequest request) {
        return null;
    }
}
