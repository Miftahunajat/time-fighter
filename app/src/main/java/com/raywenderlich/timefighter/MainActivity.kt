package com.raywenderlich.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    internal var score = 0

    internal lateinit var tapMeButton: Button
    internal lateinit var gameScoreTextview: TextView
    internal lateinit var timeLeftTextView: TextView

    internal var gameStarted = false

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountdown: Long = 60000
    internal val countdownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 60000

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called Score is: $score")

        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextview = findViewById(R.id.tv_game_score)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        tapMeButton.setOnClickListener {
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            it.startAnimation(bounceAnimation)
            incrementScore()
        }

        gameScoreTextview.text = getString(R.string.yourScore, score)

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    private fun restoreGame() {
        gameScoreTextview.text = getString(R.string.yourScore, score)

        val restoredTime = timeLeftOnTimer / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, restoredTime)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countdownInterval) {
            override fun onFinish() {
                endGame()
            }

            override fun onTick(p0: Long) {
                timeLeftOnTimer = p0
                val timeLeft = p0 / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }
        }
    }

    private fun resetGame() {
        score = 0
        gameScoreTextview.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountdown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountdown, countdownInterval) {
            override fun onFinish() {
                endGame()
            }

            override fun onTick(p0: Long) {
                timeLeftOnTimer = p0
                val timeleft = p0 / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeleft)
            }
        }

        gameStarted = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(MainActivity::class.java.simpleName, "onSaveInstanceState: Saving score $score & time left $timeLeftOnTimer");
    }



    override fun onDestroy() {
        super.onDestroy()
        Log.d(MainActivity::class.java.simpleName, "onDestroy Called");
    }

    private fun incrementScore() {
        if (!gameStarted) {
            countDownTimer.start()
            gameStarted = true
        }

        score++
        val newScore = getString(R.string.yourScore, score)
        gameScoreTextview.text = newScore

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        gameScoreTextview.startAnimation(blinkAnimation)
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionAbout) {
            showInfo()
        }

        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.aboutTitle)
        val dialogMessage = getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }
}
