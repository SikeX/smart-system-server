package org.jeecg.modules.smartInvoice.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/2/26 17:12
 * @Version: V1.0
 */
@Data
public class InvoiceResult {
    private JSONObject invoiceData;

    private JSONObject VerificationData;
}
