package com.hlwxy.xu_boot2.system.service;



import com.hlwxy.xu_boot2.system.domain.*;

import java.util.List;

public interface WeekSummaryService {
	//查询人员总条数
	Integer coun();
	//查询所有总结
	List<WeekSummaryExtendDO> findWeekSummary(Page page);
	int count();
	//根据id查新月总结
	WeekSummaryExtendDO getWeekSummaryById(WeekSummaryDO weekSummaryDO);
	//根据公司id查询 本月份 公司内所有人的月计划
	List<WeekSummaryExtendDO> findWeekSummaryByCompany(ConditionDO conditionDO);
	//根据部门id查询 本月份 部门内所有人的月计划
	List<WeekSummaryExtendDO> findWeekSummaryByDepartment(ConditionDO conditionDO);
	//组合查询总条数
	Integer queryCount(ConditionDO conditionDO);
	//多条件组合查询
	List<WeekSummaryExtendDO> compositeQueryWeekSummary(ConditionDO conditionDO);
	//添加月总结
	void addWeekSummary(WeekSummaryDO weekSummaryDO);
	//根据总结编号查询总结
	WeekSummaryExtendDO getWeekSummaryByCode(String code);
	//修改人员周总结
	void updatePeopleByCode(PeopleDO peopleDO);
	//修改月总结内容
	void updateWeekSummaryContentByPeople(WeekSummaryExtendDO weekSummaryExtendDO);
	//修改计划状态
	void updateWeekSummaryStateById(WeekSummaryDO weekSummaryDO);
}
