package com.metabubble.BWC.filter;

import com.alibaba.fastjson.JSON;
import com.metabubble.BWC.common.BaseContext;
import com.metabubble.BWC.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        log.info("拦截到请求 : {}",request.getRequestURI());

        //如果未登录则返回未登录结果
        //获取本次请求的URI
        String requestURI = request.getRequestURI(); // /backend/login.html
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                //书写功能阶段，停止拦截器，暂时让请求发出
                "/admin/**",
                "/config/**",
                "/cusservice/**",
                "/merchant/**",
                "/merchantType/**",
                "/orders/**",
                "/team/**",
                "/user/**",
                "/backend/**",
                "/front/**",
                "/common/**",
        };
        //判断本次请求是否需要处理
        boolean check = check(urls,requestURI);
        //如果不需要处理，直接放行
        if (check){
            filterChain.doFilter(request,response);
            return;
        }
        //4-1判断登录状态，如果已经登录直接放行
        if (request.getSession().getAttribute("employee")!=null){

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls 放行的路径
     * @param requestURI 网页请求的URI
     * @return 返回一个布尔值
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
