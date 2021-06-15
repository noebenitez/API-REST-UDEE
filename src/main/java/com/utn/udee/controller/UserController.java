package com.utn.udee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.udee.controller.converter.UserToUserDTOConverter;
import com.utn.udee.exception.AddressNotExistsException;
import com.utn.udee.exception.UserExistsException;
import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.Consumption;
import com.utn.udee.model.Employee;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.User;
import com.utn.udee.model.dto.*;
import com.utn.udee.model.projections.UserProjection;
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
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final UserToUserDTOConverter userDTOConverter;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper, ModelMapper modelMapper, UserToUserDTOConverter userDTOConverter) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.userDTOConverter = userDTOConverter;
        this.modelMapper = modelMapper;
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

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/addresses/{idAddress}/measurements")
    public ResponseEntity<List<MeasurementDto>> getMeasurementsAddressDateRange(@PathVariable Integer idAddress, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) throws AddressNotExistsException {
        return ResponseEntity.ok(ListMapperDto.getListDto(modelMapper, userService.getMeasurementsFromDateRange(idAddress, from, to), MeasurementDto.class));
    }


    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        User user = userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (user != null)
            return ResponseEntity.ok(LoginResponseDto.builder().token(this.generateToken(user)).build());
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/topConsumers")
    public ResponseEntity<List<UserProjection>> getTop10Consumers(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) {
        return ResponseEntity.ok(userService.getTop10Consumers(from, to));
    }


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/consumptions/{userId}")
    public ResponseEntity<List<MeasurementDto>> getRangeDateConsumption(Authentication auth, @PathVariable Integer userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) throws UserNotExistsException {
        User u = (User) auth.getPrincipal();
        User retrieved = userService.getById(userId);
        if (u.getId() == retrieved.getId()) {
            return ResponseEntity.ok(ListMapperDto.getListDto(modelMapper, userService.getRangeDateConsumption(userId, from, to), MeasurementDto.class));
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/consumptions/{userId}/total")
    public ResponseEntity<Consumption> getTotalRangeDateConsumption(Authentication auth, @PathVariable Integer userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime from, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to) throws UserNotExistsException {
        User u = (User) auth.getPrincipal();
        User retrieved = userService.getById(userId);
        if (u.getId() == retrieved.getId()) {
            return ResponseEntity.ok(userService.getTotalRangeDateConsumption(userId, from, to));
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
