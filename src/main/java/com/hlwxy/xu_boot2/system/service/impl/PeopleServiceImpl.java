package com.hlwxy.xu_boot2.system.service.impl;

import com.hlwxy.xu_boot2.system.dao.PeopleDao;
import com.hlwxy.xu_boot2.system.domain.PeopleDO;
import com.hlwxy.xu_boot2.system.dto.PeopleDTO;
import com.hlwxy.xu_boot2.system.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class PeopleServiceImpl implements PeopleService {
	@Autowired
	private PeopleDao peopleDao;

	@Override
	public PeopleDO get(Integer id){
		return peopleDao.get(id);
	}

	@Override
	public List<PeopleDO> list(Map<String, Object> map){
		return peopleDao.list(map);
	}

	@Override
	public List<PeopleDTO> listPeopleAll(Map<String, Object> map) {
		return peopleDao.listPeopleAll(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return peopleDao.count(map);
	}

	@Override
	public int save(PeopleDO people){
		return peopleDao.save(people);
	}

	@Override
	public int update(PeopleDO people){
		return peopleDao.update(people);
	}

	@Override
	public int remove(Integer id){
		return peopleDao.remove(id);
	}

	@Override
	public int batchRemove(Integer[] ids){
		return peopleDao.batchRemove(ids);
	}

	@Override
	public PeopleDO finduserid(Integer userId) {
		return peopleDao.finduserid(userId);
	}

	@Override
	public PeopleDO getpeoname(String peoName) {
		return peopleDao.getpeoname(peoName);
	}

	@Override
	public PeopleDO findByUsername(String username, String password) {
		return peopleDao.findByUsername(username,password);
	}


}
