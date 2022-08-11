package com.huangrx.guava;

import com.google.common.base.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 参数检查
 *
 * @author    hrenxiang
 * @since     2022/5/24 2:28 PM
 */
public class OptionalTest {

	@Test
	public void test() {
		testNotNullValue();
		testNullValue();
	}

	private void testNotNullValue() {
		// Optional.of(T)：获得一个Optional对象，其内部包含了一个非null的T数据类型实例，若T=null，则立刻报错。
		// Optional.absent()：获得一个Optional对象，其内部包含了空值
		// Optional.fromNullable(T)：将一个T的实例转换为Optional对象，T的实例可以不为空，也可以为空[Optional.fromNullable(null)，和Optional.absent()等价。
		Optional<Integer> possible = Optional.fromNullable(6);

		// boolean isPresent()：如果Optional包含的T实例不为null，则返回true；若T实例为null，返回false
		assertTrue(possible.isPresent());

		// T get()：返回Optional包含的T实例，该T实例必须不为空；否则，对包含null的Optional实例调用get()会抛出一个IllegalStateException异常
		assertEquals(6, possible.get().intValue());

		// T or(T)：若Optional实例中包含了传入的T的相同实例，返回Optional包含的该T实例，否则返回输入的T实例作为默认值
		assertEquals(6, possible.or(6).intValue());

		// T orNull()：返回Optional实例中包含的非空T实例，如果Optional中包含的是空值，返回null，逆操作是fromNullable()
		assertEquals(6, possible.orNull().intValue());
	}

	private void testNullValue() {
		Optional<Integer> absent = Optional.fromNullable(null);
		assertFalse(absent.isPresent());
		try {
			absent.get();
			fail();
		} catch (IllegalStateException e) {
			assertTrue(true);
		}
		assertEquals(1, absent.or(1).intValue());
		assertNull(absent.orNull());
	}
}