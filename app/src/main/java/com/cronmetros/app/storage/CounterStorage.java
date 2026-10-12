package com.cronmetros.app.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.cronmetros.app.model.Counter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CounterStorage {
    private static final String PREFS = "cron_metros";
    private static final String KEY   = "counters_v1";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public CounterStorage(Context ctx) {
        prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public List<Counter> load() {
        String json = prefs.getString(KEY, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Counter>>(){}.getType();
        List<Counter> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    public void save(List<Counter> counters) {
        prefs.edit().putString(KEY, gson.toJson(counters)).apply();
    }
}
