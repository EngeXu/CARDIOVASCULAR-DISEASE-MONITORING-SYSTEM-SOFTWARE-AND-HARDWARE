package com.BBW.Myprj;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Introduction_activity extends Activity{
	private TextView Introduction_text;//定义使用方法显示控件
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro11);// 设置要显示的布局文件
		Introduction_text = (TextView) findViewById(R.id.SecondTextView);
		Introduction_text.setText(R.string.first_content);// 此处写入使用方法
		Introduction_text.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	
}
