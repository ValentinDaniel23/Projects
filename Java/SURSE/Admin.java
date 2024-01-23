package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Admin extends Staff {
    public static class RequestsHolder {
        private static List<Request> requestsList = new ArrayList<>();

        public static void addRequest(Request request) {
            requestsList.add(request);
        }

        public static void removeRequest(Request request) {
            requestsList.remove(request);
        }

        public static void solveRequest(Request r, String type)
        {
            User user = IMDB.findUser(r.getRequesterUsername());
            if (user != null) {
                if (r.getType() == RequestTypes.DELETE_ACCOUNT) {
                    if (type.equals("Accept")) {
                        user.update("Request-ul a fost acceptat : " + r.getDescription());
                    } else {
                        user.update("Request-ul a fost respins : " + r.getDescription());
                    }
                }
                if (r.getType() == RequestTypes.OTHERS) {
                    if (type.equals("Accept")) {
                        user.update("Request-ul a fost acceptat : " + r.getDescription());
                    } else {
                        user.update("Request-ul a fost respins : " + r.getDescription());
                    }
                }
            }

            requestsList.remove(r);
            IMDB.getRequests().remove(r);
        }

        public static List<Request> getRequestsList() {
            return requestsList;
        }
    }
    public Admin(Information userInfo, int experience, AccountType accountType) {
        super(userInfo, experience, accountType);
    }
    public Admin(Information userInfo, int experience, AccountType accountType, String username) {
        super(userInfo, experience, accountType, username);
    }
    public boolean deleteUser(String name) {
        User user = IMDB.findUser(name);
        if (user == null) return false;
        if (user.getAccountType() == AccountType.ADMIN) return false;

        for (Production production : IMDB.getProductions()) {
            for (Rating rating : production.getRatings()) {
                if (rating.getUsername() == user.getUsername()) {
                    production.removeRating(user.getUsername());
                    break;
                }
            }
        }

        List <Request> requests = new ArrayList<>(IMDB.getRequests());
        for (Request request : requests) {
            String giver = request.getRequesterUsername();
            String receiver = request.getResolverUsername();

            if (giver.equals(user.getUsername())) {
                if (user instanceof Regular) ((Regular) user).removeRequest(request);
                else ((Contributor) user).removeRequest(request);
            }
            else if (receiver.equals(user.getUsername())) {
                User user1 = IMDB.findUser(giver);
                Request newRequest = new Request(RequestTypes.OTHERS, request.getCreationDate(), request.getTitle(), request.getDescription(), request.getRequesterUsername(), "ADMIN");
                if (user1 instanceof Regular) {
                    ((Regular) user1).removeRequest(request);
                    ((Regular) user1).createRequest(newRequest);
                }
                else {
                    ((Contributor) user1).removeRequest(request);
                    ((Contributor) user1).createRequest(newRequest);
                }
            }
        }

        if (user instanceof Staff) {
            for (Object object : ((Staff) user).getAdded()) {
                if (object instanceof Actor) {
                    Actor actor = (Actor) object;
                    IMDB.removeActorUser(actor);
                    IMDB.addActorUser(actor, IMDB.admin);
                    ((Staff)IMDB.admin).getAdded().add(actor);
                }
                else {
                    Production production = (Production) object;
                    IMDB.removeProductionUser(production);
                    IMDB.addProductionUser(production, IMDB.admin);
                    ((Staff)IMDB.admin).getAdded().add(production);
                }
            }
        }
        IMDB.getUsers().remove(user);
        return true;
    }
    public static String generateStrongPassword(String username) {

        return "ceva";
    }
}
