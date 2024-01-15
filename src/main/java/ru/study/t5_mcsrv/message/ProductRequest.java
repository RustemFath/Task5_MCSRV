package ru.study.t5_mcsrv.message;

import lombok.Data;
import ru.study.t5_mcsrv.utils.RequestValidator;

import java.util.Date;
import java.util.List;

/**
 * Запрос на создание экземпляра Продукта
 */
@Data
public class ProductRequest extends RequestValidator {
    /**
     * Идентификатор экземпляра продукта
     */
    private Long instanceId;
    /**
     * Тип Экземпляра Продукта
     */
    private String productType;
    /**
     * Код продукта в каталоге продуктов
     */
    private String productCode;
    /**
     * Тип регистра
     */
    private String registerType;
    /**
     * Код Клиента (mdm)
     */
    private String mdmCode;
    /**
     * Номер договора
     */
    private String contractNumber;
    /**
     * Дата заключения договора обслуживания
     */
    private Date contractDate;
    /**
     * Приоритет
     */
    private Long priority;
    /**
     * Штрафная процентная ставка
     */
    private Double interestRatePenalty;
    /**
     * Неснижаемый остаток
     */
    private Double minimalBalance;
    /**
     * Пороговая сумма
     */
    private Double thresholdAmount;
    /**
     * Реквизиты выплаты
     */
    private String accountingDetails;
    /**
     * Выбор ставки в зависимости от суммы
     */
    private String rateType;
    /**
     * Ставка налогообложения
     */
    private Double taxPercentageRate;
    /**
     * Сумма лимита технического овердрафта
     */
    private Double technicalOverdraftLimitAmount;
    /**
     * ID Договора
     */
    private Long contractId;
    /**
     * Код филиала
     */
    private String branchCode;
    /**
     * Код валюты
     */
    private String isoCurrencyCode;
    /**
     * Код срочности договора
     */
    private String urgencyCode;

    /**
     * Код точки продаж
     */
    private Long referenceCode;
    /**
     * Массив дополнительных признаков для сегмента КИБ(VIP)
     */
    private List<AdditionalPropertyVip> additionalVipProperties;
    /**
     * Массив доп.соглашений
     */
    private List<InstanceAgreement> instanceAgreements;

    @Override
    public boolean isValidate() {
        return (instanceId == null) ? isValidForCreateProduct() : isValidForCreateAgreement();
    }

    /**
     * Проверка заполненности полей для создания нового экземпляра продукта
     *
     * @return true - все нужные поля заполнены, false - в противном случае
     */
    private boolean isValidForCreateProduct() {
        if (RequestValidator.isFailField(productType, () -> setFailField("productType"))) return false;
        if (RequestValidator.isFailField(productCode, () -> setFailField("productCode"))) return false;
        if (RequestValidator.isFailField(registerType, () -> setFailField("registerType"))) return false;
        if (RequestValidator.isFailField(mdmCode, () -> setFailField("mdmCode"))) return false;
        if (RequestValidator.isFailField(contractNumber, () -> setFailField("contractNumber"))) return false;
        if (RequestValidator.isFailField(contractDate, () -> setFailField("contractDate"))) return false;
        if (RequestValidator.isFailField(priority, () -> setFailField("priority"))) return false;
        if (RequestValidator.isFailField(branchCode, () -> setFailField("branchCode"))) return false;
        if (RequestValidator.isFailField(isoCurrencyCode, () -> setFailField("isoCurrencyCode"))) return false;
        if (RequestValidator.isFailField(urgencyCode, () -> setFailField("urgencyCode"))) return false;
        if (!urgencyCode.equals("00")) {
            setFailField("urgencyCode");
            return false;
        }

        if (additionalVipProperties != null) {
            for (var property : additionalVipProperties) {
                if (!property.isValidate()) {
                    setFailField("additionalPropertiesVip." + property.getFailField());
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Проверка заполненности полей для создания нового доп соглашения к экземпляру продукта
     *
     * @return true - все нужные поля заполнены, false - в противном случае
     */
    private boolean isValidForCreateAgreement() {
        if (RequestValidator.isFailField(contractId, () -> setFailField("contractId"))) return false;

        if (instanceAgreements == null || instanceAgreements.isEmpty()) {
            setFailField("instanceAgreement");
            return false;
        }

        for (var instance : instanceAgreements) {
            if (!instance.isValidate()) {
                setFailField("instanceAgreement." + instance.getFailField());
                return false;
            }
        }

        return true;
    }
}

/**
 * Дополнительный признак для сегмента КИБ(VIP)
 */
@Data
class AdditionalPropertyVip extends RequestValidator {
    /**
     * ключ
     */
    private String key;
    /**
     * значение
     */
    private String value;
    /**
     * наименование
     */
    private String name;

    @Override
    public boolean isValidate() {
        if (RequestValidator.isFailField(key, () -> setFailField("key"))) return false;
        if (RequestValidator.isFailField(value, () -> setFailField("value"))) return false;
        return true;
    }
}