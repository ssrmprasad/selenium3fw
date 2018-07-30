package com.masteringselenium.listeners;

import com.masteringselenium.DriverBase;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenshotListener extends TestListenerAdapter{

    private boolean createFile(File screenshot)
    {
        boolean fileCreated = false;
        if(screenshot.exists())
        {
            fileCreated = true;
        }else
        {
            File parentDirectory = new File(screenshot.getParent());
            if(parentDirectory.exists()||parentDirectory.mkdirs())
            {
                try{
                    fileCreated = screenshot.createNewFile();
                }catch (IOException errorCreatingScreenshot)
                {
                    errorCreatingScreenshot.printStackTrace();
                }
            }
        }
        return fileCreated;
    }

    private void writeScreenshotToFile(WebDriver driver, File screenshot)
    {
        try
        {
            FileOutputStream screenshotStream = new FileOutputStream(screenshot);
            screenshotStream.write(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES));
            screenshotStream.close();
        }catch (IOException unableToWriteScreenshot)
        {
            System.err.println("Unable to Write " + screenshot.getAbsolutePath());
            unableToWriteScreenshot.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult failingTest) {
        try{
        WebDriver driver = DriverBase.getDriver();
        String screenShotDirectory = System.getProperty("screenshotDirectory","target/screenshots");
        String screenShotAbsolutePath = screenShotDirectory+File.separator+System.currentTimeMillis()+"_"+failingTest.getName()+".png";
        File screenShot = new File(screenShotAbsolutePath);
        if(createFile(screenShot))
        {
            try{
                writeScreenshotToFile(driver,screenShot);
            }catch (ClassCastException weNeedToAugmentOurDriverObject)
            {
                writeScreenshotToFile(new Augmenter().augment(driver),screenShot);
            }
            System.out.println("Written screenshot to " + screenShotAbsolutePath);
        }else{
            System.err.println("Unable to create " + screenShotAbsolutePath);
        }
        //super.onTestFailure(iTestResult);
    }catch (Exception ex)
        {
            System.err.println("Unable to Capture screenshot...");
            ex.printStackTrace();
        }
}
}