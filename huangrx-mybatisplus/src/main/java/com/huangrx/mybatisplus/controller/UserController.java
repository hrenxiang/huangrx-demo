package com.huangrx.mybatisplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huangrx.mybatisplus.config.ApiContextRunner;
import com.huangrx.mybatisplus.mapper.primary.UserMapper;
import com.huangrx.mybatisplus.model.entity.ProductStoreParam;
import com.huangrx.mybatisplus.model.entity.User;
import com.huangrx.mybatisplus.service.UserService;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author huangrx
 * @since 2022-04-27
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ApiContextRunner runner;

    /**
     * 添加用户信息
     * @param username 用户姓名
     * @param age 用户年纪
     * @param email 用户邮箱
     * @return 添加是否成功
     */
    @RequestMapping(value = "/insertUser" , method = RequestMethod.POST)
    public Boolean insertUser(String username,Integer age,String email) {
        User user = new User();
        user.setName(username);
        user.setAge(age);
        user.setEmail(email);

        return userService.save(user);
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    @RequestMapping(value = "/queryUser" , method = RequestMethod.GET)
    public User queryUser(String id) {
        return userService.getOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getId, id)
        );
    }

    /**
     * 根据多个ID 批量查询数据
     */
    @RequestMapping(value = "/queryUserByIds", method = RequestMethod.POST)
    public List<User> getUserById(@RequestBody ProductStoreParam productStoreParam) {
        //List<String> list = Arrays.asList(ids);
        return userMapper.selectBatchIds(productStoreParam.getStoreIds());
    }

    /**
     * 查询指定的字段
     * ==>  Preparing: SELECT id,age,name FROM user WHERE (age IN (?,?))
     * ==> Parameters: 12(String), 15(String)
     * <==    Columns: id, age, name
     * <==        Row: 1360943043256320, 12, hrx
     * <==        Row: 1360943146827776, 15, www
     * <==        Row: 1562267237673443330, 12, fff
     * <==      Total: 3
     */
    @RequestMapping(value = "/querySpecifiedField", method = RequestMethod.GET)
    public List<User> querySpecifiedField(String[] ages) {
        return userMapper.selectList(
                Wrappers.<User>lambdaQuery()
                        .select(User::getId, User::getAge, User::getName)
                        .in(User::getAge, Arrays.asList(ages))
        );
    }

    /**
     * 查询指定的字段, 平均值，最大最小值
     */
    @RequestMapping(value = "/querySpecifiedField2", method = RequestMethod.GET)
    public List<Map<String, Object>> querySpecifiedField2(String[] ages) {
        return userMapper.selectMaps(
                Wrappers.<User>query()
                        .select("avg(age) avg_age", "min(age) min_age", "max(age) max_age", "tenant_id")
                        .groupBy("tenant_id")
        );
    }

    /**
     * 查询指定的字段, 返回结果只有ID
     */
    @RequestMapping(value = "/querySpecifiedField3", method = RequestMethod.GET)
    public List<Object> querySpecifiedField3(String[] ages) {
        return userMapper.selectObjs(
                Wrappers.<User>lambdaQuery()
                        .select(User::getId, User::getAge, User::getName)
                        .in(User::getAge, Arrays.asList(ages))
        );
    }

    /**
     * 查询指定的字段, 返回结果只有ID
     */
    @RequestMapping(value = "/queryCount", method = RequestMethod.GET)
    public Integer queryCount(String[] ages) {
        return userMapper.selectCount(
                Wrappers.<User>lambdaQuery()
                        .in(User::getAge, Arrays.asList(ages))
        );
    }

    /**
     * 直接用实体对象查询相关信息
     * 可以看到，是根据实体对象中的非空属性，进行了 等值匹配查询。
     *
     * @param username 用户姓名
     * @param age      用户年纪
     * @param email    用户邮箱
     * @return 添加是否成功
     */
    @RequestMapping(value = "/queryByUserEntity" , method = RequestMethod.GET)
    public List<User> queryByUserEntity(String username, Integer age, String email) {
        User user = new User();
        user.setName(username);
        user.setAge(age);
        user.setEmail(email);

        return userMapper.selectList(
                new LambdaQueryWrapper<>(user)
        );
    }

    /**
     * 物理删除
     *
     * 根据ID删除用户
     * @param id 用户ID
     * @return 删除是否成功
     */
    @RequestMapping(value = "/deleteUserById" , method = RequestMethod.DELETE)
    public Boolean deleteUserById(String id) {
        return userService.removeById(id);
    }

    /**
     * 物理删除
     *
     * 根据条件删除相关用户信息
     */
    @RequestMapping(value = "/deleteUserByConditions", method = RequestMethod.DELETE)
    public Boolean deleteUserByConditions(String name, String age) {
        //构造条件
        Map<String,Object> map = new HashMap<>(5);
        map.put("name",name);
        map.put("age",age);
        //执行删除
        return userService.removeByMap(map);
    }

    /**
     * 逻辑删除
     *
     * 根据ID删除用户
     * @param id 用户ID
     * @return 删除是否成功
     */
    @RequestMapping(value = "/logicDeleteUserById" , method = RequestMethod.DELETE)
    public Boolean logicDeleteUserById(String id) {
        return userService.removeById(id);
    }

    /**
     * 根据主键ID修改用户数据
     */
    @RequestMapping(value = "/updateUserById", method = RequestMethod.PUT)
    public Integer updateUserById(@RequestBody User user) {
        return this.userMapper.updateById(user);
    }

    /**
     * 根据条件修改用户数据
     */
    @RequestMapping(value = "/updateUserByConditions", method = RequestMethod.PUT)
    public Integer updateUserByConditions(@RequestBody User user) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.<User>lambdaUpdate()
                .eq(User::getId, user.getId());
