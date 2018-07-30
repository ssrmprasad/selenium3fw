package com.masteringselenium;

import com.masteringselenium.config.DriverType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import static com.masteringselenium.config.DriverType.FIREFOX;

public class DriverFactory {

    private RemoteWebDriver webDriver;
    private DriverType selectedDriverType;

    private final String operatingSystem = System.getProperty("os.name").toUpperCase();
    private final String systemArchitecture = System.getProperty("os.arch");

    public DriverFactory()
    {
        DriverType driverType = FIREFOX;
        String browser = System.getProperty("browser",driverType.name().toUpperCase());
        try
        {
            driverType = DriverType.valueOf(browser);
            System.out.println("driver type value of is : " + driverType.name());
        }catch (IllegalArgumentException ignored)
        {
            System.err.println("Unknown driver specified, defaulting to '" + driverType + "'...");
        }catch (NullPointerException ignored)
        {
            System.err.println("No driver specified, defaulting to '" + driverType + "'...");
        }
        selectedDriverType = driverType;
    }

    public RemoteWebDriver getDriver()
    {
        if(null==webDriver)
        {
//            System.out.println(" ");
//            System.out.println("Current Operating System: " + operatingSystem);
//            System.out.println("Current Achitecture: " + systemArchitecture);
//            System.out.println("Current Browser Selection: Firefox");
//            System.out.println(" ");
//            webDriver = new FirefoxDriver();
            instantiateWebDriver(selectedDriverType);
            System.out.println("Current thread: " + Thread.currentThread().getId());

        }
        return webDriver;
    }

    void quitDriver()
    {
        if(null!=webDriver)
        {
            webDriver.quit();
            webDriver = null;
        }
    }

    private void instantiateWebDriver(DriverType driverType)
    {
        System.out.println(" ");
        System.out.println("Local Operating System: " +
                operatingSystem);
        System.out.println("Local Architecture: " +
                systemArchitecture);
        System.out.println("Selected Browser: " +
                selectedDriverType);
        System.out.println(" ");
        DesiredCapabilities desiredCapabilities = new
                DesiredCapabilities();
        webDriver =
                driverType.getWebDriverObject(desiredCapabilities);

    }
}
