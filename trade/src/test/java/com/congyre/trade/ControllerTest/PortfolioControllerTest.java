package com.congyre.trade.ControllerTest;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.congyre.trade.rest.PortfolioController;
import com.congyre.trade.service.PortfolioService;

import org.junit.Test;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=PortfolioControllerTest.Config.class)
public class PortfolioControllerTest {

    @SpringBootApplication(scanBasePackageClasses = {
        PortfolioController.class,
        PortfolioService.class
    })
    @EnableMongoRepositories(basePackages = "com.congyre.trade.repository")
    @PropertySource("file:src/test/resources/test.properties")

    public static class Config{}
    
    @Test
    public void donothing(){}
    
}
