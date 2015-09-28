package com.belk.api.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.logger.GenericLoggerImpl;

/**
 * Unit Testing related to GenericLoggerImpl class is performed. <br />
 * {@link GenericLoggerImpl} class is written for testing methods in The unit
 * test cases evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GenericLoggerImpl.class, Throwable.class })
public class TestGenericLoggerImpl {
	String correlationId = "123455556";
	GenericLoggerImpl genericLoggerImpl = new GenericLoggerImpl("");

	/**
	 * Test method for {@link com.belk.api.logger.GenericLoggerImpl#debug()}.
	 */
	@Test
	public final void testDebug() {

		this.genericLoggerImpl.debug("debug", this.correlationId);
		assertNotNull(this.genericLoggerImpl);
	}

	/**
	 * Test method for {@link com.belk.api.logger.GenericLoggerImpl#info()}.
	 */
	@Test
	public final void testInfo() {
		this.genericLoggerImpl.info("info", this.correlationId);
		assertNotNull(this.genericLoggerImpl);
	}

	/**
	 * Test method for {@link com.belk.api.logger.GenericLoggerImpl#warn()}.
	 */
	@Test
	public final void testWarn() {
		this.genericLoggerImpl.warn("warn", this.correlationId);
		assertNotNull(this.genericLoggerImpl);
	}

	/**
	 * Test method for {@link com.belk.api.logger.GenericLoggerImpl#error()}.
	 */
	@Test
	public final void testError() {
		final Throwable throwable = PowerMockito.mock(Throwable.class);
		this.genericLoggerImpl.error(throwable, this.correlationId);
		assertNotNull(this.genericLoggerImpl);
	}

	/**
	 * Test method for {@link com.belk.api.logger.GenericLoggerImpl#error()}.
	 */
	@Ignore
	public final void testErrorWithExceptionParameter() {
		final Exception exception = PowerMockito.mock(Exception.class);
		this.genericLoggerImpl.error("errormsg", exception, this.correlationId);
		assertNotNull(this.genericLoggerImpl);

	}

	/**
	 * Test method for {@link com.belk.api.logger.GenericLoggerImpl#fatal()}.
	 */
	@Test
	public final void testFatal() {
		this.genericLoggerImpl.fatal("fatalError");
		assertNotNull(this.genericLoggerImpl);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.GenericLoggerImpl#logMethodEntry()}.
	 */
	@Test
	public final void testLogMethodEntry() {
		final long expected = System.currentTimeMillis();

		final long actualValue = this.genericLoggerImpl
				.logMethodEntry(this.correlationId);

		assertNotNull(this.genericLoggerImpl);
		//assertEquals(expected, actualValue, 1);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.GenericLoggerImpl#logMethodEntry()}.
	 */
	@Test
	public final void testLogMethodExit() {
		this.genericLoggerImpl.logMethodExit(123, this.correlationId);
		assertNotNull(this.genericLoggerImpl);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.GenericLoggerImpl#isErrorEnabled()}.
	 */
	@Test
	public final void testIsErrorEnabled() {
		final boolean expected = true;
		final boolean actual = this.genericLoggerImpl.isErrorEnabled();

		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.GenericLoggerImpl#isFatalEnabled()}.
	 */
	@Test
	public final void testIsFatalEnabled() {
		final boolean expected = true;
		final boolean actual = this.genericLoggerImpl.isFatalEnabled();

		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.GenericLoggerImpl#isWarnEnabled()}.
	 */
	@Test
	public final void testIsWarnEnabled() {
		final boolean expected = true;
		final boolean actual = this.genericLoggerImpl.isWarnEnabled();

		assertNotNull(actual);
		assertEquals(expected, actual);
	}

}
