package com.utn.udee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.udee.exception.UserExistsException;
import com.utn.udee.model.User;
import com.utn.udee.model.dto.LoginRequestDto;
import com.utn.udee.model.dto.LoginResponseDto;
import com.utn.udee.model.dto.UserDto;
import com.utn.udee.service.UserService;
import com.utn.udee.utils.EntityURLBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.EntityInstantiators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.utn.udee.utils.Constants.JWT_SECRET;

@Slf4j
@RestController
@RequestMapping(value = "api/users")
public class UserController {

    private static final String USERS_PATH = "users";
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity addUser(@RequestBody User user) throws UserExistsException {
        User newUser = userService.add(user);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL(USERS_PATH, newUser.getId()))
                .build();
    }

    @GetMapping
    public List<User> getAll(){     //Se va a cambiar a dto
        return userService.getAll();
    }

    @PostMapping(value = "login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info(loginRequestDto.toString());
        User user = userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (user!=null){
            UserDto dto = modelMapper.map(user, UserDto.class);
            return ResponseEntity.ok(LoginResponseDto.builder().token(this.generateToken(dto)).build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping(value = "/userDetails")
    public ResponseEntity<User> userDetails(Authentication auth) {
        return ResponseEntity.ok((User) auth.getPrincipal());
    }


    private String generateToken(UserDto userDto) {
        try {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("DEFAULT_USER");
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
            return "dummy";
        }



    }
}
