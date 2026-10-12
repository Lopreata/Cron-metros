package com.cronmetros.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cronmetros.app.R;
import com.cronmetros.app.model.Counter;
import com.google.android.material.chip.Chip;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.VH> {

    public interface Listener {
        void onToggle(Counter c);
        void onReset(Counter c);
        void onDelete(Counter c);
        void onSelectionChanged(Set<String> ids);
    }

    private List<Counter> data;
    private final Set<String> selected = new HashSet<>();
    private final Listener listener;

    public CounterAdapter(List<Counter> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<Counter> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public Set<String> getSelected() { return new HashSet<>(selected); }
    public int selectedCount()       { return selected.size(); }
    public boolean isAllSelected()   { return !data.isEmpty() && selected.size() == data.size(); }

    public void clearSelection() {
        selected.clear();
        notifyDataSetChanged();
        listener.onSelectionChanged(selected);
    }

    public void selectAll() {
        for (Counter c : data) selected.add(c.id);
        notifyDataSetChanged();
        listener.onSelectionChanged(new HashSet<>(selected));
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_counter, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Counter c = data.get(pos);
        boolean sel = selected.contains(c.id);

        h.itemView.setBackgroundColor(sel ? 0xFF2A273F : 0xFF1E1E2E);
        h.colorBar.setBackgroundColor(Color.parseColor(c.color));
        h.cbSelect.setChecked(sel);
        h.tvEmoji.setText(c.emoji);
        h.tvName.setText(c.name);

        if (c.description != null && !c.description.isEmpty()) {
            h.tvDesc.setVisibility(View.VISIBLE);
            h.tvDesc.setText(c.description);
        } else {
            h.tvDesc.setVisibility(View.GONE);
        }

        h.chipPaused.setVisibility(c.isPaused() ? View.VISIBLE : View.GONE);

        h.tvDays.setText(String.valueOf(c.getDays()));
        h.tvDays.setTextColor(Color.parseColor(c.color));
        h.tvTime.setText(String.format("%02dh %02dm", c.getHours(), c.getMinutes()));

        if (c.isPaused()) {
            h.btnToggle.setImageResource(R.drawable.ic_play);
            h.btnToggle.setColorFilter(0xFFA6E3A1);
        } else {
            h.btnToggle.setImageResource(R.drawable.ic_pause);
            h.btnToggle.setColorFilter(0xFFFAB387);
        }

        h.cbSelect.setOnClickListener(v -> toggle(c, h.getAdapterPosition()));
        h.itemView.setOnClickListener(v -> toggle(c, h.getAdapterPosition()));
        h.btnToggle.setOnClickListener(v -> listener.onToggle(c));
        h.btnReset.setOnClickListener(v -> listener.onReset(c));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(c));
    }

    private void toggle(Counter c, int pos) {
        if (selected.contains(c.id)) selected.remove(c.id);
        else selected.add(c.id);
        notifyItemChanged(pos);
        listener.onSelectionChanged(new HashSet<>(selected));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        View colorBar;
        TextView tvEmoji, tvName, tvDesc, tvDays, tvTime;
        Chip chipPaused;
        ImageButton btnToggle, btnReset, btnDelete;

        VH(@NonNull View v) {
            super(v);
            cbSelect   = v.findViewById(R.id.cb_select);
            colorBar   = v.findViewById(R.id.color_bar);
            tvEmoji    = v.findViewById(R.id.tv_emoji);
            tvName     = v.findViewById(R.id.tv_name);
            tvDesc     = v.findViewById(R.id.tv_desc);
            chipPaused = v.findViewById(R.id.chip_paused);
            tvDays     = v.findViewById(R.id.tv_days);
            tvTime     = v.findViewById(R.id.tv_time);
            btnToggle  = v.findViewById(R.id.btn_toggle);
            btnReset   = v.findViewById(R.id.btn_reset);
            btnDelete  = v.findViewById(R.id.btn_delete);
        }
    }
}
