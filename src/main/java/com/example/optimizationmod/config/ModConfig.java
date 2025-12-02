package com.example.optimizationmod.config;

public class ModConfig {
    // Memory Optimization
    public boolean memoryOptimization = true;
    public boolean garbageCollectionOptimization = true;
    public int memoryCleanupInterval = 300;
    public boolean aggressiveMemoryManagement = false;
    
    // Performance Optimization
    public boolean performanceOptimization = true;
    public boolean threadOptimization = true;
    public boolean fileI0Optimization = true;
    
    // Render Optimization
    public boolean renderOptimization = true;
    public boolean textureOptimization = true;
    public int textureCacheSize = 512;
    
    // World Optimization
    public boolean worldOptimization = true;
    public boolean chunkOptimization = true;
    public boolean entityOptimization = true;
    
    // Network Optimization
    public boolean networkOptimization = true;
    public int packetOptimizationLevel = 2;
    
    // Advanced Settings
    public boolean experimentalOptimizations = false;
    public boolean debugMode = false;
    
    public ModConfig() {
        // Default constructor
    }
}
