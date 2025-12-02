package com.example.optimizationmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = Paths.get("config");
    private static final Path CONFIG_PATH = CONFIG_DIR.resolve("optimizationmod.json");

    public static ModConfig loadConfig() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                return GSON.fromJson(json, ModConfig.class);
            } else {
                ModConfig defaultConfig = new ModConfig();
                saveConfig(defaultConfig);
                return defaultConfig;
            }
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
            return new ModConfig();
        }
    }

    public static void saveConfig(ModConfig config) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(config);
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }
}