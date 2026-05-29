package infrastructure;

import service.CompletedMatchService;
import service.MatchesService;
import service.OngoingMatchService;
import service.NewMatchService;
import lombok.Getter;
import storages.CurrentMatchStorage;

@Getter
public class ServiceContainer {

    // Классы `bootstrap.AppContainer`, `infrastructure.ServiceContainer` и `infrastructure.DaoContainer`
        // пытаются реализовать механизм ручного внедрения зависимостей.
        // Сама идея централизованного создания и связывания объектов (сервисов, DAO) при старте приложения правильная,
        // однако текущая реализация излишне усложнена.
        //
        // AppContainer — это избыточный класс, который добавляет ненужный уровень абстракции.
            // Его единственная функция — хранить в себе `ServiceContainer`.
            // Компоненты приложения (сервлеты) могли бы получать `ServiceContainer` напрямую.
        //
        // ServiceContainer и DaoContainer смешивают две ответственности:
            // - Фабрики: они сами создают экземпляры сервисов и DAO.
            // - Реестра (Registry): они хранят созданные экземпляры и предоставляют к ним доступ.
        //
        // Разделение на `DaoContainer` и `ServiceContainer` не даёт преимуществ,
            // но заставляет передавать один контейнер в другой, усложняя общую картину.
        //
        // Более удачным вариантом было бы создавать все зависимости централизовано (в одном месте) и помещать в общий контейнер.

    private final NewMatchService newMatchService;
    private final OngoingMatchService ongoingMatchService;
    private final CompletedMatchService completedMatchService;
    private final MatchesService matchesService;

    public ServiceContainer(DaoContainer daoContainer) {
        CurrentMatchStorage matchStorage = new CurrentMatchStorage();
        this.matchesService = new MatchesService(daoContainer.matchDao());
        this.newMatchService = new NewMatchService(daoContainer.playerDao(), matchStorage);
        this.completedMatchService = new CompletedMatchService(daoContainer.matchDao(), daoContainer.playerDao(), matchStorage);
        this.ongoingMatchService = new OngoingMatchService(daoContainer.playerDao(),matchStorage,completedMatchService);

    }
}
