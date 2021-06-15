package com.utn.udee.repository;

import com.utn.udee.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    boolean existsByAddress(String address);

    Page<Address> findAll(Pageable pageable);
}