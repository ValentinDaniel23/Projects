package org.example;

public interface StaffInterface {
    boolean addProductionSystem(Production p);
    boolean addActorSystem(Actor a);
    boolean removeProductionSystem(String name);
    boolean removeActorSystem(String name);
    void updateProduction(Production p);
    void updateActor(Actor a);
}
