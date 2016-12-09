package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.fragments.inputcells.PictureInputCellFragment;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {
	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	SimpleTextInputCellFragment fragInputEmailAddress;
	SimpleTextInputCellFragment fragInputName;
	PictureInputCellFragment fragInputAvatar;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragInputEmailAddress = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
		fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);
		fragInputAvatar = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_picture);


		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submit();
			}
		});
	}



	void submit(){
		String password = fragInputCellPassword.getText();
		String passwordRepeat = fragInputCellPasswordRepeat.getText();
		String account = fragInputCellAccount.getText();
		String name = fragInputName.getText();
		String email = fragInputEmailAddress.getText();
		byte[] picture = fragInputAvatar.getPngData();

		if(!password.equals(passwordRepeat)){
			new AlertDialog.Builder(RegisterActivity.this)
			.setMessage("重复密码不一致")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setNegativeButton("好",null)
			.show();
			return;
		}


		password = MD5.getMD5(password+"gkkkghkg");

		OkHttpClient client = new OkHttpClient();
		MultipartBody.Builder  requestBuilderBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("passwordHash", password);


		if(fragInputAvatar.getPngData()!=null){
			requestBuilderBody
			.addFormDataPart(
					"avatar",
					"avatar",
					RequestBody
					.create(MediaType.parse("image/png"),
							fragInputAvatar.getPngData()));


		}



		Request request = new Request.Builder()
				.url("http://172.27.0.19:8080/membercenter/api/register")
				.method("post", null)
				.post(requestBuilderBody.build())
				.build();


		final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("请稍后");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {

				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();

						try{
							RegisterActivity.this.onResponse(arg0,arg1.body().string());
						}catch(Exception e){
							e.printStackTrace();
							RegisterActivity.this.onFailure(arg0,e);
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


	void onResponse(Call arg0,String responseBody){
		new AlertDialog.Builder(this)
		.setTitle("注册成功")
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


	void onFailure(Call arg0,Exception arg1){
		new AlertDialog.Builder(this)
		.setTitle("请求失败")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("好", null)
		.show();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		fragInputCellAccount.setLabelText("账户名");{
			fragInputCellAccount.setHintText("请输入账户名");
		}

		fragInputCellPassword.setLabelText("密码");{
			fragInputCellPassword.setHintText("请输入密码");
			fragInputCellPasswordRepeat.setIsPassword(true);
		}

		fragInputCellPasswordRepeat.setLabelText("重复密码");{
			fragInputCellPasswordRepeat.setHintText("请重复输入密码");
			fragInputCellPasswordRepeat.setIsPassword(true);
		}

		fragInputEmailAddress.setLabelText("电子邮件");{
			fragInputEmailAddress.setHintText("请输入电子邮件");
		}
		fragInputName.setLabelText("姓名");{
			fragInputName.setHintText("请输入姓名");
		}


	}

}
