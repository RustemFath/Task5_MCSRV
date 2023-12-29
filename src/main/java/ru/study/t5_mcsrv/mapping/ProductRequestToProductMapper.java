package ru.study.t5_mcsrv.mapping;

import org.springframework.stereotype.Service;
import ru.study.t5_mcsrv.entity.Product;

@Service
public class ProductRequestToProductMapper implements Mappable<ProductRequestMap, Product> {
    @Override
    public Product map(ProductRequestMap rq) {
        Product pr = new Product();
        pr.setProductCodeId(rq.getProductClass().getInternalId());
        pr.setType(rq.getProductRequest().getProductType());
        pr.setNumber(rq.getProductRequest().getContractNumber());
        pr.setPriority(rq.getProductRequest().getPriority());
        pr.setPenaltyRate(rq.getProductRequest().getInterestRatePenalty());
        pr.setTaxRate(rq.getProductRequest().getTaxPercentageRate());
        pr.setInterestRateType(rq.getProductRequest().getRateType());
        pr.setState("открыт");

        return pr;
    }
}
