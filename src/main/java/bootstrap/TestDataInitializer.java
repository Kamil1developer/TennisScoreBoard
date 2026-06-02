package bootstrap;

import entity.Match;
import entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class TestDataInitializer {
    private final SessionFactory factory;
    public TestDataInitializer(SessionFactory factory){
        this.factory = factory;
    }
    public void initialize(){
        createTestMatches();
    }
    private void createTestMatches(){

    }
}
