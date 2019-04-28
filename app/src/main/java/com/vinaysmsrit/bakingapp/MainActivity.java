package com.vinaysmsrit.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vinaysmsrit.bakingapp.adapter.RecipeListAdapter;
import com.vinaysmsrit.bakingapp.model.Ingredients;
import com.vinaysmsrit.bakingapp.model.Recipe;
import com.vinaysmsrit.bakingapp.views.RecipeInfoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.RecipeItemClickListener {

    @BindView(R.id.recipes_recyclerview)
    RecyclerView mRecyclerView;

    boolean mTwoPane;
    RecipeAPI mRecipeAPI;
    RecipeListAdapter mAdapter;
    private static final String TAG = RecipeConstants.APP_TAG + MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecipeConstants.RECIPE_BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRecipeAPI = retrofit.create(RecipeAPI.class);

        mTwoPane = getResources().getBoolean(R.bool.twoPane);
        int numColumns = mTwoPane ? 3: 1;

        GridLayoutManager layoutManager = new GridLayoutManager(this,numColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecipeListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        getAllRecipes();
    }

    private void getAllRecipes() {
        Call<List<Recipe>> call = mRecipeAPI.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if(!response.isSuccessful()) {
                    Log.d(TAG,"Response Failed Code: "+response.code());
                    return;
                }

                List<Recipe> recipeList = response.body();
                mAdapter.setRecipeList(recipeList);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG,"Error Retriving Posts");
            }
        });
    }

    @Override
    public void onRecipeItemClick(Recipe recipe) {
        if (recipe != null) {

            if (recipe != null) {
                SharedPreferences sharedPreferences = getSharedPreferences(RecipeConstants.SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);

                List<Ingredients> ingredientsList = recipe.getIngredients();
                StringBuffer stringBuffer = new StringBuffer(recipe.getName());
                for (Ingredients ingredient: ingredientsList) {
                    stringBuffer.append(String.format("\n * %s",ingredient.getIngredient()));
                }
                sharedPreferences.edit().putString(RecipeConstants.WIDGET_VALUE, stringBuffer.toString()).apply();
            }

            Intent recipeActivityIntent = new Intent(MainActivity.this,RecipeInfoActivity.class);
            recipeActivityIntent.putExtra(RecipeConstants.RECIPE_INFO,recipe);
            startActivity(recipeActivityIntent);
        }
    }
}
