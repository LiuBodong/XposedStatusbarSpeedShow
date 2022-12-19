package org.codebase.nss;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class MainHook implements IXposedHookZygoteInit, IXposedHookInitPackageResources {

    private static final String PKG_NAME = "com.android.systemui";

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (!resParam.packageName.equals(PKG_NAME)) {
            return;
        }
        resParam.res.hookLayout(PKG_NAME, "layout", "status_bar", new HookLayout());
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }

}
