package org.jeecg.modules.system.service.impl;

import java.util.*;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.vo.SmsMsgVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;



/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
@Service
@Slf4j
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {

	@Resource
	private SysAnnouncementMapper sysAnnouncementMapper;
	
	@Resource
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;

	@Resource
	private SysUserMapper sysUserMapper;

	@Autowired
	private SysBaseApiImpl sysBaseApi;

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	@Transactional
	@Override
	public void saveAnnouncement(SysAnnouncement sysAnnouncement) {
		String userId = sysAnnouncement.getUserIds();
		String departIds = sysAnnouncement.getDepartIds();
		String peopleType = sysAnnouncement.getPeopleType();
		Integer send_count;
		if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {

			QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("del_flag",0).isNotNull(true,"org_code");
			List<SysUser> allUserList = sysUserMapper.selectList(queryWrapper);

			send_count = allUserList.size();
//			send_count = sysBaseApi.queryAllUserBackCombo().size();
			sysAnnouncement.setSendCount(send_count);

			sysAnnouncementMapper.insert(sysAnnouncement);
//			List<ComboModel> allUserList = sysBaseApi.queryAllUserBackCombo();
			List<String> allUserIdList = new ArrayList<>();
			allUserList.forEach(item -> {
				allUserIdList.add(item.getId());
			});
//			for(ComboModel user : allUserList){
//				allUserIdList.add(user.getId());
//			}

			log.info(String.valueOf(allUserIdList));

			String anntId = sysAnnouncement.getId();
			List<SysAnnouncementSend> sysAnnouncementSendList = new ArrayList<>();
			for (String id : allUserIdList) {
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(id);
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				announcementSend.setIsDelay(0);
				sysAnnouncementSendList.add(announcementSend);
			}
			SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH.BATCH, false);
			sysAnnouncementSendMapper = sqlSession.getMapper(SysAnnouncementSendMapper.class);
			sysAnnouncementSendList.forEach(item -> {
				sysAnnouncementSendMapper.insert(item);
			});
			sqlSession.commit();

		} else if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_DEPART)){

			List<String> userList = new ArrayList<>();

			String[] departArray = departIds.split(",");
			for(String depart: departArray){
				userList.addAll(sysBaseApi.getDepAdminByDepId(depart));
			}

			sysAnnouncement.setUserIds(String.join(",",userList));
			send_count = userList.size();
			sysAnnouncement.setSendCount(send_count);
			log.info(String.valueOf(send_count));

			// 1.插入通告表记录
			sysAnnouncementMapper.insert(sysAnnouncement);
			// 2.插入用户通告阅读标记表记录
			List<SysAnnouncementSend> sysAnnouncementSendList = new ArrayList<>();

//			String[] userIdArray = new String[userIdList.size()];
//			for(int i = 0; i < userIdList.size(); i++){
//				userIdArray[i] = userIdList.get(i);
//			}

			String anntId = sysAnnouncement.getId();
			for (String s : userList) {
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(s);
//				announcementSend.setUserName(s);
//				announcementSend.setUserDepart(sysBaseApi.getDepartNamesByUsername(s).get(0));
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				announcementSend.setIsDelay(0);
				sysAnnouncementSendList.add(announcementSend);
			}
			SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH.BATCH, false);
			sysAnnouncementSendMapper = sqlSession.getMapper(SysAnnouncementSendMapper.class);
			sysAnnouncementSendList.forEach(item -> {
				sysAnnouncementSendMapper.insert(item);
			});
			sqlSession.commit();

		} else if(sysAnnouncement.getMsgType().equals("TYPE")){

			List<String> userList = new ArrayList<>();

			log.info(peopleType);

			if(StringUtils.isBlank(peopleType) || sysBaseApi.getUserIdsByTypes(peopleType).isEmpty() ) {
				throw new NullPointerException("无对应接收人员");
			} else {
//				log.info(String.valueOf(sysBaseApi.getUserIdsByTypes(peopleType)));

				sysBaseApi.getUserIdsByTypes(peopleType).forEach(user -> {
					log.info(String.valueOf(user));
					userList.add(user.getString("id"));
				});

				sysAnnouncement.setUserIds(String.join(",",userList));
				send_count = userList.size();
				sysAnnouncement.setSendCount(send_count);
				log.info(String.valueOf(send_count));

				// 1.插入通告表记录
				sysAnnouncementMapper.insert(sysAnnouncement);
				// 2.插入用户通告阅读标记表记录
				List<SysAnnouncementSend> sysAnnouncementSendList = new ArrayList<>();

//			String[] userIdArray = new String[userIdList.size()];
//			for(int i = 0; i < userIdList.size(); i++){
//				userIdArray[i] = userIdList.get(i);
//			}

				String anntId = sysAnnouncement.getId();
				for (String s : userList) {
					SysAnnouncementSend announcementSend = new SysAnnouncementSend();
					announcementSend.setAnntId(anntId);
					announcementSend.setUserId(s);
//				announcementSend.setUserName(s);
//				announcementSend.setUserDepart(sysBaseApi.getDepartNamesByUsername(s).get(0));
					announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
					announcementSend.setIsDelay(0);
					sysAnnouncementSendList.add(announcementSend);
				}
				SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH.BATCH, false);
				sysAnnouncementSendMapper = sqlSession.getMapper(SysAnnouncementSendMapper.class);
				sysAnnouncementSendList.forEach(item -> {
					sysAnnouncementSendMapper.insert(item);
				});
				sqlSession.commit();
			}

		} else {
			send_count = sysAnnouncement.getUserIds().split(",").length;
			sysAnnouncement.setSendCount(send_count);
			// 1.插入通告表记录
			sysAnnouncementMapper.insert(sysAnnouncement);
			// 2.插入用户通告阅读标记表记录
//			String userId = sysAnnouncement.getUserIds();
			String[] userIds = userId.split(",",-1);
			List<SysAnnouncementSend> sysAnnouncementSendList = new ArrayList<>();

			String anntId = sysAnnouncement.getId();
			for (String id : userIds) {
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
//				String userName = sysBaseApi.getUserById(id).getUsername();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(id);
//				announcementSend.setUserName(userName);
//				announcementSend.setUserDepart(sysBaseApi.getDepartNamesByUsername(userName).get(0));
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				announcementSend.setIsDelay(0);
				sysAnnouncementSendList.add(announcementSend);
			}
			SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH.BATCH, false);
			sysAnnouncementSendMapper = sqlSession.getMapper(SysAnnouncementSendMapper.class);
			sysAnnouncementSendList.forEach(item -> {
				sysAnnouncementSendMapper.insert(item);
			});
			sqlSession.commit();
		}
	}

	@Transactional
	@Override
	public void sendSmsMsg(SmsMsgVo smsMsgVo) {

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		String userId = smsMsgVo.getUserIds();
		String departIds = smsMsgVo.getDepartIds();
		String peopleType = smsMsgVo.getPeopleType();
		if(smsMsgVo.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {

			QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("del_flag",0).isNotNull(true,"org_code");
			List<SysUser> allUserList = sysUserMapper.selectList(queryWrapper);


			List<String> allUserNameList = new ArrayList<>();
			List<String> allUserPhone = new ArrayList<>();
			allUserList.forEach(item -> {
				allUserNameList.add(item.getUsername());
				allUserPhone.add(item.getPhone());
			});

			String receiver = String.join(",", allUserNameList);
			String receiverPhone = String.join(",", allUserPhone);

			DySmsHelper.sendSms(sysUser.getUsername(),smsMsgVo.getMsgType(),"测试",smsMsgVo.getContent(),receiver,receiverPhone);


		} else if(smsMsgVo.getMsgType().equals(CommonConstant.MSG_TYPE_DEPART)){

			List<String> userList = new ArrayList<>();

			String[] departArray = departIds.split(",");
			for(String depart: departArray){
				userList.addAll(sysBaseApi.getDepAdminByDepId(depart));
			}

			QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
			userQueryWrapper.eq("del_flag",0).in("id", userList);
			List<SysUser> allUserList = sysUserMapper.selectList(userQueryWrapper);

			List<String> userNameList = new ArrayList<>();
			List<String> userPhoneList = new ArrayList<>();

			allUserList.forEach(item -> {
				userNameList.add(item.getUsername());
				userPhoneList.add(item.getPhone());
			});

			String receiver = String.join(",", userNameList);
			String receiverPhone = String.join(",", userPhoneList);

			DySmsHelper.sendSms(sysUser.getUsername(),smsMsgVo.getMsgType(),"测试",smsMsgVo.getContent(),receiver,receiverPhone);


		} else if(smsMsgVo.getMsgType().equals("TYPE")){

			List<String> userList = new ArrayList<>();

			List<String> userNameList = new ArrayList<>();
			List<String> userPhoneList = new ArrayList<>();

			log.info(peopleType);

			if(StringUtils.isBlank(peopleType) || sysBaseApi.getUserIdsByTypes(peopleType).isEmpty() ) {
				throw new NullPointerException("无对应接收人员");
			} else {
//				log.info(String.valueOf(sysBaseApi.getUserIdsByTypes(peopleType)));

				sysBaseApi.getUserIdsByTypes(peopleType).forEach(user -> {
					log.info(String.valueOf(user));
					userNameList.add(user.getString("username"));
					userPhoneList.add(user.getString("phone"));
				});

				String receiver = String.join(",", userNameList);
				String receiverPhone = String.join(",", userPhoneList);

				DySmsHelper.sendSms(sysUser.getUsername(),smsMsgVo.getMsgType(),"测试",smsMsgVo.getContent(),receiver,receiverPhone);

			}

		} else {
//			send_count = sysAnnouncement.getUserIds().split(",").length;
//			sysAnnouncement.setSendCount(send_count);
			// 1.插入通告表记录
//			sysAnnouncementMapper.insert(sysAnnouncement);
			// 2.插入用户通告阅读标记表记录
//			String userId = sysAnnouncement.getUserIds();
			String[] userIds = userId.split(",",-1);

			QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
			userQueryWrapper.eq("del_flag",0).in("id", userIds);

			List<SysUser> allUserList = sysUserMapper.selectList(userQueryWrapper);

			List<String> userNameList = new ArrayList<>();
			List<String> userPhoneList = new ArrayList<>();

			allUserList.forEach(item -> {
				userNameList.add(item.getUsername());
				userPhoneList.add(item.getPhone());
			});

			String receiver = String.join(",", userNameList);
			String receiverPhone = String.join(",", userPhoneList);

			DySmsHelper.sendSms(sysUser.getUsername(),smsMsgVo.getMsgType(),"测试",smsMsgVo.getContent(),receiver,receiverPhone);

		}
	}
	
	/**
	 * @功能：编辑消息信息
	 */
	@Transactional
	@Override
	public boolean upDateAnnouncement(SysAnnouncement sysAnnouncement) {
		// 1.更新系统信息表数据
		sysAnnouncementMapper.updateById(sysAnnouncement);
		String userId = sysAnnouncement.getUserIds();
		if(oConvertUtils.isNotEmpty(userId)&&sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_UESR)) {
			// 2.补充新的通知用户数据
			String[] userIds = userId.substring(0, (userId.length()-1)).split(",");
			String anntId = sysAnnouncement.getId();
			for(int i=0;i<userIds.length;i++) {
				LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
				queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
				queryWrapper.eq(SysAnnouncementSend::getUserId, userIds[i]);
				List<SysAnnouncementSend> announcementSends=sysAnnouncementSendMapper.selectList(queryWrapper);
				if(announcementSends.size()<=0) {
					SysAnnouncementSend announcementSend = new SysAnnouncementSend();
					announcementSend.setAnntId(anntId);
					announcementSend.setUserId(userIds[i]);
					announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
					announcementSend.setIsDelay(0);
					sysAnnouncementSendMapper.insert(announcementSend);
				}
			}
			// 3. 删除多余通知用户数据
			Collection<String> delUserIds = Arrays.asList(userIds);
			LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
			queryWrapper.notIn(SysAnnouncementSend::getUserId, delUserIds);
			queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
			sysAnnouncementSendMapper.delete(queryWrapper);
		}
		return true;
	}

	// @功能：流程执行完成保存消息通知
	@Override
	public void saveSysAnnouncement(String title, String msgContent) {
		SysAnnouncement announcement = new SysAnnouncement();
		announcement.setTitile(title);
		announcement.setMsgContent(msgContent);
		announcement.setSender("JEECG BOOT");
		announcement.setPriority(CommonConstant.PRIORITY_L);
		announcement.setMsgType(CommonConstant.MSG_TYPE_ALL);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
		sysAnnouncementMapper.insert(announcement);
	}

	@Override
	public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId,String msgCategory) {
		 return page.setRecords(sysAnnouncementMapper.querySysCementListByUserId(page, userId, msgCategory));
	}

}
