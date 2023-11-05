package com.lcwd.electronic.store.exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFoundExceptions extends RuntimeException {

    public ResourceNotFoundExceptions(){
        super("Resource not found!!");
    }

    public ResourceNotFoundExceptions(String message){
        super(message);
    }
}
