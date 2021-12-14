<!DOCTYPE html>
<#assign base=springMacroRequestContext.getContextUrl("")>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="${base}/bigscreen/template2/js/rem.js"></script>
    <script type="text/javascript" src="${base}/bigscreen/template2/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/bigscreen/template2/js/echarts.min.js"></script>
    <link rel="stylesheet" href="${base}/bigscreen/template2/css/style.css">
    <link rel="stylesheet" href="${base}/bigscreen/template2/dist/css/jquery.orgchart.css">
    <script language="javascript" type="text/javascript" src="${base}/bigscreen/template2/js/echarts-wordcloud.min.js"></script>
    <title>智慧村务服务中心-首页</title>
    <style type="text/css">
        #chart-container {
            display:flex;
            width: 100%;
            height: 95%;
            position: absolute;
            border: 0px ;
            margin: auto;
            text-align: center;
            font-family: Arial;
            top: 0.42rem;
            bottom: 0;
            left: 0;
            right: 0;
                   }
        #chart-container2 {
            display:none;
            width: 100%;
            height: 95%;
            position: absolute;
            border: 0px ;
            margin: auto;
            text-align: center;
            font-family: Arial;
            top: 0.42rem;
            bottom: 0;
            left: 0;
            right: 0;
        }
        #natureDepart {
            display:flex;
            width: 100%;
            height: 95%;
            border: 0px ;
            margin: auto;
            text-align: center;
            font-family: Arial;
        }
        #workDepart {
            display:none;
            width: 100%;
            height: 95%;
            border: 0px ;
            margin: auto;
            text-align: center;
            font-family: Arial;
        }
        .orgchart {
            overflow: auto;
            width:110%;
            height:78%;

        }
        .orgchart .node .title { height: 30px; line-height: 25px;  }
        .orgchart .node .title .symbol { margin-top: 1px; }
    </style>

</head>

<body style="visibility: hidden;">
<div class="container-flex" tabindex="0" hidefocus="true">
    <div class="box-left">
        <div class="left-top">
            <div class="current-num">
                <div>执行第一种形态审核数</div>
                <p>${shenhe}</p>
            </div>
        </div>
        <div class="left-center">
            <div class="title-box">
                <h6>消息类型</h6>
            </div>
            <div class="chart-box pie-chart">
                <div id="pie"></div>
                <div>
                    <div class="pie-data">

                    </div>
                </div>
            </div>
        </div>
        <div class="left-bottom" class="select">
            <div class="title-box">
                <h6>道里区乡镇互动统计</h6>
                <img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt="">
                <button id="filBtn">发帖数</button>
            </div>
            <div class="chart-box">
<#--                <div class="filter-con" id="filCon" data-type="1">-->
<#--                    <div class="select" tabindex="0" hidefocus="true">-->
<#--                        <div class="select-div">-->
<#--                            派件-->
<#--                        </div>-->
<#--                        <ul class="select-ul">-->
<#--                            <li class="active" data-value="1">派件</li>-->
<#--                            <li data-value="2">寄件</li>-->
<#--                        </ul>-->
<#--                    </div>-->
<#--                    <div class="select" tabindex="0" hidefocus="true">-->
<#--                        <div class="select-div">-->
<#--                            公司-->
<#--                        </div>-->
<#--                        <ul class="select-ul company">-->
<#--                            <li class="active" data-value="">公司</li>-->
<#--                            <li data-value="1">顺丰</li>-->
<#--                            <li data-value="2">京东</li>-->
<#--                            <li data-value="2">EMS</li>-->
<#--                        </ul>-->
<#--                    </div>-->
<#--                    <div class="select" tabindex="0" hidefocus="true">-->
<#--                        <div class="select-div">-->
<#--                            快件类型-->
<#--                        </div>-->
<#--                        <ul class="select-ul">-->
<#--                            <li class="active" data-value="">快件类型</li>-->
<#--                            <li data-value="0">文件</li>-->
<#--                            <li data-value="1">物品</li>-->
<#--                        </ul>-->
<#--                    </div>-->
<#--                </div>-->
                <div id="gdMap" class="gd-map"></div>
            </div>
        </div>
    </div>

    <div class="box-center">
        <div class="center-top">
            <h1>智慧村务服务中心</h1>
        </div>
        <div class="center-center">
            <div class="weather-box">
                <div class="data">
                    <p class="time" id="time">00:00:00</p>
                    <p id="date"></p>
                </div>
                <div class="weather">
                    <img id="weatherImg" src="${base}/bigscreen/template2/images/weather/weather_img01.png" alt="">
                    <div id="weather">
                        <p class="active">多云</p>
                        <p>16-22℃</p>
                        <p>道里区</p>
                    </div>
                </div>
            </div>
            <img src="${base}/bigscreen/template2/images/line_bg.png" alt="">
            <div class="select-box">
                <ul id="barType">
                    <li class="active" data-value="1" id="nature">自然机构</li>
                    <li data-value="2" id="work">业务机构</li>
                </ul>
