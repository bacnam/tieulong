package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.*;

public class CharsetMapping {
    public static final String[] INDEX_TO_CHARSET;
    public static final String[] INDEX_TO_COLLATION;
    public static final int MAP_SIZE = 255;
    public static final Map<Integer, String> STATIC_INDEX_TO_MYSQL_CHARSET_MAP;
    public static final Map<String, Integer> STATIC_CHARSET_TO_NUM_BYTES_MAP;
    public static final Map<String, Integer> STATIC_4_0_CHARSET_TO_NUM_BYTES_MAP;
    public static final Map<String, String> MYSQL_TO_JAVA_CHARSET_MAP;
    private static final Properties CHARSET_CONFIG = new Properties();
    private static final Map<String, List<VersionedStringProperty>> JAVA_TO_MYSQL_CHARSET_MAP;
    private static final Map<String, List<VersionedStringProperty>> JAVA_UC_TO_MYSQL_CHARSET_MAP;
    private static final Map<String, String> ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET_MAP;
    private static final Map<String, String> MULTIBYTE_CHARSETS;
    private static final Map<String, Integer> MYSQL_ENCODING_NAME_TO_CHARSET_INDEX_MAP;

    private static final String MYSQL_CHARSET_NAME_armscii8 = "armscii8";

    private static final String MYSQL_CHARSET_NAME_ascii = "ascii";

    private static final String MYSQL_CHARSET_NAME_big5 = "big5";

    private static final String MYSQL_CHARSET_NAME_binary = "binary";

    private static final String MYSQL_CHARSET_NAME_cp1250 = "cp1250";

    private static final String MYSQL_CHARSET_NAME_cp1251 = "cp1251";

    private static final String MYSQL_CHARSET_NAME_cp1256 = "cp1256";

    private static final String MYSQL_CHARSET_NAME_cp1257 = "cp1257";

    private static final String MYSQL_CHARSET_NAME_cp850 = "cp850";

    private static final String MYSQL_CHARSET_NAME_cp852 = "cp852";

    private static final String MYSQL_CHARSET_NAME_cp866 = "cp866";

    private static final String MYSQL_CHARSET_NAME_cp932 = "cp932";

    private static final String MYSQL_CHARSET_NAME_dec8 = "dec8";

    private static final String MYSQL_CHARSET_NAME_eucjpms = "eucjpms";

    private static final String MYSQL_CHARSET_NAME_euckr = "euckr";

    private static final String MYSQL_CHARSET_NAME_gb2312 = "gb2312";

    private static final String MYSQL_CHARSET_NAME_gbk = "gbk";

    private static final String MYSQL_CHARSET_NAME_geostd8 = "geostd8";

    private static final String MYSQL_CHARSET_NAME_greek = "greek";
    private static final String MYSQL_CHARSET_NAME_hebrew = "hebrew";
    private static final String MYSQL_CHARSET_NAME_hp8 = "hp8";
    private static final String MYSQL_CHARSET_NAME_keybcs2 = "keybcs2";
    private static final String MYSQL_CHARSET_NAME_koi8r = "koi8r";
    private static final String MYSQL_CHARSET_NAME_koi8u = "koi8u";
    private static final String MYSQL_CHARSET_NAME_latin1 = "latin1";
    private static final String MYSQL_CHARSET_NAME_latin2 = "latin2";
    private static final String MYSQL_CHARSET_NAME_latin5 = "latin5";
    private static final String MYSQL_CHARSET_NAME_latin7 = "latin7";
    private static final String MYSQL_CHARSET_NAME_macce = "macce";
    private static final String MYSQL_CHARSET_NAME_macroman = "macroman";
    private static final String MYSQL_CHARSET_NAME_sjis = "sjis";
    private static final String MYSQL_CHARSET_NAME_swe7 = "swe7";
    private static final String MYSQL_CHARSET_NAME_tis620 = "tis620";
    private static final String MYSQL_CHARSET_NAME_ucs2 = "ucs2";
    private static final String MYSQL_CHARSET_NAME_ujis = "ujis";
    private static final String MYSQL_CHARSET_NAME_utf16 = "utf16";
    private static final String MYSQL_CHARSET_NAME_utf16le = "utf16le";
    private static final String MYSQL_CHARSET_NAME_utf32 = "utf32";
    private static final String MYSQL_CHARSET_NAME_utf8 = "utf8";
    private static final String MYSQL_CHARSET_NAME_utf8mb4 = "utf8mb4";
    private static final String MYSQL_4_0_CHARSET_NAME_croat = "croat";
    private static final String MYSQL_4_0_CHARSET_NAME_czech = "czech";
    private static final String MYSQL_4_0_CHARSET_NAME_danish = "danish";
    private static final String MYSQL_4_0_CHARSET_NAME_dos = "dos";
    private static final String MYSQL_4_0_CHARSET_NAME_estonia = "estonia";
    private static final String MYSQL_4_0_CHARSET_NAME_euc_kr = "euc_kr";
    private static final String MYSQL_4_0_CHARSET_NAME_german1 = "german1";
    private static final String MYSQL_4_0_CHARSET_NAME_hungarian = "hungarian";
    private static final String MYSQL_4_0_CHARSET_NAME_koi8_ru = "koi8_ru";
    private static final String MYSQL_4_0_CHARSET_NAME_koi8_ukr = "koi8_ukr";
    private static final String MYSQL_4_0_CHARSET_NAME_latin1_de = "latin1_de";
    private static final String MYSQL_4_0_CHARSET_NAME_usa7 = "usa7";
    private static final String MYSQL_4_0_CHARSET_NAME_win1250 = "win1250";
    private static final String MYSQL_4_0_CHARSET_NAME_win1251 = "win1251";
    private static final String MYSQL_4_0_CHARSET_NAME_win1251ukr = "win1251ukr";
    private static final String NOT_USED = "ISO8859_1";

