package ru.study.t5_mcsrv.mapping;

import lombok.*;
import ru.study.t5_mcsrv.message.ProductRegisterRequest;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductRegRequestMap {
    private ProductRegisterRequest productRegRequest;
    private Long accountId;
}