<#--                <div data-type="2">-->
<#--                    <div class="select" tabindex="0" hidefocus="true">-->
<#--                        <div class="select-div">-->
<#--                            公司-->
<#--                        </div>-->
<#--                        <ul class="select-ul company">-->
<#--                            <li class="active" data-value="">公司</li>-->
<#--                            <li data-value="1">顺丰</li>-->
<#--                            <li data-value="2">京东</li>-->
<#--                            <li data-value="2">EMS</li>-->
<#--                        </ul>-->
<#--                    </div>-->
<#--                    <div class="select" tabindex="0" hidefocus="true">-->
<#--                        <div class="select-div">-->
<#--                            快件类型-->
<#--                        </div>-->
<#--                        <ul class="select-ul">-->
<#--                            <li class="active" data-value="">快件类型</li>-->
<#--                            <li data-value="0">文件</li>-->
<#--                            <li data-value="1">物品</li>-->
<#--                        </ul>-->
<#--                    </div>-->
<#--                </div>-->
            </div>
        </div>
        <div class="center-bottom">

<#--                <div id="chart4" style="width:100%;height:95%;">-->
<#--                    <div id="gdMap1" class="gd-map" style="width:100%;height:95%;"></div>-->
                    <div id="chart-container" ></div>
                    <div id="chart-container2" ></div>


<#--                <div class="city-box">-->
<#--                    <p id="titleQ"><span>全网</span>到珠海</p>-->
<#--                    <ul class="city-btn" data-city="1">-->
<#--                        <li class="active">全网</li>-->
<#--                        <li>ABCDE</li>-->
<#--                        <li>FGHIJ</li>-->
<#--                        <li>KLMNO</li>-->
<#--                        <li>PQRST</li>-->
<#--                        <li>UVWXYZ</li>-->
<#--                    </ul>-->
<#--                    <ul class="city-div" id="city">-->

<#--                    </ul>-->
<#--                </div>-->
<#--                <ul class="ranking-box">-->
<#--                    <li><span></span>-->
<#--                        <p>城市</p>-->
<#--                        <p>派件</p>-->
<#--                    </li>-->
<#--                    <!--                        <li><span>1</span><p>上海</p><p>1sss25(万件)</p></li>&ndash;&gt;-->
<#--                </ul>-->
                <div class="enlarge-box">
                    <button class="enlarge-btn" id="fangda"></button>
                    <ul class="modal-btn">
                        <li>
                            <div></div>1</li>
                        <li>
                            <div></div>2</li>
                        <li>
                            <div></div>3</li>
                        <li>
                            <div></div>4</li>
                        <li>
                            <div></div>5</li>
                        <li>
                            <div></div>6</li>
                    </ul>
                </div>

        </div>
    </div>

    <div class="box-right">
        <div class="right-top">
            <div class="title-box">
                <h6 id="barTitle">任务完成情况</h6>
                <img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt="">
                <button data-state=1 id="tabBtn"><img src="${base}/bigscreen/template2/images/chart_icon.png" alt=""><span>图表</span></button>
            </div>
            <p class="unit">单位：个</p>
            <div class="chart-box">
                <div id="chart3" style="width:100%;height:100%;"></div>
            </div>
            <div class="data-box" style="display:none;">
                <table class="table1">
                    <tr class="bg-color">
                        <td rowspan="2">通知公告</td>
                        <td rowspan="2" class="table-data dph-data0">0</td>
                        <td>已读</td>
                        <td class="table-data dph-data2">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>未读</td>
                        <td class="table-data dph-data1">0</td>
                    </tr>


                    <tr class="bg-color">
                        <td rowspan="2">廉政提醒</td>
                        <td rowspan="2" class="table-data dph-data3">0</td>
                        <td>已读</td>
                        <td class="table-data dph-data5">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>未读</td>
                        <td class="table-data dph-data4">0</td>
                    </tr>
                    <tr>

                    <tr>
                        <td>任务下发</td>
                        <td colspan="3" class="table-data dph-data6">0</td>
                    </tr>
                </table>