//        测试乐观锁
//        this.userMapper.update(
//                user,
//                wrapper
//        );

        return this.userMapper.update(
                user,
                wrapper
        );
    }

    /**
     * 在mqpper.xml中自定义原始sql，不概述了，mybatis章节已经讲到
     */

    /**
     * 自定义接口使用条件构造器 第一种方式使用
     */
    @RequestMapping(value = "/queryUserAll1" , method = RequestMethod.GET)
    public List<User> queryUserAll1(String[] ids) {
        return userMapper.selectAll1(
                Wrappers.<User>lambdaQuery()
                        .in(User::getId, Arrays.asList(ids))
        );
    }

    /**
     * 自定义接口使用条件构造器 第二种方式使用
     */
    @RequestMapping(value = "/queryUserAll2" , method = RequestMethod.GET)
    public List<User> queryUserAll2(String[] ids) {
        return userMapper.selectAll2(
                Wrappers.<User>lambdaQuery()
                        .in(User::getId, Arrays.asList(ids))
        );
    }

    /**
     * 根据条件进行分页查询，mybatis-plus 需要闲配置 分页拦截器
     *
     * 下面这种分页方式比较适合于传统应用中，表格分页的开发。需要给出总条数，以及每页多少条。
     */
    @RequestMapping(value = "/queryUserByPage", method = RequestMethod.GET)
    public Page<User> queryUserByPage(Integer[] ages, Integer pageNum, Integer pageSize) {
        Page<User> userPage = new Page<>(pageNum, pageSize);

        userMapper.selectPage(
                userPage,
                Wrappers.<User>lambdaQuery()
                        .in(User::getAge, Arrays.asList(ages))
        );

        return userPage;
    }

    /**
     * 根据条件进行分页查询，mybatis-plus 需要闲配置 分页拦截器
     *
     * 下面这种分页方式比较适合于不查询总记录数的分页-下拉分页（就是淘宝商品可以一直向下滚动那种）
     * 这种情况下的分页通常就不需要查询总条数了，如果查询总条数浪费数据库的计算资源，使响应时间变长。
     * 所以我们应该只做分页数据查询，不查询总条数。设置page分页的第三个参数为false。
     */
    @RequestMapping(value = "/queryUserByPage2", method = RequestMethod.GET)
    public Page<User> queryUserByPage2(Integer[] ages, Integer pageNum, Integer pageSize) {
        Page<User> userPage = new Page<>(pageNum, pageSize, false);

        userMapper.selectPage(
                userPage,
                Wrappers.<User>lambdaQuery()
                        .in(User::getAge, Arrays.asList(ages))
        );

        return userPage;
    }

    /*
     * @InterceptorIgnore(tenantLine = "true") // 跨租户查询，sql不会再加租户标识
     * @Select("select es.merchant_id from fyc_pos.es_store es group by es.merchant_id")
     * List<Integer> queryMerchantId();
     */

    public static void main(String[] args) {
        List<Integer> storeIds = new ArrayList<>();
        for (Integer storeId : storeIds) {
            System.out.println(storeId);
        }
    }
}

