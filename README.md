smart-system-server 智慧村务服务端
===============

## 框架

#### 协作文档

本仓库目前有3个分支，master和dev。主要在dev分支上进行开发和测试，功能完善之后再合并到master。

```
├─master
└─dev
```

#### 1.1、准备工作

* 前端仓库： https://github.com/SikeX/smart-system-web
* 后端仓库： https://github.com/SikeX/smart-system-server

在github上fork（前端，后端）仓库到你自己的github，将你fork之后的远程仓库克隆到本地，并在你的本地仓库上添加上游仓库，即源仓库。fork一般是fork了仓库的master分支，所以可能还需要自己去切出dev分支然后拉取源仓库dev分支上的内容。

```
# 前端
git clone https://github.com/{你的github}/smart-system-web
# 后端
git clone https://github.com/{你的github}/smart-system-server

# 上游的源仓库地址只需添加一次，在你的本地前端项目
git remote add upstream https://github.com/SikeX/smart-system-web.git
# 然后添加你自己的远程仓库（如果你是clone的话，就已经自动添加的，如果你是下载的zip则需要手动添加）
git remote add origin https://github.com/{你的github}/smart-system-web.git
# 上游的源仓库地址只需添加一次，在你的本地后端项目
git remote add upstream https://github.com/SikeX/smart-system-server.git
# 然后添加你自己的远程仓库（如果你是clone的话，就已经自动添加的，如果你是下载的zip则需要手动添加）
git remote add origin https://github.com/{你的github}/smart-system-server.git

# 切出dev分支并拉取源仓库dev分支上的内容
git checkout -b dev
git pull upstream dev
```

#### 1.2.1、协作方式-1

要开发某个模块的时候，在你的本地仓库创建一个分支，例如mydev。

```
git checkout -b mydev
```

切换之后你的本地仓库上就有3个分支了，如下所示。checkout之后会自动切换到mydev分支。

```
├─master
├─dev
└─mydev
```

在mydev分支上进行开发和测试，完成相应的功能或者模块，完成之后再切回到dev分支将mydev的内容合并到dev。

```
# mydev分支上提交你的更新
git add *
git commit -m "add something"
# 切换到dev分支
git checkout dev
```

由于在你开发过程中，我也可能在开发并且更新了仓库，为了避免冲突，在合并分支之前你还需要更新你本地仓库的dev分支。先在本地仓库上添加上游仓库upstream，上游仓库即我的仓库，然后使用pull命令从上游仓库拉取更新。

```
# 拉取上游的源仓库dev分支上的更新
git pull upstream dev
```

更新完dev之后，将mydev分支合并到dev分支并提交到你自己的远程仓库。完成之后，mydev分支就可以删除了，你也可以继续留着。

```
# 将mydev分支合并到dev，可能会有冲突需要自行解决
git merge mydev
# 推送到你自己的远程仓库，注意这里是push到origin dev，不是upstream dev
git push origin dev
# 删除mydev分支
git branch -d mydev
```

推送到自己的远程仓库后，就可以到github上面给我发Pull Request了，然后等待我合并你的代码。

#### 1.2.2、协作方式-2

嫌上面太麻烦？你也可以直接在fork之后的的dev分支上面进行开发，然后在发送PR之前先从源仓库拉取更新。

```
# 提交你的代码
git add *
git commit -m "add something"
# 拉取上游的源仓库dev分支上的更新，可能会有冲突需要自行解决
git pull upstream dev
# 推送到你自己的远程仓库，注意这里是push到origin dev，不是upstream dev
git push origin dev
```

#### 1.3、备注

所有拉取远程仓库的更新的操作请在本地仓库上使用命令行完成。

开发之前和合并分支之前从上游仓库dev分支pull一下

```
# 拉取上游的源仓库dev分支上的更新
git pull upstream dev
```

严禁通过其他做法拉取更新（例如在GitHub网站上通过网页操作进行更新然后在本地pull origin dev），以免造成不必要的合并冲突以及多余的commit。

JEECG BOOT 低代码开发平台:
- 官网: http://www.jeecg.com/

- github: https://github.com/jeecgboot/jeecg-boot

## 后端技术架构
- 基础框架：Spring Boot 2.3.5.RELEASE

- 持久层框架：Mybatis-plus 3.4.3.1

- 安全框架：Apache Shiro 1.7.0，Jwt 3.11.0

- 数据库连接池：阿里巴巴Druid 1.1.22

- 缓存框架：redis

- 日志打印：logback

- 其他：fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。



## 开发环境

- 语言：Java 8

- IDE(JAVA)： Eclipse安装lombok插件 或者 IDEA

- 依赖管理：Maven

- 数据库：MySQL5.7+  &  Oracle 11g & SqlServer & postgresql

- 缓存：Redis


## 技术文档


