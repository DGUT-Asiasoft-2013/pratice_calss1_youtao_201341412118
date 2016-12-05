package com.example.helloworld.fragments.inputcells;

import com.example.helloworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	ImageView imageView;
	TextView labelText;
	TextView hintText;

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
				"≈ƒ’’",
				"œ‡≤·"
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
		.setNegativeButton("»°œ˚", null)
		.show();
	}
	
	void takePhoto(){
		Intent itnt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(itnt,1);
	}
	
	void pickFromAlbum(){
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_CANCELED) return;
		if(resultCode == 1){
			Log.d("camera capture", data.getExtras().keySet().toString());
			Toast.makeText(getActivity(), data.getDataString(), Toast.LENGTH_LONG);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void setHintText(String hintText){
		this.hintText.setHint(hintText);
	}


	public void setLabeltext(String labelText) {

		this.labelText.setText(labelText);
	}

}
