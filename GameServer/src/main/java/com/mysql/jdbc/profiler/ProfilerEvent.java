package com.mysql.jdbc.profiler;

import com.mysql.jdbc.StringUtils;

import java.util.Date;

public class ProfilerEvent {
    public static final byte TYPE_WARN = 0;
    public static final byte TYPE_OBJECT_CREATION = 1;
    public static final byte TYPE_PREPARE = 2;
    public static final byte TYPE_QUERY = 3;
    public static final byte TYPE_EXECUTE = 4;
    public static final byte TYPE_FETCH = 5;
    public static final byte TYPE_SLOW_QUERY = 6;
    protected byte eventType;
    protected long connectionId;
    protected int statementId;
    protected int resultSetId;
    protected long eventCreationTime;
    protected long eventDuration;
    protected String durationUnits;
    protected int hostNameIndex;
    protected String hostName;
    protected int catalogIndex;
    protected String catalog;
    protected int eventCreationPointIndex;
    protected String eventCreationPointDesc;
    protected String message;

    public ProfilerEvent(byte eventType, String hostName, String catalog, long connectionId, int statementId, int resultSetId, long eventCreationTime, long eventDuration, String durationUnits, String eventCreationPointDesc, String eventCreationPoint, String message) {
        this.eventType = eventType;
        this.connectionId = connectionId;
        this.statementId = statementId;
        this.resultSetId = resultSetId;
        this.eventCreationTime = eventCreationTime;
        this.eventDuration = eventDuration;
        this.durationUnits = durationUnits;
        this.eventCreationPointDesc = eventCreationPointDesc;
        this.message = message;
    }

    public static ProfilerEvent unpack(byte[] buf) throws Exception {
        int pos = 0;

        byte eventType = buf[pos++];
        long connectionId = readInt(buf, pos);
        pos += 8;
        int statementId = readInt(buf, pos);
        pos += 4;
        int resultSetId = readInt(buf, pos);
        pos += 4;
        long eventCreationTime = readLong(buf, pos);
        pos += 8;
        long eventDuration = readLong(buf, pos);
        pos += 4;

        byte[] eventDurationUnits = readBytes(buf, pos);
        pos += 4;

        if (eventDurationUnits != null) {
            pos += eventDurationUnits.length;
        }

        readInt(buf, pos);
        pos += 4;
        byte[] eventCreationAsBytes = readBytes(buf, pos);
        pos += 4;

        if (eventCreationAsBytes != null) {
            pos += eventCreationAsBytes.length;
        }

        byte[] message = readBytes(buf, pos);
        pos += 4;

        if (message != null) {
            pos += message.length;
        }

        return new ProfilerEvent(eventType, "", "", connectionId, statementId, resultSetId, eventCreationTime, eventDuration, StringUtils.toString(eventDurationUnits, "ISO8859_1"), StringUtils.toString(eventCreationAsBytes, "ISO8859_1"), null, StringUtils.toString(message, "ISO8859_1"));
    }

    private static int writeInt(int i, byte[] buf, int pos) {
        buf[pos++] = (byte) (i & 0xFF);
        buf[pos++] = (byte) (i >>> 8);
        buf[pos++] = (byte) (i >>> 16);
        buf[pos++] = (byte) (i >>> 24);

        return pos;
    }

    private static int writeLong(long l, byte[] buf, int pos) {
        buf[pos++] = (byte) (int) (l & 0xFFL);
        buf[pos++] = (byte) (int) (l >>> 8L);
        buf[pos++] = (byte) (int) (l >>> 16L);
        buf[pos++] = (byte) (int) (l >>> 24L);
        buf[pos++] = (byte) (int) (l >>> 32L);
        buf[pos++] = (byte) (int) (l >>> 40L);
        buf[pos++] = (byte) (int) (l >>> 48L);
        buf[pos++] = (byte) (int) (l >>> 56L);

        return pos;
    }

    private static int writeBytes(byte[] msg, byte[] buf, int pos) {
        pos = writeInt(msg.length, buf, pos);

        System.arraycopy(msg, 0, buf, pos, msg.length);

        return pos + msg.length;
    }

    private static int readInt(byte[] buf, int pos) {
        return buf[pos++] & 0xFF | (buf[pos++] & 0xFF) << 8 | (buf[pos++] & 0xFF) << 16 | (buf[pos++] & 0xFF) << 24;
    }

