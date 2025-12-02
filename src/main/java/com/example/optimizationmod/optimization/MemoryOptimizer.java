package com.example.optimizationmod.optimization;

import com.example.optimizationmod.OptimizationMod;
import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MemoryOptimizer {
    private final MemoryMXBean memoryBean;
    private final List<MemoryPoolMXBean> memoryPools;
    private final List<GarbageCollectorMXBean> gcBeans;
    private ScheduledExecutorService memoryMonitor;
    private long lastMemoryCleanup;
    
    public MemoryOptimizer() {
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.memoryPools = ManagementFactory.getMemoryPoolMXBeans();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.lastMemoryCleanup = System.currentTimeMillis();
    }
    
    public void optimizeMemory() {
        System.out.println("Applying memory optimizations...");
        
        setupGCMonitoring();
        optimizeMemoryPools();
        setupMemoryMonitoring();
        
        System.out.println("Memory optimizations applied");
    }
    
    public void optimizeGarbageCollection() {
        try {
            // Set GC-related system properties
            if (OptimizationMod.CONFIG.aggressiveMemoryManagement) {
                System.setProperty("XX:+UseG1GC", "true");
                System.setProperty("XX:+UnlockExperimentalVMOptions", "true");
                System.setProperty("XX:G1HeapRegionSize", "16m");
            }
            
            // Force initial GC
            System.gc();
            
        } catch (Exception e) {
            System.err.println("GC optimization failed: " + e.getMessage());
        }
    }
    
    private void setupGCMonitoring() {
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            if (gcBean instanceof NotificationEmitter) {
                NotificationEmitter emitter = (NotificationEmitter) gcBean;
                NotificationListener listener = (notification, handback) -> {
                    if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                        CompositeData cd = (CompositeData) notification.getUserData();
                        GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from(cd);
                        
                        if (OptimizationMod.CONFIG.debugMode) {
                            System.out.println("GC: " + info.getGcAction() + " - " + 
                                info.getGcName() + " took " + info.getGcInfo().getDuration() + "ms");
                        }
                    }
                };
                emitter.addNotificationListener(listener, null, null);
            }
        }
    }
    
    private void optimizeMemoryPools() {
        for (MemoryPoolMXBean pool : memoryPools) {
            if (pool.isUsageThresholdSupported()) {
                long maxMemory = pool.getUsage().getMax();
                if (maxMemory != -1) {
                    // Set usage threshold at 80% of max
                    long threshold = (long) (maxMemory * 0.8);
                    pool.setUsageThreshold(threshold);
                }
            }
        }
    }
    
    private void setupMemoryMonitoring() {
        memoryMonitor = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r, "OptimizationMod-MemoryMonitor");
            t.setDaemon(true);
            return t;
        });
        
        memoryMonitor.scheduleAtFixedRate(this::monitorMemory, 
            10, 10, TimeUnit.SECONDS);
    }
    
    private void monitorMemory() {
        try {
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            long maxMemory = runtime.maxMemory();
            
            double memoryUsage = (double) usedMemory / maxMemory;
            
            if (memoryUsage > 0.85) {
                System.out.println("High memory usage detected: " + 
                    String.format("%.2f", memoryUsage * 100) + "%");
                
                // Trigger memory cleanup if needed
                performMemoryCleanup();
            }
            
            if (OptimizationMod.CONFIG.debugMode) {
                System.out.println("Memory - Used: " + (usedMemory / 1024 / 1024) + 
                    "MB, Total: " + (totalMemory / 1024 / 1024) + 
                    "MB, Max: " + (maxMemory / 1024 / 1024) + "MB");
            }
            
        } catch (Exception e) {
            System.err.println("Memory monitoring failed: " + e.getMessage());
        }
    }
    
    private void performMemoryCleanup() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMemoryCleanup > 
            OptimizationMod.CONFIG.memoryCleanupInterval * 1000L) {
            
            System.out.println("Performing memory cleanup...");
            System.gc();
            lastMemoryCleanup = currentTime;
        }
    }
    
    public void shutdown() {
        if (memoryMonitor != null) {
            memoryMonitor.shutdown();
        }
    }
}
