package com.josepvictorr.kasep.intro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josepvictorr.kasep.MainActivity;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.IntroFragmentSlideAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntroFragment extends Fragment {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_intro, container, false);

        viewPager = root.findViewById(R.id.view_pager_info);
        return root;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        List<Fragment> list = new ArrayList<>();
        list.add(new IntroFragmentSlide1());
        list.add(new IntroFragmentSlide2());
        list.add(new IntroFragmentSlide3());
        list.add(new IntroFragmentSlide4());

        pagerAdapter = new IntroFragmentSlideAdapter(requireActivity().getSupportFragmentManager(), list);

        viewPager.setAdapter(pagerAdapter);
    }
}