    private static long readLong(byte[] buf, int pos) {
        return (buf[pos++] & 0xFF) | (buf[pos++] & 0xFF) << 8L | (buf[pos++] & 0xFF) << 16L | (buf[pos++] & 0xFF) << 24L | (buf[pos++] & 0xFF) << 32L | (buf[pos++] & 0xFF) << 40L | (buf[pos++] & 0xFF) << 48L | (buf[pos++] & 0xFF) << 56L;
    }

    private static byte[] readBytes(byte[] buf, int pos) {
        int length = readInt(buf, pos);

        pos += 4;

        byte[] msg = new byte[length];
        System.arraycopy(buf, pos, msg, 0, length);

        return msg;
    }

    public String getEventCreationPointAsString() {
        return this.eventCreationPointDesc;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(32);

        switch (this.eventType) {
            case 4:
                buf.append("EXECUTE");
                break;

            case 5:
                buf.append("FETCH");
                break;

            case 1:
                buf.append("CONSTRUCT");
                break;

            case 2:
                buf.append("PREPARE");
                break;

            case 3:
                buf.append("QUERY");
                break;

            case 0:
                buf.append("WARN");
                break;
            case 6:
                buf.append("SLOW QUERY");
                break;
            default:
                buf.append("UNKNOWN");
                break;
        }
        buf.append(" created: ");
        buf.append(new Date(this.eventCreationTime));
        buf.append(" duration: ");
        buf.append(this.eventDuration);
        buf.append(" connection: ");
        buf.append(this.connectionId);
        buf.append(" statement: ");
        buf.append(this.statementId);
        buf.append(" resultset: ");
        buf.append(this.resultSetId);

        if (this.message != null) {
            buf.append(" message: ");
            buf.append(this.message);
        }

        if (this.eventCreationPointDesc != null) {
            buf.append("\n\nEvent Created at:\n");
            buf.append(this.eventCreationPointDesc);
        }

        return buf.toString();
    }

    public byte[] pack() throws Exception {
        int len = 29;

        byte[] eventCreationAsBytes = null;

        getEventCreationPointAsString();

        if (this.eventCreationPointDesc != null) {
            eventCreationAsBytes = StringUtils.getBytes(this.eventCreationPointDesc, "ISO8859_1");

            len += 4 + eventCreationAsBytes.length;
        } else {
            len += 4;
        }

        byte[] messageAsBytes = null;

        if (this.message != null) {
            messageAsBytes = StringUtils.getBytes(this.message, "ISO8859_1");
            len += 4 + messageAsBytes.length;
        } else {
            len += 4;
        }

        byte[] durationUnitsAsBytes = null;

        if (this.durationUnits != null) {
            durationUnitsAsBytes = StringUtils.getBytes(this.durationUnits, "ISO8859_1");
            len += 4 + durationUnitsAsBytes.length;
        } else {
            len += 4;
            durationUnitsAsBytes = StringUtils.getBytes("", "ISO8859_1");
        }

        byte[] buf = new byte[len];

        int pos = 0;

        buf[pos++] = this.eventType;
        pos = writeLong(this.connectionId, buf, pos);
        pos = writeInt(this.statementId, buf, pos);
        pos = writeInt(this.resultSetId, buf, pos);
        pos = writeLong(this.eventCreationTime, buf, pos);
        pos = writeLong(this.eventDuration, buf, pos);
        pos = writeBytes(durationUnitsAsBytes, buf, pos);
        pos = writeInt(this.eventCreationPointIndex, buf, pos);

        if (eventCreationAsBytes != null) {
            pos = writeBytes(eventCreationAsBytes, buf, pos);
        } else {
            pos = writeInt(0, buf, pos);
        }

        if (messageAsBytes != null) {
            pos = writeBytes(messageAsBytes, buf, pos);
        } else {
            pos = writeInt(0, buf, pos);
        }

        return buf;
    }

    public String getCatalog() {
        return this.catalog;
    }

    public long getConnectionId() {
        return this.connectionId;
    }

    public long getEventCreationTime() {
        return this.eventCreationTime;
    }

    public long getEventDuration() {
        return this.eventDuration;
    }

    public String getDurationUnits() {
        return this.durationUnits;
    }

    public byte getEventType() {
        return this.eventType;
    }

    public int getResultSetId() {
        return this.resultSetId;
    }

    public int getStatementId() {
        return this.statementId;
    }

    public String getMessage() {
        return this.message;
    }
}

