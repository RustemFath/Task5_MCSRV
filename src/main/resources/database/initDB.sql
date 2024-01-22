CREATE TABLE IF NOT EXISTS tpp_product
(
    id                                 bigint,
    product_code_id                    bigint references tpp_ref_product_class(internal_id),
    client_id                          bigint,
    type                               varchar(50),
    number                             varchar(50),
    priority                           bigint,
    date_of_conclusion                 timestamp,
    start_date_time                    timestamp,
    end_date_time                      timestamp,
    days                               bigint,
    penalty_rate                       numeric,
    nso                                numeric,
    threshold_amount                   numeric,
    requisite_type                     varchar(50),
    interest_rate_type                 varchar(50),
    tax_rate                           numeric,
    reason_close                       varchar(50),
    state                              varchar(50),
    CONSTRAINT pk_tpp_product PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tpp_ref_account_type
(
    internal_id bigint
        CONSTRAINT pk_tpp_ref_account_type PRIMARY KEY,
    value_code       varchar(50)
);

CREATE TABLE IF NOT EXISTS tpp_ref_product_class
(
    internal_id      bigint
        CONSTRAINT pk_tpp_ref_product_class PRIMARY KEY,
    value_code       varchar(100),
    gbl_code         varchar(10),
    gbl_name         varchar(100),
    product_row_code varchar(10),
    product_row_name varchar(100),
    subclass_code    varchar(100),
    subclass_name    varchar(100)
);

CREATE TABLE IF NOT EXISTS tpp_ref_product_register_type
(
    internal_id              bigint
        CONSTRAINT pk_tpp_ref_product_register_type PRIMARY KEY,
    value_code               varchar(100),
    register_type_name       varchar(100),
    product_class_code       varchar(25),
    account_type             varchar(50)
);

CREATE TABLE IF NOT EXISTS tpp_product_register
(
    id            bigint
        CONSTRAINT pk_tpp_product_register PRIMARY KEY,
    product_id    bigint references tpp_product(id),
    type          varchar(100),
    account_id    bigint,
    currency_code varchar(20),
    state         varchar(50)
);

CREATE TABLE IF NOT EXISTS agreements
(
    id         bigint CONSTRAINT pk_agreements PRIMARY KEY,
    product_id bigint references tpp_product(id),
    number     varchar(50)
);

CREATE TABLE IF NOT EXISTS tpp_account_pool
(
    id                 bigint
        CONSTRAINT pk_tpp_account_pool PRIMARY KEY,
    branch_code        varchar(20),
    currency_code      varchar(20),
    mdm_code           varchar(50),
    priority_code      varchar(30),
    register_type_code varchar(50)
);

CREATE TABLE IF NOT EXISTS tpp_account_number
(
    id              bigint
        CONSTRAINT pk_tpp_account_number PRIMARY KEY,
    account_pool_id bigint references tpp_account_pool(id),
    account_number  varchar(25)
);

CREATE SEQUENCE table_id_seq;