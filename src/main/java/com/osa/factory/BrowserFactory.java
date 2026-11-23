package com.osa.factory;

import com.microsoft.playwright.*;
import com.osa.utility.AppContains;
import com.osa.utility.PropertyReader;

/**
 * One Playwright instance per TestNG worker thread. Do not share objects across
 * threads.
 */
public final class BrowserFactory {
    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();
    private static final java.util.Queue<Object[]> RETAINED = new java.util.concurrent.ConcurrentLinkedQueue<>();

    private BrowserFactory() {
    }

    public static void initializeBrowser(String name) {
        initializeBrowser(name, false);
    }

    public static void initializeBrowser(String name, boolean keepOpen) {
        if (PLAYWRIGHT.get() != null)
            throw new IllegalStateException("Browser already initialized on thread");
        try {
            Playwright pw = Playwright.create();
            PLAYWRIGHT.set(pw);
            BrowserType type;
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(!keepOpen && PropertyReader.getBoolean("headless"))
                    .setSlowMo(PropertyReader.getInt("slow.mo"));
            switch (name.toLowerCase(java.util.Locale.ROOT)) {
                case "chromium":
                    type = pw.chromium();
                    break;
                case "chrome":
                    type = pw.chromium();
                    options.setChannel("chrome");
                    break;
                case "edge":
                case "msedge":
                    type = pw.chromium();
                    options.setChannel("msedge");
                    break;
                case "firefox":
                    type = pw.firefox();
                    break;
                case "webkit":
                    type = pw.webkit();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + name);
            }
            Browser browser = type.launch(options);
            BROWSER.set(browser);
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(PropertyReader.getInt("viewport.width"),
                            PropertyReader.getInt("viewport.height")));
            CONTEXT.set(context);
            PAGE.set(context.newPage());
            AppContains.LOG.info("Initialized browser {}", name);
        } catch (RuntimeException e) {
            closeBrowser();
            throw e;
        }
    }

    public static Page getPage() {
        Page p = PAGE.get();
        if (p == null)
            throw new IllegalStateException("No page on this thread. Initialize browser first.");
        return p;
    }

    public static void closeBrowser() {
        try {
            if (CONTEXT.get() != null)
                CONTEXT.get().close();
        } finally {
            try {
                if (BROWSER.get() != null)
                    BROWSER.get().close();
            } finally {
                try {
                    if (PLAYWRIGHT.get() != null)
                        PLAYWRIGHT.get().close();
                } finally {
                    PAGE.remove();
                    CONTEXT.remove();
                    BROWSER.remove();
                    PLAYWRIGHT.remove();
                }
            }
        }
    }

    public static void retainBrowser() {
        if (PLAYWRIGHT.get() != null) {
            RETAINED.add(new Object[] { PLAYWRIGHT.get(), BROWSER.get(), CONTEXT.get(), PAGE.get() });
        }
        PAGE.remove();
        CONTEXT.remove();
        BROWSER.remove();
        PLAYWRIGHT.remove();
    }

    public static boolean hasRetainedBrowsers() {
        return !RETAINED.isEmpty();
    }
}
