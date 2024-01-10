package ru.study.t5_mcsrv.mapping;

import lombok.Getter;
import ru.study.t5_mcsrv.entity.ProductClass;
import ru.study.t5_mcsrv.message.ProductRequest;

@Getter
public class ProductRequestMap {
    private ProductRequest productRequest;
    private ProductClass productClass;

    public static ProductRequestMap createMap() {
        return new ProductRequestMap();
    }

    public ProductRequestMap setProductRequest(ProductRequest productRequest) {
        this.productRequest = productRequest;
        return this;
    }

    public ProductRequestMap setProductClass(ProductClass productClass) {
        this.productClass = productClass;
        return this;
    }
}
