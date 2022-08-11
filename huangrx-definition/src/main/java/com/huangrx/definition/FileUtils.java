package com.huangrx.definition;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩解压缩
 *
 * @author hrenxiang
 * @since 2022/6/19 19:22
 */
public class FileUtils {

    /**
     * 下载文件 <br/>
     *
     * 文件的下载是指通过文件的url在网络中得到文件的输入流。
     * 这里直接通过java自带的HttpURLConnection来从网络中获取文件流。
     *
     * @param urlPath 文件URL
     * @return
     */
    public static InputStream downloadByUrl(String urlPath) {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestProperty("Charset", "UTF-8");
            httpUrlConnection.connect();
            return httpUrlConnection.getInputStream();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 下载文件并保存本地 <br/>
     *
     * 这里就是将文件下载下来并保存到本地，通过downloadByUrl 方法得到文件流，然后，定义输出流FileOutputStream 来将数据写入到目标文件中，在操作文件时一定要注意在finally代码块中关闭输入流和输出流。
     *
     * @param fileUrl  文件的url
     * @param fileName 文件的原始文件名
     * @param targetDir 是文件的保存目录，可以自行指定
     * @return
     */
    public static String downloadAndSaveFile(String fileUrl, String fileName, String targetDir) throws IOException {
        if (StringUtils.isAnyBlank(fileUrl, fileName)) {
            return null;
        }
        //2.保存文件
        File file = new File(targetDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String[] fileNameStr = fileName.split("\\.");
        String newFileName = fileNameStr[0] + "." + fileNameStr[1];
        File targetFile = new File(targetDir + "/" + newFileName);
        InputStream is = null;
        FileOutputStream fos = null;
        //1.下载文件
        try {
            is = downloadByUrl(fileUrl);
            fos = new FileOutputStream(targetFile);
            byte[] bufferByte = new byte[1024];
            int len;
            while ((len = is.read(bufferByte)) > 0) {
                fos.write(bufferByte, 0, len);
            }
            fos.flush();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
        }
        return targetFile.getAbsolutePath();
    }

    /**
     * 文件压缩的整体思路是：
     *
     * 指定待压缩的目录以及压缩文件名，定义zip文件的输出流zos。
     * 如果文件是一个文件则向zos中添加一个zipEntry实体
     * 将源目录下的文件数据写入zos中。
     * 如果是空文件夹需要特殊处理
     * 如果是非空文件夹，需要循环其子目录，递归压缩。
     */

    /**
     * @param srcFile   需要压缩的文件目录
     * @param targetDir 压缩文件的目标路径
     * @return
     */
    public static void zipFile(File srcFile, String targetDir) {
        long start = System.currentTimeMillis();

        FileOutputStream out = null;
        ZipOutputStream zos = null;

        try {
            out = new FileOutputStream(new File(targetDir));
            zos = new ZipOutputStream(out);
            compress(srcFile, zos, srcFile.getName(), true);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 递归压缩方法 <br/>
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure) throws IOException {
        byte[] buf = new byte[1024];
        if (sourceFile.isFile()) {
            //向zip输出流中添加一个zip实体，构造器name为zip实体文件的名字
            zos.putNextEntry(new ZipEntry(name));
            //copy文件到zip输出流中
            int len;
            FileInputStream in = null;
            try {
                in = new FileInputStream(sourceFile);
                while ((len = in.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.flush();
            } finally {
                zos.closeEntry();
                IOUtils.closeQuietly(in);
            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 判断是否需要保留原来的文件结构时，需要对空文件夹进行处理
                if (keepDirStructure) {
                    //空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }

        }
    }

    /**
     * 通过ZipFile来读取压缩包，整体思路就是：
     *
     * 首先定义好解压之后的目录，一般就是跟zip文件的同名目录。
     * 定义ZipFile对象，不过需要指定编码gbk，不然可能会出现中文乱码的情况
     * 通过 zfile.entries() 获取zipFile对象下的所有元素，通过 zList.hasMoreElements() 是否为true不断循环读取
     * 通过 zList.nextElement() 获取压缩包里zipEntry实体，这个实体可能是文件也可能是目录
     * 通过newFile(destDir, zipEntry) 方法来创建zipEntry实体的父目录。
     * 如果zipEntry对象是目录的话，则直接返回
     * 如果zipEntry对象是文件的话，则需要将该文件的数据写入到本地。需要注意的是输入流和输出流用完之后需要立马在finally代码块中关闭掉。
     */

    /**
     * @param zipFileDir: zip文件的绝对路径
     */
    public static String unzipFile(String zipFileDir) throws IOException {
        //接收解压后的存放路径， 兼容文件名带多个点的情况
        String targetDir = zipFileDir.substring(0, zipFileDir.indexOf(".zip"));
        File destDir = new File(targetDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        byte[] buffer = new byte[1024];
        InputStream fis = null;
        ZipEntry zipEntry;
        FileOutputStream fos = null;
        try (ZipFile zFile = new ZipFile(zipFileDir, Charset.forName("gbk"))) {
            //指定编码，防止由于中文导致的MALFORMED问题
            Enumeration<? extends ZipEntry> zList = zFile.entries();
            // 遍历压缩包中的所有元素
            while (zList.hasMoreElements()) {
                zipEntry = zList.nextElement();
                //创建子目录
                File newFile = newFile(destDir, zipEntry);
                //文件夹的话就继续
                if (zipEntry.isDirectory()) {
                    continue;
                }
                try {
                    fis = new BufferedInputStream(zFile.getInputStream(zipEntry));
                    fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    //放在循环外效率更高
                    fos.flush();
                } finally {
                    IOUtils.closeQuietly(fos);
                    IOUtils.closeQuietly(fis);
                }
            }
        }
        return targetDir;
    }

    //创建子目录
    private static File newFile(File destinationDir, ZipEntry zipEntry) {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destFilePath = destFile.getParent();
        File file = new File(destFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return destFile;
    }

    /**
     * 递归删除
     *
     * @param targetFile 需要递归删除的目录
     */
    public static void deleteFile(File targetFile) {
        if (!targetFile.exists()) {
            return;
        }
        //如果是文件直接删除
        if (targetFile.exists() && targetFile.isFile()) {
            targetFile.delete();
            return;
        }
        File[] filesArray = targetFile.listFiles();
        if (filesArray == null || filesArray.length <= 0) {
            return;
        }
        for (File file : filesArray) {
            //如果是文件或者该目录没有子目录，就删除
            if (file.isFile() || Objects.requireNonNull(file.listFiles()).length <= 0) {
                file.delete();
            } else if (file.isDirectory()) {
                //递归删除
                deleteFile(file);
            }
        }
        //清空目录
        targetFile.delete();
    }

    public static void main(String[] args) throws IOException {
        FileUtils.zipFile(new File("C:\\Users\\huangrx\\Desktop\\aaa"), "C:\\Users\\huangrx\\Desktop\\zzz.zip");

        FileUtils.zipFile(new File("C:\\Users\\huangrx\\Desktop\\aa.txt"), "C:\\Users\\huangrx\\Desktop\\aa.zip");

        FileUtils.unzipFile("C:\\Users\\huangrx\\Desktop\\aa.zip");
    }


}