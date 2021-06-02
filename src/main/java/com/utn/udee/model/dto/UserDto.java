package com.utn.udee.model.dto;

import com.utn.udee.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer id;
    private String dni;
    private String firstname;
    private String lastname;
    private String username;

}
