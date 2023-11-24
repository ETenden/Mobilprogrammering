package com.example.definitely_not_spotify
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Gjør at HiltViewModel som brukes i mye av koden funker.
@HiltAndroidApp
class PlayingWithDefNotSpotify : Application()