package org.jeecg.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.demo.test.entity.Cloud;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.entity.SysDepart;
import org.jeecg.modules.demo.test.entity.partyUser;
import org.jeecg.modules.demo.test.mapper.JeecgDemoMapper;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.jeecg.modules.demo.test.vo.DepartIdModel;
import org.jeecg.modules.demo.test.vo.SysDepartTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
public class JeecgDemoServiceImpl extends ServiceImpl<JeecgDemoMapper, JeecgDemo> implements IJeecgDemoService {

	@Autowired
	JeecgDemoMapper jeecgDemoMapper;
	
	/**
	 * 事务控制在service层面
	 * 加上注解：@Transactional，声明的方法就是一个独立的事务（有异常DB操作全部回滚）
	 */
	@Override
	@Transactional
	public void testTran() {
		JeecgDemo pp = new JeecgDemo();
		pp.setAge(1111);
		pp.setName("测试事务  小白兔 1");
		jeecgDemoMapper.insert(pp);
		
		JeecgDemo pp2 = new JeecgDemo();
		pp2.setAge(2222);
		pp2.setName("测试事务  小白兔 2");
		jeecgDemoMapper.insert(pp2);
		
		Integer.parseInt("hello");//自定义异常
		
		JeecgDemo pp3 = new JeecgDemo();
		pp3.setAge(3333);
		pp3.setName("测试事务  小白兔 3");
		jeecgDemoMapper.insert(pp3);
		return ;
	}


	/**
	 * 缓存注解测试： redis
	 */
	@Override
	@Cacheable(cacheNames = CacheConstant.TEST_DEMO_CACHE, key = "#id")
	public JeecgDemo getByIdCacheable(String id) {
		JeecgDemo t = jeecgDemoMapper.selectById(id);
		System.err.println("---未读缓存，读取数据库---");
		System.err.println(t);
		return t;
	}


	@Override
	public IPage<JeecgDemo> queryListWithPermission(int pageSize,int pageNo) {
		Page<JeecgDemo> page = new Page<>(pageNo, pageSize);
		//编程方式，获取当前请求的数据权限规则SQL片段
		String sql = QueryGenerator.installAuthJdbc(JeecgDemo.class);
		return this.baseMapper.queryListWithPermission(page, sql);
	}

	@Override
	public String getExportFields() {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		//权限配置列导出示例
		//1.配置前缀与菜单中配置的列前缀一致
		List<String> noAuthList = new ArrayList<>();
		List<String> exportFieldsList = new ArrayList<>();
		String permsPrefix = "testdemo:";
		//查询配置菜单有效字段
		List<String> allAuth = this.jeecgDemoMapper.queryAllAuth(permsPrefix);
		//查询已授权字段
		List<String> userAuth = this.jeecgDemoMapper.queryUserAuth(sysUser.getId(),permsPrefix);
		//列出未授权字段
		for(String perms : allAuth){
			if(!userAuth.contains(perms)){
				noAuthList.add(perms.substring(permsPrefix.length()));
			}
		}
		//实体类中字段与未授权字段比较，列出需导出字段
		Field[] fileds = JeecgDemo.class.getDeclaredFields();
		List<Field> list = new ArrayList(Arrays.asList(fileds));
		for(Field field : list){
			if(!noAuthList.contains(field.getName())){
				exportFieldsList.add(field.getName());
			}
		}
		return exportFieldsList != null && exportFieldsList.size()>0 ? String.join(",", exportFieldsList) : "";
	}

	@Override
	public String getYushuZ(){
		return this.jeecgDemoMapper.getTopicCount("榆树镇");
	}

	@Override
	public String getXinnongZ(){
		return this.jeecgDemoMapper.getTopicCount("新农镇");
	}

	@Override
	public String getXinfaZ(){
		return this.jeecgDemoMapper.getTopicCount("新发镇");
	}

	@Override
	public String getTaipingZ(){
		return this.jeecgDemoMapper.getTopicCount("太平镇");
	}

	@Override
	public String getTongzhi(){
		return this.jeecgDemoMapper.getMessageCount("1");
	}
	@Override
	public String getLianzheng(){
		return this.jeecgDemoMapper.getMessageCount("2");
	}

	@Override
	public String getRenwu(){
		return this.jeecgDemoMapper.getMessageCount("3");
	}

	@Override
	public String getShenhe(){
		return this.jeecgDemoMapper.getShenhe();
	}

	@Override
	public String getTongzhiyidu(){
		return this.jeecgDemoMapper.getTongzhiyidu();
	}

	@Override
	public String getTongzhiweidu(){
		return this.jeecgDemoMapper.getTongzhiweidu();
	}

	@Override
	public String getLianzhengyidu(){
		return this.jeecgDemoMapper.getLianzhengyidu();
	}

	@Override
	public String getLianzhengweidu(){
		return this.jeecgDemoMapper.getLianzhengweidu();
	}

	@Override
	public String getRenwutiao(){
		return this.jeecgDemoMapper.getRenwutiao();
	}

	@Override
	public String getDaishenhe(){
		return this.jeecgDemoMapper.getDaishenhe();
	}

	@Override
	public String getYishenhe(){
		return this.jeecgDemoMapper.getYishenhe();
	}

	@Override
	public List<partyUser> getCloudData(){
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("-MM-dd");
		String partyDate = formatter.format(date).toString();
System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
System.out.println(this.jeecgDemoMapper.getCloudData(partyDate));
		return this.jeecgDemoMapper.getCloudData(partyDate);
	}

}
