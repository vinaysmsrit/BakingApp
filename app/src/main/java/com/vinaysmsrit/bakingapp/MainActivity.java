package com.vinaysmsrit.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.vinaysmsrit.bakingapp.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView mPostsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPostsView = findViewById(R.id.posts_tv);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeAPI recipeAPI = retrofit.create(RecipeAPI.class);

        Call<List<Post>> call = recipeAPI.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(!response.isSuccessful()) {
                    mPostsView.setText("Response Failed Code: "+response.code());
                    return;
                }

                List<Post> posts = response.body();

                for(Post post: posts) {
                    mPostsView.append(" id:"+post.getId()+"\n");
                    mPostsView.append(" userId:"+post.getUserId()+"\n");
                    mPostsView.append(" title:"+post.getTitle()+"\n");
                    mPostsView.append(" body:"+post.getText()+"\n\n");
                }


            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                 mPostsView.setText("Error Retriving Posts");
            }
        });





    }
}
