insert into tpp_ref_product_register_type( internal_id
                                         , value_code
                                         , register_type_name
                                         , product_class_code
                                         , account_type)
values ( NEXTVAL('table_id_seq')
       , '03.012.002_47533_ComSoLd'
       , 'Хранение ДМ.'
       , '03.012.002'
       , 'Клиентский');

insert into tpp_ref_product_register_type( internal_id
                                         , value_code
                                         , register_type_name
                                         , product_class_code
                                         , account_type)
values ( NEXTVAL('table_id_seq')
       , '03.012.002_47534_ComSoLd'
       , 'Хранение ДМ.'
       , '03.012.002'
       , 'Внутрибанковский');

insert into tpp_ref_product_register_type( internal_id
                                         , value_code
                                         , register_type_name
                                         , product_class_code
                                         , account_type)
values ( NEXTVAL('table_id_seq')
       , '02.001.005_45343_CoDowFF'
       , 'Серебро. Выкуп.'
       , '02.001.005_45343'
       , 'Клиентский');


insert into tpp_ref_account_type( internal_id
                                , value_code)
values ( NEXTVAL('table_id_seq')
       , 'Клиентский');

insert into tpp_ref_account_type( internal_id
                                , value_code)
values ( NEXTVAL('table_id_seq')
       , 'Внутрибанковский');

insert into tpp_ref_product_class( internal_id
                                 , value_code
                                 , gbl_code
                                 , gbl_name
                                 , product_row_code
                                 , product_row_name
                                 , subclass_code
                                 , subclass_name)
values ( NEXTVAL('table_id_seq')
       , '03.012.002'
       , '03'
       , 'Розничный бизнес'
       , '012'
       , 'Драг. металлы'
       , '002'
       , 'Хранение');

insert into tpp_ref_product_class( internal_id
                                 , value_code
                                 , gbl_code
                                 , gbl_name
                                 , product_row_code
                                 , product_row_name
                                 , subclass_code
                                 , subclass_name)
values ( NEXTVAL('table_id_seq')
       , '02.001.005'
       , '02'
       , 'Розничный бизнес'
       , '001'
       , 'Сырье'
       , '005'
       , 'Продажа');

insert into tpp_account_pool ( id
                             , branch_code
                             , currency_code
                             , mdm_code
                             , priority_code
                             , register_type_code)
values ( NEXTVAL('table_id_seq')
       , '0022'
       , '800'
       , '15'
       , '00'
       , '03.012.002_47533_ComSoLd');
insert into tpp_account_number (id,
                                account_pool_id
    , account_number)
select NEXTVAL('table_id_seq'), id, '475335516415314841861'
from tpp_account_pool
where branch_code = '0022';
insert into tpp_account_number (id,
                                account_pool_id
    , account_number)
select NEXTVAL('table_id_seq'), id, '4753321651354151'
from tpp_account_pool
where branch_code = '0022';
insert into tpp_account_number (id,
                                account_pool_id
    , account_number)
select NEXTVAL('table_id_seq'), id, '4753352543276345'
from tpp_account_pool
where branch_code = '0022';


insert into tpp_account_pool ( id
                             , branch_code
                             , currency_code
                             , mdm_code
                             , priority_code
                             , register_type_code)
values ( NEXTVAL('table_id_seq')
       , '0021'
       , '500'
       , '13'
       , '00'
       , '02.001.005_45343_CoDowFF');

insert into tpp_account_number (id,
                                account_pool_id
    , account_number)
select NEXTVAL('table_id_seq'), id, '453432352436453276'
from tpp_account_pool
where branch_code = '0021';
insert into tpp_account_number (id,
                                account_pool_id
    , account_number)
select NEXTVAL('table_id_seq'), id, '45343221651354151'
from tpp_account_pool
where branch_code = '0021';
insert into tpp_account_number (id,
                                account_pool_id
    , account_number)
select NEXTVAL('table_id_seq'), id, '4534352543276345'
from tpp_account_pool
where branch_code = '0021';