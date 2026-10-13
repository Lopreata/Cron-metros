package com.cronmetros.app;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.cronmetros.app.model.Counter;
import com.cronmetros.app.storage.CounterStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MilestoneWorker extends Worker {

    public MilestoneWorker(@NonNull Context ctx, @NonNull WorkerParameters params) {
        super(ctx, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context ctx = getApplicationContext();
        CounterStorage storage = new CounterStorage(ctx);
        List<Counter> counters = storage.load();
        if (counters == null || counters.isEmpty()) return Result.success();

        boolean changed = false;
        Random rng = new Random();

        // 1. Check each counter for newly reached milestones
        for (Counter c : counters) {
            int hi      = c.highestMilestoneIndex();
            int notified = c.notifiedMilestone != null ? c.notifiedMilestone : -1;
            if (hi > notified) {
                int notifId = Math.abs(c.id.hashCode()) % 50_000;
                NotificationHelper.showMilestoneNotification(
                        ctx, c.emoji, c.name,
                        Counter.MILESTONE_LABELS[hi],
                        c.getDays(), notifId);
                c.notifiedMilestone = hi;
                changed = true;
            }
        }

        // 2. Sporadic encouragement (~40 % chance per run, roughly every 15 h)
        //    Pick a random counter that has been running at least 1 day.
        if (rng.nextFloat() < 0.40f) {
            List<Counter> eligible = new ArrayList<>();
            for (Counter c : counters) {
                if (!c.isPaused() && c.getDays() >= 1) eligible.add(c);
            }
            if (!eligible.isEmpty()) {
                Counter pick    = eligible.get(rng.nextInt(eligible.size()));
                int notifId     = 90_000 + rng.nextInt(1_000);
                NotificationHelper.showEncouragementNotification(
                        ctx, pick.emoji, pick.name, pick.getDays(), notifId);
            }
        }

        if (changed) storage.save(counters);
        return Result.success();
    }
}
