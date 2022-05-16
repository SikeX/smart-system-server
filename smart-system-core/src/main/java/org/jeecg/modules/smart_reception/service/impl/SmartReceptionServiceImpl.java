package org.jeecg.modules.smart_reception.service.impl;

import org.jeecg.modules.smart_reception.entity.SmartReception;
import org.jeecg.modules.smart_reception.entity.Smart_8Visitor;
import org.jeecg.modules.smart_reception.entity.Smart_8Stay;
import org.jeecg.modules.smart_reception.entity.Smart_8Dining;
import org.jeecg.modules.smart_reception.entity.Smart_8List;
import org.jeecg.modules.smart_reception.mapper.Smart_8VisitorMapper;
import org.jeecg.modules.smart_reception.mapper.Smart_8StayMapper;
import org.jeecg.modules.smart_reception.mapper.Smart_8DiningMapper;
import org.jeecg.modules.smart_reception.mapper.Smart_8ListMapper;
import org.jeecg.modules.smart_reception.mapper.SmartReceptionMapper;
import org.jeecg.modules.smart_reception.service.ISmartReceptionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 公务接待2.0
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
@Service
public class SmartReceptionServiceImpl extends ServiceImpl<SmartReceptionMapper, SmartReception> implements ISmartReceptionService {

	@Autowired
	private SmartReceptionMapper smartReceptionMapper;
	@Autowired
	private Smart_8VisitorMapper smart_8VisitorMapper;
	@Autowired
	private Smart_8StayMapper smart_8StayMapper;
	@Autowired
	private Smart_8DiningMapper smart_8DiningMapper;
	@Autowired
	private Smart_8ListMapper smart_8ListMapper;

	@Override
	public String getDepartIdByOrgCode(String orgCode) {
		return smartReceptionMapper.getDepartIdByOrgCode(orgCode);
	}

	@Override
	public List<String> getChildrenIdByOrgCode(String orgCode) {
		return smartReceptionMapper.getChildrenIdByOrgCode(orgCode);
	}

	@Override
	@Transactional
	public void delMain(String id) {
		smart_8VisitorMapper.deleteByMainId(id);
		smart_8StayMapper.deleteByMainId(id);
		smart_8DiningMapper.deleteByMainId(id);
		smart_8ListMapper.deleteByMainId(id);
		smartReceptionMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			smart_8VisitorMapper.deleteByMainId(id.toString());
			smart_8StayMapper.deleteByMainId(id.toString());
			smart_8DiningMapper.deleteByMainId(id.toString());
			smart_8ListMapper.deleteByMainId(id.toString());
			smartReceptionMapper.deleteById(id);
		}
	}

	
}
