/*
Copyright 2012 Aphid Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.ilife.suixinji;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.aphidmobile.flip.FlipViewController;
import com.ilife.suixinji.ui.FlipAdapter;

public class FlipActivity extends Activity {

	private FlipViewController flipView;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		int imageIndex = bundle.getInt("imageIndex");
		List<String> datas = bundle.getStringArrayList("imageList");

		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		flipView.setAdapter(new FlipAdapter(this, datas), imageIndex);
		setContentView(flipView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		flipView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		flipView.onPause();
	}
}
