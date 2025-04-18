package org.apache.http.impl.auth;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EncodingUtils;

@NotThreadSafe
final class NTLMEngineImpl
implements NTLMEngine
{
private static final Charset UNICODE_LITTLE_UNMARKED = CharsetUtils.lookup("UnicodeLittleUnmarked");

private static final Charset DEFAULT_CHARSET = Consts.ASCII;

protected static final int FLAG_REQUEST_UNICODE_ENCODING = 1;

protected static final int FLAG_REQUEST_TARGET = 4;

protected static final int FLAG_REQUEST_SIGN = 16;

protected static final int FLAG_REQUEST_SEAL = 32;

protected static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;

protected static final int FLAG_REQUEST_NTLMv1 = 512;
protected static final int FLAG_DOMAIN_PRESENT = 4096;
protected static final int FLAG_WORKSTATION_PRESENT = 8192;
protected static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
protected static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
protected static final int FLAG_REQUEST_VERSION = 33554432;
protected static final int FLAG_TARGETINFO_PRESENT = 8388608;
protected static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
protected static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
protected static final int FLAG_REQUEST_56BIT_ENCRYPTION = -2147483648;
private static final SecureRandom RND_GEN;
private static final byte[] SIGNATURE;

static {
SecureRandom rnd = null;
try {
rnd = SecureRandom.getInstance("SHA1PRNG");
} catch (Exception ignore) {}

RND_GEN = rnd;

byte[] bytesWithoutNull = "NTLMSSP".getBytes(Consts.ASCII);
SIGNATURE = new byte[bytesWithoutNull.length + 1];
System.arraycopy(bytesWithoutNull, 0, SIGNATURE, 0, bytesWithoutNull.length);
SIGNATURE[bytesWithoutNull.length] = 0;
}

private static final Type1Message TYPE_1_MESSAGE = new Type1Message();

static String getResponseFor(String message, String username, String password, String host, String domain) throws NTLMEngineException {
String response;
if (message == null || message.trim().equals("")) {
response = getType1Message(host, domain);
} else {
Type2Message t2m = new Type2Message(message);
response = getType3Message(username, password, host, domain, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo());
} 

return response;
}

static String getType1Message(String host, String domain) throws NTLMEngineException {
return TYPE_1_MESSAGE.getResponse();
}

static String getType3Message(String user, String password, String host, String domain, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
return (new Type3Message(domain, host, user, password, nonce, type2Flags, target, targetInformation)).getResponse();
}

private static String stripDotSuffix(String value) {
if (value == null) {
return null;
}
int index = value.indexOf(".");
if (index != -1) {
return value.substring(0, index);
}
return value;
}

private static String convertHost(String host) {
return stripDotSuffix(host);
}

private static String convertDomain(String domain) {
return stripDotSuffix(domain);
}

private static int readULong(byte[] src, int index) throws NTLMEngineException {
if (src.length < index + 4) {
throw new NTLMEngineException("NTLM authentication - buffer too small for DWORD");
}
return src[index] & 0xFF | (src[index + 1] & 0xFF) << 8 | (src[index + 2] & 0xFF) << 16 | (src[index + 3] & 0xFF) << 24;
}

private static int readUShort(byte[] src, int index) throws NTLMEngineException {
if (src.length < index + 2) {
throw new NTLMEngineException("NTLM authentication - buffer too small for WORD");
}
return src[index] & 0xFF | (src[index + 1] & 0xFF) << 8;
}

private static byte[] readSecurityBuffer(byte[] src, int index) throws NTLMEngineException {
int length = readUShort(src, index);
int offset = readULong(src, index + 4);
if (src.length < offset + length) {
throw new NTLMEngineException("NTLM authentication - buffer too small for data item");
}

byte[] buffer = new byte[length];
System.arraycopy(src, offset, buffer, 0, length);
return buffer;
}

private static byte[] makeRandomChallenge() throws NTLMEngineException {
if (RND_GEN == null) {
throw new NTLMEngineException("Random generator not available");
}
byte[] rval = new byte[8];
synchronized (RND_GEN) {
RND_GEN.nextBytes(rval);
} 
return rval;
}

private static byte[] makeSecondaryKey() throws NTLMEngineException {
if (RND_GEN == null) {
throw new NTLMEngineException("Random generator not available");
}
byte[] rval = new byte[16];
synchronized (RND_GEN) {
RND_GEN.nextBytes(rval);
} 
return rval;
}

protected static class CipherGen
{
protected final String domain;

protected final String user;

protected final String password;

protected final byte[] challenge;
protected final String target;
protected final byte[] targetInformation;
protected byte[] clientChallenge;
protected byte[] clientChallenge2;
protected byte[] secondaryKey;
protected byte[] timestamp;
protected byte[] lmHash = null;
protected byte[] lmResponse = null;
protected byte[] ntlmHash = null;
protected byte[] ntlmResponse = null;
protected byte[] ntlmv2Hash = null;
protected byte[] lmv2Hash = null;
protected byte[] lmv2Response = null;
protected byte[] ntlmv2Blob = null;
protected byte[] ntlmv2Response = null;
protected byte[] ntlm2SessionResponse = null;
protected byte[] lm2SessionResponse = null;
protected byte[] lmUserSessionKey = null;
protected byte[] ntlmUserSessionKey = null;
protected byte[] ntlmv2UserSessionKey = null;
protected byte[] ntlm2SessionResponseUserSessionKey = null;
protected byte[] lanManagerSessionKey = null;

public CipherGen(String domain, String user, String password, byte[] challenge, String target, byte[] targetInformation, byte[] clientChallenge, byte[] clientChallenge2, byte[] secondaryKey, byte[] timestamp) {
this.domain = domain;
this.target = target;
this.user = user;
this.password = password;
this.challenge = challenge;
this.targetInformation = targetInformation;
this.clientChallenge = clientChallenge;
this.clientChallenge2 = clientChallenge2;
this.secondaryKey = secondaryKey;
this.timestamp = timestamp;
}

public CipherGen(String domain, String user, String password, byte[] challenge, String target, byte[] targetInformation) {
this(domain, user, password, challenge, target, targetInformation, null, null, null, null);
}

public byte[] getClientChallenge() throws NTLMEngineException {
if (this.clientChallenge == null) {
this.clientChallenge = NTLMEngineImpl.makeRandomChallenge();
}
return this.clientChallenge;
}

public byte[] getClientChallenge2() throws NTLMEngineException {
if (this.clientChallenge2 == null) {
this.clientChallenge2 = NTLMEngineImpl.makeRandomChallenge();
}
return this.clientChallenge2;
}

public byte[] getSecondaryKey() throws NTLMEngineException {
if (this.secondaryKey == null) {
this.secondaryKey = NTLMEngineImpl.makeSecondaryKey();
}
return this.secondaryKey;
}

public byte[] getLMHash() throws NTLMEngineException {
if (this.lmHash == null) {
this.lmHash = NTLMEngineImpl.lmHash(this.password);
}
return this.lmHash;
}

public byte[] getLMResponse() throws NTLMEngineException {
if (this.lmResponse == null) {
this.lmResponse = NTLMEngineImpl.lmResponse(getLMHash(), this.challenge);
}
return this.lmResponse;
}

public byte[] getNTLMHash() throws NTLMEngineException {
if (this.ntlmHash == null) {
this.ntlmHash = NTLMEngineImpl.ntlmHash(this.password);
}
return this.ntlmHash;
}

public byte[] getNTLMResponse() throws NTLMEngineException {
if (this.ntlmResponse == null) {
this.ntlmResponse = NTLMEngineImpl.lmResponse(getNTLMHash(), this.challenge);
}
return this.ntlmResponse;
}

public byte[] getLMv2Hash() throws NTLMEngineException {
if (this.lmv2Hash == null) {
this.lmv2Hash = NTLMEngineImpl.lmv2Hash(this.domain, this.user, getNTLMHash());
}
return this.lmv2Hash;
}

public byte[] getNTLMv2Hash() throws NTLMEngineException {
if (this.ntlmv2Hash == null) {
this.ntlmv2Hash = NTLMEngineImpl.ntlmv2Hash(this.domain, this.user, getNTLMHash());
}
return this.ntlmv2Hash;
}

public byte[] getTimestamp() {
if (this.timestamp == null) {
long time = System.currentTimeMillis();
time += 11644473600000L;
time *= 10000L;

this.timestamp = new byte[8];
for (int i = 0; i < 8; i++) {
this.timestamp[i] = (byte)(int)time;
time >>>= 8L;
} 
} 
return this.timestamp;
}

public byte[] getNTLMv2Blob() throws NTLMEngineException {
if (this.ntlmv2Blob == null) {
this.ntlmv2Blob = NTLMEngineImpl.createBlob(getClientChallenge2(), this.targetInformation, getTimestamp());
}
return this.ntlmv2Blob;
}

public byte[] getNTLMv2Response() throws NTLMEngineException {
if (this.ntlmv2Response == null) {
this.ntlmv2Response = NTLMEngineImpl.lmv2Response(getNTLMv2Hash(), this.challenge, getNTLMv2Blob());
}
return this.ntlmv2Response;
}

public byte[] getLMv2Response() throws NTLMEngineException {
if (this.lmv2Response == null) {
this.lmv2Response = NTLMEngineImpl.lmv2Response(getLMv2Hash(), this.challenge, getClientChallenge());
}
return this.lmv2Response;
}

public byte[] getNTLM2SessionResponse() throws NTLMEngineException {
if (this.ntlm2SessionResponse == null) {
this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(getNTLMHash(), this.challenge, getClientChallenge());
}
return this.ntlm2SessionResponse;
}

public byte[] getLM2SessionResponse() throws NTLMEngineException {
if (this.lm2SessionResponse == null) {
byte[] clntChallenge = getClientChallenge();
this.lm2SessionResponse = new byte[24];
System.arraycopy(clntChallenge, 0, this.lm2SessionResponse, 0, clntChallenge.length);
Arrays.fill(this.lm2SessionResponse, clntChallenge.length, this.lm2SessionResponse.length, (byte)0);
} 
return this.lm2SessionResponse;
}

public byte[] getLMUserSessionKey() throws NTLMEngineException {
if (this.lmUserSessionKey == null) {
this.lmUserSessionKey = new byte[16];
System.arraycopy(getLMHash(), 0, this.lmUserSessionKey, 0, 8);
Arrays.fill(this.lmUserSessionKey, 8, 16, (byte)0);
} 
return this.lmUserSessionKey;
}

public byte[] getNTLMUserSessionKey() throws NTLMEngineException {
if (this.ntlmUserSessionKey == null) {
NTLMEngineImpl.MD4 md4 = new NTLMEngineImpl.MD4();
md4.update(getNTLMHash());
this.ntlmUserSessionKey = md4.getOutput();
} 
return this.ntlmUserSessionKey;
}

public byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
if (this.ntlmv2UserSessionKey == null) {
byte[] ntlmv2hash = getNTLMv2Hash();
byte[] truncatedResponse = new byte[16];
System.arraycopy(getNTLMv2Response(), 0, truncatedResponse, 0, 16);
this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(truncatedResponse, ntlmv2hash);
} 
return this.ntlmv2UserSessionKey;
}

public byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
if (this.ntlm2SessionResponseUserSessionKey == null) {
byte[] ntlm2SessionResponseNonce = getLM2SessionResponse();
byte[] sessionNonce = new byte[this.challenge.length + ntlm2SessionResponseNonce.length];
System.arraycopy(this.challenge, 0, sessionNonce, 0, this.challenge.length);
System.arraycopy(ntlm2SessionResponseNonce, 0, sessionNonce, this.challenge.length, ntlm2SessionResponseNonce.length);
this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(sessionNonce, getNTLMUserSessionKey());
} 
return this.ntlm2SessionResponseUserSessionKey;
}

