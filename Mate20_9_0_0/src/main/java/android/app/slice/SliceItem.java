package android.app.slice;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.RemoteViews;
import com.android.internal.util.ArrayUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

public final class SliceItem implements Parcelable {
    public static final Creator<SliceItem> CREATOR = new Creator<SliceItem>() {
        public SliceItem createFromParcel(Parcel in) {
            return new SliceItem(in);
        }

        public SliceItem[] newArray(int size) {
            return new SliceItem[size];
        }
    };
    public static final String FORMAT_ACTION = "action";
    public static final String FORMAT_BUNDLE = "bundle";
    public static final String FORMAT_IMAGE = "image";
    public static final String FORMAT_INT = "int";
    public static final String FORMAT_LONG = "long";
    public static final String FORMAT_REMOTE_INPUT = "input";
    public static final String FORMAT_SLICE = "slice";
    public static final String FORMAT_TEXT = "text";
    @Deprecated
    public static final String FORMAT_TIMESTAMP = "long";
    private static final String TAG = "SliceItem";
    private final String mFormat;
    protected String[] mHints;
    private final Object mObj;
    private final String mSubType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SliceType {
    }

    public SliceItem(Object obj, String format, String subType, List<String> hints) {
        this(obj, format, subType, (String[]) hints.toArray(new String[hints.size()]));
    }

    public SliceItem(Object obj, String format, String subType, String[] hints) {
        this.mHints = hints;
        this.mFormat = format;
        this.mSubType = subType;
        this.mObj = obj;
    }

    public SliceItem(PendingIntent intent, Slice slice, String format, String subType, String[] hints) {
        this(new Pair(intent, slice), format, subType, hints);
    }

    public List<String> getHints() {
        return Arrays.asList(this.mHints);
    }

    public String getFormat() {
        return this.mFormat;
    }

    public String getSubType() {
        return this.mSubType;
    }

    public CharSequence getText() {
        return (CharSequence) this.mObj;
    }

    public Bundle getBundle() {
        return (Bundle) this.mObj;
    }

    public Icon getIcon() {
        return (Icon) this.mObj;
    }

    public PendingIntent getAction() {
        return (PendingIntent) ((Pair) this.mObj).first;
    }

    public RemoteViews getRemoteView() {
        return (RemoteViews) this.mObj;
    }

    public RemoteInput getRemoteInput() {
        return (RemoteInput) this.mObj;
    }

    public int getInt() {
        return ((Integer) this.mObj).intValue();
    }

    public Slice getSlice() {
        if ("action".equals(getFormat())) {
            return (Slice) ((Pair) this.mObj).second;
        }
        return (Slice) this.mObj;
    }

    public long getLong() {
        return ((Long) this.mObj).longValue();
    }

    @Deprecated
    public long getTimestamp() {
        return ((Long) this.mObj).longValue();
    }

    public boolean hasHint(String hint) {
        return ArrayUtils.contains(this.mHints, hint);
    }

    public SliceItem(Parcel in) {
        this.mHints = in.readStringArray();
        this.mFormat = in.readString();
        this.mSubType = in.readString();
        this.mObj = readObj(this.mFormat, in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.mHints);
        dest.writeString(this.mFormat);
        dest.writeString(this.mSubType);
        writeObj(dest, flags, this.mObj, this.mFormat);
    }

    public boolean hasHints(String[] hints) {
        if (hints == null) {
            return true;
        }
        for (String hint : hints) {
            if (!TextUtils.isEmpty(hint) && !ArrayUtils.contains(this.mHints, hint)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnyHints(String[] hints) {
        if (hints == null) {
            return false;
        }
        for (String hint : hints) {
            if (ArrayUtils.contains(this.mHints, hint)) {
                return true;
            }
        }
        return false;
    }

    private static String getBaseType(String type) {
        int index = type.indexOf(47);
        if (index >= 0) {
            return type.substring(0, index);
        }
        return type;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void writeObj(Parcel dest, int flags, Object obj, String type) {
        Object obj2;
        String baseType = getBaseType(type);
        switch (baseType.hashCode()) {
            case -1422950858:
                if (baseType.equals("action")) {
                    obj2 = 4;
                    break;
                }
            case -1377881982:
                if (baseType.equals(FORMAT_BUNDLE)) {
                    obj2 = 3;
                    break;
                }
            case 104431:
                if (baseType.equals(FORMAT_INT)) {
                    obj2 = 6;
                    break;
                }
            case 3327612:
                if (baseType.equals("long")) {
                    obj2 = 7;
                    break;
                }
            case 3556653:
                if (baseType.equals(FORMAT_TEXT)) {
                    obj2 = 5;
                    break;
                }
            case 100313435:
                if (baseType.equals(FORMAT_IMAGE)) {
                    obj2 = 1;
                    break;
                }
            case 100358090:
                if (baseType.equals("input")) {
                    obj2 = 2;
                    break;
                }
            case 109526418:
                if (baseType.equals("slice")) {
                    obj2 = null;
                    break;
                }
            default:
                obj2 = -1;
                break;
        }
        switch (obj2) {
            case null:
            case 1:
            case 2:
            case 3:
                ((Parcelable) obj).writeToParcel(dest, flags);
                return;
            case 4:
                ((PendingIntent) ((Pair) obj).first).writeToParcel(dest, flags);
                ((Slice) ((Pair) obj).second).writeToParcel(dest, flags);
                return;
            case 5:
                TextUtils.writeToParcel((CharSequence) obj, dest, flags);
                return;
            case 6:
                dest.writeInt(((Integer) obj).intValue());
                return;
            case 7:
                dest.writeLong(((Long) obj).longValue());
                return;
            default:
                return;
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Object readObj(String type, Parcel in) {
        Object obj;
        String baseType = getBaseType(type);
        switch (baseType.hashCode()) {
            case -1422950858:
                if (baseType.equals("action")) {
                    obj = 3;
                    break;
                }
            case -1377881982:
                if (baseType.equals(FORMAT_BUNDLE)) {
                    obj = 7;
                    break;
                }
            case 104431:
                if (baseType.equals(FORMAT_INT)) {
                    obj = 4;
                    break;
                }
            case 3327612:
                if (baseType.equals("long")) {
                    obj = 5;
                    break;
                }
            case 3556653:
                if (baseType.equals(FORMAT_TEXT)) {
                    obj = 1;
                    break;
                }
            case 100313435:
                if (baseType.equals(FORMAT_IMAGE)) {
                    obj = 2;
                    break;
                }
            case 100358090:
                if (baseType.equals("input")) {
                    obj = 6;
                    break;
                }
            case 109526418:
                if (baseType.equals("slice")) {
                    obj = null;
                    break;
                }
            default:
                obj = -1;
                break;
        }
        switch (obj) {
            case null:
                return Slice.CREATOR.createFromParcel(in);
            case 1:
                return TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            case 2:
                return Icon.CREATOR.createFromParcel(in);
            case 3:
                return new Pair((PendingIntent) PendingIntent.CREATOR.createFromParcel(in), (Slice) Slice.CREATOR.createFromParcel(in));
            case 4:
                return Integer.valueOf(in.readInt());
            case 5:
                return Long.valueOf(in.readLong());
            case 6:
                return RemoteInput.CREATOR.createFromParcel(in);
            case 7:
                return Bundle.CREATOR.createFromParcel(in);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported type ");
                stringBuilder.append(type);
                throw new RuntimeException(stringBuilder.toString());
        }
    }
}
