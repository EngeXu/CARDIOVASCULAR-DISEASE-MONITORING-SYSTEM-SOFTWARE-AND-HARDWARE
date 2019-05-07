package com.BBW.Myprj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Fifthactivity_subhealthmain  extends Activity{
	private Button subhealth = null;///进入"亚健康评估"界面 按键
	private Button chinese_med = null;///进入"中医体质评估"界面 按键
	private TextView Sub_result = null;///显示亚健康评估结果
	private TextView Chinesemed_result = null;///显示中医体质评估结果
	public static String sub_content="@!@亲,请点击\"亚健康评估\"键,开始您的身体健康程度评估";//亚健康评估结果标题
	public static String Chi_content="@!@亲,请点击\"中医体质评判\"键,开始您的体质评估";	//体质评估结果标题
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subhealth_main);// 设置要显示的布局文件
		subhealth = (Button) findViewById(R.id.sub_health);
		subhealth.setOnClickListener(new MyButtonListener());												
	
		chinese_med= (Button) findViewById(R.id.chinese_med);
		chinese_med.setOnClickListener(new MyButtonListener());	
		
		Sub_result= (TextView) findViewById(R.id.sub_result);
		Sub_result.setText(sub_content);
		Sub_result.setMovementMethod(ScrollingMovementMethod.getInstance());///设置为动态文本框
		
		Chinesemed_result= (TextView) findViewById(R.id.chinesemed_result);
		Chinesemed_result.setText(Chi_content);
		Chinesemed_result.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	public void onResume(){
		Sub_result.setText(sub_content);///重新设置标题
		Chinesemed_result.setText(Chi_content);		
		super.onResume();
	}
	public void onRetart(){
		super.onRestart();
		Sub_result.setText(sub_content);
		Chinesemed_result.setText(Chi_content);	
		
	}
	public void onStart(){
		super.onStart();
		Sub_result.setText(sub_content);
		Chinesemed_result.setText(Chi_content);	
		
	}
	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {//按确定按钮，进入second_activity
			if(arg0 == subhealth) { ////如果选择"亚健康评估",进入亚健康评估界面
				Intent intent1 = new Intent();
				intent1.putExtra("style", "1");///传递键值对,"1"表示进行亚健康评估。	
				intent1.setClass(Fifthactivity_subhealthmain.this, fifth_activity_subhealth.class);
				Fifthactivity_subhealthmain.this.startActivity(intent1);
			}
			else if (arg0 == chinese_med){////如果选择"中医体质评估",进入对应界面
				Intent intent2 = new Intent();
				intent2.putExtra("style", "2");///传递键值对,"2"表示中医体质评估。				
				intent2.setClass(Fifthactivity_subhealthmain.this, fifth_activity_subhealth.class);
				Fifthactivity_subhealthmain.this.startActivity(intent2);
				
			}
		}
	}

}