public byte[] getLanManagerSessionKey() throws NTLMEngineException {
if (this.lanManagerSessionKey == null) {
try {
byte[] keyBytes = new byte[14];
System.arraycopy(getLMHash(), 0, keyBytes, 0, 8);
Arrays.fill(keyBytes, 8, keyBytes.length, (byte)-67);
Key lowKey = NTLMEngineImpl.createDESKey(keyBytes, 0);
Key highKey = NTLMEngineImpl.createDESKey(keyBytes, 7);
byte[] truncatedResponse = new byte[8];
System.arraycopy(getLMResponse(), 0, truncatedResponse, 0, truncatedResponse.length);
Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
des.init(1, lowKey);
byte[] lowPart = des.doFinal(truncatedResponse);
des = Cipher.getInstance("DES/ECB/NoPadding");
des.init(1, highKey);
byte[] highPart = des.doFinal(truncatedResponse);
this.lanManagerSessionKey = new byte[16];
System.arraycopy(lowPart, 0, this.lanManagerSessionKey, 0, lowPart.length);
System.arraycopy(highPart, 0, this.lanManagerSessionKey, lowPart.length, highPart.length);
} catch (Exception e) {
throw new NTLMEngineException(e.getMessage(), e);
} 
}
return this.lanManagerSessionKey;
}
}

