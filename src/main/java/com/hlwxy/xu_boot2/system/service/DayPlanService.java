package com.hlwxy.xu_boot2.system.service;



import com.hlwxy.xu_boot2.system.domain.*;

import java.util.List;

public interface DayPlanService {
	//查询人员总条数
	Integer coun();
	//查询所有计划
	List<DayPlanExtendDO> findDayPlan(Page page);
	int count();
	//根据id查新月计划
	DayPlanExtendDO getDayPlanById(DayPlanDO dayPlanDO);
	//根据公司id查询 本月份 公司内所有人的月计划
	List<DayPlanExtendDO> findDayPlanByCompany(ConditionDO conditionDO);
	//根据部门id查询 本月份 部门内所有人的月计划
	List<DayPlanExtendDO> findDayPlanByDepartment(ConditionDO conditionDO);
	//组合查询总条数
	Integer queryCount(ConditionDO conditionDO);
	//多条件组合查询
	List<DayPlanExtendDO> compositeQuery(ConditionDO conditionDO);
	//添加月计划
	void addDayPlan(DayPlanDO dayPlanDO);
	//根据计划编号查询计划
	DayPlanExtendDO getDayPlanByCode(String code);
	//修改人员周计划
	void updatePeopleByCode(PeopleDO peopleDO);
	//修改周计划内容
	void updateDayPlanContentByPeople(DayPlanExtendDO dayPlanExtendDO);
	//修改计划状态
	void updateDayPlanStateById(DayPlanDO dayPlanDO);
}
