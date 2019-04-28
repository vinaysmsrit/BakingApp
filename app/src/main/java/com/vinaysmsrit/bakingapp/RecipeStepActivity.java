package com.vinaysmsrit.bakingapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.vinaysmsrit.bakingapp.model.Recipe;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnButtonInteractionListener {

    private static final int DEFAULT_INT_VALUE = -1;

    private int mCurrentPosition = DEFAULT_INT_VALUE;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipe_step_activity);


        if (savedInstanceState == null) {
            mRecipe = getIntent().getParcelableExtra(RecipeUtil.RECIPE_INFO);
            mCurrentPosition = getIntent().getIntExtra(RecipeUtil.STEP_POSITION,-1);
            setupStepFragment(mCurrentPosition,mRecipe);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupStepFragment(int position,Recipe recipe) {
        if (recipe != null) {
            setTitle(recipe.getName());
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeUtil.RECIPE_INFO,recipe);
            arguments.putInt(RecipeUtil.STEP_POSITION,position);
            RecipeStepFragment fragment = new RecipeStepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonInteraction(int position) {
        setupStepFragment(position,mRecipe);
    }
}
