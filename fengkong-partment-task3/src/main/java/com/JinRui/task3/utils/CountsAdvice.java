package com.JinRui.task3.utils;

import org.springframework.aop.AfterAdvice;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * ClassName:CountsAdvice
 * Package: com.JinRui.task3.utils
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/11 15:52
 * @Version 1.0
 */
public class CountsAdvice implements AfterReturningAdvice {

    //后置通知
    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {




    }
}
