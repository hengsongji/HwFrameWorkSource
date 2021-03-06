package android.media;

import android.os.Handler;
import android.view.Surface;
import dalvik.system.CloseGuard;

public final class RemoteDisplay {
    public static final int DISPLAY_ERROR_CONNECTION_DROPPED = 2;
    public static final int DISPLAY_ERROR_UNKOWN = 1;
    public static final int DISPLAY_FLAG_PEER_HDCP_SUPPORT = 256;
    public static final int DISPLAY_FLAG_SECURE = 1;
    public static final int DISPLAY_WARING_PLAY_ACTION = 50;
    private final CloseGuard mGuard = CloseGuard.get();
    private final Handler mHandler;
    private final Listener mListener;
    private final String mOpPackageName;
    private long mPtr;

    public interface Listener {
        int notifyUibcCreate(int i);

        void onDisplayConnected(Surface surface, int i, int i2, int i3, int i4);

        void onDisplayDisconnected();

        void onDisplayError(int i);
    }

    private native void nativeCheckVerificationResult(long j, boolean z);

    private native void nativeDispose(long j);

    private native long nativeListen(String str, String str2);

    private native void nativePause(long j);

    private native void nativeResume(long j);

    private native void nativeSetDisplayParameters(long j, boolean z, String str);

    private native void nativeSetVideoBitrate(long j, int i);

    private native void nativeUpdateVideoPlayState(long j, int i);

    private RemoteDisplay(Listener listener, Handler handler, String opPackageName) {
        this.mListener = listener;
        this.mHandler = handler;
        this.mOpPackageName = opPackageName;
    }

    protected void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public static RemoteDisplay listen(String iface, Listener listener, Handler handler, String opPackageName) {
        if (iface == null) {
            throw new IllegalArgumentException("iface must not be null");
        } else if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else if (handler != null) {
            RemoteDisplay display = new RemoteDisplay(listener, handler, opPackageName);
            display.startListening(iface);
            return display;
        } else {
            throw new IllegalArgumentException("handler must not be null");
        }
    }

    public void dispose() {
        dispose(false);
    }

    public void pause() {
        nativePause(this.mPtr);
    }

    public void resume() {
        nativeResume(this.mPtr);
    }

    private void dispose(boolean finalized) {
        if (this.mPtr != 0) {
            if (this.mGuard != null) {
                if (finalized) {
                    this.mGuard.warnIfOpen();
                } else {
                    this.mGuard.close();
                }
            }
            nativeDispose(this.mPtr);
            this.mPtr = 0;
        }
    }

    private void startListening(String iface) {
        this.mPtr = nativeListen(iface, this.mOpPackageName);
        if (this.mPtr != 0) {
            this.mGuard.open("dispose");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Could not start listening for remote display connection on \"");
        stringBuilder.append(iface);
        stringBuilder.append("\"");
        throw new IllegalStateException(stringBuilder.toString());
    }

    private void notifyDisplayConnected(Surface surface, int width, int height, int flags, int session) {
        final Surface surface2 = surface;
        final int i = width;
        final int i2 = height;
        final int i3 = flags;
        final int i4 = session;
        this.mHandler.post(new Runnable() {
            public void run() {
                RemoteDisplay.this.mListener.onDisplayConnected(surface2, i, i2, i3, i4);
            }
        });
    }

    private void notifyDisplayDisconnected() {
        this.mHandler.post(new Runnable() {
            public void run() {
                RemoteDisplay.this.mListener.onDisplayDisconnected();
            }
        });
    }

    private void notifyDisplayError(final int error) {
        this.mHandler.post(new Runnable() {
            public void run() {
                RemoteDisplay.this.mListener.onDisplayError(error);
            }
        });
    }

    public void setVideoBitrate(int bitrate) {
        nativeSetVideoBitrate(this.mPtr, bitrate);
    }

    public void setDisplayParameters(boolean isContentProtectionRequired, String verificationCode) {
        nativeSetDisplayParameters(this.mPtr, isContentProtectionRequired, verificationCode);
    }

    public void checkVerificationResult(boolean isRight) {
        nativeCheckVerificationResult(this.mPtr, isRight);
    }

    public void updateVideoPlayState(int state) {
        nativeUpdateVideoPlayState(this.mPtr, state);
    }

    private int notifyUibcCreate(int capSupport) {
        return this.mListener.notifyUibcCreate(capSupport);
    }
}
