package io.boot.api.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.ByteUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ApiHandler {
    private final String clientId;
    private final Sign signer;
    private final Sign verifier;
    private final ObjectMapper objectMapper;

    public ApiHandler(String clientId, String clientKey, String privateKey, ObjectMapper objectMapper) {
        this.clientId = clientId;
        this.objectMapper = objectMapper;

        RSA rsaSigner = new RSA(privateKey, null);
        this.signer = SecureUtil.sign(SignAlgorithm.SHA512withRSA);
        this.signer.setPrivateKey(rsaSigner.getPrivateKey());

        RSA rsaVerifier = new RSA(null, clientKey);
        this.verifier = SecureUtil.sign(SignAlgorithm.SHA512withRSA);
        this.verifier.setPublicKey(rsaVerifier.getPublicKey());

    }

    public void verify(String body, String apiName, String reqId, String sign) {


    }

}
