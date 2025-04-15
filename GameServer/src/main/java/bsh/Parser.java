package bsh;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Vector;

public class Parser
        implements ParserTreeConstants, ParserConstants {
    private static int[] jj_la1_0;
    private static int[] jj_la1_1;
    private static int[] jj_la1_2;
    private static int[] jj_la1_3;
    private static int[] jj_la1_4;

    static {
        jj_la1_0();
        jj_la1_1();
        jj_la1_2();
        jj_la1_3();
        jj_la1_4();
    }

    private final int[] jj_la1 = new int[87];
    private final JJCalls[] jj_2_rtns = new JJCalls[31];
    private final LookaheadSuccess jj_ls;
    public ParserTokenManager token_source;
    public Token token;
    public Token jj_nt;
    public boolean lookingAhead = false;
    protected JJTParserState jjtree = new JJTParserState();
    boolean retainComments = false;
    JavaCharStream jj_input_stream;
    private int jj_ntk;
    private Token jj_scanpos;
    private Token jj_lastpos;
    private int jj_la;
    private boolean jj_semLA;
    private int jj_gen;
    private boolean jj_rescan = false;
    private int jj_gc = 0;
    private Vector jj_expentries;
    private int[] jj_expentry;
    private int jj_kind;
    private int[] jj_lasttokens;
    private int jj_endpos;

    public Parser(InputStream stream) {
        this.jj_ls = new LookaheadSuccess();

        this.jj_expentries = new Vector();

        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.jj_input_stream = new JavaCharStream(stream, 1, 1);
        this.token_source = new ParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        int i;
        for (i = 0; i < 87;) {
            this.jj_la1[i] = -1;
            i++;
        }
        for (i = 0; i < this.jj_2_rtns.length;) {
            this.jj_2_rtns[i] = new JJCalls();
            i++;
        }
    }

    public Parser(Reader stream) {
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new Vector();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.jj_input_stream = new JavaCharStream(stream, 1, 1);
        this.token_source = new ParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        int i;
        for (i = 0; i < 87;) {
            this.jj_la1[i] = -1;
            i++;
        }
        for (i = 0; i < this.jj_2_rtns.length;) {
            this.jj_2_rtns[i] = new JJCalls();
            i++;
        }
    }

    public Parser(ParserTokenManager tm) {
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new Vector();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        int i;
        for (i = 0; i < 87;) {
            this.jj_la1[i] = -1;
            i++;
        }
        for (i = 0; i < this.jj_2_rtns.length;) {
            this.jj_2_rtns[i] = new JJCalls();
            i++;
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        boolean print = false;
        int i = 0;
        if (args[0].equals("-p")) {
            i++;
            print = true;
        }
        for (; i < args.length; i++) {
            Reader in = new FileReader(args[i]);
            Parser parser = new Parser(in);
            parser.setRetainComments(true);
            while (!parser.Line()) {
                if (print) {
                    System.out.println(parser.popNode());
                }
            }
        }
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[] { 1, 134218752, 134218752, 8192, 33554432, 0, 541214720, 0, 0, 0, 0, 0, 0, 608323584,
                608323584, 0, 0, 541214720, 0, 541214720, 541214720, 541214720, 0, 608323584, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 608323584, 0, 0, 608323584, 67108864, 0, 0, 608323584, 0, 0,
                67108864, 0, 0, 0, 67108864, 67108864, 608323584, 0, 0, 0, 0, 0, 610420736, 1074270208, 0, 1081344,
                1081344, 8388608, 742542336, 608323584, 608323584, 1073741824, 608323584, 0, 0, 0, 0, 608323584, 65536,
                268435456 };
    }

    private static void jj_la1_1() {
        jj_la1_1 = new int[] { 0, 68892800, 68892800, 32, 0, 2, 33587280, 4194304, 0, 65536, 0, 4, 0, 310412112,
                310412112, 0, 0, 32848, 0, 32848, 33587280, 32848, 0, 310412112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 310412112, 0, 0, 310412112, 310379264, 0, 0, 310412112, 0, 0, 310379264,
                0, 0, 0, 310379008, 8388608, 310412112, 0, 0, 256, 0, 0, 444891985, 19415040, 66564, 0, 0, 0, 379304912,
                310412112, 310412112, 0, 310412112, 0, 0, 0, 0, 310412112, 0, 0 };
    }

    private static void jj_la1_2() {
        jj_la1_2 = new int[] { 0, 0, 0, 0, 0, 0, 32, 0, 17408, 0, 65536, 0, 131072, 12584237, 12584237, 32768, 32768,
                32, 32, 32, 32, 0, 32768, 12583213, 131072, 16777216, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2080374784,
                -2080374784, 0, 2017198080, 2017198080, 0, 0, 0, 0, 0, 0, 0, 12583213, 12582912, 12582912, 301,
                12583213, 256, 0, 301, 256, 70656, 269, 32, 256, 70656, 13, 0, 12583213, 32768, 4352, 0, 4096, 4096,
                12600621, 0, 16, 0, 0, 0, 12583213, 12583213, 12583213, 0, 12583213, 32768, 32768, 32, 32, 12583213, 0,
                0 };
    }

    private static void jj_la1_3() {
        jj_la1_3 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 240, 240, 0, 0, 0, 0, 0, 0, 0, 0, 240, -675282944,
                0, 3, 3, 12, 12, 12288, 12288, 16384, 3072, 3072, 0, 0, 0, 0, 0, 4128768, 4128768, 192, 192, 33536,
                33536, 192, 240, 0, 0, 0, 0, 0, 48, 0, 0, 0, 0, 0, 0, 0, 0, 0, 240, 0, 0, 0, 0, 0, 240, 0, 0, 0, 0, 0,
                240, 240, 240, 0, 240, 0, 0, 0, 0, 240, 0, 0 };
    }

    private static void jj_la1_4() {
        jj_la1_4 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    }

    public void setRetainComments(boolean b) {
        this.retainComments = b;
    }

    void jjtreeOpenNodeScope(Node n) {
        ((SimpleNode) n).firstToken = getToken(1);
    }

    void jjtreeCloseNodeScope(Node n) {
        ((SimpleNode) n).lastToken = getToken(0);
    }

    void reInitInput(Reader in) {
        ReInit(in);
    }

    public SimpleNode popNode() {
        if (this.jjtree.nodeArity() > 0) {
            return (SimpleNode) this.jjtree.popNode();
        }
        return null;
    }

    void reInitTokenInput(Reader in) {
        this.jj_input_stream.ReInit(in, this.jj_input_stream.getEndLine(), this.jj_input_stream.getEndColumn());
    }

    boolean isRegularForStatement() {
        int curTok = 1;

        Token tok = getToken(curTok++);
        if (tok.kind != 30) // not 'for'
            return false;

        tok = getToken(curTok++);
        if (tok.kind != 72) // not '('
            return false;

        while (true) {
            tok = getToken(curTok++);
            switch (tok.kind) {
                case 89:
                    return true;
                case 78:
                    return false;
                case 0:
                    return false;
            }
        }
    }

    ParseException createParseException(String message) {
        Token errortok = this.token;
        int line = errortok.beginLine, column = errortok.beginColumn;
        String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
        return new ParseException("Parse error at line " + line + ", column " + column + " : " + message);
    }

    public final boolean Line() throws ParseException {
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 0:
                jj_consume_token(0);
                Interpreter.debug("End of File!");
                return true;
        }

        this.jj_la1[0] = this.jj_gen;
        if (jj_2_1(1)) {
            BlockStatement();
            return false;
        }
        jj_consume_token(-1);
        throw new ParseException();
    }

    public final Modifiers Modifiers(int context, boolean lookahead) throws ParseException {
        Modifiers mods = null;

        while (true) {
            int kind = (this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk;

            // Nếu không còn token hợp lệ → thoát vòng lặp
            switch (kind) {
                case 10:
                case 27:
                case 39:
                case 43:
                case 44:
                case 45:
                case 48:
                case 49:
                case 51:
                case 52:
                case 58:
                    break; // tiếp tục xử lý
                default:
                    this.jj_la1[1] = this.jj_gen;
                    break;
            }

            // Tiêu thụ token
            switch (kind) {
                case 43:
                    jj_consume_token(43);
                    break;
                case 44:
                    jj_consume_token(44);
                    break;
                case 45:
                    jj_consume_token(45);
                    break;
                case 51:
                    jj_consume_token(51);
                    break;
                case 27:
                    jj_consume_token(27);
                    break;
                case 39:
                    jj_consume_token(39);
                    break;
                case 52:
                    jj_consume_token(52);
                    break;
                case 58:
                    jj_consume_token(58);
                    break;
                case 10:
                    jj_consume_token(10);
                    break;
                case 48:
                    jj_consume_token(48);
                    break;
                case 49:
                    jj_consume_token(49);
                    break;
                default:
                    this.jj_la1[2] = this.jj_gen;
                    jj_consume_token(-1); // unexpected token
                    throw new ParseException();
            }

            if (!lookahead) {
                try {
                    if (mods == null)
                        mods = new Modifiers();
                    mods.addModifier(context, getToken(0).image);
                } catch (IllegalStateException e) {
                    throw createParseException(e.getMessage());
                }
            }

            // ✅ Điều kiện dừng vòng lặp sau mỗi vòng
            this.jj_ntk = -1; // reset lookahead
            if ((this.jj_ntk = jj_ntk()) != 10 &&
                    this.jj_ntk != 27 && this.jj_ntk != 39 && this.jj_ntk != 43 &&
                    this.jj_ntk != 44 && this.jj_ntk != 45 && this.jj_ntk != 48 &&
                    this.jj_ntk != 49 && this.jj_ntk != 51 && this.jj_ntk != 52 &&
                    this.jj_ntk != 58) {
                break; // ✅ thoát vòng lặp nếu không còn modifier
            }
        }
        return mods;

    }

    public final void ClassDeclaration() throws ParseException {
        BSHClassDeclaration jjtn000 = new BSHClassDeclaration(1);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);

        try {
            int numInterfaces;
            Modifiers mods = Modifiers(0, false);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 13:
                    jj_consume_token(13);
                    break;
                case 37:
                    jj_consume_token(37);
                    jjtn000.isInterface = true;
                    break;
                default:
                    this.jj_la1[3] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            Token name = jj_consume_token(69);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 25:
                    jj_consume_token(25);
                    AmbiguousName();
                    jjtn000.extend = true;
                    break;
                default:
                    this.jj_la1[4] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 33:
                    jj_consume_token(33);
                    numInterfaces = NameList();
                    jjtn000.numInterfaces = numInterfaces;
                    break;
                default:
                    this.jj_la1[5] = this.jj_gen;
                    break;
            }
            Block();
            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.modifiers = mods;
            jjtn000.name = name.image;
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void MethodDeclaration() throws ParseException {
        BSHMethodDeclaration jjtn000 = new BSHMethodDeclaration(2);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        Token t = null;

        try {
            int count;
            Modifiers mods = Modifiers(1, false);
            jjtn000.modifiers = mods;
            if (jj_2_2(2147483647)) {
                t = jj_consume_token(69);
                jjtn000.name = t.image;
            } else {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 11:
                    case 14:
                    case 17:
                    case 22:
                    case 29:
                    case 36:
                    case 38:
                    case 47:
                    case 57:
                    case 69:
                        ReturnType();
                        t = jj_consume_token(69);
                        jjtn000.name = t.image;
                        break;
                    default:
                        this.jj_la1[6] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
            FormalParameters();
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 54:
                    jj_consume_token(54);
                    count = NameList();
                    jjtn000.numThrows = count;
                    break;
                default:
                    this.jj_la1[7] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 74:
                    Block();
                    break;
                case 78:
                    jj_consume_token(78);
                    break;
                default:
                    this.jj_la1[8] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void PackageDeclaration() throws ParseException {
        BSHPackageDeclaration jjtn000 = new BSHPackageDeclaration(3);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(42);
            AmbiguousName();
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ImportDeclaration() throws ParseException {
        BSHImportDeclaration jjtn000 = new BSHImportDeclaration(4);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        Token s = null;
        Token t = null;
        try {
            if (jj_2_3(3)) {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 48:
                        s = jj_consume_token(48);
                        break;
                    default:
                        this.jj_la1[9] = this.jj_gen;
                        break;
                }
                jj_consume_token(34);
                AmbiguousName();
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 80:
                        t = jj_consume_token(80);
                        jj_consume_token(104);
                        break;
                    default:
                        this.jj_la1[10] = this.jj_gen;
                        break;
                }
                jj_consume_token(78);
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtc000 = false;
                jjtreeCloseNodeScope(jjtn000);
                if (s != null)
                    jjtn000.staticImport = true;
                if (t != null)
                    jjtn000.importPackage = true;
            } else {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 34:
                        jj_consume_token(34);
                        jj_consume_token(104);
                        jj_consume_token(78);
                        this.jjtree.closeNodeScope(jjtn000, true);
                        jjtc000 = false;
                        jjtreeCloseNodeScope(jjtn000);
                        jjtn000.superImport = true;
                        break;
                    default:
                        this.jj_la1[11] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void VariableDeclarator() throws ParseException {
        BSHVariableDeclarator jjtn000 = new BSHVariableDeclarator(5);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            Token t = jj_consume_token(69);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 81:
                    jj_consume_token(81);
                    VariableInitializer();
                    break;
                default:
                    this.jj_la1[12] = this.jj_gen;
                    break;
            }
            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.name = t.image;
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void VariableInitializer() throws ParseException {
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 74:
                ArrayInitializer();
                return;
            case 11:
            case 14:
            case 17:
            case 22:
            case 26:
            case 29:
            case 36:
            case 38:
            case 40:
            case 41:
            case 47:
            case 55:
            case 57:
            case 60:
            case 64:
            case 66:
            case 67:
            case 69:
            case 72:
            case 86:
            case 87:
            case 100:
            case 101:
            case 102:
            case 103:
                Expression();
                return;
        }
        this.jj_la1[13] = this.jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }

    public final void ArrayInitializer() throws ParseException {
        BSHArrayInitializer jjtn000 = new BSHArrayInitializer(6);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(74);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 74:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    VariableInitializer();

                    while (jj_2_4(2)) {

                        jj_consume_token(79);
                        VariableInitializer();
                    }
                    break;
                default:
                    this.jj_la1[14] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 79:
                    jj_consume_token(79);
                    break;
                default:
                    this.jj_la1[15] = this.jj_gen;
                    break;
            }
            jj_consume_token(75);
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void FormalParameters() throws ParseException {
        BSHFormalParameters jjtn000 = new BSHFormalParameters(7);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(72);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 29:
                case 36:
                case 38:
                case 47:
                case 69:
                    FormalParameter();

                    while (true) {
                        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                            case 79:
                                break;

                            default:
                                this.jj_la1[16] = this.jj_gen;
                                break;
                        }
                        jj_consume_token(79);
                        FormalParameter();
                    }
                default:
                    this.jj_la1[17] = this.jj_gen;
                    break;
            }
            jj_consume_token(73);
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void FormalParameter() throws ParseException {
        BSHFormalParameter jjtn000 = new BSHFormalParameter(8);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            if (jj_2_5(2)) {
                Type();
                Token t = jj_consume_token(69);
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtc000 = false;
                jjtreeCloseNodeScope(jjtn000);
                jjtn000.name = t.image;
            } else {
                Token t;
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 69:
                        t = jj_consume_token(69);
                        this.jjtree.closeNodeScope(jjtn000, true);
                        jjtc000 = false;
                        jjtreeCloseNodeScope(jjtn000);
                        jjtn000.name = t.image;
                        break;
                    default:
                        this.jj_la1[18] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void Type() throws ParseException {
        BSHType jjtn000 = new BSHType(9);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 29:
                case 36:
                case 38:
                case 47:
                    PrimitiveType();
                    break;
                case 69:
                    AmbiguousName();
                    break;
                default:
                    this.jj_la1[19] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }

            while (jj_2_6(2)) {

                jj_consume_token(76);
                jj_consume_token(77);
                jjtn000.addArrayDimension();
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ReturnType() throws ParseException {
        BSHReturnType jjtn000 = new BSHReturnType(10);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 57:
                    jj_consume_token(57);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.isVoid = true;
                    break;
                case 11:
                case 14:
                case 17:
                case 22:
                case 29:
                case 36:
                case 38:
                case 47:
                case 69:
                    Type();
                    break;
                default:
                    this.jj_la1[20] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void PrimitiveType() throws ParseException {
        BSHPrimitiveType jjtn000 = new BSHPrimitiveType(11);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                    jj_consume_token(11);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.type = boolean.class;
                    break;
                case 17:
                    jj_consume_token(17);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.type = char.class;
                    break;
                case 14:
                    jj_consume_token(14);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.type = byte.class;
                    break;
                case 47:
                    jj_consume_token(47);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.type = short.class;
                    break;
                case 36:
                    jj_consume_token(36);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.type = int.class;
                    break;
                case 38:
                    jj_consume_token(38);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.type = long.class;
                    break;
                case 29:
                    jj_consume_token(29);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.type = float.class;
                    break;
                case 22:
                    jj_consume_token(22);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.type = double.class;
                    break;
                default:
                    this.jj_la1[21] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void AmbiguousName() throws ParseException {
        BSHAmbiguousName jjtn000 = new BSHAmbiguousName(12);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);

        try {
            Token t = jj_consume_token(69);
            StringBuffer s = new StringBuffer(t.image);

            while (jj_2_7(2)) {

                jj_consume_token(80);
                t = jj_consume_token(69);
                s.append("." + t.image);
            }
            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.text = s.toString();
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final int NameList() throws ParseException {
        int count = 0;
        AmbiguousName();
        count++;

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 79:
                    jj_consume_token(79);
                    AmbiguousName();
                    count++;
                    break;

                default:
                    this.jj_la1[22] = this.jj_gen;
                    return count;
            }
        }
    }

    public final void Expression() throws ParseException {
        if (jj_2_8(2147483647)) {
            Assignment();
        } else {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    ConditionalExpression();
                    return;
            }
            this.jj_la1[23] = this.jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void Assignment() throws ParseException {
        BSHAssignment jjtn000 = new BSHAssignment(13);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            PrimaryExpression();
            int op = AssignmentOperator();
            jjtn000.operator = op;
            Expression();
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final int AssignmentOperator() throws ParseException {
        Token t;
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 81:
                jj_consume_token(81);

                t = getToken(0);
                return t.kind;
            case 120:
                jj_consume_token(120);
                t = getToken(0);
                return t.kind;
            case 121:
                jj_consume_token(121);
                t = getToken(0);
                return t.kind;
            case 127:
                jj_consume_token(127);
                t = getToken(0);
                return t.kind;
            case 118:
                jj_consume_token(118);
                t = getToken(0);
                return t.kind;
            case 119:
                jj_consume_token(119);
                t = getToken(0);
                return t.kind;
            case 122:
                jj_consume_token(122);
                t = getToken(0);
                return t.kind;
            case 126:
                jj_consume_token(126);
                t = getToken(0);
                return t.kind;
            case 124:
                jj_consume_token(124);
                t = getToken(0);
                return t.kind;
            case 128:
                jj_consume_token(128);
                t = getToken(0);
                return t.kind;
            case 129:
                jj_consume_token(129);
                t = getToken(0);
                return t.kind;
            case 130:
                jj_consume_token(130);
                t = getToken(0);
                return t.kind;
            case 131:
                jj_consume_token(131);
                t = getToken(0);
                return t.kind;
            case 132:
                jj_consume_token(132);
                t = getToken(0);
                return t.kind;
            case 133:
                jj_consume_token(133);
                t = getToken(0);
                return t.kind;
        }
        this.jj_la1[24] = this.jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }

    public final void ConditionalExpression() throws ParseException {
        BSHTernaryExpression jjtn001;
        boolean jjtc001;
        ConditionalOrExpression();
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 88:
                jj_consume_token(88);
                Expression();
                jj_consume_token(89);
                jjtn001 = new BSHTernaryExpression(14);
                jjtc001 = true;
                this.jjtree.openNodeScope(jjtn001);
                jjtreeOpenNodeScope(jjtn001);
                try {
                    ConditionalExpression();
                } catch (Throwable jjte001) {
                    if (jjtc001) {
                        this.jjtree.clearNodeScope(jjtn001);
                        jjtc001 = false;
                    } else {
                        this.jjtree.popNode();
                    }
                    if (jjte001 instanceof RuntimeException) {
                        throw (RuntimeException) jjte001;
                    }
                    if (jjte001 instanceof ParseException) {
                        throw (ParseException) jjte001;
                    }
                    throw (Error) jjte001;
                } finally {
                    if (jjtc001) {
                        this.jjtree.closeNodeScope(jjtn001, 3);
                        jjtreeCloseNodeScope(jjtn001);
                    }
                }
                return;
        }
        this.jj_la1[25] = this.jj_gen;
    }

    public final void ConditionalOrExpression() throws ParseException {
        Token t = null;
        ConditionalAndExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 96:
                case 97:
                    break;

                default:
                    this.jj_la1[26] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 96:
                    t = jj_consume_token(96);
                    break;
                case 97:
                    t = jj_consume_token(97);
                    break;
                default:
                    this.jj_la1[27] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            ConditionalAndExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void ConditionalAndExpression() throws ParseException {
        Token t = null;
        InclusiveOrExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 98:
                case 99:
                    break;

                default:
                    this.jj_la1[28] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 98:
                    t = jj_consume_token(98);
                    break;
                case 99:
                    t = jj_consume_token(99);
                    break;
                default:
                    this.jj_la1[29] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            InclusiveOrExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void InclusiveOrExpression() throws ParseException {
        Token t = null;
        ExclusiveOrExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 108:
                case 109:
                    break;

                default:
                    this.jj_la1[30] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 108:
                    t = jj_consume_token(108);
                    break;
                case 109:
                    t = jj_consume_token(109);
                    break;
                default:
                    this.jj_la1[31] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            ExclusiveOrExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void ExclusiveOrExpression() throws ParseException {
        Token t = null;
        AndExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 110:
                    break;

                default:
                    this.jj_la1[32] = this.jj_gen;
                    break;
            }
            t = jj_consume_token(110);
            AndExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void AndExpression() throws ParseException {
        Token t = null;
        EqualityExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 106:
                case 107:
                    break;

                default:
                    this.jj_la1[33] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 106:
                    t = jj_consume_token(106);
                    break;
                case 107:
                    t = jj_consume_token(107);
                    break;
                default:
                    this.jj_la1[34] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            EqualityExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void EqualityExpression() throws ParseException {
        Token t = null;
        InstanceOfExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 90:
                case 95:
                    break;

                default:
                    this.jj_la1[35] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 90:
                    t = jj_consume_token(90);
                    break;
                case 95:
                    t = jj_consume_token(95);
                    break;
                default:
                    this.jj_la1[36] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            InstanceOfExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void InstanceOfExpression() throws ParseException {
        BSHBinaryExpression jjtn001;
        boolean jjtc001;
        Token t = null;
        RelationalExpression();
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 35:
                t = jj_consume_token(35);
                Type();
                jjtn001 = new BSHBinaryExpression(15);
                jjtc001 = true;
                this.jjtree.openNodeScope(jjtn001);
                jjtreeOpenNodeScope(jjtn001);
                try {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtc001 = false;
                    jjtreeCloseNodeScope(jjtn001);
                    jjtn001.kind = t.kind;
                } finally {
                    if (jjtc001) {
                        this.jjtree.closeNodeScope(jjtn001, 2);
                        jjtreeCloseNodeScope(jjtn001);
                    }
                }
                return;
        }
        this.jj_la1[37] = this.jj_gen;
    }

    public final void RelationalExpression() throws ParseException {
        Token t = null;
        ShiftExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 82:
                case 83:
                case 84:
                case 85:
                case 91:
                case 92:
                case 93:
                case 94:
                    break;

                default:
                    this.jj_la1[38] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 84:
                    t = jj_consume_token(84);
                    break;
                case 85:
                    t = jj_consume_token(85);
                    break;
                case 82:
                    t = jj_consume_token(82);
                    break;
                case 83:
                    t = jj_consume_token(83);
                    break;
                case 91:
                    t = jj_consume_token(91);
                    break;
                case 92:
                    t = jj_consume_token(92);
                    break;
                case 93:
                    t = jj_consume_token(93);
                    break;
                case 94:
                    t = jj_consume_token(94);
                    break;
                default:
                    this.jj_la1[39] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            ShiftExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void ShiftExpression() throws ParseException {
        Token t = null;
        AdditiveExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                    break;

                default:
                    this.jj_la1[40] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 112:
                    t = jj_consume_token(112);
                    break;
                case 113:
                    t = jj_consume_token(113);
                    break;
                case 114:
                    t = jj_consume_token(114);
                    break;
                case 115:
                    t = jj_consume_token(115);
                    break;
                case 116:
                    t = jj_consume_token(116);
                    break;
                case 117:
                    t = jj_consume_token(117);
                    break;
                default:
                    this.jj_la1[41] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            AdditiveExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void AdditiveExpression() throws ParseException {
        Token t = null;
        MultiplicativeExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 102:
                case 103:
                    break;

                default:
                    this.jj_la1[42] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 102:
                    t = jj_consume_token(102);
                    break;
                case 103:
                    t = jj_consume_token(103);
                    break;
                default:
                    this.jj_la1[43] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            MultiplicativeExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void MultiplicativeExpression() throws ParseException {
        Token t = null;
        UnaryExpression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 104:
                case 105:
                case 111:
                    break;

                default:
                    this.jj_la1[44] = this.jj_gen;
                    break;
            }
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 104:
                    t = jj_consume_token(104);
                    break;
                case 105:
                    t = jj_consume_token(105);
                    break;
                case 111:
                    t = jj_consume_token(111);
                    break;
                default:
                    this.jj_la1[45] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            UnaryExpression();
            BSHBinaryExpression jjtn001 = new BSHBinaryExpression(15);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 2);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 2);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        }
    }

    public final void UnaryExpression() throws ParseException {
        BSHUnaryExpression jjtn001;
        boolean jjtc001;
        Token t = null;
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 102:
            case 103:
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 102:
                        t = jj_consume_token(102);
                        break;
                    case 103:
                        t = jj_consume_token(103);
                        break;
                    default:
                        this.jj_la1[46] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                UnaryExpression();
                jjtn001 = new BSHUnaryExpression(16);
                jjtc001 = true;
                this.jjtree.openNodeScope(jjtn001);
                jjtreeOpenNodeScope(jjtn001);
                try {
                    this.jjtree.closeNodeScope(jjtn001, 1);
                    jjtc001 = false;
                    jjtreeCloseNodeScope(jjtn001);
                    jjtn001.kind = t.kind;
                } finally {
                    if (jjtc001) {
                        this.jjtree.closeNodeScope(jjtn001, 1);
                        jjtreeCloseNodeScope(jjtn001);
                    }
                }
                return;
            case 100:
                PreIncrementExpression();
                return;
            case 101:
                PreDecrementExpression();
                return;
            case 11:
            case 14:
            case 17:
            case 22:
            case 26:
            case 29:
            case 36:
            case 38:
            case 40:
            case 41:
            case 47:
            case 55:
            case 57:
            case 60:
            case 64:
            case 66:
            case 67:
            case 69:
            case 72:
            case 86:
            case 87:
                UnaryExpressionNotPlusMinus();
                return;
        }
        this.jj_la1[47] = this.jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }

    public final void PreIncrementExpression() throws ParseException {
        Token t = null;
        t = jj_consume_token(100);
        PrimaryExpression();
        BSHUnaryExpression jjtn001 = new BSHUnaryExpression(16);
        boolean jjtc001 = true;
        this.jjtree.openNodeScope(jjtn001);
        jjtreeOpenNodeScope(jjtn001);
        try {
            this.jjtree.closeNodeScope(jjtn001, 1);
            jjtc001 = false;
            jjtreeCloseNodeScope(jjtn001);
            jjtn001.kind = t.kind;
        } finally {
            if (jjtc001) {
                this.jjtree.closeNodeScope(jjtn001, 1);
                jjtreeCloseNodeScope(jjtn001);
            }
        }
    }

    public final void PreDecrementExpression() throws ParseException {
        Token t = null;
        t = jj_consume_token(101);
        PrimaryExpression();
        BSHUnaryExpression jjtn001 = new BSHUnaryExpression(16);
        boolean jjtc001 = true;
        this.jjtree.openNodeScope(jjtn001);
        jjtreeOpenNodeScope(jjtn001);
        try {
            this.jjtree.closeNodeScope(jjtn001, 1);
            jjtc001 = false;
            jjtreeCloseNodeScope(jjtn001);
            jjtn001.kind = t.kind;
        } finally {
            if (jjtc001) {
                this.jjtree.closeNodeScope(jjtn001, 1);
                jjtreeCloseNodeScope(jjtn001);
            }
        }
    }

    public final void UnaryExpressionNotPlusMinus() throws ParseException {
        BSHUnaryExpression jjtn001;
        boolean jjtc001;
        Token t = null;
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 86:
            case 87:
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 87:
                        t = jj_consume_token(87);
                        break;
                    case 86:
                        t = jj_consume_token(86);
                        break;
                    default:
                        this.jj_la1[48] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                UnaryExpression();
                jjtn001 = new BSHUnaryExpression(16);
                jjtc001 = true;
                this.jjtree.openNodeScope(jjtn001);
                jjtreeOpenNodeScope(jjtn001);
                try {
                    this.jjtree.closeNodeScope(jjtn001, 1);
                    jjtc001 = false;
                    jjtreeCloseNodeScope(jjtn001);
                    jjtn001.kind = t.kind;
                } finally {
                    if (jjtc001) {
                        this.jjtree.closeNodeScope(jjtn001, 1);
                        jjtreeCloseNodeScope(jjtn001);
                    }
                }
                return;
        }
        this.jj_la1[49] = this.jj_gen;
        if (jj_2_9(2147483647)) {
            CastExpression();
        } else {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                    PostfixExpression();
                    return;
            }
            this.jj_la1[50] = this.jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void CastLookahead() throws ParseException {
        if (jj_2_10(2)) {
            jj_consume_token(72);
            PrimitiveType();
        } else if (jj_2_11(2147483647)) {
            jj_consume_token(72);
            AmbiguousName();
            jj_consume_token(76);
            jj_consume_token(77);
        } else {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 72:
                    jj_consume_token(72);
                    AmbiguousName();
                    jj_consume_token(73);
                    switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                        case 87:
                            jj_consume_token(87);
                            return;
                        case 86:
                            jj_consume_token(86);
                            return;
                        case 72:
                            jj_consume_token(72);
                            return;
                        case 69:
                            jj_consume_token(69);
                            return;
                        case 40:
                            jj_consume_token(40);
                            return;
                        case 26:
                        case 41:
                        case 55:
                        case 57:
                        case 60:
                        case 64:
                        case 66:
                        case 67:
                            Literal();
                            return;
                    }
                    this.jj_la1[51] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }

            this.jj_la1[52] = this.jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void PostfixExpression() throws ParseException {
        Token t = null;
        if (jj_2_12(2147483647)) {
            PrimaryExpression();
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 100:
                    t = jj_consume_token(100);
                    break;
                case 101:
                    t = jj_consume_token(101);
                    break;
                default:
                    this.jj_la1[53] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            BSHUnaryExpression jjtn001 = new BSHUnaryExpression(16);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);
            jjtreeOpenNodeScope(jjtn001);
            try {
                this.jjtree.closeNodeScope(jjtn001, 1);
                jjtc001 = false;
                jjtreeCloseNodeScope(jjtn001);
                jjtn001.kind = t.kind;
                jjtn001.postfix = true;
            } finally {
                if (jjtc001) {
                    this.jjtree.closeNodeScope(jjtn001, 1);
                    jjtreeCloseNodeScope(jjtn001);
                }
            }
        } else {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                    PrimaryExpression();
                    return;
            }
            this.jj_la1[54] = this.jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void CastExpression() throws ParseException {
        BSHCastExpression jjtn000 = new BSHCastExpression(17);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            if (jj_2_13(2147483647)) {
                jj_consume_token(72);
                Type();
                jj_consume_token(73);
                UnaryExpression();
            } else {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 72:
                        jj_consume_token(72);
                        Type();
                        jj_consume_token(73);
                        UnaryExpressionNotPlusMinus();
                        break;
                    default:
                        this.jj_la1[55] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void PrimaryExpression() throws ParseException {
        BSHPrimaryExpression jjtn000 = new BSHPrimaryExpression(18);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            PrimaryPrefix();

            while (true) {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 74:
                    case 76:
                    case 80:
                        break;

                    default:
                        this.jj_la1[56] = this.jj_gen;
                        break;
                }
                PrimarySuffix();
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void MethodInvocation() throws ParseException {
        BSHMethodInvocation jjtn000 = new BSHMethodInvocation(19);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            AmbiguousName();
            Arguments();
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void PrimaryPrefix() throws ParseException {
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 26:
            case 41:
            case 55:
            case 57:
            case 60:
            case 64:
            case 66:
            case 67:
                Literal();
                return;
            case 72:
                jj_consume_token(72);
                Expression();
                jj_consume_token(73);
                return;
            case 40:
                AllocationExpression();
                return;
        }
        this.jj_la1[57] = this.jj_gen;
        if (jj_2_14(2147483647)) {
            MethodInvocation();
        } else if (jj_2_15(2147483647)) {
            Type();
        } else {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 69:
                    AmbiguousName();
                    return;
            }
            this.jj_la1[58] = this.jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void PrimarySuffix() throws ParseException {
        BSHPrimarySuffix jjtn000 = new BSHPrimarySuffix(20);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        Token t = null;
        try {
            if (jj_2_16(2)) {
                jj_consume_token(80);
                jj_consume_token(13);
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtc000 = false;
                jjtreeCloseNodeScope(jjtn000);
                jjtn000.operation = 0;
            } else {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 76:
                        jj_consume_token(76);
                        Expression();
                        jj_consume_token(77);
                        this.jjtree.closeNodeScope(jjtn000, true);
                        jjtc000 = false;
                        jjtreeCloseNodeScope(jjtn000);
                        jjtn000.operation = 1;
                        break;
                    case 80:
                        jj_consume_token(80);
                        t = jj_consume_token(69);
                        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                            case 72:
                                Arguments();
                                break;
                            default:
                                this.jj_la1[59] = this.jj_gen;
                                break;
                        }
                        this.jjtree.closeNodeScope(jjtn000, true);
                        jjtc000 = false;
                        jjtreeCloseNodeScope(jjtn000);
                        jjtn000.operation = 2;
                        jjtn000.field = t.image;
                        break;
                    case 74:
                        jj_consume_token(74);
                        Expression();
                        jj_consume_token(75);
                        this.jjtree.closeNodeScope(jjtn000, true);
                        jjtc000 = false;
                        jjtreeCloseNodeScope(jjtn000);
                        jjtn000.operation = 3;
                        break;
                    default:
                        this.jj_la1[60] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void Literal() throws ParseException {
        BSHLiteral jjtn000 = new BSHLiteral(21);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            Token x;
            boolean b;
            String literal;
            char ch;
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 60:
                    x = jj_consume_token(60);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    literal = x.image;
                    ch = literal.charAt(literal.length() - 1);
                    if (ch == 'l' || ch == 'L') {

                        literal = literal.substring(0, literal.length() - 1);

                        jjtn000.value = new Primitive((new Long(literal)).longValue());
                        break;
                    }
                    try {
                        jjtn000.value = new Primitive(Integer.decode(literal).intValue());
                    } catch (NumberFormatException e) {
                        throw createParseException("Error or number too big for integer type: " + literal);
                    }
                    break;

                case 64:
                    x = jj_consume_token(64);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    literal = x.image;
                    ch = literal.charAt(literal.length() - 1);
                    if (ch == 'f' || ch == 'F') {

                        literal = literal.substring(0, literal.length() - 1);
                        jjtn000.value = new Primitive((new Float(literal)).floatValue());

                        break;
                    }
                    if (ch == 'd' || ch == 'D') {
                        literal = literal.substring(0, literal.length() - 1);
                    }
                    jjtn000.value = new Primitive((new Double(literal)).doubleValue());
                    break;

                case 66:
                    x = jj_consume_token(66);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    try {
                        jjtn000.charSetup(x.image.substring(1, x.image.length() - 1));
                    } catch (Exception e) {
                        throw createParseException("Error parsing character: " + x.image);
                    }
                    break;
                case 67:
                    x = jj_consume_token(67);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    try {
                        jjtn000.stringSetup(x.image.substring(1, x.image.length() - 1));
                    } catch (Exception e) {
                        throw createParseException("Error parsing string: " + x.image);
                    }
                    break;
                case 26:
                case 55:
                    b = BooleanLiteral();
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.value = new Primitive(b);
                    break;
                case 41:
                    NullLiteral();
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.value = Primitive.NULL;
                    break;
                case 57:
                    VoidLiteral();
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.value = Primitive.VOID;
                    break;
                default:
                    this.jj_la1[61] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final boolean BooleanLiteral() throws ParseException {
        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
            case 55:
                jj_consume_token(55);
                return true;

            case 26:
                jj_consume_token(26);
                return false;
        }

        this.jj_la1[62] = this.jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }

    public final void NullLiteral() throws ParseException {
        jj_consume_token(41);
    }

    public final void VoidLiteral() throws ParseException {
        jj_consume_token(57);
    }

    public final void Arguments() throws ParseException {
        BSHArguments jjtn000 = new BSHArguments(22);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(72);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    ArgumentList();
                    break;
                default:
                    this.jj_la1[63] = this.jj_gen;
                    break;
            }
            jj_consume_token(73);
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ArgumentList() throws ParseException {
        Expression();

        while (true) {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 79:
                    break;

                default:
                    this.jj_la1[64] = this.jj_gen;
                    break;
            }
            jj_consume_token(79);
            Expression();
        }
    }

    public final void AllocationExpression() throws ParseException {
        BSHAllocationExpression jjtn000 = new BSHAllocationExpression(23);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            if (jj_2_18(2)) {
                jj_consume_token(40);
                PrimitiveType();
                ArrayDimensions();
            } else {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 40:
                        jj_consume_token(40);
                        AmbiguousName();
                        switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                            case 76:
                                ArrayDimensions();
                                break;
                            case 72:
                                Arguments();
                                if (jj_2_17(2)) {
                                    Block();
                                }
                                break;
                        }

                        this.jj_la1[65] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();

                    default:
                        this.jj_la1[66] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ArrayDimensions() throws ParseException {
        BSHArrayDimensions jjtn000 = new BSHArrayDimensions(24);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            if (jj_2_21(2)) {

                while (true) {
                    jj_consume_token(76);
                    Expression();
                    jj_consume_token(77);
                    jjtn000.addDefinedDimension();
                    if (jj_2_19(2)) {
                        continue;
                    }

                    break;
                }

                while (jj_2_20(2)) {

                    jj_consume_token(76);
                    jj_consume_token(77);
                    jjtn000.addUndefinedDimension();
                }
            } else {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {

                    case 76:
                        while (true) {
                            jj_consume_token(76);
                            jj_consume_token(77);
                            jjtn000.addUndefinedDimension();
                            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                                case 76:
                                    continue;
                            }
                            break;
                        }
                        this.jj_la1[67] = this.jj_gen;

                        ArrayInitializer();
                        break;
                    default:
                        this.jj_la1[68] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void Statement() throws ParseException {
        if (jj_2_22(2)) {
            LabeledStatement();
        } else {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 74:
                    Block();
                    return;
                case 78:
                    EmptyStatement();
                    return;
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    StatementExpression();
                    jj_consume_token(78);
                    return;
                case 50:
                    SwitchStatement();
                    return;
                case 32:
                    IfStatement();
                    return;
                case 59:
                    WhileStatement();
                    return;
                case 21:
                    DoStatement();
                    return;
            }
            this.jj_la1[69] = this.jj_gen;
            if (isRegularForStatement()) {
                ForStatement();
            } else {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 30:
                        EnhancedForStatement();
                        return;
                    case 12:
                        BreakStatement();
                        return;
                    case 19:
                        ContinueStatement();
                        return;
                    case 46:
                        ReturnStatement();
                        return;
                    case 51:
                        SynchronizedStatement();
                        return;
                    case 53:
                        ThrowStatement();
                        return;
                    case 56:
                        TryStatement();
                        return;
                }
                this.jj_la1[70] = this.jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            }
        }
    }

    public final void LabeledStatement() throws ParseException {
        jj_consume_token(69);
        jj_consume_token(89);
        Statement();
    }

    public final void Block() throws ParseException {
        BSHBlock jjtn000 = new BSHBlock(25);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(74);

            while (jj_2_23(1)) {

                BlockStatement();
            }
            jj_consume_token(75);
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void BlockStatement() throws ParseException {
        if (jj_2_24(2147483647)) {
            ClassDeclaration();
        } else if (jj_2_25(2147483647)) {
            MethodDeclaration();
        } else if (jj_2_26(2147483647)) {
            MethodDeclaration();
        } else if (jj_2_27(2147483647)) {
            TypedVariableDeclaration();
            jj_consume_token(78);
        } else if (jj_2_28(1)) {
            Statement();
        } else {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 34:
                case 48:
                    ImportDeclaration();
                    return;
                case 42:
                    PackageDeclaration();
                    return;
                case 68:
                    FormalComment();
                    return;
            }
            this.jj_la1[71] = this.jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void FormalComment() throws ParseException {
        BSHFormalComment jjtn000 = new BSHFormalComment(26);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            Token t = jj_consume_token(68);
            this.jjtree.closeNodeScope(jjtn000, this.retainComments);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.text = t.image;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, this.retainComments);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void EmptyStatement() throws ParseException {
        jj_consume_token(78);
    }

    public final void StatementExpression() throws ParseException {
        Expression();
    }

    public final void SwitchStatement() throws ParseException {
        BSHSwitchStatement jjtn000 = new BSHSwitchStatement(27);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(50);
            jj_consume_token(72);
            Expression();
            jj_consume_token(73);
            jj_consume_token(74);

            OUTER: while (true) {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 15:
                    case 20:
                        break;

                    default:
                        this.jj_la1[72] = this.jj_gen;
                        break OUTER; // ✅ Thoát vòng lặp đúng cách
                }

                SwitchLabel();

                while (jj_2_29(1)) {
                    BlockStatement();
                }
            }
            jj_consume_token(75); // ✅ reachable code
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void SwitchLabel() throws ParseException {
        BSHSwitchLabel jjtn000 = new BSHSwitchLabel(28);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 15:
                    jj_consume_token(15);
                    Expression();
                    jj_consume_token(89);
                    break;
                case 20:
                    jj_consume_token(20);
                    jj_consume_token(89);
                    this.jjtree.closeNodeScope(jjtn000, true);
                    jjtc000 = false;
                    jjtreeCloseNodeScope(jjtn000);
                    jjtn000.isDefault = true;
                    break;
                default:
                    this.jj_la1[73] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void IfStatement() throws ParseException {
        BSHIfStatement jjtn000 = new BSHIfStatement(29);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(32);
            jj_consume_token(72);
            Expression();
            jj_consume_token(73);
            Statement();
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 23:
                    jj_consume_token(23);
                    Statement();
                    break;
                default:
                    this.jj_la1[74] = this.jj_gen;
                    break;
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void WhileStatement() throws ParseException {
        BSHWhileStatement jjtn000 = new BSHWhileStatement(30);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(59);
            jj_consume_token(72);
            Expression();
            jj_consume_token(73);
            Statement();
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void DoStatement() throws ParseException {
        BSHWhileStatement jjtn000 = new BSHWhileStatement(30);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(21);
            Statement();
            jj_consume_token(59);
            jj_consume_token(72);
            Expression();
            jj_consume_token(73);
            jj_consume_token(78);
            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.isDoStatement = true;
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ForStatement() throws ParseException {
        BSHForStatement jjtn000 = new BSHForStatement(31);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        Token t = null;
        try {
            jj_consume_token(30);
            jj_consume_token(72);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 10:
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 27:
                case 29:
                case 36:
                case 38:
                case 39:
                case 40:
                case 41:
                case 43:
                case 44:
                case 45:
                case 47:
                case 48:
                case 49:
                case 51:
                case 52:
                case 55:
                case 57:
                case 58:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    ForInit();
                    jjtn000.hasForInit = true;
                    break;
                default:
                    this.jj_la1[75] = this.jj_gen;
                    break;
            }
            jj_consume_token(78);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    Expression();
                    jjtn000.hasExpression = true;
                    break;
                default:
                    this.jj_la1[76] = this.jj_gen;
                    break;
            }
            jj_consume_token(78);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    ForUpdate();
                    jjtn000.hasForUpdate = true;
                    break;
                default:
                    this.jj_la1[77] = this.jj_gen;
                    break;
            }
            jj_consume_token(73);
            Statement();
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void EnhancedForStatement() throws ParseException {
        BSHEnhancedForStatement jjtn000 = new BSHEnhancedForStatement(32);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        Token t = null;
        try {
            if (jj_2_30(4)) {
                jj_consume_token(30);
                jj_consume_token(72);
                t = jj_consume_token(69);
                jj_consume_token(89);
                Expression();
                jj_consume_token(73);
                Statement();
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtc000 = false;
                jjtreeCloseNodeScope(jjtn000);
                jjtn000.varName = t.image;
            } else {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 30:
                        jj_consume_token(30);
                        jj_consume_token(72);
                        Type();
                        t = jj_consume_token(69);
                        jj_consume_token(89);
                        Expression();
                        jj_consume_token(73);
                        Statement();
                        this.jjtree.closeNodeScope(jjtn000, true);
                        jjtc000 = false;
                        jjtreeCloseNodeScope(jjtn000);
                        jjtn000.varName = t.image;
                        break;
                    default:
                        this.jj_la1[78] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ForInit() throws ParseException {
        Token t = null;
        if (jj_2_31(2147483647)) {
            TypedVariableDeclaration();
        } else {
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    StatementExpressionList();
                    return;
            }
            this.jj_la1[79] = this.jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
    }

    public final void TypedVariableDeclaration() throws ParseException {
        BSHTypedVariableDeclaration jjtn000 = new BSHTypedVariableDeclaration(33);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);

        try {
            Modifiers mods = Modifiers(2, false);
            Type();
            VariableDeclarator();

            OUTER: while (true) {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 79: // Dấu phẩy ,
                        break;

                    default:
                        this.jj_la1[80] = this.jj_gen;
                        break OUTER; // ✅ Thoát khi không còn biến nữa
                }
                jj_consume_token(79);
                VariableDeclarator();
            }

            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.modifiers = mods;
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void StatementExpressionList() throws ParseException {
        BSHStatementExpressionList jjtn000 = new BSHStatementExpressionList(34);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            StatementExpression();

            while (true) {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 79:
                        break;

                    default:
                        this.jj_la1[81] = this.jj_gen;
                        break;
                }
                jj_consume_token(79);
                StatementExpression();
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ForUpdate() throws ParseException {
        StatementExpressionList();
    }

    public final void BreakStatement() throws ParseException {
        BSHReturnStatement jjtn000 = new BSHReturnStatement(35);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(12);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 69:
                    jj_consume_token(69);
                    break;
                default:
                    this.jj_la1[82] = this.jj_gen;
                    break;
            }
            jj_consume_token(78);
            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.kind = 12;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ContinueStatement() throws ParseException {
        BSHReturnStatement jjtn000 = new BSHReturnStatement(35);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(19);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 69:
                    jj_consume_token(69);
                    break;
                default:
                    this.jj_la1[83] = this.jj_gen;
                    break;
            }
            jj_consume_token(78);
            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.kind = 19;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ReturnStatement() throws ParseException {
        BSHReturnStatement jjtn000 = new BSHReturnStatement(35);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(46);
            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 11:
                case 14:
                case 17:
                case 22:
                case 26:
                case 29:
                case 36:
                case 38:
                case 40:
                case 41:
                case 47:
                case 55:
                case 57:
                case 60:
                case 64:
                case 66:
                case 67:
                case 69:
                case 72:
                case 86:
                case 87:
                case 100:
                case 101:
                case 102:
                case 103:
                    Expression();
                    break;
                default:
                    this.jj_la1[84] = this.jj_gen;
                    break;
            }
            jj_consume_token(78);
            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.kind = 46;
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void SynchronizedStatement() throws ParseException {
        BSHBlock jjtn000 = new BSHBlock(25);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(51);
            jj_consume_token(72);
            Expression();
            jj_consume_token(73);
            Block();
            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);
            jjtn000.isSynchronized = true;
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void ThrowStatement() throws ParseException {
        BSHThrowStatement jjtn000 = new BSHThrowStatement(36);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(53);
            Expression();
            jj_consume_token(78);
        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void TryStatement() throws ParseException {
        BSHTryStatement jjtn000 = new BSHTryStatement(37);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        boolean closed = false;
        try {
            jj_consume_token(56); // try
            Block();

            OUTER: while (true) {
                switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                    case 16: // catch
                        break;
                    default:
                        this.jj_la1[85] = this.jj_gen;
                        break OUTER; // ✅ Thoát khi không còn catch
                }

                jj_consume_token(16); // catch
                jj_consume_token(72); // (
                FormalParameter();
                jj_consume_token(73); // )
                Block();
                closed = true;
            }

            switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
                case 28: // finally
                    jj_consume_token(28);
                    Block();
                    closed = true;
                    break;
                default:
                    this.jj_la1[86] = this.jj_gen;
                    break;
            }

            this.jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtreeCloseNodeScope(jjtn000);

            if (!closed)
                throw generateParseException();

        } catch (Throwable jjte000) {
            if (jjtc000) {
                this.jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                this.jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                throw (RuntimeException) jjte000;
            }
            if (jjte000 instanceof ParseException) {
                throw (ParseException) jjte000;
            }
            throw (Error) jjte000;
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope(jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    private final boolean jj_2_1(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_1();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(0, xla);
        }

    }

    private final boolean jj_2_2(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_2();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(1, xla);
        }

    }

    private final boolean jj_2_3(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_3();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(2, xla);
        }

    }

    private final boolean jj_2_4(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_4();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(3, xla);
        }

    }

    private final boolean jj_2_5(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_5();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(4, xla);
        }

    }

    private final boolean jj_2_6(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_6();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(5, xla);
        }

    }

    private final boolean jj_2_7(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_7();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(6, xla);
        }

    }

    private final boolean jj_2_8(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_8();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(7, xla);
        }

    }

    private final boolean jj_2_9(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_9();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(8, xla);
        }

    }

    private final boolean jj_2_10(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_10();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(9, xla);
        }

    }

    private final boolean jj_2_11(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_11();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(10, xla);
        }

    }

    private final boolean jj_2_12(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_12();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(11, xla);
        }

    }

    private final boolean jj_2_13(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_13();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(12, xla);
        }

    }

    private final boolean jj_2_14(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_14();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(13, xla);
        }

    }

    private final boolean jj_2_15(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_15();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(14, xla);
        }

    }

    private final boolean jj_2_16(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_16();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(15, xla);
        }

    }

    private final boolean jj_2_17(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_17();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(16, xla);
        }

    }

    private final boolean jj_2_18(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_18();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(17, xla);
        }

    }

    private final boolean jj_2_19(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_19();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(18, xla);
        }

    }

    private final boolean jj_2_20(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_20();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(19, xla);
        }

    }

    private final boolean jj_2_21(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_21();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(20, xla);
        }

    }

    private final boolean jj_2_22(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_22();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(21, xla);
        }

    }

    private final boolean jj_2_23(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_23();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(22, xla);
        }

    }

    private final boolean jj_2_24(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_24();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(23, xla);
        }

    }

    private final boolean jj_2_25(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_25();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(24, xla);
        }

    }

    private final boolean jj_2_26(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_26();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(25, xla);
        }

    }

    private final boolean jj_2_27(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_27();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(26, xla);
        }

    }

    private final boolean jj_2_28(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_28();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(27, xla);
        }

    }

    private final boolean jj_2_29(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_29();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(28, xla);
        }

    }

    private final boolean jj_2_30(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_30();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(29, xla);
        }

    }

    private final boolean jj_2_31(int xla) {
        this.jj_la = xla;
        this.jj_lastpos = this.jj_scanpos = this.token;
        try {
            return !jj_3_31();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(30, xla);
        }

    }

    private final boolean jj_3R_47() {
        if (jj_3R_92())
            return true;
        return false;
    }

    private final boolean jj_3R_169() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(106)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(107))
                return true;
        }
        if (jj_3R_164())
            return true;
        return false;
    }

    private final boolean jj_3R_46() {
        if (jj_3R_91())
            return true;
        return false;
    }

    private final boolean jj_3R_28() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_46()) {
            this.jj_scanpos = xsp;
            if (jj_3R_47()) {
                this.jj_scanpos = xsp;
                if (jj_3R_48()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_49()) {
                        this.jj_scanpos = xsp;
                        if (jj_3_28()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_50()) {
                                this.jj_scanpos = xsp;
                                if (jj_3R_51()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3R_52())
                                        return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean jj_3_23() {
        if (jj_3R_28())
            return true;
        return false;
    }

    private final boolean jj_3R_161() {
        if (jj_3R_164())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_169()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_38() {
        if (jj_scan_token(74))
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3_23()) {
                this.jj_scanpos = xsp;

                if (jj_scan_token(75))
                    return true;
                return false;
            }

        }
    }

    private final boolean jj_3R_158() {
        if (jj_3R_161())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_167()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_40() {
        if (jj_scan_token(69))
            return true;
        if (jj_scan_token(89))
            return true;
        if (jj_3R_45())
            return true;
        return false;
    }

    private final boolean jj_3R_156() {
        if (jj_scan_token(88))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(89))
            return true;
        if (jj_3R_108())
            return true;
        return false;
    }

    private final boolean jj_3R_165() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(108)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(109))
                return true;
        }
        if (jj_3R_158())
            return true;
        return false;
    }

    private final boolean jj_3R_153() {
        if (jj_3R_158())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_165()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_90() {
        if (jj_3R_124())
            return true;
        return false;
    }

    private final boolean jj_3R_89() {
        if (jj_3R_123())
            return true;
        return false;
    }

    private final boolean jj_3R_88() {
        if (jj_3R_122())
            return true;
        return false;
    }

    private final boolean jj_3R_162() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(98)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(99))
                return true;
        }
        if (jj_3R_153())
            return true;
        return false;
    }

    private final boolean jj_3R_87() {
        if (jj_3R_121())
            return true;
        return false;
    }

    private final boolean jj_3R_148() {
        if (jj_3R_153())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_162()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_86() {
        if (jj_3R_120())
            return true;
        return false;
    }

    private final boolean jj_3R_85() {
        if (jj_3R_119())
            return true;
        return false;
    }

    private final boolean jj_3R_84() {
        if (jj_3R_118())
            return true;
        return false;
    }

    private final boolean jj_3R_159() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(96)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(97))
                return true;
        }
        if (jj_3R_148())
            return true;
        return false;
    }

    private final boolean jj_3R_83() {
        if (jj_3R_117())
            return true;
        return false;
    }

    private final boolean jj_3R_135() {
        if (jj_3R_148())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_159()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_82() {
        if (jj_3R_116())
            return true;
        return false;
    }

    private final boolean jj_3R_81() {
        if (jj_3R_115())
            return true;
        return false;
    }

    private final boolean jj_3R_80() {
        if (jj_3R_114())
            return true;
        return false;
    }

    private final boolean jj_3R_108() {
        if (jj_3R_135())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_156())
            this.jj_scanpos = xsp;
        return false;
    }

    private final boolean jj_3R_79() {
        if (jj_3R_113())
            return true;
        return false;
    }

    private final boolean jj_3R_78() {
        if (jj_3R_112())
            return true;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3_17() {
        if (jj_3R_38())
            return true;
        return false;
    }

    private final boolean jj_3R_77() {
        if (jj_3R_38())
            return true;
        return false;
    }

    private final boolean jj_3R_45() {
        Token xsp = this.jj_scanpos;
        if (jj_3_22()) {
            this.jj_scanpos = xsp;
            if (jj_3R_77()) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(78)) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_78()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_79()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_80()) {
                                this.jj_scanpos = xsp;
                                if (jj_3R_81()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3R_82()) {
                                        this.jj_scanpos = xsp;
                                        this.lookingAhead = true;
                                        this.jj_semLA = isRegularForStatement();
                                        this.lookingAhead = false;
                                        if (!this.jj_semLA || jj_3R_83()) {
                                            this.jj_scanpos = xsp;
                                            if (jj_3R_84()) {
                                                this.jj_scanpos = xsp;
                                                if (jj_3R_85()) {
                                                    this.jj_scanpos = xsp;
                                                    if (jj_3R_86()) {
                                                        this.jj_scanpos = xsp;
                                                        if (jj_3R_87()) {
                                                            this.jj_scanpos = xsp;
                                                            if (jj_3R_88()) {
                                                                this.jj_scanpos = xsp;
                                                                if (jj_3R_89()) {
                                                                    this.jj_scanpos = xsp;
                                                                    if (jj_3R_90())
                                                                        return true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean jj_3_22() {
        if (jj_3R_40())
            return true;
        return false;
    }

    private final boolean jj_3R_34() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(81)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(120)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(121)) {
                    this.jj_scanpos = xsp;
                    if (jj_scan_token(127)) {
                        this.jj_scanpos = xsp;
                        if (jj_scan_token(118)) {
                            this.jj_scanpos = xsp;
                            if (jj_scan_token(119)) {
                                this.jj_scanpos = xsp;
                                if (jj_scan_token(122)) {
                                    this.jj_scanpos = xsp;
                                    if (jj_scan_token(126)) {
                                        this.jj_scanpos = xsp;
                                        if (jj_scan_token(124)) {
                                            this.jj_scanpos = xsp;
                                            if (jj_scan_token(128)) {
                                                this.jj_scanpos = xsp;
                                                if (jj_scan_token(129)) {
                                                    this.jj_scanpos = xsp;
                                                    if (jj_scan_token(130)) {
                                                        this.jj_scanpos = xsp;
                                                        if (jj_scan_token(131)) {
                                                            this.jj_scanpos = xsp;
                                                            if (jj_scan_token(132)) {
                                                                this.jj_scanpos = xsp;
                                                                if (jj_scan_token(133))
                                                                    return true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean jj_3R_111() {
        if (jj_scan_token(79))
            return true;
        if (jj_3R_29())
            return true;
        return false;
    }

    private final boolean jj_3R_160() {
        if (jj_scan_token(76))
            return true;
        if (jj_scan_token(77))
            return true;
        return false;
    }

    private final boolean jj_3R_152() {
        if (jj_3R_69())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3_17())
            this.jj_scanpos = xsp;
        return false;
    }

    private final boolean jj_3R_157() {
        if (jj_3R_160())
            return true;
        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_160()) {
                this.jj_scanpos = xsp;

                if (jj_3R_97())
                    return true;
                return false;
            }

        }
    }

    private final boolean jj_3_8() {
        if (jj_3R_33())
            return true;
        if (jj_3R_34())
            return true;
        return false;
    }

    private final boolean jj_3_20() {
        if (jj_scan_token(76))
            return true;
        if (jj_scan_token(77))
            return true;
        return false;
    }

    private final boolean jj_3R_151() {
        if (jj_3R_150())
            return true;
        return false;
    }

    private final boolean jj_3_19() {
        if (jj_scan_token(76))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(77))
            return true;
        return false;
    }

    private final boolean jj_3R_107() {
        if (jj_3R_33())
            return true;
        if (jj_3R_34())
            return true;
        if (jj_3R_39())
            return true;
        return false;
    }

    private final boolean jj_3_21() {
        if (jj_3_19())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3_19()) {
                this.jj_scanpos = xsp;

                while (true) {
                    xsp = this.jj_scanpos;
                    if (jj_3_20()) {
                        this.jj_scanpos = xsp;
                        return false;
                    }

                    if (jj_3_19()) {
                        break;
                    }
                }

                break;
            } else {
                break;
            }
        }

        return false;
    }

    private final boolean jj_3R_150() {
        Token xsp = this.jj_scanpos;
        if (jj_3_21()) {
            this.jj_scanpos = xsp;
            if (jj_3R_157())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_71() {
        if (jj_3R_108())
            return true;
        return false;
    }

    private final boolean jj_3R_39() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_70()) {
            this.jj_scanpos = xsp;
            if (jj_3R_71())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_70() {
        if (jj_3R_107())
            return true;
        return false;
    }

    private final boolean jj_3R_145() {
        if (jj_scan_token(40))
            return true;
        if (jj_3R_29())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_151()) {
            this.jj_scanpos = xsp;
            if (jj_3R_152())
                return true;
        }
        return false;
    }

    private final boolean jj_3_18() {
        if (jj_scan_token(40))
            return true;
        if (jj_3R_36())
            return true;
        if (jj_3R_150())
            return true;
        return false;
    }

    private final boolean jj_3R_130() {
        Token xsp = this.jj_scanpos;
        if (jj_3_18()) {
            this.jj_scanpos = xsp;
            if (jj_3R_145())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_147() {
        if (jj_scan_token(79))
            return true;
        if (jj_3R_39())
            return true;
        return false;
    }

    private final boolean jj_3R_76() {
        if (jj_3R_29())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_111()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_134() {
        if (jj_3R_39())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_147()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_106() {
        if (jj_3R_134())
            return true;
        return false;
    }

    private final boolean jj_3_7() {
        if (jj_scan_token(80))
            return true;
        if (jj_scan_token(69))
            return true;
        return false;
    }

    private final boolean jj_3R_69() {
        if (jj_scan_token(72))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_106())
            this.jj_scanpos = xsp;
        if (jj_scan_token(73))
            return true;
        return false;
    }

    private final boolean jj_3R_29() {
        if (jj_scan_token(69))
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3_7()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_68() {
        if (jj_scan_token(22))
            return true;
        return false;
    }

    private final boolean jj_3R_67() {
        if (jj_scan_token(29))
            return true;
        return false;
    }

    private final boolean jj_3R_155() {
        if (jj_scan_token(26))
            return true;
        return false;
    }

    private final boolean jj_3R_66() {
        if (jj_scan_token(38))
            return true;
        return false;
    }

    private final boolean jj_3R_65() {
        if (jj_scan_token(36))
            return true;
        return false;
    }

    private final boolean jj_3R_154() {
        if (jj_scan_token(55))
            return true;
        return false;
    }

    private final boolean jj_3R_149() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_154()) {
            this.jj_scanpos = xsp;
            if (jj_3R_155())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_64() {
        if (jj_scan_token(47))
            return true;
        return false;
    }

    private final boolean jj_3R_56() {
        if (jj_3R_29())
            return true;
        return false;
    }

    private final boolean jj_3R_63() {
        if (jj_scan_token(14))
            return true;
        return false;
    }

    private final boolean jj_3R_62() {
        if (jj_scan_token(17))
            return true;
        return false;
    }

    private final boolean jj_3R_61() {
        if (jj_scan_token(11))
            return true;
        return false;
    }

    private final boolean jj_3R_36() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_61()) {
            this.jj_scanpos = xsp;
            if (jj_3R_62()) {
                this.jj_scanpos = xsp;
                if (jj_3R_63()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_64()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_65()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_66()) {
                                this.jj_scanpos = xsp;
                                if (jj_3R_67()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3R_68())
                                        return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean jj_3R_144() {
        if (jj_scan_token(57))
            return true;
        return false;
    }

    private final boolean jj_3R_74() {
        if (jj_3R_32())
            return true;
        return false;
    }

    private final boolean jj_3R_42() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_73()) {
            this.jj_scanpos = xsp;
            if (jj_3R_74())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_73() {
        if (jj_scan_token(57))
            return true;
        return false;
    }

    private final boolean jj_3R_143() {
        if (jj_scan_token(41))
            return true;
        return false;
    }

    private final boolean jj_3_6() {
        if (jj_scan_token(76))
            return true;
        if (jj_scan_token(77))
            return true;
        return false;
    }

    private final boolean jj_3R_142() {
        if (jj_3R_149())
            return true;
        return false;
    }

    private final boolean jj_3R_55() {
        if (jj_3R_36())
            return true;
        return false;
    }

    private final boolean jj_3R_110() {
        if (jj_scan_token(79))
            return true;
        if (jj_3R_109())
            return true;
        return false;
    }

    private final boolean jj_3R_141() {
        if (jj_scan_token(67))
            return true;
        return false;
    }

    private final boolean jj_3R_32() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_55()) {
            this.jj_scanpos = xsp;
            if (jj_3R_56())
                return true;
        }
        while (true) {
            xsp = this.jj_scanpos;
            if (jj_3_6()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_140() {
        if (jj_scan_token(66))
            return true;
        return false;
    }

    private final boolean jj_3R_190() {
        if (jj_scan_token(28))
            return true;
        if (jj_3R_38())
            return true;
        return false;
    }

    private final boolean jj_3_4() {
        if (jj_scan_token(79))
            return true;
        if (jj_3R_31())
            return true;
        return false;
    }

    private final boolean jj_3R_189() {
        if (jj_scan_token(16))
            return true;
        if (jj_scan_token(72))
            return true;
        if (jj_3R_109())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_38())
            return true;
        return false;
    }

    private final boolean jj_3R_136() {
        if (jj_scan_token(69))
            return true;
        return false;
    }

    private final boolean jj_3_5() {
        if (jj_3R_32())
            return true;
        if (jj_scan_token(69))
            return true;
        return false;
    }

    private final boolean jj_3R_75() {
        if (jj_3R_109())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_110()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_109() {
        Token xsp = this.jj_scanpos;
        if (jj_3_5()) {
            this.jj_scanpos = xsp;
            if (jj_3R_136())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_124() {
        if (jj_scan_token(56))
            return true;
        if (jj_3R_38())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_189()) {
                this.jj_scanpos = xsp;

                xsp = this.jj_scanpos;
                if (jj_3R_190())
                    this.jj_scanpos = xsp;
                return false;
            }

        }
    }

    private final boolean jj_3R_43() {
        if (jj_scan_token(72))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_75())
            this.jj_scanpos = xsp;
        if (jj_scan_token(73))
            return true;
        return false;
    }

    private final boolean jj_3R_163() {
        if (jj_3R_31())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3_4()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_139() {
        if (jj_scan_token(64))
            return true;
        return false;
    }

    private final boolean jj_3R_97() {
        if (jj_scan_token(74))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_163())
            this.jj_scanpos = xsp;
        xsp = this.jj_scanpos;
        if (jj_scan_token(79))
            this.jj_scanpos = xsp;
        if (jj_scan_token(75))
            return true;
        return false;
    }

    private final boolean jj_3R_30() {
        if (jj_scan_token(80))
            return true;
        if (jj_scan_token(104))
            return true;
        return false;
    }

    private final boolean jj_3R_123() {
        if (jj_scan_token(53))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3R_180() {
        if (jj_scan_token(81))
            return true;
        if (jj_3R_31())
            return true;
        return false;
    }

    private final boolean jj_3R_54() {
        if (jj_3R_39())
            return true;
        return false;
    }

    private final boolean jj_3R_188() {
        if (jj_3R_39())
            return true;
        return false;
    }

    private final boolean jj_3R_53() {
        if (jj_3R_97())
            return true;
        return false;
    }

    private final boolean jj_3R_31() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_53()) {
            this.jj_scanpos = xsp;
            if (jj_3R_54())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_122() {
        if (jj_scan_token(51))
            return true;
        if (jj_scan_token(72))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_38())
            return true;
        return false;
    }

    private final boolean jj_3R_177() {
        if (jj_scan_token(79))
            return true;
        if (jj_3R_176())
            return true;
        return false;
    }

    private final boolean jj_3R_210() {
        if (jj_scan_token(79))
            return true;
        if (jj_3R_112())
            return true;
        return false;
    }

    private final boolean jj_3R_121() {
        if (jj_scan_token(46))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_188())
            this.jj_scanpos = xsp;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3R_129() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_138()) {
            this.jj_scanpos = xsp;
            if (jj_3R_139()) {
                this.jj_scanpos = xsp;
                if (jj_3R_140()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_141()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_142()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_143()) {
                                this.jj_scanpos = xsp;
                                if (jj_3R_144())
                                    return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean jj_3R_138() {
        if (jj_scan_token(60))
            return true;
        return false;
    }

    private final boolean jj_3R_146() {
        if (jj_3R_69())
            return true;
        return false;
    }

    private final boolean jj_3R_176() {
        if (jj_scan_token(69))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_180())
            this.jj_scanpos = xsp;
        return false;
    }

    private final boolean jj_3R_105() {
        if (jj_3R_129())
            return true;
        return false;
    }

    private final boolean jj_3R_120() {
        if (jj_scan_token(19))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_scan_token(69))
            this.jj_scanpos = xsp;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3R_119() {
        if (jj_scan_token(12))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_scan_token(69))
            this.jj_scanpos = xsp;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3R_195() {
        if (jj_3R_205())
            return true;
        return false;
    }

    private final boolean jj_3R_128() {
        if (jj_scan_token(34))
            return true;
        if (jj_scan_token(104))
            return true;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3R_133() {
        if (jj_scan_token(74))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(75))
            return true;
        return false;
    }

    private final boolean jj_3R_205() {
        if (jj_3R_112())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_210()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_132() {
        if (jj_scan_token(80))
            return true;
        if (jj_scan_token(69))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_146())
            this.jj_scanpos = xsp;
        return false;
    }

    private final boolean jj_3_3() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(48))
            this.jj_scanpos = xsp;
        if (jj_scan_token(34))
            return true;
        if (jj_3R_29())
            return true;
        xsp = this.jj_scanpos;
        if (jj_3R_30())
            this.jj_scanpos = xsp;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3R_94() {
        Token xsp = this.jj_scanpos;
        if (jj_3_3()) {
            this.jj_scanpos = xsp;
            if (jj_3R_128())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_93() {
        if (jj_3R_41())
            return true;
        if (jj_3R_32())
            return true;
        if (jj_3R_176())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_177()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_131() {
        if (jj_scan_token(76))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(77))
            return true;
        return false;
    }

    private final boolean jj_3R_95() {
        if (jj_scan_token(42))
            return true;
        if (jj_3R_29())
            return true;
        return false;
    }

    private final boolean jj_3_2() {
        if (jj_scan_token(69))
            return true;
        if (jj_scan_token(72))
            return true;
        return false;
    }

    private final boolean jj_3R_175() {
        if (jj_3R_38())
            return true;
        return false;
    }

    private final boolean jj_3_16() {
        if (jj_scan_token(80))
            return true;
        if (jj_scan_token(13))
            return true;
        return false;
    }

    private final boolean jj_3R_104() {
        Token xsp = this.jj_scanpos;
        if (jj_3_16()) {
            this.jj_scanpos = xsp;
            if (jj_3R_131()) {
                this.jj_scanpos = xsp;
                if (jj_3R_132()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_133())
                        return true;
                }
            }
        }
        return false;
    }

    private final boolean jj_3R_174() {
        if (jj_scan_token(54))
            return true;
        if (jj_3R_76())
            return true;
        return false;
    }

    private final boolean jj_3_15() {
        if (jj_3R_32())
            return true;
        if (jj_scan_token(80))
            return true;
        if (jj_scan_token(13))
            return true;
        return false;
    }

    private final boolean jj_3_31() {
        if (jj_3R_41())
            return true;
        if (jj_3R_32())
            return true;
        if (jj_scan_token(69))
            return true;
        return false;
    }

    private final boolean jj_3_14() {
        if (jj_3R_37())
            return true;
        return false;
    }

    private final boolean jj_3R_126() {
        if (jj_scan_token(69))
            return true;
        return false;
    }

    private final boolean jj_3R_127() {
        if (jj_3R_42())
            return true;
        if (jj_scan_token(69))
            return true;
        return false;
    }

    private final boolean jj_3R_92() {
        if (jj_3R_41())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_126()) {
            this.jj_scanpos = xsp;
            if (jj_3R_127())
                return true;
        }
        if (jj_3R_43())
            return true;
        xsp = this.jj_scanpos;
        if (jj_3R_174())
            this.jj_scanpos = xsp;
        xsp = this.jj_scanpos;
        if (jj_3R_175()) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(78))
                return true;
        }
        return false;
    }

    private final boolean jj_3R_204() {
        if (jj_3R_205())
            return true;
        return false;
    }

    private final boolean jj_3R_103() {
        if (jj_3R_29())
            return true;
        return false;
    }

    private final boolean jj_3R_203() {
        if (jj_3R_93())
            return true;
        return false;
    }

    private final boolean jj_3R_194() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_203()) {
            this.jj_scanpos = xsp;
            if (jj_3R_204())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_102() {
        if (jj_3R_32())
            return true;
        return false;
    }

    private final boolean jj_3R_58() {
        if (jj_3R_104())
            return true;
        return false;
    }

    private final boolean jj_3R_125() {
        if (jj_scan_token(37))
            return true;
        return false;
    }

    private final boolean jj_3R_101() {
        if (jj_3R_37())
            return true;
        return false;
    }

    private final boolean jj_3R_100() {
        if (jj_3R_130())
            return true;
        return false;
    }

    private final boolean jj_3R_99() {
        if (jj_scan_token(72))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(73))
            return true;
        return false;
    }

    private final boolean jj_3R_137() {
        if (jj_scan_token(30))
            return true;
        if (jj_scan_token(72))
            return true;
        if (jj_3R_32())
            return true;
        if (jj_scan_token(69))
            return true;
        if (jj_scan_token(89))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_45())
            return true;
        return false;
    }

    private final boolean jj_3R_184() {
        if (jj_scan_token(23))
            return true;
        if (jj_3R_45())
            return true;
        return false;
    }

    private final boolean jj_3R_173() {
        if (jj_scan_token(33))
            return true;
        if (jj_3R_76())
            return true;
        return false;
    }

    private final boolean jj_3R_57() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_98()) {
            this.jj_scanpos = xsp;
            if (jj_3R_99()) {
                this.jj_scanpos = xsp;
                if (jj_3R_100()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_101()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_102()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_103())
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean jj_3R_98() {
        if (jj_3R_129())
            return true;
        return false;
    }

    private final boolean jj_3R_172() {
        if (jj_scan_token(25))
            return true;
        if (jj_3R_29())
            return true;
        return false;
    }

    private final boolean jj_3_30() {
        if (jj_scan_token(30))
            return true;
        if (jj_scan_token(72))
            return true;
        if (jj_scan_token(69))
            return true;
        if (jj_scan_token(89))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_45())
            return true;
        return false;
    }

    private final boolean jj_3R_118() {
        Token xsp = this.jj_scanpos;
        if (jj_3_30()) {
            this.jj_scanpos = xsp;
            if (jj_3R_137())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_37() {
        if (jj_3R_29())
            return true;
        if (jj_3R_69())
            return true;
        return false;
    }

    private final boolean jj_3R_185() {
        if (jj_3R_194())
            return true;
        return false;
    }

    private final boolean jj_3R_91() {
        if (jj_3R_41())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_scan_token(13)) {
            this.jj_scanpos = xsp;
            if (jj_3R_125())
                return true;
        }
        if (jj_scan_token(69))
            return true;
        xsp = this.jj_scanpos;
        if (jj_3R_172())
            this.jj_scanpos = xsp;
        xsp = this.jj_scanpos;
        if (jj_3R_173())
            this.jj_scanpos = xsp;
        if (jj_3R_38())
            return true;
        return false;
    }

    private final boolean jj_3_13() {
        if (jj_scan_token(72))
            return true;
        if (jj_3R_36())
            return true;
        return false;
    }

    private final boolean jj_3R_187() {
        if (jj_3R_195())
            return true;
        return false;
    }

    private final boolean jj_3R_186() {
        if (jj_3R_39())
            return true;
        return false;
    }

    private final boolean jj_3R_33() {
        if (jj_3R_57())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_58()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_217() {
        if (jj_scan_token(72))
            return true;
        if (jj_3R_32())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_208())
            return true;
        return false;
    }

    private final boolean jj_3R_216() {
        if (jj_scan_token(72))
            return true;
        if (jj_3R_32())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_191())
            return true;
        return false;
    }

    private final boolean jj_3R_117() {
        if (jj_scan_token(30))
            return true;
        if (jj_scan_token(72))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_185())
            this.jj_scanpos = xsp;
        if (jj_scan_token(78))
            return true;
        xsp = this.jj_scanpos;
        if (jj_3R_186())
            this.jj_scanpos = xsp;
        if (jj_scan_token(78))
            return true;
        xsp = this.jj_scanpos;
        if (jj_3R_187())
            this.jj_scanpos = xsp;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_45())
            return true;
        return false;
    }

    private final boolean jj_3R_214() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_216()) {
            this.jj_scanpos = xsp;
            if (jj_3R_217())
                return true;
        }
        return false;
    }

    private final boolean jj_3_12() {
        if (jj_3R_33())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_scan_token(100)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(101))
                return true;
        }
        return false;
    }

    private final boolean jj_3R_219() {
        if (jj_3R_33())
            return true;
        return false;
    }

    private final boolean jj_3R_116() {
        if (jj_scan_token(21))
            return true;
        if (jj_3R_45())
            return true;
        if (jj_scan_token(59))
            return true;
        if (jj_scan_token(72))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3_11() {
        if (jj_scan_token(72))
            return true;
        if (jj_3R_29())
            return true;
        if (jj_scan_token(76))
            return true;
        return false;
    }

    private final boolean jj_3R_218() {
        if (jj_3R_33())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_scan_token(100)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(101))
                return true;
        }
        return false;
    }

    private final boolean jj_3R_215() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_218()) {
            this.jj_scanpos = xsp;
            if (jj_3R_219())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_72() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(43)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(44)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(45)) {
                    this.jj_scanpos = xsp;
                    if (jj_scan_token(51)) {
                        this.jj_scanpos = xsp;
                        if (jj_scan_token(27)) {
                            this.jj_scanpos = xsp;
                            if (jj_scan_token(39)) {
                                this.jj_scanpos = xsp;
                                if (jj_scan_token(52)) {
                                    this.jj_scanpos = xsp;
                                    if (jj_scan_token(58)) {
                                        this.jj_scanpos = xsp;
                                        if (jj_scan_token(10)) {
                                            this.jj_scanpos = xsp;
                                            if (jj_scan_token(48)) {
                                                this.jj_scanpos = xsp;
                                                if (jj_scan_token(49))
                                                    return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean jj_3R_115() {
        if (jj_scan_token(59))
            return true;
        if (jj_scan_token(72))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_45())
            return true;
        return false;
    }

    private final boolean jj_3R_60() {
        if (jj_scan_token(72))
            return true;
        if (jj_3R_29())
            return true;
        if (jj_scan_token(73))
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_scan_token(87)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(86)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(72)) {
                    this.jj_scanpos = xsp;
                    if (jj_scan_token(69)) {
                        this.jj_scanpos = xsp;
                        if (jj_scan_token(40)) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_105())
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean jj_3R_59() {
        if (jj_scan_token(72))
            return true;
        if (jj_3R_29())
            return true;
        if (jj_scan_token(76))
            return true;
        if (jj_scan_token(77))
            return true;
        return false;
    }

    private final boolean jj_3_9() {
        if (jj_3R_35())
            return true;
        return false;
    }

    private final boolean jj_3_29() {
        if (jj_3R_28())
            return true;
        return false;
    }

    private final boolean jj_3R_114() {
        if (jj_scan_token(32))
            return true;
        if (jj_scan_token(72))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_3R_45())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_184())
            this.jj_scanpos = xsp;
        return false;
    }

    private final boolean jj_3R_41() {
        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_72()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_35() {
        Token xsp = this.jj_scanpos;
        if (jj_3_10()) {
            this.jj_scanpos = xsp;
            if (jj_3R_59()) {
                this.jj_scanpos = xsp;
                if (jj_3R_60())
                    return true;
            }
        }
        return false;
    }

    private final boolean jj_3_10() {
        if (jj_scan_token(72))
            return true;
        if (jj_3R_36())
            return true;
        return false;
    }

    private final boolean jj_3R_213() {
        if (jj_3R_215())
            return true;
        return false;
    }

    private final boolean jj_3R_212() {
        if (jj_3R_214())
            return true;
        return false;
    }

    private final boolean jj_3R_202() {
        if (jj_scan_token(20))
            return true;
        if (jj_scan_token(89))
            return true;
        return false;
    }

    private final boolean jj_3R_211() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(87)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(86))
                return true;
        }
        if (jj_3R_191())
            return true;
        return false;
    }

    private final boolean jj_3R_208() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_211()) {
            this.jj_scanpos = xsp;
            if (jj_3R_212()) {
                this.jj_scanpos = xsp;
                if (jj_3R_213())
                    return true;
            }
        }
        return false;
    }

    private final boolean jj_3R_201() {
        if (jj_scan_token(15))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(89))
            return true;
        return false;
    }

    private final boolean jj_3R_193() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_201()) {
            this.jj_scanpos = xsp;
            if (jj_3R_202())
                return true;
        }
        return false;
    }

    private final boolean jj_3R_183() {
        if (jj_3R_193())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3_29()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_207() {
        if (jj_scan_token(101))
            return true;
        if (jj_3R_33())
            return true;
        return false;
    }

    private final boolean jj_3_1() {
        if (jj_3R_28())
            return true;
        return false;
    }

    private final boolean jj_3R_113() {
        if (jj_scan_token(50))
            return true;
        if (jj_scan_token(72))
            return true;
        if (jj_3R_39())
            return true;
        if (jj_scan_token(73))
            return true;
        if (jj_scan_token(74))
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_183()) {
                this.jj_scanpos = xsp;

                if (jj_scan_token(75))
                    return true;
                return false;
            }

        }
    }

    private final boolean jj_3R_209() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(104)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(105)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(111))
                    return true;
            }
        }
        if (jj_3R_191())
            return true;
        return false;
    }

    private final boolean jj_3R_206() {
        if (jj_scan_token(100))
            return true;
        if (jj_3R_33())
            return true;
        return false;
    }

    private final boolean jj_3R_199() {
        if (jj_3R_208())
            return true;
        return false;
    }

    private final boolean jj_3R_198() {
        if (jj_3R_207())
            return true;
        return false;
    }

    private final boolean jj_3R_197() {
        if (jj_3R_206())
            return true;
        return false;
    }

    private final boolean jj_3R_196() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(102)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(103))
                return true;
        }
        if (jj_3R_191())
            return true;
        return false;
    }

    private final boolean jj_3R_191() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_196()) {
            this.jj_scanpos = xsp;
            if (jj_3R_197()) {
                this.jj_scanpos = xsp;
                if (jj_3R_198()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_199())
                        return true;
                }
            }
        }
        return false;
    }

    private final boolean jj_3R_44() {
        if (jj_scan_token(54))
            return true;
        if (jj_3R_76())
            return true;
        return false;
    }

    private final boolean jj_3R_112() {
        if (jj_3R_39())
            return true;
        return false;
    }

    private final boolean jj_3R_181() {
        if (jj_3R_191())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_209()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_200() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(102)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(103))
                return true;
        }
        if (jj_3R_181())
            return true;
        return false;
    }

    private final boolean jj_3R_178() {
        if (jj_3R_181())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_200()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_96() {
        if (jj_scan_token(68))
            return true;
        return false;
    }

    private final boolean jj_3R_192() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(112)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(113)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(114)) {
                    this.jj_scanpos = xsp;
                    if (jj_scan_token(115)) {
                        this.jj_scanpos = xsp;
                        if (jj_scan_token(116)) {
                            this.jj_scanpos = xsp;
                            if (jj_scan_token(117))
                                return true;
                        }
                    }
                }
            }
        }
        if (jj_3R_178())
            return true;
        return false;
    }

    private final boolean jj_3R_171() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(90)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(95))
                return true;
        }
        if (jj_3R_166())
            return true;
        return false;
    }

    private final boolean jj_3R_170() {
        if (jj_3R_178())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_192()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_52() {
        if (jj_3R_96())
            return true;
        return false;
    }

    private final boolean jj_3R_182() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(84)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(85)) {
                this.jj_scanpos = xsp;
                if (jj_scan_token(82)) {
                    this.jj_scanpos = xsp;
                    if (jj_scan_token(83)) {
                        this.jj_scanpos = xsp;
                        if (jj_scan_token(91)) {
                            this.jj_scanpos = xsp;
                            if (jj_scan_token(92)) {
                                this.jj_scanpos = xsp;
                                if (jj_scan_token(93)) {
                                    this.jj_scanpos = xsp;
                                    if (jj_scan_token(94))
                                        return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (jj_3R_170())
            return true;
        return false;
    }

    private final boolean jj_3_27() {
        if (jj_3R_41())
            return true;
        if (jj_3R_32())
            return true;
        if (jj_scan_token(69))
            return true;
        return false;
    }

    private final boolean jj_3R_51() {
        if (jj_3R_95())
            return true;
        return false;
    }

    private final boolean jj_3R_168() {
        if (jj_3R_170())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_182()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    private final boolean jj_3R_50() {
        if (jj_3R_94())
            return true;
        return false;
    }

    private final boolean jj_3_26() {
        if (jj_3R_41())
            return true;
        if (jj_scan_token(69))
            return true;
        if (jj_3R_43())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_44())
            this.jj_scanpos = xsp;
        if (jj_scan_token(74))
            return true;
        return false;
    }

    private final boolean jj_3R_179() {
        if (jj_scan_token(35))
            return true;
        if (jj_3R_32())
            return true;
        return false;
    }

    private final boolean jj_3_28() {
        if (jj_3R_45())
            return true;
        return false;
    }

    private final boolean jj_3R_166() {
        if (jj_3R_168())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_3R_179())
            this.jj_scanpos = xsp;
        return false;
    }

    private final boolean jj_3_25() {
        if (jj_3R_41())
            return true;
        if (jj_3R_42())
            return true;
        if (jj_scan_token(69))
            return true;
        if (jj_scan_token(72))
            return true;
        return false;
    }

    private final boolean jj_3R_49() {
        if (jj_3R_93())
            return true;
        if (jj_scan_token(78))
            return true;
        return false;
    }

    private final boolean jj_3_24() {
        if (jj_3R_41())
            return true;

        Token xsp = this.jj_scanpos;
        if (jj_scan_token(13)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(37))
                return true;
        }
        return false;
    }

    private final boolean jj_3R_167() {
        if (jj_scan_token(110))
            return true;
        if (jj_3R_161())
            return true;
        return false;
    }

    private final boolean jj_3R_48() {
        if (jj_3R_92())
            return true;
        return false;
    }

    private final boolean jj_3R_164() {
        if (jj_3R_166())
            return true;

        while (true) {
            Token xsp = this.jj_scanpos;
            if (jj_3R_171()) {
                this.jj_scanpos = xsp;

                return false;
            }

        }
    }

    public void ReInit(InputStream stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        this.jj_gen = 0;
        int i;
        for (i = 0; i < 87;) {
            this.jj_la1[i] = -1;
            i++;
        }
        for (i = 0; i < this.jj_2_rtns.length;) {
            this.jj_2_rtns[i] = new JJCalls();
            i++;
        }

    }

    public void ReInit(Reader stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        this.jj_gen = 0;
        int i;
        for (i = 0; i < 87;) {
            this.jj_la1[i] = -1;
            i++;
        }
        for (i = 0; i < this.jj_2_rtns.length;) {
            this.jj_2_rtns[i] = new JJCalls();
            i++;
        }

    }

    public void ReInit(ParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        this.jj_gen = 0;
        int i;
        for (i = 0; i < 87;) {
            this.jj_la1[i] = -1;
            i++;
        }
        for (i = 0; i < this.jj_2_rtns.length;) {
            this.jj_2_rtns[i] = new JJCalls();
            i++;
        }

    }

    private final Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = this.token).next != null) {
            this.token = this.token.next;
        } else {
            this.token = this.token.next = this.token_source.getNextToken();
        }
        this.jj_ntk = -1;
        if (this.token.kind == kind) {
            this.jj_gen++;
            if (++this.jj_gc > 100) {
                this.jj_gc = 0;
                for (int i = 0; i < this.jj_2_rtns.length; i++) {
                    JJCalls c = this.jj_2_rtns[i];
                    while (c != null) {
                        if (c.gen < this.jj_gen)
                            c.first = null;
                        c = c.next;
                    }
                }
            }
            return this.token;
        }
        this.token = oldToken;
        this.jj_kind = kind;
        throw generateParseException();
    }

    private final boolean jj_scan_token(int kind) {
        if (this.jj_scanpos == this.jj_lastpos) {
            this.jj_la--;
            if (this.jj_scanpos.next == null) {
                this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken();
            } else {
                this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next;
            }
        } else {
            this.jj_scanpos = this.jj_scanpos.next;
        }
        if (this.jj_rescan) {
            int i = 0;
            Token tok = this.token;
            while (tok != null && tok != this.jj_scanpos) {
                i++;
                tok = tok.next;
            }
            if (tok != null)
                jj_add_error_token(kind, i);
        }
        if (this.jj_scanpos.kind != kind)
            return true;
        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos)
            throw this.jj_ls;
        return false;
    }

    public final Token getNextToken() {
        if (this.token.next != null) {
            this.token = this.token.next;
        } else {
            this.token = this.token.next = this.token_source.getNextToken();
        }
        this.jj_ntk = -1;
        this.jj_gen++;
        return this.token;
    }

    private void jj_add_error_token(int kind, int pos) {
        if (pos >= 100)
            return;
        if (pos == this.jj_endpos + 1) {
            this.jj_lasttokens[this.jj_endpos++] = kind;
        } else if (this.jj_endpos != 0) {
            this.jj_expentry = new int[this.jj_endpos];
            for (int i = 0; i < this.jj_endpos; i++) {
                this.jj_expentry[i] = this.jj_lasttokens[i];
            }
            boolean exists = false;
            for (Enumeration<int[]> e = this.jj_expentries.elements(); e.hasMoreElements();) {
                int[] oldentry = e.nextElement();
                if (oldentry.length == this.jj_expentry.length) {
                    exists = true;
                    for (int j = 0; j < this.jj_expentry.length; j++) {
                        if (oldentry[j] != this.jj_expentry[j]) {
                            exists = false;
                            break;
                        }
                    }
                    if (exists)
                        break;
                }
            }
            if (!exists)
                this.jj_expentries.addElement(this.jj_expentry);
            if (pos != 0)
                this.jj_lasttokens[(this.jj_endpos = pos) - 1] = kind;
        }
    }

    public final Token getToken(int index) {
        Token t = this.lookingAhead ? this.jj_scanpos : this.token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) {
                t = t.next;
            } else {
                t = t.next = this.token_source.getNextToken();
            }
        }
        return t;
    }

    private final int jj_ntk() {
        if ((this.jj_nt = this.token.next) == null)
            return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;
        return this.jj_ntk = this.jj_nt.kind;
    }

    public ParseException generateParseException() {
        this.jj_expentries.removeAllElements();
        boolean[] la1tokens = new boolean[134];
        int i;
        for (i = 0; i < 134; i++) {
            la1tokens[i] = false;
        }
        if (this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for (i = 0; i < 87; i++) {
            if (this.jj_la1[i] == this.jj_gen) {
                for (int k = 0; k < 32; k++) {
                    if ((jj_la1_0[i] & 1 << k) != 0) {
                        la1tokens[k] = true;
                    }
                    if ((jj_la1_1[i] & 1 << k) != 0) {
                        la1tokens[32 + k] = true;
                    }
                    if ((jj_la1_2[i] & 1 << k) != 0) {
                        la1tokens[64 + k] = true;
                    }
                    if ((jj_la1_3[i] & 1 << k) != 0) {
                        la1tokens[96 + k] = true;
                    }
                    if ((jj_la1_4[i] & 1 << k) != 0) {
                        la1tokens[128 + k] = true;
                    }
                }
            }
        }
        for (i = 0; i < 134; i++) {
            if (la1tokens[i]) {
                this.jj_expentry = new int[1];
                this.jj_expentry[0] = i;
                this.jj_expentries.addElement(this.jj_expentry);
            }
        }
        this.jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        int[][] exptokseq = new int[this.jj_expentries.size()][];
        for (int j = 0; j < this.jj_expentries.size(); j++) {
            exptokseq[j] = (int[]) this.jj_expentries.elementAt(j);
        }
        return new ParseException(this.token, exptokseq, tokenImage);
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }

    private final void jj_rescan_token() {
        this.jj_rescan = true;
        for (int i = 0; i < 31;) {
            JJCalls p = this.jj_2_rtns[i];
            while (true) {
                if (p.gen > this.jj_gen) {
                    this.jj_la = p.arg;
                    this.jj_lastpos = this.jj_scanpos = p.first;
                    switch (i) {
                        case 0:
                            jj_3_1();
                            break;
                        case 1:
                            jj_3_2();
                            break;
                        case 2:
                            jj_3_3();
                            break;
                        case 3:
                            jj_3_4();
                            break;
                        case 4:
                            jj_3_5();
                            break;
                        case 5:
                            jj_3_6();
                            break;
                        case 6:
                            jj_3_7();
                            break;
                        case 7:
                            jj_3_8();
                            break;
                        case 8:
                            jj_3_9();
                            break;
                        case 9:
                            jj_3_10();
                            break;
                        case 10:
                            jj_3_11();
                            break;
                        case 11:
                            jj_3_12();
                            break;
                        case 12:
                            jj_3_13();
                            break;
                        case 13:
                            jj_3_14();
                            break;
                        case 14:
                            jj_3_15();
                            break;
                        case 15:
                            jj_3_16();
                            break;
                        case 16:
                            jj_3_17();
                            break;
                        case 17:
                            jj_3_18();
                            break;
                        case 18:
                            jj_3_19();
                            break;
                        case 19:
                            jj_3_20();
                            break;
                        case 20:
                            jj_3_21();
                            break;
                        case 21:
                            jj_3_22();
                            break;
                        case 22:
                            jj_3_23();
                            break;
                        case 23:
                            jj_3_24();
                            break;
                        case 24:
                            jj_3_25();
                            break;
                        case 25:
                            jj_3_26();
                            break;
                        case 26:
                            jj_3_27();
                            break;
                        case 27:
                            jj_3_28();
                            break;
                        case 28:
                            jj_3_29();
                            break;
                        case 29:
                            jj_3_30();
                            break;
                        case 30:
                            jj_3_31();
                            break;
                    }

                }
                p = p.next;
                if (p == null)
                    i++;
            }
        }
        this.jj_rescan = false;
    }

    private final void jj_save(int index, int xla) {
        JJCalls p = this.jj_2_rtns[index];
        while (p.gen > this.jj_gen) {
            if (p.next == null) {
                p = p.next = new JJCalls();
                break;
            }
            p = p.next;
        }
        p.gen = this.jj_gen + xla - this.jj_la;
        p.first = this.token;
        p.arg = xla;
    }

    private static final class LookaheadSuccess extends Error {
        private LookaheadSuccess() {
        }
    }

    static final class JJCalls {
        int gen;
        Token first;
        int arg;
        JJCalls next;
    }
}
