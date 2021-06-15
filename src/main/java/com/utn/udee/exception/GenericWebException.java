package com.utn.udee.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class GenericWebException extends Exception{

    HttpStatus status;
    String code;
}
