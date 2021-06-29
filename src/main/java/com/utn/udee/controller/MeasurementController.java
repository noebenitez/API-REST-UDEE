package com.utn.udee.controller;

import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.Meter;
import com.utn.udee.model.dto.InvoiceDto;
import com.utn.udee.model.dto.MeasurementDto;
import com.utn.udee.model.dto.MeasurementSenderDto;
import com.utn.udee.model.dto.MeterDto;
import com.utn.udee.service.MeasurementService;
import com.utn.udee.service.MeterService;
import com.utn.udee.utils.EntityURLBuilder;
import com.utn.udee.utils.ResponseEntityMaker;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/measurements")
public class MeasurementController {
    private static final String MEASUREMENTS_PATH = "measurements";
    private MeasurementService measurementService;
    private MeterService meterService;
    private ConversionService conversionService;
    private ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, MeterService meterService,ConversionService conversionService,ModelMapper modelMapper)
    {
        this.measurementService = measurementService;
        this.meterService=meterService;
        this.conversionService = conversionService;
        this.modelMapper = modelMapper;
    }
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<MeasurementDto> getById(@PathVariable Integer id) throws MeasurementNotExistsException
    {
         Measurement m = measurementService.getById(id);
        return ResponseEntity.ok(modelMapper.map(m,MeasurementDto.class));
    }

    @PostMapping
    public ResponseEntity addMeasurement(@RequestBody @Valid MeasurementSenderDto measurement) throws MeterNotExistsException {
        Meter meter = meterService.getbySerialNumber(measurement.getSerialNumber());
        Measurement m = measurementService.add(measurement,meter);
        return ResponseEntity.created(EntityURLBuilder.buildURL(MEASUREMENTS_PATH,m.getId())).build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("")
    public ResponseEntity<List<MeasurementDto>> getAll(Pageable pageable)
    {
        Page p = measurementService.getAllMeasurements(pageable);
        Page to= p.map(measurement -> conversionService.convert(measurement, MeasurementDto.class));
        return ResponseEntityMaker.response(to.getContent(),to);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Integer id)
    {
        measurementService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    /*@PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody Measurement measurement)
    {

    }*/

//    @GetMapping("/")
//    public ResponseEntity<List<MeasurementDto>> getTop10Consumers(@RequestParam @DateTimeFormat(pattern="yyyy-mm-dd") LocalDateTime from,
//                                                                  @RequestParam @DateTimeFormat(pattern="yyyy-mm-dd") LocalDateTime till)
//    {
//        measurementService.getTop10Consumers(from,till);
//    }



}
