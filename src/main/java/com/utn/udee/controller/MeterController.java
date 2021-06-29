package com.utn.udee.controller;

import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.exception.MeterSameAddressExistsException;
import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.Meter;
import com.utn.udee.model.dto.MeterDto;
import com.utn.udee.service.MeasurementService;
import com.utn.udee.service.MeterService;
import com.utn.udee.utils.ResponseEntityMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Controller
@RequestMapping("/meters")
public class MeterController {
    private MeterService meterService;
    private MeasurementService measurementService;
    @Autowired
    private ConversionService conversionService;

    @Autowired
    public MeterController(MeterService meterService, MeasurementService measurementService)
    {
        this.meterService=meterService;
        this.measurementService=measurementService;
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping
    public ResponseEntity addMeter(@RequestBody Meter meter) throws MeterSameAddressExistsException {
        meter.setSerialNumber(UUID.randomUUID().toString());
        Meter m =  meterService.add(meter);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(m.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

  /*  @GetMapping("/{id}")
    public Meter getById(@PathVariable Integer id)
    {
        return meterService.getById(id);
    }

*/
     @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<MeterDto> getMeterDtoById(@PathVariable Integer id) throws MeterNotExistsException {
        Meter m = meterService.getById(id);
        return ResponseEntity.ok(MeterDto.builder().serialNumber(m.getSerialNumber()).brand(m.getBrand()).model(m.getModel()).build());
    }

/*    ///requestParam is ?
    @GetMapping("/")
    public PaginationResponse<Meter> getAll(@RequestParam(value = "size",defaultValue="20") Integer size,
                                            @RequestParam(value = "page", defaultValue= "0") Integer page)
    {
        return meterService.getAll(page,size);
    }*/
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("")
    public ResponseEntity<Page<Meter>> getAll(Pageable pageable)
    {
        Page p = meterService.getAll(pageable);
        return ResponseEntityMaker.response(p.getContent(),p);
    }

    @PutMapping("/{idMeter}/measurements/{idMeasurement}")
    public ResponseEntity addMeasurementToMeter(@PathVariable Integer idMeter, @PathVariable Integer idMeasurement) throws MeasurementNotExistsException, MeterNotExistsException {
        Measurement m = measurementService.getById(idMeasurement);
        meterService.addMeasurementToMeter(idMeter,m);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{idMeter}/measurements")
    public ResponseEntity<Page<Measurement>> getMeasurementsByMeter(@PathVariable Integer idMeter, Pageable pageable)
    {
        Page<Measurement> p = measurementService.getMeasurementsByMeter(idMeter,pageable);
       return ResponseEntityMaker.response(p.getContent(),p);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{idMeter}")
    public ResponseEntity deleteById(@PathVariable Integer idMeter) throws MeterNotExistsException {
        meterService.deleteById(idMeter);
        return ResponseEntity.accepted().build();
    }
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/{idMeter}")
    public ResponseEntity updateMeter(@PathVariable Integer idMeter, @RequestBody Meter meter) throws MeterNotExistsException {
        meterService.updateMeter(idMeter,meter);
        return ResponseEntity.ok().build();
    }


/*    @GetMapping("/{idMeter}/measurements")
    public ResponseEntity<List<Measurement>> getMeasurementsByMeter(@PathVariable Integer idMeter, Pageable pageable){
       Page p =  meterService.getMeasurementsByMeter(idMeter,pageable);
        return ResponseEntityMaker.response(p.getContent(),p);
    }*/


}
