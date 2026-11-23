package com.osa.pages;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.osa.utility.AppContains;

public class Practice {

    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();
        //Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("msedge"));
       // Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("firefox"));
        Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome"));

      Page page = browser.newPage();
      page.navigate("http://localhost:8087/login");
        
        AppContains.LOG.info("This class is for practice purpose only, not part of the framework. CTRL+C to exit.");
    }
    
}
