package com.huangrx.guava;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 比较器
 *
 * @author    hrenxiang
 * @since     2022/5/24 2:41 PM
 */
public class OrderingTest {

	@Test
	public void testNatural() {
		// test int order
		List<Integer> unorderedIntList = Lists.newArrayList(5, 3, 2, 4, 1);
		Collections.sort(unorderedIntList, Ordering.natural());
		System.out.println(unorderedIntList);

		// test string order "Jerry", "Ohaha", "Rock", "Test", "Yeah"
		List<String> unorderedStringList = Lists.newArrayList("Test", "Jerry", "Rock", "Ohaha", "Yeah");
		Collections.sort(unorderedStringList, Ordering.natural());
		System.out.println(unorderedStringList);
	}

	@Test
	public void testFrom() {
		List<Integer> unorderedIntList = Lists.newArrayList(5, 3, 2, 4, 1);
		Collections.sort(unorderedIntList, Ordering.from(new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				return i1.compareTo(i2);
			}
		}));
		System.out.println(unorderedIntList);
	}

	@Test
	public void testReverse() {
		List<Integer> unorderedIntList = Lists.newArrayList(5, 2, 4, 3, 1);
		Collections.sort(unorderedIntList, Ordering.natural().reverse());
		System.out.println(unorderedIntList);
	}

	@Test
	public void testNullFirst() {
		List<Integer> unorderedIntList = Lists.newArrayList(5, 3, null, 4, 1);
		Collections.sort(unorderedIntList, Ordering.natural().nullsFirst());
		// [null, 1, 3, 4, 5]
		System.out.println(unorderedIntList);
	}

	@Test
	public void testNullLast() {
		List<Integer> unorderedIntList = Lists.newArrayList(5, 3, null, 4, 1);
		Collections.sort(unorderedIntList, Ordering.natural().nullsLast());
		System.out.println(unorderedIntList);
	}

	@Test
	public void testCompound() {
		List<String> unorderedStringList = Lists.newArrayList("Oest", "Jerry", "Jock", "Ohaha", "Yeah");
		List<String> orderedStringList = Lists.newArrayList("Jock", "Jerry", "Ohaha", "Oest", "Yeah");

		Ordering<String> firstLetterOrdering = Ordering.from(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.substring(0, 1).compareTo(s2.substring(0, 1));
			}
		});
		Collections.sort(unorderedStringList, firstLetterOrdering.compound(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s2.substring(1, s2.length()).compareTo(s1.substring(1, s1.length()));
			}
		}));
		System.out.println(unorderedStringList);
	}

	@Test
	public void testOnResultOf() {
		List<String> unorderedStringList = Lists.newArrayList("Oest", "Jarry", "Jock", "Ohaha", "Ybah");

		Ordering<String> secondLetterOrdering = Ordering.natural().onResultOf(new Function<String, String>() {
			@Override
			public String apply(String input) {
				// 去除首字母进行比较
				return input.substring(1, input.length());
			}
		});

		Collections.sort(unorderedStringList, secondLetterOrdering);
		System.out.println(unorderedStringList);
	}

	@Test
	public void testGreatestOf() {
		List<Integer> unorderList = Lists.newArrayList(5, 3, 2, 4, 1);
		List<Integer> orderList = Lists.newArrayList(5, 4);
		assertTrue(orderList.equals(Ordering.natural().greatestOf(unorderList, 2)));

		orderList = Lists.newArrayList(5, 4, 3, 2, 1);
		assertTrue(orderList.equals(Ordering.natural().greatestOf(unorderList, 8)));
	}

	@Test
	public void testLeastOf() {
		List<Integer> unorderList = Lists.newArrayList(5, 3, 2, 4, 1);
		List<Integer> orderList = Lists.newArrayList(1, 2);
		assertTrue(orderList.equals(Ordering.natural().leastOf(unorderList, 2)));

		orderList = Lists.newArrayList(1, 2, 3, 4, 5);
		assertTrue(orderList.equals(Ordering.natural().leastOf(unorderList, 8)));
	}

	@Test
	public void testIsOrdered() {
		// 大于可通过
		List<Integer> orderList = Lists.newArrayList(1, 2, 3, 4, 5);
		assertTrue(Ordering.natural().isOrdered(orderList));

		// 大于或等于也可通过
		orderList = Lists.newArrayList(1, 2, 2, 4, 5);
		assertTrue(Ordering.natural().isOrdered(orderList));
	}

	@Test
	public void testIsStrictlyOrdered() {
		// 大于可通过
		List<Integer> orderList = Lists.newArrayList(1, 2, 3, 4, 5);
		assertTrue(Ordering.natural().isStrictlyOrdered(orderList));

		// 大于或等于不可通过
		orderList = Lists.newArrayList(1, 2, 2, 4, 5);
		assertFalse(Ordering.natural().isStrictlyOrdered(orderList));
	}

	@Test
	public void testSortedCopy() {
		List<Integer> unorderList = Lists.newArrayList(5, 3, 2, 4, 1);
		List<Integer> orderList = Lists.newArrayList(1, 2, 3, 4, 5);
		assertTrue(orderList.equals(Ordering.natural().sortedCopy(unorderList)));
	}
}