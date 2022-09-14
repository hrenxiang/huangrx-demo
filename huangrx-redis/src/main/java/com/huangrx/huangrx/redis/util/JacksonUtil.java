package com.huangrx.huangrx.redis.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Sets;
import com.huangrx.huangrx.redis.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Jackson工具类
 * 优势：
 * 数据量高于百万的时候，速度和FastJson相差极小
 * API和注解支持最完善，可定制性最强
 * 支持的数据源最广泛（字符串，对象，文件、流、URL）
 *
 * @author    hrenxiang
 * @since     2022/5/24 10:21 PM
 */
@Slf4j
public class JacksonUtil {
    private static ObjectMapper mapper;

    private static final Set<JsonReadFeature> JSON_READ_FEATURES_ENABLED = Sets.newHashSet(
            //允许在JSON中使用Java注释
            JsonReadFeature.ALLOW_JAVA_COMMENTS,
            //允许 json 存在没用双引号括起来的 field
            JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES,
            //允许 json 存在使用单引号括起来的 field
            JsonReadFeature.ALLOW_SINGLE_QUOTES,
            //允许 json 存在没用引号括起来的 ascii 控制字符
            JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS,
            //允许 json number 类型的数存在前导 0 (例: 0001)
            JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS,
            //允许 json 存在 NaN, INF, -INF 作为 number 类型
            JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS,
            //允许 只有Key没有Value的情况
            JsonReadFeature.ALLOW_MISSING_VALUES,
            //允许数组json的结尾多逗号
            JsonReadFeature.ALLOW_TRAILING_COMMA
    );

    static {
        try {
            //初始化
            mapper = initMapper();
        } catch (Exception e) {
            log.error("jackson config error", e);
        }
    }

    public static ObjectMapper initMapper() {
        JsonMapper.Builder builder = JsonMapper.builder().enable(JSON_READ_FEATURES_ENABLED.toArray(new JsonReadFeature[0]));
        return initMapperConfig(builder.build());
    }

