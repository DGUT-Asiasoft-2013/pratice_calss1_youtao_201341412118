package com.example.helloworld.fragments.inputcells;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.example.helloworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureInputCellFragment extends BaseInputCellFragment {

	final int REQUESTCODE_CAMERA = 1;
	final int REQUESTCODE_ALBUM = 2;

	ImageView imageView;
	TextView labelText;
	TextView hintText;
	byte[] pngData;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_inputcell_picture, container);
		imageView = (ImageView) view.findViewById(R.id.image);
		labelText = (TextView) view.findViewById(R.id.label);
		hintText = (TextView) view.findViewById(R.id.hint);

		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onImageViewClicked();
			}
		});


		return view;
	}



	void onImageViewClicked(){
		String[] items={
				"����",
				"���"
		};

		new AlertDialog.Builder(getActivity())
		.setTitle(hintText.getText())
		.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch(which){
				case 0:
					takePhoto();
					break;
				case 1:
					pickFromAlbum();
					break;

				default:
					break;
				}

			}
		})
		.setNegativeButton("ȡ��", null)
		.show();
	}

	void takePhoto(){
		Intent itnt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(itnt,REQUESTCODE_CAMERA);
	}

	void pickFromAlbum(){
		Intent itnt = new Intent(Intent.ACTION_GET_CONTENT);
		itnt.setType("image/*");
		startActivityForResult(itnt,REQUESTCODE_ALBUM);

	}
	
	
	void saveBitmap(Bitmap bmp){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, baos);
		pngData = baos.toByteArray();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_CANCELED) return;
		if(requestCode==REQUESTCODE_CAMERA){
			Bitmap bmp = (Bitmap)data.getExtras().get("data");
			saveBitmap(bmp);
			imageView.setImageBitmap(bmp);
		}else if(requestCode ==REQUESTCODE_ALBUM ){
			try{
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
				saveBitmap(bmp);
				imageView.setImageBitmap(bmp);
			}catch(Exception e){
				e.printStackTrace();
			}
	
		}

	}

	public void setHintText(String hintText){
		this.hintText.setHint(hintText);
	}


	public void setLabeltext(String labelText) {

		this.labelText.setText(labelText);
	}
	public byte[] getPngData() {
		// TODO Auto-generated method stub
		return pngData;
	}

}
