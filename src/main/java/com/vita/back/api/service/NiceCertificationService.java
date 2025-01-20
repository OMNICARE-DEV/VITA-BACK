package com.vita.back.api.service;

import com.vita.back.api.model.request.NiceCertificateAuthRequest;
import com.vita.back.api.model.request.NiceCertificationRequest;
import com.vita.back.api.model.request.RegKeyRequest;
import com.vita.back.api.model.response.NiceCertificateAuthResponse;
import com.vita.back.api.model.response.NiceCirtificationResponse;
import com.vita.back.api.model.response.RegKeyResponse;
import com.vita.back.common.exception.VitaException;

public interface NiceCertificationService {
    public NiceCirtificationResponse userJoinCertification(NiceCertificationRequest request) throws VitaException;

    public RegKeyResponse selfAuthRegKey(RegKeyRequest request) throws VitaException;

    public NiceCertificateAuthResponse userJoinCertificateAuth(NiceCertificateAuthRequest request) throws VitaException;
}
