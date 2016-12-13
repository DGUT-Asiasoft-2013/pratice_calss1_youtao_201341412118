package com.example.helloworld.fragment.pages;

import java.io.IOException;

import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.entity.User;
import com.example.helloworld.fragment.AvatarView;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.color;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyProfileFragment extends Fragment {
	View view;
	TextView textView;
	ProgressBar progress;
	AvatarView avtar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			
			view=inflater.inflate(R.layout.fragment_page_my_profile, null);
			textView=(TextView)view.findViewById(R.id.text);
			progress=(ProgressBar)view.findViewById(R.id.prograss);
		}
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		textView.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
		
		OkHttpClient client = Server.getSharedClient();
		Request request = Server.requestBuilderWithApi("me")
				.method("get", null)
				.build();

		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				try{
					final User user =new ObjectMapper().readValue(arg1.body().bytes(), User.class);
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyProfileFragment.this.onResponse(arg0, user);
						}
					});
				}catch(final Exception e){
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyProfileFragment.this.onFailure(arg0,e);
						}
					});
				}
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				// TODO Auto-generated method stub
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						MyProfileFragment.this.onFailure(arg0, arg1);
					}
				});
			}
		});
	}
	
	void onResponse(Call arg0,User user){
		progress.setVisibility(View.GONE);
//		avtar.load(user);
		textView.setVisibility(View.VISIBLE);
		textView.setVisibility(Color.BLACK);
		textView.setText("Hello,"+user.getName());
		
	}
	
	void onFailure(final Call arg0,final Exception e){
		progress.setVisibility(View.GONE);
		textView.setVisibility(View.VISIBLE);
		textView.setTextColor(Color.RED);
		textView.setText(e.getMessage());
	}
}
