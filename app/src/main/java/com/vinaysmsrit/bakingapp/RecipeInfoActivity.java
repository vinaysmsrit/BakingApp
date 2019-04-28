package com.vinaysmsrit.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.vinaysmsrit.bakingapp.Interfaces.IStepClicked;
import com.vinaysmsrit.bakingapp.model.Recipe;
import com.vinaysmsrit.bakingapp.model.Steps;
import com.vinaysmsrit.bakingapp.R;

public class RecipeInfoActivity extends AppCompatActivity implements IStepClicked, RecipeStepFragment.OnButtonInteractionListener {

    private static final String TAG = RecipeUtil.APP_TAG + RecipeInfoActivity.class.getSimpleName();

    public Recipe getRecipe() {
        return mRecipe;
    }

    Recipe mRecipe;
    boolean mTwoPane;
    private int mCurStepPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_info_activity);


        Intent intent = getIntent();
        if (intent.hasExtra(RecipeUtil.RECIPE_INFO)) {
            Log.d(TAG," RECIPE_INFO conatined ");
            mRecipe = getIntent().getParcelableExtra(RecipeUtil.RECIPE_INFO);
        } else if (savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelable(RecipeUtil.RECIPE_INFO);
            mCurStepPosition = savedInstanceState.getInt(RecipeUtil.STEP_POSITION,0);
        }

        mTwoPane = getResources().getBoolean(R.bool.twoPane);

        if (mRecipe != null) {
            setTitle(mRecipe.getName());
            setupRecipeFragment(mRecipe);

            if(mTwoPane) {
                setupStepFragment(mCurStepPosition,mRecipe);
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(RecipeUtil.RECIPE_INFO,mRecipe);
        outState.putInt(RecipeUtil.STEP_POSITION,mCurStepPosition);
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

    private void setupRecipeFragment(Recipe recipe) {
        RecipeInfoFragment recipeInfoFragment = new RecipeInfoFragment();
        recipeInfoFragment.setRecipe(recipe);
        recipeInfoFragment.addListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_info_container, recipeInfoFragment)
                .commit();
    }

    @Override
    public void onStepClicked(int position) {
        Log.d(TAG,"onStepClicked currentStep position= "+position);
        Toast.makeText(this,"Step pos="+position+" clicked", Toast.LENGTH_LONG).show();
        setupStepFragment(position, mRecipe);
    }

    private void setupStepFragment(int position, Recipe recipe) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeUtil.RECIPE_INFO,recipe);
            arguments.putInt(RecipeUtil.STEP_POSITION,position);
            RecipeStepFragment fragment = new RecipeStepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, fragment)
                    .commit();
        } else {
            Intent recipeStepActivityIntent = new Intent(RecipeInfoActivity.this,RecipeStepActivity.class);
            recipeStepActivityIntent.putExtra(RecipeUtil.STEP_POSITION,position);
            recipeStepActivityIntent.putExtra(RecipeUtil.RECIPE_INFO,mRecipe);
            startActivity(recipeStepActivityIntent);
        }
    }

    @Override
    public void onButtonInteraction(int position) {
        if (mTwoPane) {
            setupStepFragment(position,mRecipe);
        } else {
            Log.e(TAG,"Should NEVER COME HERE !!");
        }

    }
}
