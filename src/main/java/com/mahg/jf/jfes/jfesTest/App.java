package com.mahg.jf.jfes.jfesTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.mahg.jf.jfes.service.UserService;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ImportResource(value = {"classpath:spring-idx.xml"})

public class App 
{
    public static void main( String[] args )
    
    {
    	ApplicationContext application = SpringApplication.run(App.class, args);
    	UserService userService=(UserService) application.getBean("userService");
    	userService.findUserByjfid("mahg");
        System.out.println( "Hello World!" );
    }
}
