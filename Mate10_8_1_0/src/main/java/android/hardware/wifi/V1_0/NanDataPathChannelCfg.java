package android.hardware.wifi.V1_0;

public final class NanDataPathChannelCfg {
    public static final int CHANNEL_NOT_REQUESTED = 0;
    public static final int FORCE_CHANNEL_SETUP = 2;
    public static final int REQUEST_CHANNEL_SETUP = 1;

    public static final java.lang.String dumpBitfield(int r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.hardware.wifi.V1_0.NanDataPathChannelCfg.dumpBitfield(int):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:31)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:296)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:199)
Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: not-int
	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:568)
	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:56)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:102)
	... 5 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.wifi.V1_0.NanDataPathChannelCfg.dumpBitfield(int):java.lang.String");
    }

    public static final String toString(int o) {
        if (o == 0) {
            return "CHANNEL_NOT_REQUESTED";
        }
        if (o == 1) {
            return "REQUEST_CHANNEL_SETUP";
        }
        if (o == 2) {
            return "FORCE_CHANNEL_SETUP";
        }
        return "0x" + Integer.toHexString(o);
    }
}
