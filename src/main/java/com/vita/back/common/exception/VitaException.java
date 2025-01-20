package com.vita.back.common.exception;

import lombok.Getter;

@Getter
public class VitaException extends Exception {
    
    private static final long serialVersionUID = -4864185459566844601L;

    private String code;
    private String message;
    
    public VitaException(VitaCode bc) {
        this.code = bc.getCode();
        this.message = bc.getMessage();
    }
    
    public VitaException(VitaCode bc, String message) {
        this.code = bc.getCode();
        this.message = message;
    }
    
    public VitaException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
