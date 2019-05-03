package resources;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Resource {
    private Resource() {
    }

    public static InputStream getFile(String path) throws FileNotFoundException {
        InputStream is = Resource.class.getResourceAsStream(path);
        if (is == null)
            throw new FileNotFoundException(path);
        return is;
    }
}
