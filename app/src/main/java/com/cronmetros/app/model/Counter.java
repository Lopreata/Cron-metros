package com.cronmetros.app.model;

import java.util.UUID;

public class Counter {
    public String id;
    public String name;
    public String emoji;
    public String description;
    public String color;
    public long startedAt;
    public long pausedAt;      // 0 = running; >0 = timestamp when paused
    public long totalPausedMs; // accumulated paused duration
    public long createdAt;

    public static Counter create(String name, String emoji, String description, String color) {
        Counter c = new Counter();
        c.id = UUID.randomUUID().toString();
        c.name = name;
        c.emoji = emoji != null ? emoji : "⭐";
        c.description = description != null ? description : "";
        c.color = color != null ? color : "#6366F1";
        long now = System.currentTimeMillis();
        c.startedAt = now;
        c.createdAt = now;
        c.pausedAt = 0;
        c.totalPausedMs = 0;
        return c;
    }

    public long effectiveElapsedMs() {
        long end = isPaused() ? pausedAt : System.currentTimeMillis();
        return Math.max(0, end - startedAt - totalPausedMs);
    }

    public int getDays()    { return (int)(effectiveElapsedMs() / 86_400_000L); }
    public int getHours()   { return (int)((effectiveElapsedMs() % 86_400_000L) / 3_600_000L); }
    public int getMinutes() { return (int)((effectiveElapsedMs() % 3_600_000L) / 60_000L); }

    public boolean isPaused() { return pausedAt > 0; }

    public void pause() {
        if (!isPaused()) pausedAt = System.currentTimeMillis();
    }

    public void resume() {
        if (isPaused()) {
            totalPausedMs += System.currentTimeMillis() - pausedAt;
            pausedAt = 0;
        }
    }

    public void reset() {
        startedAt = System.currentTimeMillis();
        pausedAt = 0;
        totalPausedMs = 0;
    }
}
