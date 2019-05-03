package tests;

import model.DownloadFileRequest;
import org.junit.Assert;
import org.junit.Test;
import util.Upload;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DownloadFileRequestTests {
    @Test
    public void test() throws UnsupportedEncodingException {
        DownloadFileRequest src = new DownloadFileRequest("abcdef", 1, "A123456788", "abc.def", "aaa.pdf");

        String link = Upload.generateDownloadLink(null, src);

        int idx = link.indexOf("?d=") + 3;
        String token = URLDecoder.decode(link.substring(idx), String.valueOf(StandardCharsets.UTF_8));

        DownloadFileRequest parsedRequest = Upload.decodeDownloadLink(token);

        Assert.assertEquals(src, parsedRequest);
    }
}
