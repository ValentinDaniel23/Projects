package org.example;

public class UserFactory {
    private static UserFactory instance = null;
    private UserFactory() {
    }
    public static UserFactory getInstance() {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }
    public static User factory(User.Information userInfo, int experience, AccountType accountType, String username) {
        if (accountType == AccountType.ADMIN)
            return new Admin(userInfo, experience, accountType, username);
        if (accountType == AccountType.CONTRIBUTOR)
            return new Contributor(userInfo, experience, accountType, username);
        if (accountType == AccountType.REGULAR)
            return new Regular(userInfo, experience, accountType, username);
        return null;
    }
}
