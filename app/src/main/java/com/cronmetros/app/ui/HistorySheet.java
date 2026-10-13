package com.cronmetros.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cronmetros.app.R;
import com.cronmetros.app.model.Counter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistorySheet extends BottomSheetDialogFragment {

    private static final String ARG_COUNTER_ID = "counter_id";
    private Counter counter;

    public static HistorySheet newInstance(Counter counter) {
        HistorySheet s = new HistorySheet();
        Bundle b = new Bundle();
        b.putString(ARG_COUNTER_ID, counter.id);
        s.setArguments(b);
        s.counter = counter;
        return s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitle = view.findViewById(R.id.tv_history_title);
        TextView tvEmpty = view.findViewById(R.id.tv_history_empty);
        RecyclerView rv  = view.findViewById(R.id.rv_history);

        tvTitle.setText("Histórico · " + counter.emoji + " " + counter.name);

        List<Counter.HistoryEntry> history = counter.history;
        boolean empty = (history == null || history.isEmpty());
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rv.setVisibility(empty ? View.GONE : View.VISIBLE);

        if (!empty) {
            rv.setLayoutManager(new LinearLayoutManager(requireContext()));
            rv.setAdapter(new HistoryAdapter(history));
        }
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

        private final List<Counter.HistoryEntry> items;
        private final SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        HistoryAdapter(List<Counter.HistoryEntry> items) { this.items = items; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            Counter.HistoryEntry e = items.get(pos);

            h.tvDays.setText(String.valueOf(e.getDays()));

            String dur;
            if (e.getDays() > 0) {
                dur = e.getDays() + "d " + e.getHours() + "h " + e.getMins() + "m";
            } else if (e.getHours() > 0) {
                dur = e.getHours() + "h " + e.getMins() + "m";
            } else {
                dur = e.getMins() + " minutos";
            }
            h.tvDuration.setText(dur);

            String start = sdf.format(new Date(e.startedAt));
            String end   = sdf.format(new Date(e.endedAt));
            h.tvDates.setText(start + " → " + end);
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvDays, tvDuration, tvDates;
            VH(@NonNull View v) {
                super(v);
                tvDays     = v.findViewById(R.id.tv_h_days);
                tvDuration = v.findViewById(R.id.tv_h_duration);
                tvDates    = v.findViewById(R.id.tv_h_dates);
            }
        }
    }
}
