package com.utn.udee.exception;

import org.springframework.http.HttpStatus;

public class AddressNotExistsException extends GenericWebException {

    public AddressNotExistsException(){
        this.status = HttpStatus.NOT_FOUND;
        this.code = "05";
    }

    public String getMessage(){
        return "Address doesn't exists.";
    }
}
