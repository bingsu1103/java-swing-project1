package com.library.dao;

import com.library.model.Librarian;
import com.library.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarianDAO {

    public List<Librarian> readAll() {
        List<Librarian> list = new ArrayList<>();
        File file = new File(Constants.LIBRARIAN_FILE);
        if (!file.exists())
            return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                Librarian librarian = Librarian.fromDataString(line);
                if (librarian != null) {
                    list.add(librarian);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file librarians: " + e.getMessage());
        }
        return list;
    }

    public void writeAll(List<Librarian> list) {
        ensureDataDir();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.LIBRARIAN_FILE))) {
            for (Librarian librarian : list) {
                writer.write(librarian.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi file librarians: " + e.getMessage());
        }
    }

    public void add(Librarian librarian) {
        List<Librarian> list = readAll();
        list.add(librarian);
        writeAll(list);
    }

    public Librarian findByUsername(String username) {
        List<Librarian> list = readAll();
        for (Librarian lib : list) {
            if (lib.getUsername().equals(username)) {
                return lib;
            }
        }
        return null;
    }

    public String generateId() {
        List<Librarian> list = readAll();
        int maxId = 0;
        for (Librarian lib : list) {
            String numPart = lib.getId().replace(Constants.PREFIX_LIBRARIAN, "");
            try {
                int num = Integer.parseInt(numPart);
                if (num > maxId)
                    maxId = num;
            } catch (NumberFormatException ignored) {
            }
        }
        return Constants.PREFIX_LIBRARIAN + String.format("%03d", maxId + 1);
    }

    private void ensureDataDir() {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
