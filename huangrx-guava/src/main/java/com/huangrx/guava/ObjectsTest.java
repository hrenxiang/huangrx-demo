package com.huangrx.guava;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 常用的对象方法
 *
 * @author    hrenxiang
 * @since     2022/5/24 2:09 PM
 */
public class ObjectsTest {

	@Test
	public void test() {
		testEquals();
		testHashCode();
		testCompare();
	}

	@SuppressWarnings("all")
	private void testEquals() {
		System.out.println(Objects.equal("a", "c"));
		System.out.println(Objects.equal("a", null));
		System.out.println(Objects.equal(null, "a"));
		System.out.println(Objects.equal(null, null));
	}

	private void testHashCode() {
		System.out.println(Objects.hashCode("a", "b", "c"));
		System.out.println(Objects.hashCode("c", "b", "a"));
		System.out.println(Objects.hashCode("a", "b", "c") == Objects.hashCode("c", "b", "a"));
	}

	private void testCompare() {
		assertEquals(-1, Ints.compare(1, 2));
		assertEquals(1, Ints.compare(2, 1));
		assertEquals(0, Ints.compare(1, 1));

		assertEquals(1, ComparisonChain.start().compare(1, 1).compare("aString", "aString").compareFalseFirst(true, false).result());
	}
}