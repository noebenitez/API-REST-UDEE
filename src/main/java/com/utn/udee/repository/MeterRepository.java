package com.utn.udee.repository;

import com.utn.udee.model.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Integer> {
    Meter findMeterBySerialNumber(String serialNumber);

    Optional<Meter> findMeterByAddress_Id(Integer id);
}
