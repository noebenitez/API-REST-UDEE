package com.utn.udee.repository;

import com.utn.udee.model.Client;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.projections.ConsumptionProjection;
import com.utn.udee.model.projections.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {


    Page<Measurement> findMeasurementByMeterId(Integer meterId, Pageable p);

    @Query(value="SELECT m.* from measurements m " +
            "inner join meters t on t.id = m.id_meter " +
            "where t.id_address = :idAddress and (m.m_datetime between :from and :to) " +
            "order by m.m_datetime ASC",nativeQuery = true)
    List<Measurement> getByAddressAndRangeDate(@Param("idAddress") Integer idAddress, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);


    @Query(value="SELECT u.*, sum(m.measurement) as sum from users u " +
                 "inner join addresses a on a.id_customer = u.id " +
                 "inner join meters me on me.id_address = a.id " +
                 "inner join measurements m on m.id_meter = me.id " +
                 "where m.m_datetime between :from and :to " +
                 "group by(u.dni) " +
                 "order by sum DESC " +
                 "LIMIT 10", nativeQuery = true)
    List<UserProjection> getTop10Consumers(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value="SELECT m.* from measurements m " +
                 "inner join meters me on m.id_meter = me.id " +
                 "inner join addresses a on a.id_customer = :idUser " +
                 "where (me.id_address = a.id) and (m.m_datetime between :from and :to)", nativeQuery = true )
    List<Measurement> getConsumptionByRangeDate(@Param("idUser") Integer idUser,@Param("from") LocalDateTime from,@Param("to") LocalDateTime to);


    @Query(value="SELECT sum(m.measurement) as totalKw,sum(m.measurement*m.price) as totalAmount from measurements m " +
            "inner join meters me on m.id_meter = me.id " +
            "inner join addresses a on a.id_customer = :idUser " +
            "where (me.id_address = a.id) and (m.m_datetime between :from and :to)", nativeQuery = true )
    ConsumptionProjection getTotalConsumptionByRangeDate(@Param("idUser") Integer idUser, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);



//    @Query(value="SELECT m.* from measurements m" +
//            "inner join meters mt on mt.id = m.id_meter" +
//            "inner join addresses a on a.id = mt.id_address" +
//            "where a.id = :addressId",
//            countQuery = "SELECT count(m.*) from measurements m" +
//                    "inner join meters mt on mt.id = m.id_meter" +
//                    "inner join addresses a on a.id = mt.id_address" +
//                    "where a.id = :addressId",nativeQuery = true)
//    Page<Measurement> findAllMeasurementsByAddressId(Integer addressId, Pageable pageable);
}

