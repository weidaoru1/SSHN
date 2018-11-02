package com.fus.cocoon.utils.spring;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component("SpringContextUtils")
@Scope("singleton")
public final class SpringContextUtils implements ApplicationContextAware{
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext appliContext)
			throws BeansException {
		applicationContext = appliContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public static Object getBean(String beanName)throws BeansException
  {
    return applicationContext.getBean(beanName);
  }
	@SuppressWarnings("unchecked")
	public static Object getBean(String name, Class requiredType) throws BeansException
  {
    return applicationContext.getBean(name, requiredType);
  }

  public static boolean containsBean(String name)
  {
    return applicationContext.containsBean(name);
  }

  public static boolean isSingleton(String name)
    throws NoSuchBeanDefinitionException
  {
    return applicationContext.isSingleton(name);
  }
  @SuppressWarnings("unchecked")
public static Class getType(String name)
  throws NoSuchBeanDefinitionException
  {
    return applicationContext.getType(name);
  }

  public static String[] getAliases(String name)
    throws NoSuchBeanDefinitionException
  {
    return applicationContext.getAliases(name);
}

}
