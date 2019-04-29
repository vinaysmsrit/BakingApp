package com.vinaysmsrit.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinaysmsrit.bakingapp.Interfaces.IStepItemClickListener;
import com.vinaysmsrit.bakingapp.R;
import com.vinaysmsrit.bakingapp.RecipeConstants;
import com.vinaysmsrit.bakingapp.model.Steps;

import java.util.List;

public class RecipeStepsListAdapter extends RecyclerView.Adapter<RecipeStepsListAdapter.ViewHolder> {

    private static final String TAG = RecipeConstants.APP_TAG + RecipeStepsListAdapter.class.getSimpleName();
    private Context mContext;
    private List<Steps> mValues;
    private boolean mTwoPane;

    private final IStepItemClickListener stepItemClickListener;

    public RecipeStepsListAdapter(IStepItemClickListener listener,
                           List<Steps> items,
                           boolean twoPane) {
        stepItemClickListener = listener;
        mValues = items;
        mTwoPane = twoPane;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mStepInfoView.setText((mValues.get(position).getStepId()+1)+". "+mValues.get(position).getShortDescription());
        holder.itemView.setTag(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mStepInfoView;

        ViewHolder(View view) {
            super(view);
            mStepInfoView = (TextView) view.findViewById(R.id.step_info);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG,"RecipeStepsListAdapter - onClick, pos="+getAdapterPosition());
            stepItemClickListener.onStepItemClick(getAdapterPosition());
        }
    }
}
