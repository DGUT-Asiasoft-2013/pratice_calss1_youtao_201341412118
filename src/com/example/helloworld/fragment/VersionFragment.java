package com.example.helloworld.fragment;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.helloworld.R;

public class VersionFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstancestate){
		
		View view = inflater.inflate(R.layout.fragment_version,null);
		TextView textVersion = (TextView)view.findViewById(R.id.text);
		
		PackageManager pkgm=this.getActivity().getPackageManager();
		
		try{
			PackageInfo appinf = pkgm.getPackageInfo(getActivity().getPackageName(), 0);
			textVersion.setText(appinf.packageName+ "" + appinf.versionName);
		}catch(NameNotFoundException e)
		{
			e.printStackTrace();
			textVersion.setText("ERROR");
		}
		return view;
	}

}
