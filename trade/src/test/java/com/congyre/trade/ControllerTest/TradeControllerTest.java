package com.congyre.trade.ControllerTest;

import com.congyre.trade.rest.TradeController;
import com.congyre.trade.service.TradeService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=TradeControllerTest.Config.class)

public class TradeControllerTest {

    @SpringBootApplication(scanBasePackageClasses = {
        TradeController.class,
        TradeService.class
    })
    @EnableMongoRepositories(basePackages = "com.congyre.trade.repository")
    @PropertySource("file:src/test/resources/test.properties")

    public static class Config{}
    
    @Test
    public void donothing(){}
    
}