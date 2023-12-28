package ru.study.t5_mcsrv.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity(name = "tpp_product")
@NoArgsConstructor
/**
 * ЭП. Экземпляр Продукта
 */
public class Product {
    /**
     * ID экземпляра продукта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name="product_seq", sequenceName="table_id_seq", allocationSize=1)
    private Long id;

    /**
     * Код продукта в каталоге продуктов
     */
    @Column(name = "product_code_id")
    private Long productCodeId;

    /**
     * ID Клиента
     */
    @Column(name = "client_id")
    private Long clientId;

    /**
     * Тип Экземпляра Продукта
     */
    private String type;

    /**
     * Номер договора
     */
    private String number;

    /**
     * Приоритет
     */
    private Long priority;

    @Column(name = "date_of_conclusion")
    private Date dateOfConclusion;

    @Column(name = "start_date_time")
    private Date startDateTime;

    @Column(name = "end_date_time")
    private Date endDateTime;

    private Long days;

    /**
     * Штрафная процентная ставка
     */
    @Column(name = "penalty_rate")
    private Double penaltyRate;

    private Double nso;

    @Column(name = "threshold_amount")
    private Double thresholdAmount;

    @Column(name = "requisite_type")
    private String requisiteType;

    @Column(name = "interestRate_type")
    private String interestRateType;

    @Column(name = "tax_rate")
    private Double taxRate;

    @Column(name = "reason_close")
    private String reasonClose;

    private String state;
}

