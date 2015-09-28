package com.belk.api.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpConnectionLoader.class)
public class TestHttpConnectionLoader {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	final String correlationId = "1233445";
	final HttpConnectionLoader httpConnectionLoader = new HttpConnectionLoader();
	
	@Test
	public final void testafterPropertiesSet() throws BaseException{
		httpConnectionLoader.afterPropertiesSet();
		assertNotNull(httpConnectionLoader);
	}
	


}
