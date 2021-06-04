package com.utn.udee.utils;

import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@NoArgsConstructor
public class EntityURLBuilder {

    public static URI buildURL(final String entity, final Integer id){    //Url a la entity que se da de alta

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(("/{entity}/{id}"))
                .buildAndExpand(entity, id)
                .toUri();
    }
}
