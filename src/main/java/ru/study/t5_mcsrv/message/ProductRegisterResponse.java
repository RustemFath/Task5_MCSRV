package ru.study.t5_mcsrv.message;

import lombok.Data;

/**
 * Ответ на запрос создания Продуктового регистра
 */
@Data
public class ProductRegisterResponse extends StatusResponse {
    /** Идентификатор продуктового регистра */
    private String registerId;

    @Override
    public void setTextResponse(String text) {
        setRegisterId(text);
    }
}
