package com.osa.utility;
import com.microsoft.playwright.Page;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
public final class ScreenshotUtil {
    private ScreenshotUtil() {}
    public static byte[] captureBytes(Page page) { return page.screenshot(new Page.ScreenshotOptions().setFullPage(true)); }
    public static Path save(Page page, String label) {
        try {
            Path dir = Path.of(PropertyReader.getProperty("screenshot.path"));
            Files.createDirectories(dir);
            String safe = label.replaceAll("[^A-Za-z0-9_-]", "_");
            Path file = dir.resolve(safe + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + ".png");
            Files.write(file, captureBytes(page));
            return file;
        } catch (Exception e) { throw new RuntimeException("Screenshot failed", e); }
    }
}
