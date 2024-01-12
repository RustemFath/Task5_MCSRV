package ru.study.t5_mcsrv.mapping;

import lombok.*;
import ru.study.t5_mcsrv.entity.ProductClass;
import ru.study.t5_mcsrv.message.ProductRequest;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductRequestMap {
    private ProductRequest productRequest;
    private ProductClass productClass;
}
