package com.example.helloworld;

import com.example.helloworld.fragment.PasswordRecoverStep1Fragments;
import com.example.helloworld.fragment.PasswordRecoverStep1Fragments.OnGoNextListener;
import com.example.helloworld.fragment.PasswordRecoverStep2Fragments;

import android.app.Activity;
import android.os.Bundle;

public class PasswordRecoverActivity extends Activity {
	
	PasswordRecoverStep1Fragments step1 = new PasswordRecoverStep1Fragments();
	PasswordRecoverStep2Fragments step2 = new PasswordRecoverStep2Fragments();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_password_recover);
		
		step1.setOnGoNextListener(new OnGoNextListener() {
			
			@Override
			public void onGoNext() {
				// TODO Auto-generated method stub
				goStep2();
			}
		});
		
		getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();
	}
	
	void goStep2(){
		getFragmentManager()
		.beginTransaction()
		.setCustomAnimations(
				R.animator.slide_in_right, 
				R.animator.slide_out_left, 
				R.animator.slide_in_left, 
				R.animator.slide_out_right)
		.replace(R.id.container, step2)
		.addToBackStack(null)
		.commit();
	}
}
