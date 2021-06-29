package com.utn.udee.service;

import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.exception.MeterSameAddressExistsException;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.Meter;
import com.utn.udee.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeterService {

    private final MeterRepository meterRepository;
//    private MeasurementService measurementService;

    @Autowired
    public MeterService(MeterRepository meterRepository)
    {
        this.meterRepository=meterRepository;
    }

    public Meter add(Meter meter) throws MeterSameAddressExistsException {
        if (meterRepository.findMeterByAddress_Id(meter.getAddress().getId()).isPresent())
            throw new MeterSameAddressExistsException();
        return meterRepository.save(meter);
 }

    public Meter getById(Integer id) throws MeterNotExistsException {
        return meterRepository.findById(id).orElseThrow(MeterNotExistsException::new);
    }


    public Page<Meter> getAll(Pageable pageable)
    {
        return meterRepository.findAll(pageable);
    }

    public void addMeasurementToMeter(Integer idMeter, Measurement m) throws MeasurementNotExistsException, MeterNotExistsException {
//        Measurement m = measurementService.getById(idMeasurement);
            Meter meter = meterRepository.findById(idMeter).orElseThrow(MeterNotExistsException::new);
            meter.getMeasurements().add(m);
            meterRepository.save(meter);
    }

//    public Page<Measurement> getMeasurementsByMeter(Integer idMeter,Pageable p) {
//        return measurementService.findMeasurementsByMeter(idMeter,p);
//        /// return measurementService.findMeasurementsByMeter(idMeter).stream().map(m -> MeasurementDto.builder().id(m.getId()).measurement(m.getMeasurement()).price(m.getPrice()).datetime(m.getDatetime()).invoice(m.getInvoice()).build()).collect(Collectors.toList());
//    }

    public void deleteById(Integer idMeter) throws MeterNotExistsException {

        meterRepository.findById(idMeter).orElseThrow(MeterNotExistsException::new);
        meterRepository.deleteById(idMeter);
    }

    public void updateMeter(Integer idMeter, Meter meter) throws MeterNotExistsException {
        Meter m = meterRepository.findById(idMeter).orElseThrow(MeterNotExistsException::new);
        m.setBrand(meter.getBrand());
        m.setModel(meter.getModel());
        meterRepository.save(m);

    }

    public Meter getbySerialNumber(String serialNumber) throws MeterNotExistsException {
       Meter m  = meterRepository.findMeterBySerialNumber(serialNumber);
       if (m != null) return m;
       else throw new MeterNotExistsException();

    }

}