package com.utn.udee.service;

import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.model.Client;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.Meter;
import com.utn.udee.model.dto.MeasurementSenderDto;
import com.utn.udee.model.projections.ConsumptionProjection;
import com.utn.udee.model.projections.UserProjection;
import com.utn.udee.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository)
    {
        this.measurementRepository=measurementRepository;
    }

    public Measurement getById(Integer id) throws MeasurementNotExistsException {
        return measurementRepository.findById(id).orElseThrow(MeasurementNotExistsException::new);
    }

    public Measurement add(MeasurementSenderDto measurement, Meter meter) throws MeterNotExistsException {

        Measurement newMeasurement = Measurement.builder().datetime(LocalDateTime.parse(measurement.getDate())).measurement(measurement.getValue()).price(meter.getAddress().getTariff().getTariff()*measurement.getValue()).meter(meter).build();
        Measurement m  = measurementRepository.save(newMeasurement);
        return m;
    }


    public Page<Measurement> getMeasurementsByMeter(Integer idMeter, Pageable p) {
        return measurementRepository.findMeasurementByMeterId(idMeter, p);
    }

    public Page<Measurement> getAllMeasurements(Pageable pageable) {
        return measurementRepository.findAll(pageable);
    }

    public void deleteById(Integer id) {

        measurementRepository.deleteById(id);
    }
//
//    public List<User> getTop10Consumers(LocalDateTime from, LocalDateTime to) {
//        return measurementRepository.getTop10Consumers(from,to);
//
//    }
    ///id meter comes from user, validating address existence and idmeter existence prior calling this method
    public List<Measurement> getMeasurementsByAddressRangeDate(Integer idAddress, LocalDateTime from, LocalDateTime to)
    {
        return measurementRepository.getByAddressAndRangeDate(idAddress,from,to);
    }


    public List<UserProjection> getTop10Consumers(LocalDateTime from, LocalDateTime to) {
        return measurementRepository.getTop10Consumers(from,to);
    }

    public List<Measurement> getRangeDateConsumption(Integer userId, LocalDateTime from, LocalDateTime to) {
          return measurementRepository.getConsumptionByRangeDate(userId,from,to);
    }
    public ConsumptionProjection getTotalRangeDateConsumption(Integer userId, LocalDateTime from, LocalDateTime to) {

        return measurementRepository.getTotalConsumptionByRangeDate(userId,from,to);
    }

/*    public Page<Measurement> getAllMeasurementsByAddressId(Integer addressId, Pageable pageable) {
      return  measurementRepository.findAllMeasurementsByAddressId(addressId,pageable);
    }*/
}
