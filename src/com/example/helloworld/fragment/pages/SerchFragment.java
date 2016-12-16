package com.example.helloworld.fragment.pages;

import java.io.IOException;
import java.util.List;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.entity.Article;
import com.example.helloworld.entity.Page;
import com.example.helloworld.fragment.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SerchFragment extends Fragment {
	View view;
	ListView listView;
	EditText keyword;

	List<Article> data;
	int page =0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_page_search_list, null);

			listView = (ListView) view.findViewById(R.id.list_search);
			keyword = (EditText) view.findViewById(R.id.search_keyword);

			listView.setAdapter(listAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);

				}
			}); 
		}
		return view;
	}


	@Override
	public void onResume() {

		super.onResume();
		view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				searchByKeyword();

			}
		});
	}

	void searchByKeyword(){
		String keywords = keyword.getText().toString();
		Editable edittext = keyword.getText();
		InputMethodManager inputMethodManager =(InputMethodManager)getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromInputMethod(keyword.getWindowToken(), 0);
		reload(keywords);
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflatParms")

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_feed_item,null);
			}else{
				view = convertView;
			}


			TextView Title = (TextView) view.findViewById(R.id.title); 
			AvatarView avatar = (AvatarView)view.findViewById(R.id.avatar); 
			TextView textContent = (TextView) view.findViewById(R.id.text);  
			TextView textAuthorName = (TextView)view.findViewById(R.id.username); 
			TextView textDate = (TextView)view.findViewById(R.id.date); 

			Article article = data.get(position);

			Title.setText(article.getTitle());
			textContent.setText(article.getText());
			textAuthorName.setText(article.getAuthorName());
			avatar.load(Server.serverAddress+article.getAuthorAvatar());

			String list_createDate = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
			textDate.setText(list_createDate);
			return view;
		}






		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public Object getItem(int position) {

			return data.get(position);
		}

		@Override
		public int getCount() {

			return data == null ? 0:data.size();
		}
	};

	void onItemClicked(int position){

		Article article = data.get(position);

		Intent itnt = new Intent(getActivity(),FeedContentActivity.class);
		itnt.putExtra("data", article);

		startActivity(itnt);
	}

	void reload(String keywords){
		Request request =Server.requestBuilderWithApi("/article/s/"+keywords).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					String value = arg1.body().string();
					final Page<Article>data = new ObjectMapper()
							.readValue(value, new TypeReference<Page<Article>>() {});

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SerchFragment.this.page = data.getNumber();
							SerchFragment.this.data = data.getContent();

							listAdapter.notifyDataSetInvalidated();

						}
					});
				}catch(final Exception e){
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setMessage(e.getMessage())
							.show();

						}
					});
				}

			}

			@Override
			public void onFailure(Call arg0,final IOException e) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(getActivity())
						.setMessage(e.getMessage())
						.show();

					}
				});

			}
		});
	}
}
