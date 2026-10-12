package com.cronmetros.app.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.cronmetros.app.R;
import com.cronmetros.app.model.Counter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddCounterSheet extends BottomSheetDialogFragment {

    public interface Callback { void onCreated(Counter counter); }

    private static final String[] EMOJIS = {
        "😴","💪","🚭","🍺","🍕","💊","🏃","📚",
        "🧘","💰","❤️","🌱","🎯","✨","🔥","⭐",
        "🏆","🎵","🧹","💻","🌙","☀️","🍎","💧"
    };
    private static final String[] COLORS = {
        "#6366F1","#8B5CF6","#EC4899","#EF4444",
        "#F97316","#EAB308","#22C55E","#14B8A6",
        "#3B82F6","#06B6D4","#84CC16","#F43F5E"
    };
    private static final String[] SUGGESTIONS = {
        "Dias dormindo bem","Dias sem recaída","Dias sem fumar",
        "Dias sem álcool","Dias se exercitando","Dias meditando",
        "Dias estudando","Dias sem açúcar","Dias bebendo água",
        "Dias de dieta","Dias lendo","Dias sem redes sociais"
    };

    private String selEmoji = "⭐";
    private String selColor = "#6366F1";
    private Callback callback;

    public static AddCounterSheet newInstance() { return new AddCounterSheet(); }
    public void setCallback(Callback cb) { this.callback = cb; }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle s) {
        return inf.inflate(R.layout.sheet_add_counter, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle s) {
        super.onViewCreated(root, s);

        TextInputEditText etName = root.findViewById(R.id.et_name);
        TextInputEditText etDesc = root.findViewById(R.id.et_desc);
        LinearLayout llSugg = root.findViewById(R.id.ll_suggestions);
        GridLayout gridEmoji = root.findViewById(R.id.grid_emojis);
        GridLayout gridColor = root.findViewById(R.id.grid_colors);

        float dp = requireContext().getResources().getDisplayMetrics().density;

        // Suggestions
        for (String s2 : SUGGESTIONS) {
            MaterialButton btn = new MaterialButton(requireContext(), null,
                com.google.android.material.R.attr.materialButtonOutlinedStyle);
            btn.setText(s2);
            btn.setTextSize(11);
            btn.setPadding((int)(8*dp),0,(int)(8*dp),0);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, (int)(32*dp));
            lp.setMarginEnd((int)(6*dp));
            btn.setLayoutParams(lp);
            btn.setOnClickListener(v -> { etName.setText(s2); etName.setSelection(s2.length()); });
            llSugg.addView(btn);
        }

        // Emoji grid
        gridEmoji.setColumnCount(8);
        for (int i = 0; i < EMOJIS.length; i++) {
            String e = EMOJIS[i];
            TextView tv = new TextView(requireContext());
            tv.setText(e);
            tv.setTextSize(22);
            tv.setGravity(android.view.Gravity.CENTER);
            int sz = (int)(44*dp);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = sz; lp.height = sz;
            lp.setMargins(3,3,3,3);
            tv.setLayoutParams(lp);
            tv.setBackgroundResource(R.drawable.bg_emoji_btn);
            tv.setSelected(e.equals(selEmoji));
            tv.setOnClickListener(v -> {
                selEmoji = e;
                for (int j = 0; j < gridEmoji.getChildCount(); j++)
                    gridEmoji.getChildAt(j).setSelected(false);
                tv.setSelected(true);
            });
            gridEmoji.addView(tv);
        }

        // Color grid
        gridColor.setColumnCount(6);
        for (String col : COLORS) {
            View sw = new View(requireContext());
            int sz = (int)(36*dp);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = sz; lp.height = sz;
            lp.setMargins(5,5,5,5);
            sw.setLayoutParams(lp);
            sw.setBackgroundResource(R.drawable.bg_color_swatch);
            sw.getBackground().setTint(Color.parseColor(col));
            sw.setSelected(col.equals(selColor));
            sw.setOnClickListener(v -> {
                selColor = col;
                for (int j = 0; j < gridColor.getChildCount(); j++)
                    gridColor.getChildAt(j).setSelected(false);
                sw.setSelected(true);
            });
            gridColor.addView(sw);
        }

        root.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());
        root.findViewById(R.id.btn_create).setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            if (name.isEmpty()) { etName.setError("Obrigatório"); return; }
            String desc = etDesc.getText() != null ? etDesc.getText().toString().trim() : "";
            Counter c2 = Counter.create(name, selEmoji, desc, selColor);
            if (callback != null) callback.onCreated(c2);
            dismiss();
        });
    }
}