static byte[] hmacMD5(byte[] value, byte[] key) throws NTLMEngineException {
HMACMD5 hmacMD5 = new HMACMD5(key);
hmacMD5.update(value);
return hmacMD5.getOutput();
}

static byte[] RC4(byte[] value, byte[] key) throws NTLMEngineException {
try {
Cipher rc4 = Cipher.getInstance("RC4");
rc4.init(1, new SecretKeySpec(key, "RC4"));
return rc4.doFinal(value);
} catch (Exception e) {
throw new NTLMEngineException(e.getMessage(), e);
} 
}

static byte[] ntlm2SessionResponse(byte[] ntlmHash, byte[] challenge, byte[] clientChallenge) throws NTLMEngineException {
try {
MessageDigest md5 = MessageDigest.getInstance("MD5");
md5.update(challenge);
md5.update(clientChallenge);
byte[] digest = md5.digest();

byte[] sessionHash = new byte[8];
System.arraycopy(digest, 0, sessionHash, 0, 8);
return lmResponse(ntlmHash, sessionHash);
} catch (Exception e) {
if (e instanceof NTLMEngineException) {
throw (NTLMEngineException)e;
}
throw new NTLMEngineException(e.getMessage(), e);
} 
}

private static byte[] lmHash(String password) throws NTLMEngineException {
try {
byte[] oemPassword = password.toUpperCase(Locale.ROOT).getBytes(Consts.ASCII);
int length = Math.min(oemPassword.length, 14);
byte[] keyBytes = new byte[14];
System.arraycopy(oemPassword, 0, keyBytes, 0, length);
Key lowKey = createDESKey(keyBytes, 0);
Key highKey = createDESKey(keyBytes, 7);
byte[] magicConstant = "KGS!@#$%".getBytes(Consts.ASCII);
Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
des.init(1, lowKey);
byte[] lowHash = des.doFinal(magicConstant);
des.init(1, highKey);
byte[] highHash = des.doFinal(magicConstant);
byte[] lmHash = new byte[16];
System.arraycopy(lowHash, 0, lmHash, 0, 8);
System.arraycopy(highHash, 0, lmHash, 8, 8);
return lmHash;
} catch (Exception e) {
throw new NTLMEngineException(e.getMessage(), e);
} 
}

