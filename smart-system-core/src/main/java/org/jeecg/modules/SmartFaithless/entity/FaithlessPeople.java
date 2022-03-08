package org.jeecg.modules.SmartFaithless.entity;

import lombok.Data;

/**
 * @author zxh
 * @version 1.0
 * @description: 失信被执行人
 * @date 2022/2/28 15:53
 */
@Data
public class FaithlessPeople {
//          "datatype": "失信被执行人",
//            "iname": "赵五"
//            "sexname": "女性",
//            "age": "35",
//            "casecode": "（2018）粤0106执2984号",
//            "gistcid": "（2016）粤0106民初9317号",
//            "areaname": "广东省",
//            "courtname": "广东省广州市天河区人民法院",
//            "regdate": "2018-02-01",
//            "publishdate": "2018-05-22",
    private String datatype;// 数据类型 固定输出”失信被执行人”
    private String iname;// 失信被执行人姓名
    private String sexname;// 性别
    private String age;//  年龄
    private String casecode;// 案号
    private String gistcid;//执行依据文号
    private String areaname;// 地域名称
    private String courtname;// 执行法院
    private String regdate;// 立案时间 YYYY-MM-DD
    private String publishdate;//  发布时间 YYYY-MM-DD
}