<#--                <table class="table1" style="display:none;">-->
<#--                    <tr>-->
<#--                        <td>入库件</td>-->
<#--                        <td colspan="3" class="table-data mail-data1">1</td>-->
<#--                    </tr>-->
<#--                    <tr class="bg-color">-->
<#--                        <td rowspan="2">在库件</td>-->
<#--                        <td rowspan="2" class="table-data mail-data2">1</td>-->
<#--                        <td>正常件</td>-->
<#--                        <td class="table-data mail-data7">1</td>-->
<#--                    </tr>-->
<#--                    <tr class="bg-color">-->
<#--                        <td>滞留件</td>-->
<#--                        <td class="table-data mail-data4">1</td>-->
<#--                    </tr>-->

<#--                    <tr>-->
<#--                        <td>出库件</td>-->
<#--                        <td colspan="3" class="mail-data6">1</td>-->
<#--                    </tr>-->
<#--                    <tr class="bg-color">-->
<#--                        <td>丢失件</td>-->
<#--                        <td colspan="3" class="mail-data3">1</td>-->
<#--                    </tr>-->
<#--                    <tr>-->
<#--                        <td>撤销件</td>-->
<#--                        <td colspan="3" class="table-data mail-data5">1</td>-->
<#--                    </tr>-->
<#--                </table>-->
            </div>
        </div>
        <div class="right-center">
            <div class="title-box">
                <p id="switchBtn"><span class="active" data-dataType="income">待审核</span><img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt=""><span data-dataType="expend">已审核</span></p>
                <img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt="">
                <button id="filBtn" >审核数</button>
            </div>
            <div class="data-box">
                <p class="data-number" id="totalProfit">${daishenhe}</p>
<#--                <div class="time-box" id="timeBox">-->
<#--                    <div class="time-div">-->
<#--                        <input class="time-input" type="text" value="" id="startTime">-->
<#--                        <img src="${base}/bigscreen/template2/images/selsct_time.png" alt="">-->
<#--                    </div>-->
<#--                    <div class="time-div end">-->
<#--                        <input class="time-input" type="text" value="" id="endTime">-->
<#--                        <img src="${base}/bigscreen/template2/images/selsct_time.png" alt="">-->
<#--                    </div>-->
<#--                </div>-->
            </div>
        </div>
        <div class="right-bottom">
            <div class="title-box">
                <h6 id="barTitle">入党纪念日</h6>
                <img class="line-img" src="${base}/bigscreen/template2/images/line-blue.png" alt="">
                <button id="setBtn"><img src="${base}/bigscreen/template2/images/data_icon.png" alt="">${riqi}</button>
            </div>
            <div id="birthdayPeople" class="data-box">
<#--                <div class="settings-box">-->
<#--                    <p><img src="${base}/bigscreen/template2/images/teacher_icon.png" alt="">今日值班：<span id="name_a"></span><span id="date_a"></span></p>-->
<#--                    <p><img src="${base}/bigscreen/template2/images/people_iocn.png" alt="">负责人：<span id="lea_a"></span></p>-->
<#--                </div>-->
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="pop-up">
        <span class="close-pop"></span>
        <h2 class="title">执行第一种形态审核数</h2>
        <div class="pop-data-box">
            <p>${shenhe}</p>
        </div>
    </div>

    <div class="pop-up">
        <span class="close-pop"></span>
        <h2 class="title">消息类型</h2>
        <div class="chart-box pie-chart">
            <div id="pie1"></div>
            <div>
                <div class="pie-data">
                </div>
            </div>
        </div>
    </div>

    <div class="pop-up">
        <span class="close-pop"></span>
        <h2 class="title">道里区乡镇互动统计 </h2>
