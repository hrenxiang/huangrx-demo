package com.huangrx.guava;

import com.google.common.base.Defaults;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 默认值
 *
 * @author    hrenxiang
 * @since     2022/5/24 2:04 PM
 */
public class DefaultsTest {

	@Test
	public void testGetDefaultValue() {
		assertNotEquals(Boolean.TRUE, Defaults.defaultValue(boolean.class));
		assertEquals('\0', Defaults.defaultValue(char.class).charValue());
		assertEquals(0, Defaults.defaultValue(byte.class).byteValue());
		assertEquals(0, Defaults.defaultValue(short.class).shortValue());
		assertEquals(0, Defaults.defaultValue(int.class));
		assertEquals(0, Defaults.defaultValue(long.class));
		assertEquals(0.0f, Defaults.defaultValue(float.class));
		assertEquals(0.0d, Defaults.defaultValue(double.class));
		assertNull(Defaults.defaultValue(void.class));
		assertNull(Defaults.defaultValue(String.class));
	}
}