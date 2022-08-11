package com.huangrx.definition;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

/**
 * 提供精确的加减乘除运算
 *
 * @author    hrenxiang
 * @since     2022/6/19 18:09
 */
public class BigDecimalUtils {

	/**
	 * 默认保留位：2
	 */
	private static final int DEFAULT_SCALE = 2;

	/**
	 * 默认四舍五入规则为：>=.5进位处理
	 */
	private static final RoundingMode DEFAULT_ROUND = RoundingMode.HALF_UP;
	
	/**
	 * 提供精确的加法运算。
	 *
	 * @param v1 		被加数
	 * @param v2 		加数
	 * @return 			两个参数的和
	 */
	public static Double add(String v1, String v2){
        BigDecimal b1 = new BigDecimal(v1);    
        BigDecimal b2 = new BigDecimal(v2);    
        return b1.add(b2).setScale(DEFAULT_SCALE, DEFAULT_ROUND).doubleValue();
    }

	/**
	 * 提供精确的加法运算。
	 *
	 * @param v1 		被加数
	 * @param v2 		加数
	 * @return 			两个参数的和
	 */
	public static Double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).setScale(DEFAULT_SCALE, DEFAULT_ROUND).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1 		被减数
	 * @param v2 		减数
	 * @return 			两个参数的差
	 */
	public static Double sub(String v1, String v2){
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).setScale(DEFAULT_SCALE, DEFAULT_ROUND).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1 		被减数
	 * @param v2 		减数
	 * @return 			两个参数的差
	 */
	public static Double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).setScale(DEFAULT_SCALE, DEFAULT_ROUND).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1 		被乘数
	 * @param v2 		乘数
	 * @return 			两个参数的积
	 */
	public static Double mul(String v1, String v2){
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).setScale(DEFAULT_SCALE, DEFAULT_ROUND).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1 		被乘数
	 * @param v2 		乘数
	 * @return 			两个参数的积
	 */
	public static Double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).setScale(DEFAULT_SCALE, DEFAULT_ROUND).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后2位，以后的数字四舍五入。
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static Double div(double v1, double v2) {
		return div(v1, v2, DEFAULT_SCALE);
	}
	
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 *
	 * @param v1	 	除数
	 * @param v2 		被除数
	 * @param scale 	精确精度
	 * @return 			除法运算结果
	 */
	public static Double div(String v1, String v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		if (ValidateHelper.isEmpty(scale)) {
			scale = DEFAULT_SCALE;
		}

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, DEFAULT_ROUND).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 *
	 * @param v1	 	除数
	 * @param v2 		被除数
	 * @param scale 	精确精度
	 * @return 			除法运算结果
	 */
	public static Double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		if (ValidateHelper.isEmpty(scale)) {
			scale = DEFAULT_SCALE;
		}

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, DEFAULT_ROUND).doubleValue();
	}
	
	/**
	 * 比较两个数<br>
	 * v1 > v2 return 1<br>
	 * v1 = v2 return 0<br>
	 * v1 < v2 return -1
	 * @param v1 		被比较数
	 * @param v2 		比较数
	 * @return 			比较过后的返回值
	 */
	public static int compareTo(String v1, String v2){
        BigDecimal b1 = new BigDecimal(v1);    
        BigDecimal b2 = new BigDecimal(v2);    
        return b1.compareTo(b2);    
    }

	/**
	 * 比较两个数<br>
	 * v1 > v2 return 1<br>
	 * v1 = v2 return 0<br>
	 * v1 < v2 return -1
	 * @param v1 		被比较数
	 * @param v2 		比较数
	 * @return 			比较过后的返回值
	 */
	public static int compareTo(double v1, double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.compareTo(b2);
	}

	/**
	 * 返回较小数
	 *
	 * @param v1 		被比较数
	 * @param v2 		比较数
	 * @return 			较小数
	 */
	public static Double returnMin(String v1,String v2){
        BigDecimal b1 = new BigDecimal(v1);    
        BigDecimal b2 = new BigDecimal(v2);    
        return b1.min(b2).doubleValue();
    }

	/**
	 * 返回较小数
	 *
	 * @param v1 		被比较数
	 * @param v2 		比较数
	 * @return 			较小数
	 */
	public static Double returnMin(double v1, double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.min(b2).doubleValue();
	}

	/**
	 * 返回较大数
	 *
	 * @param v1 被比较数
	 * @param v2 比较数
	 * @return 较大数
	 */
	public static Double returnMax(String v1, String v2){
        BigDecimal b1 = new BigDecimal(v1);    
        BigDecimal b2 = new BigDecimal(v2);    
        return b1.max(b2).doubleValue();
    }

	/**
	 * 返回较大数
	 *
	 * @param v1 被比较数
	 * @param v2 比较数
	 * @return 较大数
	 */
	public static Double returnMax(double v1, double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.max(b2).doubleValue();
	}

	/**
	 * 处理BigDecimal数据，保留scale位小数
	 * @author:huangrx
	 *
	 * @param value
	 * @param scale
	 * @return
	 */
	public static BigDecimal getValue(BigDecimal value, int scale){
		if(!ValidateHelper.isEmpty(value)){
			return value.setScale(scale, DEFAULT_ROUND);
		}
		return value;
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 *
	 * @param v     	需要四舍五入的数字
	 * @param scale 	小数点后保留几位
	 * @return 			四舍五入后的结果
	 */
	public static Double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, DEFAULT_ROUND).doubleValue();
	}
	
	/**
	 * 将object转换为Bigdecimal
	 * 
	 * @author:huangrx
	 *
	 * @param value
	 * 				待转换的数值
	 * @return
	 */
	public static BigDecimal getBigDecimal(Object value){
		BigDecimal resultValue;
		if(value instanceof String){
			resultValue =  new BigDecimal((String)value);
		}
		else if(value instanceof Integer){
			resultValue =  new BigDecimal((Integer)value);
		}
		else if(value instanceof Long){
			resultValue =  new BigDecimal((Long)value);
		}
		else if(value instanceof Double){
			resultValue = BigDecimal.valueOf((Double) value);
		}
		else{
			resultValue = (BigDecimal) value;
		}
		
		return resultValue;
	}
	
	
	/**
	 * 将object转换为Bigdecimal,若object为空，则返回resultValue
	 * 
	 * @autor:huangrx
	 *
	 * @param value
	 * @return
	 */
	public static BigDecimal getBigDecimal(Object value,BigDecimal resultValue){
		if(ValidateHelper.isEmpty(value)){
			return resultValue;
		}
		
		resultValue = getBigDecimal(resultValue);
		
		return resultValue;
	}
	
	/**
	 * 将BigDecimal 转换成Long
	 * @autor:huangrx
	 *
	 * @param value
	 * @return
	 */
	public static Long bigDecimalToLong(BigDecimal value){
		if(value != null){
			return value.longValue();
		}
		return null;
	}
	
	/**
	 * 将BigDecimal 转换成integer
	 * @autor:huangc
	 * @date:2014年9月20日
	 *
	 * @param value
	 * @return
	 */
	public static Integer bigDecimalToInteger(BigDecimal value){
		if(value != null){
			return value.intValue();
		}
		return null;
	}

	private static class ValidateHelper {

		/**
		 * 检验对象是否为空,String 中只有空格在对象中也算空.
		 * @param object
		 * @return 为空返回true,否则false.
		 */
		@SuppressWarnings("rawtypes")
		public static boolean isEmpty(Object object) {
			if (null == object) {
				return true;
			} else if (object instanceof String) {
				return "".equals(object.toString().trim());
			} else if (object instanceof Iterable) {
				return !((Iterable) object).iterator().hasNext();
			} else if (object.getClass().isArray()) {
				return Array.getLength(object) == 0;
			} else if (object instanceof Map) {
				return ((Map) object).size() == 0;
			} else if (Number.class.isAssignableFrom(object.getClass())) {
				return false;
			} else if (Date.class.isAssignableFrom(object.getClass())) {
				return false;
			} else {
				return false;
			}
		}
	}
}