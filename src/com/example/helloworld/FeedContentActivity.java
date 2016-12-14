package com.example.helloworld;

import java.io.IOException;
import java.util.List;




import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.entity.Article;
import com.example.helloworld.entity.Comment;
import com.example.helloworld.entity.Page;
import com.example.helloworld.fragment.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FeedContentActivity extends Activity {

	private Article article;
	private View loadMoreView;

	List<Comment> comments;
	int page = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feed_content);

		article =(Article) getIntent().getSerializableExtra("Text");

		ListView list =(ListView) findViewById(R.id.list);
		View headerView = LayoutInflater.from(this).inflate(R.layout.widget_acticle_detial,null);{
			TextView textContent = (TextView) headerView.findViewById(R.id.text);
			TextView textTitle = (TextView) headerView.findViewById(R.id.title);
			TextView textAuthorName = (TextView) headerView.findViewById(R.id.username);
			TextView textDate = (TextView) headerView.findViewById(R.id.date);
			AvatarView avatar = (AvatarView) headerView.findViewById(R.id.avatar);


			textContent.setText(article.getText());
			textTitle.setText(article.getTitle());
			textAuthorName.setText(article.getAuthorName());
			avatar.load(Server.serverAddress+article.getAuthorAvatar());

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
			textDate.setText(dateStr);

			list.addHeaderView(headerView, null, false);

			headerView.findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					makeComment();
				}
			});
		//	LayoutInflater().from(this).inflate(R.layout.widget_load_more_button,null)
			loadMoreView = LayoutInflater.from(this).inflate(R.layout.widget_load_more_button,null);{
				list.addFooterView(loadMoreView);
			}
		}
		list.setAdapter(adapter);
	}
	
	BaseAdapter adapter =new BaseAdapter() {

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			
			
			if(view == null){
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_feed_item,null);
				
			}
			
			Comment comment = comments.get(position);

			TextView textContent = (TextView) view.findViewById(R.id.text);
			TextView textTitle = (TextView) view.findViewById(R.id.title);
			TextView textAuthorName = (TextView) view.findViewById(R.id.username);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);

			textContent.setText(comment.getText());
			textAuthorName.setText(comment.getAuthor().getName());
			avatar.load(Server.serverAddress +comment.getAuthor().getAvatar());

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
			textDate.setText(dateStr);
			return view;

		}

		@Override
		public long getItemId(int position) {

			return comments.get(position).getId();
		}

		@Override
		public Object getItem(int position) {

			return comments.get(position);
		}

		@Override
		public int getCount() {

			return comments == null ? 0: comments.size();
		}
	};
	
	void makeComment(){
		Intent itnt = new Intent(this,NewCommentActivity.class);
		itnt.putExtra("data", article);
		startActivity(itnt);
		overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reload();
	}

	void reload() {
		Request request = Server.requestBuilderWithApi("/article/"+article.getId()+"/comments")
				.get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					String responseBody = arg1.body().string();
					Log.d("responseBody", responseBody);
					
					final Page<Comment> data = new ObjectMapper().readValue(responseBody, 
							new TypeReference<Page<Comment>>() {
						
					});
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							FeedContentActivity.this.reloadDate(data);
							
						}
					});
				}catch(final Exception e){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							FeedContentActivity.this.onFailure(e);
							
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						FeedContentActivity.this.onFailure(e);
						
					}
				});
				
			}
		});
	}

	 protected void appendData(Page<Comment> data) {
		if(data.getNumber()>page){
			page =data.getNumber();
			if(comments == null){
				comments = data.getContent();
				
			}else {
				comments.addAll(data.getContent());
			}
		}
		adapter.notifyDataSetChanged();
		
	}

	protected void reloadDate(Page<Comment> data) {
		page = data.getNumber();
		comments = data.getContent();
		adapter.notifyDataSetInvalidated();
		
	}

	void onFailure(Exception e) {
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
		
	}
}