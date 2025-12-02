package com.example.optimizationmod;

import com.example.optimizationmod.config.ConfigHandler;
import com.example.optimizationmod.config.ModConfig;
import com.example.optimizationmod.hooks.EventHooks;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptimizationMod implements ModInitializer {
    public static final String MOD_ID = "optimizationmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Optimization Mod");
        
        // Load configuration
        CONFIG = ConfigHandler.loadConfig();
        
        // Register event hooks
        EventHooks.registerHooks();
        
        LOGGER.info("Optimization Mod initialized successfully");
    }
}
