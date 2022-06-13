package org.jeecg.modules.smart_reception.service.impl;

import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.jeecg.modules.smart_reception.entity.Smart_8Dining;
import org.jeecg.modules.smart_reception.mapper.Smart_8DiningMapper;
import org.jeecg.modules.smart_reception.service.ISmart_8DiningService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 用餐情况
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Service
public class Smart_8DiningServiceImpl extends ServiceImpl<Smart_8DiningMapper, Smart_8Dining> implements ISmart_8DiningService {

	@Autowired
	private Smart_8DiningMapper smart_8DiningMapper;
	
	@Override
	public List<Smart_8Dining> selectByMainId(String mainId) {
		return smart_8DiningMapper.selectByMainId(mainId);
	}

	@Override
	public List<Smart_8Dining> sendInformation() {
		return smart_8DiningMapper.sendInformation();
	}

	@Override
	public List<SysRole> getUser(){
		return smart_8DiningMapper.getUser();
	}

	@Override
	public String getDepartmentIdBymainId(String mainId){
		return smart_8DiningMapper.getDepartmentIdBymainId(mainId);
	}

	@Override
	public String getReceptionDepBymainId(String mainId){
		return smart_8DiningMapper.getReceptionDepBymainId(mainId);
	}

	@Override
	public String getDepartmentNameById(String departmentId){
		return smart_8DiningMapper.getDepartmentNameById(departmentId);
	}

	@Override
	public 	String getUserNameByUsername(String username){
		return smart_8DiningMapper.getUserNameByUsername(username);
	}

	@Override
	public  String getMainIdById(String id){
		return smart_8DiningMapper.getMainIdById(id);
	}


}
