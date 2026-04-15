package com.library.service;

import com.library.dao.LibrarianDAO;
import com.library.model.Librarian;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final LibrarianDAO librarianDAO;
    private static Librarian currentLibrarian;

    public AuthService() {
        this.librarianDAO = new LibrarianDAO();
    }

    public boolean login(String username, String password) {
        Librarian librarian = librarianDAO.findByUsername(username);
        if (librarian != null && BCrypt.checkpw(password, librarian.getPassword())) {
            currentLibrarian = librarian;
            return true;
        }
        return false;
    }

    public static Librarian getCurrentLibrarian() {
        return currentLibrarian;
    }
}
