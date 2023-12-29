package ru.study.t5_mcsrv.message;

import lombok.Data;
import ru.study.t5_mcsrv.utils.Utils;

import java.util.Date;

/**
 * Доп. соглашение
 */
@Data
public class InstanceAgreement extends RequestValidator {
    /**
     * ID доп.Ген.соглашения
     */
    private String generalAgreementId;
    /**
     * ID доп.соглашения
     */
    private String supplementaryAgreementId;
    /**
     * Тип соглашения
     */
    private String agreementType;
    /**
     * Идентификатор периодичности учета
     */
    private Long shedulerJobId;
    /**
     * Номер ДС
     */
    private String number;
    /**
     * Дата начала сделки
     */
    private Date openingDate;
    /**
     * Дата окончания сделки
     */
    private Date closingDate;
    /**
     * Дата отзыва сделки
     */
    private Date cancelDate;
    /**
     * Срок действия сделки
     */
    private Long validityDuration;
    /**
     * Причина расторжения
     */
    private String cancellationReason;
    /**
     * Состояние/статус
     */
    private String status;
    /**
     * Начисление начинается с (дата)
     */
    private Date interestCalculationDate;
    /**
     * Процент начисления на остаток
     */
    private Double interestRate;
    /**
     * Коэффициент ставки
     */
    private Double coefficient;
    /**
     * Действие коэффициента ставки
     */
    private String coefficientAction;
    /**
     * Минимум по ставке
     */
    private Double minimumInterestRate;
    /**
     * Коэффициент по минимальной ставке
     */
    private Double minimumInterestRateCoefficient;
    /**
     * Действие коэффициента по минимальной ставке
     */
    private String minimumInterestRateCoefficientAction;
    /**
     * Максимум по ставке
     */
    private Double maximalInterestRate;
    /**
     * Коэффициент по максимальной ставке
     */
    private Double maximalInterestRateCoefficient;
    /**
     * Действие коэффициента по максимальной ставке
     */
    private String maximalInterestRateCoefficientAction;

    @Override
    public boolean isValidate() {
        if (Utils.isFailField(number, () -> setFailField("number"))) return false;
        if (Utils.isFailField(openingDate, () -> setFailField("openingDate"))) return false;
        return true;
    }
}