package com.huangrx.sms.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * http util 远程请求工具类
 *
 * @author    hrenxiang
 * @since    2022/4/29 1:57 PM
 */
public class HttpUtils {

	private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	private static final String CONSTANT_1 = "?";
	private static final String CONSTANT_2 = "&";
	private static final String CONSTANT_3 = "=";
	
	private HttpUtils() {
	}

	private static CloseableHttpClient getCloseableHttpClient() {
		return HttpClients.createDefault();
	}

	private static class DefaultRequestConfigBuilder {
		private static final RequestConfig INSTANCE =
				RequestConfig.custom()
						//设置连接超时时间
						.setConnectTimeout(5000)
						// 设置请求超时时间
						.setConnectionRequestTimeout(5000)
						.setSocketTimeout(5000)
						//默认允许自动重定向
						.setRedirectsEnabled(true)
						.build();
	}

	/**
	 * 获取默认请求配置
	 *
	 * @return RequestConfig
	 */
	private static RequestConfig getDefaultRequestConfig() {
		return DefaultRequestConfigBuilder.INSTANCE;
	}

	/**
	 * 获取SSl客户端
	 *
	 * @return closeableHttpClient
	 */
	private static CloseableHttpClient getSslClientDefault() {
		try {
			//信任所有
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			logger.info("ssl客户端创建失败", e);
			throw new RuntimeException("ssl客户端创建失败");
		}

	}

	/**
	 * 简单的get请求
	 *
	 * @param url url String
	 * @return response String
	 */
	public static String httpGet(String url) {
		return httpGet(url, "UTF-8");
	}

	/**
	 * 所有的get最终都会调用到这个里, 可设置字符集
	 *
	 * @param url url String
	 * @param charset 字符集
	 * @return response String
	 */
	public static String httpGet(String url, String charset) {
		CloseableHttpClient closeableHttpClient = getCloseableHttpClient();
		HttpGet httpGet = getHttpGetMethod(url);
		try {
			CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			logger.info("httpGet请求错误:url=" + url, e);
		} finally {
			closeHttpClient(closeableHttpClient);
		}
		return null;
	}

	/**
	 * 带参数的get请求
	 *
	 * @param url url String
	 * @param param 参数
	 * @return response String
	 */
	public static String httpGet(String url, Object param) {
		return httpGet(url, param, "UTF-8");
	}

	/**
	 * 带参数与字符集的get请求
	 *
	 * @param url url String
	 * @param param 参数
	 * @param charset 字符集
	 * @return response String
	 */
	public static String httpGet(String url, Object param, String charset) {
		return httpGet(doGetParams(url, param), charset);
	}

	/**
	 * 简单的post请求
	 *
	 * @param url url String
	 * @return response String
	 */
	public static String httpPost(String url) {
		return httpPost(url, "UTF-8");
	}

	/**
	 * 所有的post请求都会调用这个，可设置字符集
	 *
	 * @param url url String
	 * @param charset 字符集
	 * @return response String
	 */
	public static String httpPost(String url, String charset) {
		CloseableHttpClient closeableHttpClient = getCloseableHttpClient();
		HttpPost httpPost = getHttpPostMethod(url);
		try {
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			logger.info("httpPost请求错误:url=" + url, e);
		} finally {
			closeHttpClient(closeableHttpClient);
		}
		return null;
	}

	/**
	 * 带参数的post请求
	 *
	 * @param url url String
	 * @param param 参数
	 * @return response String
	 */
	public static String httpPost(String url, Object param) {
		return httpPost(url, param, "UTF-8");
	}

	/**
	 * 带参数和字符集的post请求
	 *
	 * @param url url String
	 * @param param 参数
	 * @param charset 字符集
	 * @return response String
	 */
	public static String httpPost(String url, Object param, String charset) {
		return httpPost(url, param, null, charset);
	}

