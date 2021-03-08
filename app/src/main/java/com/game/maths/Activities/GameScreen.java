package com.game.maths.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.maths.Models.CustomAnimationBar;
import com.game.maths.DataHandler;
import com.game.maths.Models.DataVars;
import com.game.maths.PrefClass;
import com.game.maths.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameScreen extends Activity implements OnClickListener {
    int highScore = 0;
    AtomicBoolean gameEnded;
    private RelativeLayout llMain;
    boolean resultOfGame;
    private TextView tvfirstDigit, tvoperator, tvsecondDigit, tvresult, tvhighscore;
    private Button bright, bwrong;
    private ImageView ivtimerBar;
    private int width;
    private CustomAnimationBar barAnimation;
    private Intent intent;
    private Context context = this;
    private int heightOfTimer;
    private boolean isPaused = false;
    private int adcounter = 5;
    private int gamePlayNumber;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    private int inititialTimer = 1000;
    private int scoreMultiplier = 1;
    private int timerEasy, timerMedium, timerHard , firstDigit , secondDigit;
    private EditText etresult;
    private boolean modeGame =false;

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialization layout startScreen
        setContentView(R.layout.gamescreen);
        //check of Mode Game
        modeGame=PrefClass.getModeQuestion();
        //if game not Ended = false
        gameEnded = new AtomicBoolean(false);

        //setup time question and levels (easy or Medium or Hard)
        timerEasy = Integer.parseInt(getResources().getString(R.string.timerEasy));
        timerMedium = Integer.parseInt(getResources().getString(R.string.timerMedium));
        timerHard = Integer.parseInt(getResources().getString(R.string.timerHard));

        // Choose the difficulty of the game and time
        if (DataHandler.getDifficulty() == 1) {
            scoreMultiplier = 1;
            inititialTimer = timerEasy;
        } else if (DataHandler.getDifficulty() == 2) {
            scoreMultiplier = 2;
            inititialTimer = timerMedium;
        } else {
            scoreMultiplier = 3;
            inititialTimer = timerHard;
        }

        // initialization elements layout
        initialiseUiElements();
        //init SharedPreference
        pref = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        edit = pref.edit();
        //Save score limit to sol question
        gamePlayNumber = pref.getInt("GAMEPLAYNUMBER", 1);

        //set color for time of question
        ivtimerBar.setBackgroundColor(getResources().getColor(R.color.timerBack));
        //set color for background of question
        llMain.setBackgroundColor(Color.parseColor(DataHandler.randomColorGetter()));

        //reset number of question
        tvhighscore.setText("0");
        width = DataHandler.getDeviceWidth();

        //setup time of question with animation
        heightOfTimer = getResources().getDimensionPixelSize(R.dimen.padding);
        barAnimation = new CustomAnimationBar(ivtimerBar, (float) width, heightOfTimer, 0.0F, heightOfTimer,
                inititialTimer);
        barAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation anim) {
                // When time runs out, the game ends
                if (ivtimerBar.getWidth() == 0) {
                    endGame();
                }

            }

            public void onAnimationRepeat(Animation anim) {
            }

            public void onAnimationStart(Animation anim) {
            }
        });

        //setup time animation
        barAnimation.cancel();
        ivtimerBar.setAnimation(barAnimation);

        //setOnclick button correct and wrong
        bright.setOnClickListener(this);
        bwrong.setOnClickListener(this);
        DataHandler.replay();

        //init Game
        initialiseGame();
        barAnimation.start();
    }

    // initialization elements layout
    private void initialiseUiElements() {
        tvfirstDigit = findViewById(R.id.tvfirstDigit);
        tvsecondDigit = findViewById(R.id.tvsecondDigit);
        tvresult = findViewById(R.id.tvresult);
        etresult = findViewById(R.id.et_result);
        tvoperator =  findViewById(R.id.tvoperator);
        tvhighscore = findViewById(R.id.tvhighscore);
        llMain = findViewById(R.id.llmain);
        ivtimerBar =  findViewById(R.id.ivtimerBar);
        bright = findViewById(R.id.bright);
        bwrong = findViewById(R.id.bwrong);

        //set font numbers text
        tvfirstDigit.setTypeface(DataHandler.getFont(context));
        tvsecondDigit.setTypeface(DataHandler.getFont(context));
        tvresult.setTypeface(DataHandler.getFont(context));
        tvoperator.setTypeface(DataHandler.getFont(context));
        tvhighscore.setTypeface(DataHandler.getFont(context));
        ((TextView) findViewById(R.id.tvequal)).setTypeface(DataHandler.getFont(context));
        // check any mode
        if (modeGame)
          {
              //show editText and Hide TextView
            etresult.setVisibility(View.VISIBLE);
            bwrong.setVisibility(View.GONE);
            //Open Keyboard
              InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
          }
            else
                tvresult.setVisibility(View.VISIBLE);
    }
