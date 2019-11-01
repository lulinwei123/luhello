package com.hlwxy.xu_boot2.system.controller;


import com.hlwxy.xu_boot2.common.utils.DateTool;
import com.hlwxy.xu_boot2.system.domain.*;
import com.hlwxy.xu_boot2.system.service.MonthlyPlanService;
import com.hlwxy.xu_boot2.system.service.WeekPlanService;
import com.hlwxy.xu_boot2.system.service.WeekSummaryService;
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
@RequestMapping("/system/weekSummary")
public class WeekSummaryController {
	@Resource
	private MonthlyPlanService monthlyPlanService;
	@Resource
	private WeekSummaryService weekSummaryService;
	@Resource
	private WeekPlanService weekPlanService;

	@ResponseBody
	@RequestMapping("/list")
	public Map<String,Object> list(Page page) {
		//查询列表数据
		Map<String,Object> map=new HashMap<>();
		//计算开始检索位置
		page.setPc(page.getPageSize()*(page.getPage()-1));
		Integer pp=weekSummaryService.coun();
		List<WeekSummaryExtendDO> weekSummaryExtendDOS = weekSummaryService.findWeekSummary(page);
		int total = weekSummaryService.count();
	    map.put("weekSummaryExtendDOS",weekSummaryExtendDOS);
		map.put("pp",pp);
	    map.put("total",total);
		return map;
	}

	//查询个人月总结
	@ResponseBody
	@RequestMapping("/getWeekSummary")
	public Map<String,Object> getWeekSummary() {
		Map<String,Object> map=new HashMap<>();
		DateTool dateTool=new DateTool();
		try{
			PeopleDO people=getUser();
			WeekSummaryDO weekSummaryDO = new WeekSummaryDO();
			weekSummaryDO.setId(people.getWeekSummaryId());
			//查询数据
			WeekSummaryExtendDO weekSummaryExtendDO = weekSummaryService.getWeekSummaryById(weekSummaryDO);
			if(!weekSummaryExtendDO.getWeek_summary_time().equals(dateTool.WeekAndWeek())){
				map.put("code",0);
				map.put("msg","您还没有编写本周的总结");
			}else {
				map.put("weekSummaryExtendDO",weekSummaryExtendDO);
				map.put("code",0);
			}

		}catch (Exception e){
			map.put("code",-1);
			map.put("msg","系统异常");
		}
		return map;
	}

	//根据计划id查询
	@ResponseBody
	@RequestMapping("/getWeekSummaryById")
	//chax
	@RequiresPermissions("system:weekSummary:edit")
	public Map<String,Object> getWeekSummaryById(@RequestBody WeekSummaryDO weekSummaryDO) {
		Map<String,Object> map=new HashMap<>();
		try{
			//查询周总结数据
			WeekSummaryExtendDO weekSummaryExtendDO = weekSummaryService.getWeekSummaryById(weekSummaryDO);
			//封装到map
			map.put("weekSummaryExtendDO",weekSummaryExtendDO);
			map.put("code",0);
		}catch (Exception e){
			map.put("code",-1);
			map.put("msg","系统异常");
		}
		return map;
	}

