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
	
	private String Gender;//�ַ�������
	private double  age;//˫��������	
	private double  height;
	private double  weight;
	private double  bmi;

	private String Age;//�ַ�������
	private String Height;
	private String Weight;
	
	private Button BMI = null;//BMI��
	private Button BMR = null;//BMR��
	private Button BurnPB = null;//ȼ֬���ʼ�
	private Button Noise = null;//����������

	private TextView Result_Para;//��ʾ���
	private TextView Intro_Para;//��������
	
	private AudioRecord ar;//�������
	private int bs;
	private static final int SAMPLE_RATE_IN_HZ = 44100;//�������Ƶ��
	public boolean isRun = false; //�����Ƿ����
    
//    private String Noise_content;
	 private Handler handler = new Handler(){///������Ϣ�߳�
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
		setContentView(R.layout.activity_healthparameter13);// ����Ҫ��ʾ�Ĳ����ļ�
		
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
		
		Intent intent = getIntent();// �õ����ݵ� ����
		Gender = intent.getStringExtra("Gender");
		 Age = intent.getStringExtra("Age");
		 Height = intent.getStringExtra("Height");
		 Weight = intent.getStringExtra("Weight");



	}
	class MainThread extends Thread {
		  public void run() {
		   Looper.prepare();
		   //��ʼ�����
		    bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
		    AudioFormat.CHANNEL_CONFIGURATION_MONO,
		    AudioFormat.ENCODING_PCM_16BIT);
		    ar = new AudioRecord(MediaRecorder.AudioSource.MIC,
		    SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO,
		    AudioFormat.ENCODING_PCM_16BIT, bs);
		    ar.startRecording();//��ʼ��¼
		    isRun = true;
		    while (isRun) {
		     short[] buffer = new short[bs];
		     int r=ar.read(buffer, 0, bs);
		     int v = 0;
		     for (int i = 0; i < buffer.length; i++) {
		      v += buffer[i]*buffer[i];
		     }
		     Message msg = new Message();//���������ͼ����������С
		     msg.what=1;
		     msg.obj="Ŀǰ��������Ϊ:"+(int)Math.round(10*Math.log10(v/r))+"dB;";
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
		 ////�����ذ���ִ������
		 public void onBackPressed() {
		  super.onBackPressed();
		  if(isRun){///�����tureΪ�������,�˳���Ҫ�ر�,������
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
			 DecimalFormat nf = new DecimalFormat("0.000");//����С����λ��
			 String text="";
			if(arg0 == BMI) {
				if(isRun){///ֹͣ�ɼ�����
					  isRun = false;
				}
				if (Weight.equals("kg")){///�ж��Ƿ�����Ϣ����
					text="�뷵��������,���������Ϣ!";
				}
				else{
				height = Double.parseDouble(Height )/100;
				weight  = Double.parseDouble(Weight );				
				bmi=weight/(height*height);//����BMIָ��
				text="BMI="+nf.format(bmi)+";";
				if (bmi<18.5){//�ж�����
					text+="ƫ��";
				}
				else if(bmi>=18.5 && bmi<=23.9){
					text+="����";
				}
				else if(bmi>23.9 && bmi<27.9){
					text+="ƫ��";
				}
				else{
					text+="����";
				}
				double std_weight=0;///�������,�����׼���غ��������ط�Χ
				double high_weight=0;
				double low_weight=0;
				if(Gender.equals("��")){///�Ա�ͬ,������ͬ
					std_weight=height*height*22;
					high_weight=std_weight+std_weight*0.1;
					low_weight=std_weight-std_weight*0.1;
				}
				else {
					std_weight=height*height*20;
					high_weight=std_weight+std_weight*0.1;
					low_weight=std_weight-std_weight*0.1;
				}
				///��ʾ���
				Intro_Para.setText("���ı�׼����Ϊ��"+nf.format(std_weight)+"kg.\n�����������ط�Χ��"+nf.format(high_weight)+"kg"+"~"+nf.format(low_weight)+"kg"+"\nBMIָ��,����������ָ�����������ָ��,��Ŀǰ�����ⳣ�õĺ����������ݳ̶��Լ��Ƿ񽡿���һ����׼�������������ָ����22��\n���������");
				}
				Result_Para.setText(text);
				}
			else if (arg0 ==BMR){
				if(isRun){
					  isRun = false;
				}
				///û�м���
				text="�ף��Բ��𰡣�";
				Result_Para.setText(text);
				Intro_Para.setText("������л�ʣ����BMR����ָ�������ڰ���״̬��(ͨ��Ϊ����״̬)���ĵ�����������˵����������������������ϡ�����Ҫ��ã����������Ҳ�������Ҿ�͵����������������");
				}
			else if (arg0 ==BurnPB){
				if(isRun){
					  isRun = false;
				}
				if (Age.equals("��")){
					text="�뷵��������,���������Ϣ!";
				}
				else{
				double maxpb=0;
				double high_pb=0;
				double low_pb=0;
				age=Double.parseDouble(Age);
				maxpb=220-age;
				high_pb=maxpb*0.8;
				low_pb=maxpb*0.6;
				text="�����ȼ֬����Ϊ��"+nf.format(maxpb)+"��/��;\n"+"�еͶ��˶�����Ϊ"+nf.format(low_pb)+"~"+nf.format(high_pb)+"��/�֣�\n";
				Intro_Para.setText("ȼ֬������ָ���˶�ǿ�ȴﵽȼ��֬��ʱ�����ʣ���ȷ�Լ���ȼ֬�����Ƕ��ٿ�����Ч�����˶�ǿ�ȣ��ﵽȼ��֬����Ч������Ϊȼ֬�˶������ڻ��߸���ȼ֬���ʣ�������ȼ֬�˶���ȼ�յ�Ҳ�Ͳ���֬���ˣ�ÿ�˵�ȼ֬�������˶��졣");
				}
				Result_Para.setText(text);
									}
			else if (arg0 ==Noise){

				MainThread mt = new MainThread();//�����߳�,��ʼ¼��
				mt.start();
				Intro_Para.setText("���Ƿ�������������Ϣ��ѧϰ�͹������������Լ�������Ҫ���������������ŵ��������������������˵����������Դ�ܶ࣬�磬�ֵ��ϵ���������������ͼ������˵�������������صĻ��������Լ��ھӵ��ӻ����������������������\n���巶ΧΪ��\n0 ��20 �ֱ��� �ܾ��������о�������\n20 ��40 �ֱ��� �����������������\n40 ��60 �ֱ� ��һ�㡢��ͨ����̸����\n60 ��70 �ֱ��� ���֡������񾭣�\n70 ��90 �ֱ� ���ܳ�����ϸ���ܵ��ƻ���\n90 ��100 �ֱ��� ���ּӾ硢��������\n100 ��120 �ֱ��� �������ܡ���һ���Ӽ���ʱ������\n120�ֱ����ϣ� ��������ȫ����");
				
			}
		}
	}
}
	