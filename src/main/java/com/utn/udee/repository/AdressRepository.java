package com.utn.udee.repository;

import com.utn.udee.model.Adress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdressRepository extends JpaRepository<Adress, Integer> {
    boolean existsByAdress(String adress);

    Page<Adress> findAll(Pageable pageable);
}
