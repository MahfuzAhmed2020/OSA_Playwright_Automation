package com.osa.practice_Plwrt;

import com.osa.factory.BrowserFactory;

public class Run_Practice {

    public static void main(String[] args) {
        new Run_Practice().run(args.length > 0 ? args[0] : "webkit", true, false);
    }

    public void run(String browserName) {
        run(browserName, false, false);
    }

    public void run(String browserName, boolean keepOpen) {
        run(browserName, false, keepOpen);
    }

    private void run(String browserName, boolean pause, boolean keepOpen) {
        BrowserFactory.initializeBrowser(browserName, keepOpen || pause);
        try {
            Practice_Registration_P_o_m p_o_m = new Practice_Registration_P_o_m(BrowserFactory.getPage());
            register(p_o_m);
            if (pause) {
                BrowserFactory.getPage().pause();
            }
        } finally {
            if (keepOpen) {
                BrowserFactory.retainBrowser();
            } else {
                BrowserFactory.closeBrowser();
            }
        }
    }

    private void register(Practice_Registration_P_o_m p_o_m) {
        p_o_m.open();
        p_o_m.verifyPageLoaded();
        p_o_m.enterFirstName("abdul");
        p_o_m.enterLastName("senate");
        p_o_m.enterEmail("abdul@google.com");
        p_o_m.enterPassword("Test@1234");
        p_o_m.enterConfirmPassword("Test@1234");
        p_o_m.acceptTermsAndPrivacy();
        p_o_m.clickCreateAccountButton();
        p_o_m.verifyMessage("This email address is already used by another account.");
        System.out.println(p_o_m.getMessage());
    }

}
