package org.jeecg.modules.test.testDict.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.test.testDict.service.ITestDictService;
import org.jeecg.modules.test.testDict.entity.TestDict;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 字典测试
 * @Author: jeecg-boot
 * @Date:   2021-11-15
 * @Version: V1.0
 */
@Api(tags="字典测试")
@RestController
@RequestMapping("/testDict/testDict")
@Slf4j
public class TestDictController extends JeecgController<TestDict, ITestDictService> {
	@Autowired
	private ITestDictService testDictService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testDict
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "字典测试-分页列表查询")
	@ApiOperation(value="字典测试-分页列表查询", notes="字典测试-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TestDict testDict,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestDict> queryWrapper = QueryGenerator.initQueryWrapper(testDict, req.getParameterMap());
		Page<TestDict> page = new Page<TestDict>(pageNo, pageSize);
		IPage<TestDict> pageList = testDictService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testDict
	 * @return
	 */
	@AutoLog(value = "字典测试-添加")
	@ApiOperation(value="字典测试-添加", notes="字典测试-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TestDict testDict) {
		testDictService.save(testDict);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testDict
	 * @return
	 */
	@AutoLog(value = "字典测试-编辑")
	@ApiOperation(value="字典测试-编辑", notes="字典测试-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TestDict testDict) {
		testDictService.updateById(testDict);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "字典测试-通过id删除")
	@ApiOperation(value="字典测试-通过id删除", notes="字典测试-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		testDictService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "字典测试-批量删除")
	@ApiOperation(value="字典测试-批量删除", notes="字典测试-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testDictService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "字典测试-通过id查询")
	@ApiOperation(value="字典测试-通过id查询", notes="字典测试-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TestDict testDict = testDictService.getById(id);
		if(testDict==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(testDict);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testDict
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestDict testDict) {
        return super.exportXls(request, testDict, TestDict.class, "字典测试");
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
        return super.importExcel(request, response, TestDict.class);
    }

}
