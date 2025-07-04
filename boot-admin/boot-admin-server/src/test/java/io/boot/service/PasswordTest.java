package io.boot.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.boot.commons.tools.redis.RedisUtils;
import io.boot.entity.SysRoleEntity;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTest {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private RedisUtils redisUtils;

    @Test
    public void encode() {
        String password = "123456";
        password = passwordEncoder.encode(password);

        System.out.println(password);

        JSONObject root = JSON.parseObject("kkkkk");
        JSONObject data = root.getJSONObject("data");
        String table = root.getString("table");
        SysRoleEntity javaObject = data.toJavaObject(SysRoleEntity.class);
        redisUtils.hSet(table, javaObject.getId().toString(), javaObject);
    }

}
