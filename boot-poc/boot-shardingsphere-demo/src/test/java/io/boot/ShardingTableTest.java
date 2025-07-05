package io.boot;

import io.boot.dao.UserLogDao;
import io.boot.entity.UserLogEntity;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * 测试 ShardingSphere 分表
 *
 * @author epiphyllum.zhou@gmail.com
 */
@SpringBootTest
public class ShardingTableTest {
    @Resource
    private UserLogDao userLogDao;

    @Test
    public void shardingTest() {
        UserLogEntity log = new UserLogEntity();
        //log.setId(1L);
        log.setName("test");
        log.setContent("测试");
        log.setCreateTime(LocalDateTime.now());

        userLogDao.insert(log);
    }
}