- 在线演示 ：  [http://boot.jeecg.com](http://boot.jeecg.com)

- 在线文档：  [http://doc.jeecg.com](http://doc.jeecg.com)

- 常见问题：  [http://jeecg.com/doc/qa](http://jeecg.com/doc/qa)

- QQ交流群 ：  ④774126647、③816531124、①284271917、②769925425


## 专项文档

#### 一、查询过滤器用法

```
QueryWrapper<?> queryWrapper = QueryGenerator.initQueryWrapper(?, req.getParameterMap());
```

代码示例：

```

	@GetMapping(value = "/list")
	public Result<IPage<JeecgDemo>> list(JeecgDemo jeecgDemo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, 
	                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<JeecgDemo>> result = new Result<IPage<JeecgDemo>>();
		
		//调用QueryGenerator的initQueryWrapper
		QueryWrapper<JeecgDemo> queryWrapper = QueryGenerator.initQueryWrapper(jeecgDemo, req.getParameterMap());
		
		Page<JeecgDemo> page = new Page<JeecgDemo>(pageNo, pageSize);
		IPage<JeecgDemo> pageList = jeecgDemoService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

```



- 查询规则 (本规则不适用于高级查询,高级查询有自己对应的查询类型可以选择 )

| 查询模式           | 用法    | 说明                         |
|---------- |-------------------------------------------------------|------------------|
| 模糊查询     | 支持左右模糊和全模糊  需要在查询输入框内前或后带\*或是前后全部带\* |    |
| 取非查询     | 在查询输入框前面输入! 则查询该字段不等于输入值的数据(数值类型不支持此种查询,可以将数值字段定义为字符串类型的) |    |
| \>  \>= < <=     | 同取非查询 在输入框前面输入对应特殊字符即表示走对应规则查询 |    |
| in查询     | 若传入的数据带,(逗号) 则表示该查询为in查询 |    |
| 多选字段模糊查询     | 上述4 有一个特例，若某一查询字段前后都带逗号 则会将其视为走这种查询方式 ,该查询方式是将查询条件以逗号分割再遍历数组 将每个元素作like查询 用or拼接,例如 现在name传入值 ,a,b,c, 那么结果sql就是 name like '%a%' or name like '%b%' or name like '%c%' |    |


#### 二、AutoPoi(EXCEL工具类-EasyPOI衍变升级重构版本）

  [在线文档](https://github.com/zhangdaiscott/autopoi)



#### 三、代码生成器

> 功能说明：   一键生成的代码（包括：controller、service、dao、mapper、entity、vue）

 - 模板位置： src/main/resources/jeecg/code-template
 - 技术文档： http://doc.jeecg.com/2043916



#### 四、编码排重使用示例

重复校验效果：
![输入图片说明](https://static.oschina.net/uploads/img/201904/19191836_eGkQ.png "在这里输入图片标题")

1.引入排重接口,代码如下:  

```
import { duplicateCheck } from '@/api/api'
```
2.找到编码必填校验规则的前端代码,代码如下:  

```
<a-input placeholder="请输入编码" v-decorator="['code', validatorRules.code ]"/>

code: {
            rules: [
              { required: true, message: '请输入编码!' },
              {validator: this.validateCode}
            ]
          },
```
3.找到rules里validator对应的方法在哪里,然后使用第一步中引入的排重校验接口.  
  以用户online表单编码为示例,其中四个必传的参数有:  
    
```
  {tableName:表名,fieldName:字段名,fieldVal:字段值,dataId:表的主键},
```
 具体使用代码如下:  

```
    validateCode(rule, value, callback){
        let pattern = /^[a-z|A-Z][a-z|A-Z|\d|_|-]{0,}$/;
        if(!pattern.test(value)){
          callback('编码必须以字母开头，可包含数字、下划线、横杠');
        } else {
          var params = {
            tableName: "onl_cgreport_head",
            fieldName: "code",
            fieldVal: value,
            dataId: this.model.id
          };
          duplicateCheck(params).then((res)=>{
            if(res.success){
             callback();
            }else{
              callback(res.message);
            }
          })
        }
      },
```


## docker镜像用法
  文档： http://doc.jeecg.com/2043889

 ``` 
注意： 如果本地安装了mysql和redis,启动容器前先停掉本地服务，不然会端口冲突。
       net stop redis
       net stop mysql
 
 # 1.配置host

    # jeecgboot
    127.0.0.1   jeecg-boot-redis
    127.0.0.1   jeecg-boot-mysql
    127.0.0.1   jeecg-boot-system
	
# 2.修改项目配置文件 application.yml
    active: dev
	
# 3.修改application-dev.yml文件的数据库和redis链接
	修改数据库连接和redis连接，将连接改成host方式

# 4.先进JAVA项目jeecg-boot根路径 maven打包
    mvn clean package
 

# 5.构建镜像__容器组（当你改变本地代码，也可重新构建镜像）
    docker-compose build


# 6.启动镜像__容器组（也可取代运行中的镜像）
    docker-compose up -d

# 7.访问后台项目（注意要开启swagger）
    http://localhost:8080/jeecg-boot/doc.html
 ```