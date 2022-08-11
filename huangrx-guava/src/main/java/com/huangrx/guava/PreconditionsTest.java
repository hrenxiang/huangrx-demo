package com.huangrx.guava;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.cache.support.NullValue;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * 参数检查
 *
 * @author hrenxiang
 * @since 2022/5/24 3:05 PM
 */
public class PreconditionsTest {

    @Test
    public void testCheckArgument() {
        int i = 1;
        checkArgument(i > 0, "参数是%s, 参数必须为正整数", i);

        int b = -1;
        checkArgument(b > 0, "参数是%s, 参数必须为正整数", b);
    }

    @Test
    public void testCheckNotNull() {
        Object value = new Object();

        checkNotNull(value, "参数是null");
        System.out.println("参数不是null");

        value = null;
        checkNotNull(value, "参数是null");
    }

    @Test
    public void testCheckState() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        checkState(list.size() < 6, "集体长度应该小于6");
        System.out.println("集体长度应该小于6");

        list.add(6);
        checkState(list.size() < 6, "集体长度应该小于6");
    }

    @Test
    public void testCheckElementIndex() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3);
        // [0, size)
        checkElementIndex(list.size(), 4);

        try {
            checkElementIndex(list.size(), 3);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckPositionIndex() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3);
        // [0, size]
        checkPositionIndex(list.size(), 3);

        try {
            checkPositionIndex(list.size(), 2);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckPositionIndexs() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        checkPositionIndexes(4, 5, list.size());

        try {
            checkPositionIndexes(5, 6, list.size());
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}