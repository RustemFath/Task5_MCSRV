package ru.study.t5_mcsrv.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.study.t5_mcsrv.entity.Agreement;

import java.util.List;

@Repository
public interface AgreementRepository extends CrudRepository<Agreement, Long> {
    List<Agreement> findAgreementsByNumber(String number);
}
