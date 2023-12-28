package ru.study.t5_mcsrv.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "tpp_ref_product_register_type")
/**
 * 167_Справочник типов регистров
 */
public class ProductRegisterType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prt_seq")
    @SequenceGenerator(name="prt_seq", sequenceName="table_id_seq", allocationSize=1)
    @Column(name = "internal_id")
    private Long internalId;

    private String value;

    @Column(name = "register_type_name")
    private String registerTypeName;

    @Column(name = "product_class_code")
    private String productClassCode;

    @Column(name = "account_type")
    private String accountType;
}
