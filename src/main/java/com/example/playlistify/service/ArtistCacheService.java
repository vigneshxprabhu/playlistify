package com.example.playlistify.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.playlistify.model.ArtistDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
@Service
public class ArtistCacheService {
    private final ObjectMapper objectMapper;

    public ArtistCacheService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    }
  
    private final Map<String, ArtistDetails> cache = new HashMap<>();
  
    @Value("${playlistify.cache.path}")
    private String cacheFile;
    public ArtistDetails get(String artistName) {
    return cache.get(artistName);
    }

   public void put(String artistName, ArtistDetails artist) throws IOException {

    cache.put(artistName, artist);

     
    }

    public void save() throws IOException {

    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(cacheFile), cache);

    System.out.println("Cache saved. Total artists: " + cache.size());

    }
    public void load() throws IOException {

    File file = new File(cacheFile);

    if (!file.exists()|| file.length() == 0) {
        System.out.println("Cache file is empty. Starting with an empty cache.");
        return;
    }

    Map<String, ArtistDetails> loadedCache =objectMapper.readValue(file,new TypeReference<Map<String, ArtistDetails>>() {});

    cache.putAll(loadedCache);

    System.out.println("Loaded " + cache.size() + " artists from cache.");
}

@PostConstruct
public void initialize() {

    try {
        load();
    } catch (IOException e) {
        System.out.println("Failed to load cache.");
        e.printStackTrace();
    }
}
    
}
