package ru.study.t5_mcsrv.repo;

import org.springframework.data.repository.CrudRepository;
import ru.study.t5_mcsrv.entity.AccountPool;

public interface AccountPoolRepository extends CrudRepository<AccountPool, Long> {
    AccountPool findAccountPoolByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
            String branchCode, String currencyCode, String mdmCode, String priorityCode, String registerTypeCode
    );
}