	/**
	 * post请求 底层方法
	 *
	 * @param url url String
	 * @param param 参数
	 * @param headers 请求头
	 * @param charset 字符集
	 * @return response string
	 */
	public static String httpPost(String url, Object param, Map<String, String> headers, String charset) {

		CloseableHttpClient closeableHttpClient = getCloseableHttpClient();
		HttpPost postMethod = getHttpPostMethod(url);
		setHeaders(postMethod, headers);
		List<NameValuePair> nvps = doPostParam(param);
		try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(nvps, charset);
			postMethod.setEntity(httpEntity);
			CloseableHttpResponse response = closeableHttpClient.execute(postMethod);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (UnsupportedEncodingException e) {
			logger.info("httpPost添加表单数据失败", e);
		} catch (ClientProtocolException e) {
			logger.info("httpPost协议失败", e);
		} catch (IOException e) {
			logger.info("httpPost请求失败", e);
		} finally {
			closeHttpClient(closeableHttpClient);
		}

		return null;
	}

	/**
	 * 构建 get请求的 url 和 参数
	 * @param url url String
	 * @param param 参数
	 * @return response String
	 */
	private static String doGetParams(String url, Object param) {
		if (param == null) {
			return url;
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(url);
		if (param instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) param;
			Set<Map.Entry<String, Object>> entries = map.entrySet();
			Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
			
			if (url.contains(CONSTANT_1)) {
				while (iterator.hasNext()) {
					Map.Entry<String, Object> next = iterator.next();
					builder.append(CONSTANT_2).append(next.getKey()).append(CONSTANT_3).append(next.getValue());
				}
			} else {
				int count = 0;
				while (iterator.hasNext()) {
					Map.Entry<String, Object> next = iterator.next();
					if (count == 0) {
						builder.append(CONSTANT_1).append(next.getKey()).append(CONSTANT_3).append(next.getValue());
						count++;
					} else {
						builder.append(CONSTANT_2).append(next.getKey()).append(CONSTANT_3).append(next.getValue());
					}
					
				}
			}
		} else {
			
			List<Field> fields = FieldUtils.getAllFieldsList(param.getClass());
			if (url.contains(CONSTANT_1)) {
				for (Field field : fields) {
					String fieldName = field.getName();
					try {
						field.setAccessible(true);
						Object value = field.get(param);
						if (value != null) {
							builder.append(CONSTANT_2).append(fieldName).append(CONSTANT_3).append(value);
						}
					} catch (IllegalAccessException e) {
						logger.info("反射参数失败:参数=" + fieldName, e);
					}
				}
				
			} else {
				int count = 0;
				for (Field field : fields) {
					String fieldName = field.getName();
					try {
						field.setAccessible(true);
						Object value = field.get(param);
						if (value != null) {
							if (count == 0) {
								builder.append(CONSTANT_1).append(fieldName).append(CONSTANT_3).append(value);
								count++;
							} else {
								builder.append(CONSTANT_2).append(fieldName).append(CONSTANT_3).append(value);
							}
						}
					} catch (IllegalAccessException e) {
						logger.info("反射参数失败:参数=" + fieldName, e);
					}
				}
			}
		}
		return builder.toString();
	}

