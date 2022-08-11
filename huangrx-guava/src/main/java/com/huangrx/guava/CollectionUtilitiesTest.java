package com.huangrx.guava;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

/**
 * 强大的集合工具类
 *
 * @author    hrenxiang
 * @since     2022/5/24 3:13 PM
 */
public class CollectionUtilitiesTest {

	@Test
	public void testLists() {
		Lists.newArrayList();
		Lists.newArrayList(1, 2, 3);
		Lists.newArrayList(Sets.newHashSet(1, 2, 3));
		Lists.newArrayListWithCapacity(10);
		Lists.newArrayListWithExpectedSize(10);

		Lists.newLinkedList();
		Lists.newLinkedList(Sets.newHashSet(1, 2, 3));

		System.out.println(Lists.partition(Lists.newArrayList(1, 2, 3, 4, 5), 2));
		Lists.reverse(Lists.newArrayList(1, 2, 3, 4, 5));
	}

	@Test
	public void testSets() {
		// 静态工厂方法
		Sets.newHashSet();
		Sets.newHashSet(1, 2, 3);
		Sets.newHashSetWithExpectedSize(10);
		Sets.newHashSet(Lists.newArrayList(1, 2, 3));

		Sets.newLinkedHashSet();
		Sets.newLinkedHashSetWithExpectedSize(10);
		Sets.newLinkedHashSet(Lists.newArrayList(1, 2, 3));

		Sets.newTreeSet();
		Sets.newTreeSet(Lists.newArrayList(1, 2, 3));
		Sets.newTreeSet(Ordering.natural());

		// 集合运算(返回SetView)
		// 取并集[1,2,3,4,5]
		Sets.union(Sets.newHashSet(1, 2, 3), Sets.newHashSet(4, 5, 6)).toString();
		// 取交集[3]
		Sets.intersection(Sets.newHashSet(1, 2, 3), Sets.newHashSet(3, 4, 5));
		// 只在set1, 不在set2[1,2]
		Sets.difference(Sets.newHashSet(1, 2, 3), Sets.newHashSet(3, 4, 5));
		// 交集取反[1,2,4,5]
		Sets.symmetricDifference(Sets.newHashSet(1, 2, 3), Sets.newHashSet(3, 4, 5));

		// 其他工具方法
		// 返回所有集合的笛卡尔积
		Sets.cartesianProduct(Lists.newArrayList(Sets.newHashSet(1, 2), Sets.newHashSet(3, 4)));
		// 返回给定集合的所有子集
		Sets.powerSet(Sets.newHashSet(1, 2, 3));
	}
}