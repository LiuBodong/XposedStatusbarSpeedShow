package org.codebase.nss;

import android.content.res.XResources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class HookLayout extends XC_LayoutInflated {

    @Override
    public void handleLayoutInflated(LayoutInflatedParam liParam) throws Throwable {
        XResources res = liParam.res;
        View view = liParam.view;
        int identifier = res.getIdentifier("status_bar_left_side", "id", "com.android.systemui");
        LinearLayout linearLayout = (LinearLayout) view.findViewById(identifier);
        LinearLayout speedLayout = new NetSpeedLayout(linearLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        speedLayout.setLayoutParams(layoutParams);
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = linearLayout.getChildAt(i);
            String simpleName = v.getClass().getSimpleName();
            if (simpleName.equals("Clock")) {
                linearLayout.addView(speedLayout, i + 1);
                break;
            }
        }
    }
}
