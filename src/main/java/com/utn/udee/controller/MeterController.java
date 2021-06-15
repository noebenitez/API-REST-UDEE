package com.utn.udee.controller;

import com.utn.udee.exception.MeasurementNotExistsException;
import com.utn.udee.exception.MeterNotExistsException;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.Meter;
import com.utn.udee.model.dto.MeasurementDto;
import com.utn.udee.model.dto.MeterDto;
import com.utn.udee.service.MeterService;
import com.utn.udee.utils.ResponseEntityMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/meters")
public class MeterController {
    private MeterService meterService;
    @Autowired
    private ConversionService conversionService; //interfaz del metodo convert, le pasamos source y target
    ///por cada converter que tenemos o cada implementacion de converter
    ///tengo un converter lo almacena., donde convierte personas a personasdto
    ///una capa mas arriba me provee la interfaz que es la interfaz del conversionservice
    ///es una unica instancia
    ///se encarga de ir a buscar los converters/

    @Autowired
    public MeterController(MeterService meterService)
    {
        this.meterService=meterService;
    }


    @PostMapping
    public ResponseEntity addMeter(@RequestBody Meter meter)
    {
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

    @GetMapping("/")
    public ResponseEntity<Page<Meter>> getAll(Pageable pageable)
    {
        Page p = meterService.getAll(pageable);
        return ResponseEntityMaker.response(p.getContent(),p);
    }

    @PutMapping("/{idMeter}/measurements/{idMeasurement}")
    public ResponseEntity addMeasurementToMeter(@PathVariable Integer idMeter, @PathVariable Integer idMeasurement) throws MeasurementNotExistsException, MeterNotExistsException {
        meterService.addMeasurementToMeter(idMeter,idMeasurement);
        return ResponseEntity.ok().build();
    }
    ///paginar???
    @GetMapping("/{idMeter}/measurements")
    public ResponseEntity<Page<Measurement>> getMeasurementsByMeter(@PathVariable Integer idMeter, Pageable pageable)
    {
        Page p = meterService.getMeasurementsByMeter(idMeter,pageable);
       return response(p);
    }

    @DeleteMapping("/{idMeter}")
    public ResponseEntity deleteById(@PathVariable Integer idMeter)
    {
        meterService.deleteById(idMeter);
        return ResponseEntity.ok().build();
    }
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
private ResponseEntity response(Page page) {
    HttpStatus httpStatus = page.getContent().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
    return ResponseEntity.
            status(httpStatus).
            header("X-Total-Count", Long.toString(page.getTotalElements())).
            header("X-Total-Pages", Long.toString(page.getTotalPages())).
            body(page.getContent());

}

}
