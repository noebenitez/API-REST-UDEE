package com.utn.udee.service;

import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.Meter;
import com.utn.udee.model.dto.MeasurementDto;
import com.utn.udee.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeterService {

    private final MeterRepository meterRepository;
    private final MeasurementService measurementService;

    @Autowired
    public MeterService(MeterRepository meterRepository, MeasurementService measurementService)
    {
        this.meterRepository=meterRepository;
        this.measurementService=measurementService;
    }

    public Meter add(Meter meter) {
        Meter m =  meterRepository.save(meter);
        return m;
 }

    public Meter getById(Integer id) throws MeterNotExistsException {
        return meterRepository.findById(id).orElseThrow(MeterNotExistsException::new);
    }
/*///ver sort
    public PaginationResponse<Meter> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Meter> meterPage = meterRepository.findAll(pageable); ///me devolvera una page de meter
        return new PaginationResponse<>(meterPage.getContent(),meterPage.getTotalPages(),meterPage.getTotalElements());
    }*/

    public Page<Meter> getAll(Pageable pageable)
    {
        return meterRepository.findAll(pageable);
    }

    public void addMeasurementToMeter(Integer idMeter, Integer idMeasurement) throws MeasurementNotExistsException, MeterNotExistsException {
            Measurement m = measurementService.getById(idMeasurement);
            Meter meter = meterRepository.findById(idMeter).orElseThrow(MeterNotExistsException::new);
            meter.getMeasurements().add(m);
            meterRepository.save(meter);
    }

    public Page<Measurement> getMeasurementsByMeter(Integer idMeter,Pageable p) {
        return measurementService.findMeasurementsByMeter(idMeter,p);
        /// return measurementService.findMeasurementsByMeter(idMeter).stream().map(m -> MeasurementDto.builder().id(m.getId()).measurement(m.getMeasurement()).price(m.getPrice()).datetime(m.getDatetime()).invoice(m.getInvoice()).build()).collect(Collectors.toList());
    }

    public void deleteById(Integer idMeter) {
        meterRepository.deleteById(idMeter);
    }

    public void updateMeter(Integer idMeter, Meter meter) throws MeterNotExistsException {
        Meter m = meterRepository.findById(idMeter).orElseThrow(MeterNotExistsException::new);

    }
}