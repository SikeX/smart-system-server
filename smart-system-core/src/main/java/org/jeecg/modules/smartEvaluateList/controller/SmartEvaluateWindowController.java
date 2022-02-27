package org.jeecg.modules.smartEvaluateList.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.smartEvaluateList.entity.SmartEvaluateWindow;
import org.jeecg.modules.smartEvaluateList.service.ISmartEvaluateWindowService;
import org.jeecg.modules.smartSentMsg.entity.SmartSentMsg;
import org.jeecg.modules.smartSentMsg.service.ISmartSentMsgService;
import org.jeecg.modules.smart_window_unit.service.ISmartWindowUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 阳光评廉表
 * @Author: jeecg-boot
 * @Date:   2021-11-09
 * @Version: V1.0
 */
@Api(tags="阳光评廉表")
@RestController
@RequestMapping("/smartEvaluateList/smartEvaluateWindow")
@Slf4j
public class SmartEvaluateWindowController extends JeecgController<SmartEvaluateWindow, ISmartEvaluateWindowService> {
	@Autowired
	private ISmartEvaluateWindowService smartEvaluateWindowService;
	@Autowired
	private ISmartWindowUnitService smartWindowUnitService;
	@Autowired
	private ISmartSentMsgService smartSentMsgService;


	/**
	 * 分页列表查询
	 *
	 * @param smartEvaluateWindow
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "阳光评廉表-分页列表查询")
	@ApiOperation(value="阳光评廉表-分页列表查询", notes="阳光评廉表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartEvaluateWindow smartEvaluateWindow,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartEvaluateWindow> queryWrapper = QueryGenerator.initQueryWrapper(smartEvaluateWindow, req.getParameterMap());
		Page<SmartEvaluateWindow> page = new Page<SmartEvaluateWindow>(pageNo, pageSize);
		IPage<SmartEvaluateWindow> pageList = smartEvaluateWindowService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartEvaluateWindow
	 * @return
	 */
	@AutoLog(value = "阳光评廉表-添加")
	@ApiOperation(value="阳光评廉表-添加", notes="阳光评廉表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartEvaluateWindow smartEvaluateWindow) {
		smartEvaluateWindowService.save(smartEvaluateWindow);
		//不满意给负责人发送短信
		String evaluate = smartEvaluateWindow.getEvaluateResult();
		System.out.println(".................");
		System.out.println(smartEvaluateWindow);
		System.out.println(evaluate);
		if(evaluate.equals("2")){
			System.out.println("发送短信.................");
			String windowsId = smartEvaluateWindow.getWindowsId();
			String windowsName = smartEvaluateWindow.getWindowsName();
			String content = "您负责的"+windowsName+"收到不满意评价！";
            System.out.println(smartWindowUnitService.getById(windowsId));

			String receiverPhone = (smartWindowUnitService.getById(windowsId)).getPhone();
			DySmsHelper.sendSms(content, receiverPhone);
			//保存发送记录
			SmartSentMsg smartSentMsg = new SmartSentMsg();
			smartSentMsg.setContent(content);
			smartSentMsg.setReceiverPhone(receiverPhone);
			smartSentMsgService.save(smartSentMsg);
		}
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartEvaluateWindow
	 * @return
	 */
	@AutoLog(value = "阳光评廉表-编辑")
	@ApiOperation(value="阳光评廉表-编辑", notes="阳光评廉表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartEvaluateWindow smartEvaluateWindow) {
		smartEvaluateWindowService.updateById(smartEvaluateWindow);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "阳光评廉表-通过id删除")
	@ApiOperation(value="阳光评廉表-通过id删除", notes="阳光评廉表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartEvaluateWindowService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "阳光评廉表-批量删除")
	@ApiOperation(value="阳光评廉表-批量删除", notes="阳光评廉表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartEvaluateWindowService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "阳光评廉表-通过id查询")
	@ApiOperation(value="阳光评廉表-通过id查询", notes="阳光评廉表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartEvaluateWindow smartEvaluateWindow = smartEvaluateWindowService.getById(id);
		if(smartEvaluateWindow==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartEvaluateWindow);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartEvaluateWindow
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartEvaluateWindow smartEvaluateWindow) {
        return super.exportXls(request, smartEvaluateWindow, SmartEvaluateWindow.class, "阳光评廉表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartEvaluateWindow.class);
    }

}