    static {
        HashMap<String, Integer> tempNumBytesMap = new HashMap<String, Integer>();
        tempNumBytesMap.put("armscii8", Integer.valueOf(1));
        tempNumBytesMap.put("ascii", Integer.valueOf(1));
        tempNumBytesMap.put("big5", Integer.valueOf(2));
        tempNumBytesMap.put("binary", Integer.valueOf(1));
        tempNumBytesMap.put("cp1250", Integer.valueOf(1));
        tempNumBytesMap.put("cp1251", Integer.valueOf(1));
        tempNumBytesMap.put("cp1256", Integer.valueOf(1));
        tempNumBytesMap.put("cp1257", Integer.valueOf(1));
        tempNumBytesMap.put("cp850", Integer.valueOf(1));
        tempNumBytesMap.put("cp852", Integer.valueOf(1));
        tempNumBytesMap.put("cp866", Integer.valueOf(1));
        tempNumBytesMap.put("cp932", Integer.valueOf(2));
        tempNumBytesMap.put("dec8", Integer.valueOf(1));
        tempNumBytesMap.put("eucjpms", Integer.valueOf(3));
        tempNumBytesMap.put("euckr", Integer.valueOf(2));
        tempNumBytesMap.put("gb2312", Integer.valueOf(2));
        tempNumBytesMap.put("gbk", Integer.valueOf(2));
        tempNumBytesMap.put("geostd8", Integer.valueOf(1));
        tempNumBytesMap.put("greek", Integer.valueOf(1));
        tempNumBytesMap.put("hebrew", Integer.valueOf(1));
        tempNumBytesMap.put("hp8", Integer.valueOf(1));
        tempNumBytesMap.put("keybcs2", Integer.valueOf(1));
        tempNumBytesMap.put("koi8r", Integer.valueOf(1));
        tempNumBytesMap.put("koi8u", Integer.valueOf(1));
        tempNumBytesMap.put("latin1", Integer.valueOf(1));
        tempNumBytesMap.put("latin2", Integer.valueOf(1));
        tempNumBytesMap.put("latin5", Integer.valueOf(1));
        tempNumBytesMap.put("latin7", Integer.valueOf(1));
        tempNumBytesMap.put("macce", Integer.valueOf(1));
        tempNumBytesMap.put("macroman", Integer.valueOf(1));
        tempNumBytesMap.put("sjis", Integer.valueOf(2));
        tempNumBytesMap.put("swe7", Integer.valueOf(1));
        tempNumBytesMap.put("tis620", Integer.valueOf(1));
        tempNumBytesMap.put("ucs2", Integer.valueOf(2));
        tempNumBytesMap.put("ujis", Integer.valueOf(3));
        tempNumBytesMap.put("utf16", Integer.valueOf(4));
        tempNumBytesMap.put("utf16le", Integer.valueOf(4));
        tempNumBytesMap.put("utf32", Integer.valueOf(4));
        tempNumBytesMap.put("utf8", Integer.valueOf(3));
        tempNumBytesMap.put("utf8mb4", Integer.valueOf(4));
        STATIC_CHARSET_TO_NUM_BYTES_MAP = Collections.unmodifiableMap(tempNumBytesMap);

        tempNumBytesMap = new HashMap<String, Integer>();
        tempNumBytesMap.put("croat", Integer.valueOf(1));
        tempNumBytesMap.put("czech", Integer.valueOf(1));
        tempNumBytesMap.put("danish", Integer.valueOf(1));
        tempNumBytesMap.put("dos", Integer.valueOf(1));
        tempNumBytesMap.put("estonia", Integer.valueOf(1));
        tempNumBytesMap.put("euc_kr", Integer.valueOf(2));
        tempNumBytesMap.put("german1", Integer.valueOf(1));
        tempNumBytesMap.put("hungarian", Integer.valueOf(1));
        tempNumBytesMap.put("koi8_ru", Integer.valueOf(1));
        tempNumBytesMap.put("koi8_ukr", Integer.valueOf(1));
        tempNumBytesMap.put("latin1_de", Integer.valueOf(1));
        tempNumBytesMap.put("usa7", Integer.valueOf(1));
        tempNumBytesMap.put("win1250", Integer.valueOf(1));
        tempNumBytesMap.put("win1251", Integer.valueOf(1));
        tempNumBytesMap.put("win1251ukr", Integer.valueOf(1));
        STATIC_4_0_CHARSET_TO_NUM_BYTES_MAP = Collections.unmodifiableMap(tempNumBytesMap);

        CHARSET_CONFIG.setProperty("javaToMysqlMappings", "US-ASCII =\t\t\tusa7,US-ASCII =\t\t\t>4.1.0 ascii,Big5 = \t\t\t\tbig5,GBK = \t\t\t\tgbk,SJIS = \t\t\t\tsjis,EUC_CN = \t\t\tgb2312,EUC_JP = \t\t\tujis,EUC_JP_Solaris = \t>5.0.3 eucjpms,EUC_KR = \t\t\teuc_kr,EUC_KR = \t\t\t>4.1.0 euckr,ISO8859_1 =\t\t\t*latin1,ISO8859_1 =\t\t\tlatin1_de,ISO8859_1 =\t\t\tgerman1,ISO8859_1 =\t\t\tdanish,ISO8859_2 =\t\t\tlatin2,ISO8859_2 =\t\t\tczech,ISO8859_2 =\t\t\thungarian,ISO8859_2  =\t\tcroat,ISO8859_7  =\t\tgreek,ISO8859_7  =\t\tlatin7,ISO8859_8  = \t\thebrew,ISO8859_9  =\t\tlatin5,ISO8859_13 =\t\tlatvian,ISO8859_13 =\t\tlatvian1,ISO8859_13 =\t\testonia,Cp437 =             *>4.1.0 cp850,Cp437 =\t\t\t\tdos,Cp850 =\t\t\t\tcp850,Cp852 = \t\t\tcp852,Cp866 = \t\t\tcp866,KOI8_R = \t\t\tkoi8_ru,KOI8_R = \t\t\t>4.1.0 koi8r,TIS620 = \t\t\ttis620,Cp1250 = \t\t\tcp1250,Cp1250 = \t\t\twin1250,Cp1251 = \t\t\t*>4.1.0 cp1251,Cp1251 = \t\t\twin1251,Cp1251 = \t\t\tcp1251cias,Cp1251 = \t\t\tcp1251csas,Cp1256 = \t\t\tcp1256,Cp1251 = \t\t\twin1251ukr,Cp1252 =             latin1,Cp1257 = \t\t\tcp1257,MacRoman = \t\t\tmacroman,MacCentralEurope = \tmacce,UTF-8 = \t\tutf8,UTF-8 =\t\t\t\t*> 5.5.2 utf8mb4,UnicodeBig = \tucs2,US-ASCII =\t\tbinary,Cp943 =        \tsjis,MS932 =\t\t\tsjis,MS932 =        \t>4.1.11 cp932,WINDOWS-31J =\tsjis,WINDOWS-31J = \t>4.1.11 cp932,CP932 =\t\t\tsjis,CP932 =\t\t\t*>4.1.11 cp932,SHIFT_JIS = \tsjis,ASCII =\t\t\tascii,LATIN5 =\t\tlatin5,LATIN7 =\t\tlatin7,HEBREW =\t\thebrew,GREEK =\t\t\tgreek,EUCKR =\t\t\teuckr,GB2312 =\t\tgb2312,LATIN2 =\t\tlatin2,UTF-16 = \t>5.2.0 utf16,UTF-16LE = \t>5.6.0 utf16le,UTF-32 = \t>5.2.0 utf32");

        HashMap<String, List<VersionedStringProperty>> javaToMysqlMap = new HashMap<String, List<VersionedStringProperty>>();

        populateMapWithKeyValuePairsVersioned("javaToMysqlMappings", javaToMysqlMap, false);
        JAVA_TO_MYSQL_CHARSET_MAP = Collections.unmodifiableMap(javaToMysqlMap);

        HashMap<String, String> mysqlToJavaMap = new HashMap<String, String>();

        Set<String> keySet = JAVA_TO_MYSQL_CHARSET_MAP.keySet();
        Iterator<String> javaCharsets = keySet.iterator();
        while (javaCharsets.hasNext()) {
            String javaEncodingName = javaCharsets.next();
            List<VersionedStringProperty> mysqlEncodingList = JAVA_TO_MYSQL_CHARSET_MAP.get(javaEncodingName);

            Iterator<VersionedStringProperty> mysqlEncodings = mysqlEncodingList.iterator();

            String mysqlEncodingName = null;

            while (mysqlEncodings.hasNext()) {
                VersionedStringProperty mysqlProp = mysqlEncodings.next();
                mysqlEncodingName = mysqlProp.toString();

                mysqlToJavaMap.put(mysqlEncodingName, javaEncodingName);
                mysqlToJavaMap.put(mysqlEncodingName.toUpperCase(Locale.ENGLISH), javaEncodingName);
            }
        }

        mysqlToJavaMap.put("cp932", "Windows-31J");
        mysqlToJavaMap.put("CP932", "Windows-31J");

        MYSQL_TO_JAVA_CHARSET_MAP = Collections.unmodifiableMap(mysqlToJavaMap);

        TreeMap<String, List<VersionedStringProperty>> ucMap = new TreeMap<String, List<VersionedStringProperty>>(String.CASE_INSENSITIVE_ORDER);
        Iterator<String> javaNamesKeys = JAVA_TO_MYSQL_CHARSET_MAP.keySet().iterator();
        while (javaNamesKeys.hasNext()) {
            String key = javaNamesKeys.next();
            ucMap.put(key.toUpperCase(Locale.ENGLISH), JAVA_TO_MYSQL_CHARSET_MAP.get(key));
        }
        JAVA_UC_TO_MYSQL_CHARSET_MAP = Collections.unmodifiableMap(ucMap);

        HashMap<String, String> tempMapMulti = new HashMap<String, String>();

        CHARSET_CONFIG.setProperty("multibyteCharsets", "Big5 = \t\t\tbig5,GBK = \t\t\tgbk,SJIS = \t\t\tsjis,EUC_CN = \t\tgb2312,EUC_JP = \t\tujis,EUC_JP_Solaris = eucjpms,EUC_KR = \t\teuc_kr,EUC_KR = \t\t>4.1.0 euckr,Cp943 =        \tsjis,Cp943 = \t\tcp943,WINDOWS-31J =\tsjis,WINDOWS-31J = \tcp932,CP932 =\t\t\tcp932,MS932 =\t\t\tsjis,MS932 =        \tcp932,SHIFT_JIS = \tsjis,EUCKR =\t\t\teuckr,GB2312 =\t\tgb2312,UTF-8 = \t\tutf8,utf8 =          utf8,UnicodeBig = \tucs2,UTF-16 = \t>5.2.0 utf16,UTF-16LE = \t>5.6.0 utf16le,UTF-32 = \t>5.2.0 utf32");

        populateMapWithKeyValuePairsUnversioned("multibyteCharsets", tempMapMulti, true);

        MULTIBYTE_CHARSETS = Collections.unmodifiableMap(tempMapMulti);

        Collation[] collation = new Collation[255];
        collation[1] = new Collation(1, "big5_chinese_ci", "big5");
        collation[2] = new Collation(2, "latin2_czech_cs", "latin2");
        collation[3] = new Collation(3, "dec8_swedish_ci", "dec8", "ISO8859_1");
        collation[4] = new Collation(4, "cp850_general_ci", "cp850", "ISO8859_1");
        collation[5] = new Collation(5, "latin1_german1_ci", "latin1");
        collation[6] = new Collation(6, "hp8_english_ci", "hp8", "ISO8859_1");
        collation[7] = new Collation(7, "koi8r_general_ci", "koi8r");
        collation[8] = new Collation(8, "latin1_swedish_ci", "latin1");
        collation[9] = new Collation(9, "latin2_general_ci", "latin2");
        collation[10] = new Collation(10, "swe7_swedish_ci", "swe7", "ISO8859_1");
        collation[11] = new Collation(11, "ascii_general_ci", "ascii");
        collation[12] = new Collation(12, "ujis_japanese_ci", "ujis");
        collation[13] = new Collation(13, "sjis_japanese_ci", "sjis");
        collation[14] = new Collation(14, "cp1251_bulgarian_ci", "cp1251");
        collation[15] = new Collation(15, "latin1_danish_ci", "latin1");
        collation[16] = new Collation(16, "hebrew_general_ci", "hebrew");
        collation[17] = new Collation(17, "latin1_german1_ci", "win1251");
        collation[18] = new Collation(18, "tis620_thai_ci", "tis620");
        collation[19] = new Collation(19, "euckr_korean_ci", "euckr");
        collation[20] = new Collation(20, "latin7_estonian_cs", "latin7", "ISO8859_13");
        collation[21] = new Collation(21, "latin2_hungarian_ci", "latin2");
        collation[22] = new Collation(22, "koi8u_general_ci", "koi8u", "KOI8_R");
        collation[23] = new Collation(23, "cp1251_ukrainian_ci", "cp1251");
        collation[24] = new Collation(24, "gb2312_chinese_ci", "gb2312");
        collation[25] = new Collation(25, "greek_general_ci", "greek");
        collation[26] = new Collation(26, "cp1250_general_ci", "cp1250");
        collation[27] = new Collation(27, "latin2_croatian_ci", "latin2");
        collation[28] = new Collation(28, "gbk_chinese_ci", "gbk");
        collation[29] = new Collation(29, "cp1257_lithuanian_ci", "cp1257");
        collation[30] = new Collation(30, "latin5_turkish_ci", "latin5");
        collation[31] = new Collation(31, "latin1_german2_ci", "latin1");
        collation[32] = new Collation(32, "armscii8_general_ci", "armscii8", "ISO8859_1");
        collation[33] = new Collation(33, "utf8_general_ci", "utf8");
        collation[34] = new Collation(34, "cp1250_czech_cs", "cp1250");
        collation[35] = new Collation(35, "ucs2_general_ci", "ucs2");
        collation[36] = new Collation(36, "cp866_general_ci", "cp866");
        collation[37] = new Collation(37, "keybcs2_general_ci", "keybcs2", "Cp895");
        collation[38] = new Collation(38, "macce_general_ci", "macce");
        collation[39] = new Collation(39, "macroman_general_ci", "macroman");
        collation[40] = new Collation(40, "cp852_general_ci", "cp852", "LATIN2");
        collation[41] = new Collation(41, "latin7_general_ci", "latin7", "ISO8859_13");
        collation[42] = new Collation(42, "latin7_general_cs", "latin7", "ISO8859_13");
        collation[43] = new Collation(43, "macce_bin", "macce");
        collation[44] = new Collation(44, "cp1250_croatian_ci", "cp1250");
        collation[45] = new Collation(45, "utf8mb4_general_ci", "utf8mb4");
        collation[46] = new Collation(46, "utf8mb4_bin", "utf8mb4");
        collation[47] = new Collation(47, "latin1_bin", "latin1");
        collation[48] = new Collation(48, "latin1_general_ci", "latin1");
        collation[49] = new Collation(49, "latin1_general_cs", "latin1");
        collation[50] = new Collation(50, "cp1251_bin", "cp1251");
        collation[51] = new Collation(51, "cp1251_general_ci", "cp1251");
        collation[52] = new Collation(52, "cp1251_general_cs", "cp1251");
        collation[53] = new Collation(53, "macroman_bin", "macroman");
        collation[54] = new Collation(54, "utf16_general_ci", "utf16");
        collation[55] = new Collation(55, "utf16_bin", "utf16");
        collation[56] = new Collation(56, "utf16le_general_ci", "utf16le");
        collation[57] = new Collation(57, "cp1256_general_ci", "cp1256");
        collation[58] = new Collation(58, "cp1257_bin", "cp1257");
        collation[59] = new Collation(59, "cp1257_general_ci", "cp1257");
        collation[60] = new Collation(60, "utf32_general_ci", "utf32");
        collation[61] = new Collation(61, "utf32_bin", "utf32");
        collation[62] = new Collation(62, "utf16le_bin", "utf16le");
        collation[63] = new Collation(63, "binary", "binary");
        collation[64] = new Collation(64, "armscii8_bin", "armscii8", "ISO8859_2");
        collation[65] = new Collation(65, "ascii_bin", "ascii");
        collation[66] = new Collation(66, "cp1250_bin", "cp1250");
        collation[67] = new Collation(67, "cp1256_bin", "cp1256");
        collation[68] = new Collation(68, "cp866_bin", "cp866");
        collation[69] = new Collation(69, "dec8_bin", "dec8", "US-ASCII");
        collation[70] = new Collation(70, "greek_bin", "greek");
        collation[71] = new Collation(71, "hebrew_bin", "hebrew");
        collation[72] = new Collation(72, "hp8_bin", "hp8", "US-ASCII");
        collation[73] = new Collation(73, "keybcs2_bin", "keybcs2", "Cp895");
        collation[74] = new Collation(74, "koi8r_bin", "koi8r");
        collation[75] = new Collation(75, "koi8u_bin", "koi8u", "KOI8_R");
        collation[76] = new Collation(76, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[77] = new Collation(77, "latin2_bin", "latin2");
        collation[78] = new Collation(78, "latin5_bin", "latin5");
        collation[79] = new Collation(79, "latin7_bin", "latin7");
        collation[80] = new Collation(80, "cp850_bin", "cp850");
        collation[81] = new Collation(81, "cp852_bin", "cp852");
        collation[82] = new Collation(82, "swe7_bin", "swe7", "ISO8859_1");
        collation[83] = new Collation(83, "utf8_bin", "utf8");
        collation[84] = new Collation(84, "big5_bin", "big5");
        collation[85] = new Collation(85, "euckr_bin", "euckr");
        collation[86] = new Collation(86, "gb2312_bin", "gb2312");
        collation[87] = new Collation(87, "gbk_bin", "gbk");
        collation[88] = new Collation(88, "sjis_bin", "sjis");
        collation[89] = new Collation(89, "tis620_bin", "tis620");
        collation[90] = new Collation(90, "ucs2_bin", "ucs2");
        collation[91] = new Collation(91, "ujis_bin", "ujis");
        collation[92] = new Collation(92, "geostd8_general_ci", "geostd8", "US-ASCII");
        collation[93] = new Collation(93, "geostd8_bin", "geostd8", "US-ASCII");
        collation[94] = new Collation(94, "latin1_spanish_ci", "latin1");
        collation[95] = new Collation(95, "cp932_japanese_ci", "cp932");
        collation[96] = new Collation(96, "cp932_bin", "cp932");
        collation[97] = new Collation(97, "eucjpms_japanese_ci", "eucjpms");
        collation[98] = new Collation(98, "eucjpms_bin", "eucjpms");
        collation[99] = new Collation(99, "cp1250_polish_ci", "cp1250");
        collation[100] = new Collation(100, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[101] = new Collation(101, "utf16_unicode_ci", "utf16");
        collation[102] = new Collation(102, "utf16_icelandic_ci", "utf16");
        collation[103] = new Collation(103, "utf16_latvian_ci", "utf16");
        collation[104] = new Collation(104, "utf16_romanian_ci", "utf16");
        collation[105] = new Collation(105, "utf16_slovenian_ci", "utf16");
        collation[106] = new Collation(106, "utf16_polish_ci", "utf16");
        collation[107] = new Collation(107, "utf16_estonian_ci", "utf16");
        collation[108] = new Collation(108, "utf16_spanish_ci", "utf16");
        collation[109] = new Collation(109, "utf16_swedish_ci", "utf16");
        collation[110] = new Collation(110, "utf16_turkish_ci", "utf16");
        collation[111] = new Collation(111, "utf16_czech_ci", "utf16");
        collation[112] = new Collation(112, "utf16_danish_ci", "utf16");
        collation[113] = new Collation(113, "utf16_lithuanian_ci", "utf16");
        collation[114] = new Collation(114, "utf16_slovak_ci", "utf16");
        collation[115] = new Collation(115, "utf16_spanish2_ci", "utf16");
        collation[116] = new Collation(116, "utf16_roman_ci", "utf16");
        collation[117] = new Collation(117, "utf16_persian_ci", "utf16");
        collation[118] = new Collation(118, "utf16_esperanto_ci", "utf16");
        collation[119] = new Collation(119, "utf16_hungarian_ci", "utf16");
        collation[120] = new Collation(120, "utf16_sinhala_ci", "utf16");
        collation[121] = new Collation(121, "utf16_german2_ci", "utf16");
        collation[122] = new Collation(122, "utf16_croatian_ci", "utf16");
        collation[123] = new Collation(123, "utf16_unicode_520_ci", "utf16");
        collation[124] = new Collation(124, "utf16_vietnamese_ci", "utf16");
        collation[125] = new Collation(125, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[126] = new Collation(126, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[127] = new Collation(127, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[128] = new Collation(128, "ucs2_unicode_ci", "ucs2");
        collation[129] = new Collation(129, "ucs2_icelandic_ci", "ucs2");
        collation[130] = new Collation(130, "ucs2_latvian_ci", "ucs2");
        collation[131] = new Collation(131, "ucs2_romanian_ci", "ucs2");
        collation[132] = new Collation(132, "ucs2_slovenian_ci", "ucs2");
        collation[133] = new Collation(133, "ucs2_polish_ci", "ucs2");
        collation[134] = new Collation(134, "ucs2_estonian_ci", "ucs2");
        collation[135] = new Collation(135, "ucs2_spanish_ci", "ucs2");
        collation[136] = new Collation(136, "ucs2_swedish_ci", "ucs2");
        collation[137] = new Collation(137, "ucs2_turkish_ci", "ucs2");
        collation[138] = new Collation(138, "ucs2_czech_ci", "ucs2");
        collation[139] = new Collation(139, "ucs2_danish_ci", "ucs2");
        collation[140] = new Collation(140, "ucs2_lithuanian_ci", "ucs2");
        collation[141] = new Collation(141, "ucs2_slovak_ci", "ucs2");
        collation[142] = new Collation(142, "ucs2_spanish2_ci", "ucs2");
        collation[143] = new Collation(143, "ucs2_roman_ci", "ucs2");
        collation[144] = new Collation(144, "ucs2_persian_ci", "ucs2");
        collation[145] = new Collation(145, "ucs2_esperanto_ci", "ucs2");
        collation[146] = new Collation(146, "ucs2_hungarian_ci", "ucs2");
        collation[147] = new Collation(147, "ucs2_sinhala_ci", "ucs2");
        collation[148] = new Collation(148, "ucs2_german2_ci", "ucs2");
        collation[149] = new Collation(149, "ucs2_croatian_ci", "ucs2");
        collation[150] = new Collation(150, "ucs2_unicode_520_ci", "ucs2");
        collation[151] = new Collation(151, "ucs2_vietnamese_ci", "ucs2");
        collation[152] = new Collation(152, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[153] = new Collation(153, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[154] = new Collation(154, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[155] = new Collation(155, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[156] = new Collation(156, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[157] = new Collation(157, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[158] = new Collation(158, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[159] = new Collation(159, "ucs2_general_mysql500_ci", "ucs2");
        collation[160] = new Collation(160, "utf32_unicode_ci", "utf32");
        collation[161] = new Collation(161, "utf32_icelandic_ci", "utf32");
        collation[162] = new Collation(162, "utf32_latvian_ci", "utf32");
        collation[163] = new Collation(163, "utf32_romanian_ci", "utf32");
        collation[164] = new Collation(164, "utf32_slovenian_ci", "utf32");
        collation[165] = new Collation(165, "utf32_polish_ci", "utf32");
        collation[166] = new Collation(166, "utf32_estonian_ci", "utf32");
        collation[167] = new Collation(167, "utf32_spanish_ci", "utf32");
        collation[168] = new Collation(168, "utf32_swedish_ci", "utf32");
        collation[169] = new Collation(169, "utf32_turkish_ci", "utf32");
        collation[170] = new Collation(170, "utf32_czech_ci", "utf32");
        collation[171] = new Collation(171, "utf32_danish_ci", "utf32");
        collation[172] = new Collation(172, "utf32_lithuanian_ci", "utf32");
        collation[173] = new Collation(173, "utf32_slovak_ci", "utf32");
        collation[174] = new Collation(174, "utf32_spanish2_ci", "utf32");
        collation[175] = new Collation(175, "utf32_roman_ci", "utf32");
        collation[176] = new Collation(176, "utf32_persian_ci", "utf32");
        collation[177] = new Collation(177, "utf32_esperanto_ci", "utf32");
        collation[178] = new Collation(178, "utf32_hungarian_ci", "utf32");
        collation[179] = new Collation(179, "utf32_sinhala_ci", "utf32");
        collation[180] = new Collation(180, "utf32_german2_ci", "utf32");
        collation[181] = new Collation(181, "utf32_croatian_ci", "utf32");
        collation[182] = new Collation(182, "utf32_unicode_520_ci", "utf32");
        collation[183] = new Collation(183, "utf32_vietnamese_ci", "utf32");
        collation[184] = new Collation(184, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[185] = new Collation(185, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[186] = new Collation(186, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[187] = new Collation(187, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[188] = new Collation(188, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[189] = new Collation(189, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[190] = new Collation(190, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[191] = new Collation(191, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[192] = new Collation(192, "utf8_unicode_ci", "utf8");
        collation[193] = new Collation(193, "utf8_icelandic_ci", "utf8");
        collation[194] = new Collation(194, "utf8_latvian_ci", "utf8");
        collation[195] = new Collation(195, "utf8_romanian_ci", "utf8");
        collation[196] = new Collation(196, "utf8_slovenian_ci", "utf8");
        collation[197] = new Collation(197, "utf8_polish_ci", "utf8");
        collation[198] = new Collation(198, "utf8_estonian_ci", "utf8");
        collation[199] = new Collation(199, "utf8_spanish_ci", "utf8");
        collation[200] = new Collation(200, "utf8_swedish_ci", "utf8");
        collation[201] = new Collation(201, "utf8_turkish_ci", "utf8");
        collation[202] = new Collation(202, "utf8_czech_ci", "utf8");
        collation[203] = new Collation(203, "utf8_danish_ci", "utf8");
        collation[204] = new Collation(204, "utf8_lithuanian_ci", "utf8");
        collation[205] = new Collation(205, "utf8_slovak_ci", "utf8");
        collation[206] = new Collation(206, "utf8_spanish2_ci", "utf8");
        collation[207] = new Collation(207, "utf8_roman_ci", "utf8");
        collation[208] = new Collation(208, "utf8_persian_ci", "utf8");
        collation[209] = new Collation(209, "utf8_esperanto_ci", "utf8");
        collation[210] = new Collation(210, "utf8_hungarian_ci", "utf8");
        collation[211] = new Collation(211, "utf8_sinhala_ci", "utf8");
        collation[212] = new Collation(212, "utf8_german2_ci", "utf8");
        collation[213] = new Collation(213, "utf8_croatian_ci", "utf8");
        collation[214] = new Collation(214, "utf8_unicode_520_ci", "utf8");
        collation[215] = new Collation(215, "utf8_vietnamese_ci", "utf8");
        collation[216] = new Collation(216, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[217] = new Collation(217, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[218] = new Collation(218, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[219] = new Collation(219, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[220] = new Collation(220, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[221] = new Collation(221, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[222] = new Collation(222, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[223] = new Collation(223, "utf8_general_mysql500_ci", "utf8");
        collation[224] = new Collation(224, "utf8mb4_unicode_ci", "utf8mb4");
        collation[225] = new Collation(225, "utf8mb4_icelandic_ci", "utf8mb4");
        collation[226] = new Collation(226, "utf8mb4_latvian_ci", "utf8mb4");
        collation[227] = new Collation(227, "utf8mb4_romanian_ci", "utf8mb4");
        collation[228] = new Collation(228, "utf8mb4_slovenian_ci", "utf8mb4");
        collation[229] = new Collation(229, "utf8mb4_polish_ci", "utf8mb4");
        collation[230] = new Collation(230, "utf8mb4_estonian_ci", "utf8mb4");
        collation[231] = new Collation(231, "utf8mb4_spanish_ci", "utf8mb4");
        collation[232] = new Collation(232, "utf8mb4_swedish_ci", "utf8mb4");
        collation[233] = new Collation(233, "utf8mb4_turkish_ci", "utf8mb4");
        collation[234] = new Collation(234, "utf8mb4_czech_ci", "utf8mb4");
        collation[235] = new Collation(235, "utf8mb4_danish_ci", "utf8mb4");
        collation[236] = new Collation(236, "utf8mb4_lithuanian_ci", "utf8mb4");
        collation[237] = new Collation(237, "utf8mb4_slovak_ci", "utf8mb4");
        collation[238] = new Collation(238, "utf8mb4_spanish2_ci", "utf8mb4");
        collation[239] = new Collation(239, "utf8mb4_roman_ci", "utf8mb4");
        collation[240] = new Collation(240, "utf8mb4_persian_ci", "utf8mb4");
        collation[241] = new Collation(241, "utf8mb4_esperanto_ci", "utf8mb4");
        collation[242] = new Collation(242, "utf8mb4_hungarian_ci", "utf8mb4");
        collation[243] = new Collation(243, "utf8mb4_sinhala_ci", "utf8mb4");
        collation[244] = new Collation(244, "utf8mb4_german2_ci", "utf8mb4");
        collation[245] = new Collation(245, "utf8mb4_croatian_ci", "utf8mb4");
        collation[246] = new Collation(246, "utf8mb4_unicode_520_ci", "utf8mb4");
        collation[247] = new Collation(247, "utf8mb4_vietnamese_ci", "utf8mb4");
        collation[248] = new Collation(248, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[249] = new Collation(249, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[250] = new Collation(250, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[251] = new Collation(251, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[252] = new Collation(252, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[253] = new Collation(253, "latin1_german1_ci", "latin1", "ISO8859_1");
        collation[254] = new Collation(254, "utf8mb3_general_cs", "utf8");

        INDEX_TO_COLLATION = new String[255];
        INDEX_TO_CHARSET = new String[255];
        Map<Integer, String> indexToMysqlCharset = new HashMap<Integer, String>();
        Map<String, Integer> indexMap = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        int i;
        for (i = 1; i < 255; i++) {
            INDEX_TO_COLLATION[i] = (collation[i]).collationName;
            indexToMysqlCharset.put(Integer.valueOf(i), (collation[i]).charsetName);
            INDEX_TO_CHARSET[i] = (collation[i]).javaCharsetName;

            if (INDEX_TO_CHARSET[i] != null) indexMap.put(INDEX_TO_CHARSET[i], Integer.valueOf(i));

        }

        for (i = 1; i < 255; i++) {
            if (INDEX_TO_COLLATION[i] == null)
                throw new RuntimeException("Assertion failure: No mapping from charset index " + i + " to a mysql collation");
            if (indexToMysqlCharset.get(Integer.valueOf(i)) == null)
                throw new RuntimeException("Assertion failure: No mapping from charset index " + i + " to a mysql character set");
            if (INDEX_TO_CHARSET[i] == null)
                throw new RuntimeException("Assertion failure: No mapping from charset index " + i + " to a Java character set");

        }
        MYSQL_ENCODING_NAME_TO_CHARSET_INDEX_MAP = Collections.unmodifiableMap(indexMap);
        STATIC_INDEX_TO_MYSQL_CHARSET_MAP = Collections.unmodifiableMap(indexToMysqlCharset);

        Map<String, String> tempMap = new HashMap<String, String>();

        tempMap.put("czech", "latin2");
        tempMap.put("danish", "latin1");
        tempMap.put("dutch", "latin1");
        tempMap.put("english", "latin1");
        tempMap.put("estonian", "latin7");
        tempMap.put("french", "latin1");
        tempMap.put("german", "latin1");
        tempMap.put("greek", "greek");
        tempMap.put("hungarian", "latin2");
        tempMap.put("italian", "latin1");
        tempMap.put("japanese", "ujis");
        tempMap.put("japanese-sjis", "sjis");
        tempMap.put("korean", "euckr");
        tempMap.put("norwegian", "latin1");
        tempMap.put("norwegian-ny", "latin1");
        tempMap.put("polish", "latin2");
        tempMap.put("portuguese", "latin1");
        tempMap.put("romanian", "latin2");
        tempMap.put("russian", "koi8r");
        tempMap.put("serbian", "cp1250");
        tempMap.put("slovak", "latin2");
        tempMap.put("spanish", "latin1");
        tempMap.put("swedish", "latin1");
        tempMap.put("ukrainian", "koi8u");

        ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET_MAP = Collections.unmodifiableMap(tempMap);
    }

    public static final String getMysqlEncodingForJavaEncoding(String javaEncodingUC, Connection conn) throws SQLException {
        try {
            List<VersionedStringProperty> mysqlEncodings = JAVA_UC_TO_MYSQL_CHARSET_MAP.get(javaEncodingUC);

            if (mysqlEncodings != null) {
                Iterator<VersionedStringProperty> iter = mysqlEncodings.iterator();

                VersionedStringProperty versionedProp = null;

                while (iter.hasNext()) {
                    VersionedStringProperty propToCheck = iter.next();

                    if (conn == null) {

                        return propToCheck.toString();
                    }

                    if (versionedProp != null && !versionedProp.preferredValue &&
                            versionedProp.majorVersion == propToCheck.majorVersion && versionedProp.minorVersion == propToCheck.minorVersion && versionedProp.subminorVersion == propToCheck.subminorVersion) {

                        return versionedProp.toString();
                    }

                    if (propToCheck.isOkayForVersion(conn)) {
                        if (propToCheck.preferredValue) {
                            return propToCheck.toString();
                        }

                        versionedProp = propToCheck;
                    }
                }

                if (versionedProp != null) {
                    return versionedProp.toString();
                }
            }

            return null;
        } catch (SQLException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor) null);
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    static final int getNumberOfCharsetsConfigured() {
        return MYSQL_TO_JAVA_CHARSET_MAP.size() / 2;
    }

    static final String getCharacterEncodingForErrorMessages(ConnectionImpl conn) throws SQLException {
        if (conn.versionMeetsMinimum(5, 5, 0)) {
            String str = conn.getServerVariable("character_set_results");
            if (str != null) {
                String str1 = conn.getJavaEncodingForMysqlEncoding(str);
                if (str1 != null) {
                    return str1;
                }
            }

            return "UTF-8";
        }

        String errorMessageFile = conn.getServerVariable("language");

        if (errorMessageFile == null || errorMessageFile.length() == 0) {
            return "Cp1252";
        }

        int endWithoutSlash = errorMessageFile.length();

        if (errorMessageFile.endsWith("/") || errorMessageFile.endsWith("\\")) {
            endWithoutSlash--;
        }

        int lastSlashIndex = errorMessageFile.lastIndexOf('/', endWithoutSlash - 1);

        if (lastSlashIndex == -1) {
            lastSlashIndex = errorMessageFile.lastIndexOf('\\', endWithoutSlash - 1);
        }

        if (lastSlashIndex == -1) {
            lastSlashIndex = 0;
        }

        if (lastSlashIndex == endWithoutSlash || endWithoutSlash < lastSlashIndex) {
            return "Cp1252";
        }

        errorMessageFile = errorMessageFile.substring(lastSlashIndex + 1, endWithoutSlash);

        String errorMessageEncodingMysql = ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET_MAP.get(errorMessageFile);

        if (errorMessageEncodingMysql == null) {
            return "Cp1252";
        }

        String javaEncoding = conn.getJavaEncodingForMysqlEncoding(errorMessageEncodingMysql);

        if (javaEncoding == null) {
            return "Cp1252";
        }

        return javaEncoding;
    }

    static final boolean isAliasForSjis(String encoding) {
        return ("SJIS".equalsIgnoreCase(encoding) || "WINDOWS-31J".equalsIgnoreCase(encoding) || "MS932".equalsIgnoreCase(encoding) || "SHIFT_JIS".equalsIgnoreCase(encoding) || "CP943".equalsIgnoreCase(encoding));
    }

    static final boolean isMultibyteCharset(String javaEncodingName) {
        String javaEncodingNameUC = javaEncodingName.toUpperCase(Locale.ENGLISH);

        return MULTIBYTE_CHARSETS.containsKey(javaEncodingNameUC);
    }

    private static void populateMapWithKeyValuePairsUnversioned(String configKey, Map<String, String> mapToPopulate, boolean addUppercaseKeys) {
        String javaToMysqlConfig = CHARSET_CONFIG.getProperty(configKey);
        if (javaToMysqlConfig == null)
            throw new RuntimeException("Could not find configuration value \"" + configKey + "\" in Charsets.properties resource");

        List<String> mappings = StringUtils.split(javaToMysqlConfig, ",", true);
        if (mappings == null)
            throw new RuntimeException("Missing/corrupt entry for \"" + configKey + "\" in Charsets.properties.");

        Iterator<String> mappingsIter = mappings.iterator();
        while (mappingsIter.hasNext()) {
            String aMapping = mappingsIter.next();
            List<String> parsedPair = StringUtils.split(aMapping, "=", true);
            if (parsedPair.size() != 2)
                throw new RuntimeException("Syntax error in Charsets.properties resource for token \"" + aMapping + "\".");

            String key = ((String) parsedPair.get(0)).toString();
            String value = ((String) parsedPair.get(1)).toString();
            mapToPopulate.put(key, value);

            if (addUppercaseKeys) mapToPopulate.put(key.toUpperCase(Locale.ENGLISH), value);
        }
    }

    private static void populateMapWithKeyValuePairsVersioned(String configKey, Map<String, List<VersionedStringProperty>> mapToPopulate, boolean addUppercaseKeys) {
        String javaToMysqlConfig = CHARSET_CONFIG.getProperty(configKey);
        if (javaToMysqlConfig == null)
            throw new RuntimeException("Could not find configuration value \"" + configKey + "\" in Charsets.properties resource");

        List<String> mappings = StringUtils.split(javaToMysqlConfig, ",", true);
        if (mappings == null)
            throw new RuntimeException("Missing/corrupt entry for \"" + configKey + "\" in Charsets.properties.");

        Iterator<String> mappingsIter = mappings.iterator();
        while (mappingsIter.hasNext()) {
            String aMapping = mappingsIter.next();
            List<String> parsedPair = StringUtils.split(aMapping, "=", true);
            if (parsedPair.size() != 2)
                throw new RuntimeException("Syntax error in Charsets.properties resource for token \"" + aMapping + "\".");

            String key = ((String) parsedPair.get(0)).toString();
            String value = ((String) parsedPair.get(1)).toString();

            List<VersionedStringProperty> versionedProperties = mapToPopulate.get(key);

            if (versionedProperties == null) {
                versionedProperties = new ArrayList<VersionedStringProperty>();
                mapToPopulate.put(key, versionedProperties);
            }

            VersionedStringProperty verProp = new VersionedStringProperty(value);
            versionedProperties.add(verProp);

            if (addUppercaseKeys) {
                String keyUc = key.toUpperCase(Locale.ENGLISH);
                versionedProperties = mapToPopulate.get(keyUc);

                if (versionedProperties == null) {
                    versionedProperties = new ArrayList<VersionedStringProperty>();
                    mapToPopulate.put(keyUc, versionedProperties);
                }

                versionedProperties.add(verProp);
            }
        }
    }

    public static int getCharsetIndexForMysqlEncodingName(String name) {
        if (name == null) {
            return 0;
        }

        Integer asInt = MYSQL_ENCODING_NAME_TO_CHARSET_INDEX_MAP.get(name);

        if (asInt == null) {
            return 0;
        }

        return asInt.intValue();
    }
}

