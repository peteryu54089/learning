package ep;

import com.sun.istack.internal.NotNull;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import util.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CryptographyLib {
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private static final String ALG = "AES/CBC/PKCS7Padding";

    private final SecureRandom randomSecureRandom = new SecureRandom();
    private final SecretKey key;
    private final IvParameterSpec iv;
    private final String uuid;
    private final String encryptedAesKeyBase64;

    public String getUuid() {
        return uuid;
    }

    public class EncryptedContent {
        private final String sign;
        private final byte[] encData;
        private String hexEncData = null;

        public EncryptedContent(InputStream is) throws IOException {
            this(IOUtils.toByteArray(is));
        }

        public EncryptedContent(@NotNull byte[] rawData) {
            encData = encrypt(rawData);
            sign = toHex(sha256(rawData));
        }

        public String getSign() {
            return sign;
        }

        public synchronized String getHexEncData() {
            if (hexEncData == null)
                hexEncData = encodeBase64(encData);

            return hexEncData;
        }
    }

    public class EncryptedFile {
        private final File sourceFile;
        private File targetFile;
        private String sign;

        public EncryptedFile(File sourceFile) throws IOException {
            this(sourceFile, new File(sourceFile.getParentFile(),
                    FilenameUtils.removeExtension(sourceFile.getName())));
        }

        public EncryptedFile(File sourceFile, File targetFile) {
            this.sourceFile = sourceFile;
            this.targetFile = new File(targetFile.getPath() + ".bin");
        }

        public void encrypt() throws IOException {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(this.sourceFile, "r")) {
                byte[] document = new byte[(int) randomAccessFile.length()];
                randomAccessFile.readFully(document);
                sign = toHex(sha256(document));
                Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key, iv);
                byte[] encData = cipher.doFinal(document);
                Files.write(targetFile.toPath(), encData, StandardOpenOption.CREATE_NEW);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
        }

        public String getSign() {
            return sign;
        }

        public File getTargetFile() {
            return targetFile;
        }
    }

    public CryptographyLib(String uuid, String rsaXml) {
        this.uuid = uuid;
        try {
            Cipher cipher = Cipher.getInstance(ALG, "BC");
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            key = keyGen.generateKey();
            byte[] ivBytes = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(ivBytes);
            this.iv = new IvParameterSpec(ivBytes);
            encryptedAesKeyBase64 = encodeBase64(encAESKey(rsaXml,
                    genAESKey(key.getEncoded(), iv.getIV())
            ));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decodeBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static byte[] sha256(@NotNull String str) {
        return sha256(str.getBytes(UTF_8));
    }

    public static byte[] sha256(@NotNull byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] md5(@NotNull String str) {
        return md5(str.getBytes(UTF_8));
    }

    public static byte[] md5(@NotNull byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return digest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] convertByteToDotNetByte(byte[] bytes) {
        int[] ret = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i] & 0xff;
        }

        return ret;
    }

    private static String toHex(byte[] data) {
        return Hex.encodeHexString(data).toUpperCase();
    }

    public String getEncryptedAesKeyBase64() {
        return encryptedAesKeyBase64;
    }

    public byte[] encrypt(byte[] sourceData) {
        try {
            Cipher cipher = getCipher(
                    encodeBase64(genAESKey(key.getEncoded(), iv.getIV())),
                    Cipher.ENCRYPT_MODE
            );
            return cipher.doFinal(sourceData);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt(byte[] sourceData) {
        try {
            Cipher cipher = getCipher(
                    encodeBase64(genAESKey(key.getEncoded(), iv.getIV())),
                    Cipher.DECRYPT_MODE
            );
            return cipher.doFinal(sourceData);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public EncryptedFile encryptedFile(File source) throws IOException {
        return new EncryptedFile(source);
    }

    public byte[] encrypt(String sourceData) {
        return (encrypt(sourceData.getBytes(UTF_8)));
    }

    public byte[] decrypt(String sourceData) {
        return decrypt(decodeBase64(sourceData));
    }

    private byte[] genAESKey(byte[] key, byte[] iv) {
        byte[] newData = new byte[key.length + iv.length];
        System.arraycopy(key, 0, newData, 0, key.length);
        System.arraycopy(iv, 0, newData, key.length, iv.length);

        return newData;
    }

    /**
     * @param dotNetPublicKeyStr <RSAKeyValue><Modulus>vpUxxxxxy</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>
     * @param aesKeyWithIv
     * @return
     */
    private byte[] encAESKey(final String dotNetPublicKeyStr, byte[] aesKeyWithIv) {
        Function<String, String> extractPair = (tag) -> {
            return StringUtils.extractPair(dotNetPublicKeyStr, "<" + tag + ">", "</" + tag + ">");
        };

        try {
            String modBase64Str = extractPair.apply("Modulus");
            String eBase64Str = extractPair.apply("Exponent");
            BigInteger modulus = new BigInteger(1, decodeBase64(modBase64Str));
            BigInteger exponent = new BigInteger(1, decodeBase64(eBase64Str));
            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            PublicKey key = KeyFactory.getInstance("RSA", "BC")
                    .generatePublic(spec);
            Cipher cipher =
                    Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(aesKeyWithIv);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IndexOutOfBoundsException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cipher getCipher(String cryptoKey, int mode) {
        byte[] keyBytes = sha256(cryptoKey);
        byte[] ivBytes = md5(cryptoKey);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
        return getCipher(mode, skeySpec, iv);
    }

    private static Cipher getCipher(int mode, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(ALG, "BC");
            cipher.init(mode, key, iv);
            return cipher;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