	//多条件组合查询
	@ResponseBody
	@RequestMapping("/compositeQueryWeekSummary")
	public Map<String,Object> compositeQueryWeekSummary(ConditionDO conditionDO) {
		Map<String,Object> map=new HashMap<>();
		String sss=conditionDO.getTime().replace('-','年');
		conditionDO.setTime(sss);
		//计算开始检索位置
		conditionDO.setPc(conditionDO.getPageSize()*(conditionDO.getPage()-1));
		try{
			//组合查询
			List<WeekSummaryExtendDO> weekSummaryExtendDOS = weekSummaryService.compositeQueryWeekSummary(conditionDO);
			Integer pp=weekSummaryService.queryCount(conditionDO);
			map.put("weekSummaryExtendDOS",weekSummaryExtendDOS);
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
	@RequestMapping("/addWeekSummary")
	//添加
	@RequiresPermissions("system:weekSummary:add")
	public Map<String,Object> addWeekSummary(@RequestBody WeekSummaryDO weekSummaryDO) {
		Map<String,Object> map=new HashMap<>();
		DateTool dateTool=new DateTool();
		try{
			PeopleDO people=getUser();
			//判断是否编写过周计划
			if (people.getWeekPlanId()!=null&&!people.getWeekPlanId().equals("")){// 编写过
				WeekPlanDO weekPlanDO=new WeekPlanDO();
				weekPlanDO.setId(people.getWeekPlanId());
				WeekPlanExtendDO weekPlanExtendDO=weekPlanService.getWeekPlanById(weekPlanDO);
				if (weekPlanExtendDO.getWeek_plan_time().equals(dateTool.WeekAndWeek())){ //是本周的计划

					if (people.getWeekSummaryId()!=null&&!people.getWeekSummaryId().equals("")){ //判断该用户是否编写过周总结
						weekSummaryDO.setId(people.getWeekSummaryId());
						WeekSummaryExtendDO weekSummaryExtendDO1=weekSummaryService.getWeekSummaryById(weekSummaryDO);
						//判断该周总结是否为本周的周总结
						if (weekSummaryExtendDO1.getWeek_summary_time().equals(dateTool.WeekAndWeek())){  //是本周，修改
							WeekSummaryExtendDO weekSummaryExtendDO=new WeekSummaryExtendDO();
							weekSummaryExtendDO.setWeekSummaryId(people.getWeekSummaryId());
							weekSummaryExtendDO.setWeek_summary_content(weekSummaryDO.getWeek_summary_content());
							weekSummaryExtendDO.setWeek_summary_state(weekSummaryDO.getWeek_summary_state());
							weekSummaryExtendDO.setWeek_summary_entry_time(dateTool.adyAndDay());
							weekSummaryService.updateWeekSummaryContentByPeople(weekSummaryExtendDO);
							map.put("msg","保存成功");
							map.put("code",0);
						}else {  //不是本周
							weekSummaryDO.setWeek_summary_code(String.valueOf(UUID.randomUUID()));
							weekSummaryDO.setWeek_summary_entry_time(dateTool.adyAndDay());
							weekSummaryDO.setWeek_summary_time(dateTool.WeekAndWeek());
							weekSummaryDO.setWeek_summary_reply("否");//是否查看，默认为否
							weekSummaryDO.setWeek_summary_see("否");//是否回复，默认为否
							weekSummaryDO.setPeo_id(people.getId());
							weekSummaryService.addWeekSummary(weekSummaryDO);
							WeekSummaryExtendDO weekSummaryExtendDO=weekSummaryService.getWeekSummaryByCode(weekSummaryDO.getWeek_summary_code());
							people.setWeekSummaryId(weekSummaryExtendDO.getId());
							//修改人员月计划编码
							weekSummaryService.updatePeopleByCode(people);
							map.put("code",0);
						}
					}else {  //未编写过周总结
						weekSummaryDO.setWeek_summary_code(String.valueOf(UUID.randomUUID()));
						weekSummaryDO.setWeek_summary_entry_time(dateTool.adyAndDay());
						weekSummaryDO.setWeek_summary_time(dateTool.WeekAndWeek());
						weekSummaryDO.setWeek_summary_reply("否");//是否查看，默认为否
						weekSummaryDO.setWeek_summary_see("否");//是否回复，默认为否
						weekSummaryDO.setPeo_id(people.getId());
						weekSummaryService.addWeekSummary(weekSummaryDO);
						WeekSummaryExtendDO weekSummaryExtendDO=weekSummaryService.getWeekSummaryByCode(weekSummaryDO.getWeek_summary_code());
						people.setWeekSummaryId(weekSummaryExtendDO.getId());
						//修改人员月计划编码
						weekSummaryService.updatePeopleByCode(people);
						map.put("code",0);
					}

				}else { //不是本周的总结
					if (weekSummaryDO.getWeek_summary_state()==1){ //保存按钮
						weekSummaryDO.setWeek_summary_code(String.valueOf(UUID.randomUUID()));
						weekSummaryDO.setWeek_summary_entry_time(dateTool.adyAndDay());
						weekSummaryDO.setWeek_summary_time(dateTool.WeekAndWeek());
						weekSummaryDO.setWeek_summary_reply("否");//是否查看，默认为否
						weekSummaryDO.setWeek_summary_see("否");//是否回复，默认为否
						weekSummaryDO.setPeo_id(people.getId());
						weekSummaryService.addWeekSummary(weekSummaryDO);
						WeekSummaryExtendDO weekSummaryExtendDO=weekSummaryService.getWeekSummaryByCode(weekSummaryDO.getWeek_summary_code());
						people.setWeekSummaryId(weekSummaryExtendDO.getId());
						//修改人员月计划编码
						weekSummaryService.updatePeopleByCode(people);
						map.put("code",0);
						map.put("msg","保存成功！");
					}else {
						map.put("code",-1);
						map.put("msg","请先编写周计划！");
					}

				}
			}else { //未编写周计划
				if (weekSummaryDO.getWeek_summary_state()==1){ //保存按钮
					weekSummaryDO.setWeek_summary_code(String.valueOf(UUID.randomUUID()));
					weekSummaryDO.setWeek_summary_entry_time(dateTool.adyAndDay());
					weekSummaryDO.setWeek_summary_time(dateTool.WeekAndWeek());
					weekSummaryDO.setWeek_summary_reply("否");//是否查看，默认为否
					weekSummaryDO.setWeek_summary_see("否");//是否回复，默认为否
					weekSummaryDO.setPeo_id(people.getId());
					weekSummaryService.addWeekSummary(weekSummaryDO);
					WeekSummaryExtendDO weekSummaryExtendDO=weekSummaryService.getWeekSummaryByCode(weekSummaryDO.getWeek_summary_code());
					people.setWeekSummaryId(weekSummaryExtendDO.getId());
					//修改人员月计划编码
					weekSummaryService.updatePeopleByCode(people);
					map.put("code",0);
					map.put("msg","保存成功！");
				}else {
					map.put("code",-1);
					map.put("msg","请先编写周计划！");
				}

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
	@RequestMapping("/updateWeekSummaryStateById")
	@RequiresPermissions("system:weekSummary:sh")
	public Map<String,Object> updateWeekSummaryStateById(@RequestBody WeekSummaryDO weekSummaryDO) {
		Map<String,Object> map=new HashMap<>();
		try{
			weekSummaryService.updateWeekSummaryStateById(weekSummaryDO);
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
