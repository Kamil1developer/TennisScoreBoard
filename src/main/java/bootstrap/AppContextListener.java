package bootstrap;

import infrastructure.DaoContainer;
import infrastructure.ServiceContainer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        DaoContainer daoContainer = new DaoContainer(factory);
        ServiceContainer serviceContainer = new ServiceContainer(daoContainer);
        AppContainer container = new AppContainer(serviceContainer, daoContainer);

        servletContext.setAttribute("appContainer", container);

    }
}
