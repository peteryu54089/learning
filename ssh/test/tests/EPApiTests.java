package tests;

import ep.EPApi;
import ep.model.ResponseData;
import ep.model.ResponseFID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class EPApiTests {
    private EPApi api;

    @Before
    public void setUp() {
        api = new EPApi("3Xvd1MsiHWtjtARuMcShhJsO2BumhS", "014300_APIDefault");
    }

    @After
    public void tearDown() {
        api = null;
    }

    @Test
    public void testSendFileToFtp() throws IOException {
        File tmpFile = File.createTempFile("epApiTest", null);
        tmpFile.deleteOnExit();
        Files.write(tmpFile.toPath(), new byte[]{1, 2, 3, 4, 5});

        Assert.assertTrue(api.upLoadFileToFtp(tmpFile, new File("/")));
    }

    @Test
    public void testRequestSendFile() throws IOException {
        File tmpFile = File.createTempFile("epApiTest", null);
        tmpFile.deleteOnExit();
        Files.write(tmpFile.toPath(), new byte[]{1, 2, 3, 4, 5});

        ResponseFID r = api.sendFileRequest("014300", "107", "1", "5", tmpFile);

        Assert.assertTrue(r.FID > 0);
    }

    @Test
    public void testCheckFile() throws IOException {
        int fid=1;

        ResponseData r = api.checkStatus(fid);


        Assert.assertTrue(r.FID > 0);
    }
}
