package org.jeecg.modules.smart_window_unit.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import org.jeecg.modules.smart_window_people.service.ISmartWindowPeopleService;
import org.jeecg.modules.smart_window_unit.api.response.BaseResponse;
import org.jeecg.modules.smart_window_unit.api.response.StatusCode;
import org.jeecg.modules.smart_window_unit.entity.QRCodeUtil;
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.smart_window_unit.service.ISmartWindowUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;



class BaseController {

	protected static Logger log= LoggerFactory.getLogger(BaseController.class);

}



 /**
 * @Description: 窗口单位
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
@Api(tags="窗口单位")
@RestController
@RequestMapping("/smart_window_unit/smartWindowUnit")
@Slf4j
public class SmartWindowUnitController<ISysDepartService> extends JeecgController<SmartWindowUnit, ISmartWindowUnitService> {
	@Autowired
	private ISmartWindowUnitService smartWindowUnitService;
	private ISmartWindowPeopleService smartWindowPersonService;
	private ISysBaseAPI sysBaseAPI;

	private ISysDepartService sysDepartService;

	/**
	 * 分页列表查询
	 *
	 * @param smartWindowUnit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "窗口单位-分页列表查询")
	@ApiOperation(value="窗口单位-分页列表查询", notes="窗口单位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartWindowUnit smartWindowUnit,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartWindowUnit> queryWrapper = QueryGenerator.initQueryWrapper(smartWindowUnit, req.getParameterMap());
		Page<SmartWindowUnit> page = new Page<SmartWindowUnit>(pageNo, pageSize);
		IPage<SmartWindowUnit> pageList = smartWindowUnitService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加,并直接生成二维码
	 *
	 * @param smartWindowUnit
	 * @return
	 */

	private static final String RootPath="${path.upload}/windows";
	private static final String FileFormat=".png";
	private static final ThreadLocal<SimpleDateFormat> LOCALDATEFORMAT=ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));

	@AutoLog(value = "窗口单位-添加")
	@ApiOperation(value="窗口单位-添加", notes="窗口单位-添加")
	@PostMapping(value = "/add")
	public Object add(@RequestBody SmartWindowUnit smartWindowUnit) {
//		String peopleId = smartWindowUnit.getPeople();
//		smartWindowUnit.setPeople(sysBaseAPI.getUserById(peopleId).getRealname());

//		String principalId = smartWindowUnit.getPrincipal();
//		smartWindowUnit.setPrincipal(sysBaseAPI.getUserById(principalId).getRealname());

		String principalId = smartWindowUnit.getPrincipal();
		smartWindowUnit.setPrincipal(smartWindowUnitService.getUserNameById(principalId));
		String pid = smartWindowUnit.getPid();
		String departName = smartWindowUnitService.getDepartNameById(pid);
//		String windowUnitPid = smartWindowUnitService.getById(departName).getPid();
//		sysBaseAPI.getParentDepartId()
		// 1. 根据ID生成二维码，并存储到本地
		String content = "http://192.168.1.100/:3000/SmartEvaluate/modules/SmartEvaluateForm?exeDept="+departName+"&windowsName="+smartWindowUnit.getName()+"&personName=大厅";//exeDept主管部门名称，windowsName窗口名称，personName具体被举报人名，可删除留空判断
		BaseResponse response=new BaseResponse(StatusCode.Success);
		try {
			final String fileName=LOCALDATEFORMAT.get().format(new Date());
			QRCodeUtil.createCodeToFile(content,new File(RootPath),fileName+FileFormat);


			log.info(fileName);
			// 2. 将存储路径保存到 smartWindowsUnit
			smartWindowUnit.setQrcode("windows/"+fileName+FileFormat);
			smartWindowUnitService.save(smartWindowUnit);
			// 3. 调用  edit() 更新数据
			edit(smartWindowUnit);

			return Result.OK("添加成功！");
		}catch (Exception e){
			response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
		}
		return response;
	}
	
	/**
	 *  编辑
	 *
	 * @param smartWindowUnit
	 * @return
	 */
	@AutoLog(value = "窗口单位-编辑")
	@ApiOperation(value="窗口单位-编辑", notes="窗口单位-编辑")
	@PutMapping(value = "/edit")
	public Object edit(@RequestBody SmartWindowUnit smartWindowUnit) {
		smartWindowUnitService.updateById(smartWindowUnit);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "窗口单位-通过id删除")
	@ApiOperation(value="窗口单位-通过id删除", notes="窗口单位-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartWindowUnitService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "窗口单位-批量删除")
	@ApiOperation(value="窗口单位-批量删除", notes="窗口单位-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartWindowUnitService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "窗口单位-通过id查询")
	@ApiOperation(value="窗口单位-通过id查询", notes="窗口单位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartWindowUnit smartWindowUnit = smartWindowUnitService.getById(id);
		if(smartWindowUnit==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartWindowUnit);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartWindowUnit
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartWindowUnit smartWindowUnit) {
        return super.exportXls(request, smartWindowUnit, SmartWindowUnit.class, "窗口单位");
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
        return super.importExcel(request, response, SmartWindowUnit.class);
    }

}
