package com.vinaysmsrit.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinaysmsrit.bakingapp.Interfaces.IStepClicked;
import com.vinaysmsrit.bakingapp.Interfaces.IStepItemClickListener;
import com.vinaysmsrit.bakingapp.adapter.RecipeStepsListAdapter;
import com.vinaysmsrit.bakingapp.model.Ingredients;
import com.vinaysmsrit.bakingapp.model.Recipe;

import java.util.List;

public class RecipeInfoFragment extends Fragment implements IStepItemClickListener {

    private static final String TAG = RecipeInfoFragment.class.getSimpleName();
    Recipe mRecipe;

    private IStepClicked stepClickListener;

    public void setRecipe(Recipe recipe) {
        Log.d(TAG,"setRecipe");
        this.mRecipe = recipe;
    }

    public RecipeInfoFragment() {
        Log.d(TAG,"RecipeInfoFragment created");
    }

    public void addListener(IStepClicked listener) {
        stepClickListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.recipe_info_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getActivity();

        TextView ingredientsView = (TextView) view.findViewById(R.id.ingredients_view);
        RecyclerView stepsRecyclerView = view.findViewById(R.id.steps_list);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setNestedScrollingEnabled(false);

        if (mRecipe != null) {
            Log.d(TAG,"Recipe Value not NULL");

            List<Ingredients> ingredientsList = mRecipe.getIngredients();
            StringBuffer stringBuffer = new StringBuffer();
            for (Ingredients ingredient: ingredientsList) {
                stringBuffer.append(String.format(" * %.1f %s - %s",ingredient.getQuantity(),ingredient.getMeasure(),ingredient.getIngredient()+"\n"));
            }
            ingredientsView.setText(stringBuffer);

            RecipeStepsListAdapter stepsListAdapter = new RecipeStepsListAdapter(this,mRecipe.getSteps(),false);
            stepsRecyclerView.setAdapter(stepsListAdapter);
        } else {
            Log.d(TAG,"Recipe Value NULL");
        }
    }


    @Override
    public void onStepItemClick(int position) {
        stepClickListener.onStepClicked(position);
    }
}
