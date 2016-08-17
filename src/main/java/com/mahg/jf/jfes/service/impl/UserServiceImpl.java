package com.mahg.jf.jfes.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mahg.jf.jfes.dao.DevInfoMapper;
import com.mahg.jf.jfes.dao.DynamicDataSourceRegister;
import com.mahg.jf.jfes.service.UserService;



@Service(value="userService")
public class UserServiceImpl implements UserService {
   private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Resource
	DevInfoMapper dao;

	public void findUserByjfid(String id) {
		System.out.println("findUserByjfid:"+id);
	   HashMap map=	dao.getBaseInfo(430L);
	   logger.info(map.toString());
	}

}
