package com.example.helloworld.fragment;

import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

public class PasswordRecoverStep2Fragments extends Fragment {
	SimpleTextInputCellFragment fragPassword,fragPasswordRepeat;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view = inflater.inflate(R.layout.fragment_password_recover_step2,null);
			fragPassword=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
			fragPasswordRepeat=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
		}
		
		view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				OnSubmitClicked();

			}
		});	
		
		return view;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		fragPassword.setLabelText("√‹¬Î");{
			fragPassword.setHintText("«Î ‰»Î√‹¬Î");
			fragPassword.setIsPassword(true);
		}

		fragPasswordRepeat.setLabelText("÷ÿ∏¥√‹¬Î");{
			fragPasswordRepeat.setHintText("«Î÷ÿ∏¥ ‰»Î√‹¬Î");
			fragPasswordRepeat.setIsPassword(true);
		}
	}



	public String getText() {
		// TODO Auto-generated method stub
		return fragPassword.getText().toString();
	}
	
	public static interface onSubmitClickedListener{
		void OnSubmitClicked();
	}
	
	onSubmitClickedListener onsubmitClickedListener;
	
	public void setonSubmitClickedListener(onSubmitClickedListener onSubmitClickedListener){
		this.onsubmitClickedListener = onSubmitClickedListener;
	}
	
	
	void OnSubmitClicked(){
//		if(fragPassword.getText().equals(fragPasswordRepeat.getText())){
			if(onsubmitClickedListener!=null){
				onsubmitClickedListener.OnSubmitClicked();
//			}
//		}else {
//			new AlertDialog.Builder(getActivity())
//			.setMessage("√‹¬Î≤ª“ª÷¬")
//			.show();
		}
	}
}
