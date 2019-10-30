package com.hlwxy.xu_boot2.system.controller;


import com.hlwxy.xu_boot2.common.utils.DateTool;
import com.hlwxy.xu_boot2.system.domain.*;
import com.hlwxy.xu_boot2.system.service.MonthlyPlanService;
import com.hlwxy.xu_boot2.system.service.MonthlySummaryService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 *
 * @date 2019-09-26 22:35:53
 */

@Controller
@RequestMapping("/system/monthlySummary")
public class MonthlySummaryController {
	@Resource
	private MonthlyPlanService monthlyPlanService;
	@Resource
	private MonthlySummaryService monthlySummaryService;
	@Resource
	private MonthlyController monthlyController;

	@ResponseBody
	@RequestMapping("/list")
	public Map<String,Object> list(Page page) {
		//查询列表数据
		Map<String,Object> map=new HashMap<>();
		//计算开始检索位置
		page.setPc(page.getPageSize()*(page.getPage()-1));
		Integer pp=monthlySummaryService.coun();
		List<MonthlySummaryExtendDO> monthlySummaryExtendDOS = monthlySummaryService.findSummarySummary(page);
		int total = monthlySummaryService.count();
	    map.put("monthlySummaryExtendDOS",monthlySummaryExtendDOS);
	    map.put("pp",pp);
	    map.put("total",total);
		return map;
	}

	//查询个人月总结
	@ResponseBody
	@RequestMapping("/getMonthlySummary")
	public Map<String,Object> getMonthlySummary() {
		Map<String,Object> map=new HashMap<>();
		try{
			PeopleDO people=getUser();
//			com.hlwxy.xu_boot2.system.domain.PeopleDO people=monthlyPlanService.getPeople(user);
			MonthlySummaryDO monthlySummaryDO = new MonthlySummaryDO();
			monthlySummaryDO.setId(people.getMonthlySummaryId());
			//查询数据
			MonthlySummaryExtendDO monthlySummaryExtendDO = monthlySummaryService.getMonthlySummaryById(monthlySummaryDO);
			map.put("monthlySummaryExtendDO",monthlySummaryExtendDO);
			map.put("code",0);
		}catch (Exception e){
			map.put("code",-1);
			map.put("msg","系统异常");
		}
		return map;
	}

	//根据计划id查询
	@ResponseBody
	@RequestMapping("/getMonthlySummaryById")
	//查询
	@RequiresPermissions("system:monthlySummary:edit")
	public Map<String,Object> getMonthlySummaryById(@RequestBody MonthlySummaryDO monthlySummaryDO) {
		Map<String,Object> map=new HashMap<>();
		try{
			//获取总结的人
//			PeopleDO people=monthlyPlanService.getPeopleByCode(peopleDO);
//			MonthlySummaryDO monthlySummaryDO = new MonthlySummaryDO();
//			monthlySummaryDO.setId(people.getMonthlySummaryId());
			//查询月总结数据
			MonthlySummaryExtendDO monthlySummaryExtendDO = monthlySummaryService.getMonthlySummaryById(monthlySummaryDO);
			map.put("monthlySummaryExtendDO",monthlySummaryExtendDO);
//			map.put("monthlyPlanExtendDo",monthlyPlanExtendDo);
			map.put("code",0);
		}catch (Exception e){
			map.put("code",-1);
			map.put("msg","系统异常");
		}
		return map;
	}

	//多条件组合查询
	@ResponseBody
	@RequestMapping("/compositeQueryMonthlySummary")
	public Map<String,Object> compositeQueryMonthlySummary(ConditionDO conditionDO) {
		Map<String,Object> map=new HashMap<>();
		String sss=conditionDO.getTime().replace('-','年');
		conditionDO.setTime(sss);
		//计算开始检索位置
		conditionDO.setPc(conditionDO.getPageSize()*(conditionDO.getPage()-1));
		try{
			//组合查询
			List<MonthlySummaryExtendDO> monthlySummaryExtendDOS = monthlySummaryService.compositeQueryMonthlySummary(conditionDO);
			Integer pp=monthlySummaryService.queryCount(conditionDO);
			map.put("monthlySummaryExtendDOS",monthlySummaryExtendDOS);
			map.put("pp",pp);
			map.put("code",0);
		}catch (Exception e){
			System.out.println(e.getMessage());
			map.put("code",-1);
			map.put("msg","请输入正确的查询条件");
		}
		return map;
	}

