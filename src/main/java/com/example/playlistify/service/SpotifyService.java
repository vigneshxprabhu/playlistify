package com.example.playlistify.service;

import com.example.playlistify.model.Track;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyService {

   public List<Track> getLikedSongs(){
     List<Track> songs=new ArrayList<>();

     Track song1=new Track();

     song1.setId("2");
     song1.setName("do u love me");
     songs.add(song1);
     return songs;
   }
}