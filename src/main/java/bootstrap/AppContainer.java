package bootstrap;

import infrastructure.DaoContainer;
import infrastructure.ServiceContainer;

public class AppContainer {
    private final DaoContainer daoContainer;
    private final ServiceContainer serviceContainer;
    public AppContainer(ServiceContainer serviceContainer,DaoContainer daoContainer){
        this.serviceContainer = serviceContainer;
        this.daoContainer = daoContainer;
    }
    public ServiceContainer services(){
        return serviceContainer;
    }
}
