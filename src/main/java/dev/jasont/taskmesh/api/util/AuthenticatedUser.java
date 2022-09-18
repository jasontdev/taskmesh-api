package dev.jasont.taskmesh.api.util;

public class AuthenticatedUser {

    private String id;

    public AuthenticatedUser(String id) {
        this.id = id;
    }

    public void setId(String id) {
       this.id = id; 
    }

    public String getId() {
        return id;
    }
    
}
