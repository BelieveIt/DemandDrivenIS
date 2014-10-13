package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SysUser;

import commonbean.LoginBean;

/**
 * Servlet Filter implementation class RegionSupplyChainAdmin
 */
@WebFilter("/region/inventory/*")
public class RegionSupplyChainAdminFilter implements Filter {
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		LoginBean loginBean = (LoginBean)((HttpServletRequest)request).getSession().getAttribute("loginBean");
        if (loginBean != null && loginBean.getSysUser()!=null) {
        	SysUser sysUser = loginBean.getSysUser();
        	if(sysUser.getUsertype().equals(SysUser.REGION_INVENTORY)){
        		chain.doFilter(request, response);
        	}else {
        		String contextPath = ((HttpServletRequest)request).getContextPath();
                ((HttpServletResponse)response).sendRedirect(contextPath + "/login.xhtml");
			}
        }else {
        	String contextPath = ((HttpServletRequest)request).getContextPath();
            ((HttpServletResponse)response).sendRedirect(contextPath + "/login.xhtml");
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
