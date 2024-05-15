package com.realworld.seleniumrealworldapp.infra;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class ScreenshotUtil {

    @Autowired
    protected ApplicationContext ctx;

    @Value("${screenshot.path}")
    private Path path;

    public void takeScreenshot(String testName){
        String fileName = testName + ".png";
        File sourceFile = ((TakesScreenshot)ctx.getBean(WebDriver.class)).getScreenshotAs(OutputType.FILE);
        try {
            FileCopyUtils.copy(sourceFile, path.resolve(fileName).toFile());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
