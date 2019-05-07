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
	public SendMSG senMSG;//��ʼ�����Ͷ�����	
	private TextView alert_content;//��ʾ��������
	private Vibrator mVibrator01;//��
	private MediaPlayer mMediaPlayer; //���ֲ���
	Camera cam = Camera.open(); //�����
	public String PhoneNumber1;//�õ��绰����1
	public String PhoneNumber2;//�õ��绰����2

	
	/////////��λ��ʼ��////////////
	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 60000;
	
@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert16);
		
		Intent intent = getIntent();// �õ����ݵ� ����
		final String Name = intent.getStringExtra("Name");//��ȡ����
		PhoneNumber1 = intent.getStringExtra("PhoneNumber1");//��ȡ��һ���绰����
		PhoneNumber2 = intent.getStringExtra("PhoneNumber2");
		
		alert_content = (TextView)findViewById(R.id.alert);
		
		mVibrator01 = ( Vibrator )getApplication().getSystemService( Service .VIBRATOR_SERVICE ); //��	
		alert_content.setText(getString(R.string.helpcontent)+"\n"+PhoneNumber1+"\n"+"����"+"\n"+PhoneNumber2);//д�뱨������
		/////////��λ////////////
		locationClient = new LocationClient(this);
        //���ö�λ����
        LocationClientOption option = new LocationClientOption();
        option.setAddrType("all");
        option.setOpenGps(true);		//�Ƿ��GPS
        option.setCoorType("bd09ll");		//���÷���ֵ���������͡�
        option.setPriority(LocationClientOption.NetWorkFirst);	//���ö�λ���ȼ�
        option.setProdName("LocationDemo");	//���ò�Ʒ�����ơ�ǿ�ҽ�����ʹ���Զ���Ĳ�Ʒ�����ƣ����������Ժ�Ϊ���ṩ����Ч׼ȷ�Ķ�λ����
        option.setScanSpan(UPDATE_TIME);//���ö�ʱ��λ��ʱ��������λ����
        locationClient.setLocOption(option);
        //ע��λ�ü�����
        locationClient.registerLocationListener(new BDLocationListener() {
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					Toast.makeText(Alert_activity.this,"����������" ,Toast.LENGTH_LONG).show();
					return;		
				}	
				//Toast��õĵ�ַ
				Toast.makeText(Alert_activity.this, location.getAddrStr(),Toast.LENGTH_LONG).show();
				//��������
				String place ="\n!���!"+"\n�໤����"+Name+"������Σ��֮��,�뼰ʱ����!!!"+"ʱ��:"+location.getTime()+",λ��:"+location.getAddrStr()+"(γ��:"+location.getLatitude()+",����:"+location.getLongitude()+")";
				senMSG = new SendMSG(Alert_activity.this);//��ȡ����Ȩ��		
				senMSG.send(place,PhoneNumber1 );//����һ���绰���Ͷ���
				senMSG.send(place,PhoneNumber2 );	
			}
			public void onReceivePoi(BDLocation location) {}		
		});
	}

	public void onStart(){
		super.onStart();
		mVibrator01.vibrate( new long[]{100,100,100,1000},0);////��ʼ�𶯱���	
		playLocalFile();//��ʼ���ž�����
		//��ʼ��λ
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
		//����һ��Handler�������ⱨ��
		handler.post(updataThread);
		super.onResume();
	}

	public void onDestroy(){
		super.onDestroy();
		mVibrator01.cancel(); //�𶯽���
		mMediaPlayer.stop();  //ֹͣ������
		camera_ctl(false);//ֹͣ�ⱨ��
		handler.removeCallbacks(updataThread);//�Ƴ�����������߳�
//		finish();
		//ֹͣ��λ
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
		android.os.Process.killProcess(android.os.Process.myPid());////���������ʱ,����ֹͣ���б�����ʩ.�����ܿ����´α���.
	}
	
	//����Handlerʵ������Ʊ任
	Handler handler = new Handler();
	Runnable updataThread = new Runnable() {
	int i =0;	//���Ʊ����仯�Ͳ�����ȵ绰
		@Override
		public void run() {
			// TODO Auto-generated method stub
			i++;
			if (i%2==0){
			camera_ctl(true);//���������
			alert_content.setBackgroundColor(Color.WHITE);}//��Ļ�������
			else{camera_ctl(false);//�ر������
			alert_content.setBackgroundColor(Color.BLACK);}//��Ļ�������
			if(i%20==0){
				call();//�����绰
				handler.postDelayed(updataThread, 10000);//�߳���ʱ10s
			}else{
			handler.postDelayed(updataThread, 500);}//�߳���ʱ0.5s
			
		}
	};
	//����������ӳ���
	private void camera_ctl(boolean model){
		String value;
		Camera.Parameters parameters = cam.getParameters();
		if (model==true){
			value = Camera.Parameters.FLASH_MODE_TORCH;}//�������
		else{
			value = Camera.Parameters.FLASH_MODE_OFF;//�������
		}
		parameters.setFlashMode(value);
		cam.setParameters(parameters);
	}
	private void playLocalFile() {        
		mMediaPlayer = MediaPlayer.create(this,R.raw.alarm); //���뾯�����ļ�
		mMediaPlayer.setLooping(true);//����Ϊѭ������
		try {    
			mMediaPlayer.prepare();      //�������� 
			}   catch (IllegalStateException e) 
			{                   
			}  catch (IOException e) { 
			}
			mMediaPlayer.start();        //��ʼ����
		}

	 private void call(){
		Intent Call = new Intent("android.intent.action.CALL",Uri.parse("tel:"+PhoneNumber1));//����һ�������绰
		startActivity(Call);
		 
	 }
	

}

	
