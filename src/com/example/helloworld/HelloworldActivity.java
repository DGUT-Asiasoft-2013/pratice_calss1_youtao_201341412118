package com.example.helloworld;

import com.example.helloworld.fragment.MainTabbarFragment;
import com.example.helloworld.fragment.MainTabbarFragment.OnTabSelectedListener;
import com.example.helloworld.fragment.pages.FeedLisFragment;
import com.example.helloworld.fragment.pages.MyProfileFragment;
import com.example.helloworld.fragment.pages.NoteLisFragment;
import com.example.helloworld.fragment.pages.SerchFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class HelloworldActivity extends Activity {

	FeedLisFragment contentFeddList = new FeedLisFragment();
	NoteLisFragment contentNoteList = new NoteLisFragment();
	SerchFragment contenSerchPage = new SerchFragment();
	MyProfileFragment contenMyProfile = new MyProfileFragment();

	MainTabbarFragment tabbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_helloworld);
		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				changeContentFragment(index);

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		tabbar.setSelectedItem(0);
	}

	void changeContentFragment(int index){
		Fragment newFrag = null;
		switch (index) {
		case 0:newFrag = contentFeddList;break;
		case 1:newFrag = contentNoteList;break;
		case 2:newFrag = contenSerchPage;break;
		case 3:newFrag = contenMyProfile;break;

		default:
			break;
		}
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content, newFrag)
		.commit();
	}

}
