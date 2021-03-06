package android.media.hwmnote;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

class HwMnoteParser {
    protected static final short BIG_ENDIAN_TAG = (short) 19789;
    protected static final int DEFAULT_IFD0_OFFSET = 8;
    public static final int EVENT_END = 3;
    public static final int EVENT_NEW_TAG = 1;
    public static final int EVENT_START_OF_IFD = 0;
    public static final int EVENT_VALUE_OF_REGISTERED_TAG = 2;
    protected static final byte[] HW_MNOTE_HEADER = new byte[]{(byte) 72, (byte) 85, (byte) 65, (byte) 87, (byte) 69, (byte) 73, (byte) 0, (byte) 0};
    private static final short HW_MNOTE_TAG_FACE_IFD = HwMnoteInterfaceImpl.getTrueTagKey(HwMnoteInterfaceUtils.HW_MNOTE_TAG_FACE_IFD);
    private static final short HW_MNOTE_TAG_SCENE_IFD = HwMnoteInterfaceImpl.getTrueTagKey(HwMnoteInterfaceUtils.HW_MNOTE_TAG_SCENE_IFD);
    protected static final short LITTLE_ENDIAN_TAG = (short) 18761;
    private static final boolean LOGV = false;
    protected static final int OFFSET_SIZE = 2;
    public static final int OPTION_HW_MNOTE_IFD_0 = 1;
    public static final int OPTION_HW_MNOTE_IFD_FACE = 4;
    public static final int OPTION_HW_MNOTE_IFD_SCENE = 2;
    private static final String TAG = "HwMnoteParser";
    protected static final int TAG_SIZE = 12;
    protected static final short TIFF_HEADER_TAIL = (short) 42;
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private boolean mContainHwMnoteData = false;
    private final TreeMap<Integer, Object> mCorrespondingEvent = new TreeMap();
    private byte[] mDataAboveIfd0;
    private int mIfdStartOffset = 0;
    private int mIfdType;
    private final HwMnoteInterfaceImpl mInterface;
    private boolean mNeedToParseOffsetsInCurrentIfd;
    private int mNumOfTagInIfd = 0;
    private final int mOptions;
    private HwMnoteTag mTag;
    private final CountedDataInputStream mTiffStream;

    private static class HwMnoteTagEvent {
        boolean isRequested;
        HwMnoteTag tag;

        HwMnoteTagEvent(HwMnoteTag tag, boolean isRequireByUser) {
            this.tag = tag;
            this.isRequested = isRequireByUser;
        }
    }

    private static class IfdEvent {
        int ifd;
        boolean isRequested;

        IfdEvent(int ifd, boolean isInterestedIfd) {
            this.ifd = ifd;
            this.isRequested = isInterestedIfd;
        }
    }

    private boolean isIfdRequested(int ifdType) {
        boolean z = true;
        switch (ifdType) {
            case 0:
                if ((this.mOptions & 1) == 0) {
                    z = false;
                }
                return z;
            case 1:
                if ((this.mOptions & 2) == 0) {
                    z = false;
                }
                return z;
            case 2:
                if ((this.mOptions & 4) == 0) {
                    z = false;
                }
                return z;
            default:
                return false;
        }
    }

