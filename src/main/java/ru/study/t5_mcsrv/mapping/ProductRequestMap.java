package ru.study.t5_mcsrv.mapping;

import lombok.Data;
import ru.study.t5_mcsrv.entity.ProductClass;
import ru.study.t5_mcsrv.message.ProductRequest;

@Data
public class ProductRequestMap {
    private ProductRequest productRequest;
    private ProductClass productClass;
}
