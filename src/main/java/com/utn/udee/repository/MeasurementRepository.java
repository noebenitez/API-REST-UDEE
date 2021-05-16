package com.utn.udee.repository;

import com.utn.udee.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends JpaRepository<Integer, Measurement> {
}
