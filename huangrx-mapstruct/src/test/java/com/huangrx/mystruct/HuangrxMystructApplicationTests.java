package com.huangrx.mystruct;

import com.huangrx.mystruct.converter.GoodInfoConvert;
import com.huangrx.mystruct.converter.OrderConvert;
import com.huangrx.mystruct.converter.StudentConvert;
import com.huangrx.mystruct.dto.GoodInfoDTO;
import com.huangrx.mystruct.dto.OrderDTO;
import com.huangrx.mystruct.dto.StudentDTO;
import com.huangrx.mystruct.po.GoodInfo;
import com.huangrx.mystruct.po.GoodType;
import com.huangrx.mystruct.po.Order;
import com.huangrx.mystruct.po.Student;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class HuangrxMystructApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void from() {
        List<Order> orders = new ArrayList<>();
        Order order = Order.builder()
                .id(123L)
                .buyerPhone("11111111111")
                .buyerAddress("重庆")
                .amount(10000L)
                .payStatus(1)
                .createTime(LocalDateTime.now())
                .build();
        OrderDTO orderDTO = OrderConvert.INSTANCE.from(order);
        System.out.println("order:    " + order);
        System.out.println("orderDTO: " + orderDTO);

        System.out.println("=====================");

        orders.add(order);
        order = Order.builder()
                .id(456L)
                .buyerPhone("22222222222")
                .buyerAddress("四川")
                .amount(10001L)
                .payStatus(1)
                .createTime(LocalDateTime.now())
                .build();
        orders.add(order);
        List<OrderDTO> orderDTOS = OrderConvert.INSTANCE.from(orders);
        System.out.println(orderDTOS);
    }

    @Test
    void ignoreField() {
        Order order = Order.builder()
                .amount(1000L)
                .buyerAddress("中国华夏")
                .buyerPhone("15236325327")
                .createTime(LocalDateTime.now())
                .id(123L)
                .payStatus(2).build();
        System.out.println(order);
        System.out.println(OrderConvert.INSTANCE.ignoreField(order));
    }

    @Test
    public void testMorePoToSingleDTO() {
        GoodInfo goodInfo = GoodInfo.builder()
                .id(1L)
                .title("Mybatis技术内幕")
                .price(79.00)
                .order(100)
                .typeId(2L)
                .build();

        GoodType goodType = GoodType.builder()
                .id(2L)
                .name("计算机")
                .show(1)
                .order(3)
                .build();
        GoodInfoDTO goodInfoDTO = GoodInfoConvert.INSTANCE.from(goodInfo, goodType);
        System.out.println("goodInfo:    " + goodInfo);
        System.out.println("goodType:    " + goodType);
        System.out.println("goodInfoDTO: " + goodInfoDTO);
    }

    @Test
    public void testRule() {
        Student student = Student.builder().id(1L).name("John").age(18).admissionTime(LocalDateTime.now()).sex(0).build();
        StudentConvert studentConvert = Mappers.getMapper(StudentConvert.class);
        StudentDTO studentDTO = StudentConvert.INSTANCE.from(student);
        System.out.println("student:" + student);
        System.out.println("studentDTO: " + studentDTO);
    }
}
