package com.saltlux.deepsignal.web.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.validation.constraints.NotNull;

public class FileUtil {

    public static boolean deleteFileIfExist(@NotNull String path) throws IOException {
        File file = new File(path);
        return Files.deleteIfExists(file.toPath());
    }
}