    private HwMnoteParser(InputStream inputStream, int options, HwMnoteInterfaceImpl iRef) throws IOException, Exception {
        if (inputStream != null) {
            this.mInterface = iRef;
            this.mContainHwMnoteData = seekTiffData(inputStream);
            this.mTiffStream = new CountedDataInputStream(inputStream);
            this.mOptions = options;
            if (this.mContainHwMnoteData) {
                parseTiffHeader();
                long offset = this.mTiffStream.readUnsignedInt();
                if (offset <= 2147483647L) {
                    this.mIfdType = 0;
                    if (isIfdRequested(0) || needToParseOffsetsInCurrentIfd()) {
                        registerIfd(0, offset);
                        if (offset != 8) {
                            this.mDataAboveIfd0 = new byte[(((int) offset) - 8)];
                            read(this.mDataAboveIfd0);
                        }
                    }
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid offset ");
                stringBuilder.append(offset);
                throw new Exception(stringBuilder.toString());
            }
            return;
        }
        throw new IOException("Null argument inputStream to HwMnoteParser");
    }

    protected static HwMnoteParser parse(InputStream inputStream, HwMnoteInterfaceImpl iRef) throws IOException, Exception {
        return new HwMnoteParser(inputStream, 7, iRef);
    }

    protected int next() throws IOException, Exception {
        if (!this.mContainHwMnoteData) {
            return 3;
        }
        int offset = this.mTiffStream.getReadByteCount();
        int endOfTags = (this.mIfdStartOffset + 2) + (12 * this.mNumOfTagInIfd);
        if (offset < endOfTags) {
            this.mTag = readTag();
            if (this.mTag == null) {
                return next();
            }
            if (this.mNeedToParseOffsetsInCurrentIfd) {
                checkOffsetTag(this.mTag);
            }
            return 1;
        }
        if (offset == endOfTags) {
            doEndOfTags();
        }
        while (this.mCorrespondingEvent.size() != 0) {
            Entry<Integer, Object> entry = this.mCorrespondingEvent.pollFirstEntry();
            HwMnoteTagEvent event = entry.getValue();
            try {
                skipTo(((Integer) entry.getKey()).intValue());
                if (event instanceof IfdEvent) {
                    Integer x = doIfdEvent(entry, (IfdEvent) event);
                    if (x != null) {
                        return x.intValue();
                    }
                }
                HwMnoteTagEvent tagEvent = event;
                this.mTag = tagEvent.tag;
                if (this.mTag.getDataType() != (short) 7) {
                    readFullTagValue(this.mTag);
                    checkOffsetTag(this.mTag);
                }
                if (tagEvent.isRequested) {
                    return 2;
                }
            } catch (IOException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to skip to data at: ");
                stringBuilder.append(entry.getKey());
                stringBuilder.append(" for ");
                stringBuilder.append(event.getClass().getName());
                stringBuilder.append(", the file may be broken.");
                Log.w(str, stringBuilder.toString());
            }
        }
        return 3;
    }

    private void doEndOfTags() throws IOException {
        if (this.mIfdType != 0) {
            int offsetSize = 4;
            if (this.mCorrespondingEvent.size() > 0) {
                offsetSize = ((Integer) this.mCorrespondingEvent.firstEntry().getKey()).intValue() - this.mTiffStream.getReadByteCount();
            }
            if (offsetSize < 4) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid size of link to next IFD: ");
                stringBuilder.append(offsetSize);
                Log.w(str, stringBuilder.toString());
                return;
            }
            long ifdOffset = readUnsignedLong();
            if (ifdOffset != 0) {
                String str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Invalid link to next IFD: ");
                stringBuilder2.append(ifdOffset);
                Log.w(str2, stringBuilder2.toString());
            }
        }
    }

    private Integer doIfdEvent(Entry<Integer, Object> entry, IfdEvent event) throws IOException, Exception {
        this.mIfdType = event.ifd;
        this.mNumOfTagInIfd = this.mTiffStream.readUnsignedShort();
        this.mIfdStartOffset = ((Integer) entry.getKey()).intValue();
        this.mNeedToParseOffsetsInCurrentIfd = needToParseOffsetsInCurrentIfd();
        if (event.isRequested) {
            return Integer.valueOf(0);
        }
        skipRemainingTagsInCurrentIfd();
        return null;
    }

    protected void skipRemainingTagsInCurrentIfd() throws IOException, Exception {
        int endOfTags = (this.mIfdStartOffset + 2) + (12 * this.mNumOfTagInIfd);
        int offset = this.mTiffStream.getReadByteCount();
        if (offset <= endOfTags) {
            if (this.mNeedToParseOffsetsInCurrentIfd) {
                while (offset < endOfTags) {
                    this.mTag = readTag();
                    offset += 12;
                    if (this.mTag != null) {
                        checkOffsetTag(this.mTag);
                    }
                }
            } else {
                skipTo(endOfTags);
            }
            readUnsignedLong();
        }
    }

