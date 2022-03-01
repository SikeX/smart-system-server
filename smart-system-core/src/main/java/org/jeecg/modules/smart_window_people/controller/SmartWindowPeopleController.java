package org.jeecg.modules.smart_window_people.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.smart_window_people.api.response.BaseResponse;
import org.jeecg.modules.smart_window_people.api.response.StatusCode;
import org.jeecg.modules.smart_window_people.entity.QRCodeUtil;
import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import org.jeecg.modules.smart_window_people.service.ISmartWindowPeopleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;


class BaseController {

	protected static Logger log= LoggerFactory.getLogger(BaseController.class);

}


 /**
 * @Description: 窗口人员管理
 * @Author: jeecg-boot
 * @Date:   2021-12-02
 * @Version: V1.0
 */
@Api(tags="窗口人员管理")
@RestController
@RequestMapping("/smart_window_people/smartWindowPeople")
@Slf4j
public class SmartWindowPeopleController extends JeecgController<SmartWindowPeople, ISmartWindowPeopleService> {
	@Autowired
	private ISmartWindowPeopleService smartWindowPeopleService;

	@Autowired
	private ISysBaseAPI sysBaseAPI;


	/**
	 * 分页列表查询
	 *
	 * @param smartWindowPeople
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "窗口人员管理-分页列表查询")
	@ApiOperation(value="窗口人员管理-分页列表查询", notes="窗口人员管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartWindowPeople smartWindowPeople,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartWindowPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartWindowPeople, req.getParameterMap());
		Page<SmartWindowPeople> page = new Page<SmartWindowPeople>(pageNo, pageSize);
		IPage<SmartWindowPeople> pageList = smartWindowPeopleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加,并直接生成二维码
	 *
	 * @param smartWindowPeople
	 * @return
	 */

	@Value(value = "${jeecg.path.upload}/people")
	private String RootPath;
	private static final String FileFormat=".png";
	private static final ThreadLocal<SimpleDateFormat> LOCALDATEFORMAT=ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));

	@AutoLog(value = "窗口人员管理-添加")
	@ApiOperation(value="窗口人员管理-添加", notes="窗口人员管理-添加")
	@PostMapping(value = "/add")
	public Object add(@RequestBody SmartWindowPeople smartWindowPeople) {
		String peopleId = smartWindowPeople.getPrincipal();
		String principalId = smartWindowPeople.getPersonId();
//		String windowsName = smartWindowPeople.getDepartmentId();
////		String windowsName = smartWindowPeople.get();
//		smartWindowPeople.setPersonId(sysBaseAPI.getUserById(peopleId).getRealname());
		smartWindowPeople.setPrincipal(sysBaseAPI.getUserById(principalId).getRealname());
//		smartWindowUnitService.addSmartWindowUnit(smartWindowUnit);
		String departmentId = smartWindowPeople.getDepartmentId();
		String pid = smartWindowPeopleService.getPidByDepartmentId(departmentId);
		String departName = smartWindowPeopleService.getDepartNameById(pid);
		String windowName = smartWindowPeopleService.getDepartmentNameByDepartmentId(departmentId);
		// 1. 根据ID生成二维码，并存储到本地
		//String content = "http://47.99.39.59:3000/SmartEvaluate/modules/SmartEvaluateForm?exeDept="+departName+"&windowsName="+windowName+"&personName="+smartWindowPeople.getPersonName();//exeDept主管部门名称，windowsName窗口名称，personName具体被举报人名

		String content = "http://47.99.39.59:3000/SmartEvaluate/modules/SmartEvaluateForm?" +
				"exeDeptId="+pid+"&exeDept="+departName+
				"&windowsId="+departmentId+"&windowsName="+windowName+
				"&personId="+smartWindowPeople.getPersonId()+"&personName="+smartWindowPeople.getPersonName();//exeDept主管部门名称，windowsName窗口名称，personName具体被举报人名，可删除留空判断

		System.out.println("@@@@@@@@@@@@@@@@@@@@@");
		System.out.println(content);
		BaseResponse response = new BaseResponse(StatusCode.Success);
		try {
			final String fileName=LOCALDATEFORMAT.get().format(new Date());
			QRCodeUtil.createCodeToFile(content,new File(RootPath),fileName+FileFormat);


			log.info(fileName);
			// 2. 将存储路径保存到 smartWindowsUnit
			smartWindowPeople.setQrcode("people/"+fileName+FileFormat);
			smartWindowPeopleService.save(smartWindowPeople);
			// 3. 调用  edit() 更新数据
			edit(smartWindowPeople);

			return Result.OK("添加成功！");
		}catch (Exception e){
			response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
		}
		return response;

	}
	
	/**
	 *  编辑
	 *
	 * @param smartWindowPeople
	 * @return
	 */
	@AutoLog(value = "窗口人员管理-编辑")
	@ApiOperation(value="窗口人员管理-编辑", notes="窗口人员管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartWindowPeople smartWindowPeople) {
		smartWindowPeopleService.updateById(smartWindowPeople);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "窗口人员管理-通过id删除")
	@ApiOperation(value="窗口人员管理-通过id删除", notes="窗口人员管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartWindowPeopleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "窗口人员管理-批量删除")
	@ApiOperation(value="窗口人员管理-批量删除", notes="窗口人员管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartWindowPeopleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "窗口人员管理-通过id查询")
	@ApiOperation(value="窗口人员管理-通过id查询", notes="窗口人员管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartWindowPeople smartWindowPeople = smartWindowPeopleService.getById(id);
		if(smartWindowPeople==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartWindowPeople);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartWindowPeople
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartWindowPeople smartWindowPeople) {
        return super.exportXls(request, smartWindowPeople, SmartWindowPeople.class, "窗口人员管理");
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
        return super.importExcel(request, response, SmartWindowPeople.class);
    }

}
