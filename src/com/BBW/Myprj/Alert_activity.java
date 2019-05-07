package com.BBW.Myprj;

import java.io.IOException;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class Alert_activity extends Activity{
	public SendMSG senMSG;//初始化发送短信类	
	private TextView alert_content;//显示报警文字
	private Vibrator mVibrator01;//震动
	private MediaPlayer mMediaPlayer; //音乐播放
	Camera cam = Camera.open(); //打开相机
	public String PhoneNumber1;//得到电话号码1
	public String PhoneNumber2;//得到电话号码2

	
	/////////定位初始化////////////
	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 60000;
	
@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert16);
		
		Intent intent = getIntent();// 得到传递的 数据
		final String Name = intent.getStringExtra("Name");//获取姓名
		PhoneNumber1 = intent.getStringExtra("PhoneNumber1");//获取第一个电话号码
		PhoneNumber2 = intent.getStringExtra("PhoneNumber2");
		
		alert_content = (TextView)findViewById(R.id.alert);
		
		mVibrator01 = ( Vibrator )getApplication().getSystemService( Service .VIBRATOR_SERVICE ); //震动	
		alert_content.setText(getString(R.string.helpcontent)+"\n"+PhoneNumber1+"\n"+"或者"+"\n"+PhoneNumber2);//写入报警内容
		/////////定位////////////
		locationClient = new LocationClient(this);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setAddrType("all");
        option.setOpenGps(true);		//是否打开GPS
        option.setCoorType("bd09ll");		//设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst);	//设置定位优先级
        option.setProdName("LocationDemo");	//设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME);//设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);
        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					Toast.makeText(Alert_activity.this,"请连接网络" ,Toast.LENGTH_LONG).show();
					return;		
				}	
				//Toast获得的地址
				Toast.makeText(Alert_activity.this, location.getAddrStr(),Toast.LENGTH_LONG).show();
				//短信内容
				String place ="\n!求救!"+"\n监护对象"+Name+"正处于危险之中,请及时救助!!!"+"时间:"+location.getTime()+",位置:"+location.getAddrStr()+"(纬度:"+location.getLatitude()+",经度:"+location.getLongitude()+")";
				senMSG = new SendMSG(Alert_activity.this);//获取短信权限		
				senMSG.send(place,PhoneNumber1 );//给第一个电话发送短信
				senMSG.send(place,PhoneNumber2 );	
			}
			public void onReceivePoi(BDLocation location) {}		
		});
	}

	public void onStart(){
		super.onStart();
		mVibrator01.vibrate( new long[]{100,100,100,1000},0);////开始震动报警	
		playLocalFile();//开始播放警报声
		//开始定位
		if (locationClient == null) {
			return;
		}
		if (locationClient.isStarted()) {
			locationClient.stop();
		}else {
			locationClient.start();
			locationClient.requestLocation();
		}
		
	}

	public void onResume(){
		//定义一个Handler，启动光报警
		handler.post(updataThread);
		super.onResume();
	}

	public void onDestroy(){
		super.onDestroy();
		mVibrator01.cancel(); //震动结束
		mMediaPlayer.stop();  //停止声报警
		camera_ctl(false);//停止光报警
		handler.removeCallbacks(updataThread);//移除报警和求救线程
//		finish();
		//停止定位
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
		android.os.Process.killProcess(android.os.Process.myPid());////当点击返回时,立即停止所有报警措施.否则不能开启下次报警.
	}
	
	//采用Handler实现闪光灯变换
	Handler handler = new Handler();
	Runnable updataThread = new Runnable() {
	int i =0;	//控制背景变化和拨打求救电话
		@Override
		public void run() {
			// TODO Auto-generated method stub
			i++;
			if (i%2==0){
			camera_ctl(true);//开启闪光灯
			alert_content.setBackgroundColor(Color.WHITE);}//屏幕背景变白
			else{camera_ctl(false);//关闭闪光灯
			alert_content.setBackgroundColor(Color.BLACK);}//屏幕背景变黑
			if(i%20==0){
				call();//开启电话
				handler.postDelayed(updataThread, 10000);//线程延时10s
			}else{
			handler.postDelayed(updataThread, 500);}//线程延时0.5s
			
		}
	};
	//开启闪光灯子程序
	private void camera_ctl(boolean model){
		String value;
		Camera.Parameters parameters = cam.getParameters();
		if (model==true){
			value = Camera.Parameters.FLASH_MODE_TORCH;}//开闪光灯
		else{
			value = Camera.Parameters.FLASH_MODE_OFF;//关闪光灯
		}
		parameters.setFlashMode(value);
		cam.setParameters(parameters);
	}
	private void playLocalFile() {        
		mMediaPlayer = MediaPlayer.create(this,R.raw.alarm); //导入警报声文件
		mMediaPlayer.setLooping(true);//设置为循环播放
		try {    
			mMediaPlayer.prepare();      //缓冲数据 
			}   catch (IllegalStateException e) 
			{                   
			}  catch (IOException e) { 
			}
			mMediaPlayer.start();        //开始播放
		}

	 private void call(){
		Intent Call = new Intent("android.intent.action.CALL",Uri.parse("tel:"+PhoneNumber1));//给第一个号码打电话
		startActivity(Call);
		 
	 }
	

}

	
