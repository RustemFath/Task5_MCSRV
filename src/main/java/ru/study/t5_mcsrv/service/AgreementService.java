package ru.study.t5_mcsrv.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProductResponse> createAgreement(ProductRequest request) {
        ProductResponse response = new ProductResponse();

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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
