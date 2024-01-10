package ru.study.t5_mcsrv.mapping;

import org.springframework.stereotype.Service;
import ru.study.t5_mcsrv.entity.ProductRegister;

@Service
public class ProductRegRequestToProductRegMapper implements Mappable<ProductRegRequestMap, ProductRegister> {
    @Override
    public ProductRegister map(ProductRegRequestMap obj) {
        ProductRegister productRegister = new ProductRegister();

        productRegister.setProductId(obj.getProductRegRequest().getInstanceId());
        productRegister.setType(obj.getProductRegRequest().getRegisterTypeCode());
        productRegister.setAccountId(obj.getAccountId());
        productRegister.setCurrencyCode(obj.getProductRegRequest().getCurrencyCode());
        productRegister.setState("открыт");

        return productRegister;
    }


}
