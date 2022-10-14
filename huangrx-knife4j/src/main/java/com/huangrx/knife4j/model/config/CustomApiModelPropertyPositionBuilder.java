//package com.huangrx.knife4j.model.config;
//
//import com.fasterxml.jackson.databind.introspect.AnnotatedField;
//import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.NonNull;
//import org.springframework.stereotype.Component;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
//import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
//import springfox.documentation.swagger.common.SwaggerPluginSupport;
//
//import java.lang.reflect.Field;
//import java.util.Optional;
//
//import static springfox.documentation.schema.Annotations.findPropertyAnnotation;
//import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;
//
///**
// * @author        hrenxiang
// * @since         2022-10-13 15:47:30
// */
//@Component
//public class CustomApiModelPropertyPositionBuilder implements ModelPropertyBuilderPlugin {
//
//    @Override
//    public boolean supports(@NonNull DocumentationType delimiter) {
//        return SwaggerPluginSupport.pluginDoesApply(delimiter);
//    }
//
//    @Override
//    public void apply(ModelPropertyContext context) {
//
//        Optional<BeanPropertyDefinition> beanPropertyDefinitionOpt = context.getBeanPropertyDefinition();
//        Optional<ApiModelProperty> annotation = Optional.empty();
//
//        if (context.getAnnotatedElement().isPresent()) {
//            annotation = findApiModePropertyAnnotation(context.getAnnotatedElement().get());
//        }
//
//        if (context.getBeanPropertyDefinition().isPresent()) {
//            annotation = findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class);
//        }
//
//        if (beanPropertyDefinitionOpt.isPresent()) {
//            BeanPropertyDefinition beanPropertyDefinition = beanPropertyDefinitionOpt.get();
//            if (annotation.isPresent() && annotation.get().position() == 0) {
//                return;
//            }
//
//            AnnotatedField field = beanPropertyDefinition.getField();
//            Class<?> clazz = field.getDeclaringClass();
//            Field[] declaredFields = clazz.getDeclaredFields();
//            Field declaredField;
//
//            try {
//                declaredField = clazz.getDeclaredField(field.getName());
//            } catch (NoSuchFieldException | SecurityException e) {
//                return;
//            }
//
//            int indexOf = -1;
//            for (int i = 0; i < declaredFields.length; i++) {
//                if(declaredFields[i] == declaredField) {
//                    indexOf = i;
//                    break;
//                }
//            }
//            if (indexOf != -1) {
//                context.getSpecificationBuilder().position(indexOf);
//            }
//
//        }
//
//    }
//
//}