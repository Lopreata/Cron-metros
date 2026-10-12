package com.cronmetros.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cronmetros.app.adapter.CounterAdapter;
import com.cronmetros.app.model.Counter;
import com.cronmetros.app.storage.CounterStorage;
import com.cronmetros.app.ui.AddCounterSheet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private CounterStorage storage;
    private List<Counter> counters;
    private CounterAdapter adapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (adapter != null) adapter.notifyDataSetChanged();
            handler.postDelayed(this, 60_000);
        }
    };

    private LinearLayout bulkBar;
    private TextView tvSelCount;
    private CheckBox cbAll;
    private RecyclerView rv;
    private View emptyState, tableHeader;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        storage  = new CounterStorage(this);
        counters = storage.load();

        rv          = findViewById(R.id.recycler);
        emptyState  = findViewById(R.id.empty_state);
        tableHeader = findViewById(R.id.table_header);
        bulkBar     = findViewById(R.id.bulk_bar);
        tvSelCount  = findViewById(R.id.tv_sel_count);
        cbAll       = findViewById(R.id.cb_all);

        adapter = new CounterAdapter(counters, new CounterAdapter.Listener() {
            @Override public void onToggle(Counter c) {
                if (c.isPaused()) c.resume(); else c.pause();
                persist();
            }
            @Override public void onReset(Counter c) {
                confirm("Zerar \"" + c.name + "\"?",
                    "A contagem recomeçará do zero.", () -> { c.reset(); persist(); });
            }
            @Override public void onDelete(Counter c) {
                confirm("Excluir \"" + c.name + "\"?",
                    "Esta ação não pode ser desfeita.", () -> { counters.remove(c); persist(); });
            }
            @Override public void onSelectionChanged(Set<String> ids) {
                updateBulkBar(ids);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddSheet());

        cbAll.setOnClickListener(v -> {
            if (adapter.isAllSelected()) adapter.clearSelection();
            else adapter.selectAll();
        });

        findViewById(R.id.btn_bulk_resume).setOnClickListener(v -> bulkAction(false));
        findViewById(R.id.btn_bulk_pause).setOnClickListener(v  -> bulkAction(true));
        findViewById(R.id.btn_bulk_reset).setOnClickListener(v  -> {
            int n = adapter.selectedCount();
            confirm("Zerar " + n + " cronômetro" + (n>1?"s":"") + "?", "", () -> {
                for (Counter c : counters) if (adapter.getSelected().contains(c.id)) c.reset();
                adapter.clearSelection(); persist();
            });
        });
        findViewById(R.id.btn_bulk_delete).setOnClickListener(v -> {
            int n = adapter.selectedCount();
            confirm("Excluir " + n + " cronômetro" + (n>1?"s":"") + "?",
                "Esta ação não pode ser desfeita.", () -> {
                    Set<String> ids = adapter.getSelected();
                    counters.removeIf(c -> ids.contains(c.id));
                    adapter.clearSelection(); persist();
                });
        });
        findViewById(R.id.btn_bulk_clear).setOnClickListener(v -> adapter.clearSelection());

        updateUI();
        handler.postDelayed(tick, 60_000);
    }

    private void showAddSheet() {
        AddCounterSheet sheet = AddCounterSheet.newInstance();
        sheet.setCallback(c -> { counters.add(0, c); persist(); });
        sheet.show(getSupportFragmentManager(), "add");
    }

    private void bulkAction(boolean pause) {
        Set<String> ids = adapter.getSelected();
        for (Counter c : counters) {
            if (!ids.contains(c.id)) continue;
            if (pause) c.pause(); else c.resume();
        }
        adapter.clearSelection();
        persist();
    }

    private void persist() {
        storage.save(counters);
        adapter.notifyDataSetChanged();
        updateUI();
    }

    private void updateUI() {
        boolean empty = counters.isEmpty();
        emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
        tableHeader.setVisibility(empty ? View.GONE : View.VISIBLE);
        rv.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void updateBulkBar(Set<String> ids) {
        int n = ids.size();
        bulkBar.setVisibility(n > 0 ? View.VISIBLE : View.GONE);
        tvSelCount.setText(n + " selecionado" + (n > 1 ? "s" : ""));
        cbAll.setChecked(adapter.isAllSelected());
    }

    private void confirm(String title, String msg, Runnable action) {
        new AlertDialog.Builder(this, R.style.DialogTheme)
            .setTitle(title)
            .setMessage(msg.isEmpty() ? null : msg)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Confirmar", (d, w) -> action.run())
            .show();
    }

    @Override protected void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(tick);
    }
}
