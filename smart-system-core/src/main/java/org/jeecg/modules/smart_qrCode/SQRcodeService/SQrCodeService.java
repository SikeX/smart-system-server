package org.jeecg.modules.smart_qrCode.SQRcodeService;/**
 * Created by Administrator on 2020/11/21.
 */

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeException;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/11/21 21:54
 **/
@Service
@Slf4j
public class SQrCodeService {

    @Autowired
    private QrConfig config;

    //生成到文件
    public void createCodeToFile(String content, String filePath) {
        try {
            QrCodeUtil.generate(content,config,FileUtil.file(filePath));
        } catch (QrCodeException e) {
            e.printStackTrace();
        }
    }

    //生成到流
    public void createCodeToStream(String content, HttpServletResponse response) {
        try {
            QrCodeUtil.generate(content,config, "png", response.getOutputStream());
        } catch (QrCodeException | IOException e) {
            e.printStackTrace();
        }
    }
}


























