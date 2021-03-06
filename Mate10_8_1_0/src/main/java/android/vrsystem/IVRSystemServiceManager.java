package android.vrsystem;

import android.content.Context;

public interface IVRSystemServiceManager {
    public static final String VR_MANAGER = "vr_system";

    void acceptInCall(Context context);

    void endInCall(Context context);

    String getContactName(Context context, String str);

    int getHelmetBattery(Context context);

    int getHelmetBrightness(Context context);

    boolean isVRApplication(Context context, String str);

    boolean isVRDisplayConnected();

    boolean isVRMode();

    boolean isVirtualScreenMode();

    void registerExpandListener(Context context, IVRListener iVRListener);

    void registerVRListener(Context context, IVRListener iVRListener);

    void setHelmetBrightness(Context context, int i);

    void setVRDisplayConnected(boolean z);

    void setVirtualScreenMode(boolean z);

    void unregisterVRListener(Context context, IVRListener iVRListener);
}
