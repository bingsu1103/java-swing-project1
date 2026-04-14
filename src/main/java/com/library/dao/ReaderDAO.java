package com.library.dao;

import com.library.model.Reader;
import com.library.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderDAO {

    public List<Reader> readAll() {
        List<Reader> list = new ArrayList<>();
        File file = new File(Constants.READER_FILE);
        if (!file.exists())
            return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                Reader reader = Reader.fromDataString(line);
                if (reader != null) {
                    list.add(reader);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file readers: " + e.getMessage());
        }
        return list;
    }

    public void writeAll(List<Reader> list) {
        ensureDataDir();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.READER_FILE))) {
            for (Reader reader : list) {
                writer.write(reader.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi file readers: " + e.getMessage());
        }
    }

    public void add(Reader reader) {
        List<Reader> list = readAll();
        list.add(reader);
        writeAll(list);
    }

    public void update(Reader updatedReader) {
        List<Reader> list = readAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaDocGia().equals(updatedReader.getMaDocGia())) {
                list.set(i, updatedReader);
                break;
            }
        }
        writeAll(list);
    }

    public void delete(String maDocGia) {
        List<Reader> list = readAll();
        list.removeIf(r -> r.getMaDocGia().equals(maDocGia));
        writeAll(list);
    }

    public Reader findById(String maDocGia) {
        return readAll().stream()
                .filter(r -> r.getMaDocGia().equals(maDocGia))
                .findFirst()
                .orElse(null);
    }

    public List<Reader> findByCmnd(String cmnd) {
        return readAll().stream()
                .filter(r -> r.getCmnd().contains(cmnd))
                .collect(Collectors.toList());
    }

    public List<Reader> findByName(String name) {
        String lowerName = name.toLowerCase();
        return readAll().stream()
                .filter(r -> r.getHoTen().toLowerCase().contains(lowerName))
                .collect(Collectors.toList());
    }

    public String generateId() {
        List<Reader> list = readAll();
        int maxId = 0;
        for (Reader r : list) {
            String numPart = r.getMaDocGia().replace(Constants.PREFIX_READER, "");
            try {
                int num = Integer.parseInt(numPart);
                if (num > maxId)
                    maxId = num;
            } catch (NumberFormatException ignored) {
            }
        }
        return Constants.PREFIX_READER + String.format("%03d", maxId + 1);
    }

    private void ensureDataDir() {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
