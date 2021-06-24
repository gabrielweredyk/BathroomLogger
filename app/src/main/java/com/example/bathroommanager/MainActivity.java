package com.example.bathroommanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

	private Button[] buttons;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//        getActionBar().hide();


		buttons = new Button[] {findViewById(R.id.survey), findViewById(R.id.submit), findViewById(R.id.schedule)};

		for (int i = 0; i < 3; i++) {
			final Button temp = buttons[i];
			temp.setOnTouchListener(
					new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_DOWN){
								temp.animate().scaleX(1.1f).setDuration(100);
								temp.animate().scaleY(1.1f).setDuration(100);
							}
							else if (event.getAction() == MotionEvent.ACTION_UP){
								Intent intent = new Intent();
	    						//Optimally, I'd like to change this from a switch case to using a Class[] but couldn't get it working

		    					int location = temp.getId();

			    				switch (location) {
				    				case R.id.survey: //fun fact: Identities are classified as integers!
					    				intent = new Intent(android.content.Intent.ACTION_VIEW);
						    			intent.setData(Uri.parse("https://entry.neric.org/BMCHSD"));
							    		break;
								    case R.id.submit:
									    intent = new Intent(MainActivity.this, SubmitBathroom.class);
									    break;
								    case R.id.schedule:
								    	intent = new Intent(MainActivity.this, Schedule.class);
								    	break;
							    }
							    startActivity(intent);
							}
							return false;
						}
					}
			);
			buttons[i] = temp;
		}

	}
}
