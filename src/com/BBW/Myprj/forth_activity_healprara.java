package com.BBW.Myprj;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class forth_activity_healprara extends Activity{
	
	private String Gender;//字符串类型
	private double  age;//双精度类型	
	private double  height;
	private double  weight;
	private double  bmi;

	private String Age;//字符串类型
	private String Height;
	private String Weight;
	
	private Button BMI = null;//BMI键
	private Button BMR = null;//BMR键
	private Button BurnPB = null;//燃脂心率键
	private Button Noise = null;//环境噪声键

	private TextView Result_Para;//显示结果
	private TextView Intro_Para;//参数介绍
	
	private AudioRecord ar;//开启麦克
	private int bs;
	private static final int SAMPLE_RATE_IN_HZ = 44100;//定义采样频率
	public boolean isRun = false; //控制是否采样
    
//    private String Noise_content;
	 private Handler handler = new Handler(){///发送消息线程
	  @Override
	  public void handleMessage(Message msg) {
	   super.handleMessage(msg);
	   switch(msg.what){
	   case 1:
		   Result_Para.setText(msg.obj.toString());
	    break;
	   default:
	    break;
	   }
	  }
	 };
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthparameter13);// 设置要显示的布局文件
		
		BMI = (Button) findViewById(R.id.BMI);
		BMI.setOnClickListener(new MyButtonListener());											

		BMR = (Button) findViewById(R.id.BMR);
		BMR.setOnClickListener(new MyButtonListener());	
		
		BurnPB  = (Button) findViewById(R.id.burnBP);
		BurnPB .setOnClickListener(new MyButtonListener());	
		
		Noise   = (Button) findViewById(R.id.Noise);
		Noise  .setOnClickListener(new MyButtonListener());	
		
		Result_Para   = (TextView) findViewById(R.id.para_result);		
		Intro_Para  = (TextView) findViewById(R.id.para_intro);
		
		Intent intent = getIntent();// 得到传递的 数据
		Gender = intent.getStringExtra("Gender");
		 Age = intent.getStringExtra("Age");
		 Height = intent.getStringExtra("Height");
		 Weight = intent.getStringExtra("Weight");



	}
	class MainThread extends Thread {
		  public void run() {
		   Looper.prepare();
		   //初始化麦克
		    bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
		    AudioFormat.CHANNEL_CONFIGURATION_MONO,
		    AudioFormat.ENCODING_PCM_16BIT);
		    ar = new AudioRecord(MediaRecorder.AudioSource.MIC,
		    SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO,
		    AudioFormat.ENCODING_PCM_16BIT, bs);
		    ar.startRecording();//开始记录
		    isRun = true;
		    while (isRun) {
		     short[] buffer = new short[bs];
		     int r=ar.read(buffer, 0, bs);
		     int v = 0;
		     for (int i = 0; i < buffer.length; i++) {
		      v += buffer[i]*buffer[i];
		     }
		     Message msg = new Message();//创建并发送计算的噪声大小
		     msg.what=1;
		     msg.obj="目前环境噪声为:"+(int)Math.round(10*Math.log10(v/r))+"dB;";
		     handler.sendMessage(msg);
		     try {
		      Thread.sleep(500);
		     } catch (InterruptedException e) {
		      e.printStackTrace();
		     }
		    }
		    ar.stop();
		    Looper.loop();
		   }
		 }
		 @Override
		 ////安返回按键执行内容
		 public void onBackPressed() {
		  super.onBackPressed();
		  if(isRun){///如果是ture为测噪过程,退出需要关闭,否则不用
		  isRun = false;
		  ar.release();
		  System.exit(0);}
		 }
		 @Override
		 public boolean onCreateOptionsMenu(Menu menu) {
		  getMenuInflater().inflate(R.menu.main, menu);
		  return true;
		 }
	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			 DecimalFormat nf = new DecimalFormat("0.000");//定义小数点位数
			 String text="";
			if(arg0 == BMI) {
				if(isRun){///停止采集声音
					  isRun = false;
				}
				if (Weight.equals("kg")){///判断是否有信息输入
					text="请返回主界面,输入个人信息!";
				}
				else{
				height = Double.parseDouble(Height )/100;
				weight  = Double.parseDouble(Weight );				
				bmi=weight/(height*height);//计算BMI指数
				text="BMI="+nf.format(bmi)+";";
				if (bmi<18.5){//判断体型
					text+="偏轻";
				}
				else if(bmi>=18.5 && bmi<=23.9){
					text+="正常";
				}
				else if(bmi>23.9 && bmi<27.9){
					text+="偏胖";
				}
				else{
					text+="肥胖";
				}
				double std_weight=0;///根据身高,计算标准体重和正常体重范围
				double high_weight=0;
				double low_weight=0;
				if(Gender.equals("男")){///性别不同,方法不同
					std_weight=height*height*22;
					high_weight=std_weight+std_weight*0.1;
					low_weight=std_weight-std_weight*0.1;
				}
				else {
					std_weight=height*height*20;
					high_weight=std_weight+std_weight*0.1;
					low_weight=std_weight-std_weight*0.1;
				}
				///显示结果
				Intro_Para.setText("您的标准体重为："+nf.format(std_weight)+"kg.\n您的正常体重范围："+nf.format(high_weight)+"kg"+"~"+nf.format(low_weight)+"kg"+"\nBMI指数,即身体质量指数，简称体质指数,是目前国内外常用的衡量人体胖瘦程度以及是否健康的一个标准，最理想的体重指数是22。\n您达标了吗？");
				}
				Result_Para.setText(text);
				}
			else if (arg0 ==BMR){
				if(isRun){
					  isRun = false;
				}
				///没有计算
				text="亲！对不起啊！";
				Result_Para.setText(text);
				Intro_Para.setText("基础代谢率（简称BMR）是指：我们在安静状态下(通常为静卧状态)消耗的最低热量，人的其他活动都建立在这个基础上。本来要算得，但算出来你也不懂，我就偷个懒，哈哈。。。");
				}
			else if (arg0 ==BurnPB){
				if(isRun){
					  isRun = false;
				}
				if (Age.equals("岁")){
					text="请返回主界面,输入个人信息!";
				}
				else{
				double maxpb=0;
				double high_pb=0;
				double low_pb=0;
				age=Double.parseDouble(Age);
				maxpb=220-age;
				high_pb=maxpb*0.8;
				low_pb=maxpb*0.6;
				text="您最大燃脂心率为："+nf.format(maxpb)+"次/分;\n"+"中低度运动心率为"+nf.format(low_pb)+"~"+nf.format(high_pb)+"次/分；\n";
				Intro_Para.setText("燃脂心率是指让运动强度达到燃烧脂肪时的心率，明确自己的燃脂心率是多少可以有效控制运动强度，达到燃烧脂肪的效果，即为燃脂运动。低于或者高于燃脂心率，都不算燃脂运动，燃烧的也就不是脂肪了，每人的燃脂心率因人而异。");
				}
				Result_Para.setText(text);
									}
			else if (arg0 ==Noise){

				MainThread mt = new MainThread();//定义线程,开始录音
				mt.start();
				Intro_Para.setText("凡是妨碍人们正常休息、学习和工作的声音，以及对人们要听的声音产生干扰的声音。从这个意义上来说，噪音的来源很多，如，街道上的汽车声、安静的图书馆里的说话声、建筑工地的机器声、以及邻居电视机过大的声音，都是噪声。\n具体范围为：\n0 －20 分贝： 很静、几乎感觉不到；\n20 －40 分贝： 安静、犹如轻声絮语；\n40 －60 分贝 ：一般、普通室内谈话；\n60 －70 分贝： 吵闹、有损神经；\n70 －90 分贝 ：很吵、神经细胞受到破坏。\n90 －100 分贝： 吵闹加剧、听力受损；\n100 －120 分贝： 难以忍受、呆一分钟即暂时致聋；\n120分贝以上： 极度聋或全聋。");
				
			}
		}
	}
}
	