package com.BBW.Myprj;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
//	private TextView mTextView01;//定义标题显示控件
//	private TextView mTextView02;//定义使用方法显示控件
	private Button Introduction = null;//使用方法键
	private Button login = null;//信息输入键
	private Button health_parameter = null;//使用方法键
	private Button sub_health = null;//信息输入键	
	private Button Intensive_care= null;//在现监护键
	private Button Self_help= null;//自救键	
	private Button Quit = null;//退出键
	
	private String Name="张三";
	private String Gender="男";
	private String Age="岁";	
	private String Height="cm";//得到电话号码1
	private String Weight="kg";//得到电话号码2
	private String PhoneNumber1="120";//得到电话号码1
	private String PhoneNumber2="120";//得到电话号码2
	private static TextReader_Write textReader_Write= new TextReader_Write();//调用TextReader_Write方法	
	ArrayList<String> DATA = new ArrayList<String>();//存储基本信息	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// 设置要显示的布局文件

//		mTextView01 = (TextView) findViewById(R.id.FirstTextView);
//		mTextView01.setText(R.string.first_title);// 此处写入标题


//		mTextView02 = (TextView) findViewById(R.id.SecondTextView);
//		mTextView02.setText(R.string.first_content);// 此处写入使用方法
//		mTextView02.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		
		Introduction = (Button) findViewById(R.id.Introduction);
		Introduction.setOnClickListener(new MyButtonListener());
		
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new MyButtonListener());

		health_parameter  = (Button) findViewById(R.id.health_parameter );
		health_parameter .setOnClickListener(new MyButtonListener());		
		
		sub_health  = (Button) findViewById(R.id.sub_health );
		sub_health.setOnClickListener(new MyButtonListener());		
		
		Intensive_care = (Button) findViewById(R.id.Intensive_care);
		Intensive_care.setOnClickListener(new MyButtonListener());		

		Self_help = (Button) findViewById(R.id.Self_help);
		Self_help.setOnClickListener(new MyButtonListener());			
		
		Quit = (Button) findViewById(R.id.Quit);
		Quit.setOnClickListener(new MyButtonListener());
		
		DATA.clear();//清除临时变量
		readerdata();	
	}
	public void onResume(){
		readerdata();
		super.onResume();
	}
	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {//按确定按钮，进入second_activity
			if(arg0 == Introduction) {
				Intent intent0 = new Intent();
				intent0.setClass(MainActivity.this, Introduction_activity.class);
				MainActivity.this.startActivity(intent0);}
			else if (arg0 ==login){
				Intent intent1 = new Intent();
				intent1.setClass(MainActivity.this, second_activity.class);
				MainActivity.this.startActivity(intent1);}
			else if (arg0 ==health_parameter){

				new AlertDialog.Builder(MainActivity.this)
			      /*弹出窗口的最上头文字*/
			      .setTitle("健康参数计算")
			      /*设置弹出窗口的图式*/
			      .setIcon(R.drawable.bk2) 
			      /*设置弹出窗口的信息*/
			      .setMessage("亲,请保证您已经输入个人信息，如果没有，请返回主界面输入。")
			      .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
			      		{
			    	  		public void onClick(DialogInterface dialoginterface, int i)
			    	  		{           
			    				Intent intent2 = new Intent();
			    				intent2.putExtra("Gender", Gender);
			    				intent2.putExtra("Age", Age);				
			    				intent2.putExtra("Height", Height );
			    				intent2.putExtra("Weight", Weight);
			    				intent2.setClass(MainActivity.this, forth_activity_healprara.class);
			    				MainActivity.this.startActivity(intent2);
			    	  		}
			      		}
			    		  			)
			      /*设置弹出窗口的返回事件*/
			      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
			      {
			    	  public void onClick(DialogInterface dialoginterface, int i)   {}
			      					}).show();	
									}
			else if (arg0 ==sub_health){
			      new AlertDialog.Builder(MainActivity.this)
			      /*弹出窗口的最上头文字*/
			      .setTitle(R.string.sub_health)
			      /*设置弹出窗口的图式*/
			      .setIcon(R.drawable.bk2) 
			      /*设置弹出窗口的信息*/
			      .setMessage(R.string.sub_health_introduce)
			      .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
			      		{
			    	  		public void onClick(DialogInterface dialoginterface, int i)
			    	  		{           
			    				
			    				Intent intent3 = new Intent();
			    				intent3.setClass(MainActivity.this, Fifthactivity_subhealthmain.class);
			    				MainActivity.this.startActivity(intent3);
			    	  		}
			      		}
			    		  			)
			      /*设置弹出窗口的返回事件*/
			      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
			      {
			    	  public void onClick(DialogInterface dialoginterface, int i)   {}
			      					}).show();				

									}			
			else if (arg0 ==Intensive_care){
				Intent intent4 = new Intent();
				intent4.putExtra("Name", Name);///不同activity之间 传递 键值对
				intent4.putExtra("PhoneNumber1", PhoneNumber1 );///不同activity之间 传递 键值对
				intent4.putExtra("PhoneNumber2", PhoneNumber2 );				
				intent4.setClass(MainActivity.this, third_activity.class);
				MainActivity.this.startActivity(intent4);}
			else if (arg0 ==Self_help){
				Intent intent5 = new Intent();
				intent5.putExtra("Name", Name);///不同activity之间 传递 键值对
				intent5.putExtra("PhoneNumber1", PhoneNumber1 );///不同activity之间 传递 键值对
				intent5.putExtra("PhoneNumber2", PhoneNumber2 );
				intent5.setClass(MainActivity.this, Alert_activity.class);
				MainActivity.this.startActivity(intent5);}				
			
		    else if (arg0 ==Quit){
			      new AlertDialog.Builder(MainActivity.this)
			      /*弹出窗口的最上头文字*/
			      .setTitle(R.string.quit_title)
			      /*设置弹出窗口的图式*/
			      .setIcon(R.drawable.bk2) 
			      /*设置弹出窗口的信息*/
			      .setMessage(R.string.quit_message)
			      .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
			      		{
			    	  		public void onClick(DialogInterface dialoginterface, int i)
			    	  		{           
			    	  			finish();/*关闭窗口*/
			    	  		}
			      		}
			    		  			)
			      /*设置弹出窗口的返回事件*/
			      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
			      {
			    	  public void onClick(DialogInterface dialoginterface, int i)   {}
			      					}).show();
		    	   				}
			
											}

												}
	public void readerdata(){
		DATA=textReader_Write.Read(MainActivity.this,"inf.txt");//从存储器中读取信息给临时变量
		if(DATA.get(0)!="false"){//如果第一次登陆，没有信息，则输入默认信息，并存入内存，供输入者参考
			Name=DATA.get(0);
			Gender=DATA.get(1);
			Age=DATA.get(2);
			Height=DATA.get(3);
			Weight=DATA.get(4);
			PhoneNumber1=DATA.get(5);
			PhoneNumber2=DATA.get(6);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	  												}

}
