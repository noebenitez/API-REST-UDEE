package com.utn.udee.repository;

import com.utn.udee.model.Tariff;
import com.utn.udee.model.TariffType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Integer> {

    Page<Tariff> findAll(Pageable pageable);

    boolean existsTariffByTariffAndTariffType(Float tariff, TariffType tariffType);
}
