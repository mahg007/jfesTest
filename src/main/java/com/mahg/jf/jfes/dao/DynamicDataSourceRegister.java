package com.mahg.jf.jfes.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import com.mahg.jf.jfes.jfesTest.PropertiesUtil;;
public class DynamicDataSourceRegister
        implements ImportBeanDefinitionRegistrar, EnvironmentAware {
 
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
 
    // 如配置文件中未指定数据源类型，使用该默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.commons.dbcp.BasicDataSource";
    // private static final Object DATASOURCE_TYPE_DEFAULT =
    // "com.zaxxer.hikari.HikariDataSource";
 
    // 数据源
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();
 
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        // 将主数据源添加到更多数据源中
        targetDataSources.put("dataSource", defaultDataSource);
        DbContextHolder.dataSourceIds.add("proDataSource");
        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
        	DbContextHolder.dataSourceIds.add(key);
        }
 
        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
 
        logger.info("Dynamic DataSource Registry");
    }
 
    /**
     * 创建DataSource
     *
     * @param type
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @return
     * @author SHANHY
     * @create 2016年1月24日
     */
    @SuppressWarnings("unchecked")
    public DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            Object type = dsMap.get("type");
            if (type == null)
                type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource
 
            Class dataSourceType;
            dataSourceType = (Class) Class.forName((String) type);
 
            String driverClassName = dsMap.get("driver").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();
 
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 加载多数据源配置
     */
    public void setEnvironment(Environment env) {
        initDefaultDataSource(env);
        initDEVDataSources(env);
    }
 
    /**
     * 初始化主数据源
     *
     * @author SHANHY
     * @create 2016年1月24日
     */
    public void initDefaultDataSource(Environment env) {
    	
        // 读取主数据源
    	Properties propertyResolver=PropertiesUtil.getInstance().getProperties("jdbc.properties");
       // RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "pro.");
        Map<String, Object> dsMap = new HashMap<String, Object>();
        dsMap.put("type", propertyResolver.getProperty("type"));
        dsMap.put("driver", propertyResolver.getProperty("pro.driver"));
        dsMap.put("url", propertyResolver.getProperty("pro.url"));
        dsMap.put("username", propertyResolver.getProperty("pro.username"));
        dsMap.put("password", propertyResolver.getProperty("pro.password"));
        defaultDataSource = buildDataSource(dsMap);
        customDataSources.put("proDataSource", defaultDataSource);
       
    }
 
    /**
     * 初始化更多数据源
     *
     * @author SHANHY
     * @create 2016年1月24日
     */
    public void initDEVDataSources(Environment env) {
        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
    	Properties propertyResolver=PropertiesUtil.getInstance().getProperties("jdbc.properties");
    	 Map<String, Object> dsMap = new HashMap<String, Object>();
         dsMap.put("type", propertyResolver.getProperty("type"));
         dsMap.put("driver", propertyResolver.getProperty("dev.driver"));
         dsMap.put("url", propertyResolver.getProperty("dev.url"));
         dsMap.put("username", propertyResolver.getProperty("dev.username"));
         dsMap.put("password", propertyResolver.getProperty("dev.password"));
         customDataSources.put("devDataSource", buildDataSource(dsMap));
         Map<String, Object> dsMap1 = new HashMap<String, Object>();
         dsMap1.put("type", propertyResolver.getProperty("type"));
         dsMap1.put("driver", propertyResolver.getProperty("test.driver"));
         dsMap1.put("url", propertyResolver.getProperty("test.url"));
         dsMap1.put("username", propertyResolver.getProperty("test.username"));
         dsMap1.put("password", propertyResolver.getProperty("test.password"));
         customDataSources.put("testDataSource", buildDataSource(dsMap1));
 
    }
 
}