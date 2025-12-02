package com.example.optimizationmod.hooks;

import com.example.optimizationmod.OptimizationMod;

public class EventHooks {
    
    public static void registerHooks() {
        OptimizationMod.LOGGER.info("Registering optimization hooks");
        
        // Register shutdown hook for cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(EventHooks::onShutdown));
        
        // Setup memory monitoring
        setupMemoryMonitoring();
        
        OptimizationMod.LOGGER.info("Optimization hooks registered");
    }
    
    private static void setupMemoryMonitoring() {
        Thread memoryMonitor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runtime runtime = Runtime.getRuntime();
                    long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                    long maxMemory = runtime.maxMemory();
                    double memoryUsage = (double) usedMemory / maxMemory;
                    
                    if (memoryUsage > 0.85) {
                        OptimizationMod.LOGGER.warn("High memory usage detected: {}%", 
                            String.format("%.2f", memoryUsage * 100));
                        System.gc();
                    }
                    
                    Thread.sleep(30000); // Check every 30 seconds
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "OptimizationMod-MemoryMonitor");
        
        memoryMonitor.setDaemon(true);
        memoryMonitor.start();
    }
    
    private static void onShutdown() {
        OptimizationMod.LOGGER.info("Shutting down Optimization Mod");
        // Cleanup tasks would go here
    }
}
