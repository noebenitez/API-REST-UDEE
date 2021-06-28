package com.utn.udee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto{

    Integer id;
    String dni;
    String firstname;
    String lastname;
    String username;

}
