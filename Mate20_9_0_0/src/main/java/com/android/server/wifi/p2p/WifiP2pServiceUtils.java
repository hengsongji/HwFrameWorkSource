package com.android.server.wifi.p2p;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.os.Binder;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import com.android.server.wifi.p2p.WifiP2pServiceImpl.P2pStateMachine;
import com.android.server.wifi.p2p.WifiP2pServiceImpl.P2pStateMachine.InactiveState;
import com.android.server.wifi.p2p.WifiP2pServiceImpl.P2pStateMachine.OngoingGroupRemovalState;
import com.huawei.utils.reflect.EasyInvokeUtils;
import com.huawei.utils.reflect.FieldObject;
import com.huawei.utils.reflect.MethodObject;
import com.huawei.utils.reflect.annotation.GetField;
import com.huawei.utils.reflect.annotation.InvokeMethod;
import com.huawei.utils.reflect.annotation.SetField;
import java.util.List;

public class WifiP2pServiceUtils extends EasyInvokeUtils {
    private static final String TAG = "WifiP2pServiceUtils";
    FieldObject<Integer> PEER_CONNECTION_USER_ACCEPT;
    MethodObject<Void> enableBTCoex;
    MethodObject<Void> handleGroupRemoved;
    FieldObject<Boolean> mAutonomousGroup;
    FieldObject<WifiP2pGroup> mGroup;
    FieldObject<InactiveState> mInactiveState;
    FieldObject<OngoingGroupRemovalState> mOngoingGroupRemovalState;
    FieldObject<WifiP2pDevice> mThisDevice;
    MethodObject<Void> replyToMessage;
    MethodObject<Void> sendP2pConnectionChangedBroadcast;

    @GetField(fieldObject = "PEER_CONNECTION_USER_ACCEPT")
    public int getPeerConnectionUserAccept(WifiP2pServiceImpl wifiP2pService) {
        return ((Integer) getField(this.PEER_CONNECTION_USER_ACCEPT, wifiP2pService)).intValue();
    }

    @GetField(fieldObject = "mGroup")
    public WifiP2pGroup getmGroup(P2pStateMachine p2pStateMachine) {
        return (WifiP2pGroup) getField(this.mGroup, p2pStateMachine);
    }

    @InvokeMethod(methodObject = "sendP2pConnectionChangedBroadcast")
    public void sendP2pConnectionChangedBroadcast(P2pStateMachine p2pStateMachine) {
        invokeMethod(this.sendP2pConnectionChangedBroadcast, p2pStateMachine, new Object[0]);
    }

    @GetField(fieldObject = "mInactiveState")
    public InactiveState getmInactiveState(P2pStateMachine p2pStateMachine) {
        return (InactiveState) getField(this.mInactiveState, p2pStateMachine);
    }

    @GetField(fieldObject = "mOngoingGroupRemovalState")
    public OngoingGroupRemovalState getmOngoingGroupRemovalState(P2pStateMachine p2pStateMachine) {
        return (OngoingGroupRemovalState) getField(this.mOngoingGroupRemovalState, p2pStateMachine);
    }

    @GetField(fieldObject = "mThisDevice")
    public WifiP2pDevice getmThisDevice(WifiP2pServiceImpl wifiP2pService) {
        return (WifiP2pDevice) getField(this.mThisDevice, wifiP2pService);
    }

    @InvokeMethod(methodObject = "enableBTCoex")
    public void enableBTCoex(P2pStateMachine p2pStateMachine) {
        invokeMethod(this.enableBTCoex, p2pStateMachine, new Object[0]);
    }

    @InvokeMethod(methodObject = "handleGroupRemoved")
    public void handleGroupRemoved(P2pStateMachine p2pStateMachine) {
        invokeMethod(this.handleGroupRemoved, p2pStateMachine, new Object[0]);
    }

    @SetField(fieldObject = "mAutonomousGroup")
    public void setAutonomousGroup(WifiP2pServiceImpl wifiP2pService, boolean value) {
        setField(this.mAutonomousGroup, wifiP2pService, Boolean.valueOf(value));
    }

    @InvokeMethod(methodObject = "replyToMessage")
    public void replyToMessage(P2pStateMachine p2pStateMachine, Message msg, int what, int arg1) {
        invokeMethod(this.replyToMessage, p2pStateMachine, new Object[]{msg, Integer.valueOf(what), Integer.valueOf(arg1)});
    }

    public boolean checkSignMatchOrIsSystemApp(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }
        int matchResult = pm.checkSignatures(Binder.getCallingUid(), Process.myUid());
        if (matchResult == 0) {
            return true;
        }
        String pckName = "";
        StringBuilder stringBuilder;
        try {
            pckName = getAppName(Binder.getCallingPid(), context);
            if (pckName == null) {
                Log.e(TAG, "pckName is null");
                return false;
            }
            ApplicationInfo info = pm.getApplicationInfo(pckName, 0);
            if (info != null && (info.flags & 1) != 0) {
                return true;
            }
            String str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("HwWifiP2pService  checkSignMatchOrIsSystemAppMatch matchRe=");
            stringBuilder.append(matchResult);
            stringBuilder.append("pckName=");
            stringBuilder.append(pckName);
            Log.d(str, stringBuilder.toString());
            return false;
        } catch (Exception ex) {
            String str2 = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("isSystemApp not found app");
            stringBuilder.append(pckName);
            stringBuilder.append("exception=");
            stringBuilder.append(ex.toString());
            Log.e(str2, stringBuilder.toString());
            return false;
        }
    }

    public String getAppName(int pID, Context context) {
        String processName = "";
        List<RunningAppProcessInfo> appProcessList = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (appProcessList == null) {
            return null;
        }
        for (RunningAppProcessInfo appProcess : appProcessList) {
            if (appProcess.pid == pID) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