private static byte[] ntlmHash(String password) throws NTLMEngineException {
if (UNICODE_LITTLE_UNMARKED == null) {
throw new NTLMEngineException("Unicode not supported");
}
byte[] unicodePassword = password.getBytes(UNICODE_LITTLE_UNMARKED);
MD4 md4 = new MD4();
md4.update(unicodePassword);
return md4.getOutput();
}

private static byte[] lmv2Hash(String domain, String user, byte[] ntlmHash) throws NTLMEngineException {
if (UNICODE_LITTLE_UNMARKED == null) {
throw new NTLMEngineException("Unicode not supported");
}
HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);

hmacMD5.update(user.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
if (domain != null) {
hmacMD5.update(domain.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
}
return hmacMD5.getOutput();
}

private static byte[] ntlmv2Hash(String domain, String user, byte[] ntlmHash) throws NTLMEngineException {
if (UNICODE_LITTLE_UNMARKED == null) {
throw new NTLMEngineException("Unicode not supported");
}
HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);

hmacMD5.update(user.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
if (domain != null) {
hmacMD5.update(domain.getBytes(UNICODE_LITTLE_UNMARKED));
}
return hmacMD5.getOutput();
}

private static byte[] lmResponse(byte[] hash, byte[] challenge) throws NTLMEngineException {
try {
byte[] keyBytes = new byte[21];
System.arraycopy(hash, 0, keyBytes, 0, 16);
Key lowKey = createDESKey(keyBytes, 0);
Key middleKey = createDESKey(keyBytes, 7);
Key highKey = createDESKey(keyBytes, 14);
Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
des.init(1, lowKey);
byte[] lowResponse = des.doFinal(challenge);
des.init(1, middleKey);
byte[] middleResponse = des.doFinal(challenge);
des.init(1, highKey);
byte[] highResponse = des.doFinal(challenge);
byte[] lmResponse = new byte[24];
System.arraycopy(lowResponse, 0, lmResponse, 0, 8);
System.arraycopy(middleResponse, 0, lmResponse, 8, 8);
System.arraycopy(highResponse, 0, lmResponse, 16, 8);
return lmResponse;
} catch (Exception e) {
throw new NTLMEngineException(e.getMessage(), e);
} 
}

private static byte[] lmv2Response(byte[] hash, byte[] challenge, byte[] clientData) throws NTLMEngineException {
HMACMD5 hmacMD5 = new HMACMD5(hash);
hmacMD5.update(challenge);
hmacMD5.update(clientData);
byte[] mac = hmacMD5.getOutput();
byte[] lmv2Response = new byte[mac.length + clientData.length];
System.arraycopy(mac, 0, lmv2Response, 0, mac.length);
System.arraycopy(clientData, 0, lmv2Response, mac.length, clientData.length);
return lmv2Response;
}

private static byte[] createBlob(byte[] clientChallenge, byte[] targetInformation, byte[] timestamp) {
byte[] blobSignature = { 1, 1, 0, 0 };
byte[] reserved = { 0, 0, 0, 0 };
byte[] unknown1 = { 0, 0, 0, 0 };
byte[] unknown2 = { 0, 0, 0, 0 };
byte[] blob = new byte[blobSignature.length + reserved.length + timestamp.length + 8 + unknown1.length + targetInformation.length + unknown2.length];

int offset = 0;
System.arraycopy(blobSignature, 0, blob, offset, blobSignature.length);
offset += blobSignature.length;
System.arraycopy(reserved, 0, blob, offset, reserved.length);
offset += reserved.length;
System.arraycopy(timestamp, 0, blob, offset, timestamp.length);
offset += timestamp.length;
System.arraycopy(clientChallenge, 0, blob, offset, 8);
offset += 8;
System.arraycopy(unknown1, 0, blob, offset, unknown1.length);
offset += unknown1.length;
System.arraycopy(targetInformation, 0, blob, offset, targetInformation.length);
offset += targetInformation.length;
System.arraycopy(unknown2, 0, blob, offset, unknown2.length);
offset += unknown2.length;
return blob;
}

private static Key createDESKey(byte[] bytes, int offset) {
byte[] keyBytes = new byte[7];
System.arraycopy(bytes, offset, keyBytes, 0, 7);
byte[] material = new byte[8];
material[0] = keyBytes[0];
material[1] = (byte)(keyBytes[0] << 7 | (keyBytes[1] & 0xFF) >>> 1);
material[2] = (byte)(keyBytes[1] << 6 | (keyBytes[2] & 0xFF) >>> 2);
material[3] = (byte)(keyBytes[2] << 5 | (keyBytes[3] & 0xFF) >>> 3);
material[4] = (byte)(keyBytes[3] << 4 | (keyBytes[4] & 0xFF) >>> 4);
material[5] = (byte)(keyBytes[4] << 3 | (keyBytes[5] & 0xFF) >>> 5);
material[6] = (byte)(keyBytes[5] << 2 | (keyBytes[6] & 0xFF) >>> 6);
material[7] = (byte)(keyBytes[6] << 1);
oddParity(material);
return new SecretKeySpec(material, "DES");
}

private static void oddParity(byte[] bytes) {
for (int i = 0; i < bytes.length; i++) {
byte b = bytes[i];
boolean needsParity = (((b >>> 7 ^ b >>> 6 ^ b >>> 5 ^ b >>> 4 ^ b >>> 3 ^ b >>> 2 ^ b >>> 1) & 0x1) == 0);

if (needsParity) {
bytes[i] = (byte)(bytes[i] | 0x1);
} else {
bytes[i] = (byte)(bytes[i] & 0xFFFFFFFE);
} 
} 
}

static class NTLMMessage
{
private byte[] messageContents = null;

private int currentOutputPosition = 0;

NTLMMessage() {}

NTLMMessage(String messageBody, int expectedType) throws NTLMEngineException {
this.messageContents = Base64.decodeBase64(messageBody.getBytes(NTLMEngineImpl.DEFAULT_CHARSET));

if (this.messageContents.length < NTLMEngineImpl.SIGNATURE.length) {
throw new NTLMEngineException("NTLM message decoding error - packet too short");
}
int i = 0;
while (i < NTLMEngineImpl.SIGNATURE.length) {
if (this.messageContents[i] != NTLMEngineImpl.SIGNATURE[i]) {
throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
}

i++;
} 

int type = readULong(NTLMEngineImpl.SIGNATURE.length);
if (type != expectedType) {
throw new NTLMEngineException("NTLM type " + Integer.toString(expectedType) + " message expected - instead got type " + Integer.toString(type));
}

this.currentOutputPosition = this.messageContents.length;
}

protected int getPreambleLength() {
return NTLMEngineImpl.SIGNATURE.length + 4;
}

protected int getMessageLength() {
return this.currentOutputPosition;
}

protected byte readByte(int position) throws NTLMEngineException {
if (this.messageContents.length < position + 1) {
throw new NTLMEngineException("NTLM: Message too short");
}
return this.messageContents[position];
}

protected void readBytes(byte[] buffer, int position) throws NTLMEngineException {
if (this.messageContents.length < position + buffer.length) {
throw new NTLMEngineException("NTLM: Message too short");
}
System.arraycopy(this.messageContents, position, buffer, 0, buffer.length);
}

protected int readUShort(int position) throws NTLMEngineException {
return NTLMEngineImpl.readUShort(this.messageContents, position);
}

protected int readULong(int position) throws NTLMEngineException {
return NTLMEngineImpl.readULong(this.messageContents, position);
}

protected byte[] readSecurityBuffer(int position) throws NTLMEngineException {
return NTLMEngineImpl.readSecurityBuffer(this.messageContents, position);
}

protected void prepareResponse(int maxlength, int messageType) {
this.messageContents = new byte[maxlength];
this.currentOutputPosition = 0;
addBytes(NTLMEngineImpl.SIGNATURE);
addULong(messageType);
}

protected void addByte(byte b) {
this.messageContents[this.currentOutputPosition] = b;
this.currentOutputPosition++;
}

protected void addBytes(byte[] bytes) {
if (bytes == null) {
return;
}
for (byte b : bytes) {
this.messageContents[this.currentOutputPosition] = b;
this.currentOutputPosition++;
} 
}

protected void addUShort(int value) {
addByte((byte)(value & 0xFF));
addByte((byte)(value >> 8 & 0xFF));
}

protected void addULong(int value) {
addByte((byte)(value & 0xFF));
addByte((byte)(value >> 8 & 0xFF));
addByte((byte)(value >> 16 & 0xFF));
addByte((byte)(value >> 24 & 0xFF));
}

String getResponse() {
byte[] resp;
if (this.messageContents.length > this.currentOutputPosition) {
byte[] tmp = new byte[this.currentOutputPosition];
System.arraycopy(this.messageContents, 0, tmp, 0, this.currentOutputPosition);
resp = tmp;
} else {
resp = this.messageContents;
} 
return EncodingUtils.getAsciiString(Base64.encodeBase64(resp));
}
}

static class Type1Message
extends NTLMMessage
{
private final byte[] hostBytes;

private final byte[] domainBytes;

Type1Message(String domain, String host) throws NTLMEngineException {
if (NTLMEngineImpl.UNICODE_LITTLE_UNMARKED == null) {
throw new NTLMEngineException("Unicode not supported");
}

String unqualifiedHost = NTLMEngineImpl.convertHost(host);

String unqualifiedDomain = NTLMEngineImpl.convertDomain(domain);

this.hostBytes = (unqualifiedHost != null) ? unqualifiedHost.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;

this.domainBytes = (unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.ROOT).getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
}

Type1Message() {
this.hostBytes = null;
this.domainBytes = null;
}

String getResponse() {
int finalLength = 40;

prepareResponse(40, 1);

addULong(-1576500735);

addUShort(0);
addUShort(0);

addULong(40);

addUShort(0);
addUShort(0);

addULong(40);

addUShort(261);

addULong(2600);

addUShort(3840);

if (this.hostBytes != null) {
addBytes(this.hostBytes);
}

if (this.domainBytes != null) {
addBytes(this.domainBytes);
}

return super.getResponse();
}
}

static class Type2Message
extends NTLMMessage
{
protected byte[] challenge;
protected String target;
protected byte[] targetInfo;
protected int flags;

Type2Message(String message) throws NTLMEngineException {
super(message, 2);

this.challenge = new byte[8];
readBytes(this.challenge, 24);

this.flags = readULong(20);

if ((this.flags & 0x1) == 0) {
throw new NTLMEngineException("NTLM type 2 message indicates no support for Unicode. Flags are: " + Integer.toString(this.flags));
}

this.target = null;

if (getMessageLength() >= 20) {
byte[] bytes = readSecurityBuffer(12);
if (bytes.length != 0) {
try {
this.target = new String(bytes, "UnicodeLittleUnmarked");
} catch (UnsupportedEncodingException e) {
throw new NTLMEngineException(e.getMessage(), e);
} 
}
} 

this.targetInfo = null;

if (getMessageLength() >= 48) {
byte[] bytes = readSecurityBuffer(40);
if (bytes.length != 0) {
this.targetInfo = bytes;
}
} 
}

byte[] getChallenge() {
return this.challenge;
}

String getTarget() {
return this.target;
}

byte[] getTargetInfo() {
return this.targetInfo;
}

int getFlags() {
return this.flags;
}
}

static class Type3Message
extends NTLMMessage
{
protected int type2Flags;

protected byte[] domainBytes;

protected byte[] hostBytes;

protected byte[] userBytes;

protected byte[] lmResp;

protected byte[] ntResp;

protected byte[] sessionKey;

Type3Message(String domain, String host, String user, String password, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
byte[] userSessionKey;
this.type2Flags = type2Flags;

String unqualifiedHost = NTLMEngineImpl.convertHost(host);

String unqualifiedDomain = NTLMEngineImpl.convertDomain(domain);

NTLMEngineImpl.CipherGen gen = new NTLMEngineImpl.CipherGen(unqualifiedDomain, user, password, nonce, target, targetInformation);

try {
if ((type2Flags & 0x800000) != 0 && targetInformation != null && target != null) {

this.ntResp = gen.getNTLMv2Response();
this.lmResp = gen.getLMv2Response();
if ((type2Flags & 0x80) != 0) {
userSessionKey = gen.getLanManagerSessionKey();
} else {
userSessionKey = gen.getNTLMv2UserSessionKey();
}

}
else if ((type2Flags & 0x80000) != 0) {

this.ntResp = gen.getNTLM2SessionResponse();
this.lmResp = gen.getLM2SessionResponse();
if ((type2Flags & 0x80) != 0) {
userSessionKey = gen.getLanManagerSessionKey();
} else {
userSessionKey = gen.getNTLM2SessionResponseUserSessionKey();
} 
} else {
this.ntResp = gen.getNTLMResponse();
this.lmResp = gen.getLMResponse();
if ((type2Flags & 0x80) != 0) {
userSessionKey = gen.getLanManagerSessionKey();
} else {
userSessionKey = gen.getNTLMUserSessionKey();
}

} 
} catch (NTLMEngineException e) {

this.ntResp = new byte[0];
this.lmResp = gen.getLMResponse();
if ((type2Flags & 0x80) != 0) {
userSessionKey = gen.getLanManagerSessionKey();
} else {
userSessionKey = gen.getLMUserSessionKey();
} 
} 

if ((type2Flags & 0x10) != 0) {
if ((type2Flags & 0x40000000) != 0) {
this.sessionKey = NTLMEngineImpl.RC4(gen.getSecondaryKey(), userSessionKey);
} else {
this.sessionKey = userSessionKey;
} 
} else {
this.sessionKey = null;
} 
if (NTLMEngineImpl.UNICODE_LITTLE_UNMARKED == null) {
throw new NTLMEngineException("Unicode not supported");
}
this.hostBytes = (unqualifiedHost != null) ? unqualifiedHost.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
this.domainBytes = (unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.ROOT).getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;

this.userBytes = user.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED);
}

String getResponse() {
int sessionKeyLen, ntRespLen = this.ntResp.length;
int lmRespLen = this.lmResp.length;

int domainLen = (this.domainBytes != null) ? this.domainBytes.length : 0;
int hostLen = (this.hostBytes != null) ? this.hostBytes.length : 0;
int userLen = this.userBytes.length;

if (this.sessionKey != null) {
sessionKeyLen = this.sessionKey.length;
} else {
sessionKeyLen = 0;
} 

int lmRespOffset = 72;
int ntRespOffset = 72 + lmRespLen;
int domainOffset = ntRespOffset + ntRespLen;
int userOffset = domainOffset + domainLen;
int hostOffset = userOffset + userLen;
int sessionKeyOffset = hostOffset + hostLen;
int finalLength = sessionKeyOffset + sessionKeyLen;

prepareResponse(finalLength, 3);

addUShort(lmRespLen);
addUShort(lmRespLen);

addULong(72);

addUShort(ntRespLen);
addUShort(ntRespLen);

addULong(ntRespOffset);

addUShort(domainLen);
addUShort(domainLen);

addULong(domainOffset);

addUShort(userLen);
addUShort(userLen);

addULong(userOffset);

addUShort(hostLen);
addUShort(hostLen);

addULong(hostOffset);

addUShort(sessionKeyLen);
addUShort(sessionKeyLen);

addULong(sessionKeyOffset);

addULong(this.type2Flags & 0x80 | this.type2Flags & 0x200 | this.type2Flags & 0x80000 | 0x2000000 | this.type2Flags & 0x8000 | this.type2Flags & 0x20 | this.type2Flags & 0x10 | this.type2Flags & 0x20000000 | this.type2Flags & Integer.MIN_VALUE | this.type2Flags & 0x40000000 | this.type2Flags & 0x800000 | this.type2Flags & 0x1 | this.type2Flags & 0x4);

addUShort(261);

addULong(2600);

addUShort(3840);

addBytes(this.lmResp);
addBytes(this.ntResp);
addBytes(this.domainBytes);
addBytes(this.userBytes);
addBytes(this.hostBytes);
if (this.sessionKey != null) {
addBytes(this.sessionKey);
}

return super.getResponse();
}
}

