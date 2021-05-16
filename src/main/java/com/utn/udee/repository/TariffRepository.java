package com.utn.udee.repository;

import com.utn.udee.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Integer, Tariff> {
}
