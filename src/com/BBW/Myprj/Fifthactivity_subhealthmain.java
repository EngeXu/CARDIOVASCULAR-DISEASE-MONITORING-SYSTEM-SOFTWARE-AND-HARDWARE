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
	private Button subhealth = null;///����"�ǽ�������"���� ����
	private Button chinese_med = null;///����"��ҽ��������"���� ����
	private TextView Sub_result = null;///��ʾ�ǽ����������
	private TextView Chinesemed_result = null;///��ʾ��ҽ�����������
	public static String sub_content="@!@��,����\"�ǽ�������\"��,��ʼ�������彡���̶�����";//�ǽ��������������
	public static String Chi_content="@!@��,����\"��ҽ��������\"��,��ʼ������������";	//���������������
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subhealth_main);// ����Ҫ��ʾ�Ĳ����ļ�
		subhealth = (Button) findViewById(R.id.sub_health);
		subhealth.setOnClickListener(new MyButtonListener());												
	
		chinese_med= (Button) findViewById(R.id.chinese_med);
		chinese_med.setOnClickListener(new MyButtonListener());	
		
		Sub_result= (TextView) findViewById(R.id.sub_result);
		Sub_result.setText(sub_content);
		Sub_result.setMovementMethod(ScrollingMovementMethod.getInstance());///����Ϊ��̬�ı���
		
		Chinesemed_result= (TextView) findViewById(R.id.chinesemed_result);
		Chinesemed_result.setText(Chi_content);
		Chinesemed_result.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	public void onResume(){
		Sub_result.setText(sub_content);///�������ñ���
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
		public void onClick(View arg0) {//��ȷ����ť������second_activity
			if(arg0 == subhealth) { ////���ѡ��"�ǽ�������",�����ǽ�����������
				Intent intent1 = new Intent();
				intent1.putExtra("style", "1");///���ݼ�ֵ��,"1"��ʾ�����ǽ���������	
				intent1.setClass(Fifthactivity_subhealthmain.this, fifth_activity_subhealth.class);
				Fifthactivity_subhealthmain.this.startActivity(intent1);
			}
			else if (arg0 == chinese_med){////���ѡ��"��ҽ��������",�����Ӧ����
				Intent intent2 = new Intent();
				intent2.putExtra("style", "2");///���ݼ�ֵ��,"2"��ʾ��ҽ����������				
				intent2.setClass(Fifthactivity_subhealthmain.this, fifth_activity_subhealth.class);
				Fifthactivity_subhealthmain.this.startActivity(intent2);
				
			}
		}
	}

}
