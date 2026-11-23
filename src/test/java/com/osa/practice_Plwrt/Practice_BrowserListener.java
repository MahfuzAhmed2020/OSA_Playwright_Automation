package com.osa.practice_Plwrt;

import com.osa.factory.BrowserFactory;
import java.util.concurrent.CountDownLatch;
import org.testng.IExecutionListener;

/** Keeps the driver JVM alive after all parallel tests have reported results. */
public class Practice_BrowserListener implements IExecutionListener {
    @Override
    public void onExecutionFinish() {
        if (!BrowserFactory.hasRetainedBrowsers()) {
            return;
        }
        System.out.println("Tests finished. Browsers remain open. Press Ctrl+C in the terminal to exit.");
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
