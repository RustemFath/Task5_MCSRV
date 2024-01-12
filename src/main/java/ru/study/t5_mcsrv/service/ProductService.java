package ru.study.t5_mcsrv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.study.t5_mcsrv.entity.Product;
import ru.study.t5_mcsrv.entity.ProductClass;
import ru.study.t5_mcsrv.entity.ProductRegisterType;
import ru.study.t5_mcsrv.mapping.ProductRequestMap;
import ru.study.t5_mcsrv.mapping.ProductRequestToProductMapper;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.message.ProductResponse;
import ru.study.t5_mcsrv.utils.ResponseMaker;
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
    @Autowired
    private ProductRegisterService productRegisterService;

    private static final String CLIENT_ACCOUNT_TYPE = "Клиентский";

    public ProductResponse validateRequest(ProductRequest request) {
        // Проверка Request.Body на обязательность
        if (request == null) {
            return ResponseMaker.getBadResponse(new ProductResponse(), "Request.Body не заполнено.");
        }

        // Проверка заполненности обязательных полей
        if (!request.isValidate()) {
            return ResponseMaker.getBadResponse(new ProductResponse(),
                    String.format("Имя обязательного параметра %s не заполнено.", request.getFailField()));
        }

        return null;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // Проверка таблицы ЭП на дубли
        List<Product> list = productRepo.findProductsByNumber(request.getContractNumber());
        if (!list.isEmpty()) {
            return ResponseMaker.getBadResponse(new ProductResponse(),
                    String.format("Параметр ContractNumber № договора %s уже существует для ЭП с ИД %d",
                    request.getContractNumber(), list.get(0).getId()));
        }

        ProductClass productClass = productClassRepo.findProductClassByValue(request.getProductCode());
        if (productClass == null) {
            return ResponseMaker.getNotFoundResponse(new ProductResponse(),
                    String.format("КодПродукта %s не найден в Каталоге продуктов", request.getProductCode()));
        }

        // Поиск списка типов регистра по коду продукта
        List<ProductRegisterType> prodRegTypeList =
                productRegisterTypeRepo.findProductRegisterTypesByProductClassCodeAndAccountType(
                        request.getProductCode(), CLIENT_ACCOUNT_TYPE);
        if (prodRegTypeList.isEmpty()) {
            return ResponseMaker.getNotFoundResponse(new ProductResponse(),
                    String.format("Список ТипРегистра не найден в Каталоге типа регистра по КодПродукта %s",
                    request.getProductCode()));
        }

        // Подготовка к созданию ЭП
        ProductRequestMap productRequestMap = ProductRequestMap.createMap()
                .setProductRequest(request)
                .setProductClass(productClass);

        // Добавить запись в таблицу tpp_product
        Product product = productRequestToProductMapper.map(productRequestMap);
        product = productRepo.save(product);

        // Создать ПР
        request.setInstanceId(product.getId());
        List<Long> productRegisters = productRegisterService.createProductRegisters(prodRegTypeList, request);

        ProductResponse response = ResponseMaker.getOkResponse(new ProductResponse(), product.getId().toString());
        response.setRegisterId(productRegisters.stream().map(Object::toString).toList());
        return response;
    }
}
