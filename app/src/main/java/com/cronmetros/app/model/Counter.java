package com.cronmetros.app.model;

import java.util.ArrayList;
import java.util.List;
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
    public List<HistoryEntry> history = new ArrayList<>();
    public Integer notifiedMilestone; // null = nenhum notificado ainda

    public static class HistoryEntry {
        public long startedAt;
        public long endedAt;
        public long durationMs;

        public int getDays()  { return (int)(durationMs / 86_400_000L); }
        public int getHours() { return (int)((durationMs % 86_400_000L) / 3_600_000L); }
        public int getMins()  { return (int)((durationMs % 3_600_000L) / 60_000L); }
    }

    public static final long[] MILESTONE_MS = {
        86_400_000L,
        7  * 86_400_000L,
        30 * 86_400_000L,
        180 * 86_400_000L,
        365 * 86_400_000L
    };
    public static final String[] MILESTONE_LABELS = {
        "1 dia", "1 semana", "1 mês", "6 meses", "1 ano"
    };

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
        c.history = new ArrayList<>();
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
        long now = System.currentTimeMillis();
        long dur = effectiveElapsedMs();
        if (dur > 0) {
            if (history == null) history = new ArrayList<>();
            HistoryEntry e = new HistoryEntry();
            e.startedAt   = startedAt;
            e.endedAt     = isPaused() ? pausedAt : now;
            e.durationMs  = dur;
            history.add(0, e);
        }
        startedAt     = now;
        pausedAt      = 0;
        totalPausedMs = 0;
    }

    /** Index of the next milestone not yet reached, or -1 if all passed. */
    public int nextMilestoneIndex() {
        long elapsed = effectiveElapsedMs();
        for (int i = 0; i < MILESTONE_MS.length; i++) {
            if (elapsed < MILESTONE_MS[i]) return i;
        }
        return -1;
    }

    /** Index of the highest milestone achieved, or -1 if none. */
    public int highestMilestoneIndex() {
        long elapsed = effectiveElapsedMs();
        int hi = -1;
        for (int i = 0; i < MILESTONE_MS.length; i++) {
            if (elapsed >= MILESTONE_MS[i]) hi = i;
        }
        return hi;
    }
}
