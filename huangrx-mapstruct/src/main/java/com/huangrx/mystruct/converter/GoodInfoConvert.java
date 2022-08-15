package com.huangrx.mystruct.converter;

import com.huangrx.mystruct.dto.GoodInfoDTO;
import com.huangrx.mystruct.po.GoodInfo;
import com.huangrx.mystruct.po.GoodType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface GoodInfoConvert  {
    GoodInfoConvert INSTANCE = Mappers.getMapper(GoodInfoConvert.class);
    // Long => String 隐式类型转换
    @Mapping(source = "good.id", target = "goodId")
    // 属性名不同
    @Mapping(source = "type.name", target = "typeName")
    // 属性名不同
    @Mapping(source = "good.title", target = "goodName")
    // 属性名不同
    @Mapping(source = "good.price", target = "goodPrice")
    GoodInfoDTO from(GoodInfo good, GoodType type);
}