package infrastructure;

import service.CompletedMatchService;
import service.OngoingMatchService;
import service.NewMatchService;
import lombok.Getter;
import storages.CurrentMatchStorage;

@Getter
public class ServiceContainer {

    private final NewMatchService newMatchService;
    private final OngoingMatchService ongoingMatchService;
    private final CompletedMatchService completedMatchService;

    public ServiceContainer(DaoContainer daoContainer) {
        CurrentMatchStorage matchStorage = new CurrentMatchStorage();
        this.newMatchService = new NewMatchService(daoContainer.playerDao(), matchStorage);
        this.completedMatchService = new CompletedMatchService(daoContainer.matchDao(), daoContainer.playerDao(), matchStorage);
        this.ongoingMatchService = new OngoingMatchService(daoContainer.playerDao(),matchStorage,completedMatchService);

    }
}
