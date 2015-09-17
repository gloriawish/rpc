package com.alibaba.middleware.race.rpc.tool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReflectionCache {
	private static final Map<String, Class<?>> PRIMITIVE_CLASS = new HashMap<String, Class<?>>();

	private static final LRUMap<String, Class<?>> CLASS_CACHE = new LRUMap<String, Class<?>>(
			128);

	private static final LRUMap<String, Method> METHOD_CACHE = new LRUMap<String, Method>(
			1024);
	static {
		PRIMITIVE_CLASS.put("boolean", boolean.class);
		PRIMITIVE_CLASS.put("byte", byte.class);
		PRIMITIVE_CLASS.put("short", short.class);
		PRIMITIVE_CLASS.put("int", int.class);
		PRIMITIVE_CLASS.put("long", long.class);
		PRIMITIVE_CLASS.put("long", long.class);
		PRIMITIVE_CLASS.put("float", float.class);
		PRIMITIVE_CLASS.put("double", double.class);
		PRIMITIVE_CLASS.put("void", void.class);

		CLASS_CACHE.putAll(PRIMITIVE_CLASS);
	}

	public static Class<?> getClass(String className)
			throws ClassNotFoundException {
		Class<?> clazz = CLASS_CACHE.get(className);
		if (null != clazz) {
			return clazz;
		}
		synchronized (CLASS_CACHE) {
			if (null == CLASS_CACHE.get(className)) {
				clazz = PRIMITIVE_CLASS.get(className);
				if (null == clazz) {
					clazz = Class.forName(className);
				}
				CLASS_CACHE.put(className, clazz);
				return clazz;
			} else {
				return CLASS_CACHE.get(className);
			}
		}
	}

	/**
	 * 获取缓存的Method
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(String className, String methodName,
			Class<?>[] parameterTypes) throws ClassNotFoundException,
			SecurityException, NoSuchMethodException {
		
		List<String> parameterTypeList = new LinkedList<String>();
		for (Class<?> parameterType : parameterTypes) 
		{
			 parameterTypeList.add(parameterType.getName());
		}
		String[] paramTypes=parameterTypeList.toArray(new String[0]);
		String key = className + "-" + methodName + "-"
				+ join(paramTypes, ";");
		Method method = METHOD_CACHE.get(key);
		if (null != method) {
			return method;
		}
		synchronized (METHOD_CACHE) {
			if (null == METHOD_CACHE.get(key)) {
				Class<?> clazz = getClass(className);
				Class<?>[] parameterClasses = new Class<?>[parameterTypes.length];
				for (int i = 0; i < parameterClasses.length; i++) {
					parameterClasses[i] = getClass(paramTypes[i]);
				}

				method = clazz.getMethod(methodName, parameterClasses);
				METHOD_CACHE.put(key, method);
				return method;
			} else {
				return METHOD_CACHE.get(key);
			}
		}
	}

	private static String join(String[] strs, String seperator) {
		if (null == strs || 0 == strs.length) {
			return "";
		}
		StringBuilder sb = new StringBuilder(1024);
		sb.append(strs[0]);
		for (int i = 1; i < strs.length; i++) {
			sb.append(seperator).append(strs[i]);
		}
		return sb.toString();
	}
}