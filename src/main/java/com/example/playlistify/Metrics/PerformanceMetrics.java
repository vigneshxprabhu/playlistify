package com.example.playlistify.Metrics;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class PerformanceMetrics {
    private long startTime;
    private long endTime;
    private int cacheHits;
    private int cacheMisses;
    private int apiCalls;
    private int artistsRequested;

    public void startTimer() {
    startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
    endTime = System.currentTimeMillis();
    }

    public long getExecutionTime() {
    return endTime - startTime;
    }
    public void printReport() {

    System.out.println();
    System.out.println("========== PERFORMANCE REPORT ==========");
    System.out.println("Artists Requested : " + artistsRequested);
    System.out.println("Cache Hits        : " + cacheHits);
    System.out.println("Cache Misses      : " + cacheMisses);
    System.out.println("API Calls         : " + apiCalls);
    System.out.println("Execution Time    : " + getExecutionTime() + " ms");
    System.out.println("========================================");
    System.out.println();
    }
    public void reset() {

    cacheHits = 0;
    cacheMisses = 0;
    apiCalls = 0;
    artistsRequested = 0;
    startTime = 0;
    endTime = 0;
    }

}
