package com.utn.udee.model.dto;

import com.utn.udee.model.Client;
import com.utn.udee.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {
    String dni;
    String firstname;
    String lastname;


    public static ClientDto getClientDto(Client client) {
        return ClientDto.builder().dni(client.getDni()).firstname(client.getFirstname()).lastname(client.getLastname()).build();
    }

}
