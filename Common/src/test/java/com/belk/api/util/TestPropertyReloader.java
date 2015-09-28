package com.belk.api.util;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.BeanFactory;

import com.belk.api.exception.BaseException;
import com.belk.api.loader.manager.LoaderManager;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyReloader.class,LoaderManager.class,BeanFactory.class})
public class TestPropertyReloader {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}

	private final BeanFactory beanFactory = PowerMockito.mock(BeanFactory.class);
	PropertyReloader propertyReloader = new PropertyReloader();
	String correlationId;
	/**
	 * Precondition required for Junit test cases for ValidationConfigRuleConverter class.
	 */
	@Before
	public final void setUp() {
		correlationId = "1234567891234567";
	}

	@Test
	public final void testUpdatePropertyMap() throws BaseException{
		Map<String, List<String>> updateRequestUriMap = TestCommonUtil.requestUriMap();
		propertyReloader.updatePropertyMap(updateRequestUriMap , correlationId);
		assertNotNull(propertyReloader);

	}
	@Test
	public final void testUpdatePropertyMapForFileName() throws BaseException{
		propertyReloader.setBeanFactory(beanFactory);
		PowerMockito.mock(ErrorLoader.class);
		LoaderManager loaderManager = PowerMockito.mock(LoaderManager.class);
		Map<String, List<String>> updateRequestUriMap = TestCommonUtil.getRequestUriMap();
		PowerMockito.when(beanFactory.getBean("endecaLoader")).thenReturn(loaderManager);
		propertyReloader.updatePropertyMap(updateRequestUriMap, correlationId);

	}
	@Test
	(expected=BaseException.class)
	public final void testUpdatePropertyMapForException() throws BaseException{
		propertyReloader.setBeanFactory(beanFactory);
		PowerMockito.mock(ErrorLoader.class);
		LoaderManager loaderManager = PowerMockito.mock(LoaderManager.class);
		Map<String, List<String>> updateRequestUriMap = TestCommonUtil.getRequestUriMapWithFileName();
		PowerMockito.when(beanFactory.getBean("endecaLoader")).thenReturn(loaderManager);
		propertyReloader.updatePropertyMap(updateRequestUriMap, correlationId);

	}
	@Test
	(expected=BaseException.class)
	public final void testUpdatePropertyMapException() throws BaseException{
		Map<String, List<String>> updateRequestUriMap = new HashMap<String, List<String>>();
		propertyReloader.updatePropertyMap(updateRequestUriMap , correlationId);
		assertNotNull(propertyReloader);

	}

}
