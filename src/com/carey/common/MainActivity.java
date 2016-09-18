package com.carey.common;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.carey.config.StaticConfig;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	
	private String path = StaticConfig.path;	//应用程序用到的APK的文件目录
	private String APKnameB = StaticConfig.APKnameB;	//APK文件目录下的APK名
	private String APKnameC = StaticConfig.APKnameC;	//APK文件目录下的APK名
	private String APKnameD = StaticConfig.APKnameD;	//APK文件目录下的APK名
	
	//底部导航栏4个元素
	private TextView news;
	private TextView read;
	private TextView media;
	private TextView me;
	
	private ViewPager viewpager;
	
	private View newsview;
	private View readview;
	private View mediaview;
	private View meview;
	private List<View>viewlist = new ArrayList<View>();
	
	private Button startB;
	private Button startC;
	private Button startD;
	
	private LayoutInflater inflater;
	
	public AssetManager mAssetManager = null;
	public Resources mResources = null;
	public Theme mTheme = null;
	
	private int currPage=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		
		initViews();

	}

	public void initViews(){
		inflater=getLayoutInflater();
				
		news = (TextView)findViewById(R.id.news);
		read = (TextView)findViewById(R.id.read);
		media = (TextView)findViewById(R.id.media);
		me = (TextView)findViewById(R.id.me);		
		
		newsview = inflater.inflate(R.layout.newsview, null);
		readview = inflater.inflate(R.layout.readview, null);
		mediaview = inflater.inflate(R.layout.mediaview, null);
		meview = inflater.inflate(R.layout.meview, null);
		
		viewlist.add(newsview);
		viewlist.add(readview);
		viewlist.add(mediaview);
		viewlist.add(meview);

		//加载插件B
		startB = (Button)newsview.findViewById(R.id.startBbt);
		startB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle paramBundle = new Bundle();
				paramBundle.putBoolean("KEY_START_FROM_OTHER_ACTIVITY", true);//表示以插件的形式加载到别的activity
				
				//加入以下代码是因为指定路径无法readable或者writable
				String dexPathB = path + APKnameB;
				Context context = getApplicationContext();// 获取Context对象；
				File dexOutputpath = context.getDir("dex", MODE_PRIVATE);
						
				Log.e("tag", "path is:" + (path + APKnameB));
				LoadAPK(paramBundle, dexPathB, dexOutputpath);
			}
		});
		
		//加载插件C
		startC = (Button)readview.findViewById(R.id.startCbt);
		startC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle paramBundle = new Bundle();
				paramBundle.putBoolean("KEY_START_FROM_OTHER_ACTIVITY", true);//表示以查件的形式加载到别的activity
				
				//加入以下代码是因为指定路径无法readable或者writable
				String dexPathC = path + APKnameC;
				Context context = getApplicationContext();// 获取Context对象；
				File dexOutputpath = context.getDir("dex", MODE_PRIVATE);
						
				Log.e("tag", "path is:" + (path + APKnameC));
				//加载APKC
				LoadAPK(paramBundle, dexPathC, dexOutputpath);
			}
		});
		
		startD = (Button)mediaview.findViewById(R.id.startDbt);
		startD.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle paramBundle = new Bundle();
				paramBundle.putBoolean("KEY_START_FROM_OTHER_ACTIVITY", true);//表示以查件的形式加载到别的activity
				
				//加入以下代码是因为指定路径无法readable或者writable
				String dexPathD = path + APKnameD;
				Context context = getApplicationContext();// 获取Context对象；
				File dexOutputpath = context.getDir("dex", MODE_PRIVATE);
						
				Log.e("tag", "path is:" + (path + APKnameD));
				//先把APKD中的R资源导入到父Activity中，即本Activity
				loadResources(path+APKnameD);
				//加载APKD
				LoadAPK(paramBundle, dexPathD, dexOutputpath);
			}
		});
		
		//根据currPage设置哪一个底部界面标题为红色
		switch(currPage){
		case 0:
			news.setTextColor(Color.RED);
			break;
		case 1:
			read.setTextColor(Color.RED);
			break;
		case 2:
			media.setTextColor(Color.RED);
			break;
		case 3:
			me.setTextColor(Color.RED);
			break;
		}

		viewpager = (ViewPager)findViewById(R.id.viewager);
		PagerAdapter pageradapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return viewlist.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				// TODO Auto-generated method stub
				((ViewPager) container).removeView(viewlist.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				// TODO Auto-generated method stub
				((ViewPager) container).addView(viewlist.get(position),0);
				return viewlist.get(position);
			}
			
		};		
		viewpager.setAdapter(pageradapter);
		viewpager.setCurrentItem(currPage);
		
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch(currPage){
				case 0:
					news.setTextColor(Color.GRAY);
					break;
				case 1:
					read.setTextColor(Color.GRAY);
					break;
				case 2:
					media.setTextColor(Color.GRAY);
					break;
				case 3:
					me.setTextColor(Color.GRAY);
					break;
				}
				
				switch(arg0){
				case 0:
					news.setTextColor(Color.RED);
					break;
				case 1:
					read.setTextColor(Color.RED);
					break;
				case 2:
					media.setTextColor(Color.RED);
					break;
				case 3:
					me.setTextColor(Color.RED);
					break;
				}
				currPage = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		OnClickListener onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()){
				case R.id.news:
					viewpager.setCurrentItem(0);
					break;
				case R.id.read:
					viewpager.setCurrentItem(1);
					break;
				case R.id.media:
					viewpager.setCurrentItem(2);
					break;
				case R.id.me:
					viewpager.setCurrentItem(3);
					break;
				}
			}
		};
		news.setOnClickListener(onClickListener);
		read.setOnClickListener(onClickListener);
		media.setOnClickListener(onClickListener);
		me.setOnClickListener(onClickListener);

	}

	/**
	 * 加载APK插件
	 * @param paramBundle
	 * @param dexpath
	 * @param dexoutputpath
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void LoadAPK(Bundle paramBundle, String dexpath, File dexoutputpath) {
		
		DexClassLoader localDexClassLoader = new DexClassLoader(dexpath,
				dexoutputpath.getAbsolutePath(), null, getClassLoader());
						
		try {
			//APK插件的包信息
			PackageInfo plocalObject = getPackageManager().getPackageArchiveInfo(dexpath, 1);

			if ((plocalObject.activities != null)&& (plocalObject.activities.length > 0)) {
				//类名
				String activityname = plocalObject.activities[0].name;
				Log.d(TAG, "activityname = " + activityname);

				//对象名
				Class localClass = localDexClassLoader.loadClass(activityname);
				Constructor localConstructor = localClass.getConstructor(new Class[] {});
				Object instance = localConstructor.newInstance(new Object[] {});
				Log.d(TAG, "instance = " + instance);

				//调用ActivityB的setActivity方法
				Method localMethodSetActivity = localClass.getDeclaredMethod("setActivity", new Class[] { Activity.class });
				localMethodSetActivity.setAccessible(true);
				localMethodSetActivity.invoke(instance, new Object[] { this });

				//调用ActivityB的onCreate方法
				Method methodonCreate = localClass.getDeclaredMethod("onCreate", new Class[] { Bundle.class });
				methodonCreate.setAccessible(true);
				methodonCreate.invoke(instance, new Object[] { paramBundle });
			}
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	/*
	 * 传资源地址
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return path;
		//return super.toString();
	}

	/**
	 * 拦截返回按钮，防止直接从APK插件退出应用，防止资源文件混乱导致应用崩溃
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		//super.onBackPressed();
		
		if(viewpager.isShown()){
			super.onBackPressed();
		}else{
			//恢复主Activity的资源文件
			resetResources();
			
			//重新加载主Activity
			setContentView(R.layout.main);
			viewlist.clear();
			initViews();
		}
		
	}
	
	/**
	 * 重置父Activity资源文件
	 */
	public void resetResources(){
		mAssetManager = null;
		mResources = null;
		mTheme = null;
	}
	
	/**
	 * 把指定路径的APK文件的资源文件加载到本Activity中
	 * @param mDexPath
	 */
	protected void loadResources(String mDexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, mDexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }
	
	@Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }
	
	
}