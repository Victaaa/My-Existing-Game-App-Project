package com.game.maths.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;


import android.util.DisplayMetrics;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.game.maths.DataHandler;
import com.game.maths.PrefClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


import com.game.maths.R;

public class StartScreen extends AppCompatActivity {


    public static Context context;
    public static int[] soundContainer;
    public static SoundPool sp;
    private Animation left_to_right,right_to_left, frombottom, scale;
    private Button bstart, bsound, bmore,optionSetting;
    private ImageView logo;
    int chooseMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialization layout startScreen
        setContentView(R.layout.startscreen);

        // initialization sound
        context = this.getApplicationContext();
        sp = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        soundContainer = new int[4];
        soundContainer[0] = sp.load(context, R.raw.buttonclick, 1);
        soundContainer[1] = sp.load(context, R.raw.correct, 1);
        soundContainer[2] = sp.load(context, R.raw.wrong, 1);

        // initialization elements layout and setup Animation for some elements
        left_to_right = AnimationUtils.loadAnimation(this, R.anim.slide_left_to_right);
        right_to_left = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.slide_bottom);
        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        bstart =  findViewById(R.id.bstart);
        optionSetting =  findViewById(R.id.btn_option);
        logo =  findViewById(R.id.img_logo);
        bsound =  findViewById(R.id.bsound);
        bmore =  findViewById(R.id.bmore);

        bstart.setAnimation(scale);
        logo.setAnimation(scale);
        bmore.setAnimation(frombottom);
        bsound.setAnimation(left_to_right);
        optionSetting.setAnimation(right_to_left);

        ( findViewById(R.id.tvname)).setAnimation(left_to_right);

        // show the time for a question
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        DataHandler.setDeviceHeight(dm.heightPixels);
        DataHandler.setDeviceWidth(dm.widthPixels);

        //handler on the app name
        ((TextView) findViewById(R.id.tvname)).setTypeface(DataHandler.getFont(context));

        // sound game init
        setSound();
    }


    @SuppressWarnings("deprecation")
    private void setSound() {
        if (!PrefClass.getSound()) {
            bsound.setBackgroundDrawable(getResources().getDrawable(R.mipmap.sound_off));
        } else {
            bsound.setBackgroundDrawable(getResources().getDrawable(R.mipmap.sound_on));
        }
    }

    //control sound Game
    @SuppressWarnings("deprecation")
    private void toggleSound() {
        if (PrefClass.getSound()) {
            bsound.setBackgroundDrawable(getResources().getDrawable(R.mipmap.sound_off));
            PrefClass.setSound(false);
        } else {
            bsound.setBackgroundDrawable(getResources().getDrawable(R.mipmap.sound_on));
            PrefClass.setSound(true);
        }
    }

    public static Context getAppContext() {
        return context;
    }

    // control volume of the Sound
    public static void correctSound() {
        if (PrefClass.getSound())
            sp.play(soundContainer[1], 100, 100, 1, 0, 1);
    }

    public static void wrongSound() {
        if (PrefClass.getSound())
            sp.play(soundContainer[2], 100, 100, 1, 0, 1);
    }

    public static void buttonClickSound() {
        if (PrefClass.getSound())
            sp.play(soundContainer[0], 100, 100, 1, 0, 1);
    }

    //when user click buttons
    public void clicked(View v) {
        switch (v.getId()) {
            case R.id.bstart:
                //start sound
                buttonClickSound();
                // go to GameScreen by Intent
                startActivity(new Intent(context, GameScreen.class));
                break;

            case R.id.bsound:
                //turn off Or ON sound
                toggleSound();
                break;
            case R.id.bmore:
                //show alert for about of the Game
                MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this , R.style.Theme_Alert);
                alert.setTitle(R.string.displayName);
                alert.setMessage(R.string.game_info_message);
                alert.setPositiveButton(R.string.ok, null);
                alert.show();
                break;

            case R.id.btn_option:


                //show alert for setting Mode the Game
                String[] listItems = {"default", "Write the result manually"};

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.Theme_Alert);
                builder.setTitle("Choose Mode");
                int checkedItem = 0; //this will checked the item when user open the dialog
                chooseMode=0;
                builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    { saveChose(which); }
                });

                builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (chooseMode ==0)
                            PrefClass.setModeQuestion(false);
                        else
                            PrefClass.setModeQuestion(true);

                        Toast.makeText(StartScreen.this, PrefClass.getModeQuestion()+"" , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;

        }

    }

    private void saveChose(int which)
    {
        chooseMode=which;
    }
}