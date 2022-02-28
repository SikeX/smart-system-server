package org.jeecg.modules.smartExportWord.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.Version;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author lord
 * @description
 * @date 2022/2/22
 */


public class WordUtils {

    private static Configuration configuration = null;

    static {
        configuration = new Configuration(new Version("2.3.30"));
        configuration.setDefaultEncoding("utf-8");
        configuration.setObjectWrapper(new DefaultObjectWrapper(new Version("2.3.30")));
    }

    private WordUtils() {
        throw new AssertionError();
    }

    /**
     * 导出单个word
     *
     * @param map      数据
     * @param title    文件名
     * @param ftlTemplateName  模板文件
     * @param response 响应
     */
    public static void exportWord(Map map, String title, String ftlTemplateName, HttpServletResponse response) {

        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            //Template freemarkerTemplate = configuration.getTemplate(ftlTemplateName, "UTF-8");
            ClassPathResource classPathResource = new ClassPathResource(ftlTemplateName);
            InputStream inputStream = classPathResource.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            Template freemarkerTemplate = new Template(ftlTemplateName, reader, configuration);
            // 调用工具类的createDoc方法生成Word文档
            String fileName = title + ".doc";
            file = createDoc(map, freemarkerTemplate, fileName);
            fin = new FileInputStream(file);

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件名

            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

            out = response.getOutputStream();
            // 缓冲区
            byte[] buffer = new byte[1024];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (out != null) {
                    out.close();
                }
                // 删除临时文件
                if (file != null) {
                    file.delete();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    /**
     * 压缩包方式导出多个word
     * 由于一次请求浏览器只能响应一次，想导出多个必须打包，亲测for循环导出只能导一个
     * 如果想做到分别单独下载，那就得用插件啦，这里不提供插件的做法
     * 思路：生成临时目录-在临时目录生成word-将临时目录打zip包-zip文件下载-删除临时目录和zip包，
     * 回收系统资源
     *
     * @param dataList
     * @param fileNamesList
     * @param ftlTemplateName
     */
    public static void exportWordBatch(List<Map<String, Object>> dataList, List<String> fileNamesList, String ftlTemplateName,
                                       HttpServletResponse response, HttpServletRequest request) {
        File file = null;
        File zipfile = null;
        File directory = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis() + ".zip");

        try {
            //以此方式，在Linux环境下也可获取到模板文件
            ClassPathResource classPathResource = new ClassPathResource(ftlTemplateName);
            InputStream inputStream = classPathResource.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            Template freemarkerTemplate = new Template(ftlTemplateName, reader, configuration);
            out = response.getOutputStream();
            //根据当前时间创建临时目录
            String path = request.getRealPath("/resources/word/" + System.currentTimeMillis());
            directory = new File(path);
            directory.mkdirs();

            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> map = dataList.get(i);
                String wordName = fileNamesList.get(i);

                // 调用工具类的createDoc方法在临时目录下生成Word文档
                createDoc(map, freemarkerTemplate, directory.getPath() + "/" + wordName + i + ".doc");
            }
            //压缩目录
            ZipUtil.createZip(path, path + "zip.zip");
            //根据路径获取刚生成的zip包文件
            zipfile = new File(path + "zip.zip");
            fin = new FileInputStream(zipfile);
            // 缓冲区
            byte[] buffer = new byte[1024];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (out != null) {
                    out.close();
                }
                if (zipfile != null) {
                    zipfile.delete();
                }
                if (directory != null) {
                    //递归删除目录及目录下文件
                    ZipUtil.deleteFile(directory);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }
    }

    //生成word文档方法
    private static File createDoc(Map<?, ?> dataMap, Template template, String filename) {

        File f = new File(filename);
        Template t = template;
        Writer w = null;
        FileOutputStream fos = null;
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            fos = new FileOutputStream(f);
            w = new OutputStreamWriter(fos, UTF_8);
            //不要偷懒写成下面酱紫: 否则无法关闭fos流，打zip包时存取被拒抛异常
            //w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                fos.close();
                w.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return f;
    }

    //生成附件
    private static void createAccessory(Map<String, Object> map, String directory) throws IOException {
        //提醒涵
        ClassPathResource classPathResource = new ClassPathResource("/templates/RemindTheCulvert.ftl");
        InputStream inputStream = classPathResource.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        Template freemarkerTemplate = new Template("/templates/RemindTheCulvert.ftl", reader, configuration);
        createDoc(map, freemarkerTemplate, directory+ "/" + "哈尔滨市党员干部操办婚礼事宜提醒涵" + ".doc");

        //承诺书
        ClassPathResource classPathResource1 = new ClassPathResource("/templates/LetterOfCommitment.ftl");
        InputStream inputStream1 = classPathResource1.getInputStream();
        InputStreamReader reader1 = new InputStreamReader(inputStream1);
        Template freemarkerTemplate1 = new Template("/templates/LetterOfCommitment.ftl", reader1, configuration);
        createDoc(map, freemarkerTemplate1, directory+ "/" + "哈尔滨市党员干部操办婚礼事宜承诺书" + ".doc");
    }

    //婚前报备导出附件（婚前报备表，承诺书，提醒涵）
    public static void preExportWordBatch(List<Map<String, Object>> dataList, List<String> fileNamesList, String ftlTemplateName, HttpServletResponse response, HttpServletRequest request) {
        File file = null;
        File zipfile = null;
        File directory = null;
        File subDirectory = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis() + ".zip");

        try {
            //以此方式，在Linux环境下也可获取到模板文件
            ClassPathResource classPathResource = new ClassPathResource(ftlTemplateName);
            InputStream inputStream = classPathResource.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            Template freemarkerTemplate = new Template(ftlTemplateName, reader, configuration);
            out = response.getOutputStream();
            //根据当前时间创建临时目录
            String path = request.getRealPath("/resources/word/" + System.currentTimeMillis());
            directory = new File(path);
            directory.mkdirs();

            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> map = dataList.get(i);
                String wordName = fileNamesList.get(i);

                //子目录
                String subPath = path + "/" + wordName + i;
                subDirectory = new File(subPath);
                subDirectory.mkdir();


                // 调用工具类的createDoc方法在临时目录下生成Word文档
                createDoc(map, freemarkerTemplate, subDirectory.getPath() + "/" + "哈尔滨市党员干部操办婚礼事宜事前报告表" + ".doc");
                //承诺书，提醒涵
                createAccessory(map, subPath);
            }
            //压缩目录
            ZipUtil.createZip(path, path + "zip.zip");
            //根据路径获取刚生成的zip包文件
            zipfile = new File(path + "zip.zip");
            fin = new FileInputStream(zipfile);
            // 缓冲区
            byte[] buffer = new byte[1024];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (out != null) {
                    out.close();
                }
                if (zipfile != null) {
                    zipfile.delete();
                }
                if (directory != null) {
                    //递归删除目录及目录下文件
                    ZipUtil.deleteFile(directory);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }
    }
}
