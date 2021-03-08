package com.game.maths.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.game.maths.DataHandler;
import com.game.maths.PrefClass;
import com.game.maths.R;


public class GameOverActivity extends Activity implements OnClickListener {

	private Button bplay, bmenu, bshare;
	private Intent intent;
	private TextView tvlastScore, tvtopscore;
	private Animation left_to_right, rightToLeft, fromBottom;
	private int score;
	private Context context = this;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initialization layout startScreen
		setContentView(R.layout.gameoverscreen);

		// initialization elements layout
		score = getIntent().getIntExtra("SCORE", 0);
		tvlastScore = findViewById(R.id.last_score);
		tvtopscore = findViewById(R.id.top_score);
		bplay = findViewById(R.id.bplay);
		bmenu =  findViewById(R.id.bmenu);
		bshare = findViewById(R.id.bshare);
		tvlastScore.setText(score + "");
		tvtopscore.setText(PrefClass.getMaxScore() + "");
		bplay.setOnClickListener(this);
		bmenu.setOnClickListener(this);
		bshare.setOnClickListener(this);

		// set Animation for some elements
		left_to_right = AnimationUtils.loadAnimation(this,
				R.anim.slide_left_to_right);
		rightToLeft = AnimationUtils.loadAnimation(this,
				R.anim.slide_right_to_left);
		fromBottom = AnimationUtils.loadAnimation(context, R.anim.slide_bottom);

		tvlastScore.setTypeface(DataHandler.getFont(context));
		tvtopscore.setTypeface(DataHandler.getFont(context));

		//set Font to TextView
		((TextView) findViewById(R.id.tvlabellast)).setTypeface(DataHandler
				.getFont(context));
		((TextView) findViewById(R.id.tvgameover)).setTypeface(DataHandler
				.getFont(context));
		((TextView) findViewById(R.id.tvlabeltop)).setTypeface(DataHandler
				.getFont(context));

		//setUp Animation for some elements
		(findViewById(R.id.tvgameover)).setAnimation(left_to_right);
		bplay.setAnimation(left_to_right);
		bmenu.setAnimation(rightToLeft);
		bshare.setAnimation(fromBottom);

	}
	// share Score
	private void shareIt() {
		String text = String.format(
				getResources().getString(R.string.shareSubject),
				PrefClass.getMaxScore());
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_TEXT,
				"Last Score: "+tvlastScore.getText()+" "+"Top Score: "+tvtopscore.getText());
		i.putExtra(Intent.EXTRA_SUBJECT, text);
		startActivity(Intent.createChooser(i, "Share"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bplay:
			//start Again with Intent to Game Screen
			StartScreen.buttonClickSound();
			intent = new Intent(GameOverActivity.this, GameScreen.class);
			startActivity(intent);
			finish();
			break;
		case R.id.bmenu:
			// go to Home
			StartScreen.buttonClickSound();
			finish();
			break;
		case R.id.bshare:
			//Share it
			shareIt();
			break;
		}

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}
}
