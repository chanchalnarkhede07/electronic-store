package com.golu.electronic.store.exceptions;

public class BadApiRequestException extends RuntimeException{

    public BadApiRequestException(String msg){
        super(msg);
    }

    public BadApiRequestException(){
        super();
    }
}
