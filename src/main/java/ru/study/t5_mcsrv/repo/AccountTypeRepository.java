package ru.study.t5_mcsrv.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.study.t5_mcsrv.entity.AccountType;

@Repository
public interface AccountTypeRepository extends CrudRepository<AccountType, Long> {
}
