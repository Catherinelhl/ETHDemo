package io.bcaas.spring.frontcontroller;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/**
 * 跨域解决二
 * @author yimi
 *
 */
public class Test2 extends MappingJackson2HttpMessageConverter {

	// 做jsonp的支持的标识，在请求参数中加该参数
	private String callbackName;

	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// // 从threadLocal中获取当前的Request对象
		// HttpServletRequest request = ((ServletRequestAttributes)
		// RequestContextHolder.currentRequestAttributes())
		// .getRequest();
		// String callbackParam = request.getParameter(callbackName);
		// if (StringUtils.isEmpty(callbackParam)) {
		// // 没有找到callback参数，直接返回json数据
		// super.writeInternal(object, outputMessage);
		// } else {
		// JsonEncoding encoding =
		// getJsonEncoding(outputMessage.getHeaders().getContentType());
		// try {
		// String result = callbackParam + "(" +
		// super.getObjectMapper().writeValueAsString(object) + ");";
		// IOUtils.write(result, outputMessage.getBody(), encoding.getJavaName());
		// } catch (JsonProcessingException ex) {
		// throw new HttpMessageNotWritableException("Could not write JSON: " +
		// ex.getMessage(), ex);
		// }
		// }
		//
	}

	public String getCallbackName() {
		return callbackName;
	}

	public void setCallbackName(String callbackName) {
		this.callbackName = callbackName;
	}
}