<#--        <div class="filter-con pop-filter" style="display:flex" data-type="3">-->
<#--            <div class="select" tabindex="0" hidefocus="true">-->
<#--                <div class="select-div">-->
<#--                    派件-->
<#--                </div>-->
<#--                <ul class="select-ul">-->
<#--                    <li class="active" data-value="1">派件</li>-->
<#--                    <li data-value="2">寄件</li>-->
<#--                </ul>-->
<#--            </div>-->
<#--            <div class="select" tabindex="0" hidefocus="true">-->
<#--                <div class="select-div">-->
<#--                    公司-->
<#--                </div>-->
<#--                <ul class="select-ul company">-->
<#--                    <li class="active" data-value="">公司</li>-->
<#--                    <li data-value="1">顺丰</li>-->
<#--                    <li data-value="2">京东</li>-->
<#--                    <li data-value="2">EMS</li>-->
<#--                </ul>-->
<#--            </div>-->
<#--            <div class="select" tabindex="0" hidefocus="true">-->
<#--                <div class="select-div">-->
<#--                    快件类型-->
<#--                </div>-->
<#--                <ul class="select-ul">-->
<#--                    <li class="active" data-value="">快件类型</li>-->
<#--                    <li data-value="0">文件</li>-->
<#--                    <li data-value="1">物品</li>-->
<#--                </ul>-->
<#--            </div>-->
<#--        </div>-->
        <div class="chart-box pop-chart">
            <div id="gdMaps" class="gd-map"></div>
        </div>
    </div>

    <div class="pop-up">
        <span class="close-pop"></span>
<#--        <div class="filter-con pop-filters" style="display:flex" data-type="4">-->
<#--            <div class="select-pop" tabindex="0" hidefocus="true">-->
<#--                <ul id="barTypes">-->
<#--                    <li class="active" data-value="1">自然机构</li>-->
<#--                    <li data-value="2">业务机构</li>-->
<#--                </ul>-->
<#--            </div>-->
<#--        </div>-->
        <div class="cont-div">
            <div class="chart-box pop-charts">
                <h2 class="title" id="titlefordepart"></h2>
                <div id="natureDepart" ></div>
                <div id="workDepart" ></div>
            </div>
        </div>

        <div class="cont-div">
            <h2 class="title" id="barTitles">任务完成情况</h2>
            <button class="btn-class" data-state=1 id="tabBtns"><img src="${base}/bigscreen/template2/images/chart_icon.png" alt=""><span>图表</span></button>
            <div class="chart-box pop-chart">
                <div id="chart3s" style="width:100%;height:90%;"></div>
            </div>
            <div class="data-box" style="display:none;">
                <table class="table1">
                    <tr class="bg-color">
                        <td rowspan="2">通知公告</td>
                        <td rowspan="2" class="table-data dph-data0">0</td>
                        <td>已读</td>
                        <td class="table-data dph-data2">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>未读</td>
                        <td class="table-data dph-data1">0</td>
                    </tr>


                    <tr class="bg-color">
                        <td rowspan="2">廉政提醒</td>
                        <td rowspan="2" class="table-data dph-data3">0</td>
                        <td>已读</td>
                        <td class="table-data dph-data5">0</td>
                    </tr>
                    <tr class="bg-color">
                        <td>未读</td>
                        <td class="table-data dph-data4">0</td>
                    </tr>
                    <tr>

                    <tr>
                        <td>任务下发</td>
                        <td colspan="3" class="table-data dph-data6">0</td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="cont-div">
            <h2 class="title" id="titles"></h2>
            <div class="pop-data-box" id="totalProfits">
            </div>
        </div>
    </div>

    <div class="pop-up">
        <span class="close-pop"></span>
        <h2 class="title">入党纪念日</h2>
        <div id="birthdayPeople2" class="pop-data-box">
    </div>
</div>
</div>

