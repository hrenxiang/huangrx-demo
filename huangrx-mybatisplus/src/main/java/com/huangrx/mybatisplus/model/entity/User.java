package com.huangrx.mybatisplus.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author huangrx
 * @since 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否有效
     * value  有效值 true   默认0
     * delval 无效值 false  默认1
     *
     * 数据库中，bit'0' = false , bit'1'= true
     *
     * 开启后，查询时也会在sql语句后加上 有效标识，只能查出来有效的数据
     */
    @TableLogic(value = "1", delval = "0")
    @TableField(select = false)
    private Integer valid;

    /**
     * 字段自动填充
     * FieldFill.DEFAULT 默认不填充
     * FieldFill.INSERT  添加时填充
     * FieldFill.UPDATE  更新时填充
     * FieldFill.INSERT_UPDATE 添加更新时填充
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 数据隔离 租户标识（商户id）
     */
    private Integer tenantId;

    /**
     * 版本字段
     */
    @Version
    private Integer version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
