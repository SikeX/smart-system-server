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

	public List<SysDepart> shunList = new ArrayList<>();


	public void getChildren(List<SysDepart> zancunList, List<SysDepart> list){
		if(zancunList == null || zancunList.size() == 0)
		{
			System.out.println(shunList);
			return;}
		 List<SysDepart> newZancun = new ArrayList<>();
		for(int i = 0; i < zancunList.size(); i++)
		{
			SysDepart oneZancun = zancunList.get(i);
			System.out.println(list);
			for(int j = 0; j < list.size(); j++)
			{
				SysDepart oneDepart = list.get(j);
				if(oneZancun.getId().equals(oneDepart.getBusinessParentId()))
				{
					shunList.add(oneDepart);
					newZancun.add(oneDepart);
				}
			}
			System.out.println(shunList);
		}
		getChildren(newZancun,list);
	}

	public void getWorkChildren(List<SysDepart> xuList,List<SysDepart> zancunList,List<SysDepart> list){
		if(zancunList == null || zancunList.size()==0)
		{return;}
		 List<SysDepart> newZancun = new ArrayList<>();
			for(int i = 0; i < zancunList.size(); i++)
			{
				SysDepart oneZancun = zancunList.get(i);
				for(int j = 0; j < list.size(); j++)
				{
					SysDepart oneDepart = list.get(j);
					if(oneZancun.getId().equals(oneDepart.getParentId()))
					{
						xuList.add(oneDepart);
						newZancun.add(oneDepart);
					}
				}
			}
		getWorkChildren(xuList,newZancun,list);
	}

	@Override
	public List<SysDepartTreeModel> getNaturalTree(){
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);
//		shunList.clear();
		//		List<SysDepart> shunList = new ArrayList<>();
		List<SysDepart> zancunList = new ArrayList<>();
//		List<SysDepart> lastList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SysDepart branch = list.get(i);
			if(branch.getDepartName().equals("部门类型"))
			{
				shunList.add(branch);
				zancunList.add(branch);
			}
			}
		getChildren(zancunList,list);
		System.out.println("00000000000000000000000000000000");
		System.out.println(shunList);
		List<SysDepartTreeModel> naturalTree = new ArrayList<>();
		for (int i = 0; i < shunList.size(); i++) {
			SysDepart aSysNode = shunList.get(i);
			SysDepartTreeModel treeModel = new SysDepartTreeModel(aSysNode);
			naturalTree.add(treeModel);
		}
		shunList.clear();
		return naturalTree;
	}

	@Override
	public List<DepartIdModel> getWorkTree(){
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
//		query.ne(SysDepart::getParentId, null);
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);

		SysDepart daoli = this.jeecgDemoTwoMapper.getDaoli();
		List<SysDepart> xuList = new ArrayList<>();
		List<SysDepart> zancunList = new ArrayList<>();
		xuList.add(daoli);
		zancunList.add(daoli);
		getWorkChildren(xuList,zancunList,list);
        System.out.println("11111111111111111111111111111111111111");
        System.out.println(xuList);
		List<DepartIdModel> workTree = new ArrayList<>();
		for (int i = 0; i < xuList.size(); i++) {
			SysDepart branch = xuList.get(i);
			DepartIdModel departIdModel = new DepartIdModel(branch);
			workTree.add(departIdModel);
		}
		return workTree;
	}

}