//init Game and Questions
    private void initialiseGame() {
        //get Question Date
        DataVars dataVars = DataHandler.extensiveRandomNumber();
        resultOfGame = dataVars.isCorrect;
        tvfirstDigit.setText(dataVars.one + "");
        tvsecondDigit.setText(dataVars.two + "");
        tvresult.setText(dataVars.resources + "");

        // is mode Result input try this
        if (modeGame)
        {
            etresult.setVisibility(View.VISIBLE);
            etresult.setText("");
            bwrong.setVisibility(View.GONE);
            firstDigit=dataVars.one;
            secondDigit=dataVars.two;
        }
        else
            tvresult.setText(dataVars.resources + "");

    //get Opreator of the question
        if (dataVars.isAdd) {
            tvoperator.setText("+");
        } else {
            tvoperator.setText("-");
        }

        if (dataVars.operator == 2)
            tvoperator.setText("x");

        //change color for background of question
        llMain.setBackgroundColor(Color.parseColor(DataHandler.randomColorGetter()));
    }


    //Game over and finish activity
    private void endGame() {
        // set game is ended
        gameEnded.set(true);
        //get Sound
        StartScreen.wrongSound();
        //set Top Score
        if (highScore > PrefClass.getMaxScore()) {
            PrefClass.setHighScore(highScore);
        }
        if (modeGame)
        {
            //close Keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etresult.getWindowToken(), 0);
        }

        DataHandler.replay();
        // go to GameOver Activity
        intent = new Intent(context, GameOverActivity.class);
        intent.putExtra("SCORE", highScore);
        startActivity(intent);
        finish();
        if (gamePlayNumber >= adcounter) {
            edit.putInt("GAMEPLAYNUMBER", 1);
            edit.commit();
        } else {
            gamePlayNumber++;
            edit.putInt("GAMEPLAYNUMBER", gamePlayNumber);
            edit.commit();
        }

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.bright:
                // when editEext is Empty
                if(etresult.getText().toString().trim().equals("") && etresult.getVisibility() == View.VISIBLE)
                {
                    Toast.makeText(context, "There is No Answer!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // bar animation timer not ended ?
                if (!barAnimation.hasEnded()) {
                    barAnimation.cancel();
                }
                //check of the Mode Type
               if (modeGame)
            {
                int total;
                if (tvoperator.getText().equals("+"))
                    total = firstDigit + secondDigit;
                else if (tvoperator.getText().equals("-"))
                    total = firstDigit - secondDigit;
                else
                    total = firstDigit * secondDigit;


                    int result = Integer.parseInt(etresult.getText().toString().trim());

                    if (total == result)
                        resultOfGame=true;
                    else
                        resultOfGame=false;
            }
            //if If the answer is correct
                if (resultOfGame) {
                    //get Sound
                    StartScreen.correctSound();
                    ivtimerBar.getLayoutParams().width = width;
                    //init next question
                    initialiseGame();
                    barAnimation.start();
                    highScore += scoreMultiplier;
                    tvhighscore.setText(highScore + "");
                } else //If the answer is incorrect
                    endGame();

                break;
            case R.id.bwrong:
                if (!gameEnded.get()) {
                    if (!barAnimation.hasEnded()) {
                        barAnimation.cancel();
                    }

                    if (!resultOfGame) {
                        StartScreen.correctSound();
                        ivtimerBar.getLayoutParams().width = width;
                        initialiseGame();
                        barAnimation.start();
                        highScore += scoreMultiplier;
                        tvhighscore.setText(highScore + "");
                    } else//If the answer is incorrect
                        endGame();

                }
                break;
        }

    }

    // when pause App
    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }
    // when Resume App
    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused) {
            isPaused = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    barAnimation.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation anim) {
                            if (ivtimerBar.getWidth() == 0) {
                                endGame();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation anim) {
                        }

                        @Override
                        public void onAnimationStart(Animation anim) {
                        }
                    });
                }
            });
            initialiseGame();
            barAnimation.start();
            @SuppressWarnings("unused")
            GameScreen gameScreen = GameScreen.this;
            ivtimerBar.setAnimation(barAnimation);
        }
    }

}

