package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;


public class PlaceholderFragment extends Fragment {
    private RecyclerView mRecyclerview;
    private FirendAdapter adapter= new FirendAdapter();
    private LottieAnimationView animationView;
    private Animation loadAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment1, container,false);
        animationView = view.findViewById(R.id.animation);
        animationView.setVisibility(View.VISIBLE);
        mRecyclerview = view.findViewById(R.id.rv);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerview.setAlpha(0f);
        mRecyclerview.setVisibility(View.GONE);
        getfirends();
        mRecyclerview.setAdapter(adapter);
        return view;
    }

    private void getfirends(){
        final List<String> friend_list = new ArrayList<>();
        for(int i=1; i<=100;i++){
            String tx = "第" + i + "好友";
            friend_list.add(tx);
        }
        adapter.notifyItem(friend_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        animationView.setRepeatCount(-1);
        animationView.playAnimation();
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_fade_out);
                animationView.startAnimation(loadAnimation);
                animationView.setVisibility(View.GONE);
                loadAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_fade_in);
                mRecyclerview.startAnimation(loadAnimation);
                mRecyclerview.setAlpha(1f);
                mRecyclerview.setVisibility(View.VISIBLE);
            }
        },5000);
    }


}
