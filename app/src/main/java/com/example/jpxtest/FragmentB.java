package com.example.jpxtest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jpxtest.databinding.FragmentBBinding;


public class FragmentB extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentBBinding bBinding = FragmentBBinding.inflate(inflater, container, false);
        return bBinding.getRoot();
    }
}
