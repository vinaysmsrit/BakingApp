package com.vinaysmsrit.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinaysmsrit.bakingapp.R;
import com.vinaysmsrit.bakingapp.model.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private RecipeItemClickListener itemClickListener;


    public RecipeListAdapter(RecipeItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        if (recipeList != null) {
            holder.bind(recipeList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return (recipeList != null)? recipeList.size() : 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeView;
        CardView recipeCard;
        Recipe recipeItem;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeCard = itemView.findViewById(R.id.card_view);
            recipeCard.setOnClickListener(this);
            recipeView = itemView.findViewById(R.id.tv_recipe);
        }

        void bind(Recipe recipe) {
            recipeItem = recipe;
            recipeView.setText(recipe.getName());
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onRecipeItemClick(recipeItem);
        }
    }

    public interface RecipeItemClickListener {
        void onRecipeItemClick(Recipe recipe);
    }
}
