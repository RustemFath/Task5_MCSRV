package ru.study.t5_mcsrv.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.study.t5_mcsrv.entity.*;
import ru.study.t5_mcsrv.mapping.ProductRegRequestMap;
import ru.study.t5_mcsrv.mapping.ProductRegRequestToProductRegMapper;
import ru.study.t5_mcsrv.message.ProductRegisterRequest;
import ru.study.t5_mcsrv.message.ProductRegisterResponse;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.utils.ResponseMaker;
import ru.study.t5_mcsrv.repo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, создающий продуктовый регистр
 */
@Service
@Setter
@Slf4j
public class ProductRegisterService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductClassRepository productClassRepo;
    @Autowired
    private ProductRegisterRepository productRegisterRepo;
    @Autowired
    private ProductRegisterTypeRepository productRegisterTypeRepo;
    @Autowired
    private AccountPoolRepository accountPoolRepo;
    @Autowired
    private AccountNumberRepository accountNumberRepo;
    @Autowired
    private ProductRegRequestToProductRegMapper productRegRequestToProductRegMapper;

    public ProductRegisterResponse validateRequest(ProductRegisterRequest request) {
        // Проверка Request.Body на обязательность
        if (request == null) {
            String info = "Request.Body не заполнено";
            log.info(info);
            return ResponseMaker.getBadResponse(new ProductRegisterResponse(), info);
        }

        // Проверка заполненности обязательных полей
        if (!request.isValidate()) {
            String info = String.format("Имя обязательного параметра %s не заполнено.", request.getFailField());
            log.info(info);
            return ResponseMaker.getBadResponse(new ProductRegisterResponse(), info);
        }

        return null;
    }

    @Transactional
    public ProductRegisterResponse createProductRegister(ProductRegisterRequest request) {
        // Проверка таблицы ПР на дубли
        List<ProductRegister> productRegisterList = productRegisterRepo.findProductRegistersByProductIdAndType(
                request.getInstanceId(), request.getRegisterTypeCode());
        if (!productRegisterList.isEmpty()) {
            String info = String.format("Параметр registryTypeCode тип регистра %s уже существует для ЭП с ИД %d",
                    request.getRegisterTypeCode(), productRegisterList.get(0).getProductId());
            log.info(info);
            return ResponseMaker.getBadResponse(new ProductRegisterResponse(), info);
        }

        // Получение родительского экземпляра продукта
        Product product = productRepo.findById(request.getInstanceId()).orElse(null);
        if (product == null) {
            String info = String.format("Экземпляр Продукта с ИД %d не найден в справочнике продуктов",
                    request.getInstanceId());
            log.info(info);
            return ResponseMaker.getNotFoundResponse(new ProductRegisterResponse(), info);
        }

        // Получение Кода Продукта по экземпляру продукта
        ProductClass productClass = productClassRepo.findProductClassByInternalId(product.getProductCodeId());
        if (productClass == null) {
            String info = String.format("КодПродукта с ИД %d не найден в Каталоге продуктов", product.getProductCodeId());
            log.info(info);
            return ResponseMaker.getNotFoundResponse(new ProductRegisterResponse(), info);
        }

        // Поиск списка типов регистра по коду продукта
        List<ProductRegisterType> prodRegTypeList =
                productRegisterTypeRepo.findProductRegisterTypesByProductClassCode(productClass.getValue());
        if (prodRegTypeList.isEmpty()) {
            String info = String.format("Список ТипРегистра не найден в Каталоге типа регистра по КодПродукта %s",
                    productClass.getValue());
            log.info(info);
            return ResponseMaker.getNotFoundResponse(new ProductRegisterResponse(), info);
        }

        // Проверка наличия КодаПродукта в списке типов регистра
        if (prodRegTypeList.stream().noneMatch(t -> t.getValue().equals(request.getRegisterTypeCode()))) {
            String info = String.format("КодПродукта %s не найдено в Каталоге продуктов для данного типа Регистра %s",
                    productClass.getValue(), request.getRegisterTypeCode());
            log.info(info);
            return ResponseMaker.getNotFoundResponse(new ProductRegisterResponse(), info);
        }

        // Получение номера счета из пула счетов
        Long accountId = getAccountFromPool(
                request.getBranchCode(),
                request.getCurrencyCode(),
                request.getMdmCode(),
                request.getPriorityCode(),
                request.getRegisterTypeCode()
        );

        // Подготовка к созданию продуктового регистра
        ProductRegRequestMap productRegRequestMap = ProductRegRequestMap.builder()
                .productRegRequest(request)
                .accountId(accountId)
                .build();

        // Создание продуктового регистра
        ProductRegister productRegister = productRegRequestToProductRegMapper.map(productRegRequestMap);
        productRegister = productRegisterRepo.save(productRegister);

        ProductRegisterResponse response =
                ResponseMaker.getOkResponse(new ProductRegisterResponse(), productRegister.getId().toString());
        log.info("created PR, response = {}", response);
        return response;
    }

    public List<Long> createProductRegisters(List<ProductRegisterType> types, ProductRequest request) {
        List<Long> results = new ArrayList<>();

        types.forEach(type -> {
            // Получение номера счета из пула счетов
            Long accountId = getAccountFromPool(
                    request.getBranchCode(),
                    request.getIsoCurrencyCode(),
                    request.getMdmCode(),
                    request.getUrgencyCode(),
                    request.getRegisterType()
            );

            // Создание продуктового регистра
            ProductRegister productRegister = new ProductRegister();
            productRegister.setProductId(request.getInstanceId());
            productRegister.setType(type.getValue());
            productRegister.setAccountId(accountId);
            productRegister.setCurrencyCode(request.getIsoCurrencyCode());
            productRegister.setState("открыт");

            productRegister = productRegisterRepo.save(productRegister);
            results.add(productRegister.getId());
        });

        log.info("created PRs, results = {}", results);
        return results;
    }

    // Получение номера счета из пула счетов
    private Long getAccountFromPool(String branchCode,
                                    String currencyCode,
                                    String mdmCode,
                                    String priorityCode,
                                    String registerTypeCode) {
        AccountPool accountPool =
                accountPoolRepo.findAccountPoolByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
                        branchCode,
                        currencyCode,
                        mdmCode,
                        priorityCode,
                        registerTypeCode
                );

        if (accountPool == null) return null;

        List<AccountNumber> accountNumbers = accountNumberRepo.findAccountNumbersByAccountPoolIdOrderById(
                accountPool.getId()
        );

        return accountNumbers.isEmpty()? null: accountNumbers.get(0).getId();
    }
}
