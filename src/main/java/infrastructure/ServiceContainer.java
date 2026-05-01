package infrastructure;

import service.MatchScoreService;
import service.NewMatchService;
import lombok.Getter;
import storages.CurrentMatchStorage;

@Getter
public class ServiceContainer {

    private final NewMatchService newMatchService;
    private final MatchScoreService matchScoreService;

    public ServiceContainer(DaoContainer daoContainer) {
        CurrentMatchStorage matchStorage = new CurrentMatchStorage();
        this.newMatchService = new NewMatchService(daoContainer.playerDao(), matchStorage);
        this.matchScoreService = new MatchScoreService(matchStorage);
    }
}
