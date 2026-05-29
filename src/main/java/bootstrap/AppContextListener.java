package bootstrap;

import infrastructure.DaoContainer;
import infrastructure.ServiceContainer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@WebListener
public class AppContextListener implements ServletContextListener {

    // TODO: Класс не реализует метод `contextDestroyed`, который вызывается при остановке приложения.
        // В приложении есть ресурсы, которые требуют явного освобождения (например, `SessionFactory` из Hibernate, которая управляет пулом соединений).
        // Без реализации `contextDestroyed` нет гарантированного способа их закрыть.
        // Это приведёт к утечкам ресурсов, особенно в окружении сервера приложений, где приложение может многократно перезапускаться.

    // Отсутствует обработка ошибок при инициализации SessionFactory.
        // Если вызов `new Configuration().configure().buildSessionFactory()` не удастся
        // (например, из-за ошибки в `hibernate.cfg.xml` или недоступности БД),
        // приложение просто упадёт с неинформативной ошибкой в логах сервера.
        // Стоит обернуть критические участки инициализации в `try-catch` для логирования осмысленного сообщения об ошибке.
        // Это значительно упростит диагностику проблем при развёртывании.

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        DaoContainer daoContainer = new DaoContainer(factory);
        ServiceContainer serviceContainer = new ServiceContainer(daoContainer);
        AppContainer container = new AppContainer(serviceContainer, daoContainer);

        // Для помещения объектов в контекст можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        servletContext.setAttribute("appContainer", container);

        TestDataInitializer dataInitializer = new TestDataInitializer(factory);
        dataInitializer.initialize();

    }
}
