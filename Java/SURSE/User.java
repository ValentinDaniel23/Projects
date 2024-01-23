package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

abstract public class User<T extends Comparable<T> > implements Observer {
    public static class Information {
        private final Credentials credentials;
        private String name;
        private String country;
        private int age;
        private final String gender;
        private LocalDate birthDate;
        private Information(informationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }
        public Credentials getCredentials() {
            return credentials;
        }
        public String getName() {
            return name;
        }
        public String getCountry() {
            return country;
        }
        public int getAge() {
            return age;
        }
        public String getGender() {
            return gender;
        }
        public LocalDate getBirthDate() {
            return birthDate;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setCountry(String country) {
            this.country = country;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }

        public static class informationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDate birthDate;

            public informationBuilder(Credentials credentials) {
                this.credentials = credentials;
            }
            public informationBuilder name(String name) {
                this.name = name;
                return this;
            }
            public informationBuilder country(String country) {
                this.country = country;
                return this;
            }
            public informationBuilder age(Number age) {
                if (age != null) {
                    this.age = age.intValue();
                }
                return this;
            }
            public informationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }
            public informationBuilder birthDate(String birthDate) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                this.birthDate = LocalDate.parse(birthDate, formatter);
                return this;
            }
            public Information build() {
                return new Information(this);
            }
        }
    }
    private Information userInfo;
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<T> favorites;
    public ExperienceStrategy experienceStrategy;
    private void initialise() {
        notifications = new ArrayList<>();
        favorites = new TreeSet<>();
    }
    public User(Information userInfo, int experience, AccountType accountType) {
        this.userInfo = userInfo;
        this.accountType = accountType;
        this.experience = experience;
        initialise();
    }
    public User(Information userInfo, int experience, AccountType accountType, String username) {
        this.userInfo = userInfo;
        this.accountType = accountType;
        this.experience = experience;
        this.username = username;
        initialise();
    }
    public Information getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(Information userInfo) {
        this.userInfo = userInfo;
    }
    public AccountType getAccountType() {
        return accountType;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public List<String> getNotifications() {
        return notifications;
    }
    public SortedSet<T> getFavorites() {
        return this.favorites;
    }
    public void addToFavorites(Object object) {
        if (object instanceof Actor || object instanceof Production)
            favorites.add((T) object);
    }
    public void removeFromFavorites(Object object) {
        if (object instanceof Actor || object instanceof Production)
            favorites.remove((T) object);
    }

    public void addToNotifications(String notification) {
        notifications.add(notification);
    }
    public void update(String notification) {
        notifications.add(notification);
    }
    public void updateExperience(int experience) {
        this.experience += experience;
        if (this.experience < 0) this.experience = 0;
    }
    public void logOff() {

    }
}
