package ru.study.t5_mcsrv.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.study.t5_mcsrv.entity.Product;
import ru.study.t5_mcsrv.entity.ProductClass;
import ru.study.t5_mcsrv.message.ProductRequest;

public class ProductRequestToProductMapperTest {
    private final ProductRequestToProductMapper mapper = new ProductRequestToProductMapper();

    private final static Long INTERNAL_ID = 10L;
    private final static String PRODUCT_TYPE = "product_type";
    private final static String CONTRACT_NUMBER = "contract_num";
    private final static Long PRIORITY = 5L;
    private final static Double PENALTY_RATE = 0.5;
    private final static Double TAX_RATE = 1.5;
    private final static String RATE_TYPE = "rate_type";
    private final static String STATE_CODE = "открыт";

    @Test
    @DisplayName("Успешный маппинг")
    public void success_map() {
        Assertions.assertEquals(createProduct(), mapper.map(createProductRequestMap()));
    }

    private Product createProduct() {
        Product pr = new Product();
        pr.setProductCodeId(INTERNAL_ID);
        pr.setType(PRODUCT_TYPE);
        pr.setNumber(CONTRACT_NUMBER);
        pr.setPriority(PRIORITY);
        pr.setPenaltyRate(PENALTY_RATE);
        pr.setTaxRate(TAX_RATE);
        pr.setInterestRateType(RATE_TYPE);
        pr.setState(STATE_CODE);
        return pr;
    }

    private ProductRequestMap createProductRequestMap() {
        ProductRequest request = new ProductRequest();
        request.setProductType(PRODUCT_TYPE);
        request.setContractNumber(CONTRACT_NUMBER);
        request.setPriority(PRIORITY);
        request.setInterestRatePenalty(PENALTY_RATE);
        request.setTaxPercentageRate(TAX_RATE);
        request.setRateType(RATE_TYPE);

        ProductClass productClass = new ProductClass();
        productClass.setInternalId(INTERNAL_ID);

        return ProductRequestMap.builder()
                .productRequest(request)
                .productClass(productClass)
                .build();
    }
}
