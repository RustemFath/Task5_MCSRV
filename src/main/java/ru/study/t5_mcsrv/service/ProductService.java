package ru.study.t5_mcsrv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;

/**
 * Сервис, создающий экземпляр продукта
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductClassRepository productClassRepo;
    @Autowired
    private ProductRegisterTypeRepository productRegisterTypeRepo;
    @Autowired
    private ProductRequestToProductMapper productRequestToProductMapper;

    private static final String CLIENT_ACCOUNT_TYPE = "Клиентский";

    public ProductResponse validateRequest(ProductRequest request) {
        // Проверка Request.Body на обязательность
        if (request == null) {
            return getBadResponse("Request.Body не заполнено.");
        }

        // Проверка заполненности обязательных полей
        if (!request.isValidate()) {
            return getBadResponse(String.format("Имя обязательного параметра %s не заполнено.",
                    request.getFailField()));
        }

        return null;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        ProductResponse response = new ProductResponse();

        // Проверка таблицы ЭП на дубли
        List<Product> list = productRepo.findProductsByNumber(request.getContractNumber());
        if (!list.isEmpty()) {
            response.setInstanceId(
                    String.format("Параметр ContractNumber № договора %s уже существует для ЭП с ИД %d",
                            request.getContractNumber(), list.get(0).getId()));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        ProductClass productClass = productClassRepo.findProductClassByValue(request.getProductCode());
        if (productClass == null) {
            response.setInstanceId(
                    String.format("КодПродукта %s не найден в Каталоге продуктов", request.getProductCode()));
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        // Поиск списка типов регистра по коду продукта
        List<ProductRegisterType> listProdRegType =
                productRegisterTypeRepo.findProductRegisterTypesByProductClassCodeAndAccountType(
                        request.getProductCode(), CLIENT_ACCOUNT_TYPE);
        if (listProdRegType.isEmpty()) {
            response.setInstanceId(
                    String.format("Список ТипРегистра не найден в Каталоге типа регистра по КодПродукта %s",
                            request.getProductCode()));
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
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
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ProductResponse getInternalErrorResponse(String errorText) {
        ProductResponse response = new ProductResponse();
        response.setInstanceId(errorText);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }

    private ProductResponse getBadResponse(String errorText) {
        ProductResponse response = new ProductResponse();
        response.setInstanceId(errorText);
        response.setStatus(HttpStatus.BAD_REQUEST);
        return response;
    }
}
