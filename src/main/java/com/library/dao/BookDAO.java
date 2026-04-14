package com.library.dao;

import com.library.model.Book;
import com.library.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookDAO {

    public List<Book> readAll() {
        List<Book> list = new ArrayList<>();
        File file = new File(Constants.BOOK_FILE);
        if (!file.exists())
            return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                Book book = Book.fromDataString(line);
                if (book != null) {
                    list.add(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file books: " + e.getMessage());
        }
        return list;
    }

    public void writeAll(List<Book> list) {
        ensureDataDir();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.BOOK_FILE))) {
            for (Book book : list) {
                writer.write(book.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi file books: " + e.getMessage());
        }
    }

    public void add(Book book) {
        List<Book> list = readAll();
        list.add(book);
        writeAll(list);
    }

    public void update(Book updatedBook) {
        List<Book> list = readAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsbn().equals(updatedBook.getIsbn())) {
                list.set(i, updatedBook);
                break;
            }
        }
        writeAll(list);
    }

    public void delete(String isbn) {
        List<Book> list = readAll();
        list.removeIf(b -> b.getIsbn().equals(isbn));
        writeAll(list);
    }

    public Book findByIsbn(String isbn) {
        return readAll().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    public List<Book> searchByIsbn(String isbn) {
        return readAll().stream()
                .filter(b -> b.getIsbn().contains(isbn))
                .collect(Collectors.toList());
    }

    public List<Book> searchByName(String name) {
        String lowerName = name.toLowerCase();
        return readAll().stream()
                .filter(b -> b.getTenSach().toLowerCase().contains(lowerName))
                .collect(Collectors.toList());
    }

    private void ensureDataDir() {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
