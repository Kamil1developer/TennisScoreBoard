package bootstrap;

import infrastructure.DaoContainer;
import infrastructure.ServiceContainer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import transaction.TransactionManager;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        TransactionManager transactionManager = new TransactionManager(factory);

        DaoContainer daoContainer = new DaoContainer(factory);
        ServiceContainer serviceContainer = new ServiceContainer(daoContainer, transactionManager);
        AppContainer container = new AppContainer(serviceContainer, daoContainer);
        servletContext.setAttribute("appContainer", container);

        TestDataInitializer dataInitializer = new TestDataInitializer(factory);
        dataInitializer.initialize();

    }
}
