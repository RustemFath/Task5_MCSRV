package ru.study.t5_mcsrv.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "tpp_product_register")
@NoArgsConstructor
/**
 * ПР. Продуктовый регистр
  */
public class ProductRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reg_seq")
    @SequenceGenerator(name="reg_seq", sequenceName="table_id_seq", allocationSize=1)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    private String type;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "currency_code")
    private String currencyCode;

    private String state;
}
