package bsh;

import java.io.IOException;
import java.io.PrintStream;

public class ParserTokenManager
        implements ParserConstants {
    public static final String[] jjstrLiteralImages = new String[]{"", null, null, null, null, null, null, null, null, null, "abstract", "boolean", "break", "class", "byte", "case", "catch", "char", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "switch", "synchronized", "transient", "throw", "throws", "true", "try", "void", "volatile", "while", null, null, null, null, null, null, null, null, null, null, null, null, "(", ")", "{", "}", "[", "]", ";", ",", ".", "=", ">", "@gt", "<", "@lt", "!", "~", "?", ":", "==", "<=", "@lteq", ">=", "@gteq", "!=", "||", "@or", "&&", "@and", "++", "--", "+", "-", "*", "/", "&", "@bitwise_and", "|", "@bitwise_or", "^", "%", "<<", "@left_shift", ">>", "@right_shift", ">>>", "@right_unsigned_shift", "+=", "-=", "*=", "/=", "&=", "@and_assign", "|=", "@or_assign", "^=", "%=", "<<=", "@left_shift_assign", ">>=", "@right_shift_assign", ">>>=", "@right_unsigned_shift_assign"};
    public static final String[] lexStateNames = new String[]{"DEFAULT"};
    static final long[] jjbitVec0 = new long[]{0L, 0L, -1L, -1L};
    static final long[] jjbitVec1 = new long[]{-2L, -1L, -1L, -1L};
    static final long[] jjbitVec3 = new long[]{2301339413881290750L, -16384L, 4294967295L, 432345564227567616L};
    static final long[] jjbitVec4 = new long[]{0L, 0L, 0L, -36028797027352577L};
    static final long[] jjbitVec5 = new long[]{0L, -1L, -1L, -1L};
    static final long[] jjbitVec6 = new long[]{-1L, -1L, 65535L, 0L};
    static final long[] jjbitVec7 = new long[]{-1L, -1L, 0L, 0L};
    static final long[] jjbitVec8 = new long[]{70368744177663L, 0L, 0L, 0L};
    static final int[] jjnextStates = new int[]{37, 38, 43, 44, 47, 48, 15, 56, 61, 73, 26, 27, 29, 17, 19, 52, 54, 9, 57, 58, 60, 2, 3, 5, 11, 12, 15, 26, 27, 31, 29, 39, 40, 15, 47, 48, 15, 63, 64, 66, 69, 70, 72, 13, 14, 20, 21, 23, 28, 30, 32, 41, 42, 45, 46, 49, 50};
    static final long[] jjtoToken = new long[]{2305843009213692929L, -195L, 63L};
    static final long[] jjtoSkip = new long[]{1022L, 0L, 0L};
    static final long[] jjtoSpecial = new long[]{896L, 0L, 0L};
    private final int[] jjrounds = new int[74];
    private final int[] jjstateSet = new int[148];
    public PrintStream debugStream = System.out;
    protected JavaCharStream input_stream;
    protected char curChar;
    int curLexState;
    int defaultLexState;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;

    public ParserTokenManager(JavaCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public ParserTokenManager(JavaCharStream stream) {
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.input_stream = stream;
    }

    private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
        switch (hiByte) {

            case 0:
                return ((jjbitVec0[i2] & l2) != 0L);
        }
        return false;
    }

    private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
        switch (hiByte) {

            case 0:
                return ((jjbitVec0[i2] & l2) != 0L);
        }
        if ((jjbitVec1[i1] & l1) != 0L)
            return true;
        return false;
    }

    private static final boolean jjCanMove_2(int hiByte, int i1, int i2, long l1, long l2) {
        switch (hiByte) {

            case 0:
                return ((jjbitVec4[i2] & l2) != 0L);
            case 48:
                return ((jjbitVec5[i2] & l2) != 0L);
            case 49:
                return ((jjbitVec6[i2] & l2) != 0L);
            case 51:
                return ((jjbitVec7[i2] & l2) != 0L);
            case 61:
                return ((jjbitVec8[i2] & l2) != 0L);
        }
        if ((jjbitVec3[i1] & l1) != 0L)
            return true;
        return false;
    }

    public void setDebugStream(PrintStream ds) {
        this.debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1, long active2) {
        switch (pos) {

            case 0:
                if ((active1 & 0x200020000000000L) != 0L)
                    return 56;
                if ((active0 & 0x3EL) != 0L)
                    return 0;
                if ((active1 & 0x10000L) != 0L)
                    return 11;
                if ((active0 & 0xFFFFFFFFFFFFC00L) != 0L) {

                    this.jjmatchedKind = 69;
                    return 35;
                }
                return -1;
            case 1:
                if ((active0 & 0x100600000L) != 0L)
                    return 35;
                if ((active0 & 0xFFFFFFEFF9FFC00L) != 0L) {

                    if (this.jjmatchedPos != 1) {

                        this.jjmatchedKind = 69;
                        this.jjmatchedPos = 1;
                    }
                    return 35;
                }
                return -1;
            case 2:
                if ((active0 & 0xEFFFECEBFDFFC00L) != 0L) {

                    if (this.jjmatchedPos != 2) {

                        this.jjmatchedKind = 69;
                        this.jjmatchedPos = 2;
                    }
                    return 35;
                }
                if ((active0 & 0x100013040000000L) != 0L)
                    return 35;
                return -1;
            case 3:
                if ((active0 & 0xC7FFCAE3E5D3C00L) != 0L) {

                    if (this.jjmatchedPos != 3) {

                        this.jjmatchedKind = 69;
                        this.jjmatchedPos = 3;
                    }
                    return 35;
                }
                if ((active0 & 0x28002408182C000L) != 0L)
                    return 35;
                return -1;
            case 4:
                if ((active0 & 0x86080003C053000L) != 0L)
                    return 35;
                if ((active0 & 0x41F7CAE02580C00L) != 0L) {

                    if (this.jjmatchedPos != 4) {

                        this.jjmatchedKind = 69;
                        this.jjmatchedPos = 4;
                    }
                    return 35;
                }
                return -1;
            case 5:
                if ((active0 & 0x41A1C2A12180C00L) != 0L) {

                    this.jjmatchedKind = 69;
                    this.jjmatchedPos = 5;
                    return 35;
                }
                if ((active0 & 0x45608400400000L) != 0L)
                    return 35;
                return -1;
            case 6:
                if ((active0 & 0x41A102A00080400L) != 0L) {

                    this.jjmatchedKind = 69;
                    this.jjmatchedPos = 6;
                    return 35;
                }
                if ((active0 & 0xC0012100800L) != 0L)
                    return 35;
                return -1;
            case 7:
                if ((active0 & 0x402000000080400L) != 0L)
                    return 35;
                if ((active0 & 0x18102A00000000L) != 0L) {

                    this.jjmatchedKind = 69;
                    this.jjmatchedPos = 7;
                    return 35;
                }
                return -1;
            case 8:
                if ((active0 & 0x8000A00000000L) != 0L) {

                    this.jjmatchedKind = 69;
                    this.jjmatchedPos = 8;
                    return 35;
                }
                if ((active0 & 0x10102000000000L) != 0L)
                    return 35;
                return -1;
            case 9:
                if ((active0 & 0x8000000000000L) != 0L) {

                    this.jjmatchedKind = 69;
                    this.jjmatchedPos = 9;
                    return 35;
                }
                if ((active0 & 0xA00000000L) != 0L)
                    return 35;
                return -1;
            case 10:
                if ((active0 & 0x8000000000000L) != 0L) {

                    if (this.jjmatchedPos != 10) {

                        this.jjmatchedKind = 69;
                        this.jjmatchedPos = 10;
                    }
                    return 35;
                }
                return -1;
            case 11:
                if ((active0 & 0x8000000000000L) != 0L)
                    return 35;
                return -1;
        }
        return -1;
    }

    private final int jjStartNfa_0(int pos, long active0, long active1, long active2) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1, active2), pos + 1);
    }

    private final int jjStopAtPos(int pos, int kind) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_0() {
        switch (this.curChar) {

            case '\t':
                return jjStartNfaWithStates_0(0, 2, 0);
            case '\n':
                return jjStartNfaWithStates_0(0, 5, 0);
            case '\f':
                return jjStartNfaWithStates_0(0, 4, 0);
            case '\r':
                return jjStartNfaWithStates_0(0, 3, 0);
            case ' ':
                return jjStartNfaWithStates_0(0, 1, 0);
            case '!':
                this.jjmatchedKind = 86;
                return jjMoveStringLiteralDfa1_0(0L, 2147483648L, 0L);
            case '%':
                this.jjmatchedKind = 111;
                return jjMoveStringLiteralDfa1_0(0L, Long.MIN_VALUE, 0L);
            case '&':
                this.jjmatchedKind = 106;
                return jjMoveStringLiteralDfa1_0(0L, 288230393331580928L, 0L);
            case '(':
                return jjStopAtPos(0, 72);
            case ')':
                return jjStopAtPos(0, 73);
            case '*':
                this.jjmatchedKind = 104;
                return jjMoveStringLiteralDfa1_0(0L, 72057594037927936L, 0L);
            case '+':
                this.jjmatchedKind = 102;
                return jjMoveStringLiteralDfa1_0(0L, 18014467228958720L, 0L);
            case ',':
                return jjStopAtPos(0, 79);
            case '-':
                this.jjmatchedKind = 103;
                return jjMoveStringLiteralDfa1_0(0L, 36028934457917440L, 0L);
            case '.':
                return jjStartNfaWithStates_0(0, 80, 11);
            case '/':
                this.jjmatchedKind = 105;
                return jjMoveStringLiteralDfa1_0(0L, 144115188075855872L, 0L);
            case ':':
                return jjStopAtPos(0, 89);
            case ';':
                return jjStopAtPos(0, 78);
            case '<':
                this.jjmatchedKind = 84;
                return jjMoveStringLiteralDfa1_0(0L, 281475110928384L, 1L);
            case '=':
                this.jjmatchedKind = 81;
                return jjMoveStringLiteralDfa1_0(0L, 67108864L, 0L);
            case '>':
                this.jjmatchedKind = 82;
                return jjMoveStringLiteralDfa1_0(0L, 5629500071084032L, 20L);
            case '?':
                return jjStopAtPos(0, 88);
            case '@':
                return jjMoveStringLiteralDfa1_0(0L, 2894169735298547712L, 42L);
            case '[':
                return jjStopAtPos(0, 76);
            case ']':
                return jjStopAtPos(0, 77);
            case '^':
                this.jjmatchedKind = 110;
                return jjMoveStringLiteralDfa1_0(0L, 4611686018427387904L, 0L);
            case 'a':
                return jjMoveStringLiteralDfa1_0(1024L, 0L, 0L);
            case 'b':
                return jjMoveStringLiteralDfa1_0(22528L, 0L, 0L);
            case 'c':
                return jjMoveStringLiteralDfa1_0(1024000L, 0L, 0L);
            case 'd':
                return jjMoveStringLiteralDfa1_0(7340032L, 0L, 0L);
            case 'e':
                return jjMoveStringLiteralDfa1_0(58720256L, 0L, 0L);
            case 'f':
                return jjMoveStringLiteralDfa1_0(2080374784L, 0L, 0L);
            case 'g':
                return jjMoveStringLiteralDfa1_0(2147483648L, 0L, 0L);
            case 'i':
                return jjMoveStringLiteralDfa1_0(270582939648L, 0L, 0L);
            case 'l':
                return jjMoveStringLiteralDfa1_0(274877906944L, 0L, 0L);
            case 'n':
                return jjMoveStringLiteralDfa1_0(3848290697216L, 0L, 0L);
            case 'p':
                return jjMoveStringLiteralDfa1_0(65970697666560L, 0L, 0L);
            case 'r':
                return jjMoveStringLiteralDfa1_0(70368744177664L, 0L, 0L);
            case 's':
                return jjMoveStringLiteralDfa1_0(4362862139015168L, 0L, 0L);
            case 't':
                return jjMoveStringLiteralDfa1_0(139611588448485376L, 0L, 0L);
            case 'v':
                return jjMoveStringLiteralDfa1_0(432345564227567616L, 0L, 0L);
            case 'w':
                return jjMoveStringLiteralDfa1_0(576460752303423488L, 0L, 0L);
            case '{':
                return jjStopAtPos(0, 74);
            case '|':
                this.jjmatchedKind = 108;
                return jjMoveStringLiteralDfa1_0(0L, 1152921508901814272L, 0L);
            case '}':
                return jjStopAtPos(0, 75);
            case '~':
                return jjStopAtPos(0, 87);
        }
        return jjMoveNfa_0(6, 0);
    }

    private final int jjMoveStringLiteralDfa1_0(long active0, long active1, long active2) {
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(0, active0, active1, active2);
            return 1;
        }
        switch (this.curChar) {

            case '&':
                if ((active1 & 0x400000000L) != 0L)
                    return jjStopAtPos(1, 98);
                break;
            case '+':
                if ((active1 & 0x1000000000L) != 0L)
                    return jjStopAtPos(1, 100);
                break;
            case '-':
                if ((active1 & 0x2000000000L) != 0L)
                    return jjStopAtPos(1, 101);
                break;
            case '<':
                if ((active1 & 0x1000000000000L) != 0L) {

                    this.jjmatchedKind = 112;
                    this.jjmatchedPos = 1;
                }
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 1L);
            case '=':
                if ((active1 & 0x4000000L) != 0L)
                    return jjStopAtPos(1, 90);
                if ((active1 & 0x8000000L) != 0L)
                    return jjStopAtPos(1, 91);
                if ((active1 & 0x20000000L) != 0L)
                    return jjStopAtPos(1, 93);
                if ((active1 & 0x80000000L) != 0L)
                    return jjStopAtPos(1, 95);
                if ((active1 & 0x40000000000000L) != 0L)
                    return jjStopAtPos(1, 118);
                if ((active1 & 0x80000000000000L) != 0L)
                    return jjStopAtPos(1, 119);
                if ((active1 & 0x100000000000000L) != 0L)
                    return jjStopAtPos(1, 120);
                if ((active1 & 0x200000000000000L) != 0L)
                    return jjStopAtPos(1, 121);
                if ((active1 & 0x400000000000000L) != 0L)
                    return jjStopAtPos(1, 122);
                if ((active1 & 0x1000000000000000L) != 0L)
                    return jjStopAtPos(1, 124);
                if ((active1 & 0x4000000000000000L) != 0L)
                    return jjStopAtPos(1, 126);
                if ((active1 & Long.MIN_VALUE) != 0L)
                    return jjStopAtPos(1, 127);
                break;
            case '>':
                if ((active1 & 0x4000000000000L) != 0L) {

                    this.jjmatchedKind = 114;
                    this.jjmatchedPos = 1;
                }
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 4503599627370496L, active2, 20L);
            case 'a':
                return jjMoveStringLiteralDfa2_0(active0, 4947869532160L, active1, 576460786663161856L, active2, 0L);
            case 'b':
                return jjMoveStringLiteralDfa2_0(active0, 1024L, active1, 43980465111040L, active2, 0L);
            case 'e':
                return jjMoveStringLiteralDfa2_0(active0, 71468256854016L, active1, 0L, active2, 0L);
            case 'f':
                if ((active0 & 0x100000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 32, 35);
                break;
            case 'g':
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 1074266112L, active2, 0L);
            case 'h':
                return jjMoveStringLiteralDfa2_0(active0, 603623087556132864L, active1, 0L, active2, 0L);
            case 'i':
                return jjMoveStringLiteralDfa2_0(active0, 402653184L, active1, 0L, active2, 0L);
            case 'l':
                return jjMoveStringLiteralDfa2_0(active0, 545267712L, active1, 562950223953920L, active2, 2L);
            case 'm':
                return jjMoveStringLiteralDfa2_0(active0, 25769803776L, active1, 0L, active2, 0L);
            case 'n':
                return jjMoveStringLiteralDfa2_0(active0, 240534945792L, active1, 0L, active2, 0L);
            case 'o':
                if ((active0 & 0x200000L) != 0L) {

                    this.jjmatchedKind = 21;
                    this.jjmatchedPos = 1;
                }
                return jjMoveStringLiteralDfa2_0(active0, 432345842331682816L, active1, 2305843017803628544L, active2, 0L);
            case 'r':
                return jjMoveStringLiteralDfa2_0(active0, 112616378963333120L, active1, 11258999068426240L, active2, 40L);
            case 't':
                return jjMoveStringLiteralDfa2_0(active0, 844424930131968L, active1, 0L, active2, 0L);
            case 'u':
                return jjMoveStringLiteralDfa2_0(active0, 37383395344384L, active1, 0L, active2, 0L);
            case 'w':
                return jjMoveStringLiteralDfa2_0(active0, 1125899906842624L, active1, 0L, active2, 0L);
            case 'x':
                return jjMoveStringLiteralDfa2_0(active0, 33554432L, active1, 0L, active2, 0L);
            case 'y':
                return jjMoveStringLiteralDfa2_0(active0, 2251799813701632L, active1, 0L, active2, 0L);
            case '|':
                if ((active1 & 0x100000000L) != 0L) {
                    return jjStopAtPos(1, 96);
                }
                break;
        }

        return jjStartNfa_0(0, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(0, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(1, active0, active1, active2);
            return 2;
        }
        switch (this.curChar) {

            case '=':
                if ((active2 & 0x1L) != 0L)
                    return jjStopAtPos(2, 128);
                if ((active2 & 0x4L) != 0L)
                    return jjStopAtPos(2, 130);
                break;
            case '>':
                if ((active1 & 0x10000000000000L) != 0L) {

                    this.jjmatchedKind = 116;
                    this.jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0L, active2, 16L);
            case 'a':
                return jjMoveStringLiteralDfa3_0(active0, 4785074604220416L, active1, 0L, active2, 0L);
            case 'b':
                return jjMoveStringLiteralDfa3_0(active0, 35184372088832L, active1, 0L, active2, 0L);
            case 'c':
                return jjMoveStringLiteralDfa3_0(active0, 4398046511104L, active1, 0L, active2, 0L);
            case 'e':
                return jjMoveStringLiteralDfa3_0(active0, 4096L, active1, 562949953421312L, active2, 2L);
            case 'f':
                return jjMoveStringLiteralDfa3_0(active0, 1048576L, active1, 0L, active2, 0L);
            case 'i':
                return jjMoveStringLiteralDfa3_0(active0, 721710636379144192L, active1, 11302979533537280L, active2, 40L);
            case 'l':
                return jjMoveStringLiteralDfa3_0(active0, 288232575242076160L, active1, 0L, active2, 0L);
            case 'n':
                return jjMoveStringLiteralDfa3_0(active0, 2252075095031808L, active1, 576460786663161856L, active2, 0L);
            case 'o':
                return jjMoveStringLiteralDfa3_0(active0, 158330211272704L, active1, 0L, active2, 0L);
            case 'p':
                return jjMoveStringLiteralDfa3_0(active0, 25769803776L, active1, 0L, active2, 0L);
            case 'r':
                if ((active0 & 0x40000000L) != 0L)
                    return jjStartNfaWithStates_0(2, 30, 35);
                if ((active1 & 0x200000000L) != 0L) {

                    this.jjmatchedKind = 97;
                    this.jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 27584547717644288L, active1, 2305843009213693952L, active2, 0L);
            case 's':
                return jjMoveStringLiteralDfa3_0(active0, 34368160768L, active1, 0L, active2, 0L);
            case 't':
                if ((active0 & 0x1000000000L) != 0L) {

                    this.jjmatchedKind = 36;
                    this.jjmatchedPos = 2;
                } else if ((active1 & 0x80000L) != 0L) {

                    this.jjmatchedKind = 83;
                    this.jjmatchedPos = 2;
                } else if ((active1 & 0x200000L) != 0L) {

                    this.jjmatchedKind = 85;
                    this.jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 71058120065024L, active1, 1342177280L, active2, 0L);
            case 'u':
                return jjMoveStringLiteralDfa3_0(active0, 36028797039935488L, active1, 0L, active2, 0L);
            case 'w':
                if ((active0 & 0x10000000000L) != 0L)
                    return jjStartNfaWithStates_0(2, 40, 35);
                break;
            case 'y':
                if ((active0 & 0x100000000000000L) != 0L) {
                    return jjStartNfaWithStates_0(2, 56, 35);
                }
                break;
        }

        return jjStartNfa_0(1, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(1, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(2, active0, active1, active2);
            return 3;
        }
        switch (this.curChar) {

            case '=':
                if ((active2 & 0x10L) != 0L)
                    return jjStopAtPos(3, 132);
                break;
            case '_':
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 2305843009213693952L, active2, 0L);
            case 'a':
                return jjMoveStringLiteralDfa4_0(active0, 288230377092288512L, active1, 0L, active2, 0L);
            case 'b':
                return jjMoveStringLiteralDfa4_0(active0, 4194304L, active1, 0L, active2, 0L);
            case 'c':
                return jjMoveStringLiteralDfa4_0(active0, 2251799813750784L, active1, 0L, active2, 0L);
            case 'd':
                if ((active0 & 0x200000000000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 57, 35);
                if ((active1 & 0x800000000L) != 0L) {

                    this.jjmatchedKind = 99;
                    this.jjmatchedPos = 3;
                }
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 576460752303423488L, active2, 0L);
            case 'e':
                if ((active0 & 0x4000L) != 0L)
                    return jjStartNfaWithStates_0(3, 14, 35);
                if ((active0 & 0x8000L) != 0L)
                    return jjStartNfaWithStates_0(3, 15, 35);
                if ((active0 & 0x800000L) != 0L)
                    return jjStartNfaWithStates_0(3, 23, 35);
                if ((active0 & 0x80000000000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 55, 35);
                return jjMoveStringLiteralDfa4_0(active0, 137472507904L, active1, 1342177280L, active2, 0L);
            case 'f':
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 562949953421312L, active2, 2L);
            case 'g':
                if ((active0 & 0x4000000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 38, 35);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 11258999068426240L, active2, 40L);
            case 'i':
                return jjMoveStringLiteralDfa4_0(active0, 563499709235200L, active1, 0L, active2, 0L);
            case 'k':
                return jjMoveStringLiteralDfa4_0(active0, 4398046511104L, active1, 0L, active2, 0L);
            case 'l':
                if ((active0 & 0x20000000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 41, 35);
                return jjMoveStringLiteralDfa4_0(active0, 576495945265448960L, active1, 0L, active2, 0L);
            case 'm':
                if ((active0 & 0x1000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 24, 35);
                break;
            case 'n':
                return jjMoveStringLiteralDfa4_0(active0, 4503599627370496L, active1, 0L, active2, 0L);
            case 'o':
                if ((active0 & 0x80000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 31, 35);
                return jjMoveStringLiteralDfa4_0(active0, 27021614944092160L, active1, 0L, active2, 0L);
            case 'r':
                if ((active0 & 0x20000L) != 0L)
                    return jjStartNfaWithStates_0(3, 17, 35);
                return jjMoveStringLiteralDfa4_0(active0, 140737488355328L, active1, 0L, active2, 0L);
            case 's':
                return jjMoveStringLiteralDfa4_0(active0, 67379200L, active1, 0L, active2, 0L);
            case 't':
                return jjMoveStringLiteralDfa4_0(active0, 1425001429861376L, active1, 43980465111040L, active2, 0L);
            case 'u':
                return jjMoveStringLiteralDfa4_0(active0, 70368744177664L, active1, 0L, active2, 0L);
            case 'v':
                return jjMoveStringLiteralDfa4_0(active0, 8796093022208L, active1, 0L, active2, 0L);
        }

        return jjStartNfa_0(2, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(2, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(3, active0, active1, active2);
            return 4;
        }
        switch (this.curChar) {

            case '_':
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 576460752303423488L, active2, 0L);
            case 'a':
                return jjMoveStringLiteralDfa5_0(active0, 13228499271680L, active1, 2305843009213693952L, active2, 0L);
            case 'c':
                return jjMoveStringLiteralDfa5_0(active0, 1688849860263936L, active1, 0L, active2, 0L);
            case 'e':
                if ((active0 & 0x4000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 26, 35);
                if ((active0 & 0x800000000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 59, 35);
                return jjMoveStringLiteralDfa5_0(active0, 17600775981056L, active1, 0L, active2, 0L);
            case 'h':
                if ((active0 & 0x10000L) != 0L)
                    return jjStartNfaWithStates_0(4, 16, 35);
                return jjMoveStringLiteralDfa5_0(active0, 2251799813685248L, active1, 11258999068426240L, active2, 40L);
            case 'i':
                return jjMoveStringLiteralDfa5_0(active0, 316659349323776L, active1, 0L, active2, 0L);
            case 'k':
                if ((active0 & 0x1000L) != 0L)
                    return jjStartNfaWithStates_0(4, 12, 35);
                break;
            case 'l':
                if ((active0 & 0x8000000L) != 0L) {

                    this.jjmatchedKind = 27;
                    this.jjmatchedPos = 4;
                }
                return jjMoveStringLiteralDfa5_0(active0, 272629760L, active1, 0L, active2, 0L);
            case 'n':
                return jjMoveStringLiteralDfa5_0(active0, 33554432L, active1, 0L, active2, 0L);
            case 'q':
                if ((active1 & 0x10000000L) != 0L)
                    return jjStopAtPos(4, 92);
                if ((active1 & 0x40000000L) != 0L)
                    return jjStopAtPos(4, 94);
                break;
            case 'r':
                return jjMoveStringLiteralDfa5_0(active0, 70523363001344L, active1, 0L, active2, 0L);
            case 's':
                if ((active0 & 0x2000L) != 0L)
                    return jjStartNfaWithStates_0(4, 13, 35);
                return jjMoveStringLiteralDfa5_0(active0, 4503599627370496L, active1, 0L, active2, 0L);
            case 't':
                if ((active0 & 0x40000L) != 0L)
                    return jjStartNfaWithStates_0(4, 18, 35);
                if ((active0 & 0x20000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 29, 35);
                if ((active0 & 0x800000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 47, 35);
                return jjMoveStringLiteralDfa5_0(active0, 288230376151711744L, active1, 562949953421312L, active2, 2L);
            case 'u':
                return jjMoveStringLiteralDfa5_0(active0, 1048576L, active1, 0L, active2, 0L);
            case 'v':
                return jjMoveStringLiteralDfa5_0(active0, 549755813888L, active1, 0L, active2, 0L);
            case 'w':
                if ((active0 & 0x20000000000000L) != 0L) {

                    this.jjmatchedKind = 53;
                    this.jjmatchedPos = 4;
                }
                return jjMoveStringLiteralDfa5_0(active0, 18014398509481984L, active1, 43980465111040L, active2, 0L);
        }

        return jjStartNfa_0(3, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(3, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(4, active0, active1, active2);
            return 5;
        }
        switch (this.curChar) {

            case '_':
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 562949953421312L, active2, 2L);
            case 'a':
                return jjMoveStringLiteralDfa6_0(active0, 3072L, active1, 576460752303423488L, active2, 0L);
            case 'c':
                if ((active0 & 0x200000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 45, 35);
                if ((active0 & 0x1000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 48, 35);
                return jjMoveStringLiteralDfa6_0(active0, 17592186044416L, active1, 0L, active2, 0L);
            case 'd':
                return jjMoveStringLiteralDfa6_0(active0, 33554432L, active1, 0L, active2, 0L);
            case 'e':
                if ((active0 & 0x400000L) != 0L)
                    return jjStartNfaWithStates_0(5, 22, 35);
                if ((active0 & 0x8000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 39, 35);
                break;
            case 'f':
                return jjMoveStringLiteralDfa6_0(active0, 137438953472L, active1, 0L, active2, 0L);
            case 'g':
                return jjMoveStringLiteralDfa6_0(active0, 4398046511104L, active1, 0L, active2, 0L);
            case 'h':
                if ((active0 & 0x4000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 50, 35);
                break;
            case 'i':
                return jjMoveStringLiteralDfa6_0(active0, 292733975779082240L, active1, 43980465111040L, active2, 0L);
            case 'l':
                return jjMoveStringLiteralDfa6_0(active0, 269484032L, active1, 0L, active2, 0L);
            case 'm':
                return jjMoveStringLiteralDfa6_0(active0, 8589934592L, active1, 0L, active2, 0L);
            case 'n':
                if ((active0 & 0x400000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 46, 35);
                return jjMoveStringLiteralDfa6_0(active0, 34360262656L, active1, 0L, active2, 0L);
            case 'r':
                return jjMoveStringLiteralDfa6_0(active0, 2251799813685248L, active1, 0L, active2, 0L);
            case 's':
                if ((active0 & 0x40000000000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 54, 35);
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 2305843009213693952L, active2, 0L);
            case 't':
                if ((active0 & 0x400000000L) != 0L)
                    return jjStartNfaWithStates_0(5, 34, 35);
                return jjMoveStringLiteralDfa6_0(active0, 571746046443520L, active1, 11258999068426240L, active2, 40L);
        }

        return jjStartNfa_0(4, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(4, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(5, active0, active1, active2);
            return 6;
        }
        switch (this.curChar) {

            case '_':
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 11258999068426240L, active2, 40L);
            case 'a':
                return jjMoveStringLiteralDfa7_0(active0, 137438953472L, active1, 0L, active2, 0L);
            case 'c':
                return jjMoveStringLiteralDfa7_0(active0, 34359739392L, active1, 0L, active2, 0L);
            case 'e':
                if ((active0 & 0x40000000000L) != 0L)
                    return jjStartNfaWithStates_0(6, 42, 35);
                if ((active0 & 0x80000000000L) != 0L)
                    return jjStartNfaWithStates_0(6, 43, 35);
                return jjMoveStringLiteralDfa7_0(active0, 4503608217305088L, active1, 0L, active2, 0L);
            case 'f':
                return jjMoveStringLiteralDfa7_0(active0, 562949953421312L, active1, 0L, active2, 0L);
            case 'l':
                return jjMoveStringLiteralDfa7_0(active0, 288230376151711744L, active1, 0L, active2, 0L);
            case 'n':
                if ((active0 & 0x800L) != 0L)
                    return jjStartNfaWithStates_0(6, 11, 35);
                break;
            case 'o':
                return jjMoveStringLiteralDfa7_0(active0, 2251799813685248L, active1, 0L, active2, 0L);
            case 's':
                if ((active0 & 0x2000000L) != 0L)
                    return jjStartNfaWithStates_0(6, 25, 35);
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 2882910691935649792L, active2, 2L);
            case 't':
                if ((active0 & 0x100000L) != 0L)
                    return jjStartNfaWithStates_0(6, 20, 35);
                return jjMoveStringLiteralDfa7_0(active0, 17592186044416L, active1, 0L, active2, 0L);
            case 'u':
                return jjMoveStringLiteralDfa7_0(active0, 524288L, active1, 0L, active2, 0L);
            case 'y':
                if ((active0 & 0x10000000L) != 0L) {
                    return jjStartNfaWithStates_0(6, 28, 35);
                }
                break;
        }

        return jjStartNfa_0(5, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(5, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(6, active0, active1, active2);
            return 7;
        }
        switch (this.curChar) {

            case 'c':
                return jjMoveStringLiteralDfa8_0(active0, 137438953472L, active1, 0L, active2, 0L);
            case 'e':
                if ((active0 & 0x80000L) != 0L)
                    return jjStartNfaWithStates_0(7, 19, 35);
                if ((active0 & 0x400000000000000L) != 0L)
                    return jjStartNfaWithStates_0(7, 58, 35);
                return jjMoveStringLiteralDfa8_0(active0, 17626545782784L, active1, 43980465111040L, active2, 0L);
            case 'h':
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 562949953421312L, active2, 2L);
            case 'i':
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 2305843009213693952L, active2, 0L);
            case 'n':
                return jjMoveStringLiteralDfa8_0(active0, 6755408030990336L, active1, 0L, active2, 0L);
            case 'p':
                if ((active0 & 0x2000000000000L) != 0L)
                    return jjStartNfaWithStates_0(7, 49, 35);
                break;
            case 's':
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 578712552117108736L, active2, 8L);
            case 't':
                if ((active0 & 0x400L) != 0L)
                    return jjStartNfaWithStates_0(7, 10, 35);
                break;
            case 'u':
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
        }

        return jjStartNfa_0(6, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa8_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(6, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(7, active0, active1, active2);
            return 8;
        }
        switch (this.curChar) {

            case '_':
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 43980465111040L, active2, 0L);
            case 'd':
                if ((active0 & 0x100000000000L) != 0L)
                    return jjStartNfaWithStates_0(8, 44, 35);
                break;
            case 'e':
                if ((active0 & 0x2000000000L) != 0L)
                    return jjStartNfaWithStates_0(8, 37, 35);
                break;
            case 'g':
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 2305843009213693952L, active2, 0L);
            case 'h':
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 2251799813685248L, active2, 8L);
            case 'i':
                return jjMoveStringLiteralDfa9_0(active0, 2251799813685248L, active1, 577023702256844800L, active2, 2L);
            case 'n':
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
            case 'o':
                return jjMoveStringLiteralDfa9_0(active0, 34359738368L, active1, 0L, active2, 0L);
            case 't':
                if ((active0 & 0x10000000000000L) != 0L)
                    return jjStartNfaWithStates_0(8, 52, 35);
                return jjMoveStringLiteralDfa9_0(active0, 8589934592L, active1, 0L, active2, 0L);
        }

        return jjStartNfa_0(7, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa9_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(7, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(8, active0, active1, active2);
            return 9;
        }
        switch (this.curChar) {

            case 'a':
                return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 8796093022208L, active2, 0L);
            case 'f':
                if ((active0 & 0x800000000L) != 0L)
                    return jjStartNfaWithStates_0(9, 35, 35);
                return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 562949953421312L, active2, 2L);
            case 'g':
                return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 576460752303423488L, active2, 0L);
            case 'i':
                return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 2251799813685248L, active2, 8L);
            case 'n':
                if ((active1 & 0x2000000000000000L) != 0L)
                    return jjStopAtPos(9, 125);
                break;
            case 'o':
                return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 35184372088832L, active2, 0L);
            case 's':
                if ((active0 & 0x200000000L) != 0L)
                    return jjStartNfaWithStates_0(9, 33, 35);
                return jjMoveStringLiteralDfa10_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
            case 'z':
                return jjMoveStringLiteralDfa10_0(active0, 2251799813685248L, active1, 0L, active2, 0L);
        }

        return jjStartNfa_0(8, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa10_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(8, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(9, active0, active1, active2);
            return 10;
        }
        switch (this.curChar) {

            case 'e':
                return jjMoveStringLiteralDfa11_0(active0, 2251799813685248L, active1, 0L, active2, 0L);
            case 'f':
                return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 2251799813685248L, active2, 8L);
            case 'i':
                return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
            case 'n':
                if ((active1 & 0x800000000000000L) != 0L)
                    return jjStopAtPos(10, 123);
                return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 8796093022208L, active2, 0L);
            case 'r':
                if ((active1 & 0x200000000000L) != 0L)
                    return jjStopAtPos(10, 109);
                break;
            case 't':
                if ((active1 & 0x2000000000000L) != 0L) {

                    this.jjmatchedKind = 113;
                    this.jjmatchedPos = 10;
                }
                return jjMoveStringLiteralDfa11_0(active0, 0L, active1, 0L, active2, 2L);
        }

        return jjStartNfa_0(9, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa11_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(9, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(10, active0, active1, active2);
            return 11;
        }
        switch (this.curChar) {

            case '_':
                return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 0L, active2, 2L);
            case 'd':
                if ((active0 & 0x8000000000000L) != 0L)
                    return jjStartNfaWithStates_0(11, 51, 35);
                if ((active1 & 0x80000000000L) != 0L)
                    return jjStopAtPos(11, 107);
                break;
            case 'g':
                return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 9007199254740992L, active2, 32L);
            case 't':
                if ((active1 & 0x8000000000000L) != 0L) {

                    this.jjmatchedKind = 115;
                    this.jjmatchedPos = 11;
                }
                return jjMoveStringLiteralDfa12_0(active0, 0L, active1, 0L, active2, 8L);
        }

        return jjStartNfa_0(10, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa12_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(10, old0, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(11, 0L, active1, active2);
            return 12;
        }
        switch (this.curChar) {

            case '_':
                return jjMoveStringLiteralDfa13_0(active1, 0L, active2, 8L);
            case 'a':
                return jjMoveStringLiteralDfa13_0(active1, 0L, active2, 2L);
            case 'n':
                return jjMoveStringLiteralDfa13_0(active1, 9007199254740992L, active2, 32L);
        }

        return jjStartNfa_0(11, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa13_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(11, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(12, 0L, active1, active2);
            return 13;
        }
        switch (this.curChar) {

            case 'a':
                return jjMoveStringLiteralDfa14_0(active1, 0L, active2, 8L);
            case 'e':
                return jjMoveStringLiteralDfa14_0(active1, 9007199254740992L, active2, 32L);
            case 's':
                return jjMoveStringLiteralDfa14_0(active1, 0L, active2, 2L);
        }

        return jjStartNfa_0(12, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa14_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(12, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(13, 0L, active1, active2);
            return 14;
        }
        switch (this.curChar) {

            case 'd':
                return jjMoveStringLiteralDfa15_0(active1, 9007199254740992L, active2, 32L);
            case 's':
                return jjMoveStringLiteralDfa15_0(active1, 0L, active2, 10L);
        }

        return jjStartNfa_0(13, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa15_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(13, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(14, 0L, active1, active2);
            return 15;
        }
        switch (this.curChar) {

            case '_':
                return jjMoveStringLiteralDfa16_0(active1, 9007199254740992L, active2, 32L);
            case 'i':
                return jjMoveStringLiteralDfa16_0(active1, 0L, active2, 2L);
            case 's':
                return jjMoveStringLiteralDfa16_0(active1, 0L, active2, 8L);
        }

        return jjStartNfa_0(14, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa16_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(14, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(15, 0L, active1, active2);
            return 16;
        }
        switch (this.curChar) {

            case 'g':
                return jjMoveStringLiteralDfa17_0(active1, 0L, active2, 2L);
            case 'i':
                return jjMoveStringLiteralDfa17_0(active1, 0L, active2, 8L);
            case 's':
                return jjMoveStringLiteralDfa17_0(active1, 9007199254740992L, active2, 32L);
        }

        return jjStartNfa_0(15, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa17_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(15, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(16, 0L, active1, active2);
            return 17;
        }
        switch (this.curChar) {

            case 'g':
                return jjMoveStringLiteralDfa18_0(active1, 0L, active2, 8L);
            case 'h':
                return jjMoveStringLiteralDfa18_0(active1, 9007199254740992L, active2, 32L);
            case 'n':
                if ((active2 & 0x2L) != 0L) {
                    return jjStopAtPos(17, 129);
                }
                break;
        }

        return jjStartNfa_0(16, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa18_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(16, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(17, 0L, active1, active2);
            return 18;
        }
        switch (this.curChar) {

            case 'i':
                return jjMoveStringLiteralDfa19_0(active1, 9007199254740992L, active2, 32L);
            case 'n':
                if ((active2 & 0x8L) != 0L) {
                    return jjStopAtPos(18, 131);
                }
                break;
        }

        return jjStartNfa_0(17, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa19_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(17, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(18, 0L, active1, active2);
            return 19;
        }
        switch (this.curChar) {

            case 'f':
                return jjMoveStringLiteralDfa20_0(active1, 9007199254740992L, active2, 32L);
        }

        return jjStartNfa_0(18, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa20_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(18, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(19, 0L, active1, active2);
            return 20;
        }
        switch (this.curChar) {

            case 't':
                if ((active1 & 0x20000000000000L) != 0L) {

                    this.jjmatchedKind = 117;
                    this.jjmatchedPos = 20;
                }
                return jjMoveStringLiteralDfa21_0(active1, 0L, active2, 32L);
        }

        return jjStartNfa_0(19, 0L, active1, active2);
    }

    private final int jjMoveStringLiteralDfa21_0(long old1, long active1, long old2, long active2) {
        if (((active1 &= old1) | (active2 &= old2)) == 0L)
            return jjStartNfa_0(19, 0L, old1, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(20, 0L, 0L, active2);
            return 21;
        }
        switch (this.curChar) {

            case '_':
                return jjMoveStringLiteralDfa22_0(active2, 32L);
        }

        return jjStartNfa_0(20, 0L, 0L, active2);
    }

    private final int jjMoveStringLiteralDfa22_0(long old2, long active2) {
        if ((active2 &= old2) == 0L)
            return jjStartNfa_0(20, 0L, 0L, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(21, 0L, 0L, active2);
            return 22;
        }
        switch (this.curChar) {

            case 'a':
                return jjMoveStringLiteralDfa23_0(active2, 32L);
        }

        return jjStartNfa_0(21, 0L, 0L, active2);
    }

    private final int jjMoveStringLiteralDfa23_0(long old2, long active2) {
        if ((active2 &= old2) == 0L)
            return jjStartNfa_0(21, 0L, 0L, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(22, 0L, 0L, active2);
            return 23;
        }
        switch (this.curChar) {

            case 's':
                return jjMoveStringLiteralDfa24_0(active2, 32L);
        }

        return jjStartNfa_0(22, 0L, 0L, active2);
    }

    private final int jjMoveStringLiteralDfa24_0(long old2, long active2) {
        if ((active2 &= old2) == 0L)
            return jjStartNfa_0(22, 0L, 0L, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(23, 0L, 0L, active2);
            return 24;
        }
        switch (this.curChar) {

            case 's':
                return jjMoveStringLiteralDfa25_0(active2, 32L);
        }

        return jjStartNfa_0(23, 0L, 0L, active2);
    }

    private final int jjMoveStringLiteralDfa25_0(long old2, long active2) {
        if ((active2 &= old2) == 0L)
            return jjStartNfa_0(23, 0L, 0L, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(24, 0L, 0L, active2);
            return 25;
        }
        switch (this.curChar) {

            case 'i':
                return jjMoveStringLiteralDfa26_0(active2, 32L);
        }

        return jjStartNfa_0(24, 0L, 0L, active2);
    }

    private final int jjMoveStringLiteralDfa26_0(long old2, long active2) {
        if ((active2 &= old2) == 0L)
            return jjStartNfa_0(24, 0L, 0L, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(25, 0L, 0L, active2);
            return 26;
        }
        switch (this.curChar) {

            case 'g':
                return jjMoveStringLiteralDfa27_0(active2, 32L);
        }

        return jjStartNfa_0(25, 0L, 0L, active2);
    }

    private final int jjMoveStringLiteralDfa27_0(long old2, long active2) {
        if ((active2 &= old2) == 0L)
            return jjStartNfa_0(25, 0L, 0L, old2);
        try {
            this.curChar = this.input_stream.readChar();
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(26, 0L, 0L, active2);
            return 27;
        }
        switch (this.curChar) {

            case 'n':
                if ((active2 & 0x20L) != 0L) {
                    return jjStopAtPos(27, 133);
                }
                break;
        }

        return jjStartNfa_0(26, 0L, 0L, active2);
    }

    private final void jjCheckNAdd(int state) {
        if (this.jjrounds[state] != this.jjround) {

            this.jjstateSet[this.jjnewStateCnt++] = state;
            this.jjrounds[state] = this.jjround;
        }
    }

    private final void jjAddStates(int start, int end) {
        do {
            this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private final void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private final void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }

    private final void jjCheckNAddStates(int start) {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    private final int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 74;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;

        while (true) {
            if (++this.jjround == Integer.MAX_VALUE)
                ReInitRounds();
            if (this.curChar < '@') {

                long l = 1L << this.curChar;

                do {
                    switch (this.jjstateSet[--i]) {

                        case 6:
                            if ((0x1FFFFFFFFL & l) != 0L) {

                                if (kind > 6)
                                    kind = 6;
                                jjCheckNAdd(0);
                            } else if ((0x3FF000000000000L & l) != 0L) {
                                jjCheckNAddStates(0, 6);
                            } else if (this.curChar == '/') {
                                jjAddStates(7, 9);
                            } else if (this.curChar == '$') {

                                if (kind > 69)
                                    kind = 69;
                                jjCheckNAdd(35);
                            } else if (this.curChar == '"') {
                                jjCheckNAddStates(10, 12);
                            } else if (this.curChar == '\'') {
                                jjAddStates(13, 14);
                            } else if (this.curChar == '.') {
                                jjCheckNAdd(11);
                            } else if (this.curChar == '#') {
                                this.jjstateSet[this.jjnewStateCnt++] = 1;
                            }
                            if ((0x3FE000000000000L & l) != 0L) {

                                if (kind > 60)
                                    kind = 60;
                                jjCheckNAddTwoStates(8, 9);
                                break;
                            }
                            if (this.curChar == '0') {

                                if (kind > 60)
                                    kind = 60;
                                jjCheckNAddStates(15, 17);
                            }
                            break;
                        case 56:
                            if (this.curChar == '*') {
                                this.jjstateSet[this.jjnewStateCnt++] = 67;
                            } else if (this.curChar == '/') {

                                if (kind > 7)
                                    kind = 7;
                                jjCheckNAddStates(18, 20);
                            }
                            if (this.curChar == '*')
                                jjCheckNAdd(62);
                            break;
                        case 0:
                            if ((0x1FFFFFFFFL & l) == 0L)
                                break;
                            if (kind > 6)
                                kind = 6;
                            jjCheckNAdd(0);
                            break;
                        case 1:
                            if (this.curChar == '!')
                                jjCheckNAddStates(21, 23);
                            break;
                        case 2:
                            if ((0xFFFFFFFFFFFFDBFFL & l) != 0L)
                                jjCheckNAddStates(21, 23);
                            break;
                        case 3:
                            if ((0x2400L & l) != 0L && kind > 8)
                                kind = 8;
                            break;
                        case 4:
                            if (this.curChar == '\n' && kind > 8)
                                kind = 8;
                            break;
                        case 5:
                            if (this.curChar == '\r')
                                this.jjstateSet[this.jjnewStateCnt++] = 4;
                            break;
                        case 7:
                            if ((0x3FE000000000000L & l) == 0L)
                                break;
                            if (kind > 60)
                                kind = 60;
                            jjCheckNAddTwoStates(8, 9);
                            break;
                        case 8:
                            if ((0x3FF000000000000L & l) == 0L)
                                break;
                            if (kind > 60)
                                kind = 60;
                            jjCheckNAddTwoStates(8, 9);
                            break;
                        case 10:
                            if (this.curChar == '.')
                                jjCheckNAdd(11);
                            break;
                        case 11:
                            if ((0x3FF000000000000L & l) == 0L)
                                break;
                            if (kind > 64)
                                kind = 64;
                            jjCheckNAddStates(24, 26);
                            break;
                        case 13:
                            if ((0x280000000000L & l) != 0L)
                                jjCheckNAdd(14);
                            break;
                        case 14:
                            if ((0x3FF000000000000L & l) == 0L)
                                break;
                            if (kind > 64)
                                kind = 64;
                            jjCheckNAddTwoStates(14, 15);
                            break;
                        case 16:
                            if (this.curChar == '\'')
                                jjAddStates(13, 14);
                            break;
                        case 17:
                            if ((0xFFFFFF7FFFFFDBFFL & l) != 0L)
                                jjCheckNAdd(18);
                            break;
                        case 18:
                            if (this.curChar == '\'' && kind > 66)
                                kind = 66;
                            break;
                        case 20:
                            if ((0x8400000000L & l) != 0L)
                                jjCheckNAdd(18);
                            break;
                        case 21:
                            if ((0xFF000000000000L & l) != 0L)
                                jjCheckNAddTwoStates(22, 18);
                            break;
                        case 22:
                            if ((0xFF000000000000L & l) != 0L)
                                jjCheckNAdd(18);
                            break;
                        case 23:
                            if ((0xF000000000000L & l) != 0L)
                                this.jjstateSet[this.jjnewStateCnt++] = 24;
                            break;
                        case 24:
                            if ((0xFF000000000000L & l) != 0L)
                                jjCheckNAdd(22);
                            break;
                        case 25:
                            if (this.curChar == '"')
                                jjCheckNAddStates(10, 12);
                            break;
                        case 26:
                            if ((0xFFFFFFFBFFFFDBFFL & l) != 0L)
                                jjCheckNAddStates(10, 12);
                            break;
                        case 28:
                            if ((0x8400000000L & l) != 0L)
                                jjCheckNAddStates(10, 12);
                            break;
                        case 29:
                            if (this.curChar == '"' && kind > 67)
                                kind = 67;
                            break;
                        case 30:
                            if ((0xFF000000000000L & l) != 0L)
                                jjCheckNAddStates(27, 30);
                            break;
                        case 31:
                            if ((0xFF000000000000L & l) != 0L)
                                jjCheckNAddStates(10, 12);
                            break;
                        case 32:
                            if ((0xF000000000000L & l) != 0L)
                                this.jjstateSet[this.jjnewStateCnt++] = 33;
                            break;
                        case 33:
                            if ((0xFF000000000000L & l) != 0L)
                                jjCheckNAdd(31);
                            break;
                        case 34:
                            if (this.curChar != '$')
                                break;
                            if (kind > 69)
                                kind = 69;
                            jjCheckNAdd(35);
                            break;
                        case 35:
                            if ((0x3FF001000000000L & l) == 0L)
                                break;
                            if (kind > 69)
                                kind = 69;
                            jjCheckNAdd(35);
                            break;
                        case 36:
                            if ((0x3FF000000000000L & l) != 0L)
                                jjCheckNAddStates(0, 6);
                            break;
                        case 37:
                            if ((0x3FF000000000000L & l) != 0L)
                                jjCheckNAddTwoStates(37, 38);
                            break;
                        case 38:
                            if (this.curChar != '.')
                                break;
                            if (kind > 64)
                                kind = 64;
                            jjCheckNAddStates(31, 33);
                            break;
                        case 39:
                            if ((0x3FF000000000000L & l) == 0L)
                                break;
                            if (kind > 64)
                                kind = 64;
                            jjCheckNAddStates(31, 33);
                            break;
                        case 41:
                            if ((0x280000000000L & l) != 0L)
                                jjCheckNAdd(42);
                            break;
                        case 42:
                            if ((0x3FF000000000000L & l) == 0L)
                                break;
                            if (kind > 64)
                                kind = 64;
                            jjCheckNAddTwoStates(42, 15);
                            break;
                        case 43:
                            if ((0x3FF000000000000L & l) != 0L)
                                jjCheckNAddTwoStates(43, 44);
                            break;
                        case 45:
                            if ((0x280000000000L & l) != 0L)
                                jjCheckNAdd(46);
                            break;
                        case 46:
                            if ((0x3FF000000000000L & l) == 0L)
                                break;
                            if (kind > 64)
                                kind = 64;
                            jjCheckNAddTwoStates(46, 15);
                            break;
                        case 47:
                            if ((0x3FF000000000000L & l) != 0L)
                                jjCheckNAddStates(34, 36);
                            break;
                        case 49:
                            if ((0x280000000000L & l) != 0L)
                                jjCheckNAdd(50);
                            break;
                        case 50:
                            if ((0x3FF000000000000L & l) != 0L)
                                jjCheckNAddTwoStates(50, 15);
                            break;
                        case 51:
                            if (this.curChar != '0')
                                break;
                            if (kind > 60)
                                kind = 60;
                            jjCheckNAddStates(15, 17);
                            break;
                        case 53:
                            if ((0x3FF000000000000L & l) == 0L)
                                break;
                            if (kind > 60)
                                kind = 60;
                            jjCheckNAddTwoStates(53, 9);
                            break;
                        case 54:
                            if ((0xFF000000000000L & l) == 0L)
                                break;
                            if (kind > 60)
                                kind = 60;
                            jjCheckNAddTwoStates(54, 9);
                            break;
                        case 55:
                            if (this.curChar == '/')
                                jjAddStates(7, 9);
                            break;
                        case 57:
                            if ((0xFFFFFFFFFFFFDBFFL & l) == 0L)
                                break;
                            if (kind > 7)
                                kind = 7;
                            jjCheckNAddStates(18, 20);
                            break;
                        case 58:
                            if ((0x2400L & l) != 0L && kind > 7)
                                kind = 7;
                            break;
                        case 59:
                            if (this.curChar == '\n' && kind > 7)
                                kind = 7;
                            break;
                        case 60:
                            if (this.curChar == '\r')
                                this.jjstateSet[this.jjnewStateCnt++] = 59;
                            break;
                        case 61:
                            if (this.curChar == '*')
                                jjCheckNAdd(62);
                            break;
                        case 62:
                            if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
                                jjCheckNAddTwoStates(62, 63);
                            break;
                        case 63:
                            if (this.curChar == '*')
                                jjCheckNAddStates(37, 39);
                            break;
                        case 64:
                            if ((0xFFFF7BFFFFFFFFFFL & l) != 0L)
                                jjCheckNAddTwoStates(65, 63);
                            break;
                        case 65:
                            if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
                                jjCheckNAddTwoStates(65, 63);
                            break;
                        case 66:
                            if (this.curChar == '/' && kind > 9)
                                kind = 9;
                            break;
                        case 67:
                            if (this.curChar == '*')
                                jjCheckNAddTwoStates(68, 69);
                            break;
                        case 68:
                            if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
                                jjCheckNAddTwoStates(68, 69);
                            break;
                        case 69:
                            if (this.curChar == '*')
                                jjCheckNAddStates(40, 42);
                            break;
                        case 70:
                            if ((0xFFFF7BFFFFFFFFFFL & l) != 0L)
                                jjCheckNAddTwoStates(71, 69);
                            break;
                        case 71:
                            if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
                                jjCheckNAddTwoStates(71, 69);
                            break;
                        case 72:
                            if (this.curChar == '/' && kind > 68)
                                kind = 68;
                            break;
                        case 73:
                            if (this.curChar == '*') {
                                this.jjstateSet[this.jjnewStateCnt++] = 67;
                            }
                            break;
                    }
                } while (i != startsAt);
            } else if (this.curChar < '') {

                long l = 1L << (this.curChar & 0x3F);

                do {
                    switch (this.jjstateSet[--i]) {

                        case 6:
                        case 35:
                            if ((0x7FFFFFE87FFFFFEL & l) == 0L)
                                break;
                            if (kind > 69)
                                kind = 69;
                            jjCheckNAdd(35);
                            break;
                        case 2:
                            jjAddStates(21, 23);
                            break;
                        case 9:
                            if ((0x100000001000L & l) != 0L && kind > 60)
                                kind = 60;
                            break;
                        case 12:
                            if ((0x2000000020L & l) != 0L)
                                jjAddStates(43, 44);
                            break;
                        case 15:
                            if ((0x5000000050L & l) != 0L && kind > 64)
                                kind = 64;
                            break;
                        case 17:
                            if ((0xFFFFFFFFEFFFFFFFL & l) != 0L)
                                jjCheckNAdd(18);
                            break;
                        case 19:
                            if (this.curChar == '\\')
                                jjAddStates(45, 47);
                            break;
                        case 20:
                            if ((0x14404410000000L & l) != 0L)
                                jjCheckNAdd(18);
                            break;
                        case 26:
                            if ((0xFFFFFFFFEFFFFFFFL & l) != 0L)
                                jjCheckNAddStates(10, 12);
                            break;
                        case 27:
                            if (this.curChar == '\\')
                                jjAddStates(48, 50);
                            break;
                        case 28:
                            if ((0x14404410000000L & l) != 0L)
                                jjCheckNAddStates(10, 12);
                            break;
                        case 40:
                            if ((0x2000000020L & l) != 0L)
                                jjAddStates(51, 52);
                            break;
                        case 44:
                            if ((0x2000000020L & l) != 0L)
                                jjAddStates(53, 54);
                            break;
                        case 48:
                            if ((0x2000000020L & l) != 0L)
                                jjAddStates(55, 56);
                            break;
                        case 52:
                            if ((0x100000001000000L & l) != 0L)
                                jjCheckNAdd(53);
                            break;
                        case 53:
                            if ((0x7E0000007EL & l) == 0L)
                                break;
                            if (kind > 60)
                                kind = 60;
                            jjCheckNAddTwoStates(53, 9);
                            break;
                        case 57:
                            if (kind > 7)
                                kind = 7;
                            jjAddStates(18, 20);
                            break;
                        case 62:
                            jjCheckNAddTwoStates(62, 63);
                            break;
                        case 64:
                        case 65:
                            jjCheckNAddTwoStates(65, 63);
                            break;
                        case 68:
                            jjCheckNAddTwoStates(68, 69);
                            break;
                        case 70:
                        case 71:
                            jjCheckNAddTwoStates(71, 69);
                            break;
                    }

                } while (i != startsAt);
            } else {

                int hiByte = this.curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3F);
                int i2 = (this.curChar & 0xFF) >> 6;
                long l2 = 1L << (this.curChar & 0x3F);

                do {
                    switch (this.jjstateSet[--i]) {

                        case 6:
                            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {

                                if (kind > 6)
                                    kind = 6;
                                jjCheckNAdd(0);
                            }
                            if (jjCanMove_2(hiByte, i1, i2, l1, l2)) {

                                if (kind > 69)
                                    kind = 69;
                                jjCheckNAdd(35);
                            }
                            break;
                        case 0:
                            if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                                break;
                            if (kind > 6)
                                kind = 6;
                            jjCheckNAdd(0);
                            break;
                        case 2:
                            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                                jjAddStates(21, 23);
                            break;
                        case 17:
                            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                                this.jjstateSet[this.jjnewStateCnt++] = 18;
                            break;
                        case 26:
                            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                                jjAddStates(10, 12);
                            break;
                        case 34:
                        case 35:
                            if (!jjCanMove_2(hiByte, i1, i2, l1, l2))
                                break;
                            if (kind > 69)
                                kind = 69;
                            jjCheckNAdd(35);
                            break;
                        case 57:
                            if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                                break;
                            if (kind > 7)
                                kind = 7;
                            jjAddStates(18, 20);
                            break;
                        case 62:
                            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                                jjCheckNAddTwoStates(62, 63);
                            break;
                        case 64:
                        case 65:
                            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                                jjCheckNAddTwoStates(65, 63);
                            break;
                        case 68:
                            if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                                jjCheckNAddTwoStates(68, 69);
                            break;
                        case 70:
                        case 71:
                            if (jjCanMove_1(hiByte, i1, i2, l1, l2)) {
                                jjCheckNAddTwoStates(71, 69);
                            }
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {

                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            if ((i = this.jjnewStateCnt) == (startsAt = 74 - (this.jjnewStateCnt = startsAt)))
                return curPos;
            try {
                this.curChar = this.input_stream.readChar();
            } catch (IOException e) {
                return curPos;
            }

        }
    }

    public void ReInit(JavaCharStream stream) {
        this.jjmatchedPos = this.jjnewStateCnt = 0;
        this.curLexState = this.defaultLexState;
        this.input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds() {
        this.jjround = -2147483647;
        for (int i = 74; i-- > 0; )
            this.jjrounds[i] = Integer.MIN_VALUE;
    }

    public void ReInit(JavaCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0) {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        }
        this.curLexState = lexState;
    }

    protected Token jjFillToken() {
        Token t = Token.newToken(this.jjmatchedKind);
        t.kind = this.jjmatchedKind;
        String im = jjstrLiteralImages[this.jjmatchedKind];
        t.image = (im == null) ? this.input_stream.GetImage() : im;
        t.beginLine = this.input_stream.getBeginLine();
        t.beginColumn = this.input_stream.getBeginColumn();
        t.endLine = this.input_stream.getEndLine();
        t.endColumn = this.input_stream.getEndColumn();
        return t;
    }

    public Token getNextToken() {
        Token specialToken = null;

        int curPos = 0;

        while (true) {
            try {
                this.curChar = this.input_stream.BeginToken();
            } catch (IOException e) {

                this.jjmatchedKind = 0;
                Token matchedToken = jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }

            this.jjmatchedKind = Integer.MAX_VALUE;
            this.jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            if (this.jjmatchedKind != Integer.MAX_VALUE) {

                if (this.jjmatchedPos + 1 < curPos)
                    this.input_stream.backup(curPos - this.jjmatchedPos - 1);
                if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {

                    Token matchedToken = jjFillToken();
                    matchedToken.specialToken = specialToken;
                    return matchedToken;
                }

                if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {

                    Token matchedToken = jjFillToken();
                    if (specialToken == null) {
                        specialToken = matchedToken;
                        continue;
                    }
                    matchedToken.specialToken = specialToken;
                    specialToken = specialToken.next = matchedToken;
                }
                continue;
            }
            break;
        }
        int error_line = this.input_stream.getEndLine();
        int error_column = this.input_stream.getEndColumn();
        String error_after = null;
        boolean EOFSeen = false;
        try {
            this.input_stream.readChar();
            this.input_stream.backup(1);
        } catch (IOException e1) {
            EOFSeen = true;
            error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
            if (this.curChar == '\n' || this.curChar == '\r') {
                error_line++;
                error_column = 0;
            } else {

                error_column++;
            }
        }
        if (!EOFSeen) {
            this.input_stream.backup(1);
            error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
        }
        throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
    }
}

