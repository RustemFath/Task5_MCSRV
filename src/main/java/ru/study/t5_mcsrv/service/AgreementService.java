package ru.study.t5_mcsrv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.study.t5_mcsrv.entity.Agreement;
import ru.study.t5_mcsrv.entity.Product;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.message.ProductResponse;
import ru.study.t5_mcsrv.repo.AgreementRepository;
import ru.study.t5_mcsrv.repo.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, создающий доп. соглашения
 */
@Service
public class AgreementService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
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
            final List<Agreement> listDC = agreementRepo.findAgreementsByNumber(dc.getNumber());
            if (!listDC.isEmpty()) {
                return ProductResponse.getBadResponse(
                        String.format("Параметр № Дополнительного соглашения (сделки) Number %s уже существует для ЭП с ИД %d",
                                dc.getNumber(), listDC.get(0).getProductId()));
            }
        }

        // Проверка на дубли номеров во входящем массиве
        int uniqNumberSize = request.getInstanceAgreements().stream().distinct().toList().size();
        if (uniqNumberSize < request.getInstanceAgreements().size()) {
            return ProductResponse.getBadResponse(
                    String.format("Обнаружены дубли параметра № Дополнительного соглашения (сделки) Number для ЭП с ИД %d",
                            request.getContractId()));
        }

        // создать массив новых доп соглашений
        List<Agreement> agreements = new ArrayList<>(request.getInstanceAgreements().size());
        for (var dc : request.getInstanceAgreements()) {
            Agreement agreement = new Agreement();
            agreement.setProductId(product.getId());
            agreement.setNumber(dc.getNumber());
            agreement = agreementRepo.save(agreement);
            agreements.add(agreement);
        }

        // Формирование ответа
        ProductResponse response = ProductResponse.getOkResponse(product.getId().toString());
        response.setSupplementaryAgreementId(agreements.stream().map(agr -> agr.getId().toString()).toList());
        return response;
    }
}
