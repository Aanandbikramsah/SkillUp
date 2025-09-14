package com.skillup.activities

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.skillup.R
import com.skillup.databinding.ActivityVideoPlayerBinding
import com.skillup.utils.Constants

class VideoPlayerActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityVideoPlayerBinding
    private var player: ExoPlayer? = null
    private var videoUrl: String = ""
    private var lessonTitle: String = ""
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        getIntentData()
        setupToolbar()
        setupPlayer()
    }
    
    private fun getIntentData() {
        videoUrl = intent.getStringExtra(Constants.EXTRA_VIDEO_URL) ?: ""
        lessonTitle = intent.getStringExtra(Constants.EXTRA_LESSON_TITLE) ?: "Video Lesson"
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = lessonTitle
        
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun setupPlayer() {
        if (videoUrl.isEmpty()) {
            // Use sample video if no URL provided
            videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4"
        }
        
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player
        
        val mediaItem = MediaItem.fromUri(videoUrl)
        player?.setMediaItem(mediaItem)
        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare()
        
        setupPlayerListeners()
    }
    
    private fun setupPlayerListeners() {
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        // Video ended
                        binding.btnMarkComplete.visibility = View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        // Video is ready to play
                        binding.progressBar.visibility = View.GONE
                    }
                    Player.STATE_BUFFERING -> {
                        // Video is buffering
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
        
        binding.btnMarkComplete.setOnClickListener {
            markLessonComplete()
        }
        
        binding.btnFullscreen.setOnClickListener {
            toggleFullscreen()
        }
    }
    
    private fun markLessonComplete() {
        // In a real app, you would update the lesson completion status
        android.widget.Toast.makeText(this, "Lesson marked as complete!", android.widget.Toast.LENGTH_SHORT).show()
        binding.btnMarkComplete.visibility = View.GONE
    }
    
    private fun toggleFullscreen() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            hideSystemUI()
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            showSystemUI()
        }
    }
    
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding.toolbar.visibility = View.GONE
        binding.btnMarkComplete.visibility = View.GONE
    }
    
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding.toolbar.visibility = View.VISIBLE
        binding.btnMarkComplete.visibility = View.VISIBLE
    }
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI()
        } else {
            showSystemUI()
        }
    }
    
    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            showSystemUI()
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onStart() {
        super.onStart()
        if (player == null) {
            setupPlayer()
        }
    }
    
    override fun onResume() {
        super.onResume()
        if (player == null) {
            setupPlayer()
        }
    }
    
    override fun onPause() {
        super.onPause()
        if (player != null) {
            playbackPosition = player?.currentPosition ?: 0
            currentWindow = player?.currentMediaItemIndex ?: 0
            playWhenReady = player?.playWhenReady ?: true
        }
    }
    
    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
    
    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.currentPosition ?: 0
            currentWindow = player?.currentMediaItemIndex ?: 0
            playWhenReady = player?.playWhenReady ?: true
            player?.release()
            player = null
        }
    }
}
