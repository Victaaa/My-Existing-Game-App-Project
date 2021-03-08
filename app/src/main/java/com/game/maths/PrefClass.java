package com.game.maths;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.game.maths.Activities.StartScreen;

public class PrefClass {

	public static final int highScore = 0;

	public static int getMaxScore() {
		return getSharedPreferences().getInt("HIGHSCORE", 0);
	}

	public static SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(StartScreen
				.getAppContext());
	}

	public static SharedPreferences getSharedPreferencesWithContext(
			Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static void setHighScore(int score) {
		getSharedPreferences().edit().putInt("HIGHSCORE", score).apply();
	}
	
	public static boolean getSound(){
		return getSharedPreferences().getBoolean("SOUND", true);
	}

	public static void setSound(boolean sound){
		getSharedPreferences().edit().putBoolean("SOUND", sound).apply();
	}
	public static boolean getModeQuestion(){
		return getSharedPreferences().getBoolean("Mode", false);
	}

	public static void  setModeQuestion(boolean type){
		getSharedPreferences().edit().putBoolean("Mode", type).apply();
	}



}
