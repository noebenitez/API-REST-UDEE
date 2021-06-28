package com.utn.udee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "Invalid entry. The username cannot be null or whitespace.")
    String username;
    @NotBlank(message = "Invalid entry. The password cannot be null or whitespace.")
    String password;
}
