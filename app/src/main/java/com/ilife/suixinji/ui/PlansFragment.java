package com.ilife.suixinji.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ilife.suixinji.R;

public class PlansFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageView iv = new ImageView(getActivity());
		iv.setImageResource(R.drawable.ic_launcher);
		return iv;
	}
}
