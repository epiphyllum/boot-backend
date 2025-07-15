package io.boot.api.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.ByteUtil;
import io.boot.commons.tools.utils.CommonUtils;
import io.boot.commons.tools.utils.IpUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ApiContext {
    private String appId;
    private String apiName;
    private String requestId;
    private String sign;
    private String body;

    // clientPublic    :
    // platformPublic
    // platformPrivate
    // IP白名单
    private String clientEntity;

    public void verify() {
        String clientPublicKey = "";  // 来自clientEntity;
        // 准备签名验证器
        RSA rsa = new RSA(null, clientPublicKey);
        Sign verifier = SecureUtil.sign(SignAlgorithm.SHA512withRSA);
        verifier.setPublicKey(rsa.getPublicKey());

        // 验证签名
        String toSign = requestId + appId + apiName + body;
        byte[] bytes = DigestUtil.sha256(toSign);
        if (!verifier.verify(bytes, ByteUtil.hextobyte(sign))) {
            log.error("验证签名失败, 代签名串[{}]\nsign=[{}]\nreqId=[{}]", toSign, sign, requestId);
            throw new BootException("签名验证失败");
        }
    }

    // 白名单检查
    public void checkIp() {
        String ip = CommonUtils.getIp();
        if (false) {
            throw new BootException(ip + "不在白名单内");
        }
    }

}
