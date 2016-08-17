package com.mahg.jf.jfes.dao;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mahg.jf.jfes.jfesTest.AppDataSourceConstant;

/**
 * @author mahg
 * created on 2016年8月11日
 * 
 */
@Aspect // 表示是一个切面类
@Component // 切面类也要先由spring初始化
public class SwitchDBAOP {
	private static Logger log = LoggerFactory.getLogger(SwitchDBAOP.class);

	@Before(value = "execution(public * com.mahg.jf.jfes.service.*.*.*(..))")
	public void switchDB() {
		if(DbContextHolder.getDbType()==null||DbContextHolder.getDbType().equals(AppDataSourceConstant.APP_DATA_SOURCE)){
			//当前数据源和系统指定数据源不同时才进行切换
			DbContextHolder.setDbType(AppDataSourceConstant.APP_DATA_SOURCE);
		}
		
	}
	


}