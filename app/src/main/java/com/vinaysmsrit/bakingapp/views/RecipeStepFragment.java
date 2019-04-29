package com.vinaysmsrit.bakingapp.views;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.vinaysmsrit.bakingapp.RecipeConstants;
import com.vinaysmsrit.bakingapp.model.Recipe;
import com.vinaysmsrit.bakingapp.model.Steps;
import com.vinaysmsrit.bakingapp.R;

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
    private static final String TAG = RecipeConstants.APP_TAG+RecipeStepFragment.class.getSimpleName();

    Recipe mRecipe;
    Steps mCurStep;
    private int mCurrentStepPosition = -1;
    private int mStepsCount = 0;

    private OnButtonInteractionListener mListener;
    private TrackSelector trackSelector;
    private SimpleExoPlayer exoPlayer;
    private long position = 0;
    private boolean mTwoPane;

    @BindView(R.id.step_details)
    TextView mDescriptionView;

    @BindView(R.id.short_description_view)
    TextView mShortDescription;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView playerView;

    @BindView(R.id.image_no_video)
    ImageView mImgNoVideo;

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

        if ((getArguments() != null)&&(getArguments().containsKey(RecipeConstants.RECIPE_INFO))) {
            mRecipe = (Recipe) getArguments().getParcelable(RecipeConstants.RECIPE_INFO);
            mCurrentStepPosition = getArguments().getInt(RecipeConstants.STEP_POSITION);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        mStepsCount = mRecipe.getSteps().size();

        mTwoPane = getResources().getBoolean(R.bool.twoPane);

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

        if (!TextUtils.isEmpty(mCurStep.getVideoURL())) {
            initializePlayer(Uri.parse(mCurStep.getVideoURL()));
        } else {
            mImgNoVideo.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
        }

        Log.d(TAG,"onViewCreated VideoUrl: "+mCurStep.getVideoURL()+"\n" +
                " Description:"+mCurStep.getDescription());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !mTwoPane) {
            showFullScreen();
        } else {
            mShortDescription.setText(mCurStep.getShortDescription());
            mDescriptionView.setText(mCurStep.getDescription());
        }
    }

    private void showFullScreen() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            //Use Google's "LeanBack" mode to get fullscreen in landscape
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        position = exoPlayer != null ? exoPlayer.getCurrentPosition() : 0;
        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.seekTo(0);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
            trackSelector = null;
        }
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
