package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import jakarta.servlet.Filter;

@WebFilter("/*")
public class CorsFilter implements Filter {

    // Класс нигде не используется в проекте (Tomcat находит этот класс и использует его,
        // добавляя в цепочку фильтров для каждого запроса, но его выполнение не оказывает никакого эффекта
        // на работу приложения), а также не выполняет никакой полезной работы, поэтому его нужно удалить.

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String origin = request.getHeader("Origin");
        chain.doFilter(request, response);

    }

}
