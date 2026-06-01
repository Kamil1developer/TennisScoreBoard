package infrastructure;

import service.CompletedMatchService;
import service.MatchesService;
import service.OngoingMatchService;
import service.NewMatchService;
import lombok.Getter;
import storages.CurrentMatchStorage;
import transaction.TransactionManager;

@Getter
public class ServiceContainer {

    private final NewMatchService newMatchService;
    private final OngoingMatchService ongoingMatchService;
    private final CompletedMatchService completedMatchService;
    private final MatchesService matchesService;

    public ServiceContainer(DaoContainer daoContainer,TransactionManager transactionManager) {
        CurrentMatchStorage matchStorage = new CurrentMatchStorage();

        this.matchesService = new MatchesService(daoContainer.matchDao(), transactionManager);
        this.newMatchService = new NewMatchService(daoContainer.playerDao(), matchStorage, transactionManager);
        this.completedMatchService = new CompletedMatchService(daoContainer.matchDao(), daoContainer.playerDao(), matchStorage, transactionManager);
        this.ongoingMatchService = new OngoingMatchService(daoContainer.playerDao(),matchStorage,completedMatchService, transactionManager);

    }
}
