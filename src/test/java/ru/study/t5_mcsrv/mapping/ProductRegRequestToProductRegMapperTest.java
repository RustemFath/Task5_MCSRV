package ru.study.t5_mcsrv.mapping;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.study.t5_mcsrv.entity.ProductRegister;
import ru.study.t5_mcsrv.message.ProductRegisterRequest;

public class ProductRegRequestToProductRegMapperTest {
    private final ProductRegRequestToProductRegMapper mapper = new ProductRegRequestToProductRegMapper();

    private final static Long INSTANCE_ID = 10L;
    private final static String REG_TYPE_CODE = "reg_type_code";
    private final static String CURRENCY_CODE = "currency_code";
    private final static Long ACCOUNT_ID = 20L;
    private final static String STATE_CODE = "открыт";

    @Test
    @DisplayName("Успешный маппинг")
    public void success_map() {
        Assertions.assertEquals(createProductRegister(), mapper.map(createProductRegRequestMap()));
    }

    private ProductRegRequestMap createProductRegRequestMap() {
        ProductRegisterRequest request = new ProductRegisterRequest();
        request.setInstanceId(INSTANCE_ID);
        request.setRegisterTypeCode(REG_TYPE_CODE);
        request.setCurrencyCode(CURRENCY_CODE);

        return ProductRegRequestMap.builder()
                .productRegRequest(request)
                .accountId(ACCOUNT_ID)
                .build();
    }

    private ProductRegister createProductRegister() {
        ProductRegister register = new ProductRegister();
        register.setProductId(INSTANCE_ID);
        register.setType(REG_TYPE_CODE);
        register.setAccountId(ACCOUNT_ID);
        register.setCurrencyCode(CURRENCY_CODE);
        register.setState(STATE_CODE);
        return register;
    }
}
