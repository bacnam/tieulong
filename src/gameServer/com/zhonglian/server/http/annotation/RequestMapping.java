package com.zhonglian.server.http.annotation;

import com.zhonglian.server.http.server.HttpMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
  String uri();
  
  HttpMethod[] method() default {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/annotation/RequestMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */