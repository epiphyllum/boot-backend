package io.boot.commons.lock;

import io.boot.commons.lock.model.User;
import io.boot.commons.lock.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest(classes = SpringBootLockTest.class)
@SpringBootApplication
public class SpringBootLockTest {
    @Resource
    UserService userService;

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLockTest.class, args);
    }

    @Test
    public void simple1Test() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable task = () -> userService.simple1();
        for (int i = 0; i < 100; i++) {
            executorService.submit(task);
        }
        Thread.sleep(10000);
    }

    @Test
    public void simple2Test() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable task = () -> userService.simple2();
        for (int i = 0; i < 100; i++) {
            executorService.submit(task);
        }
        Thread.sleep(3000);
    }

    @Test
    public void spel1Test() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable task = () -> userService.method1(new User(RANDOM.nextLong(), "BootCloud"));
        for (int i = 0; i < 100; i++) {
            executorService.submit(task);
        }
        Thread.sleep(3000);
    }

    @Test
    public void spel2Test() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable task = () -> userService.method2(new User(RANDOM.nextLong(), "BootCloud"));
        for (int i = 0; i < 100; i++) {
            executorService.submit(task);
        }
        Thread.sleep(3000);
    }
}