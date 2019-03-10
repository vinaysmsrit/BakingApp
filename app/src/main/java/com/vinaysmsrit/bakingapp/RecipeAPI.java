package com.vinaysmsrit.bakingapp;

import com.vinaysmsrit.bakingapp.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeAPI {

    @GET("posts")
    Call<List<Post>> getPosts();
}
