package com.BBW.Myprj;

//import android.app.PendingIntent;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
//import android.telephony.SmsManager;

public class second_activity extends Activity {
	// private TextView textView = null;
	private EditText name = null;
	private EditText age = null;

	
	private static final String[] m = { "男", "女" };
	private Spinner gender;
	private ArrayAdapter<String> adapter;
	
	private EditText height = null;
	private EditText weight = null;
	public EditText phone_number1 = null;
	public EditText phone_number2 = null;
	
	
	private Button okbutton = null;
	private String Gender = null;//公共变量，用于接受Gender的输入 
	private static TextReader_Write textReader_Write= new TextReader_Write();//调用TextReader_Write方法
	ArrayList<String> DATA = new ArrayList<String>();//存储基本信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login12);// 设置要显示的布局文件
		name = (EditText) findViewById(R.id.name_input);
		age = (EditText) findViewById(R.id.age_input);
		gender = (Spinner) findViewById(R.id.genderinput);
		height = (EditText) findViewById(R.id.heightinput);
		weight = (EditText) findViewById(R.id.weightinput);
		phone_number1 = (EditText) findViewById(R.id.ph_numberinput1);
		phone_number2 = (EditText) findViewById(R.id.ph_numberinput2);
		okbutton = (Button) findViewById(R.id.SecA_FirstButton);
		
		DATA.clear();//清除临时变量
		DATA=textReader_Write.Read(second_activity.this,"inf.txt");//从存储器中读取信息给临时变量
		if(DATA.get(0)=="false"){//如果第一次登陆，没有信息，则输入默认信息，并存入内存，供输入者参考
		DATA.add(0,getString(R.string.exp_name));
		DATA.add(1,getString(R.string.exp_gender_boy));	
		DATA.add(2,getString(R.string.exp_age));
		DATA.add(3,getString(R.string.exp_height));
		DATA.add(4,getString(R.string.exp_weight));
		DATA.add(5,getString(R.string.phonenumber1));
		DATA.add(6,getString(R.string.phonenumber2));

		textReader_Write.save(second_activity.this, "inf.txt",DATA);//将信息写入内存
		}		

		
		name.setHint(DATA.get(0));//以hint的形式给输入控件显示输入格式
		age.setHint(DATA.get(2));
		height.setHint(DATA.get(3));
		weight.setHint(DATA.get(4));
		phone_number1.setHint(DATA.get(5));
		phone_number2.setHint(DATA.get(6));
	
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, m);//性别下拉框
		adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
		gender.setAdapter(adapter);
		gender.setOnItemSelectedListener(new SpinnerSelectedListener());
		
		okbutton.setOnClickListener(new MyButtonListener());

	}

	public class SpinnerSelectedListener implements OnItemSelectedListener {
	 	//点击性别下拉框，获取性别输入
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			Gender= m[position];
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}

	//点home键，出现菜单
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, 1, 1, R.string.quit);
//		menu.add(0, 2, 2, R.string.help);
//		return super.onCreateOptionsMenu(menu);
//	}
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == 1) {
//			finish();
//		} else {
//
//		}
//		return super.onOptionsItemSelected(item);
//	}//点home键，出现菜单

	//点击”确定“键，存储输入信息
	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {

			String Name = name.getText().toString();	//输入内容转换为字符串	
			String Age = age.getText().toString();
			String Height = height.getText().toString();
			String Weight = weight.getText().toString();
			String PhoneNumber1 = phone_number1.getText().toString();
			String PhoneNumber2 = phone_number2.getText().toString();
			DATA.clear();
			if(Name.equals("")){//是否为空输入，若为空，读取hint内容
				DATA.add(name.getHint().toString());
			}
			else{
				DATA.add(Name);//否则，读入输入内容
			}
			DATA.add(Gender);
			if(Age.equals("")){
				DATA.add(age.getHint().toString());
			}
			else{
				DATA.add(Age);
			}
			if (Height.equals("")){
				DATA.add(height.getHint().toString());
			}
			else{
				DATA.add(Height);
			}
			 if (Weight.equals("")){
				 DATA.add(weight.getHint().toString());
			}else{
				DATA.add(Weight);	
			}
			if(PhoneNumber1.equals("")){
				DATA.add(phone_number1.getHint().toString());

			}else{
				DATA.add(PhoneNumber1);
			}
			if(PhoneNumber2.equals("")){
				DATA.add(phone_number2.getHint().toString());
			}else{

				DATA.add(PhoneNumber2);
			}


			textReader_Write.save(second_activity.this, "inf.txt",DATA);//存储信息
			//发送短信
//			Intent intent = new Intent();//启动third_activity
//			intent.putExtra("Name", DATA.get(0));///传递名字
//			intent.putExtra("Gender", DATA.get(1));///传递名字
//			intent.putExtra("Age", DATA.get(2));///传递名字
//			intent.putExtra("Height", DATA.get(3));///传递名字
//			intent.putExtra("Weight", DATA.get(4));///传递名字			
//			intent.putExtra("PhoneNumber1", DATA.get(5));///传递电话号码
//			intent.putExtra("PhoneNumber2", DATA.get(6));///传递电话号码
//			intent.setClass(second_activity.this, MainActivity.class);
//			second_activity.this.startActivity(intent);	
			second_activity.this.finish();
			
		}

	}

}
