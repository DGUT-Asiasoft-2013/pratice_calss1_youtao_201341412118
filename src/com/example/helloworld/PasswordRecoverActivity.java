package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.fragment.PasswordRecoverStep1Fragments;
import com.example.helloworld.fragment.PasswordRecoverStep1Fragments.OnGoNextListener;
import com.example.helloworld.fragment.PasswordRecoverStep2Fragments;
import com.example.helloworld.fragment.PasswordRecoverStep2Fragments.onSubmitClickedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
		
		step2.setonSubmitClickedListener(new onSubmitClickedListener() {
			
			@Override
			public void OnSubmitClicked() {
				// TODO Auto-generated method stub
				 goSubmit();
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

	void goSubmit(){
		OkHttpClient client = Server.getSharedClient();
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("email", step1.getText())
				.addFormDataPart("passwordHash", MD5.getMD5(step2.getText()))
				.build();

		final ProgressDialog progressDialog = new ProgressDialog(PasswordRecoverActivity.this);
		progressDialog.setMessage("请稍后");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		
		Request request = Server.requestBuilderWithApi("passwordrecover").post(body).build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				// TODO Auto-generated method stub

				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();

						try{
							PasswordRecoverActivity.this.onResponse(arg0,arg1.body().string());
						}catch(Exception e){
							e.printStackTrace();
							PasswordRecoverActivity.this.onFailure(arg0,e);
						}
					}
				});
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progressDialog.dismiss();

						onFailure(arg0, arg1);
					}
				});
			}
		});
	}

	protected void onFailure(Call arg0, Exception arg1) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setTitle("修改失败")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("好", null)
		.show();
	}

	protected void onResponse(Call arg0, String responseBody) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
		.setTitle("修改成功")
		.setMessage(responseBody)
		.setPositiveButton("好", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		})
		.show();
	}
}
