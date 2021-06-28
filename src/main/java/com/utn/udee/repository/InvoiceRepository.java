package com.utn.udee.repository;

import com.utn.udee.model.Invoice;
import com.utn.udee.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query(value = "SELECT i.* from invoices i " +
            "inner join users u on u.id = i.id_customer " +
            "where u.id = :userId and i.final_datetime between :from and :to " +
            "order by i.final_datetime asc", nativeQuery = true)
    List<Invoice> getInvoicesByRangeDate(@Param("userId")Integer userId, @Param("from")LocalDateTime from, @Param("to")LocalDateTime to);

    @Query(value="SELECT i.* from invoices i " +
            "inner join users u on i.id_customer = u.id " +
            "where u.id = :userId and i.payment_status LIKE 'UNPAID'",
            countQuery= "SELECT count(i.*) from invoices i " +
            "inner join users u on i.id_customer = u.id " +
            "where u.id = :userId and i.payment_status LIKE 'UNPAID'",nativeQuery = true)
    Page<Invoice> findAllByBilledToAndNotPaid(@Param("userId") Integer userId, Pageable pageable);


    @Query(value = "SELECT i.* from invoices i " +
            "inner join users u on u.id = i.id_customer " +
            "inner join addresses a on u.id = a.id_customer " +
            "where u.id = :userId and a.id = :addressId and i.payment_status LIKE 'UNPAID' " +
            "order by i.initial_datetime",
            countQuery = "SELECT count(i.*) from invoices i " +
            "inner join users u on u.id = i.id_customer " +
            "inner join addresses a on u.id = a.id_customer " +
            "where u.id = :userId and a.id = :addressId and i.payment_status LIKE 'UNPAID' " +
            "order by i.initial_datetime",nativeQuery = true)
    Page<Invoice> FindUnpaidInvoicesByUserAndAddress(@Param("userId")Integer userId,@Param("addressId") Integer addressId, Pageable pageable);
}
