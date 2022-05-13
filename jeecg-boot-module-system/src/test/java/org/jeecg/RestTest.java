package org.jeecg;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.utils.FaceRecognitionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/10 16:00
 * @Version: V1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = JeecgSystemApplication.class)
public class RestTest {

    @Test
    public void restTest() {
        FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

        faceRecognitionUtil.getToken();
    }

}
