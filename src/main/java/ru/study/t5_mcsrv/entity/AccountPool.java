package ru.study.t5_mcsrv.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "tpp_account_pool")
/**
 * Справочник Пула Счетов
 */
public class AccountPool {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accpool_seq")
    @SequenceGenerator(name = "accpool_seq", sequenceName = "table_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "mdm_code")
    private String mdmCode;

    @Column(name = "priority_code")
    private String priorityCode;

    @Column(name = "register_type_code")
    private String registerTypeCode;
}
