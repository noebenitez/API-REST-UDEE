package com.utn.udee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.udee.controller.converter.UserToUserDTOConverter;
import com.utn.udee.exception.AddressNotExistsException;
import com.utn.udee.exception.UserExistsException;
import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.*;
import com.utn.udee.model.dto.*;
import com.utn.udee.model.projections.ConsumptionProjection;
import com.utn.udee.model.projections.UserProjection;
import com.utn.udee.repository.MeasurementRepository;
import com.utn.udee.service.InvoiceService;
import com.utn.udee.service.UserService;
import com.utn.udee.utils.EntityURLBuilder;
import com.utn.udee.utils.ListMapperDto;
import com.utn.udee.utils.ResponseEntityMaker;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.utn.udee.utils.Constants.JWT_SECRET;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private static final String USERS_PATH = "users";

    private final UserService userService;
    private final InvoiceService invoiceService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final ConversionService conversionService;
    private final UserToUserDTOConverter userDTOConverter;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper, ModelMapper modelMapper, UserToUserDTOConverter userDTOConverter, InvoiceService invoiceService, ConversionService conversionService) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.userDTOConverter = userDTOConverter;
        this.modelMapper = modelMapper;
        this.invoiceService=invoiceService;
        this.conversionService=conversionService;
    }

    @PostMapping
    public ResponseEntity addUser(@RequestBody User user) throws UserExistsException {
        User newUser = userService.add(user);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL(USERS_PATH, newUser.getId()))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<UserDtoI>> getAll(Pageable pageable) {
        Page page = userService.getAll(pageable);
        return ResponseEntityMaker.response(page.getContent(), page);
    }




    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        User user = userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (user != null)
            return ResponseEntity.ok(LoginResponseDto.builder().token(this.generateToken(user)).build());
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /***consumptions***/

//5) Consulta 10 clientes más consumidores en un rango de fechas
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/consumptions/top")
    public ResponseEntity<List<UserProjection>> getTop10Consumers(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss") LocalDateTime from, @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss") LocalDateTime to) {
        return ResponseEntityMaker.response(userService.getTop10Consumers(from,to));
    }



    //4) Consulta de consumo por rango de fechas (el usuario va a ingresar un rango
//            de fechas y quiere saber cuánto consumió en ese periodo en Kwh y dinero)
    @PreAuthorize("hasRole('ROLE_CLIENT')" + "||   hasRole('ROLE_EMPLOYEE')" + "&& #userId == authentication.principal.id")
    @GetMapping("{userId}/consumptions")
    public ResponseEntity<ConsumptionProjection> getTotalRangeDateConsumption(@PathVariable Integer userId, @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss") LocalDateTime from, @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss") LocalDateTime to) throws UserNotExistsException {
            return ResponseEntity.ok(userService.getTotalRangeDateConsumption(userId, from, to));
    }

    /***measurements***/

    //5) Consulta de mediciones por rango de fechas
    @PreAuthorize("hasRole('ROLE_CLIENT')" + "||   hasRole('ROLE_EMPLOYEE')" + "&& #userId == authentication.principal.id")
    @GetMapping("/{userId}/measurements")
    public ResponseEntity<List<MeasurementDto>> getRangeDateConsumption(@PathVariable Integer userId, @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss") LocalDateTime from, @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss") LocalDateTime to) throws UserNotExistsException {

            return ResponseEntityMaker.response(ListMapperDto.getListDto(modelMapper, userService.getRangeDateConsumption(userId, from, to), MeasurementDto.class));
    }

    //6) Consulta de mediciones de un domicilio por rango de fechas
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/measurements/addresses/{idAddress}")
    public ResponseEntity<List<MeasurementDto>> getMeasurementsAddressDateRange(@PathVariable Integer idAddress, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) throws AddressNotExistsException {
        return ResponseEntityMaker.response(ListMapperDto.getListDto(modelMapper, userService.getMeasurementsFromDateRange(idAddress, from, to), MeasurementDto.class));
    }




    /**** Invoices ****/


//2) Consulta de facturas por rango de fechas
    @PreAuthorize("hasRole('ROLE_CLIENT')" + "||   hasRole('ROLE_EMPLOYEE') " + "|| #userId == authentication.principal.id")
    @GetMapping("/invoices/{userId}")
    public ResponseEntity<List<InvoiceDto>> getInvoicesByRangeDate(@PathVariable Integer userId, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) throws UserNotExistsException {
            return ResponseEntityMaker.response(invoiceService.getInvoicesByRangeDate(userId, from, to));
    }


//3) Consulta de deuda (Facturas impagas)
@PreAuthorize("hasRole('ROLE_CLIENT')" + "||   hasRole('ROLE_EMPLOYEE') " + "&& #userId == authentication.principal.id")
@GetMapping("/invoices/{userId}/unpaid")
public ResponseEntity<Page<InvoiceDto>> getUnpaidInvoices(@PathVariable Integer userId,Pageable p) throws UserNotExistsException {
    Page<InvoiceDto> page = (invoiceService.getUnpaidInvoices(userId,p)).map(invoice -> conversionService.convert(invoice, InvoiceDto.class));

        return ResponseEntityMaker.response(page.getContent(),page);
}




//4 Consulta de facturas impagas por cliente y domicilio
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@GetMapping("/invoices/{userId}/addresses/{addressId}")
public ResponseEntity<Page<InvoiceDto>> getUnpaidInvoicesByUserAndAddress(@PathVariable Integer userId, @PathVariable Integer addressId,Pageable pageable) throws UserNotExistsException, AddressNotExistsException {
    Page p = invoiceService.getUnpaidInvoicesByUserAndAddress(userId,addressId,pageable);
    Page to = p.map(invoice -> conversionService.convert(invoice, InvoiceDto.class));
   return ResponseEntityMaker.response(to.getContent(),to);
}

/*
    @GetMapping(value = "/userDetails")
    public ResponseEntity<User> userDetails(Authentication auth) {
        return ResponseEntity.ok((User) auth.getPrincipal());
    }
*/




    private String generateToken(User user) {
        try{
            String authority;
            if (user instanceof Employee)
                authority = "ROLE_EMPLOYEE";
            else
                authority = "ROLE_CLIENT";

            UserDto userDto = userDTOConverter.convert(user);
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
            String token = Jwts
                    .builder()
                    .setId("JWT")
                    .setSubject(userDto.getUsername())
                    .claim("user", objectMapper.writeValueAsString(userDto))
                    .claim("authorities",grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000000))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();
            return  token;
        } catch(Exception e) {
            return "asd";
        }
    }
}
