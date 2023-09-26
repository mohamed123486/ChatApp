package com.example.finelall.Ad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.finelall.Fragments.Chat;
import com.example.finelall.Fragments.Contctes;
import com.example.finelall.Fragments.Groups;
import com.example.finelall.Fragments.Request;

public class Fragment_Ad extends FragmentPagerAdapter {
    public Fragment_Ad(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new Chat();
            case 1:return new Groups();
            case 2:return new Contctes();
            case 3:return new Request();
            default:return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return "Chat";
            case 1:return "Room";
            case 2:return "Contact's";
            case 3:return "Request";
            default:return null;
        }

    }
}
