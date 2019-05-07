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
//	private TextView mTextView01;//���������ʾ�ؼ�
//	private TextView mTextView02;//����ʹ�÷�����ʾ�ؼ�
	private Button Introduction = null;//ʹ�÷�����
	private Button login = null;//��Ϣ�����
	private Button health_parameter = null;//ʹ�÷�����
	private Button sub_health = null;//��Ϣ�����	
	private Button Intensive_care= null;//���ּ໤��
	private Button Self_help= null;//�Ծȼ�	
	private Button Quit = null;//�˳���
	
	private String Name="����";
	private String Gender="��";
	private String Age="��";	
	private String Height="cm";//�õ��绰����1
	private String Weight="kg";//�õ��绰����2
	private String PhoneNumber1="120";//�õ��绰����1
	private String PhoneNumber2="120";//�õ��绰����2
	private static TextReader_Write textReader_Write= new TextReader_Write();//����TextReader_Write����	
	ArrayList<String> DATA = new ArrayList<String>();//�洢������Ϣ	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// ����Ҫ��ʾ�Ĳ����ļ�

//		mTextView01 = (TextView) findViewById(R.id.FirstTextView);
//		mTextView01.setText(R.string.first_title);// �˴�д�����


//		mTextView02 = (TextView) findViewById(R.id.SecondTextView);
//		mTextView02.setText(R.string.first_content);// �˴�д��ʹ�÷���
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
		
		DATA.clear();//�����ʱ����
		readerdata();	
	}
	public void onResume(){
		readerdata();
		super.onResume();
	}
	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {//��ȷ����ť������second_activity
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
			      /*�������ڵ�����ͷ����*/
			      .setTitle("������������")
			      /*���õ������ڵ�ͼʽ*/
			      .setIcon(R.drawable.bk2) 
			      /*���õ������ڵ���Ϣ*/
			      .setMessage("��,�뱣֤���Ѿ����������Ϣ�����û�У��뷵�����������롣")
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
			      /*���õ������ڵķ����¼�*/
			      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
			      {
			    	  public void onClick(DialogInterface dialoginterface, int i)   {}
			      					}).show();	
									}
			else if (arg0 ==sub_health){
			      new AlertDialog.Builder(MainActivity.this)
			      /*�������ڵ�����ͷ����*/
			      .setTitle(R.string.sub_health)
			      /*���õ������ڵ�ͼʽ*/
			      .setIcon(R.drawable.bk2) 
			      /*���õ������ڵ���Ϣ*/
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
			      /*���õ������ڵķ����¼�*/
			      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
			      {
			    	  public void onClick(DialogInterface dialoginterface, int i)   {}
			      					}).show();				

									}			
			else if (arg0 ==Intensive_care){
				Intent intent4 = new Intent();
				intent4.putExtra("Name", Name);///��ͬactivity֮�� ���� ��ֵ��
				intent4.putExtra("PhoneNumber1", PhoneNumber1 );///��ͬactivity֮�� ���� ��ֵ��
				intent4.putExtra("PhoneNumber2", PhoneNumber2 );				
				intent4.setClass(MainActivity.this, third_activity.class);
				MainActivity.this.startActivity(intent4);}
			else if (arg0 ==Self_help){
				Intent intent5 = new Intent();
				intent5.putExtra("Name", Name);///��ͬactivity֮�� ���� ��ֵ��
				intent5.putExtra("PhoneNumber1", PhoneNumber1 );///��ͬactivity֮�� ���� ��ֵ��
				intent5.putExtra("PhoneNumber2", PhoneNumber2 );
				intent5.setClass(MainActivity.this, Alert_activity.class);
				MainActivity.this.startActivity(intent5);}				
			
		    else if (arg0 ==Quit){
			      new AlertDialog.Builder(MainActivity.this)
			      /*�������ڵ�����ͷ����*/
			      .setTitle(R.string.quit_title)
			      /*���õ������ڵ�ͼʽ*/
			      .setIcon(R.drawable.bk2) 
			      /*���õ������ڵ���Ϣ*/
			      .setMessage(R.string.quit_message)
			      .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
			      		{
			    	  		public void onClick(DialogInterface dialoginterface, int i)
			    	  		{           
			    	  			finish();/*�رմ���*/
			    	  		}
			      		}
			    		  			)
			      /*���õ������ڵķ����¼�*/
			      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
			      {
			    	  public void onClick(DialogInterface dialoginterface, int i)   {}
			      					}).show();
		    	   				}
			
											}

												}
	public void readerdata(){
		DATA=textReader_Write.Read(MainActivity.this,"inf.txt");//�Ӵ洢���ж�ȡ��Ϣ����ʱ����
		if(DATA.get(0)!="false"){//�����һ�ε�½��û����Ϣ��������Ĭ����Ϣ���������ڴ棬�������߲ο�
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
