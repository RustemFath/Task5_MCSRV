package ru.study.t5_mcsrv.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.study.t5_mcsrv.entity.ProductRegister;

@Repository
public interface ProductRegisterRepository extends CrudRepository<ProductRegister, Long> {
}
