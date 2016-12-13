package com.example.helloworld.fragment.pages;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.entity.Article;
import com.example.helloworld.entity.Page;
import com.example.helloworld.fragment.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;



public class FeedLisFragment extends Fragment {
	View view;
	ListView listView;
	View btnLoadMore;
	TextView textLoadMore;
	//	String[] data;

	List<Article> data;
	int page = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_page_fedd_list, null);
			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button,null);
			textLoadMore=(TextView) btnLoadMore.findViewById(R.id.text);

			listView = (ListView)view.findViewById(R.id.list);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(lisAdapter);
			//
			//			Random rand = new Random();
			//			data = new String[10+Math.abs(rand.nextInt()%20)];
			//
			//			for(int i=0; i<data.length;i++){
			//				data[i] = "THIS POW IS"+rand.nextInt();
			//			}
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});

			btnLoadMore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					loadmore();

				}
			});


		}
		return view;
	}

	BaseAdapter lisAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_feed_item, null);
			}else{
				view = convertView;
			}

			TextView textContent =(TextView) view.findViewById(R.id.text);
			TextView textTitle = (TextView) view.findViewById(R.id.title);
			TextView textAuthorName = (TextView) view.findViewById(R.id.username);

			TextView textDate = (TextView) view.findViewById(R.id.date);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);

			Article article = data.get(position);

			textContent.setText(article.getText());
			textTitle.setText(article.getTitle());
			textAuthorName.setText(article.getAuthorName());


			avatar.load(Server.serverAddress+article.getAuthorAvatar());
			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm",article.getCreateDate()).toString();
			textDate.setText(dateStr);
			return view;

		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data==null ? 0 :data.size();
		}


	};


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
	}
	void reload(){
		Request request= Server.requestBuilderWithApi("feeds")
				.get()
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					Page<Article> data = new ObjectMapper()
							.readValue(arg1.body().string(),
									new TypeReference<Page<Article>>(){});
					FeedLisFragment.this.page = data.getNumber();
					FeedLisFragment.this.data = data.getContent();

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							lisAdapter.notifyDataSetInvalidated();

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
			public void onFailure(Call arg0, final IOException e) {
				// TODO Auto-generated method stub
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

	void onItemClicked(int position){
		Article text =data.get(position);
		Intent itnt = new Intent(getActivity(),FeedContentActivity.class);
		itnt.putExtra("Text", text);
		startActivity(itnt);
	}


	void loadmore(){
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中…");

		Request request = Server.requestBuilderWithApi("feeds/"+(page+1)).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});

				try{
					Page<Article> feeds = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Article>>() {});
					if(feeds.getNumber()>page){
						if(data==null){
							data = feeds.getContent();
						}else{
							data.addAll(feeds.getContent());
						}
						page = feeds.getNumber();

						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								lisAdapter.notifyDataSetChanged();
							}
						});
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
			}
		});
	}

}
