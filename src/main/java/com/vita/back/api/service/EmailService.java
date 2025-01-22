package com.vita.back.api.service;

import com.vita.back.api.model.data.SendEmailDto;
import com.vita.back.common.exception.VitaException;

import java.util.Map;

public interface EmailService {
    public boolean sendEmail(SendEmailDto request) throws VitaException;
}