    public static ObjectMapper initMapperConfig(ObjectMapper objectMapper) {
        String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        objectMapper.setDateFormat(new SimpleDateFormat(dateTimeFormat));
        //配置序列化级别 不设置则不忽略空值，设置则忽略相应值 Include.NON_DEFAULT 属性为默认值不序列化   ｜ Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化 ｜ Include.NON_NULL 属性为NULL 不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        //配置JSON缩进支持
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        //允许单个数值当做数组处理
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        //禁止重复键, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        //禁止使用int代表Enum的order()來反序列化Enum, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        //有属性不能映射的时候不报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //对象为空时不抛异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //时间格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //允许未知字段
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        //序列化BigDecimal时之间输出原始数字还是科学计数, 默认false, 即是否以toPlainString()科学计数方式来输出
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        //识别Java8时间
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        objectMapper.registerModule(javaTimeModule);
        // 识别Guava包的类 objectMapper.registerModule(new GuavaModule());
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseUrl(URL url, Class<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseUrl error, url: %s, type: %s", url.getPath(), type), e);
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseUrl(URL url, TypeReference<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseUrl error, url: %s, type: %s", url.getPath(), type), e);
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> parseUrlToList(URL url, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(url, collectionType);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseUrlToList error, url: %s, type: %s", url.getPath(), type), e);
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseInputStream(InputStream inputStream, Class<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseInputStream error, type: %s", type), e);
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseInputStream(InputStream inputStream, TypeReference<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseInputStream error, type: %s", type), e);
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> parseInputStreamToList(InputStream inputStream, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(inputStream, collectionType);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseInputStreamToList error, type: %s", type), e);
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseFile(File file, Class<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseFile error, file path: %s, type: %s", file.getPath(), type), e.getCause());
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseFile(File file, TypeReference<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseFile error, file path: %s, type: %s", file.getPath(), type), e);
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> parseFileToList(File file, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(file, collectionType);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseFileToList error, file path: %s, type: %s", file.getPath(), type), e);
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseString(String json, Class<V> type) {
        return parseString(json, (Type) type);
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseString(String json, TypeReference<V> type) {
        return parseString(json, type.getType());
    }

    /**
     * JSON反序列化
     */
    public static <V> V parseString(String json, Type type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseString error, json: %s, type: %s", json, type), e);
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> parseStringToList(String json, Class<V> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(json, collectionType);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseStringToList error, json: %s, type: %s", json, type), e);
        }
    }

    /**
     * JSON反序列化（Map）
     */
    public static Map<String, Object> parseStringToMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
            return mapper.readValue(json, mapType);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson parseStringToMap error, json: %s, type: %s", json), e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String toJson(V v) {
        try {
            return mapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            throw new ApiException(String.format("jackson toJson error, data: %s", v), e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String toJson(List<V> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new ApiException(String.format("jackson toJson error, data: %s", list), e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String toJson(Map<String, V> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new ApiException(String.format("jackson toJson error, data: %s", map), e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> void toJson(String path, List<V> list) {
        try (Writer writer = new FileWriter(path, true)) {
            mapper.writer().writeValues(writer).writeAll(list);
        } catch (Exception e) {
            throw new ApiException(String.format("jackson toJson file error, path: %s, list: %s", path, list), e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> void toJson(String path, V v) {
        try (Writer writer = new FileWriter(path, true)) {
            mapper.writer().writeValues(writer).write(v);
        } catch (Exception e) {
            throw new ApiException(String.format("jackson toJson file error, path: %s, data: %s", path, v), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return String，默认为 null
     */
    public static String getAsString(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return null;
            }
            return getAsString(jsonNode);
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get string error, json: %s, key: %s", json, key), e);
        }
    }

    private static String getAsString(JsonNode jsonNode) {
        return jsonNode.isTextual() ? jsonNode.textValue() : jsonNode.toString();
    }

    /**
     * 从json串中获取某个字段
     * @return int，默认为 0
     */
    public static int getAsInt(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return 0;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return 0;
            }
            return jsonNode.isInt() ? jsonNode.intValue() : Integer.parseInt(getAsString(jsonNode));
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get int error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return long，默认为 0
     */
    public static long getAsLong(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return 0L;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return 0L;
            }
            return jsonNode.isLong() ? jsonNode.longValue() : Long.parseLong(getAsString(jsonNode));
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get long error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return double，默认为 0.0
     */
    public static double getAsDouble(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return 0.0;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return 0.0;
            }
            return jsonNode.isDouble() ? jsonNode.doubleValue() : Double.parseDouble(getAsString(jsonNode));
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get double error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return BigInteger，默认为 0.0
     */
    public static BigInteger getAsBigInteger(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return new BigInteger(String.valueOf(0.00));
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return new BigInteger(String.valueOf(0.00));
            }
            return jsonNode.isBigInteger() ? jsonNode.bigIntegerValue() : new BigInteger(getAsString(jsonNode));
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get big integer error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return BigDecimal，默认为 0.00
     */
    public static BigDecimal getAsBigDecimal(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return new BigDecimal("0.00");
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return new BigDecimal("0.00");
            }
            return jsonNode.isBigDecimal() ? jsonNode.decimalValue() : new BigDecimal(getAsString(jsonNode));
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get big decimal error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return boolean, 默认为false
     */
    public static boolean getAsBoolean(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return false;
            }
            if (jsonNode.isBoolean()) {
                return jsonNode.booleanValue();
            } else {
                if (jsonNode.isTextual()) {
                    String textValue = jsonNode.textValue();
                    if ("1".equals(textValue)) {
                        return true;
                    } else {
                        return BooleanUtils.toBoolean(textValue);
                    }
                } else {//number
                    return BooleanUtils.toBoolean(jsonNode.intValue());
                }
            }
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get boolean error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return byte[], 默认为 null
     */
    public static byte[] getAsBytes(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return null;
            }
            return jsonNode.isBinary() ? jsonNode.binaryValue() : getAsString(jsonNode).getBytes();
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get byte error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return object, 默认为 null
     */
    public static <V> V getAsObject(String json, String key, Class<V> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return null;
            }
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            return parseString(getAsString(jsonNode), javaType);
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get list error, json: %s, key: %s, type: %s", json, key, type), e);
        }
    }


    /**
     * 从json串中获取某个字段
     * @return list, 默认为 null
     */
    public static <V> List<V> getAsList(String json, String key, Class<V> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return null;
            }
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return parseString(getAsString(jsonNode), collectionType);
        } catch (Exception e) {
            throw new ApiException(String.format("jackson get list error, json: %s, key: %s, type: %s", json, key, type), e);
        }
    }

    /**
     * 从json串中获取某个字段
     * @return JsonNode, 默认为 null
     */
    public static JsonNode getAsJsonObject(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            if (null == node) {
                return null;
            }
            return node.get(key);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson get object from json error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 向json中添加属性
     * @return json
     */
    public static <V> String add(String json, String key, V value) {
        try {
            JsonNode node = mapper.readTree(json);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            throw new ApiException(String.format("jackson add error, json: %s, key: %s, value: %s", json, key, value), e);
        }
    }

    /**
     * 向json中添加属性
     */
    private static <V> void add(JsonNode jsonNode, String key, V value) {
        if (value instanceof String) {
            ((ObjectNode) jsonNode).put(key, (String) value);
        } else if (value instanceof Short) {
            ((ObjectNode) jsonNode).put(key, (Short) value);
        } else if (value instanceof Integer) {
            ((ObjectNode) jsonNode).put(key, (Integer) value);
        } else if (value instanceof Long) {
            ((ObjectNode) jsonNode).put(key, (Long) value);
        } else if (value instanceof Float) {
            ((ObjectNode) jsonNode).put(key, (Float) value);
        } else if (value instanceof Double) {
            ((ObjectNode) jsonNode).put(key, (Double) value);
        } else if (value instanceof BigDecimal) {
            ((ObjectNode) jsonNode).put(key, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            ((ObjectNode) jsonNode).put(key, (BigInteger) value);
        } else if (value instanceof Boolean) {
            ((ObjectNode) jsonNode).put(key, (Boolean) value);
        } else if (value instanceof byte[]) {
            ((ObjectNode) jsonNode).put(key, (byte[]) value);
        } else {
            ((ObjectNode) jsonNode).put(key, toJson(value));
        }
    }

    /**
     * 除去json中的某个属性
     * @return json
     */
    public static String remove(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            return node.toString();
        } catch (IOException e) {
            throw new ApiException(String.format("jackson remove error, json: %s, key: %s", json, key), e);
        }
    }

    /**
     * 修改json中的属性
     */
    public static <V> String update(String json, String key, V value) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            throw new ApiException(String.format("jackson update error, json: %s, key: %s, value: %s", json, key, value), e);
        }
    }

    /**
     * 格式化Json(美化)
     * @return json
     */
    public static String format(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (IOException e) {
            throw new ApiException(String.format("jackson format json error, json: %s", json), e);
        }
    }

    /**
     * 判断字符串是否是json
     * @return json
     */
    public static boolean isJson(String json) {
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}