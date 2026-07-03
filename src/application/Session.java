package application;

import application.models.Person;

public class Session {
    private static Person currentUser;

    public static void setCurrentUser(@SuppressWarnings("exports") Person person) {
        currentUser = person;
    }

    @SuppressWarnings("exports")
	public static Person getCurrentUser() {
        return currentUser;
    }
}