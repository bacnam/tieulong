package com.mysql.jdbc;

public class EscapeTokenizer {
    private int bracesLevel = 0;

    private boolean emittingEscapeCode = false;

    private boolean inComment = false;

    private boolean inQuotes = false;

    private char lastChar = Character.MIN_VALUE;

    private char lastLastChar = Character.MIN_VALUE;

    private int pos = 0;

    private char quoteChar = Character.MIN_VALUE;

    private boolean sawVariableUse = false;

    private String source = null;

    private int sourceLength = 0;

    public EscapeTokenizer(String s) {
        this.source = s;
        this.sourceLength = s.length();
        this.pos = 0;
    }

    public synchronized boolean hasMoreTokens() {
        return (this.pos < this.sourceLength);
    }

    public synchronized String nextToken() {
        StringBuffer tokenBuf = new StringBuffer();

        if (this.emittingEscapeCode) {
            tokenBuf.append("{");
            this.emittingEscapeCode = false;
        }

        for (; this.pos < this.sourceLength; this.pos++) {
            char c = this.source.charAt(this.pos);

            if (!this.inQuotes && c == '@') {
                this.sawVariableUse = true;
            }

            if ((c == '\'' || c == '"') && !this.inComment) {
                if (this.inQuotes && c == this.quoteChar &&
                        this.pos + 1 < this.sourceLength &&
                        this.source.charAt(this.pos + 1) == this.quoteChar) {
                    if (this.lastChar != '\\') {
                        tokenBuf.append(this.quoteChar);
                        tokenBuf.append(this.quoteChar);
                        this.pos++;

                        continue;
                    }
                }

                if (this.lastChar != '\\') {
                    if (this.inQuotes) {
                        if (this.quoteChar == c) {
                            this.inQuotes = false;
                        }
                    } else {
                        this.inQuotes = true;
                        this.quoteChar = c;
                    }
                } else if (this.lastLastChar == '\\') {
                    if (this.inQuotes) {
                        if (this.quoteChar == c) {
                            this.inQuotes = false;
                        }
                    } else {
                        this.inQuotes = true;
                        this.quoteChar = c;
                    }
                }

                tokenBuf.append(c);
            } else if (c == '-') {
                if (this.lastChar == '-' && this.lastLastChar != '\\' && !this.inQuotes) {
                    this.inComment = true;
                }

                tokenBuf.append(c);
            } else if (c == '\n' || c == '\r') {
                this.inComment = false;

                tokenBuf.append(c);
            } else if (c == '{') {
                if (this.inQuotes || this.inComment) {
                    tokenBuf.append(c);
                } else {
                    this.bracesLevel++;

                    if (this.bracesLevel == 1) {
                        this.pos++;
                        this.emittingEscapeCode = true;

                        return tokenBuf.toString();
                    }

                    tokenBuf.append(c);
                }
            } else if (c == '}') {
                tokenBuf.append(c);

                if (!this.inQuotes && !this.inComment) {
                    this.lastChar = c;

                    this.bracesLevel--;

                    if (this.bracesLevel == 0) {
                        this.pos++;

                        return tokenBuf.toString();
                    }
                }
            } else {
                tokenBuf.append(c);
            }

            this.lastLastChar = this.lastChar;
            this.lastChar = c;
            continue;
        }
        return tokenBuf.toString();
    }

    boolean sawVariableUse() {
        return this.sawVariableUse;
    }
}

