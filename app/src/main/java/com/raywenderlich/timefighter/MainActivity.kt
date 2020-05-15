package com.raywenderlich.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal var score = 0

    internal lateinit var tapMeButton: Button
    internal lateinit var gameScoreTextview: TextView
    internal lateinit var timeLeftTextView: TextView

    internal var gameStarted = false

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountdown: Long = 60000
    internal val countdownInterval: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextview = findViewById(R.id.tv_game_score)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        tapMeButton.setOnClickListener {
            incrementScore()
        }

        gameScoreTextview.text = getString(R.string.yourScore, score)

    }

    private fun resetGame() {
        score = 0
        gameScoreTextview.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountdown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountdown, countdownInterval) {
            override fun onFinish() {

            }

            override fun onTick(p0: Long) {
                val timeleft = p0 / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeleft)
            }
        }

        gameStarted = false
    }

    private fun incrementScore() {
        if (!gameStarted) {
            countDownTimer.start()
            gameStarted = true
        }

        score++
        val newScore = getString(R.string.yourScore, score)
        gameScoreTextview.text = newScore
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()
    }
}
