package com.vinaysmsrit.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vinaysmsrit.bakingapp.model.Recipe;
import com.vinaysmsrit.bakingapp.model.Steps;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeStepFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = RecipeUtil.APP_TAG+RecipeStepFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Recipe mRecipe;
    Steps mCurStep;
    private int mCurrentStepPosition = -1;
    private int mStepsCount = 0;

    private OnButtonInteractionListener mListener;

    @BindView(R.id.step_details)
    TextView mStepTextView;

    @BindView(R.id.short_description_view)
    TextView mShortDescription;

    @BindView(R.id.video_url_view)
    TextView mVideoUrl;

    @BindView(R.id.prev_button)
    Button mPrevButton;

    @BindView(R.id.next_button)
    Button mNextButton;

    public RecipeStepFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null)&&(getArguments().containsKey(RecipeUtil.RECIPE_INFO))) {
            mRecipe = (Recipe) getArguments().getParcelable(RecipeUtil.RECIPE_INFO);
            mCurrentStepPosition = getArguments().getInt(RecipeUtil.STEP_POSITION);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        mStepsCount = mRecipe.getSteps().size();


        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        Log.d(TAG,"onViewCreated mStepsCount="+mStepsCount+", mCurrentStepPosition="+mCurrentStepPosition);
        if (mCurrentStepPosition >= mStepsCount) {
            mCurrentStepPosition = mStepsCount-1;
            mNextButton.setEnabled(false);
            mCurStep = mRecipe.getSteps().get(mStepsCount-1);
        } else if (mCurrentStepPosition == 0) {
            mPrevButton.setEnabled(false);
            mCurStep = mRecipe.getSteps().get(0);
        } else {
            mCurStep = mRecipe.getSteps().get(mCurrentStepPosition);
        }

        Log.d(TAG,"onViewCreated VideoUrl: "+mCurStep.getVideoURL()+"\n" +
                " Description:"+mCurStep.getDescription());

        mShortDescription.setText("Step "+mCurrentStepPosition+" : "+mCurStep.getShortDescription());
        mVideoUrl.setText("VideoUrl: "+mCurStep.getVideoURL());
        mStepTextView.setText(" Description:"+mCurStep.getDescription());

    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recipe_step_fragment, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonInteractionListener) {
            mListener = (OnButtonInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.next_button:
                mCurrentStepPosition++;
                Log.d(TAG,"onClick NEXT mStepsCount="+mStepsCount+", mCurrentStepPosition="+mCurrentStepPosition);
                if (mCurrentStepPosition+1 == mStepsCount) {
                    view.setEnabled(false);
                    view.invalidate();
                }
                mListener.onButtonInteraction(mCurrentStepPosition);
                break;
            case R.id.prev_button:
                mCurrentStepPosition--;
                Log.d(TAG,"onClick PREV mStepsCount="+mStepsCount+", mCurrentStepPosition="+mCurrentStepPosition);
                if (mCurrentStepPosition == 0) {
                    view.setEnabled(false);
                    view.invalidate();
                }
                mListener.onButtonInteraction(mCurrentStepPosition);
                break;
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnButtonInteractionListener {
        void onButtonInteraction(int position);
    }
}