package infrastructure;

import service.NewMatchService;
import lombok.Getter;

@Getter
public class ServiceContainer {

    private final NewMatchService newMatchService;

    public ServiceContainer(DaoContainer daoContainer) {
        this.newMatchService = new NewMatchService(daoContainer.playerDao());
    }
}
