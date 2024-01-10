package ru.study.t5_mcsrv.repo;

import org.springframework.data.repository.CrudRepository;
import ru.study.t5_mcsrv.entity.AccountNumber;

import java.util.List;

public interface AccountNumberRepository extends CrudRepository<AccountNumber, Long> {
    List<AccountNumber> findAccountNumbersByAccountPoolIdOrderById(Long accountPoolId);
}