	/**
	 * 构建post请求的参数
	 *
	 * @param param 参数
	 * @return List
	 */
	private static List<NameValuePair> doPostParam(Object param) {
		List<NameValuePair> nvps = new ArrayList<>();

		if (param instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) param;
			for (Map.Entry<String, Object> next : map.entrySet()) {
				nvps.add(new BasicNameValuePair(next.getKey(), next.getValue().toString()));
			}
		} else {

			List<Field> fields = FieldUtils.getAllFieldsList(param.getClass());
			for (Field field : fields) {
				String fieldName = field.getName();
				try {
					field.setAccessible(true);
					Object value = field.get(param);
					if (value != null) {
						nvps.add(new BasicNameValuePair(fieldName, value.toString()));
					}
				} catch (IllegalAccessException e) {
					logger.info("反射参数失败:参数=" + fieldName, e);
				}
			}
		}
		return nvps;

	}

	/**
	 * 使用url 构建 httpGet
	 *
	 * @param url url string
	 * @return httpGet
	 */
	private static HttpGet getHttpGetMethod(String url) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(getDefaultRequestConfig());
		return httpGet;
	}

	/**
	 * 使用url 构建 httpPost
	 *
	 * @param url url string
	 * @return httpPost
	 */
	private static HttpPost getHttpPostMethod(String url) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(getDefaultRequestConfig());
		return httpPost;
	}

	/**
	 * rest post 请求方法需要json格式的参数
	 *
	 * @param url url
	 * @param param 参数
	 * @return response string
	 */
	public static String httpRestPost(String url, Object param) {
		return httpRestPost(url, param, "UTF-8");
	}

	/**
	 * rest post 请求方法需要json格式的参数 带字符集
	 *
	 * @param url url
	 * @param param 参数
	 * @param charset 字符集
	 * @return response string
	 */
	public static String httpRestPost(String url, Object param, String charset) {
		return httpRestPost(url, param, null, charset);
	}

	/**
	 * rest post 请求方法需要json格式的参数 带字符集 带headers
	 *
	 * @param url url
	 * @param param 参数
	 * @param headers 请求头
	 * @param charset 字符集
	 * @return response string
	 */
	public static String httpRestPost(String url, Object param, Map<String, String> headers, String charset) {

		CloseableHttpClient closeableHttpClient = getCloseableHttpClient();
		HttpPost postMethod = getHttpPostMethod(url);
		setHeaders(postMethod, headers);
		postMethod.addHeader("Content-type", "application/json; charset=" + charset);
		postMethod.setHeader("Accept", "application/json");
		postMethod.setEntity(new StringEntity(JSON.toJSONString(param), Charset.forName(charset)));
		try {
			CloseableHttpResponse response = closeableHttpClient.execute(postMethod);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			logger.info("http请求异常", e);
		}

		return null;
	}
	
	public static String httpsGet(String url) {
		return httpsGet(url, "UTF-8");
	}
	
	public static String httpsGet(String url, String charset) {
		return httpsGet(url, null, charset);
	}
	
	public static String httpsGet(String url, Object param) {
		return httpsGet(url, param, "UTF-8");
	}
	
	public static String httpsGet(String url, Object param, String charset) {
		CloseableHttpClient closeableHttpClient = getSslClientDefault();
		HttpGet getMethod = getHttpGetMethod(doGetParams(url, param));
		try {
			CloseableHttpResponse response = closeableHttpClient.execute(getMethod);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			logger.info("httpsGet请求失败", e);
		} finally {
			closeHttpClient(closeableHttpClient);
		}
		return null;
	}
	
	public static String httpsPost(String url) {
		return httpsPost(url, "UTF-8");
	}
	
	public static String httpsPost(String url, String charset) {
		return httpsPost(url, null, charset);
	}
	
	public static String httpsPost(String url, Object param) {
		return httpsPost(url, param, "UTF-8");
	}
	
	public static String httpsPost(String url, Object param, String charset) {
		
		return httpsPost(url, param, null, charset);
	}
	
	public static String httpsRestPost(String url, Object param) {
		return httpsRestPost(url, param, "UTF-8");
	}
	
	public static String httpsRestPost(String url, Object param, String charset) {
		return httpsRestPost(url, param, null, charset);
	}
	
	public static String httpPostUploadFile(String url, String localFilePath, String filedName, String fileOriginName, Object params) {
		return httpPostUploadFile(url, new File(localFilePath), filedName, fileOriginName, params);
	}
	
	public static String httpPostUploadFile(String url, File file, String filedName, String fileOriginName, Object params) {
		
		if (file == null) {
			logger.info("传入的file对象不能为空");
			return null;
		}
		if (file.isDirectory()) {
			logger.info("传入的file对象不能是文件夹,请输入正确的文件路径");
			return null;
		}
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			
			return httpPostUploadFile(url, inputStream, filedName, fileOriginName, params);
			
		} catch (FileNotFoundException e) {
			logger.info("创建文件io失败");
		} finally {
			
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.info("关闭上传文件流失败", e);
				}
			}
			
		}
		return null;
	}
	
	public static String httpPostUploadFile(String url, InputStream inputStream, String filedName, String fileOriginName, Object params) {
		
		return httpPostUploadFile(url, inputStream, filedName, fileOriginName, null, params);
	}
	
	public static String httpPostUploadFile(String url, InputStream inputStream, String filedName, String fileOriginName, Map<String, String> headers, Object params) {
		if (inputStream == null) {
			logger.info("文件输入流为空");
			return null;
		}
		CloseableHttpClient httpClient = getCloseableHttpClient();
		HttpPost postMethod = getHttpPostMethod(url);
		setHeaders(postMethod, headers);
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.addBinaryBody(filedName, inputStream, ContentType.MULTIPART_FORM_DATA, fileOriginName);
		// 设置上传的其他参数
		setUploadParams(multipartEntityBuilder, params);
		HttpEntity reqEntity = multipartEntityBuilder.build();
		postMethod.setEntity(reqEntity);
		try {
			CloseableHttpResponse response = httpClient.execute(postMethod);
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeHttpClient(httpClient);
		}
		return null;
	}
	
	private static void setUploadParams(MultipartEntityBuilder multipartEntityBuilder, Object param) {
		
		if (param == null) {
			return;
		}
		if (param instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) param;
			for (Map.Entry<String, Object> next : map.entrySet()) {
				//处理中文乱码问题
				ContentType contentType = ContentType.create("text/plain", "UTF-8");
				StringBody stringBody = new StringBody(next.getValue().toString(), contentType);
				multipartEntityBuilder.addPart(next.getKey(), stringBody);

			}
		} else {

			List<Field> fields = FieldUtils.getAllFieldsList(param.getClass());
			for (Field field : fields) {
				String fieldName = field.getName();
				try {
					field.setAccessible(true);
					Object value = field.get(param);
					if (value != null) {
						//处理中文乱码问题
						ContentType contentType = ContentType.create("text/plain", "UTF-8");
						StringBody stringBody = new StringBody(value.toString(), contentType);
						multipartEntityBuilder.addPart(fieldName, stringBody);
					}
				} catch (IllegalAccessException e) {
					logger.info("反射参数失败:参数=" + fieldName, e);
				}
			}
		}
	}
	
	private static void setHeaders(HttpRequest request, Map<String, String> headers) {
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> next : headers.entrySet()) {
				request.setHeader(next.getKey(), next.getValue());
			}
		}
	}
	
	public static String httpGet(String url, Object param, Map<String, String> headers, String charset) {
		CloseableHttpClient closeableHttpClient = getCloseableHttpClient();
		url = doGetParams(url, param);
		HttpGet httpGetMethod = getHttpGetMethod(url);
		setHeaders(httpGetMethod, headers);
		try {
			CloseableHttpResponse response = closeableHttpClient.execute(httpGetMethod);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			logger.info("httpGet请求错误", e);
		} finally {
			closeHttpClient(closeableHttpClient);
		}
		
		return null;
	}
	
	public static String httpsGet(String url, Object param, Map<String, String> headers, String charset) {
		CloseableHttpClient closeableHttpClient = getSslClientDefault();
		url = doGetParams(url, param);
		HttpGet httpGetMethod = getHttpGetMethod(url);
		setHeaders(httpGetMethod, headers);
		try {
			CloseableHttpResponse response = closeableHttpClient.execute(httpGetMethod);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			logger.info("httpsGet请求错误", e);
		} finally {
			closeHttpClient(closeableHttpClient);
		}
		return null;
	}
	
	public static String httpsPost(String url, Object param, Map<String, String> headers, String charset) {
		CloseableHttpClient sslClientDefault = getSslClientDefault();
		HttpPost postMethod = getHttpPostMethod(url);
		setHeaders(postMethod, headers);
		List<NameValuePair> nvps = doPostParam(param);
		try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(nvps, charset);
			postMethod.setEntity(httpEntity);
			CloseableHttpResponse response = sslClientDefault.execute(postMethod);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (UnsupportedEncodingException e) {
			logger.info("httpsPost添加表单数据失败", e);
		} catch (ClientProtocolException e) {
			logger.info("httpsPost协议失败", e);
		} catch (IOException e) {
			logger.info("httpsPost请求失败", e);
		} finally {
			closeHttpClient(sslClientDefault);
		}
		return null;
	}
	
	public static String httpsRestPost(String url, Object param, Map<String, String> headers, String charset) {
		
		CloseableHttpClient sslClientDefault = getSslClientDefault();
		HttpPost postMethod = getHttpPostMethod(url);
		setHeaders(postMethod, headers);
		postMethod.addHeader("Content-type", "application/json; charset=" + charset);
		postMethod.setHeader("Accept", "application/json");
		postMethod.setEntity(new StringEntity(JSON.toJSONString(param), Charset.forName(charset)));
		try {
			CloseableHttpResponse response = sslClientDefault.execute(postMethod);
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (IOException e) {
			logger.info("https请求异常", e);
		}
		return null;
	}

	/**
	 * 关闭http客户端
	 *
	 * @param httpClient 客户端
	 */
	private static void closeHttpClient(Closeable httpClient) {
		try {
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (IOException e) {
			logger.info("httpClient关闭失败", e);
		}

	}


	/**
	 * sms 工具类
	 *
	 * @param host
	 * @param path
	 * @param method
	 * @param headers
	 * @param querys
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doGet(String host, String path, String method,
									 Map<String, String> headers,
									 Map<String, String> querys)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpGet request = new HttpGet(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doPost(String host, String path, String method,
									  Map<String, String> headers,
									  Map<String, String> querys,
									  Map<String, String> bodys)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		if (bodys != null) {
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

			for (String key : bodys.keySet()) {
				nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
			formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
			request.setEntity(formEntity);
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doPost(String host, String path, String method,
									  Map<String, String> headers,
									  Map<String, String> querys,
									  String body)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "utf-8"));
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doPost(String host, String path, String method,
									  Map<String, String> headers,
									  Map<String, String> querys,
									  byte[] body)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		if (body != null) {
			request.setEntity(new ByteArrayEntity(body));
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doPut(String host, String path, String method,
									 Map<String, String> headers,
									 Map<String, String> querys,
									 String body)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPut request = new HttpPut(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "utf-8"));
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doPut(String host, String path, String method,
									 Map<String, String> headers,
									 Map<String, String> querys,
									 byte[] body)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPut request = new HttpPut(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		if (body != null) {
			request.setEntity(new ByteArrayEntity(body));
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doDelete(String host, String path, String method,
										Map<String, String> headers,
										Map<String, String> querys)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		return httpClient.execute(request);
	}

	private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(host);
		if (!StringUtils.isBlank(path)) {
			sbUrl.append(path);
		}
		if (null != querys) {
			StringBuilder sbQuery = new StringBuilder();
			for (Map.Entry<String, String> query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
					sbQuery.append(query.getValue());
				}
				if (!StringUtils.isBlank(query.getKey())) {
					sbQuery.append(query.getKey());
					if (!StringUtils.isBlank(query.getValue())) {
						sbQuery.append("=");
						sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}

		return sbUrl.toString();
	}

	private static HttpClient wrapClient(String host) {
		HttpClient httpClient = null;
		if (host.startsWith("http://")) {
			httpClient = getSslClientDefault();
		}

		return httpClient;
	}
	
}
