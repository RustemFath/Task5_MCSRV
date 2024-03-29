package ru.study.t5_mcsrv.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "Agreements")
@NoArgsConstructor
/**
 * Доп. соглашение
 */
public class Agreement {
    /**
     * ID доп соглашения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agreem_seq")
    @SequenceGenerator(name = "agreem_seq", sequenceName = "table_id_seq", allocationSize = 1)
    private Long id;

    /**
     * ID продукта
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * Номер ДС
     */
    private String number;
}