    private boolean needToParseOffsetsInCurrentIfd() {
        switch (this.mIfdType) {
            case 0:
                boolean z = true;
                if (!(isIfdRequested(1) || isIfdRequested(2))) {
                    z = false;
                }
                return z;
            case 1:
                return false;
            case 2:
                return false;
            default:
                return false;
        }
    }

    protected HwMnoteTag getTag() {
        return this.mTag;
    }

    protected int getTagCountInCurrentIfd() {
        return this.mNumOfTagInIfd;
    }

    protected int getCurrentIfd() {
        return this.mIfdType;
    }

    private void skipTo(int offset) throws IOException {
        this.mTiffStream.skipTo((long) offset);
        while (!this.mCorrespondingEvent.isEmpty() && ((Integer) this.mCorrespondingEvent.firstKey()).intValue() < offset) {
            this.mCorrespondingEvent.pollFirstEntry();
        }
    }

    protected void registerForTagValue(HwMnoteTag tag) {
        if (tag.getOffset() >= this.mTiffStream.getReadByteCount()) {
            this.mCorrespondingEvent.put(Integer.valueOf(tag.getOffset()), new HwMnoteTagEvent(tag, true));
        }
    }

    private void registerIfd(int ifdType, long offset) {
        this.mCorrespondingEvent.put(Integer.valueOf((int) offset), new IfdEvent(ifdType, isIfdRequested(ifdType)));
    }

    private HwMnoteTag readTag() throws IOException, Exception {
        short tagId = this.mTiffStream.readShort();
        short dataFormat = this.mTiffStream.readShort();
        long numOfComp = this.mTiffStream.readUnsignedInt();
        if (numOfComp > 2147483647L) {
            throw new Exception("Number of component is larger then Integer.MAX_VALUE");
        } else if (HwMnoteTag.isValidType(dataFormat)) {
            HwMnoteTag tag = new HwMnoteTag(tagId, dataFormat, (int) numOfComp, this.mIfdType, ((int) numOfComp) != 0);
            int dataSize = tag.getDataSize();
            if (dataSize > 4) {
                long offset = this.mTiffStream.readUnsignedInt();
                if (offset <= 2147483647L) {
                    tag.setOffset((int) offset);
                } else {
                    throw new Exception("offset is larger then Integer.MAX_VALUE");
                }
            }
            boolean defCount = tag.hasDefinedCount();
            tag.setHasDefinedCount(false);
            readFullTagValue(tag);
            tag.setHasDefinedCount(defCount);
            if (this.mTiffStream.skip((long) (4 - dataSize)) != ((long) (4 - dataSize))) {
                Log.w(TAG, String.format("Can't skip %d byte(s)", new Object[]{Integer.valueOf(4 - dataSize)}));
            }
            tag.setOffset(this.mTiffStream.getReadByteCount() - 4);
            return tag;
        } else {
            Log.w(TAG, String.format("Tag %04x: Invalid data type %d", new Object[]{Short.valueOf(tagId), Short.valueOf(dataFormat)}));
            if (this.mTiffStream.skip(4) != 4) {
                Log.w(TAG, "Can't skip 4 bytes");
            }
            return null;
        }
    }

    private void checkOffsetTag(HwMnoteTag tag) {
        if (tag.getComponentCount() != 0) {
            short tid = tag.getTagId();
            int ifd = tag.getIfd();
            if (tid == HW_MNOTE_TAG_SCENE_IFD && checkAllowed(ifd, HwMnoteInterfaceUtils.HW_MNOTE_TAG_SCENE_IFD)) {
                if (isIfdRequested(1)) {
                    registerIfd(1, tag.getValueAt(0));
                }
            } else if (tid == HW_MNOTE_TAG_FACE_IFD && checkAllowed(ifd, HwMnoteInterfaceUtils.HW_MNOTE_TAG_FACE_IFD) && isIfdRequested(2)) {
                registerIfd(2, tag.getValueAt(0));
            }
        }
    }

