package org.jeecg.modules.smartExportWord.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author lord
 * @description
 * @date 2022/2/22
 */
public class ZipUtil {

    public static void zip(String inputFileName, String zipFileName)
            throws Exception {
        zip(zipFileName, new File(inputFileName));
    }

    private static void zip(String zipFileName, File inputFile)
            throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        zip(out, inputFile, "");
        System.out.println("zip done");
        out.close();
    }

    private static void zip(ZipOutputStream out, File f, String base)
            throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            //System.out.println(base);
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }

    /**
     * 创建ZIP文件
     *
     * @param sourcePath 文件或文件夹路径
     * @param zipPath    生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(String sourcePath, String zipPath) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                    //压缩成功后，删除打包前的文件
                    deleteFile(new File(sourcePath));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeZip(File file, String parentPath,
                                 ZipOutputStream zos) {
        if (file.exists()) {
            // 处理文件夹
            if (file.isDirectory()) {
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                for (File f : files) {
                    writeZip(f, parentPath, zos);
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void copyResource(List<String> oldResPath, String newResPath) {
        for (int m = 0; m < oldResPath.size(); m++) {
            try {
                // 如果文件夹不存在 则建立新文件夹
                (new File(newResPath)).mkdirs();
                File a = new File(oldResPath.get(m));
                // 如果已经是具体文件，读取
                if (a.isFile()) {
                    FileInputStream input = new FileInputStream(a);
                    FileOutputStream output = new FileOutputStream(newResPath + "/" + (a.getName()).toString());
                    byte[] b = new byte[1024 * 4];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                    // 如果文件夹下还存在文件，遍历，直到得到具体的文件
                } else {
                    String[] file = a.list();
                    File temp = null;
                    for (int i = 0; i < file.length; i++) {
                        if (oldResPath.get(m).endsWith(File.separator)) {
                            temp = new File(oldResPath.get(m) + file[i]);
                        } else {
                            temp = new File(oldResPath.get(m) + File.separator + file[i]);
                        }

                        if (temp.isFile()) {
                            FileInputStream input = new FileInputStream(temp);
                            FileOutputStream output = new FileOutputStream(newResPath + "/" + (temp.getName()).toString());
                            byte[] b = new byte[1024 * 4];
                            int len;
                            while ((len = input.read(b)) != -1) {
                                output.write(b, 0, len);
                            }
                            output.flush();
                            output.close();
                            input.close();
                        }
                        if (temp.isDirectory()) {
                            List<String> oldChildPath = new ArrayList<String>();
                            oldChildPath.add(oldResPath.get(m) + "/" + file[i]);
                            newResPath = newResPath + "/" + file[i];
                            // 如果是子文件夹 递归循环
                            copyResource(oldChildPath, newResPath);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 判断是否是文件
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                // 否则如果它是一个目录
                // 声明目录下所有的文件 files[];
                File files[] = file.listFiles();
                // 遍历目录下所有的文件
                for (int i = 0; i < files.length; i++) {
                    // 把每个文件 用这个方法进行迭代
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }

    /**
     * 时间格式化
     *
     * @return
     */
    public static String dateToString() {
        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = formatter.format(d);
        return time;
    }
}
