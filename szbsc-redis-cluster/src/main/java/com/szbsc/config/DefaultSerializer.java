package com.szbsc.config;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class DefaultSerializer implements RedisSerializer<Object> {
	
	private final Charset charset;

	public DefaultSerializer() {
		this(Charset.forName("UTF8"));
	}

	public DefaultSerializer(Charset charset) {
		this.charset = charset;
	}

	@Override
	public byte[] serialize(Object o) throws SerializationException {
		return o == null ? null : String.valueOf(o).getBytes(this.charset);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		return bytes == null ? null : new String(bytes, this.charset);
	}
}