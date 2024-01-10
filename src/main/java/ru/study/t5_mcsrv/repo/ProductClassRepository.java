package ru.study.t5_mcsrv.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.study.t5_mcsrv.entity.ProductClass;

@Repository
public interface ProductClassRepository extends CrudRepository<ProductClass, Long> {
    ProductClass findProductClassByValue(String value);
    ProductClass findProductClassByInternalId(Long internalId);
}
