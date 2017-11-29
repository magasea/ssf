package com.shellshellfish.aaas.account.service;

import com.shellshellfish.aaas.account.body.VerificationBody;

public interface RedisService {
    boolean doSmsVerification(VerificationBody vbody);
    boolean saveVeribody(VerificationBody vbody);
}
