package com.belk.api.cache;

import static org.powermock.api.mockito.PowerMockito.spy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Locale.Category;
import java.util.Map;
import java.util.ResourceBundle;

import junit.framework.TestCase;
import net.spy.memcached.MemcachedClient;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * Unit Testing related to CacheManager class is performed. <br />
 * {@link TestCacheManager} class is written for testing methods in
 * {@link CacheManager} The unit test cases evaluates the way the methods behave
 * for the inputs given.
 * 
 * @author Mindtree
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CacheManager.class, ResourceBundle.class,
		SocketAddress.class, InetSocketAddress.class, MemcachedClient.class })
public class TestCacheManager extends TestCase {
	MemcachedClient memCachedClient = PowerMockito.mock(MemcachedClient.class);


	/**
	 * Test method for
	 * {@link com.belk.api.cache.CacheManager#set(String,String,String)}.
	 * 
	 * 
	 * @throws Exception
	 *             Exception
	 * @param <T>
	 *            Category class
	 */
	@Ignore
	public final <T> void testSet() throws Exception {

		final CacheManager cacheManager = spy(new CacheManager());
		PowerMockito.mock(CacheManager.class);

		final CacheManager obj = new CacheManager();

		final Class<Class> clazz = java.lang.Class.class;
		final String name = clazz.getName();
		final Class aClass = obj.getClass();
		final Field field = CacheManager.class.getDeclaredField("cacheKeyMap");
		field.setAccessible(true);

		final Map<String, Class> value = new HashMap<String, Class>();
		value.put(name, clazz.getClass());

		field.set(obj, value);

		final Method method = aClass.getMethod("isCacheable", Class.class);
		method.invoke(obj, clazz);

		cacheManager.set("456", "tshirt", "1234567891234567");
		assertNotNull(cacheManager);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.cache.CacheManager#set(String,String,String)}. Test
	 * method to test set() having ExpireTime as parameter.
	 * 
	 */
	@Test
	public final void testSetWithExpireTime() {
		final Class clazz = Category.class;

		final CacheManager cacheManager = spy(new CacheManager());
		final CacheManager mockCacheManager = PowerMockito
				.mock(CacheManager.class);

		PowerMockito.when(mockCacheManager.isCacheable(clazz.getClass()))
				.thenReturn(true);

		cacheManager.set("456", "tshirt", 11111, "1234567891234567");
		assertNotNull(cacheManager);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.cache.CacheManager#get(String,Class,String)}.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Ignore
	public final void testGet() throws Exception {
		PowerMockito.mock(CacheManager.class);

		final CacheManager cacheManager = PowerMockito.spy(new CacheManager());
		PowerMockito.mock(MemcachedClient.class);

		Whitebox.invokeMethod(cacheManager, "isConnected");

		final Class<Class> clazz = java.lang.Class.class;

		cacheManager.get("456", clazz, "1234567891234567");
		assertNotNull(cacheManager);

	}

	/**
	 * Test method for {@link com.belk.api.cache.CacheManager#isConnected()}.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public final void testIsConnected() throws Exception {
		PowerMockito.spy(new CacheManager());
		final CacheManager obj = new CacheManager();
		obj.getClass();
		final Field field = CacheManager.class
				.getDeclaredField("memCachedClient");
		field.setAccessible(true);

		final InetSocketAddress localaddr = new InetSocketAddress("127.0.0.1",
				11211);
		final MemcachedClient value = new MemcachedClient(localaddr);
		field.set(obj, value);
		final boolean actual = Whitebox.invokeMethod(obj, "isConnected");

		assertEquals(true, actual);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.cache.CacheManager#isCacheAvailable()}.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Ignore
	public final void testIsCacheAvailable() throws Exception {
		final CacheManager cacheManager = PowerMockito.spy(new CacheManager());

		final boolean actual = Whitebox.invokeMethod(cacheManager,
				"isCacheAvailable");

		assertEquals(true, actual);
	}

	/**
	 * 
	 * Test method for {@link com.belk.api.cache.CacheManager#isCacheable()}.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Ignore
	public final void testIsCacheable() throws Exception {

		final CacheManager obj = new CacheManager();

		final Class<Class> clazz = java.lang.Class.class;
		final String name = clazz.getName();
		final Class aClass = obj.getClass();
		final Field field = CacheManager.class.getDeclaredField("cacheKeyMap");
		field.setAccessible(true);

		final Map<String, Class> value = new HashMap<String, Class>();
		value.put(name, clazz.getClass());

		field.set(obj, value);

		final Method method = aClass.getMethod("isCacheable", Class.class);

		final boolean returnValue = (boolean) method.invoke(obj, clazz);
		assertTrue(returnValue);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.cache.CacheManager#populateCacheKeyMap()}.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Test(expected = Exception.class)
	public final void testPopulateCacheKeyMap() throws Exception {
		final CacheManager cacheManager = PowerMockito.spy(new CacheManager());

		/**
		 * Using whitebox to forcefully invoke the private method using spy
		 * instance.
		 */
		Whitebox.invokeMethod(cacheManager, "populateCacheKeyMap");

		assertNotNull(cacheManager);
	}

	/**
	 * Test method for {@link com.belk.api.cache.CacheManager#shutDown()}.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public final void testShutDown() throws Exception {

		final CacheManager obj = new CacheManager();
		obj.getClass();
		final Field field = CacheManager.class
				.getDeclaredField("memCachedClient");
		field.setAccessible(true);

		final InetSocketAddress localaddr = new InetSocketAddress("127.0.0.1",
				11211);
		final MemcachedClient value = new MemcachedClient(localaddr);
		field.set(obj, value);

		Whitebox.invokeMethod(obj, "shutDown");

		assertNotNull(obj);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.cache.CacheManager#getObjExpiration()}.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Test(expected = Exception.class)
	public final void testGetObjExpiration() throws Exception {

		final CacheManager obj = new CacheManager();

		final Class c = Whitebox.getInnerClassType(CacheManager.class,
				"SubCacheConf");

		Whitebox.getConstructor(c).setAccessible(true);
		c.getConstructors();

		final String name = "com.belk.api.cache.CacheManager$SubCacheConf";
		obj.getClass();
		final Field field = CacheManager.class.getDeclaredField("cacheKeyMap");
		field.setAccessible(true);

		final Map<String, Class> value = new HashMap<String, Class>();

		value.put("class com.belk.api.cache.CacheManager$SubCacheConf",
				Whitebox.getInnerClassType(CacheManager.class, "SubCacheConf"));

		field.set(obj, value);

		Whitebox.invokeMethod(obj, "getObjExpiration", name);

		assertNotNull(obj);
	}

	/**
	 * Test method for {@link com.belk.api.cache.CacheManager#init()}.
	 * 
	 * @throws NoSuchFieldException
	 *             NoSuchFieldException
	 * @throws SecurityException
	 *             SecurityException
	 * @throws IllegalArgumentException
	 *             IllegalArgumentException
	 * @throws IllegalAccessException
	 *             IllegalAccessException
	 * @throws IOException
	 *             IOException
	 * @throws NoSuchMethodException
	 *             NoSuchMethodException
	 * @throws InvocationTargetException
	 *             InvocationTargetException
	 */
	@Ignore
	public final void testInit() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException, NoSuchMethodException,
			InvocationTargetException {
		final CacheManager obj = new CacheManager();
		final Class<Class> clazz = java.lang.Class.class;
		clazz.getName();
		final Class aClass = obj.getClass();
		final Field field = CacheManager.class.getDeclaredField("cacheKeyMap");
		field.setAccessible(true);

		final Field field1 = CacheManager.class
				.getDeclaredField("memCachedClient");
		field1.setAccessible(true);

		final InetSocketAddress localaddr = new InetSocketAddress("127.0.0.1",
				11211);
		final MemcachedClient value1 = new MemcachedClient(localaddr);
		field1.set(obj, value1);

		final Method method = aClass.getMethod("init", null);
		method.invoke(obj, null);

	}

	/**
	 * Test method for {@link com.belk.api.cache.CacheManager#getKeyPrepend()}.
	 * 
	 * @throws NoSuchFieldException
	 *             throws NoSuchFieldException
	 * @throws SecurityException
	 *             throws SecurityException
	 * @throws IllegalArgumentException
	 *             throws IllegalArgumentException
	 * @throws IllegalAccessException
	 *             throws IllegalAccessException
	 * @throws NoSuchMethodException
	 *             throws NoSuchMethodException
	 * @throws InvocationTargetException
	 *             throws InvocationTargetException
	 * @throws ClassNotFoundException
	 *             throws ClassNotFoundException
	 */
	@Test(expected = Exception.class)
	public final void testGetKeyPrepend() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException, ClassNotFoundException {
		final Class innerClassName = Whitebox.getInnerClassType(
				CacheManager.class, "SubCacheConf");
		PowerMockito.mock(innerClassName);

		final CacheManager obj = new CacheManager();

		final String name = innerClassName.getName();
		final Class aClass = obj.getClass();
		final Field field = CacheManager.class.getDeclaredField("cacheKeyMap");
		field.setAccessible(true);

		final Map<String, Class> value = new HashMap<String, Class>();

		value.put("class com.belk.api.cache.CacheManager$SubCacheConf",
				Whitebox.getInnerClassType(CacheManager.class, "SubCacheConf"));

		field.set(obj, value);

		final Method method = aClass.getDeclaredMethod("getKeyPrepend",
				String.class);

		method.setAccessible(true);
		method.invoke(obj, name);
		assertNotNull(obj);

	}

}
