package org.example;

import com.fasterxml.jackson.databind.introspect.AnnotationCollector;

import java.util.*;

public abstract class Staff<T extends Comparable<T> > extends User implements StaffInterface {
    private List<Request> assignedRequests;
    private SortedSet<T> added;
    private void initialise() {
        assignedRequests = new ArrayList<>();
        added = new TreeSet<>();
    }
    public Staff(Information userInfo, int experience, AccountType accountType) {
        super(userInfo, experience, accountType);
        initialise();
    }
    public Staff(Information userInfo, int experience, AccountType accountType, String username) {
        super(userInfo, experience, accountType, username);
        initialise();
    }
    public List<Request> getAssignedRequests() {
        return assignedRequests;
    }
    public SortedSet<T> getAdded() {
        return added;
    }
    public boolean addProductionSystem(Production p)
    {
        if (p == null) return false;
        if (IMDB.findProduction(p.getTitle()) != null) {
            return false;
        }
        experienceStrategy = new AddMaterialStrategy();
        super.updateExperience(experienceStrategy.calculateExperience(true));

        added.add((T) p);
        IMDB.getProductions().add(p);
        IMDB.addProductionUser(p, this);
        return true;
    }
    public boolean addActorSystem(Actor a)
    {
        if (a == null) return false;
        if (IMDB.findActor(a.getActorName()) != null) {
            return false;
        }
        experienceStrategy = new AddMaterialStrategy();
        super.updateExperience(experienceStrategy.calculateExperience(true));

        added.add((T) a);
        IMDB.getActors().add(a);
        IMDB.addActorUser(a, this);
        return true;
    }
    public boolean removeProductionSystem(String name)
    {
        if (name == null) return false;

        for (T addObject : added) {
            Object object = addObject;
            if (object instanceof Production) {
                Production production = (Production) object;
                if (production.getTitle().equals(name)) {
                    for (String actorName : production.getActorsName()) {
                        Actor actor = IMDB.findActor(actorName);
                        String type;
                        if (production instanceof Series) type = "Series";
                        else type = "Movie";
                        Map.Entry<String, String> entry = Map.entry(production.getTitle(), type);
                        actor.removeRoles(entry);
                    }
                    for (User user : IMDB.getUsers()) {
                        if (user.getFavorites().contains(production)) {
                            user.removeFromFavorites(production);
                            user.update("Productia: \"" + production.getTitle() + "\" pe care il aveai la favorite, a fost sters");
                        }
                    }

                    added.remove(production);
                    IMDB.getProductions().remove(production);
                    IMDB.removeProductionUser(production);
                    return true;
                }
            }
        }
        return false;
    }
    public boolean removeActorSystem(String name)
    {
        if (name == null) return false;

        for (T addObject : added) {
            Object object = addObject;
            if (object instanceof Actor) {
                Actor actor = (Actor) object;
                if (actor.getActorName().equals(name)) {
                    for (Map.Entry<String, String> entry : actor.getRoles()) {
                        String material = entry.getKey();
                        Production production = IMDB.findProduction(material);
                        production.getActorsName().remove(actor.getActorName());
                    }
                    for (User user : IMDB.getUsers()) {
                        if (user.getFavorites().contains(actor)) {
                            user.removeFromFavorites(actor);
                            user.update("Actorul: \"" + actor.getActorName() + "\" pe care il aveai la favorite, a fost sters");
                        }
                    }

                    experienceStrategy = new AddMaterialStrategy();
                    super.updateExperience(experienceStrategy.calculateExperience(true));

                    added.remove(actor);
                    IMDB.getActors().remove(actor);
                    IMDB.removeActorUser(actor);
                    return true;
                }
            }
        }
        return false;
    }
    public void updateProduction(Production p)
    {

    }
    public void updateActor(Actor a)
    {

    }
    public void solveRequest(Request r, String type)
    {
        User user = IMDB.findUser(r.getRequesterUsername());
        if (user != null) {
            if (r.getType() == RequestTypes.ACTOR_ISSUE) {
                Actor actor=IMDB.findActor(r.getTitle());
                if (actor == null) type="Reject";
                if (type.equals("Accept")) {
                    user.update("Request-ul pentru actorul \"" + actor.getActorName() + "\" a fost acceptat : " + r.getDescription());
                    user.experienceStrategy = new AddRequestStrategy();
                    user.updateExperience(user.experienceStrategy.calculateExperience(true));
                } else {
                    user.update("Request-ul pentru actorul \"" + actor.getActorName() + "\" a fost respins : " + r.getDescription());
                }
            }
            if (r.getType() == RequestTypes.MOVIE_ISSUE) {
                Production production=IMDB.findProduction(r.getTitle());
                if (production == null) type="Reject";
                if (type.equals("Accept")) {
                    user.update("Request-ul pentru actorul \"" + production.getTitle() + "\" a fost acceptat : " + r.getDescription());
                    user.experienceStrategy = new AddRequestStrategy();
                    user.updateExperience(user.experienceStrategy.calculateExperience(true));
                } else {
                    user.update("Request-ul pentru actorul \"" + production.getTitle() + "\" a fost respins : " + r.getDescription());
                }
            }
        }

        assignedRequests.remove(r);
        IMDB.getRequests().remove(r);
    }
}