	//保存和提交
	@ResponseBody
	@RequestMapping("/addMonthlySummary")
	@RequiresPermissions("system:monthlySummary:add")
	public Map<String,Object> addMonthlySummary(@RequestBody MonthlySummaryDO monthlySummaryDO) {
		Map<String,Object> map=new HashMap<>();
		DateTool dateTool=new DateTool();
		try{
			PeopleDO people=getUser();
//			com.hlwxy.xu_boot2.system.domain.PeopleDO people=monthlyPlanService.getPeople(user);

			//判断是否编写过月计划
			if (people.getMonthlyPlanId()!=null&&!people.getMonthlyPlanId().equals("")){  //编写过
				MonthlyPlan monthlyPlan=new MonthlyPlan();
				monthlyPlan.setId(people.getMonthlyPlanId());
				MonthlyPlanExtendDo monthlyPlanExtendDo=monthlyPlanService.getMonthlyPlanById(monthlyPlan);
				if (monthlyPlanExtendDo.getMonthly_plan_time().equals(dateTool.years())) { //是本月的计划

					//判断该用户是否编写过月总结
					if (people.getMonthlySummaryId()!=null&&!people.getMonthlySummaryId().equals("")){//编写过，进行下一步判断
						monthlySummaryDO.setId(people.getMonthlySummaryId());
						MonthlySummaryExtendDO monthlySummaryExtendDO1=monthlySummaryService.getMonthlySummaryById(monthlySummaryDO);
						//判断是否为本月的月总结
						if (monthlySummaryExtendDO1.getMonthly_summary_time().equals(dateTool.years())){  //是本月的，修改
							MonthlySummaryExtendDO monthlySummaryExtendDO=new MonthlySummaryExtendDO();
							monthlySummaryExtendDO.setMonthlySummaryId(people.getMonthlySummaryId());
							monthlySummaryExtendDO.setMonthly_summary_content(monthlySummaryDO.getMonthly_summary_content());
							monthlySummaryExtendDO.setMonthly_summary_state(monthlySummaryDO.getMonthly_summary_state());
							monthlySummaryExtendDO.setMonthly_summary_entry_time(dateTool.adyAndDay());
							monthlySummaryService.updateMonthlySummaryContentByPeople(monthlySummaryExtendDO);
							map.put("msg","保存成功");
							map.put("code",0);
						}else {  //不是本月的，添加
							monthlySummaryDO.setMonthly_summary_code(String.valueOf(UUID.randomUUID()));
							monthlySummaryDO.setMonthly_summary_entry_time(dateTool.adyAndDay());
							monthlySummaryDO.setMonthly_summary_time(dateTool.years());
							monthlySummaryDO.setMonthly_summary_reply("否");//是否查看，默认为否
							monthlySummaryDO.setMonthly_summary_see("否");//是否回复，默认为否
							monthlySummaryDO.setPeo_id(people.getId());
							monthlySummaryService.addMonthlySummary(monthlySummaryDO);
							MonthlySummaryExtendDO monthlySummaryExtendDO=monthlySummaryService.getMonthlySummaryByCode(monthlySummaryDO.getMonthly_summary_code());
//							UserAndIdDO userAndIdDO=new UserAndIdDO();
//							userAndIdDO.setUserDO(user);
//							userAndIdDO.setId(monthlySummaryExtendDO.getId());
							people.setMonthlySummaryId(monthlySummaryExtendDO.getId());
							//修改人员月计划编码
							monthlySummaryService.updatePeopleByCode(people);
							map.put("code",0);
						}
					}else {
						monthlySummaryDO.setMonthly_summary_code(String.valueOf(UUID.randomUUID()));
						monthlySummaryDO.setMonthly_summary_entry_time(dateTool.adyAndDay());
						monthlySummaryDO.setMonthly_summary_time(dateTool.years());
						monthlySummaryDO.setMonthly_summary_reply("否");//是否查看，默认为否
						monthlySummaryDO.setMonthly_summary_see("否");//是否回复，默认为否
						monthlySummaryDO.setPeo_id(people.getId());
						monthlySummaryService.addMonthlySummary(monthlySummaryDO);
						MonthlySummaryExtendDO monthlySummaryExtendDO=monthlySummaryService.getMonthlySummaryByCode(monthlySummaryDO.getMonthly_summary_code());
//						UserAndIdDO userAndIdDO=new UserAndIdDO();
//						userAndIdDO.setUserDO(user);
//						userAndIdDO.setId(monthlySummaryExtendDO.getId());
						people.setMonthlySummaryId(monthlySummaryExtendDO.getId());
						//修改人员月计划编码
						monthlySummaryService.updatePeopleByCode(people);
						map.put("code",0);
					}

				}else {
					map.put("code",-1);
					map.put("msg","您未编写本月的计划，不可以编写本月的总结！");
				}
			}else {
				map.put("code",-1);
				map.put("msg","您未编写本月的计划，不可以编写本月的总结！");
			}

		}catch (Exception e){
			map.put("code",-1);
			map.put("msg","系统异常");
			System.out.println(e.getMessage());
		}
		return map;
	}

	//审核月总结
	@ResponseBody
	@RequestMapping("/updateMonthlySummaryStateById")
	@RequiresPermissions("system:monthlySummary:sh")
	public Map<String,Object> updateMonthlyPlanStateById(@RequestBody MonthlySummaryDO monthlySummaryDO) {
		Map<String,Object> map=new HashMap<>();
		try{
			monthlySummaryService.updateMonthlySummaryStateById(monthlySummaryDO);
			map.put("code",0);
		}catch (Exception e){
			map.put("code",-1);
			map.put("msg","系统异常");
		}
		return map;
	}

	private PeopleDO getUser(){
		PeopleDO user = (PeopleDO) SecurityUtils.getSubject().getPrincipal();
		PeopleDO peopleDO=monthlyPlanService.getPeopleById(user);
		return peopleDO;
	}



}
