package org.jeecg.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.entity.SysDepart;
import org.jeecg.modules.demo.test.entity.partyUser;
import org.jeecg.modules.demo.test.mapper.JeecgDemoMapper;
import org.jeecg.modules.demo.test.mapper.JeecgDemoTwoMapper;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.jeecg.modules.demo.test.service.IJeecgDemoServiceTwo;
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
public class JeecgDemoServiceImplTwo extends ServiceImpl<JeecgDemoTwoMapper, SysDepart> implements IJeecgDemoServiceTwo {

	@Autowired
	JeecgDemoTwoMapper jeecgDemoTwoMapper;


	@Override
	public List<SysDepartTreeModel> getNaturalTree(){
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
//		query.ne(SysDepart::getParentId, null);
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);
		List<SysDepartTreeModel> naturalTree = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysDepart branch = list.get(i);
			SysDepartTreeModel sysDepartTreeModel = new SysDepartTreeModel(branch);
			naturalTree.add(sysDepartTreeModel);
			}

		return naturalTree;
	}

	@Override
	public List<DepartIdModel> getWorkTree(){
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		query.ne(SysDepart::getParentId, null);
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);
		List<DepartIdModel> workTree = new ArrayList<>();
		SysDepart daoli = this.jeecgDemoTwoMapper.getDaoli();
		DepartIdModel daoliModel = new DepartIdModel(daoli);
		workTree.add(daoliModel);
		for (int i = 0; i < list.size(); i++) {
			SysDepart branch = list.get(i);
			DepartIdModel departIdModel = new DepartIdModel(branch);
			workTree.add(departIdModel);
		}
		return workTree;
	}

}
