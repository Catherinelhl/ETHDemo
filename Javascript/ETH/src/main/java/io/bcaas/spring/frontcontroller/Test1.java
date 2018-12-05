package io.bcaas.spring.frontcontroller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;
/**
 * 解决跨域问题(方式一扩展消息转化器)
 * @author yimi
 *
 */
@ControllerAdvice
public class Test1 extends AbstractJsonpResponseBodyAdvice {
 
public Test1(){
super("callback");
}
}