    private boolean checkAllowed(int ifd, int tagId) {
        int info = this.mInterface.getTagInfo().get(tagId);
        if (info == 0) {
            return false;
        }
        return HwMnoteInterfaceImpl.isIfdAllowed(info, ifd);
    }

    protected void readFullTagValue(HwMnoteTag tag) throws IOException {
        if (isValidHwMnote(tag.getDataType())) {
            int size = tag.getComponentCount();
            if (this.mCorrespondingEvent.size() > 0 && ((Integer) this.mCorrespondingEvent.firstEntry().getKey()).intValue() < this.mTiffStream.getReadByteCount() + size) {
                doTagValueOverlaps(tag, this.mCorrespondingEvent.firstEntry().getValue());
            }
        }
        short dataType = tag.getDataType();
        if (dataType == (short) 4) {
            long[] value = new long[tag.getComponentCount()];
            int n = value.length;
            for (int i = 0; i < n; i++) {
                value[i] = readUnsignedLong();
            }
            tag.setValue(value);
        } else if (dataType == (short) 7) {
            byte[] buf = new byte[tag.getComponentCount()];
            read(buf);
            tag.setValue(buf);
        }
    }

    private boolean isValidHwMnote(short type) {
        return type == (short) 7;
    }

    private void doTagValueOverlaps(HwMnoteTag tag, Object event) {
        String str;
        StringBuilder stringBuilder;
        if (event instanceof IfdEvent) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Ifd ");
            stringBuilder.append(((IfdEvent) event).ifd);
            stringBuilder.append(" overlaps value for tag: \n");
            stringBuilder.append(tag.getTagId());
            Log.w(str, stringBuilder.toString());
        } else if (event instanceof HwMnoteTagEvent) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Tag value for tag: \n");
            stringBuilder.append(((HwMnoteTagEvent) event).tag.getTagId());
            stringBuilder.append(" overlaps value for tag: \n");
            stringBuilder.append(tag.getTagId());
            Log.w(str, stringBuilder.toString());
        }
        int size = ((Integer) this.mCorrespondingEvent.firstEntry().getKey()).intValue() - this.mTiffStream.getReadByteCount();
        String str2 = TAG;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Invalid size of tag: \n");
        stringBuilder2.append(tag.getTagId());
        stringBuilder2.append(" setting count to: ");
        stringBuilder2.append(size);
        Log.w(str2, stringBuilder2.toString());
        tag.forceSetComponentCount(size);
    }

    private void parseTiffHeader() throws IOException, Exception {
        short byteOrder = this.mTiffStream.readShort();
        if (LITTLE_ENDIAN_TAG == byteOrder) {
            this.mTiffStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        } else if (BIG_ENDIAN_TAG == byteOrder) {
            this.mTiffStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        } else {
            throw new Exception("Invalid TIFF header");
        }
        if (this.mTiffStream.readShort() != (short) 42) {
            throw new Exception("Invalid TIFF header");
        }
    }

    private boolean seekTiffData(InputStream inputStream) throws IOException, Exception {
        byte[] header = new byte[8];
        if (new CountedDataInputStream(inputStream).read(header, 0, 8) < 8) {
            return false;
        }
        if (Arrays.equals(header, HW_MNOTE_HEADER)) {
            return true;
        }
        Log.w(TAG, "Invalid Huawei Maker Note.");
        return false;
    }

    protected int read(byte[] buffer) throws IOException {
        return this.mTiffStream.read(buffer);
    }

    protected int readUnsignedShort() throws IOException {
        return this.mTiffStream.readShort() & 65535;
    }

    protected long readUnsignedLong() throws IOException {
        return ((long) readLong()) & 4294967295L;
    }

    protected int readLong() throws IOException {
        return this.mTiffStream.readInt();
    }

    protected ByteOrder getByteOrder() {
        return this.mTiffStream.getByteOrder();
    }
}
