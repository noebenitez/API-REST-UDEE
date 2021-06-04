package com.utn.udee.repository;

import com.utn.udee.model.User;
import com.utn.udee.model.dto.UserDtoI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String username, String password);

    boolean existsByDni(String dni);

    @Query(value = "SELECT * from users",
            countQuery = "select count(*) from users",
            nativeQuery = true)
    Page<UserDtoI> findAllDto(Pageable pageable);

}
