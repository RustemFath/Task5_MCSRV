package ru.study.t5_mcsrv.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class StatusResponse {
    @JsonIgnore
    private HttpStatus status;

    public abstract void setTextResponse(String text);
}
