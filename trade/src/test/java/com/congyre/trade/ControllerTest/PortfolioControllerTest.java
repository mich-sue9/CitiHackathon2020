package com.congyre.trade.ControllerTest;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=PortfolioControllerTest.Config.class)
public class PortfolioControllerTest {
    public static class Config{}
    
}
