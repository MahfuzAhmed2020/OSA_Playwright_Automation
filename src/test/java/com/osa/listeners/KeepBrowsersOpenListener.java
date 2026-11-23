package com.osa.listeners;

import com.osa.factory.BrowserFactory;
import org.testng.IExecutionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Keep the Playwright driver JVM alive while the user inspects completed
 * scenarios.
 */
public class KeepBrowsersOpenListener implements IExecutionListener {
    @Override
    public void onExecutionFinish() {
        if (!BrowserFactory.hasRetainedBrowsers())
            return;
        System.out
                .println("Tests finished. Browsers remain open for inspection. Press Ctrl+C in the terminal to exit.");
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
