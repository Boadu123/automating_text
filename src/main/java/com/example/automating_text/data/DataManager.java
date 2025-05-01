package com.example.automating_text.data;

import com.example.automating_text.data.TextData;
import java.util.*;

public class DataManager {
    private final Map<String, TextData> dataStore = new HashMap<>();

    // Create
    public void addData(TextData data) {
        if (data == null || data.getId() == null) {
            throw new IllegalArgumentException("Data or ID cannot be null");
        }
        dataStore.put(data.getId(), data);
    }

    // Read
    public TextData getData(String id) {
        if (!dataStore.containsKey(id)) {
            throw new NoSuchElementException("Data with ID " + id + " not found");
        }
        return dataStore.get(id);
    }

    public List<TextData> getAllData() {
        return new ArrayList<>(dataStore.values());
    }

    // Update
    public void updateData(TextData data) {
        if (!dataStore.containsKey(data.getId())) {
            throw new NoSuchElementException("Data with ID " + data.getId() + " not found");
        }
        dataStore.put(data.getId(), data);
    }

    // Delete
    public boolean deleteData(String id) {
        if (!dataStore.containsKey(id)) {
            return false;
        }
        dataStore.remove(id);
        return true;
    }

    public boolean containsId(String id) {
        return dataStore.containsKey(id);
    }
}