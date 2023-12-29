package ru.study.t5_mcsrv.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Ответ на запрос создания Продуктового регистра
 */
@Data
public class ProductRegisterResponse {
    @JsonIgnore
    private HttpStatus status;

    /** Идентификатор продуктового регистра */
    private String accountId;
}
