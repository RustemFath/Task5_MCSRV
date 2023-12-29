package ru.study.t5_mcsrv.message;

import lombok.Data;
import ru.study.t5_mcsrv.utils.Utils;

/**
 * Запрос на создание Продуктового регистра
 */
@Data
public class ProductRegisterRequest extends RequestValidator {
    /**
     * Идентификатор экземпляра продукта
     */
    private Long instanceId;
    /**
     * Тип регистра
     */
    private String registerTypeCode;
    /**
     * Тип счета
     */
    private String accountType;
    /**
     * Код валюты
     */
    private String currencyCode;
    /**
     * Код филиала
     */
    private String branchCode;
    /**
     * Код срочности
     */
    private String priorityCode;
    /**
     * Id клиента
     */
    private String mdmCode;
    /**
     * Код клиента
     */
    private String clientCode;
    /**
     * Регион принадлежности железной дороги
     */
    private String trainRegion;
    /**
     * Счетчик
     */
    private String counter;
    /**
     * Код точки продаж
     */
    private String salesCode;

    /**
     * Проверка заполненности полей для создания нового Продуктового регистра
     *
     * @return true - все нужные поля заполнены, false - в противном случае
     */
    @Override
    public boolean isValidate() {
        if (Utils.isFailField(instanceId, () -> setFailField("instanceId"))) return false;
        if (Utils.isFailField(registerTypeCode, () -> setFailField("registerTypeCode"))) return false;
        if (Utils.isFailField(accountType, () -> setFailField("accountType"))) return false;
        if (Utils.isFailField(currencyCode, () -> setFailField("currencyCode"))) return false;
        if (Utils.isFailField(priorityCode, () -> setFailField("priorityCode"))) return false;
        if (!priorityCode.equals("00")) {
            setFailField("priorityCode");
            return false;
        }

        return true;
    }
}
