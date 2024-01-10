package ru.study.t5_mcsrv.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "tpp_account_number")
/**
 * Справочник номеров Счетов
 */
public class AccountNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accnum_seq")
    @SequenceGenerator(name = "accnum_seq", sequenceName = "table_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "account_pool_id")
    private Long accountPoolId;

    @Column(name = "account_number")
    private String accountNumber;
}
