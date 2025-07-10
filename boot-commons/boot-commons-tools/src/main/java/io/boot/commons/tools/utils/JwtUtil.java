package io.boot.commons.tools.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import java.util.Date;

public class JwtUtil {
    private static final String KEY = "wingo@977100";

    //接收业务数据,生成token并返回
    // withClaim 配置有效载荷
    // withExpiresAt 配置过期时间
    // sign 配置加密算法和密钥
    public static String genToken(String claim, Long elapseSeconds) {
        return JWT.create()
                .withClaim("claims", claim)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * elapseSeconds))
                .sign(Algorithm.HMAC256(KEY));
    }

    //接收token,验证token,并返回业务数据
    public static String parseToken(String token) {
        Claim claims = JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims");
        return claims.asString();
    }
}