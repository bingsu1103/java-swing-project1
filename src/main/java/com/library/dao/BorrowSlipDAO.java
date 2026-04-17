package com.library.dao;

import com.library.model.BorrowSlip;
import com.library.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowSlipDAO {

    public List<BorrowSlip> readAll() {
        List<BorrowSlip> list = new ArrayList<>();
        File file = new File(Constants.BORROW_SLIP_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                BorrowSlip slip = BorrowSlip.fromDataString(line);
                if (slip != null) {
                    list.add(slip);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file borrowslips: " + e.getMessage());
        }
        return list;
    }

    public void writeAll(List<BorrowSlip> list) {
        ensureDataDir();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.BORROW_SLIP_FILE))) {
            for (BorrowSlip slip : list) {
                writer.write(slip.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi file borrowslips: " + e.getMessage());
        }
    }

    public void add(BorrowSlip slip) {
        List<BorrowSlip> list = readAll();
        list.add(slip);
        writeAll(list);
    }

    public void update(BorrowSlip updatedSlip) {
        List<BorrowSlip> list = readAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaPhieu().equals(updatedSlip.getMaPhieu())) {
                list.set(i, updatedSlip);
                break;
            }
        }
        writeAll(list);
    }

    private void ensureDataDir() {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
