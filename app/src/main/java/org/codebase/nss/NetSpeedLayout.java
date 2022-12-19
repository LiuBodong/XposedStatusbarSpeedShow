package org.codebase.nss;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

@SuppressWarnings("all")
public class NetSpeedLayout extends LinearLayout {

    private static final String[] units = new String[]{
            "B",
            "KiB",
            "MiB",
            "GiB",
            "TiB"
    };
    private long lastMs = System.currentTimeMillis();
    private long rx = -1;
    private long tx = -1;

    public NetSpeedLayout(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        TextView upView = new TextView(getContext());
        upView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9F);
        upView.setSingleLine();
        upView.setLineHeight(0);
        upView.setTextColor(Color.RED);
        TextView downView = new TextView(getContext());
        downView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9F);
        downView.setSingleLine();
        downView.setLineHeight(0);
        downView.setTextColor(Color.RED);
        addView(upView, WRAP_CONTENT, WRAP_CONTENT);
        addView(downView, WRAP_CONTENT, WRAP_CONTENT);
        Handler handler = new Handler();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                long currentMs = System.currentTimeMillis();
                downView.setText(getDownSpeed(currentMs));
                upView.setText(getUpSpeed(currentMs));
                lastMs = currentMs;
                handler.postDelayed(this, 1000L);
            }
        };
        task.run();
    }

    private String getUpSpeed(long currentMs) {
        if (currentMs == lastMs) {
            return "↑0.0KiB";
        }
        long ms = currentMs - lastMs;
        long totalTxBytes = TrafficStats.getTotalTxBytes();
        if (this.tx <= 0) {
            this.tx = totalTxBytes;
        }
        double bytes = Double.valueOf(totalTxBytes - tx);
        double sec = ms / 1000d;
        double speed = bytes / sec;
        this.tx = totalTxBytes;
        String speedWithUnit = calculateSpeed(speed);
        return String.format(Locale.getDefault(), "↑%s", speedWithUnit);
    }

    private String getDownSpeed(long currentMs) {
        if (currentMs == lastMs) {
            return "↓0.0KiB";
        }
        long ms = currentMs - lastMs;
        long totalRxBytes = TrafficStats.getTotalRxBytes();
        if (this.rx <= 0) {
            this.rx = totalRxBytes;
        }
        double bytes = Double.valueOf(totalRxBytes - rx);
        double sec = ms / 1000d;
        double speed = bytes / sec;
        this.rx = totalRxBytes;
        String speedWithUnit = calculateSpeed(speed);
        return String.format(Locale.getDefault(), "↓%s", speedWithUnit);
    }

    private String calculateSpeed(double speed) {
        int i = 0;
        while (speed > 1024 && i < units.length) {
            speed = speed / 1024;
            i++;
        }
        return String.format("%.1f%s", speed, units[i]);
    }

}
