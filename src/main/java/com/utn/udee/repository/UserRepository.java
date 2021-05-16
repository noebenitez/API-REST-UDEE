package com.utn.udee.repository;

import com.utn.udee.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Integer, User> {
}
