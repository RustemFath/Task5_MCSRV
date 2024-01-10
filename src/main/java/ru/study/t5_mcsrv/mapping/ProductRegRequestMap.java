package ru.study.t5_mcsrv.mapping;

import lombok.Getter;
import ru.study.t5_mcsrv.message.ProductRegisterRequest;

@Getter
public class ProductRegRequestMap {
    private ProductRegisterRequest productRegRequest;
    private Long accountId;

    public static ProductRegRequestMap createMap() {
        return new ProductRegRequestMap();
    }

    public ProductRegRequestMap setProductRegRequest(ProductRegisterRequest productRegRequest) {
        this.productRegRequest = productRegRequest;
        return this;
    }

    public ProductRegRequestMap setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }
}
