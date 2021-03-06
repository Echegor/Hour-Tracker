package archelo.hourtracker.activities;

import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;

import archelo.hourtracker.R;
import archelo.hourtracker.fragments.TabFragment;

public class TimeCollector extends AppCompatActivity {
    private static String TAG = "TimeCollector";
    int height;
    int width;
//    FloatingActionButton mFab;
    ConstraintLayout mConstraintLayout;
    CoordinatorLayout mCoordinatorLayout;

    int duration = 300;
    Transition sharedElementEnterTransition;
    Transition.TransitionListener mTransitionListener;

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_collector);

        //getWindow().setEnterTransition(null);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        height = displayMetrics.heightPixels;
//        width = displayMetrics.widthPixels;
//        mFab = (FloatingActionButton) findViewById(R.id.next_fab);
//
//        mConstraintLayout = (ConstraintLayout) findViewById(R.id.bg);
//
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
//
//        sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();


//        mTransitionListener = new Transition.TransitionListener() {
//            @Override
//            public void onTransitionStart(Transition transition) {
//
//            }
//
//            @Override
//            public void onTransitionEnd(Transition transition) {
//                setAnim(mCoordinatorLayout, true);
//                setFab(mFab, false);
//            }
//
//            @Override
//            public void onTransitionCancel(Transition transition) {
//
//            }
//
//            @Override
//            public void onTransitionPause(Transition transition) {
//
//            }
//
//            @Override
//            public void onTransitionResume(Transition transition) {
//
//            }
//        };

        //sharedElementEnterTransition.addListener(mTransitionListener);


    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"Back wsa pressed");
        FragmentManager fragmentManager = getSupportFragmentManager();
        TabFragment tab = (TabFragment) fragmentManager.findFragmentById(R.id.fragment_place);
        if(tab != null ){
            Log.d(TAG,"Fragment has handled back press");
            if(!tab.onBackPressed())
                new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Discard Changes")
                    .setMessage("Are you sure you want leave?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handleBackPressed();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            return;
        }

        handleBackPressed();


    }

    public void handleBackPressed(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            sharedElementEnterTransition.removeListener(mTransitionListener);
//            setAnim(mCoordinatorLayout, false);
//            setFab(mFab, true);
//        } else {
//
//            super.onBackPressed();
//
//        }
        super.onBackPressed();
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void setAnim(final View myView, boolean isShow) {
//        // previously invisible view
//
//// get the center for the clipping circle
//        int cx = mFab.getWidth() / 2;
//        int cy = mFab.getHeight() / 2;
//
//// get the final radius for the clipping circle
//        float finalRadius = (float) Math.hypot(width, height);
//
//        int[] startingLocation = new int[2];
//        mFab.getLocationInWindow(startingLocation);
//
//// create the animator for this view (the start radius is zero)
//        Animator anim;
//        if (isShow) {
//            anim =
//                    ViewAnimationUtils.createCircularReveal(myView, (int) (mFab.getX() + cx), (int) (mFab.getY() + cy), 0, finalRadius);
//            // make the view visible and start the animation
//            myView.setVisibility(View.VISIBLE);
//        } else {
//            anim =
//                    ViewAnimationUtils.createCircularReveal(myView, (int) (mFab.getX() + cx), (int) (mFab.getY() + cy), finalRadius, 0);
//            anim.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//                    myView.setVisibility(View.INVISIBLE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            });
//        }
//
//        anim.setDuration(duration);
//        anim.start();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    void setFab(final View myView, boolean isShow) {
//
//// get the center for the clipping circle
//        int cx = myView.getWidth() / 2;
//        int cy = myView.getHeight() / 2;
//
//// get the initial radius for the clipping circle
//        float initialRadius = (float) Math.hypot(cx, cy);
//        Animator anim;
//        if (isShow) {
//// create the animation (the final radius is zero)
//            anim =
//                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, initialRadius);
//// make the view invisible when the animation is done
//            anim.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    myView.setVisibility(View.VISIBLE);
//                    finishAfterTransition();
//                }
//            });
//            anim.setDuration(duration);
//        } else {
//            anim =
//                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
//// make the view invisible when the animation is done
//            anim.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    myView.setVisibility(View.INVISIBLE);
//                }
//            });
//        }
//// start the animation
//        anim.start();
//
//    }

    @Override
    public void onResume(){
        Log.d(TAG,"onResume");
        super.onResume();
        mCoordinatorLayout.setVisibility(View.VISIBLE);
//        mFab.setVisibility(View.GONE);
    }
}
