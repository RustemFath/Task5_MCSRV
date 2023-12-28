package ru.study.t5_mcsrv.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "tpp_ref_account_type")
/**
 * 165_Справочник Тип Счета
 */
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acctype_seq")
    @SequenceGenerator(name = "acctype_seq", sequenceName = "table_id_seq", allocationSize = 1)
    @Column(name = "internal_id")
    private Long internalId;

    private String value;
}
