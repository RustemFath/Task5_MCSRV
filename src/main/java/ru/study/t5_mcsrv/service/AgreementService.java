package ru.study.t5_mcsrv.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.study.t5_mcsrv.entity.Agreement;
import ru.study.t5_mcsrv.entity.Product;
import ru.study.t5_mcsrv.message.ProductRequest;
import ru.study.t5_mcsrv.message.ProductResponse;
import ru.study.t5_mcsrv.utils.ResponseMaker;
import ru.study.t5_mcsrv.repo.AgreementRepository;
import ru.study.t5_mcsrv.repo.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, создающий доп. соглашения
 */
@Service
@Setter
@Slf4j
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
            String info = String.format("ЭП с ИД %d не найден в БД", request.getContractId());
            log.info(info);
            return ResponseMaker.getNotFoundResponse(new ProductResponse(), info);
        }

        // Проверка таблицы ДС на дубли
        for (var dc : request.getInstanceAgreements()) {
            final List<Agreement> listDC = agreementRepo.findAgreementsByNumber(dc.getNumber());
            if (!listDC.isEmpty()) {
                String info = String.format("Параметр № Дополнительного соглашения (сделки) Number %s уже существует для ЭП с ИД %d",
                        dc.getNumber(), listDC.get(0).getProductId());
                log.info(info);
                return ResponseMaker.getBadResponse(new ProductResponse(), info);
            }
        }

        // Проверка на дубли номеров во входящем массиве
        int uniqNumberSize = request.getInstanceAgreements().stream().distinct().toList().size();
        if (uniqNumberSize < request.getInstanceAgreements().size()) {
            String info = String.format("Обнаружены дубли параметра № Дополнительного соглашения (сделки) Number для ЭП с ИД %d",
                    request.getContractId());
            log.info(info);
            return ResponseMaker.getBadResponse(new ProductResponse(), info);
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
        ProductResponse response = ResponseMaker.getOkResponse(new ProductResponse(), product.getId().toString());
        response.setSupplementaryAgreementId(agreements.stream().map(agr -> agr.getId().toString()).toList());
        log.info("Доп соглашения созданы {}", response);
        return response;
    }
}
