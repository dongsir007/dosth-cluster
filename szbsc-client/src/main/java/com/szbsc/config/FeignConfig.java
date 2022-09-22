package com.szbsc.config;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

//@Component
public class FeignConfig implements RequestInterceptor {
	
	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;
	
	@Bean
	public Encoder feignFormEncoder() {
		return new SpringFormEncoder(new SpringEncoder(messageConverters));
	}
	
	@Bean
	public Decoder feignDecoder() {
		return new ResponseEntityDecoder(new SpringDecoder( () -> {
			FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter() {
//				@Override
//				public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
//						throws IOException, HttpMessageNotReadableException {
//					Object result = null;
//					if ("java.lang.String".equals(type.getTypeName())) {
//						result = StreamUtils.copyToString(inputMessage.getBody(), Charset.forName("utf8"));
//					} else {
//						result = super.read(type, contextClass, inputMessage);
//					}
//					return result;
//				}
			};
			
			FastJsonConfig fastJsonConfig = new FastJsonConfig();
			fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
			
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			converter.setSupportedMediaTypes(mediaTypes);
			
			converter.setFastJsonConfig(fastJsonConfig);
			
			return new HttpMessageConverters(converter);
		}));
	}

	@Override
	public void apply(RequestTemplate template) {
	}
	
	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}