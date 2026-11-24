package com.oggy.music

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

data class Song(
    val id: String,
    val title: String,
    val artist: String
)

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private lateinit var player: ExoPlayer

    private lateinit var searchInput: EditText
    private lateinit var statusText: TextView
    private lateinit var nowPlaying: TextView
    private lateinit var playPauseButton: Button
    private lateinit var songAdapter: SongAdapter

    private var searchCall: Call? = null
    private var searchVersion = 0
    private var hasSelectedSong = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        player = ExoPlayer.Builder(this).build()

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val title = TextView(this).apply {
            text = "OGGY MUSIC"
            textSize = 26f
        }

        searchInput = EditText(this).apply {
            hint = "গান বা শিল্পীর নাম লিখুন"
            setSingleLine(true)
        }

        val searchButton = Button(this).apply {
            text = "সার্চ করুন"
        }

        statusText = TextView(this).apply {
            text = "গান খুঁজতে সার্চ করুন"
        }

        songAdapter = SongAdapter { song ->
            playSong(song)
        }

        val songList = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = songAdapter
        }

        nowPlaying = TextView(this).apply {
            text = "কোনো গান চলছে না"
        }

        playPauseButton = Button(this).apply {
            text = "প্লে"
            isEnabled = false
      
