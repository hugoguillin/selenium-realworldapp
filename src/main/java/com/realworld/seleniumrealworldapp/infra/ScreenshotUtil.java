package com.realworld.seleniumrealworldapp.infra;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ScreenshotUtil {

    @Autowired
    protected ApplicationContext ctx;

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = ".png")
    public byte[] takeScreenshot(){
        WebDriver driver = ctx.getBean(WebDriver.class);
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }
}
