package ru.study.t5_mcsrv.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.study.t5_mcsrv.entity.Agreement;
import ru.study.t5_mcsrv.entity.Product;
import ru.study.t5_mcsrv.entity.ProductClass;
import ru.study.t5_mcsrv.entity.ProductRegisterType;
import ru.study.t5_mcsrv.mapping.ProductRequestMap;
import ru.study.t5_mcsrv.mapping.ProductRequestToProductMapper;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.message.ProductResponse;
import ru.study.t5_mcsrv.repo.AgreementRepository;
import ru.study.t5_mcsrv.repo.ProductClassRepository;
import ru.study.t5_mcsrv.repo.ProductRegisterTypeRepository;
import ru.study.t5_mcsrv.repo.ProductRepository;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "${rest.product-endpoint}", produces = "application/json")
public class ProductController {
    private ProductRepository productRepo;
    private AgreementRepository agreementRepo;
    private ProductRegisterTypeRepository productRegisterTypeRepo;
    private ProductClassRepository productClassRepo;
    private ProductRequestToProductMapper productRequestToProductMapper;

    private static final String CLIENT_ACCOUNT_TYPE = "Клиентский";

    @PostMapping(consumes = "application/json")
    @Transactional
    public ResponseEntity<ProductResponse> postProductRegister(@RequestBody ProductRequest request) {

        ProductResponse response = new ProductResponse();
        try {
            // Проверка Request.Body на обязательность
            if (request == null) {
                response.setInstanceId("Request.Body не заполнено.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Проверка заполненности обязательных полей
            if (!request.isValidate()) {
                response.setInstanceId(
                        String.format("Имя обязательного параметра %s не заполнено.", request.getFailField()));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Создание нового экземпляра продукта
            if (request.getInstanceId() == null) {
                // Проверка таблицы ЭП на дубли
                List<Product> list = productRepo.findProductsByNumber(request.getContractNumber());
                if (!list.isEmpty()) {
                    response.setInstanceId(
                            String.format("Параметр ContractNumber № договора %s уже существует для ЭП с ИД %d",
                                    request.getContractNumber(), list.get(0).getId()));
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                ProductClass productClass = productClassRepo.findProductClassByValue(request.getProductCode());
                if (productClass == null) {
                    response.setInstanceId(
                            String.format("КодПродукта %s не найден в Каталоге продуктов", request.getProductCode()));
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                // Поиск списка типов регистра по коду продукта
                List<ProductRegisterType> listProdRegType =
                        productRegisterTypeRepo.findProductRegisterTypesByProductClassCodeAndAccountType(
                                request.getProductCode(), CLIENT_ACCOUNT_TYPE);
                if (listProdRegType.isEmpty()) {
                    response.setInstanceId(
                            String.format("Список ТипРегистра не найден в Каталоге типа регистра по КодПродукта %s",
                                    request.getProductCode()));
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                // Подготовка к созданию ЭП
                ProductRequestMap productRequestMap = new ProductRequestMap();
                productRequestMap.setProductRequest(request);
                productRequestMap.setProductClass(productClass);

                // Добавить запись в таблицу tpp_product
                Product product = productRequestToProductMapper.map(productRequestMap);
                productRepo.save(product);

                // Создать ПР

                response.setInstanceId(product.getId().toString());
            }
            // Создание нового доп соглашения к существующему договору
            else {
                // Проверка на существование договора с ID
                Product oldProduct = productRepo.findById(request.getContractId()).orElse(null);
                if (oldProduct == null) {
                    response.setInstanceId(
                            String.format("ЭП с ИД %d не найден в БД", request.getContractId()));
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                // Проверка таблицы ДС на дубли
                for (var dc : request.getInstanceAgreements()) {
                    List<Agreement> listDC = agreementRepo.findAgreementsByNumber(dc.getNumber());
                    if (!listDC.isEmpty()) {
                        response.setInstanceId(
                                String.format("Параметр № Дополнительного соглашения (сделки) Number %s уже существует для ЭП с ИД %d",
                                        dc.getNumber(), listDC.get(0).getProductId()));
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                }

                // создать новое доп соглашение

                // Запрос у ГС счета и привязка к продуктовому регистру

                response.setInstanceId(oldProduct.getId().toString());
            }
        }
        catch (Exception e) {
            log.info(e.toString());
            throw new RuntimeException(e);
        }
        // Отправка ответа в систему-источник запроса на создание Экземпляра продукта
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
