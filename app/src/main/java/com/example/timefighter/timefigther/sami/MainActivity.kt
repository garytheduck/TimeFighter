package com.example.timefighter.timefigther.sami

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


import android.media.MediaPlayer
import android.os.PersistableBundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    var mMediaPlayer: MediaPlayer? = null
    fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.aaaaaahhhh)
            mMediaPlayer!!.isLooping = false
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }
    fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
    internal var score = 0

    //declarare obiecte
    internal lateinit var tapMeButton: Button
    internal lateinit  var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView

    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 60000

    companion object{
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate called. Score is : $score")

        tapMeButton = findViewById(R.id.tapMebutton)
        gameScoreTextView = findViewById(R.id.gameScore)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        //resetare joc
        resetGame()
        //asteptam click pe buton
        tapMeButton.setOnClickListener{ view ->
            incrementscore()
            playSound()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState : Saving Score : $score & Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }
    private fun resetGame(){
        score =  0
        gameScoreTextView.text = getString(R.string.yourScore, 0)
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }
            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false
    }
    private fun incrementscore() {
        if(!gameStarted){
            startGame()
        }
        score++
        val newScore = getString(R.string.yourScore, score)
        gameScoreTextView.text = newScore
    }
    private fun startGame(){
        countDownTimer.start()
        gameStarted = true
    }
    private fun endGame(){
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()
    }
}