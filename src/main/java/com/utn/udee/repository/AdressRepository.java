package com.utn.udee.repository;

import com.utn.udee.model.Adress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdressRepository extends JpaRepository<Integer, Adress> {
}
