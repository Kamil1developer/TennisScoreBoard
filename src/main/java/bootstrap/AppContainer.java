package bootstrap;

import infrastructure.DaoContainer;
import infrastructure.ServiceContainer;

public class AppContainer {
    private final ServiceContainer serviceContainer;
    public AppContainer(ServiceContainer serviceContainer,DaoContainer daoContainer){
        this.serviceContainer = serviceContainer;
    }
    public ServiceContainer services(){
        return serviceContainer;
    }
}
