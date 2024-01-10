package ru.study.t5_mcsrv.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.study.t5_mcsrv.entity.Agreement;
import ru.study.t5_mcsrv.entity.Product;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.message.ProductResponse;
import ru.study.t5_mcsrv.repo.AgreementRepository;
import ru.study.t5_mcsrv.repo.ProductRepository;

import java.util.List;

/**
 * Сервис, создающий доп. соглашение
 */
@Service
public class AgreementService {
    private ProductRepository productRepo;
    private AgreementRepository agreementRepo;

    @Transactional
    public ProductResponse createAgreement(ProductRequest request) {
        // Проверка на существование договора с ID
        Product product = productRepo.findById(request.getContractId()).orElse(null);
        if (product == null) {
            return ProductResponse.getNotFoundResponse(
                    String.format("ЭП с ИД %d не найден в БД", request.getContractId()));
        }

        // Проверка таблицы ДС на дубли
        for (var dc : request.getInstanceAgreements()) {
            List<Agreement> listDC = agreementRepo.findAgreementsByNumber(dc.getNumber());
            if (!listDC.isEmpty()) {
                return ProductResponse.getBadResponse(
                        String.format("Параметр № Дополнительного соглашения (сделки) Number %s уже существует для ЭП с ИД %d",
                                dc.getNumber(), listDC.get(0).getProductId()));
            }
        }

        // создать новое доп соглашение

        // Запрос у ГС счета и привязка к продуктовому регистру

        ProductResponse response = new ProductResponse();
        response.setInstanceId(product.getId().toString());
        response.setStatus(HttpStatus.OK);
        return response;
    }
}
