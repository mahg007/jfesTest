package com.mahg.jf.jfes.jfesTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;


import com.mahg.jf.jfes.dao.DynamicDataSourceRegister;
import com.mahg.jf.jfes.service.UserService;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
@ImportResource(value = {"classpath:spring-idx.xml"})

public class App 
{
    public static void main( String[] args )
    
    {
    	ApplicationContext application = SpringApplication.run(App.class, args);
    	AppDataSourceConstant.APP_DATA_SOURCE="devDataSource";
    	UserService userService=(UserService) application.getBean("userService");
    	userService.findUserByjfid("mahg");
        System.out.println( "Hello World!" );
        AppDataSourceConstant.APP_DATA_SOURCE="testDataSource";
        UserService userService1=(UserService) application.getBean("userService");
    	userService1.findUserByjfid("mahg");
        System.out.println( "Hello World2!" );
    }
}
