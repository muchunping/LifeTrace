package com.ilife.suixinji;

import java.io.File;
import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ilife.suixinji.ui.IdealFragment;
import com.ilife.suixinji.ui.NewRecordActivity;
import com.ilife.suixinji.ui.PlansFragment;
import com.ilife.suixinji.ui.RecordsFragment;
import com.ilife.suixinji.ui.SummaryFragment;

public class MainActivity extends ActionBarActivity implements OnPageChangeListener {
	private TabsAdapter mTabsAdapter;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSearchText = new TextView(this);
		
		pager = (ViewPager) findViewById(R.id.viewPager1);
		mTabsAdapter = new TabsAdapter();
		pager.setAdapter(mTabsAdapter);
		pager.setOnPageChangeListener(this);
		
		final ActionBar bar = getSupportActionBar();
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);

		bar.addTab(bar.newTab().setCustomView(
				createTabCustomView(R.drawable.ic_myfoot)).setTabListener(new TabListener()));
		bar.addTab(bar.newTab().setCustomView(
				createTabCustomView(R.drawable.ic_myplan)).setTabListener(new TabListener()));
		bar.addTab(bar.newTab().setCustomView(
				createTabCustomView(R.drawable.ic_summary)).setTabListener(new TabListener()));
		bar.addTab(bar.newTab().setCustomView(
				createTabCustomView(R.drawable.ic_morefeatures)).setTabListener(new TabListener()));
		
		checkSDcard();
	}
	
	private boolean checkSDcard() {
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = spf.edit();
		editor.commit();
		return false;
	}

	@Override
	public File getDatabasePath(String name) {
		String dbDir;
		File externalStorage = Environment.getExternalStorageDirectory();
		if(!externalStorage.exists()){
			boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
			if(!sdExist)
				Log.e("SD", "SD");
			dbDir = Environment.getDataDirectory().getAbsolutePath();
		}else{
			dbDir = externalStorage.getAbsolutePath();
		}
		dbDir += "/SuiXinJi";
		String dbPath = dbDir + "/" + name;
		Log.i("ss", dbPath);
		File dirFile = new File(dbDir);
		if (!dirFile.exists())
			if(!dirFile.mkdirs() && !dirFile.isDirectory()){
				Log.e("IOException", "" + dirFile.getAbsolutePath() + "");
				File file = getDir(name, Context.MODE_PRIVATE);
				dbPath = file.getPath() + "/" + name;
			}
		
		boolean isFileCreateSuccess = false;
		File dbFile = new File(dbPath);
		if (!dbFile.exists()) {
			try {
				isFileCreateSuccess = dbFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			isFileCreateSuccess = true;
        
        //������ݿ��ļ�����
        if(isFileCreateSuccess)
            return dbFile;
        else 
            return super.getDatabasePath(name);
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
	}
	
//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.loading, menu);
//        SearchView searchView = (SearchView) MenuItemCompat
//                .getActionView(menu.findItem(R.id.action_search));
//        searchView.setOnQueryTextListener(mOnQueryTextListener);
//        return true;
//    }

	int mSortMode = -1;
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (mSortMode != -1) {
//            Drawable icon = menu.findItem(mSortMode).getIcon();
//            menu.findItem(R.id.action_sort).setIcon(icon);
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }
    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//    	switch (item.getItemId()) {
//		case R.id.action_add:
//			startActivity(new Intent(this, NewRecordActivity.class));
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//    }
    
    public void onSort(MenuItem item) {
        mSortMode = item.getItemId();
        supportInvalidateOptionsMenu();
    }
	
	private View createTabCustomView(int icon){
		ImageView imageView = new ImageView(this);
		imageView.setPadding(2, 5, 2, 20);
		imageView.setImageResource(icon);
		return imageView;
	}
	
	TextView mSearchText;
	private final SearchView.OnQueryTextListener mOnQueryTextListener =
            new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String newText) {
            newText = TextUtils.isEmpty(newText) ? "" : "Query so far: " + newText;
            mSearchText.setText(newText);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Toast.makeText(MainActivity.this,
                    "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
            return true;
        }
    };
	
	private class TabListener implements ActionBar.TabListener {

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
    			pager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(MainActivity.this, "Reselected!", Toast.LENGTH_SHORT).show();
        }

    }
	
	private class TabsAdapter extends FragmentPagerAdapter{
		private Fragment[] fragments = {
				new RecordsFragment(),
				new PlansFragment(),
				new IdealFragment(),
				new SummaryFragment()
		};

		public TabsAdapter() {
			super(getSupportFragmentManager());
		}

		@Override
		public Fragment getItem(int position) {
			return fragments[position];
		}

		@Override
		public int getCount() {
			return fragments.length;
		}
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		final ActionBar bar = getSupportActionBar();
		bar.selectTab(bar.getTabAt(position));
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}
}
