package com.fus.cocoon.utils.object;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

public final class ReflectionUtils {
	public static <T> List<Class<?>> getAssignedClass(Class<?> cls, List<Class<?>> clses)
	  {
	    List<Class<?>> classes = new ArrayList<Class<?>>();
	    for (Class<?> c : clses) {
	      if ((cls.isAssignableFrom(c)) && (!cls.equals(c))) {
	        classes.add(c);
	      }
	    }
	    return classes;
	  }

	  public static List<Class<?>> getClasses(Class<?> cls)
	    throws ClassNotFoundException
	  {
	    String pk = cls.getPackage().getName();
	    String path = pk.replace('.', '/');
	    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	    URL url = classloader.getResource(path);
	    return getClasses(new File(url.getFile()), pk, null);
	  }

	  @SuppressWarnings("unchecked")
	public static List<Class<?>> getClasses(File dir, String pk, String[] outsides)
	    throws ClassNotFoundException
	  {
		List<Class<?>> classes = new ArrayList<Class<?>>();
	    if (!dir.exists()) {
	      return classes;
	    }
	    String thisPk = pk + ".";
	    for (File f : dir.listFiles()) {
	      if (f.isDirectory()) {
	        classes.addAll(getClasses(f, thisPk + f.getName(), outsides));
	      }
	      String name = f.getName();
	      if (name.endsWith(".class")) {
	        Class clazz = null;
	        String clazzName = thisPk + name.substring(0, name.length() - 6);
	        if ((outsides == null) || (outsides.length == 0) || (!ArrayUtils.contains(outsides, clazzName))) {
	          try {
	            clazz = Class.forName(clazzName);
	          } catch (Throwable e) {
	            e.printStackTrace();
	          }
	          if (clazz != null) {
	            classes.add(clazz);
	          }
	        }
	      }
	    }
	    return classes;
	  }

	  @SuppressWarnings("unchecked")
	public static <T> T cloneInstance(T instance)
	  {
	    Class cls = instance.getClass();
	    Object newIns = BeanUtils.instantiateClass(cls);
	    BeanUtils.copyProperties(instance, newIns);
	    return (T) newIns;
	  }

	  public static String getSimpleSurname(Class<?> clazz)
	  {
	    if (clazz == null) {
	      return null;
	    }
	    return StringUtils.substringBefore(clazz.getSimpleName(), "_$$_");
	  }

	  public static String getSurname(Class<?> clazz)
	  {
	    if (clazz == null) {
	      return null;
	    }
	    return StringUtils.substringBefore(clazz.getName(), "_$$_");
	  }

	  public static Object getFieldValue(Object object, Field field)
	  {
	    makeAccessible(field);

	    Object result = null;
	    try {
	      result = field.get(object);
	    } catch (IllegalAccessException e) {
	      e.printStackTrace();
	    }
	    return result;
	  }

	  public static Object getFieldValue(Object object, String fieldName)
	  {
	    try
	    {
	      Field field = getDeclaredField(object, fieldName);

	      if (field == null) {
	        throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
	      }
	      return getFieldValue(object, field);
	    } catch (Exception e) {
	      String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	      try {
	        Method method = object.getClass().getMethod(methodName, new Class[0]);
	        return method.invoke(object, new Object[0]);
	      } catch (Exception e1) {
	        e1.printStackTrace();
	      }
	    }
	    return null;
	  }

	  @SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(Class clazz)
	  {
	    return getSuperClassGenricType(clazz, 0);
	  }

	  @SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz, int index)
	  {
	    Type genType = clazz.getGenericSuperclass();

	    if (!(genType instanceof ParameterizedType)) {
	      return Object.class;
	    }

	    Type[] params = ((ParameterizedType)genType).getActualTypeArguments();

	    if ((index >= params.length) || (index < 0)) {
	      return Object.class;
	    }
	    if (!(params[index] instanceof Class)) {
	      return Object.class;
	    }
	    return (Class)params[index];
	  }

	  public static <T> void setFieldValue(T object, Field field, Object value)
	  {
	    makeAccessible(field);
	    try
	    {
	      field.set(object, value);
	    } catch (IllegalAccessException e) {
	      e.printStackTrace();
	    }
	  }

	  public static <T> void setFieldValue(T object, String fieldName, Object value)
	  {
	    try
	    {
	      Field field = getDeclaredField(object, fieldName);

	      if (field == null) {
	        throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
	      }
	      setFieldValue(object, field, value);
	    } catch (Exception e) {
	      String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	      try {
	        Method method = object.getClass().getMethod(methodName, new Class[] { value.getClass() });
	        method.invoke(object, new Object[] { value });
	      } catch (Exception e1) {
	        e1.printStackTrace();
	      }
	    }
	  }

	  @SuppressWarnings("unchecked")
	public static Field getDeclaredField(Object object, String fieldName)
	  {
	    for (Class superClass = object.getClass(); superClass != Object.class; ) {
	      try {
	        return superClass.getDeclaredField(fieldName);
	      }
	      catch (NoSuchFieldException localNoSuchFieldException)
	      {
	        superClass = superClass.getSuperclass();
	      }

	    }

	    return null;
	  }

	  @SuppressWarnings("unchecked")
	public static List<Field> getDeclaredFields(Object object)
	  {
	    List<Field> fields = new ArrayList<Field>();
	    for (Class superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
	      Field[] fs = superClass.getDeclaredFields();
	      fields.addAll(Arrays.asList(fs));
	    }
	    return fields;
	  }

	  @SuppressWarnings("unchecked")
	public static List<Field> getDeclaredFieldsNoneTransient(Object object)
	  {
		List<Field> fields = new ArrayList<Field>();
	    for (Class superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
	      Field[] fs = superClass.getDeclaredFields();
	      for (Field field : fs) {
	        if (field.getAnnotation(Transient.class) == null) {
	          fields.add(field);
	        }
	      }
	    }
	    return fields;
	  }

	  protected static void makeAccessible(Field field)
	  {
	    if ((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers())))
	      field.setAccessible(true);
	  }
}
