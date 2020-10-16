package com.yly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 下午1:53
 * @since 1.0
 */
public final class Utils {

    private Utils() {
        throw new RuntimeException();
    }

    public static void copyClassToFile(String pkgName, String fileName, byte[] data) {
        FileOutputStream fileOutputStream = null;
        try {
            File pkgFile = new File(pkgName);
            if (!pkgFile.exists()) {
                pkgFile.mkdirs();
            }
            File geneFile = new File(pkgFile, fileName);
            if (!geneFile.exists()) {
                geneFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(geneFile);
            fileOutputStream.write(data);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
