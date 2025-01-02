package com.hops.hops_new_api.common.service;

import com.hops.hops_new_api.common.exception.HopsException;
import com.hops.hops_new_api.common.model.Request.NiceCertificationRequest;
import com.hops.hops_new_api.common.model.Request.RegKeyRequest;
import com.hops.hops_new_api.common.model.Response.NiceCirtificationResponse;
import com.hops.hops_new_api.common.model.Response.RegKeyResponse;

public interface NiceCertificationService {
    public NiceCirtificationResponse userJoinCertification(NiceCertificationRequest request) throws HopsException;

    public RegKeyResponse selfAuthRegKey(RegKeyRequest request) throws HopsException;
}
