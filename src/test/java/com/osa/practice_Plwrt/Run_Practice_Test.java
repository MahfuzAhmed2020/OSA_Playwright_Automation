package com.osa.practice_Plwrt;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Run_Practice_Test {
    @Test
    @Parameters({"browser", "keepOpen"})
    public void verifyDuplicateEmailRegistration(@Optional("firefox") String browser,
            @Optional("false") boolean keepOpen) {
        boolean retainBrowser = Boolean.parseBoolean(
                System.getProperty("keepOpen", Boolean.toString(keepOpen)));
        new Run_Practice().run(browser, retainBrowser);
    }
}
