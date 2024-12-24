package com.hops.hops_new_api.common.exception;

import lombok.Getter;

@Getter
public class HopsException extends Exception {
    
    private static final long serialVersionUID = -4864185459566844601L;

    private String code;
    private String message;
    
    public HopsException(HopsCode bc) {
        this.code = bc.getCode();
        this.message = bc.getMessage();
    }
    
    public HopsException(HopsCode bc, String message) {
        this.code = bc.getCode();
        this.message = message;
    }
    
    public HopsException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
