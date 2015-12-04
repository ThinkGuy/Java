package com.lxw.intercepter;



import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class TimerIntercepter extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		long start = System.currentTimeMillis();
		
		//执行下个拦截器，如果已经是最后一个拦截器就直接执行action。
		String result = invocation.invoke();
		
		long end = System.currentTimeMillis();
		
		System.out.println("共花费时间"+ (end - start) +" ms");
		
		return result;
		
	}

}
