package org.jeecg.modules.qrCode.controller; /**
 * Created by Administrator on 2020/11/16.
 */
import org.jeecg.modules.qrCode.api.response.BaseResponse;
import org.jeecg.modules.qrCode.api.response.StatusCode;
import org.jeecg.modules.qrCode.QRcodeService.QrCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
//import java.util.Set;

/**
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/11/16 22:34
 **/
@RestController
@RequestMapping("/qrCode")

public class QrCodeController extends BaseController{

   private static final String RootPath="/opt/QRCode";

   private static final String FileFormat=".png";

    private static final ThreadLocal<SimpleDateFormat> LOCALDATEFORMAT=ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));

   /* //生成二维码并将其存放于本地目录
    @PostMapping("generate/v1")
    public BaseResponse generateV1(String content){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            final String fileName=LOCALDATEFORMAT.get().format(new Date());
            QRCodeUtil.createCodeToFile(content,new File(RootPath),fileName+FileFormat);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //生成二维码并将其返回给前端调用者
    @PostMapping("generate/v2")
    public BaseResponse generateV2(String content,HttpServletResponse servletResponse){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            QRCodeUtil.createCodeToOutputStream(content,servletResponse.getOutputStream());

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
*/


    @Autowired
    private QrCodeService codeService;

    //生成二维码并将其返回给前端调用者_hutool
    //@PostMapping("generate/v3")
    @GetMapping(value="generate/v3")
    public BaseResponse generateV3(String content,HttpServletResponse servletResponse){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
           //将生成的二维码文件存放于文件目录中
            final String fileName=LOCALDATEFORMAT.get().format(new Date());
            codeService.createCodeToFile(content,RootPath+ File.separator+fileName+".png");

            //将生成的二维码文件直接返回给前端响应流
            codeService.createCodeToStream(content,servletResponse);

        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

   // private volatile Set<String> dataSet= Sets.newLinkedHashSet();


   /* //编码生成器:前14位用年月日时分秒表示yyyyMMddHHmmss + 后6位用递增的数字  组成
    @PostMapping("generate/code")
    public BaseResponse generateCode(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            *//*dataSet.clear();
            dataSet.add(generateCodeInV1());
            log.info("---- 总数：{} ----",dataSet.size());*//*

            //多线程并发生成编码-N个线程并发测试，共负责生成Y个编码
            *//*dataSet.clear();
            ForkJoinPool joinPool=new ForkJoinPool(10);
            joinPool.execute(() -> IntStream.rangeClosed(1,1000).parallel().forEach(value -> {
                dataSet.add(generateCodeInV1());
            }));
            joinPool.shutdown();
            joinPool.awaitTermination(30, TimeUnit.MINUTES);
            log.info("---- 总数：{} ----",dataSet.size());*//*


            //多线程并发生成编码-N个线程并发测试，共负责生成Y个编码-synchronized 简单粗暴
            *//*dataSet.clear();
            ForkJoinPool joinPool=new ForkJoinPool(10);
            joinPool.execute(() -> IntStream.rangeClosed(1,100).parallel().forEach(value -> {
                dataSet.add(generateCodeInV2());
            }));
            joinPool.shutdown();
            joinPool.awaitTermination(30, TimeUnit.MINUTES);
            log.info("---- 总数：{} ----",dataSet.size());*//*

            response.setData(dataSet);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }



    @Autowired

    private QrCodeMapper qrCodeMapper;

    //随机的后6位商品编号，毫秒级上限为999999,应该是满足的 （只要间隔一定的频率重新发布/重启应用时，则当前计数器将重置为 100000）
    private static AtomicLong atomicLong=new AtomicLong(100000);

    @PostMapping("generate/code/v2")
    public BaseResponse generateCodeV2(){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //qrCodeMapper.insertSelective(new QrCode(generateCodeInV1()));

            //多线程并发测试
            *//*ForkJoinPool joinPool=new ForkJoinPool(20);
            joinPool.execute(() -> IntStream.rangeClosed(1,1000).parallel().forEach(value -> {
                qrCodeMapper.insertSelective(new QrCode(generateCodeInV1()));
            }));
            joinPool.shutdown();
            joinPool.awaitTermination(30, TimeUnit.MINUTES);*//*


            //方式一：原子操作数(单体应用系统架构)
            //qrCodeMapper.insertSelective(new QrCode(generateCodeInV1()));

            //方式二：雪花算法(单体/分布式)
            //qrCodeMapper.insertSelective(new QrCode(generateCodeInV2()));

            //方式三：Redis的自增操作(单体/分布式)
            qrCodeMapper.insertSelective(new QrCode(generateCodeInV3()));

            //方式四：zookeeper自增型节点
            //qrCodeMapper.insertSelective(new QrCode(generateCodeInV4()));
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }


    //全局唯一编码 - 正常情况 - 单体应用系统架构下可用 “原子操作数” 控制并发(加上本身就有计数功能)
    private String generateCodeInV1(){
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date()) + atomicLong.getAndIncrement();
    }

    private static final Snowflake snowflake=new Snowflake(5,5);

    //全局唯一编码 - 雪花算法
    private String generateCodeInV2(){
        return snowflake.nextIdStr()+RandomStringUtils.randomNumeric(1);
    }
*/

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String RedisKeyCode="sb:technology:code:v1";

    private static final Long LimitMaxCode=1000L;

    private static final Long InitKeyCode=99L;

    //每次项目重启都可以将其重置为初始值
    @PostConstruct
    public void init(){
        redisTemplate.delete(RedisKeyCode);

        redisTemplate.opsForValue().set(RedisKeyCode,InitKeyCode);
    }

    //全局唯一编码 - Redis:要使用 Redis的 INCRBY命令，需要设置缓存中key的序列化机制为：StringRedisSerializer;
    //不然会出现：ERR value is not an integer or out of range
    private String generateCodeInV3(){
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
        Long currCode=redisTemplate.opsForValue().increment(RedisKeyCode,1L);
        //编码上限/阈值检测机制
        if (Objects.equals(LimitMaxCode,currCode)){
            redisTemplate.opsForValue().set(RedisKeyCode,InitKeyCode);
            currCode=redisTemplate.opsForValue().increment(RedisKeyCode,1L);
        }
        return format.format(new Date()) + currCode;
    }


  /*  //zookeeper生成全局唯一标志符的方式
    private static final String ID_NODE = "/QRCodeV2";

    //zk客户端实例
    @Autowired
    private CuratorFramework client;

    //全局唯一编码 - zookeeper
    private String generateCodeInV4() throws Exception{
        if (null == client.checkExists().forPath(ID_NODE)) {
            //PERSISTENT(0, false, false) 持久型节点； PERSISTENT_SEQUENTIAL(2, false, true) 持久顺序型节点；
            //EPHEMERAL(1, true, false) 临时型节点；EPHEMERAL_SEQUENTIAL(3, true, true) 临时顺序型节点；
            //client.create().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(ID_NODE, new byte[0]);

            client.create().withMode(CreateMode.PERSISTENT).forPath(ID_NODE, new byte[0]);
        }
        //根据节点的版本号-从0开始递增的，因此位数也是不断在变化的(只要path不变)
        //Stat stat = client.setData().withVersion(-1).forPath(ID_NODE);
        Stat stat = client.setData().forPath(ID_NODE,new byte[0]);
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");

        return format.format(new Date()) + stat.getVersion();
    }*/
}

























