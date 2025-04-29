package com.example.dependency.qualifier;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Slf4j
public class QualifierTest {

    @Autowired
    @Qualifier("laptop")
    Computer laptop;

    @Autowired
    @Qualifier("desktop")
    Computer desktop;

    @Autowired
    Computer computer; // @Primary 붙은 laptop이 주입됨

    @Test
    public void computerTest() {
        log.info("Laptop 모니터 너비 : {}", laptop.getScreenWidth());
        log.info("Desktop 모니터 너비 : {}", desktop.getScreenWidth());
        log.info("기본 컴퓨터 (Primary) 너비 : {}", computer.getScreenWidth());
    }
}
