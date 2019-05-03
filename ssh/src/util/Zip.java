package util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Zip {
    private static byte[] EMPTY = new byte[0];

    public static ZipArchiveOutputStream addFile(final ZipArchiveOutputStream zipOut, File src, File dst) throws IOException {
        if (src == null || dst == null)
            return zipOut;

        ZipArchiveEntry zipEntry = new ZipArchiveEntry(src, dst.getPath());
        zipOut.putArchiveEntry(zipEntry);
        if (src.isFile()) {
            zipOut.write(FileUtils.readFileToByteArray(src));
        } else {
            zipOut.write(EMPTY);
        }

        return zipOut;
    }

    //public static String getProfitZip(List<Profit> profitList) throws IOException {
    //    String fileName = new File(Upload.saveRoot).getAbsolutePath();
    //    fileName += new Date().getTime() + ".zip";
    //    System.out.println(fileName);
    //    byte[] buffer = new byte[1024];
    //    File f = new File(fileName);
    //    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
    //    String root = new File(Upload.saveRoot).getAbsolutePath();
    //    for (Profit profit : profitList) {
    //        ZipEntry e = new ZipEntry(profit.getDocument());
    //        out.putNextEntry(e);
    //        FileInputStream in = new FileInputStream(root + "\\" + profit.getIdno() + "\\" + profit.getDocument());
    //        int len;
    //        while ((len = in.read(buffer)) > 0) {
    //            out.write(buffer, 0, len);
    //        }
    //        in.close();
    //    }
//
    //    out.closeEntry();
    //    out.close();
//
    //    return fileName;
    //}
}
