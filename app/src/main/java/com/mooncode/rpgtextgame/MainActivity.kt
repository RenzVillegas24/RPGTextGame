package com.mooncode.rpgtextgame

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var rollButton: MaterialButton
    private lateinit var attackSwitch: SwitchMaterial
    private lateinit var defendSwitch: SwitchMaterial
    private lateinit var healSwitch: SwitchMaterial
    private lateinit var actionImage: ImageView
    private lateinit var meText: TextView
    private lateinit var monsterText: TextView
    private lateinit var meStatsText: TextView
    private lateinit var monsterStatsText: TextView
    private lateinit var actionLayout: GridLayout
    private lateinit var actionTextView: TextView
    private lateinit var meCardView: MaterialCardView
    private lateinit var monsterCardView: MaterialCardView


    private var myTurn: Boolean = true
    private var myHealth: Int = 200
    private var monsterHealth: Int = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        rollButton = findViewById(R.id.btnRoll)
        attackSwitch = findViewById(R.id.btnAttack)
        defendSwitch = findViewById(R.id.btnDefend)
        healSwitch = findViewById(R.id.btnHeal)
        actionImage = findViewById(R.id.imgAction)
        meText = findViewById(R.id.txtMe)
        monsterText = findViewById(R.id.txtMonster)
        meStatsText = findViewById(R.id.txtMeStats)
        monsterStatsText = findViewById(R.id.txtMonsterStats)
        actionLayout = findViewById(R.id.layoutAction)
        actionTextView = findViewById(R.id.txtAction)
        meCardView = findViewById(R.id.cardMe)
        monsterCardView = findViewById(R.id.cardMonster)


        val sz125dp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 125.0F, resources.displayMetrics).toInt()

        val growAnimation = ValueAnimator.ofInt(0, sz125dp)
        growAnimation.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            actionLayout.layoutParams.height = value
            actionLayout.requestLayout()
        }
        growAnimation.duration = 350

        val shrinkAnimation = ValueAnimator.ofInt(sz125dp, 0)
        shrinkAnimation.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            actionLayout.layoutParams.height = value
            actionLayout.requestLayout()
        }
        shrinkAnimation.duration = 350




        fun actionImageUpdate() {
            rollButton.isEnabled = !(!healSwitch.isChecked && !attackSwitch.isChecked && !defendSwitch.isChecked)

            if (!rollButton.isEnabled) {
                if (actionImage.height != 0) {
                    shrinkAnimation.start()
                }
            } else {
                if (actionImage.height != sz125dp){
                    growAnimation.start()
                }
            }

            if (healSwitch.isChecked)
                actionTextView.text = "Heal"
            else if (attackSwitch.isChecked)
                actionTextView.text = "Attack"
            else if (defendSwitch.isChecked)
                actionTextView.text = "Defend"
        }

        // Set click listeners for the switches and button
        attackSwitch.setOnClickListener {
            // Handle Attack switch event
            if (attackSwitch.isChecked) {
                // Perform action when Attack is enabled
                defendSwitch.isChecked = false
                healSwitch.isChecked = false
                actionImage.setImageResource(R.drawable.sword)

            }  else {
                // Perform action when Defend is disabled
            }

            actionImageUpdate()

        }

        defendSwitch.setOnClickListener {
            // Handle Defend switch event
            if (defendSwitch.isChecked) {
                // Perform action when Defend is enabled
                attackSwitch.isChecked = false
                healSwitch.isChecked = false
                actionImage.setImageResource(R.drawable.shield)

            } else {
                // Perform action when Defend is disabled
            }

            actionImageUpdate()

        }

        healSwitch.setOnClickListener {
            // Handle Heal switch event
            if (healSwitch.isChecked) {
                // Perform action when Heal is enabled
                attackSwitch.isChecked = false
                defendSwitch.isChecked = false
                actionImage.setImageResource(R.drawable.heart)

            }  else {
                // Perform action when Defend is disabled
            }

            actionImageUpdate()

        }

        var myDefense = 0.0F
        var monsterDefense = 0.0F

        meCardView.cardElevation = 20F
        monsterCardView.cardElevation = 0F

        fun monsterAction() {
            // Monster turn with random actions
            meCardView.cardElevation = 0F
            monsterCardView.cardElevation = 20F

            // Add an animation for selecting the random action

            val actions = listOf("Heal", "Attack", "Defend")

            var monsterAction = 0


            var countDownTimer = object : CountDownTimer(2000, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    attackSwitch.isEnabled = false
                    defendSwitch.isEnabled = false
                    healSwitch.isEnabled = false
                    rollButton.isEnabled = false
                    monsterAction = Random.nextInt(actions.size)
                    actionTextView.text = "CHOOSING ACTION:\n${actions[monsterAction]}"

                    if (monsterAction == 0)
                        actionImage.setImageResource(R.drawable.heart)
                    else if (monsterAction == 1)
                        actionImage.setImageResource(R.drawable.sword)
                    else if (monsterAction == 2)
                        actionImage.setImageResource(R.drawable.shield)

                }

                override fun onFinish() {
                    attackSwitch.isEnabled = true
                    defendSwitch.isEnabled = true
                    healSwitch.isEnabled = true
                    rollButton.isEnabled = true

                    actionTextView.text = "MONSTER ACTION:\n${actions[monsterAction]}"
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({
                countDownTimer.start()
            }, 2000)




        }



        rollButton.setOnClickListener {
            // Handle Roll button click event
            // Perform action when the Roll button is clicked

            if (attackSwitch.isChecked) {
                // Perform action when Attack is enabled
                var monsterHealthChanges = 0
                monsterStatsText.text = "$monsterHealth HP"
                actionImage.setImageResource(R.drawable.sword)
                actionImage.animate()
                    .rotation(360F*3)
                    .setDuration(1500)
                    .setDuration(1500).setUpdateListener { p0 ->
                        monsterHealthChanges = Random.nextInt(30)
                        actionTextView.text = "$monsterHealthChanges HP"
                    }
                    .setListener(object: Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                        attackSwitch.isEnabled = false
                        defendSwitch.isEnabled = false
                        healSwitch.isEnabled = false
                        rollButton.isEnabled = false
                    }
                    override fun onAnimationCancel(p0: Animator) {}
                    override fun onAnimationRepeat(p0: Animator) {}
                    override fun onAnimationEnd(p0: Animator) {
                        attackSwitch.isEnabled = true
                        defendSwitch.isEnabled = true
                        healSwitch.isEnabled = true
                        rollButton.isEnabled = true
                        actionImage.rotation = 0F
                        monsterHealth -= monsterHealthChanges
                        monsterStatsText.text = "$monsterHealth HP"

                        monsterAction()
                    }
                }).start()

            } else if (defendSwitch.isChecked) {
                // Perform action when Defend is enabled
                var myDefenseChanges = 0.0F
                meStatsText.text = "$myHealth HP"
                actionImage.setImageResource(R.drawable.shield)
                actionImage.animate()
                    .rotation(360F*3)
                    .setDuration(1500)
                    .setDuration(1500).setUpdateListener { p0 ->
                        myDefenseChanges = Random.nextInt(100)/100.0F
                        actionTextView.text = "${myDefenseChanges*100}%"
                    }
                    .setListener(object: Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                        attackSwitch.isEnabled = false
                        defendSwitch.isEnabled = false
                        healSwitch.isEnabled = false
                        rollButton.isEnabled = false
                    }
                    override fun onAnimationCancel(p0: Animator) {}
                    override fun onAnimationRepeat(p0: Animator) {}
                    override fun onAnimationEnd(p0: Animator) {
                        attackSwitch.isEnabled = true
                        defendSwitch.isEnabled = true
                        healSwitch.isEnabled = true
                        rollButton.isEnabled = true
                        actionImage.rotation = 0F
                        myDefense = myDefenseChanges
                        meStatsText.text = "$myHealth HP"

                        monsterAction()
                    }
                }).start()

            } else if (healSwitch.isChecked) {
                // Perform action when Heal is enabled
                var myHealthChanges = 0
                meStatsText.text = "$myHealth HP"
                actionImage.setImageResource(R.drawable.heart)
                actionImage.animate()
                    .rotation(360F*3)
                    .setDuration(1500).setUpdateListener { p0 ->
                        myHealthChanges = Random.nextInt(15)
                        actionTextView.text = "$myHealthChanges HP"
                    }
                    .setListener(object: Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                        attackSwitch.isEnabled = false
                        defendSwitch.isEnabled = false
                        healSwitch.isEnabled = false
                        rollButton.isEnabled = false
                    }
                    override fun onAnimationCancel(p0: Animator) {}
                    override fun onAnimationRepeat(p0: Animator) {}
                    override fun onAnimationEnd(p0: Animator) {
                        attackSwitch.isEnabled = true
                        defendSwitch.isEnabled = true
                        healSwitch.isEnabled = true
                        rollButton.isEnabled = true
                        actionImage.rotation = 0F
                        myHealth += myHealthChanges
                        meStatsText.text = "$myHealth HP"

                        monsterAction()


                    }
                }).start()

            }

            if (myHealth <= 0) {
                // Perform action when the player's health is 0
                actionTextView.text = "You Lose!"
                actionImage.setImageResource(R.drawable.dead)
                attackSwitch.isEnabled = false
                defendSwitch.isEnabled = false
                healSwitch.isEnabled = false
                rollButton.isEnabled = false
            } else if (monsterHealth <= 0) {
                // Perform action when the monster's health is 0
                actionTextView.text = "You Win!"
                actionImage.setImageResource(R.drawable.trophy)
                attackSwitch.isEnabled = false
                defendSwitch.isEnabled = false
                healSwitch.isEnabled = false
                rollButton.isEnabled = false
            }

            // Monster turn with random actions



            //actionShowAnimation.start()
            //actionHideAnimation.start()
        }
    }
}