package org.example;

import java.util.List;

public class Contributor extends Staff implements RequestsManager {
    public Contributor(Information userInfo, int experience, AccountType accountType) {
        super(userInfo, experience, accountType);
    }
    public Contributor(Information userInfo, int experience, AccountType accountType, String username) {
        super(userInfo, experience, accountType, username);
    }
    public boolean createRequest(Request r)
    {
        String username = r.getRequesterUsername();
        String to = r.getResolverUsername();

        if (r.getType() == RequestTypes.OTHERS || r.getType() == RequestTypes.DELETE_ACCOUNT) {
            for (Request request1 : Admin.RequestsHolder.getRequestsList()) {
                if (request1.getRequesterUsername().equals(username) && request1.getType() == r.getType()) {
                    return false;
                }
            }
            Admin.RequestsHolder.addRequest(r);
            IMDB.getRequests().add(r);
            return true;
        }
        else {
            User resolver = IMDB.findUser(to);
            List<Request> rrequest = ((Staff) resolver).getAssignedRequests();
            for (Request request1 : rrequest) {
                if (request1.getRequesterUsername().equals(username) && request1.getType() == r.getType()) {
                    return false;
                }
            }
            User user = IMDB.findUser(to);
            user.update("Ai primit un request de la utilizatorul " + username);
            ((Staff) resolver).getAssignedRequests().add(r);
            IMDB.getRequests().add(r);
            return true;
        }

    }
    public boolean removeRequest(Request r)
    {
        String username = r.getRequesterUsername();
        String to = r.getResolverUsername();

        if (r.getType() == RequestTypes.OTHERS || r.getType() == RequestTypes.DELETE_ACCOUNT) {
            for (Request request1 : Admin.RequestsHolder.getRequestsList()) {
                if (request1.getRequesterUsername().equals(username) && request1.getType() == r.getType()) {
                    Admin.RequestsHolder.removeRequest(request1);
                    IMDB.getRequests().remove(request1);
                    return true;
                }
            }
            return false;
        }
        else {
            User resolver = IMDB.findUser(to);
            List<Request> rrequest = ((Staff) resolver).getAssignedRequests();
            for (Request request1 : rrequest) {
                if (request1.getRequesterUsername().equals(username) && request1.getType() == r.getType()) {
                    ((Staff) resolver).getAssignedRequests().remove(request1);
                    IMDB.getRequests().remove(request1);
                    return false;
                }
            }
        }
        return false;
    }
}
