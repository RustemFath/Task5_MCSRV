package ru.study.t5_mcsrv.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.study.t5_mcsrv.entity.ProductRegisterType;

import java.util.List;

@Repository
public interface ProductRegisterTypeRepository extends CrudRepository<ProductRegisterType, Long> {
    List<ProductRegisterType> findProductRegisterTypesByProductClassCodeAndAccountType(String productClassCode, String accountType);
}
