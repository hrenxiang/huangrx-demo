package com.huangrx.mock;

import com.huangrx.mock.dao.UserDao;
import com.huangrx.mock.po.User;
import com.huangrx.mock.service.UserService;
import lombok.val;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class HuangrxMockApplicationTests {

    @Resource
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @Test
    void contextLoads() {
        //普通的使用userService，他里面会再去调用userDao取得数据库的数据
        User user = userService.getUserById(3);
        System.out.println(user);

        // 定义当调用mock userDao的getUserById()方法，并且参数为3时，就返回id为200、name为I'm mock3的user对象
        Mockito.when(userDao.getUserById(3)).thenReturn(new User(200, "huangrx"));
        user = userService.getUserById(3);
        System.out.println(user);
    }

    /**
     * 【静态方法模拟 实例】
     *
     * mock 了一个 List
     * 这里只是为了用作 Demo 示例，通常对于 List 这种简单的类对象创建而言，直接 new 一个真实的对象即可，无需进行 mock
     *
     * verify() 会检验对象是否在前面已经执行了相关行为，
     * 这里 mockedList 在 verify 之前已经执行了 add("one") 和 clear() 行为，所以verify() 会通过。
     */
    @Test
    void testMock() {
        val mockList = mock(List.class);
        mockList.add(1);
        mockList.clear();
        verify(mockList).add(1);
        verify(mockList).clear();
    }

    /**
     * 【静态方法模拟 实例】
     *
     * 当对象 mockedList 调用 get()方法，并且参数为 1 时，返回结果为"huangrx cool"
     *
     * Mockito 的底层原理是使用 cglib 动态生成一个 代理类对象
     * 因此，mock 出来的对象其实质就是一个 代理，该代理在 没有配置 / 指定行为 的情况下，默认返回 空值。
     */
    @Test
    void testMock2() {
        val mockList = mock(List.class);
        when(mockList.get(1)).thenReturn("huangrx cool");
        System.out.println(mockList.get(1));

        // null
        System.out.println(mockList.get(1000));
    }

    @Test
    void testMock3() {

        UserDao userDao1 = mock(UserDao.class);

        //普通的使用userService，他里面会再去调用userDao取得数据库的数据
        User user = userService.getUserById(3);
        System.out.println(user);

        // 定义当调用mock userDao的getUserById()方法，并且参数为3时，就返回id为200、name为I'm mock3的user对象
        Mockito.when(userDao1.getUserById(3)).thenReturn(new User(200, "huangrx"));
        user = userService.getUserById(3);

        // bean 变了，不是容器中的bean，所以还是空
        System.out.println(user);
    }

    //@InjectMocks
    //private UserService userService;
    //
    //@Mock
    //private UserDao userDao;

    /**
     * @Mock: 创建一个Mock.
     * @InjectMocks: 创建一个实例，简单的说是这个Mock可以调用真实代码的方法，其余用@Mock（或@Spy）注解创建的mock将被注入到用该实例中。
     */
    @Test
    void testMock4() {

        //普通的使用userService，他里面会再去调用userDao取得数据库的数据
        User user = userService.getUserById(3);
        System.out.println(user);

        // 定义当调用mock userDao的getUserById()方法，并且参数为3时，就返回id为200、name为I'm mock3的user对象
        Mockito.when(userDao.getUserById(3)).thenReturn(new User(200, "huangrx"));
        user = userService.getUserById(3);

        // bean 变了，不是容器中的bean，所以还是空
        System.out.println(user);
    }

    @Test
    void testMock5() {
        Assertions.assertEquals(1,1);
        Assertions.assertEquals('a', 'a');
        //Assertions.assertEquals(1, 2, "数字不等");


        Assertions.assertNotEquals(1, 2, "等于了");


        // 如果参数是 "" ，判断还是通过的
        //Assertions.assertNotNull(null);

        // 如果参数是 "" ，判断是通不过的
        Assertions.assertNull(null);


        Assertions.assertFalse('a' == 'b', "字符不等");
        Assertions.assertTrue('a' == 'a', "字符不等");

        // 内部判断 还是 ==
        Assertions.assertNotSame(12, 1);
        Assertions.assertSame(1, 1);


        //Assertions.fail("你好");


    }


}
