package com.orangehrm.qa.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton ConfigReader that lazily loads and caches properties from config.properties.
 * Provides type-safe accessors for all framework configuration values.
 */
public final class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE = "config.properties";

    private static volatile ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Thread-safe double-checked locking singleton accessor.
     */
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (stream == null) {
                throw new RuntimeException("Configuration file not found on classpath: " + CONFIG_FILE);
            }
            properties.load(stream);
            log.info("Configuration loaded successfully from: {}", CONFIG_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE, e);
        }
    }

    // ── String accessors ──────────────────────────────────────────────────────

    public String getBaseUrl() {
        return getRequired("base.url");
    }

    public String getAdminUsername() {
        return getRequired("admin.username");
    }

    public String getAdminPassword() {
        return getRequired("admin.password");
    }

    public String getBrowser() {
        return getRequired("browser");
    }

    public String getScreenshotDir() {
        return getProperty("screenshot.dir", "test-output/screenshots");
    }

    public String getEnvironment() {
        return getProperty("environment", "staging");
    }

    // ── Boolean accessors ─────────────────────────────────────────────────────

    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }

    public boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    // ── Integer accessors ─────────────────────────────────────────────────────

    public int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "15"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout", "30"));
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private String getRequired(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Required configuration key is missing or empty: " + key);
        }
        return value.trim();
    }

    private String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }
}