</body>
<script type="text/javascript" src="${base}/bigscreen/template2/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/layer/layer.min.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/echarts.min.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/china.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/data/guangdong.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/base.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/js/vue.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/dist/js/jquery.orgchart.js"></script>
<script type="text/javascript" src="${base}/bigscreen/template2/dist/js/JSONLoop.js"></script>
<script language="javascript" type="text/javascript" src="${base}/bigscreen/template2/js/echarts-wordcloud.min.js"></script>

<#--<script type="text/javascript" src="${base}/bigscreen/template2/element/index.js"></script>-->

<script type="text/javascript">
    $('document').ready(function () {
       var daishenhe = 0;
       var yishenhe = 0;
        $.ajax({
            type: "post",
            async: false, //同步执行
            url: "getShenhe",
            // data : {},
            dataType: "json", //返回数据形式为json
            success: function (result) {
                if (result) {
                    daishenhe = result.daishenhe;
                    yishenhe = result.yishenhe;
                }

            }
        });
        $("body").css('visibility', 'visible');
        var localData = [$('#teacher').val(), $('#start').val() + '/' + $('#end').val(), $('#leader').val()]
        localStorage.setItem("data", localData);
        $('#conBtn').on('click', function () {
            localData = [$('#teacher').val(), $('#start').val() + '/' + $('#end').val(), $('#leader').val()]
            if (typeof (Storage) !== "undefined") {
                localStorage.setItem("data", localData);
                var arr = localStorage.getItem("data").split(',');
                $('#name_a').html(arr[0]);
                $('#date_a').html(arr[1]);
                $('#lea_a').html(arr[2]);
            }
        })
        $('#fangda').on('click', function () {
            if ($(this).siblings('ul').is(":hidden")) {
                $(this).addClass('active').siblings('ul').show();
            } else {
                $(this).removeClass('active').siblings('ul').hide();
            }
        })

        $('.modal-btn>li').on('click', function () {
            var index = $(this).index();
            if (index <= 2) {
                $('.container').attr('style', 'visibility: visible').find('.pop-up').eq(index).attr('style', 'visibility: visible').siblings().attr('style', 'visibility: hidden');
            } else if (index > 2 && index < 5) {
                $('.container').attr('style', 'visibility: visible').find('.pop-up').eq(3).attr('style', 'visibility: visible').siblings().attr('style', 'visibility: hidden');
                $('#natureDepart').hide();
                $('#workDepart').hide();
                if (index = 3) {
                    if ($("#barType").find('.active').data('value') == 1) {
                        $('#titlefordepart').html('自然部门层级关系');
                        $('#workDepart').hide();
                        $('#natureDepart').show();
                    } else if ($("#barType").find('.active').data('value') == 2) {
                        $('#titlefordepart').html('业务部门层级关系');
                        $('#natureDepart').hide();
                        $('#workDepart').show();
                    }
                }
                $('.cont-div').eq(index - 3).attr('style', 'visibility: visible').siblings('.cont-div').attr('style', 'visibility: hidden');
            } else if (index == 5) {
                $('.container').attr('style', 'visibility: visible').find('.pop-up').eq(3).attr('style', 'visibility: visible').siblings().attr('style', 'visibility: hidden');
                // $('.filter-con .pop-filters').hide();
                if ($('#switchBtn').find('.active').data('datatype') == "income") {
                    $('#titles').html('待审核');
                    $('#totalProfits').html(daishenhe);
                    $('.cont-div').eq(2).attr('style', 'visibility: visible').siblings('.cont-div').attr('style', 'visibility: hidden');
                } else if ($('#switchBtn').find('.active').data('datatype') == 'expend') {
                    $('#titles').html('已审核');
                    $('#totalProfits').html(yishenhe);
                    $('.cont-div').eq(2).attr('style', 'visibility: visible').siblings('div').attr('style', 'visibility: hidden');
                }
            }
        })
    })
        $(function(){
            //词云
            // console.log("---------------------词云1")
            let cloudData=[];
            $.ajax({
                type: "post",
                async: false, //同步执行
                url: "getCloudData",
                // data : {},
                dataType: "json", //返回数据形式为json
                success: function (result) {
                    if (result) {
                        if(result.data && result.data.length>0)
                        {cloudData = result.data
                        }
                        else{
                            for(var i=0;i<10;i++)
                            {
                                cloudData.push({name:'党员纪念日',value:'1927'});
                            }
                        }
                    }

                }
            });
            console.log("-----------------------！！！！！！！！！！！！！---------------------获取Cloud信息")
            console.log(cloudData)
            let wordCloud=echarts.init(document.getElementById('birthdayPeople'));
            let wordCloud2=echarts.init(document.getElementById('birthdayPeople2'));
            // console.log("---------------------词云1")
            window.addEventListener('resize', function () {
                wordCloud.resize();
                wordCloud2.resize();
            });
            // console.log("---------------------词云3")
            let wordCloud_option={

                left: 'center',
                top: 'center',
                tooltip: {
                        trigger: 'item',
                        formatter: '{b}<br/>{c} (年)'

                    // textStyle: {
                    //     color: '#FFF',
                    //     fontSize:12
                    // }
                    },
                series : [{
                    type : 'wordCloud',
                    shape:'smooth',
                    drawOutOfBound: true,
                    gridSize : 10,
                    sizeRange : [ 10, 24 ],
                    rotationRange: [0, 0],
                    textStyle : {
                        normal : {
                            color :function (d) {
                                // Random color
                                return 'rgba(255,255,255,'+Math.random()+ ')';
                            }
                        },
                        emphasis : {
                            shadowBlur : 10,
                            shadowColor : '#333'
                        }
                    },
                    data :cloudData
                }]
            };
            // console.log("---------------------词云4")
            wordCloud.setOption(wordCloud_option);
            wordCloud2.setOption(wordCloud_option);
            // console.log("---------------------词云5")
        })

    $(function(){
       var naturalSet = []
        var workSet = []
        var naturalData = {
            'name': '暂无部门信息'
        }
        var workData = {
            'name': '暂无部门信息'
        }
        $.ajax({
            type: "post",
            async: false, //同步执行
            url: "getDepartTree",
            // data : {},
            dataType: "json", //返回数据形式为json
            success: function (result) {
                if (result) {
                    if(result.naturalData && result.naturalData.length>0)
                    {naturalSet = result.naturalData;
                    console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!获取接口数据")
                        console.log(naturalSet)
                    }
                    if(result.workData && result.workData.length>0)
                    { workSet = result.workData;}
                }
            }
        });

       if(workSet && workSet.length>0)
       {
           console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!set循环得到Data")
           workSet.forEach(function(item, index) {
               if (!item.parentId) {
                   delete item.parentId;
                   Object.assign(workData, item);
               } else {
                   var jsonloop = new JSONLoop(workData, 'id', 'children');
                   jsonloop.findNodeById(workData, item.parentId, function(err, node) {
                       if (err) {
                           console.error(err);
                       } else {
                           delete item.parentId;
                           if (node.children) {
                               node.children.push(item);
                               var b = 2;
                           } else {
                               node.children = [ item ];
                               var a = 1;
                           }
                       }
                   });
               }
           });

       }

        if(naturalSet && naturalSet.length>0)
        {
            naturalSet.forEach(function(item, index) {
                if (!item.parentId) {
                    delete item.parentId;
                    Object.assign(naturalData, item);
                } else {
                    var jsonloop = new JSONLoop(naturalData, 'id', 'children');
                    jsonloop.findNodeById(naturalData, item.parentId, function(err, node) {
                        if (err) {
                            console.error(err);
                        } else {
                            delete item.parentId;
                            if (node.children) {
                                node.children.push(item);
                                var b = 2;
                            } else {
                                node.children = [ item ];
                                var a = 1;
                            }
                        }
                    });
                }
            });

        }
console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        console.log(naturalData)

        $('#chart-container').orgchart({
            'data' : naturalData,
            'direction': 'l2r'
        });
        $('#chart-container2').orgchart({
            'data' : workData,
            'direction': 'l2r'
        });
        $('#natureDepart').orgchart({
            'data' : naturalData,
            'direction': 'l2r'
        });
        $('#workDepart').orgchart({
            'data' : workData,
            'direction': 'l2r'
        });
    })

    //
    // var vm = new Vue({
    //     el: '#myvue',
    //     data: {
    //         aa:'',
    //         bb:'',
    //     },
    //     mounted(){
    //         this.aa = 1
    //         this.bb = 2
    //     }
    //
    // })

</script>




</html>