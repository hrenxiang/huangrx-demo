package com.huangrx.mybatisplus.model.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author huangrx
 * @since 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Dept extends Model<Dept> {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 部门
     */
    private String deptName;

    /**
     * 邮箱
     */
    private String deptEmail;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