static void writeULong(byte[] buffer, int value, int offset) {
buffer[offset] = (byte)(value & 0xFF);
buffer[offset + 1] = (byte)(value >> 8 & 0xFF);
buffer[offset + 2] = (byte)(value >> 16 & 0xFF);
buffer[offset + 3] = (byte)(value >> 24 & 0xFF);
}

static int F(int x, int y, int z) {
return x & y | (x ^ 0xFFFFFFFF) & z;
}

static int G(int x, int y, int z) {
return x & y | x & z | y & z;
}

static int H(int x, int y, int z) {
return x ^ y ^ z;
}

static int rotintlft(int val, int numbits) {
return val << numbits | val >>> 32 - numbits;
}

static class MD4
{
protected int A = 1732584193;
protected int B = -271733879;
protected int C = -1732584194;
protected int D = 271733878;
protected long count = 0L;
protected byte[] dataBuffer = new byte[64];

void update(byte[] input) {
int curBufferPos = (int)(this.count & 0x3FL);
int inputIndex = 0;
while (input.length - inputIndex + curBufferPos >= this.dataBuffer.length) {

int transferAmt = this.dataBuffer.length - curBufferPos;
System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
this.count += transferAmt;
curBufferPos = 0;
inputIndex += transferAmt;
processBuffer();
} 

if (inputIndex < input.length) {
int transferAmt = input.length - inputIndex;
System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
this.count += transferAmt;
curBufferPos += transferAmt;
} 
}

byte[] getOutput() {
int bufferIndex = (int)(this.count & 0x3FL);
int padLen = (bufferIndex < 56) ? (56 - bufferIndex) : (120 - bufferIndex);
byte[] postBytes = new byte[padLen + 8];

postBytes[0] = Byte.MIN_VALUE;

for (int i = 0; i < 8; i++) {
postBytes[padLen + i] = (byte)(int)(this.count * 8L >>> 8 * i);
}

update(postBytes);

byte[] result = new byte[16];
NTLMEngineImpl.writeULong(result, this.A, 0);
NTLMEngineImpl.writeULong(result, this.B, 4);
NTLMEngineImpl.writeULong(result, this.C, 8);
NTLMEngineImpl.writeULong(result, this.D, 12);
return result;
}

protected void processBuffer() {
int[] d = new int[16];

for (int i = 0; i < 16; i++) {
d[i] = (this.dataBuffer[i * 4] & 0xFF) + ((this.dataBuffer[i * 4 + 1] & 0xFF) << 8) + ((this.dataBuffer[i * 4 + 2] & 0xFF) << 16) + ((this.dataBuffer[i * 4 + 3] & 0xFF) << 24);
}

int AA = this.A;
int BB = this.B;
int CC = this.C;
int DD = this.D;
round1(d);
round2(d);
round3(d);
this.A += AA;
this.B += BB;
this.C += CC;
this.D += DD;
}

protected void round1(int[] d) {
this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[0], 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[1], 7);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[2], 11);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[3], 19);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[4], 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[5], 7);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[6], 11);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[7], 19);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[8], 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[9], 7);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[10], 11);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[11], 19);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[12], 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[13], 7);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[14], 11);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[15], 19);
}

