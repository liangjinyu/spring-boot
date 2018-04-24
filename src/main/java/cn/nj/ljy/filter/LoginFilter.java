package cn.nj.ljy.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import cn.nj.ljy.common.Const;
import cn.nj.ljy.util.WebPathMatcher;

@Order(1)
@WebFilter(filterName = "BaseFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);
    public static final String DEFAULT_STATIC_RESOURCES = "default-static";
    private Set<String> excludesPattern = new HashSet<String>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludesPattern.add("/test/*");
        excludesPattern.add("/login/*");
        excludesPattern.add("/logout/*");
        excludesPattern.add("/ljy");
        excludesPattern.add("/resume");
        excludesPattern.add("/yx");
        excludesPattern.add("/testTransaction");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        LOGGER.info(" in base filter ");
        if (isExcluded(request)) {
            chain.doFilter(request, response);
            return;
        }

        handle(request, response, chain);
        return;
    }

    private void handle(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 判断是否登录
    	HttpSession session = ((HttpServletRequest)request).getSession(true);
    	if(session == null){
    		LOGGER.warn(" no session");
    		((HttpServletResponse)response).sendRedirect("/");
    	}else{
    		LOGGER.info("has session "+session);
    		String userName = (String)session.getAttribute(Const.USER_SESSION);
    		if(userName != null ){
    			 chain.doFilter(request, response);
    		}else{
    			((HttpServletResponse)response).sendRedirect("/");
    		}
    		
    	}
    	
       

    }

    protected boolean isExcluded(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        return "/".equals(requestURI)||(excludesPattern.contains(DEFAULT_STATIC_RESOURCES) // 设置默认的静态资源不拦截
                && isStaticResource(requestURI)) || WebPathMatcher.matchAny(excludesPattern, requestURI);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    public static boolean matchAny(Set<String> patterns, String requestURI) {
        if (patterns == null) {
            return false;
        }

        if (!requestURI.startsWith("/")) {
            requestURI = "/" + requestURI;
        }

        for (String pattern : patterns) {
            if (WebPathMatcher.matches(pattern, requestURI)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否为静态资源, 则Filter不过滤静态请求
     *
     * 注：目前模板项目自带404 500页面，会有默认的图片，需要exlcude掉
     *
     * @param requestURI
     * @return
     */
    public static boolean isStaticResource(String requestURI) {
        return requestURI.endsWith(".htm") || requestURI.endsWith(".html") || requestURI.endsWith(".jsp") // JSP默认也不拦截！
                || requestURI.endsWith(".js") || requestURI.endsWith(".css") || requestURI.endsWith(".jpg")
                || requestURI.endsWith(".jpeg") || requestURI.endsWith(".png") || requestURI.endsWith(".gif")
                || requestURI.endsWith(".ico");
    }
}