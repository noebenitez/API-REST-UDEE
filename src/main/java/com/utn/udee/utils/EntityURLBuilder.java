package com.henry.demo.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class EntityURLBuilder {

    public static String buildURL(final String entity, final String id) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(("/{entity}/{id}"))
                .buildAndExpand(entity, id)
                .toUriString();
    }
}
