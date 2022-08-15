package com.huangrx.mystruct.converter;

import com.huangrx.mystruct.dto.OrderDTO;
import com.huangrx.mystruct.po.Order;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderConvert {
    OrderConvert INSTANCE = Mappers.getMapper(OrderConvert.class);
    @Named(value = "useMe")
	@Mapping(source = "id", target = "orderId")
    @Mapping(source = "createTime", target = "orderTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    OrderDTO from(Order order);

    @IterableMapping(qualifiedByName = "useMe")
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "createTime", target = "orderTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    List<OrderDTO> from(List<Order> orders);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(source = "createTime", target = "orderTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    OrderDTO ignoreField(Order order);

}