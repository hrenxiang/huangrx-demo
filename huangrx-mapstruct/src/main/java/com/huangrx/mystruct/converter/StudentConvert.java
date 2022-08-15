package com.huangrx.mystruct.converter;

import com.huangrx.mystruct.converter.rule.CustomMapping;
import com.huangrx.mystruct.dto.StudentDTO;
import com.huangrx.mystruct.po.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(imports = {CustomMapping.class})
public interface StudentConvert {

    StudentConvert INSTANCE = Mappers.getMapper(StudentConvert.class);

    @Mapping(source = "id", target = "studentId")
    @Mapping(source = "name", target = "studentName")
    @Mapping(source = "age", target = "age")
    @Mapping(target = "ageLevel", expression = "java(CustomMapping.ageLevel(student.getAge()))")
    @Mapping(target = "sexName", expression = "java(CustomMapping.sexName(student.getSex()))")
    @Mapping(source = "admissionTime", target = "admissionDate", dateFormat = "yyyy-MM-dd")
    StudentDTO from(Student student);

    default LocalDate map(LocalDateTime time) {
        return time.toLocalDate();
    }
}