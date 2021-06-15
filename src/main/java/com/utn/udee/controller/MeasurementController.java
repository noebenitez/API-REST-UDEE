package com.utn.udee.controller;

import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.dto.MeasurementDto;
import com.utn.udee.model.dto.MeterDto;
import com.utn.udee.service.MeasurementService;
import com.utn.udee.utils.EntityURLBuilder;
import com.utn.udee.utils.ResponseEntityMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/measurements")
public class MeasurementController {
    private static final String MEASUREMENTS_PATH = "measurements";
    private MeasurementService measurementService;
    @Autowired
    public MeasurementController(MeasurementService measurementService)
    {
        this.measurementService = measurementService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementDto> getById(@PathVariable Integer id) throws MeasurementNotExistsException
    {
         Measurement m = measurementService.getById(id);
        return ResponseEntity.ok(MeasurementDto.getMeasurementDto(m));
    }

    @PostMapping
    public ResponseEntity addMeasurement(@RequestBody Measurement measurement)
    {
        Measurement m = measurementService.add(measurement);
        return ResponseEntity.created(EntityURLBuilder.buildURL(MEASUREMENTS_PATH,m.getId())).build();
    }

    @GetMapping("")
    public ResponseEntity<Page<Measurement>> getAll(Pageable pageable)
    {
        Page p = measurementService.getAllMeasurements(pageable);
        return ResponseEntityMaker.response(p.getContent(),p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Integer id)
    {
        measurementService.deleteById(id);
        return ResponseEntity.ok().build();
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
