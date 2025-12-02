package com.example.optimizationmod;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptimizationModClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("OptimizationMod/Client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Optimization Mod Client");
        // Client-side optimizations would go here
    }
}