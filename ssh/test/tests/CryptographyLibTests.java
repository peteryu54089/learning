package tests;

import ep.CryptographyLib;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CryptographyLibTests {
    private CryptographyLib cryptographyLib;

    @Before
    public void setUp() {
        cryptographyLib = new CryptographyLib(
                "8416dd80-d1eb-4ccf-8e12-fa2b034e59b1",
                "<RSAKeyValue><Modulus>4hvGFnPop/MTA7Ij1KiYylrRGWVFY6X1/ETHG3gmeorZxn7eu/yZuYSlXunss7jTQGKwhOhHnU1M1LcXioo07wF4V3V4PdRBpY0UW4Ls0MpWRiR+9YXEHAyNPSX11/F9eYMWgaX51RB05x4/c7mCCwu+4IBPOgDcQmDj1oxy63s=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>"
        );
    }

    @After
    public void tearDown() {
        cryptographyLib = null;
    }

    @Test
    public void testEncAesKey() {
        String str = cryptographyLib.getEncryptedAesKeyBase64();
        String data = "{\n" +
                "    \"APIKey\": \"3Xvd1MsiHWtjtARuMcShhJsO2BumhS\",\n" +
                "    \"學校IP\": \"163.22.21.75\",\n" +
                "    \"FTP帳號\": \"AlanChang\",\n" +
                "    \"FTP密碼\": \"admin#m8s5LZqhLhC5rytu\",\n" +
                "    \"EP帳號\": \"014300_APIDefault\",\n" +
                "    \"上傳單位\": \"000000\",\n" +
                "    \"學年度\": \"105\",\n" +
                "    \"學期\": \"1\",\n" +
                "    \"名冊別\": \"5\",\n" +
                "    \"副檔名\": \".xlsx\",\n" +
                "    \"上傳時間\": \"20180313_13_55_55\",\n" +
                "    \"Signature\": \"\",\n" +
                "    \"狀態\": \"add\"\n" +
                "}";
        String aesKey = cryptographyLib.getEncryptedAesKeyBase64();
        byte[] encData = cryptographyLib.encrypt(data); //.getBytes(StandardCharsets.UTF_8)
        Assert.assertNotEquals("", str);
    }
}