protected void round2(int[] d) {
this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[0] + 1518500249, 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[4] + 1518500249, 5);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[8] + 1518500249, 9);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[12] + 1518500249, 13);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[1] + 1518500249, 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[5] + 1518500249, 5);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[9] + 1518500249, 9);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[13] + 1518500249, 13);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[2] + 1518500249, 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[6] + 1518500249, 5);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[10] + 1518500249, 9);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[14] + 1518500249, 13);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[3] + 1518500249, 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[7] + 1518500249, 5);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[11] + 1518500249, 9);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[15] + 1518500249, 13);
}

protected void round3(int[] d) {
this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[0] + 1859775393, 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[8] + 1859775393, 9);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[4] + 1859775393, 11);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[12] + 1859775393, 15);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[2] + 1859775393, 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[10] + 1859775393, 9);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[6] + 1859775393, 11);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[14] + 1859775393, 15);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[1] + 1859775393, 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[9] + 1859775393, 9);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[5] + 1859775393, 11);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[13] + 1859775393, 15);

this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[3] + 1859775393, 3);
this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[11] + 1859775393, 9);
this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[7] + 1859775393, 11);
this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[15] + 1859775393, 15);
}
}

static class HMACMD5
{
protected byte[] ipad;

protected byte[] opad;

protected MessageDigest md5;

HMACMD5(byte[] input) throws NTLMEngineException {
byte[] key = input;
try {
this.md5 = MessageDigest.getInstance("MD5");
} catch (Exception ex) {

throw new NTLMEngineException("Error getting md5 message digest implementation: " + ex.getMessage(), ex);
} 

this.ipad = new byte[64];
this.opad = new byte[64];

int keyLength = key.length;
if (keyLength > 64) {

this.md5.update(key);
key = this.md5.digest();
keyLength = key.length;
} 
int i = 0;
while (i < keyLength) {
this.ipad[i] = (byte)(key[i] ^ 0x36);
this.opad[i] = (byte)(key[i] ^ 0x5C);
i++;
} 
while (i < 64) {
this.ipad[i] = 54;
this.opad[i] = 92;
i++;
} 

this.md5.reset();
this.md5.update(this.ipad);
}

byte[] getOutput() {
byte[] digest = this.md5.digest();
this.md5.update(this.opad);
return this.md5.digest(digest);
}

void update(byte[] input) {
this.md5.update(input);
}

void update(byte[] input, int offset, int length) {
this.md5.update(input, offset, length);
}
}

public String generateType1Msg(String domain, String workstation) throws NTLMEngineException {
return getType1Message(workstation, domain);
}

public String generateType3Msg(String username, String password, String domain, String workstation, String challenge) throws NTLMEngineException {
Type2Message t2m = new Type2Message(challenge);
return getType3Message(username, password, workstation, domain, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo());
}
}

