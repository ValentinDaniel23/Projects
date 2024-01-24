package org.example;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class IMDB {
    private static IMDB instance = null;
    private static final List<User> users;
    private static final List<Actor> actors;
    private static final List<Request> requests;
    private static final List<Production> productions;
    private static final Map<Actor, User> actorCreator;
    private static final Map<Production, User> productionCreator;
    public static boolean notificationIsOn = false;
    public static User user, admin;

    static {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
        actorCreator = new HashMap<>();
        productionCreator = new HashMap<>();

        admin = new Admin(null, 0, AccountType.ADMIN, "ADMIN");
        users.add(admin);
    }
    public static Production findProduction(String name) {
        for (Production production : productions) {
            if (production.getTitle().equals(name)) {
                return production;
            }
        }
        return null;
    }
    public static Actor findActor(String name) {
        for (Actor actor : actors) {
            if (actor.getActorName().equals(name)) {
                return actor;
            }
        }
        return null;
    }
    public static User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }
    public static User getActorUser(Actor actor) {
        return actorCreator.get(actor);
    }
    public static User getProductionUser(Production production) {
        return productionCreator.get(production);
    }
    public static void addActorUser(Actor actor, User user) {
        actorCreator.put(actor, user);
    }
    public static void addProductionUser(Production production, User user) {
        productionCreator.put(production, user);
    }
    public static void removeActorUser(Actor actor) {
        actorCreator.remove(actor);
    }
    public static void removeProductionUser(Production production) {
        productionCreator.remove(production);
    }
    public static List<User> getUsers() {
        return users;
    }

    public static List<Actor> getActors() {
        return actors;
    }

    public static List<Request> getRequests() {
        return requests;
    }

    public static List<Production> getProductions() {
        return productions;
    }
    private void parseActors() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("actors.json"));

            JSONArray jsonArray = (JSONArray) obj;

            for (Object arrayElement : jsonArray) {
                JSONObject jsonObject = (JSONObject) arrayElement;

                String name = (String) jsonObject.get("name");
                String biography = (String) jsonObject.get("biography");
                JSONArray performances = (JSONArray) jsonObject.get("performances");

                Actor actor = new Actor(name, biography);

                for (Object arrayElement2 : performances) {
                    JSONObject jsonObject2 = (JSONObject) arrayElement2;

                    String title = (String) jsonObject2.get("title");
                    String type = (String) jsonObject2.get("type");

                    actor.addRoles(actor.createEntry(title, type));
                }
                actors.add(actor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseProduction() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("production.json"));

            JSONArray jsonArray = (JSONArray) obj;

            for (Object arrayElement : jsonArray) {
                JSONObject jsonObject = (JSONObject) arrayElement;

                String title = (String) jsonObject.get("title");
                String description = (String) jsonObject.get("plot");
                Double averageRating = (Double) jsonObject.get("averageRating");
                String type = (String) jsonObject.get("type");
                Production production;

                if (type.equals("Movie")) {
                    String duration = (String) jsonObject.get("duration");
                    Number releaseYear = (Number) jsonObject.get("releaseYear");
                    if (releaseYear != null)
                        production = new Movie(title, description, averageRating, duration, releaseYear.toString());
                    else
                        production = new Movie(title, description, averageRating, duration, null);
                }
                else {
                    Number numSeasons = (Number) jsonObject.get("numSeasons");
                    Number releaseYear = (Number) jsonObject.get("releaseYear");

                    JSONObject seasonsObject = (JSONObject) jsonObject.get("seasons");

                    if (releaseYear != null)
                        production = new Series(title, description, averageRating, numSeasons.intValue(), releaseYear.toString());
                    else
                        production = new Series(title, description, averageRating, numSeasons.intValue(), null);

                    for (Object seasonKey : seasonsObject.keySet()) {
                        JSONArray episodesArray = (JSONArray) seasonsObject.get(seasonKey);
                        ((Series) production).addSeason((String) seasonKey);

                        for (Object episode : episodesArray) {
                            JSONObject episodeObject = (JSONObject) episode;

                            String episodeName = (String) episodeObject.get("episodeName");
                            String duration = (String) episodeObject.get("duration");

                            Episode episodeInstance = new Episode(episodeName, duration);

                            ((Series) production).addEpisode((String) seasonKey, episodeInstance);
                        }
                    }
                }

                JSONArray directorsArray = (JSONArray) jsonObject.get("directors");

                for (Object arrayElement2 : directorsArray) {
                    String director = (String) arrayElement2;
                    production.addDirector(director);
                }

                JSONArray actorsArray = (JSONArray) jsonObject.get("actors");

                for (Object arrayElement2 : actorsArray) {
                    String actor = (String) arrayElement2;
                    if (IMDB.findActor(actor) != null) {}
                    else {
                        // System.out.println("Lol mi-am luat teapa cu asta " + actor);
                    }
                    production.addActor(actor);
                }

                JSONArray genresArray = (JSONArray) jsonObject.get("genres");

                for (Object arrayElement2 : genresArray) {
                    String genre = (String) arrayElement2;
                    Genre genreProduction = Genre.valueOf(genre);
                    production.addGenre(genreProduction);
                }

                JSONArray performances = (JSONArray) jsonObject.get("ratings");

                for (Object arrayElement2 : performances) {
                    JSONObject jsonObject2 = (JSONObject) arrayElement2;

                    String username = (String) jsonObject2.get("username");
                    Number note = (Number) jsonObject2.get("rating");
                    String comment = (String) jsonObject2.get("comment");

                    Rating rating = new Rating(username, note.intValue(), comment);
                    production.addRating(rating);
                }
                productions.add(production);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseAccounts() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("accounts.json"));

            JSONArray jsonArray = (JSONArray) obj;

            for (Object arrayElement : jsonArray) {
                JSONObject jsonObject = (JSONObject) arrayElement;

                String username = (String) jsonObject.get("username");
                String stringExperience = (String) jsonObject.get("experience");
                int experience;
                if (stringExperience != null) {
                    experience = Integer.parseInt(stringExperience);
                }
                else {
                    experience = 10000000;
                }
                String userType = (String) jsonObject.get("userType");
                AccountType accountType = AccountType.valueOf(userType.toUpperCase());

                JSONObject jsonObject2 = (JSONObject) jsonObject.get("information");
                JSONObject jsonObject3 = (JSONObject) jsonObject2.get("credentials");
                String email = (String) jsonObject3.get("email");
                String password = (String) jsonObject3.get("password");
                Credentials credentials = new Credentials(email, password);
                String name = (String) jsonObject2.get("name");
                String country = (String) jsonObject2.get("country");
                Number age = (Number) jsonObject2.get("age");
                String gender = (String) jsonObject2.get("gender");
                String date = (String) jsonObject2.get("birthDate");

                User.Information information = new User.Information.informationBuilder(credentials)
                                                .name(name)
                                                .country(country)
                                                .age(age)
                                                .gender(gender)
                                                .birthDate(date)
                                                .build();

                User user = UserFactory.factory(information, experience, accountType, username);

                JSONArray favoriteProductions = (JSONArray) jsonObject.get("favoriteProductions");

                if (favoriteProductions != null) {
                    for (Object arrayElement2 : favoriteProductions) {
                        String productionName = (String) arrayElement2;
                        Production production = findProduction(productionName);

                        if (production == null) {}
                        else {
                            user.addToFavorites(production);
                        }
                    }
                }

                JSONArray favoriteActors = (JSONArray) jsonObject.get("favoriteActors");

                if (favoriteActors != null) {
                    for (Object arrayElement2 : favoriteActors) {
                        String actorName = (String) arrayElement2;
                        Actor actor = findActor(actorName);

                        if (actor == null) {}
                        else {
                            user.addToFavorites(actor);
                        }
                    }
                }

                JSONArray productionsContribution = (JSONArray) jsonObject.get("productionsContribution");


                if (productionsContribution != null && user instanceof Staff) {
                    for (Object arrayElement2 : productionsContribution) {
                        String productionName = (String) arrayElement2;
                        Production production = findProduction(productionName);

                        ((Staff) user).getAdded().add(production);
                        addProductionUser(production, user);
                    }
                }

                JSONArray actorsContribution = (JSONArray) jsonObject.get("actorsContribution");

                if (actorsContribution != null && user instanceof Staff) {
                    for (Object arrayElement2 : actorsContribution) {
                        String actorName = (String) arrayElement2;
                        Actor actor = findActor(actorName);

                        ((Staff) user).getAdded().add(actor);
                        addActorUser(actor, user);
                    }
                }

                JSONArray notifications = (JSONArray) jsonObject.get("notifications");

                if (notifications != null) {
                    for (Object arrayElement2 : notifications) {
                        String notificationMessage = (String) arrayElement2;
                        if (notificationMessage != null) {
                            user.addToNotifications(notificationMessage);
                        }
                    }
                }
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseRequests() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("requests.json"));

            JSONArray jsonArray = (JSONArray) obj;

            for (Object arrayElement : jsonArray) {
                JSONObject jsonObject = (JSONObject) arrayElement;

                String type = (String) jsonObject.get("type");
                RequestTypes typeRequest;
                if (type.equals("DELETE_ACCOUNT")) {
                    typeRequest = RequestTypes.DELETE_ACCOUNT;
                }
                else if (type.equals("ACTOR_ISSUE")) {
                    typeRequest = RequestTypes.ACTOR_ISSUE;
                }
                else if (type.equals("MOVIE_ISSUE")) {
                    typeRequest = RequestTypes.MOVIE_ISSUE;
                }
                else if (type.equals("OTHERS")) {
                    typeRequest = RequestTypes.OTHERS;
                }
                else typeRequest = null;

                String createdDate = (String) jsonObject.get("createdDate");
                String username = (String) jsonObject.get("username");
                String to = (String) jsonObject.get("to");
                String description = (String) jsonObject.get("description");

                Request request;
                String name;
                if (RequestTypes.ACTOR_ISSUE == typeRequest) {
                    name = (String) jsonObject.get("actorName");
                }
                else if (RequestTypes.MOVIE_ISSUE == typeRequest) {
                    name = (String) jsonObject.get("movieTitle");
                }
                else {
                    name = null;
                }
                request = new Request(typeRequest, null, name, description, username, to);
                request.getFormattedCreationDate(createdDate);

                User user = IMDB.findUser(request.getRequesterUsername());
                if (user instanceof Regular) ((Regular) user).createRequest(request);
                else ((Contributor) user).createRequest(request);

                requests.add(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private IMDB() {
        parseActors();
        parseProduction();
        parseAccounts();
        parseRequests();

        notificationIsOn = true;

        for (Production production : getProductions()) {
            for (String name : production.getActorsName()) {
                Actor actor = findActor(name);
                if (actor == null) {
                    actor = new Actor(name, "no info");
                    ((Staff) admin).addActorSystem(actor);
                }
                if (production instanceof Movie) {
                    Map.Entry<String, String> entry = Map.entry(production.getTitle(), "Movie");
                    if (!actor.getRoles().contains(entry)) actor.addRoles(entry);
                }
                else {
                    Map.Entry<String, String> entry = Map.entry(production.getTitle(), "Series");
                    if (!actor.getRoles().contains(entry)) actor.addRoles(entry);
                }
            }
        }

        for (Actor actor : getActors()) {
            for (Map.Entry<String, String> entry : actor.getRoles()) {
                String name = entry.getKey();
                String type = entry.getValue();
                Production production = findProduction(name);
                if (production == null) {
                    if (type.equals("Movie")) production = new Movie(name, "no info", 0.0, "0 minutes", "2000");
                    else production = new Series(name, "no info", 0.0, 0, "2000");
                    ((Staff) admin).addProductionSystem(production);
                }

                if (!production.getActorsName().contains(actor.getActorName())) production.addActor(actor.getActorName());
            }
        }
    }
    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public void run()
    {
        boolean ok = true;
        Scanner scanner = new Scanner(System.in);

        while (ok) {
            IMDB.user = null;

            System.out.println("Alege modul de utilizare:");
            System.out.println("1. Terminal");
            System.out.println("2. Interfata Grafica");
            System.out.println("3. Exit");

            int optiune;

            try {
                optiune = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            switch (optiune) {
                case 1:
                    ok = false;
                    cliLogin();
                    break;
                case 2:
                    LoginWindow loginWindow = new LoginWindow();
                    ok = false;
                    break;
                case 3:
                    ok = false;
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }

        scanner.close();
    }

    public static void main(String args[]) {
        IMDB app = IMDB.getInstance();
        app.run();
    }

    public void cliLogin()
    {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        System.out.println("Do you want to exit?");
        System.out.println("Type 1 for Yes");
        System.out.println("Type anything for No");

        String message = scanner.nextLine();
        if (message.equals("1")) {
            return;
        }

        while (ok) {
            System.out.println("Username:");
            String username = scanner.nextLine();
            System.out.println("Password:");
            String password = scanner.nextLine();

            if (username.equals("") || password.equals("")) {
                System.out.println("Please fill the credentials");
                continue;
            }

            user = IMDB.findUser(username);
            if (user == null) {
                System.out.println("Not a valid username");
                continue;
            }
            else {
                if (user.getUserInfo().getCredentials().getPassword().equals(password)) {
                    System.out.println("Welcome back user " + username + "!");
                    cliMenu();
                    ok = false;
                }
                else {
                    System.out.println("Not a valid password");
                    continue;
                }
            }
        }

        scanner.close();
    }

    public void cliMenu()
    {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok) {
            System.out.println("Username: " + user.getUsername());
            if (user.getAccountType() == AccountType.ADMIN) System.out.println("User experience: -");
            else System.out.println("User experience: " + user.getExperience());

            System.out.println("Choose action:");
            System.out.println("1) View productions details");
            System.out.println("2) View actors details");
            System.out.println("3) View notifications");
            System.out.println("4) Search for actor/movie/series");
            System.out.println("5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("6) Add/Delete rating to actor/movie/series");
            System.out.println("7) Add/Delete request to actor/movie/series");
            System.out.println("8) Add/Delete request Delete/Other Account");
            System.out.println("9) Solve your request");
            System.out.println("10) Solve admin request");
            System.out.println("11) Add/Delete user");
            System.out.println("12) Add/Delete actor/movie/series from system");
            System.out.println("13) Update Actor Details");
            System.out.println("14) Update Movie Details");
            System.out.println("15) User settings");
            System.out.println("16) Logout");

            int optiune;

            try {
                optiune = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            switch (optiune) {
                case 1:
                    Collections.sort(productions);
                    for (Production production : productions) {
                        production.displayInfo();
                    }
                    break;
                case 2:
                    Collections.sort(actors);
                    for (Actor actor : actors) {
                        actor.displayInfo();
                    }
                    break;
                case 3:
                    List<String> notifications = user.getNotifications();
                    for (String notification : notifications) {
                        System.out.println(notification);
                    }
                    break;
                case 4:
                    cliSearch();
                    break;
                case 5:
                    cliFavorites();
                    break;
                case 6:
                    cliRating();
                    break;
                case 7:
                    if (user.getAccountType() == AccountType.ADMIN) {
                        System.out.println("N-ai acces la optiunea asta");
                    }
                    else {
                        cliRequest();
                    }
                    break;
                case 8:
                    if (user.getAccountType() == AccountType.ADMIN) {
                        System.out.println("N-ai acces la optiunea asta");
                    }
                    else {
                        cliRequestOther();
                    }
                    break;
                case 9:
                    if (user.getAccountType() == AccountType.REGULAR) {
                        System.out.println("N-ai acces la optiunea asta");
                    }
                    else {
                        cliYourRequest();
                    }
                    break;
                case 10:
                    if (user.getAccountType() == AccountType.REGULAR || user.getAccountType() == AccountType.CONTRIBUTOR) {
                        System.out.println("N-ai acces la optiunea asta");
                    }
                    else {
                        cliAdminRequest();
                    }
                    break;
                case 11:
                    if (user.getAccountType() == AccountType.REGULAR || user.getAccountType() == AccountType.CONTRIBUTOR) {
                        System.out.println("N-ai acces la optiunea asta");
                    }
                    else {
                        cliAddDeleteUser();
                    }
                    break;
                case 12:
                    if (user.getAccountType() == AccountType.REGULAR) {
                        System.out.println("N-ai acces la optiunea asta");
                    }
                    else {
                        cliAddDeleteMaterial();
                    }
                    break;
                case 13:
                    if (user.getAccountType() == AccountType.REGULAR) {
                        System.out.println("N-ai acces la optiunea asta");
                    }
                    else {
                        cliEditActor();
                    }
                    break;
                case 14:
                    if (user.getAccountType() == AccountType.REGULAR) {
                        System.out.println("N-ai acces la optiunea asta");
                    }
                    else {
                        cliEditProduction();
                    }
                    break;
                case 15:
                    cliUserSettings();
                    break;
                case 16:
                    ok = false;
                    cliLogin();
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }

        scanner.close();
    }
    public void cliSearch()
    {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            System.out.println("Choose:");
            System.out.println("1) Actor");
            System.out.println("2) Movie");
            System.out.println("3) Series");
            System.out.println("4) Exit");

            int optiune;

            try {
                optiune = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            System.out.println("Choose name:");
            String name = scanner.nextLine();

            switch (optiune) {
                case 1:
                    Actor actor = IMDB.findActor(name);
                    if (actor == null) {
                        System.out.println("Actor inexistent");
                    }
                    else {
                        actor.displayInfo();
                    }
                    break;
                case 2:
                    Production production = IMDB.findProduction(name);
                    if (production == null || production instanceof Series) {
                        System.out.println("Film inexistent");
                    }
                    else {
                        production.displayInfo();
                    }
                    break;
                case 3:
                    Production production2 = IMDB.findProduction(name);
                    if (production2 == null || production2 instanceof Movie) {
                        System.out.println("Serial inexistent");
                    }
                    else {
                        production2.displayInfo();
                    }
                    break;
                case 4:
                    ok = false;
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    public void cliFavorites()
    {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            System.out.println("1) Add");
            System.out.println("2) Remove");
            System.out.println("3) Exit");

            int optiune1;
            try {
                optiune1 = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            if (optiune1 == 3) {
                break;
            }
            if (optiune1 < 1 && optiune1 > 2) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            System.out.println("Choose to add in favorites:");
            System.out.println("1) Actor");
            System.out.println("2) Movie");
            System.out.println("3) Series");
            System.out.println("4) Exit");

            int optiune;
            try {
                optiune = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            System.out.println("Choose name:");
            String name = scanner.nextLine();

            switch (optiune) {
                case 1:
                    if (optiune1 == 1) {
                        Actor actor = IMDB.findActor(name);
                        if (actor == null) {
                            System.out.println("Actor inexistent");
                        } else {
                            if (user.getFavorites().contains(actor)) {
                                System.out.println("Actor deja in lista de favorite");
                            }
                            else {
                                user.addToFavorites(actor);
                                System.out.println("Actor added");
                            }
                        }
                    }
                    else {
                        Actor actor = IMDB.findActor(name);
                        if (actor == null) {
                            System.out.println("Actor inexistent");
                        }
                        else {
                            if (!user.getFavorites().contains(actor)) {
                                System.out.println("Actor deja nu se afla in lista de favorite");
                            }
                            else {
                                user.removeFromFavorites(actor);
                                System.out.println("Actor removed");
                            }
                        }
                    }
                    break;
                case 2:
                    if (optiune1 == 1) {
                        Production production = IMDB.findProduction(name);
                        if (production == null || production instanceof Series) {
                            System.out.println("Film inexistent");
                        } else {
                            if (user.getFavorites().contains(production)) {
                                System.out.println("Film deja in lista de favorite");
                            }
                            else {
                                user.addToFavorites(production);
                                System.out.println("Film added");
                            }
                        }
                    }
                    else {
                        Production production = IMDB.findProduction(name);
                        if (production == null || production instanceof Series) {
                            System.out.println("Film inexistent");
                        }
                        else {
                            if (!user.getFavorites().contains(production)) {
                                System.out.println("Film deja nu se afla in lista de favorite");
                            }
                            else {
                                user.removeFromFavorites(production);
                                System.out.println("Film removed");
                            }
                        }
                    }
                    break;
                case 3:
                    if (optiune1 == 1) {
                        Production production = IMDB.findProduction(name);
                        if (production == null || production instanceof Movie) {
                            System.out.println("Serial inexistent");
                        } else {
                            if (user.getFavorites().contains(production)) {
                                System.out.println("Serial deja in lista de favorite");
                            }
                            else {
                                user.addToFavorites(production);
                                System.out.println("Serial added");
                            }
                        }
                    }
                    else {
                        Production production = IMDB.findProduction(name);
                        if (production == null || production instanceof Movie) {
                            System.out.println("Serial inexistent");
                        }
                        else {
                            if (!user.getFavorites().contains(production)) {
                                System.out.println("Serial deja nu se afla in lista de favorite");
                            }
                            else {
                                user.removeFromFavorites(production);
                                System.out.println("Serial removed");
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }
    public void cliRating()
    {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            System.out.println("Choose to give rating:");
            System.out.println("1) Movie");
            System.out.println("2) Series");
            System.out.println("3) Exit");

            int optiune;
            try {
                optiune = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            if (optiune == 3) {
                break;
            }
            if (optiune > 3 || optiune < 1) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            System.out.println("Choose name:");
            String name = scanner.nextLine();

            Production production = null;
            if (optiune == 1) {
                production = findProduction(name);
                if (production == null || production instanceof Series) {
                    System.out.println("Film inexistent");
                    continue;
                }
            }
            if (optiune == 2) {
                production = findProduction(name);
                if (production == null || production instanceof Movie) {
                    System.out.println("Serial inexistent");
                    continue;
                }
            }



            System.out.println("1) Add rating");
            System.out.println("2) Remove rating");

            int optiune1;
            try {
                optiune1 = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            if (optiune1 < 1 && optiune1 > 2) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            System.out.println("Add note:");

            int note;
            try {
                note = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            if (note < 1 || note > 10) {
                System.out.println("Te rog sa introduci un numar valid");
                continue;
            }

            System.out.println("Add comment:");

            String comment;
            comment = scanner.nextLine();
            Rating rating = new Rating(user.getUsername(), note, comment);

            boolean ans = false;

            if (optiune1 == 1) {
                ans = production.addRating(rating);
                if (ans == false) {
                    System.out.println("ai deja un rating pentru aceasta productie");
                }
                else {
                    System.out.println("rating adaugat");
                }
            }
            else {
                ans = production.removeRating(rating.getUsername());
                if (ans == false) {
                    System.out.println("deja ai sters rating pentru aceasta productie");
                }
                else {
                    System.out.println("rating sters");
                }
            }
        }
    }
    public void cliRequest() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            System.out.println("Choose to give request:");
            System.out.println("1) Actor");
            System.out.println("2) Movie");
            System.out.println("3) Series");
            System.out.println("4) Exit");

            int optiune;
            try {
                optiune = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            if (optiune == 4) {
                break;
            }
            if (optiune > 4 || optiune < 1) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            System.out.println("Choose name:");
            String name = scanner.nextLine();

            Production production = null;
            Actor actor = null;
            if (optiune == 1) {
                actor = findActor(name);
                if (actor == null) {
                    System.out.println("Actor inexistent");
                    continue;
                }
            }
            if (optiune == 2) {
                production = findProduction(name);
                if (production == null || production instanceof Series) {
                    System.out.println("Film inexistent");
                    continue;
                }
            }
            if (optiune == 3) {
                production = findProduction(name);
                if (production == null || production instanceof Movie) {
                    System.out.println("Serial inexistent");
                    continue;
                }
            }

            RequestTypes requestTypes = null;
            if (optiune == 1) {
                requestTypes = RequestTypes.ACTOR_ISSUE;
            }
            else {
                requestTypes = RequestTypes.MOVIE_ISSUE;
            }
            LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            String username = IMDB.user.getUsername();
            String movieTitle = name;

            String to = null;
            if (optiune == 1) {
                to = IMDB.getActorUser(actor).getUsername();
            }
            else {
                to = IMDB.getProductionUser(production).getUsername();
            }
            System.out.println("Adauga un comentariu:");
            String message = scanner.nextLine();

            Request request = new Request(requestTypes, currentTime, movieTitle, message, username, to);
            if (user instanceof Regular) {
                boolean found = ((Regular) user).createRequest(request);
                if (found == false) {
                    System.out.println("Request deja existent");
                }
                else {
                    System.out.println("Request adaugat");
                }
            }
            else {
                boolean found = ((Contributor) user).createRequest(request);
                if (found == false) {
                    System.out.println("Request deja existent");
                }
                else {
                    System.out.println("Request adaugat");
                }
            }
        }
    }
    public void cliRequestOther() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            System.out.println("Choose to give request:");
            System.out.println("1) Other");
            System.out.println("2) Delete User");
            System.out.println("3) Exit");

            int optiune;
            try {
                optiune = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }

            if (optiune == 3) {
                break;
            }
            if (optiune > 3 || optiune < 1) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            RequestTypes requestTypes = null;
            if (optiune == 1) {
                requestTypes = RequestTypes.OTHERS;
            }
            else {
                requestTypes = RequestTypes.DELETE_ACCOUNT;
            }
            LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            String username = IMDB.user.getUsername();
            String to = "ADMIN";

            System.out.println("Adauga un comentariu:");
            String message = scanner.nextLine();

            Request request = new Request(requestTypes, currentTime, null, message, username, to);
            if (user instanceof Regular) {
                boolean found = ((Regular) user).createRequest(request);
                if (found == false) {
                    System.out.println("Request deja existent");
                }
                else {
                    System.out.println("Request adaugat");
                }
            }
            else {
                boolean found = ((Contributor) user).createRequest(request);
                if (found == false) {
                    System.out.println("Request deja existent");
                }
                else {
                    System.out.println("Request adaugat");
                }
            }
        }
    }
    public void cliYourRequest() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            List<Request> requests = ((Staff) user).getAssignedRequests();
            for (Request request : requests) {
                System.out.println(request.getTitle() + " - " + request.getRequesterUsername() + ": " + request.getDescription() + " -> " + request.getCreationDate());
            }
            System.out.println("What requests do you want? - " + requests.size() + " request-uri");
            System.out.println("Type 0 for exit");

            int pos;
            try {
                pos = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos == 0) {
                break;
            }
            if (pos < 0 || pos > requests.size()) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            Request request = requests.get(pos - 1);
            System.out.println("1: Accept");
            System.out.println("2: Reject");

            int pos1;
            try {
                pos1 = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos1 < 1 || pos1 > 2) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            if (pos1 == 1) {
                ((Staff) IMDB.user).solveRequest(request, "Accept");
                System.out.println("Request acceptat");
            }
            if (pos1 == 2) {
                ((Staff) IMDB.user).solveRequest(request, "Reject");
                System.out.println("Request respins");
            }
        }
    }

    public void cliAdminRequest() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            List<Request> requests = Admin.RequestsHolder.getRequestsList();
            for (Request request : requests) {
                System.out.println(request.getType().name() + " - " + request.getRequesterUsername() + ": " + request.getDescription() + " -> " + request.getCreationDate());
            }
            System.out.println("What requests do you want? - " + requests.size() + " request-uri");
            System.out.println("Type 0 for exit");

            int pos;
            try {
                pos = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos == 0) {
                break;
            }
            if (pos < 0 || pos > requests.size()) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            Request request = requests.get(pos - 1);
            System.out.println("1: Accept");
            System.out.println("2: Reject");

            int pos1;
            try {
                pos1 = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos1 < 1 || pos1 > 2) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }
            System.out.println(pos1);
            if (pos1 == 1) {
                Admin.RequestsHolder.solveRequest(request, "Accept");
                System.out.println("Request acceptat");
            }
            if (pos1 == 2) {
                Admin.RequestsHolder.solveRequest(request, "Reject");
                System.out.println("Request respins");
            }
        }
    }
    public void cliAddDeleteUser() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            System.out.println("1: Add user");
            System.out.println("2: Delete user");
            System.out.println("3: Exit");

            int pos;
            try {
                pos = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos == 3) {
                break;
            }
            if (pos < 1 || pos > 3) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            if (pos == 1) {
                User user1 = null;
                String email;
                String password;
                String name;
                String username;

                System.out.println("Write name");
                name = scanner.nextLine();
                System.out.println("Write email");
                email = scanner.nextLine();
                System.out.println("Write password");
                password = scanner.nextLine();
                System.out.println("Write username");
                username = scanner.nextLine();
                Credentials credentials = new Credentials(email, password);
                User.Information information =new User.Information.informationBuilder(credentials).name(name).build();

                user1 = new Regular(information, 0, AccountType.REGULAR, username);
                users.add(user1);
                System.out.println("User " + username + " adaugat.");
            }
            else {
                System.out.println("Write username to delete");
                String username = scanner.nextLine();

                if (username != null && !username.isEmpty()) {
                    boolean found = ((Admin) IMDB.user).deleteUser(username);

                    if (!found) {
                        System.out.println("No user deleted");
                    }
                    else {
                        System.out.println("User " + username + " deleted");
                    }
                } else {
                    System.out.println("No user deleted");
                }
            }
        }
    }
    public void cliAddDeleteMaterial() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok)
        {
            System.out.println("1: Add Material");
            System.out.println("2: Delete Material");
            System.out.println("3: Exit");

            int pos;
            try {
                pos = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos == 3) {
                break;
            }
            if (pos < 1 || pos > 3) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            System.out.println("1: Actor");
            System.out.println("2: Movie");
            System.out.println("3: Series");

            int pos1;
            try {
                pos1 = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos < 1 || pos > 3) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            if (pos == 1) {
                System.out.println("Name:");
                String name = scanner.nextLine();
                if (pos1 == 1) {
                    System.out.println("Biography:");
                    String biography;
                    biography = scanner.nextLine();

                    Actor actor1 = new Actor(name, biography);
                    boolean found = ((Staff) user).addActorSystem(actor1);
                    if (found) System.out.println("Actor " + name + " adaugat");
                    else System.out.println("Actor " + name + " nu s-a adaugat");
                }
                if (pos1 == 2) {
                    System.out.println("Plot:");
                    String plot;
                    plot = scanner.nextLine();
                    System.out.println("releaseYear:");
                    String releaseYear;
                    releaseYear = scanner.nextLine();

                    Movie movie = new Movie(name, plot, 0.0, "", releaseYear);
                    boolean found = ((Staff) user).addProductionSystem(movie);
                    if (found) System.out.println("Film " + name + " adaugat");
                    else System.out.println("Film " + name + " nu s-a adaugat");
                }
                if (pos1 == 3) {
                    System.out.println("Plot:");
                    String plot;
                    plot = scanner.nextLine();
                    System.out.println("releaseYear:");
                    String releaseYear;
                    releaseYear = scanner.nextLine();

                    Series series = new Series(name, plot, 0.0, 0, releaseYear);
                    boolean found = ((Staff) user).addProductionSystem(series);
                    if (found) System.out.println("Serial " + name + " adaugat");
                    else System.out.println("Serial " + name + " nu s-a adaugat");
                }
            }
            else {
                for (Object object : ((Staff) user).getAdded()) {
                    if (object instanceof Actor) {
                        System.out.println("Actor : " + ((Actor) object).getActorName());
                    }
                    else {
                        if (object instanceof Movie) System.out.println("Material : " + ((Movie) object).getTitle());
                        else System.out.println("Material : " + ((Series) object).getTitle());
                    }
                }
                System.out.println("Name:");
                String name = scanner.nextLine();
                if (pos1 == 1) {
                    Actor actor1 = findActor(name);
                    boolean found = ((Staff) user).removeActorSystem(name);
                    if (found) System.out.println("Actor " + name + " sters");
                    else System.out.println("Actor " + name + " nu s-a sters");
                }
                if (pos1 == 2) {
                    Production production1 = findProduction(name);
                    boolean found = ((Staff) user).removeProductionSystem(name);
                    if (found) System.out.println("Film " + name + " sters");
                    else System.out.println("Film " + name + " nu s-a sters");
                }
                if (pos1 == 3) {
                    Production production1 = findProduction(name);
                    boolean found = ((Staff) user).removeProductionSystem(name);
                    if (found) System.out.println("Serial " + name + " sters");
                    else System.out.println("Serial " + name + " nu s-a sters");
                }
            }
        }
    }
    public void cliEditActor() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok) {
            System.out.println("1: Actor");
            System.out.println("2: Exit");

            int pos;
            try {
                pos = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos == 2) {
                break;
            }
            if (pos < 1 || pos > 2) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            System.out.println("Name:");
            String name = scanner.nextLine();

            Actor actor = findActor(name);
            if (actor == null) {
                System.out.println("Actor inexistent");
            }
            else {
                System.out.println("Biography:");
                String biography = scanner.nextLine();
                actor.setBiography(biography);

                System.out.println("Actor setat");
            }
        }
    }
    public void cliEditProduction() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok) {
            System.out.println("1: Movie");
            System.out.println("2: Serial");
            System.out.println("3: Exit");

            int pos;
            try {
                pos = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos == 3) {
                break;
            }
            if (pos < 1 || pos > 3) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            System.out.println("Name:");
            String name = scanner.nextLine();

            Production production = findProduction(name);
            if (production == null) {
                System.out.println("Productie inexistenta");
            }
            else {
                if (production instanceof Movie && pos == 2) {
                    System.out.println("Productie inexistenta");
                    break;
                }
                if (production instanceof Series && pos == 1) {
                    System.out.println("Productie inexistenta");
                    break;
                }
                System.out.println("Plot:");
                String description = scanner.nextLine();
                System.out.println("releaseYear:");
                String releaseYear = scanner.nextLine();

                production.setDescription(description);
                if (production instanceof Movie) {
                    ((Movie) production).setReleaseYear(releaseYear);
                }
                else {
                    ((Series) production).setReleaseYear(releaseYear);
                }

                System.out.println("Productie setata");
            }
        }
    }
    public void cliUserSettings() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;

        while (ok) {
            System.out.println("1: Settings");
            System.out.println("2: Exit");

            int pos;
            try {
                pos = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Te rog sa introduci un numar valid.");
                scanner.nextLine();
                continue;
            }
            if (pos == 2) {
                break;
            }
            if (pos < 1 || pos > 2) {
                System.out.println("Te rog sa introduci un numar valid.");
                continue;
            }

            System.out.println("Edit password:");
            String password = scanner.nextLine();

            System.out.println("Edit mail");
            String email = scanner.nextLine();

            System.out.println("Edit country");
            String country = scanner.nextLine();

            user.getUserInfo().setCountry(country);
            user.getUserInfo().getCredentials().setPassword(password);
            user.getUserInfo().getCredentials().setEmail(email);

            System.out.println("User setat");
        }
    }
}

