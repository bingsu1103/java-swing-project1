package com.library.model;

public class Librarian {
    private String id;
    private String username;
    private String password;
    private String fullName;

    public Librarian() {
    }

    public Librarian(String id, String username, String password, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String toDataString() {
        return String.join("|", id, username, password, fullName);
    }

    public static Librarian fromDataString(String dataString) {
        String[] parts = dataString.split("\\|");
        if (parts.length < 4)
            return null;
        return new Librarian(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
    }

    @Override
    public String toString() {
        return "Librarian{id='" + id + "', username='" + username + "', fullName='" + fullName + "'}";
    }
}
