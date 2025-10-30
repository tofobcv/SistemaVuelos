package corp.local.sistemavuelos.web;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        // URLs públicas que no requieren autenticación
        if (path.startsWith("/login") || path.startsWith("/registro") || 
            path.startsWith("/recuperacionPassword") || path.startsWith("/verificarEmail") ||
            path.equals("/") || path.equals("/index.jsp") || 
            path.startsWith("/buscarVuelos") || path.startsWith("/resultados.jsp") ||
            path.startsWith("/js/") || path.startsWith("/css/") || path.startsWith("/resources/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Para URLs protegidas, verificar si el usuario está autenticado
        if (session == null || session.getAttribute("usuario") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    
    @Override
    public void destroy() {}
}