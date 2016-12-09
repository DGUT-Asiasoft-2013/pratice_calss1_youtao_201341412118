package com.example.helloworld.fragment;

import com.example.helloworld.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainTabbarFragment extends Fragment {

	View btnNew,tabFedds,tabNotes,tabSerach,tabMe;
	View[] tabs;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_tabbar, null);


		btnNew = view.findViewById(R.id.btn_new);
		tabFedds = view.findViewById(R.id.tab_fedds);
		tabNotes = view.findViewById(R.id.tab_notes);
		tabSerach = view.findViewById(R.id.tab_serach);
		tabMe = view.findViewById(R.id.tab_me);

		tabs = new View[]{
				tabFedds,tabNotes,tabSerach,tabMe
		};

		for(final View tab:tabs){
			tab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					onTabClicked(tab);
				}
			});
		}

		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onNewClicked();
			}
		});

		return view;
	}



	public static interface OnTabSelectedListener{
		void onTabSelected(int index);
	}

	OnTabSelectedListener onTabSelectedListener;

	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
		this.onTabSelectedListener = onTabSelectedListener;
	}

	public void setSelectedItem(int index){
		if(index>=0 && index<tabs.length){
			onTabClicked(tabs[index]);
		}
	}

	void onTabClicked(View tab){
		int selectedIndex =-1;
		for(int i=0;i<tabs.length;i++){
			View othertab = tabs[i];

			if(othertab == tab){
				othertab.setSelected(true);
				selectedIndex = i;
			}
			else{
				othertab.setSelected(false);
			}
		}
		if(onTabSelectedListener != null && selectedIndex>=0){
			onTabSelectedListener.onTabSelected(selectedIndex);
		}
	}

	public static interface OnNewClickedListener{
		void onNewClicked();
	}

	OnNewClickedListener onNewClickedListener;

	public void setOnNewClickedListener(OnNewClickedListener listener){
		this.onNewClickedListener = listener;
	}

	void onNewClicked(){
		if(onNewClickedListener!=null)
			onNewClickedListener.onNewClicked();
	}
}
