package com.BBW.Myprj;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Introduction_activity extends Activity{
	private TextView Introduction_text;//����ʹ�÷�����ʾ�ؼ�
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro11);// ����Ҫ��ʾ�Ĳ����ļ�
		Introduction_text = (TextView) findViewById(R.id.SecondTextView);
		Introduction_text.setText(R.string.first_content);// �˴�д��ʹ�÷���
		Introduction_text.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	
}
