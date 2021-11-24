package com.codejune.sutaekhighschool.meal;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.codejune.sutaekhighschool.MealLoadHelper;
import com.codejune.sutaekhighschool.R;

public class TuesdayMeal extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    String[] lunchstring = new String[7];
    String[] dinnerstring = new String[7];
    String[] lunchkcalstring = new String[7];
    String[] dinnerkcalstring = new String[7];
    String[] lunchpeople = new String[7];
    String[] dinnerpeople = new String[7];
    TextView LunchText;
    TextView DinnerText;
    SwipeRefreshLayout SRL;

    public static TuesdayMeal newInstance(int sectionNumber) {
        TuesdayMeal fragment = new TuesdayMeal();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public TuesdayMeal() {
    }

    private final Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SRL = (SwipeRefreshLayout)
                inflater.inflate(R.layout.fragment_day_meal, container, false);
        LunchText = (TextView)SRL.findViewById(R.id.lunchtxt);
        DinnerText = (TextView)SRL.findViewById(R.id.dinnertxt);
        SRL.setRefreshing(true);
        SRL.setColorSchemeColors(Color.rgb(231, 76, 60),
                Color.rgb(46, 204, 113), Color.rgb(41, 128, 185),
                Color.rgb(241, 196, 15));
        SRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMealTask();
            }
        });
        loadMealTask();
        return SRL;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private void loadMealTask(){
        SRL.setRefreshing(true);
        final Handler mHandler = new Handler();
        new Thread()
        {

            public void run()
            {
                mHandler.post(new Runnable(){

                    public void run()
                    {

                    }
                });
                try{
                    lunchstring = MealLoadHelper.getMeal("goe.go.kr","J100000656","4","04","2"); //Get Lunch Menu Date
                    lunchkcalstring = MealLoadHelper.getKcal("goe.go.kr","J100000656","4","04","2"); //Get Lunch Menu Kcal Value
                    dinnerstring = MealLoadHelper.getMeal("goe.go.kr","J100000656","4","04","3"); //Get Dinner Menu Date
                    dinnerkcalstring = MealLoadHelper.getKcal("goe.go.kr","J100000656","4","04","3"); //Get Dinner Menu Kcal Value
                    lunchpeople = MealLoadHelper.getPeople("goe.go.kr","J100000656","4","04","2"); //Get Lunch People
                    dinnerpeople = MealLoadHelper.getPeople("goe.go.kr","J100000656","4","04","3"); //Get Dinner People
                }catch (Exception e){}

                mHandler.post(new Runnable()
                {
                    public void run() {

                        Log.d("Setting Text", "Setting Meal Text");
                        Log.d("Lunch", "Lunch : " + lunchstring[2] + "/ Kcal : " + lunchkcalstring[2]);
                        Log.d("Dinner", "Dinner : " + dinnerstring[2] + "/ Kcal : " + dinnerkcalstring[2]);
                        try {
                            if (lunchstring[2] == null || "".equals(lunchstring[2]) || " ".equals(lunchstring[2])) {
                                LunchText.setText("\n" + getString(R.string.mealnone));
                            } else {
                                LunchText.setText("\n" + lunchstring[2] + "\n\n" + lunchkcalstring[2]);
                            } if (dinnerstring[2] == null || "".equals(dinnerstring[2]) || " ".equals(dinnerstring[2])) {
                                DinnerText.setText("\n" + getString(R.string.mealnone));
                            } else {
                                DinnerText.setText("\n" + dinnerstring[2] + "\n\n" + dinnerkcalstring[2]);
                            }
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                        

                        Log.d("DONE", "Done Setting Content");
                        SRL.setRefreshing(false);
                        handler.sendEmptyMessage(0);

                    }
                });
            }
        }.start();
    }
}
