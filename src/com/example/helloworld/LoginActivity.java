package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.entity.User;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {

	SimpleTextInputCellFragment fragAccount,fragPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goRegister();
			}
		});

		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goLogin();

			}
		});;

		findViewById(R.id.btn_forget_password).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goRecoverPassword();
			}
		});


		fragAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);

	}

	protected void onResume(){
		super.onResume();
		fragAccount.setLabelText("用户名");
		fragAccount.setHintText("请输入用户名");
		fragPassword.setLabelText("密码");
		fragPassword.setHintText("请输入密码");
		fragPassword.setIsPassword(true);

	}

	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}

	void goLogin(){


		//		Intent itnt = new Intent(this,HelloworldActivity.class);
		//		startActivity(itnt);
		String account = fragAccount.getText();
		String password = fragPassword.getText();

		password = MD5.getMD5(password+"gkkkghkg");
		OkHttpClient client = new OkHttpClient();
		MultipartBody.Builder  requestBuilderBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account", account)
				.addFormDataPart("passwordHash", password);

		Request request = new Request.Builder()
				.url("http://172.27.0.19:8080/membercenter/api/login")
				.method("post", null)
				.post(requestBuilderBody.build())
				.build();
		final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
		progressDialog.setMessage("请稍后");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				
				final String reSponseString = arg1.body().string();
				ObjectMapper mapper = new ObjectMapper();
				final User user = mapper.readValue(reSponseString, User.class);

				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();

						try{
							LoginActivity.this.onResponse(arg0,reSponseString,user);
						}catch(Exception e){
							e.printStackTrace();
							LoginActivity.this.onFailure(arg0,e);
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


	void onResponse(Call arg0,String responseBody,User user){
//		User user=new User();
		new AlertDialog.Builder(this)
		.setMessage("Hello,"+user.getName())
		
//		.setTitle("登陆成功")
//		.setMessage(responseBody)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				goHelloWorld();
			}
		})

		.show();

	}

	void goHelloWorld(){
		Intent itnt = new Intent(this,HelloworldActivity.class);
		startActivity(itnt);
	}


	void onFailure(Call arg0,Exception arg1){
		new AlertDialog.Builder(this)
		.setTitle("登陆失败")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("好", null)
		.show();
	}


	void goRecoverPassword(){
		Intent itnt = new Intent(this,PasswordRecoverActivity.class);
		startActivity(itnt);
	}
}


















