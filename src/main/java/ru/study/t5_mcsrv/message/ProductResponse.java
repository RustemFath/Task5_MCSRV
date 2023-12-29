package ru.study.t5_mcsrv.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.List;

/**
 * Ответ на запрос создания экземпляра Продукта
 */
@Data
public class ProductResponse {
    @JsonIgnore
    private HttpStatus status;

    /** Идентификатор экземпляра продукта */
    private String instanceId;
    /** Массив идентификаторов продуктовых регистров */
    private List<String> registerId;
    /** Массив доп. соглашений */
    private List<String> supplementaryAgreementId;
}