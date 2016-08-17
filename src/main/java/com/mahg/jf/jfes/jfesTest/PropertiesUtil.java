package com.mahg.jf.jfes.jfesTest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Properties;

public class PropertiesUtil {

    private static String SYSTEM = "jf-settings.conf";
	private static Hashtable<String, Properties> register = new Hashtable<String, Properties>();// 静态对象初始化[在其它对象之前]
	private static PropertiesUtil ec = new PropertiesUtil();// 创建对象ec

	private PropertiesUtil() {
		
	}

	/**
	 * 取得EnvironmentConfig的一个实例
	 */
	public static PropertiesUtil getInstance() {
		return ec;// 返回EnvironmentConfig对象
	}

	/**
	 * 读取配置文件
	 */
	public Properties getProperties(String fileName) {// 传递配置文件路径
		InputStream is = null;// 定义输入流is
		Properties p = null;
		try {
			p = (Properties) register.get(fileName);// 将fileName存于一个HashTable
			/**
			 * 如果为空就尝试输入进文件
			 */
			if (p == null) {
				try {
					is = new FileInputStream(fileName);// 创建输入流
				} catch (Exception e) {
					if (fileName.startsWith("/"))
						// 用getResourceAsStream()方法用于定位并打开外部文件。
						is = PropertiesUtil.class
								.getResourceAsStream(fileName);
					else
						is = PropertiesUtil.class.getResourceAsStream("/"
								+ fileName);
				}
				p = new Properties();
				p.load(is);// 加载输入流
				register.put(fileName, p);// 将其存放于HashTable缓存
//				is.close();// 关闭输入流
			}
		} catch (Exception e) {
//			e.printStackTrace(System.out);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
		return p;// 返回Properties对象
	}
	
	

	/**
	 * 此处插入方法描述。
	 */
	public String getValue(String fileName, String strKey) {
		Properties p = getProperties(fileName);
		try {
			return (String) p.getProperty(strKey);
		} catch (Exception e) {
//			e.printStackTrace(System.out);
		}
		return null;
	}
	
    public  String getParmValue(String fileName,String strKey, Object[] arguments) {

		Properties p = getProperties(fileName);
		String formatString = (String) p.getProperty(strKey);

		// No need for any formatting if no parameters are specified
		if (arguments == null || arguments.length == 0) {
			return formatString;
		} else {
			MessageFormat formatter = new MessageFormat(formatString);
			return formatter.format(arguments);
		}

    }

	/**
     * 设置指定key的value
     */
	public void setValue(String strKey, String strValue) {
		Properties p = getProperties(SYSTEM);
		p.setProperty(strKey, strValue);
	}
	
}