package com.ice.httpclient.utils;

import androidx.annotation.Nullable;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

/**
 * Desc:解压缩工具类
 * Created by icewater on 2024/01/02.
 */
public class Zip4jUtils {

    public static void unZipFile(final String zipFilePath, final String filePath)
            throws Exception {
        unZipFileWithPassword(zipFilePath,filePath,null);
    }

    public static void unZipFileWithPassword(String zipFilePath, String filePath,
                                             @Nullable String password)
            throws Exception{
        File zipFile = new File(zipFilePath);
        ZipFile zFile = new ZipFile(zipFile);
        zFile.setFileNameCharset("utf-8");

        if (!zFile.isValidZipFile()) {
            //将原压缩文件删除
            if (Utils.isFileExist(zipFilePath)) {
                File file = new File(zipFilePath);
                Utils.deleteFile(file);
            }
            throw new ZipException("exception zip file is valid");
        }
        File destDir = new File(filePath);
        if (destDir.isDirectory() && !destDir.exists()) {
            boolean makeDirResult = destDir.mkdir();
            if (!makeDirResult) {
                throw new ZipException("exception make dir error");
            }
        }
        if (zFile.isEncrypted()) {
            zFile.setPassword(password); // 设置解压密码
        }
        zFile.setRunInThread(false); //true 在子线程中进行解压 , false主线程中解压
        zFile.extractAll(filePath); //将压缩文件解压到filePath中...
        //将原压缩文件删除
        if (Utils.isFileExist(zipFilePath)) {
            File file = new File(zipFilePath);
            Utils.deleteFile(file);
        }
    }
}
