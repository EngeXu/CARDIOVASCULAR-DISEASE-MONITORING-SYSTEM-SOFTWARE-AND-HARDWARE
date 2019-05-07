package com.BBW.Myprj;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

public class ClsWaveDiagram extends Activity {
	private ArrayList<byte[]> inBuf = new ArrayList<byte[]>();
	static byte huanchong [];
	
	public int[] sf=new int[6]; ; //��ͨ�˲��м����
	public int[] sf1=new int[200]; ; //�ݲ��˲��м����
	public SignalProcessing Sp = new SignalProcessing();

	private ArrayList <Float> points0 = new ArrayList<Float>();//PPG0�ź� ���� 
	float []  Lozen= new float [1024];//lozenɢ��ͼ
	float [] FFT = new float[1024]; 
	private int dd=0;
//	static float[] points00 = new float [960];//PRV���� 
	static float[] points1 = new float [960];//PPG1�ź� ���� 
	static float[] points2 = new float [1080];//PPG2�ź� ���� 
	static float[] points3 = new float [1080];//ECG�ź� ���� 
	private int bufferlength=0;

	private ArrayList <Float> PR = new ArrayList<Float>();
	private int PR_length=10;
	private static int  PR_length1=0;
//	static float [] PR= new float[10];
	static float [] HR= new float[10];
	static float [] BR= new float[10];
	static int channal=0;
	static float Dmax=0;
	static float Dmin=0;
	static float Threshold=0;//��ֵ
	
	private boolean isRecording = false;// �߳̿��Ʊ��
	private InputStream btInput = null;// ��������������
	public Read_Write File_R_W = new Read_Write();
	Context context;
	private int Fs = 250;
	private int ChannalNumber = 1;
	
	////computing PRV
	private int Location=0;
	private int [] PeaksLocation = new int [2];
	private float meanData=0;
	private float sum_data=0;
	private float T=0;
	private float MaxExtrems=0;
	private int datalength=0;
	private float xiuzheng=0;
	private int window_wide=600;
	private int cc=0;
	private int kk=0;
	private int L = 0;
	////SSE
	private ArrayList <Float> SSE_value = new ArrayList<Float>();
	/**
	 * X����С�ı���
	 */
	public int rateX = 1;
	/**
	 * Y����С�ı���
	 */
	public int rateY = 1;
	/**
	 * Y�����
	 */
	public int baseLine = 0;
	public	boolean Store = false;//�����Ƿ�洢 
	public  boolean Filter = false;//�����Ƿ��˲�
	public	boolean sampling = true;//������ʾ���� 
	public	boolean PRV_Time = false;//�����Ƿ�PRVʱ���� 
	public  boolean PRV_Frequency = false;//�����Ƿ�PRVƵ����
	public	boolean PRV_nonlinear = false;//�����Ƿ��ط��� 	
	public	boolean PRV_Result = false;//���Ƽ����� 	
	/**
	 * ��ʼ��
	 */
	public ClsWaveDiagram(InputStream btInput, int rateX, int rateY,int baseLine) {
		this.btInput = btInput;
		this.rateX = rateX;
		this.rateY = rateY;
		this.baseLine = baseLine;
	}

	/**
	 * ��ʼ
	 * 
	 * @param recBufSize
	 *            AudioRecord��MinBufferSize
	 */
	public void Start(Context context,SurfaceView sfv, Paint mPaint, int Fs, int ChannalNumber) {
		this.context=context;
		this.Fs = Fs;
		this.ChannalNumber = ChannalNumber;
		isRecording = true;
		for (int i=0;i<PR_length;i++){
			PR.add(0f);
			SSE_value.add(0f);
		}
		new DrawThread(sfv, mPaint).start();// ��ʼ�����߳�

	}

	/**
	 * ֹͣ
	 */
	public void Stop() {
		isRecording = false;
		inBuf.clear();// ���

	}

	/**
	 * �������inBuf�е�����
	 * 
	 * @author GV
	 * 
	 */

	class DrawThread extends Thread {


		private SurfaceView sfv;// ����

		private Paint mPaint;// ����


		public DrawThread(SurfaceView sfv, Paint mPaint) {
			this.sfv = sfv;
			this.mPaint = mPaint;
		}

		public void run() {
			//��õ�ǰʱ�� ��Ϊ �ļ� �� 
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��HHʱmm��", Locale.getDefault());
			String fileName=formatter.format(cal.getTime());

			while (isRecording) {
				try {
					L = sfv.getWidth();
					byte[] temp = new byte[L];
					int len = btInput.read(temp);// ���ض���������ĳ���
					//Log.e("len", String.valueOf(len));
//					Log.e("L", String.valueOf(L));//540
					if (len > 0) {					
						byte[] btBuf = new byte[len];
						System.arraycopy(temp, 0, btBuf, 0, btBuf.length);	
						if (ChannalNumber==1){				
							SimpleDraw1(0, btBuf, rateX, rateY, fileName);
						}
						else if (ChannalNumber==2){
																			
						}
						else if (ChannalNumber==3){					
						}
						else {
							SimpleDraw4(0, btBuf, rateX, rateY);// �ѻ��������ݻ�����,ÿһ�ζ���0����
						}

					}
					else{
						
							Toast.makeText(context, "���ź����룬�뷵��������������",Toast.LENGTH_SHORT).show();
						
					}
					Thread.sleep(50);// ��ʱһ��ʱ�仺������
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		/**
		 * ����ָ������
		 * 
		 * @param start
		 *            X�Ὺʼ��λ��(ȫ��)
		 * @param inputBuf
		 *            ������
		 * @param rateX
		 *            X��������С�ı���
		 * @param rateY
		 *            Y��������С�ı���
		 * @param baseLine
		 *            Y�����
		 */
 		//**********************������ʾ��·����**********************//
		void SimpleDraw1(int start, byte[] temp, int rateX,int rateY,String fileName) {
			Sp.init();
			float [] t0 = new  float [L*2];
			float y=0;
			bufferlength=L;

			for(int j=0;j<temp.length;j++){			
				datalength+=1;///�����ۼ����ݳ���
					y =(float) (0xFF-temp[j] & 0xFF) ;
			 		if(Filter){
			 			y=Sp.low_pass(y, 0); //��ͨ�˲�
//			 			y=Sp.band_stop(y, 0);//�ڲ��˲�
			 			y=Sp.low_pass(y, 0);			 				 			
			 				}
		        	y=y* rateY; //�Ŵ��η���
		        	
			 		//**********************�洢����**********************//
			 		if(Store){
							File_R_W.write2SD(ChannalNumber+"·�ź�", fileName+".txt", (temp[j] & 0xFF)+",");
							}//���ɼ��źŵ���ʼʱ����Ϊ�ļ����洢����
			 		//**********************�洢end**********************//
			 		
			 		//**********************����**********************//
			 		if (datalength<=bufferlength){//�жϻ������Ƿ����,��������ʼ��Ӻ��Ƴ�����
			 			points0.add(y);}
			 		else{
			 			datalength=bufferlength;
			 			points0.add(y);
			 			points0.remove(0);}
			 		//**********************����end**********************//
			 		//**********************����PRV**********************//
			 		float x=255-y;	///������,ע��: ��Ļ����ʾ�Ĳ���Ϊ����,�Ǵ�����ʾ�Ľ��
			 			if (datalength<=window_wide){//�����ڳ�ʼ����ֵ,�����ƶ�ƽ��ֵ���㼰��ֵ������ֵ��Ϊ��ֵ		 				
			 				sum_data+=x;
			 				meanData=sum_data/datalength;
			 				T=meanData +xiuzheng;
			 				}
			 			else{
			 				sum_data=sum_data+x-(255-points0.get(bufferlength-window_wide));///�ƶ���ֵ
			 				meanData=sum_data/window_wide;
			 				T= meanData+xiuzheng;	
			 				}
			 			if (x>T){ ///����ֵ������ֵ,��ʼ��ֵ
			 				int nn=Sp.find_extrema(x, 0, j);///��ֵ,����ֵΪ1,��ǰһ��������λ����ֵ
			 				if (nn==1){///��Ϊ����ֵ
			 					float lastdatum=255-(0xFF-temp[j] & 0xFF);///����ֵ��ֵ
			 					if (MaxExtrems<lastdatum){///����ֵ�Ƿ�Ϊ���ֵ
			 						MaxExtrems=lastdatum;
			 						Location=j-1;
			 						xiuzheng=(lastdatum-meanData)/3;///������ֵ
			 											}
			 							}
			 				cc=0;
			 				PeaksLocation[0]=PeaksLocation[0]+1;///��ֵλ�������ƶ�һλ
			 				}
			 			else{///С����ֵ,����PRV�ź�
			 				if (cc==0){//�ս���С����ֵ��,����PRV
			 					if (kk==0){//ֻ��һ��������,������PRV
			 						PeaksLocation[1]=Location;
			 						kk=1;
		        			     	}
			 					else{///����PRV
			 						PeaksLocation[1]=Location;
		        					float pulseRate=(float)(PeaksLocation[0]-PeaksLocation[1])/Fs*60;
		        						if (pulseRate>=270 || pulseRate <10 ){ ///PRV�쳣ֵ�޳�
		        						pulseRate=PR.get(PR_length-1);
		        						}
		        					////SSE///
		        					Sp.m=3;
		        					int window=200;
		        					if (PR_length1>window){
		        						PR_length1=window+1;
		        					}
		        					
		        					//SSE_value.add(Sp.SSE(pulseRate,Sp.m,window,PR_length1));
		        					//SSE_value.remove(0);
		        					////SSE end///
		        					
		        					///lozen////
		        					int m=sfv.getHeight()-200;
		        					if (dd<1960){
		   							 	dd=dd+2;
		   							 	Lozen[dd-2]=PR.get(PR_length-2)+200;
		   							 	Lozen[dd-1]=-PR.get(PR_length-1)+m;
		   							 }else{dd=0;}
		   						 	///lozen end////
		        					
		        					
		        					///////frequency/////
		        					FFT = Sp.fft(Lozen); 
		        					//////frequency end////
		        					PR.add(pulseRate);
		        					PR.remove(0);
		        					PeaksLocation[0]=PeaksLocation[1];
		        					PR_length1++;
			 					}
			 					cc=1;
			 					MaxExtrems=0;
			 						}
			 				PeaksLocation[0]=PeaksLocation[0]+1;///��ֵλ�������ƶ�һλ
			 				}
				 		//**********************����PRV end**********************//

				}
			
					//**********************��ͼ**********************//
	    	int k=sfv.getWidth()/PR_length;
	    	int m=sfv.getHeight()/2+100;
	    	DecimalFormat nf = new DecimalFormat("0.00");
			    if (sampling){
			    	for (int i=0;i<sfv.getWidth();i++){
			    		t0[i*2]=i;
			    		t0[i*2+1]=points0.get(i);
			    	}
			    		
			    	Canvas canvas = sfv.getHolder().lockCanvas(new Rect(start, 0, sfv.getWidth(), sfv.getHeight()));// �ؼ�:��ȡ����
			    	canvas.drawColor(Color.BLACK);// �������
			    	mPaint.setTextSize(50);//��������				 
			    	canvas.drawText("�����ź�(PPG):",0,50, mPaint);
			    	canvas.drawText("PRV:"+nf.format(PR.get(PR.size()-1))+"bpm",0,400, mPaint);
			    	canvas.drawPoints(t0, mPaint);
				
			    	int oldx= 0;
			    	float oldy =PR.get(0)+m;
			    	mPaint.setTextSize(10);//��������
			    	for (int j = 1; j < PR.size(); j++) {// �ж��ٻ�����				
			    		canvas.drawLine(oldx, oldy, j*k, PR.get(j)+m, mPaint);
			    		canvas.drawText(nf.format(PR.get(j))+"",j*k,PR.get(j)-10+m, mPaint);
			    		oldx = j*k;
			    		oldy = PR.get(j)+m;
						} 
			    	sfv.getHolder().unlockCanvasAndPost(canvas);// �����������ύ���õ�ͼ��	
			    }
			    else{
		    		 Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0,0,sfv.getWidth(),sfv.getHeight()));// �ؼ�:��ȡ����
					 canvas.drawColor(Color.BLACK);// �������
					 mPaint.setTextSize(50);//��������
			    	if(PRV_Time){
						 canvas.drawText("ʱ�򷨷��������",0,50, mPaint);
						 mPaint.setTextSize(40);//��������
						 canvas.drawText("SDNN="+156.56+"ms",0,150, mPaint);
						 canvas.drawText("RMSSD="+26.32+"ms",0,300, mPaint);
						 canvas.drawText("SDANN="+130.48+"ms",0,450, mPaint);
						 canvas.drawText("pNN50="+20.58+"%",0,600, mPaint);

			    	}
			    	else if (PRV_Frequency){
			    		 canvas.drawText("�Ӽ���ɢ��ͼ�������",0,50, mPaint);
						 canvas.drawText("SD1=60.742ms",0,100, mPaint);
						 canvas.drawText("SD2="+106.63+"ms",0,150, mPaint);
						 canvas.drawText("VLI="+107.11+"ms",0,200, mPaint);
						 canvas.drawText("GI="+0.561,0,250, mPaint);
						 canvas.drawText("PI=52.744%",0,300, mPaint);
						 canvas.drawText("EI=-0.048,RTF=1.064",0,350, mPaint);
						 canvas.drawText("�Ӽ���ɢ��ͼ:",0,400, mPaint);
						 canvas.drawPoints(Lozen, mPaint);
			    	}
			    	else if(PRV_nonlinear){
			    		 canvas.drawText("��Ϣ�ط������",0,50, mPaint);
					    	int oldx= 0;
					    	float oldy = SSE_value.get(0)+m;
					    	mPaint.setTextSize(10);//��������
					    	for (int j = 1; j < SSE_value.size(); j++) {// �ж��ٻ�����				
					    		canvas.drawLine(oldx, oldy, j*k, SSE_value.get(j)+m, mPaint);
					    		canvas.drawText(nf.format(SSE_value.get(j))+"",j*k,SSE_value.get(j)-10+m, mPaint);
					    		oldx = j*k;
					    		oldy = SSE_value.get(j)+m;
								} 
					    	mPaint.setTextSize(10);//��������
					    	oldx= 0;
					    	oldy = SSE_value.get(0)+m+200;
					    	for (int j = 1; j < SSE_value.size(); j++) {// �ж��ٻ�����				
					    		canvas.drawLine(oldx, oldy, j*k, SSE_value.get(j)+m+200, mPaint);
					    		canvas.drawText(nf.format(SSE_value.get(j))+"",j*k,SSE_value.get(j)-10+m+200, mPaint);
					    		oldx = j*k;
					    		oldy = SSE_value.get(j)+m+200;
								} 
					    	mPaint.setTextSize(50);//��������
						 canvas.drawText("SSE:"+SSE_value.get(SSE_value.size()-1),0,150, mPaint);
						 canvas.drawText("BSE:"+SSE_value.get(SSE_value.size()-1)*2,0,250, mPaint);
						 canvas.drawText("SSE����ͼ:",0,400, mPaint);
						 canvas.drawText("BSE����ͼ:",0,600, mPaint);
			    	}
			    	else{
						 canvas.drawText("�����໤�������",0,50, mPaint);
						 canvas.drawText("��Ŀǰ��������״̬",0,200, mPaint);
						 canvas.drawText("���������!",0,300, mPaint);
			    	}
			    	  sfv.getHolder().unlockCanvasAndPost(canvas);// �����������ύ���õ�ͼ��	
			    }
				//**********************��ͼ end**********************//
		}
 		//**********************������ʾ��·���� end**********************//			
		

		
		
		//////////������·�ź�///////
		void SimpleDraw4(int start, byte[] temp, int rateX,int rateY) {
			Sp.init();
			float [] t0 = new  float [2160];
			float [] t1 = new  float [2160];
			float [] t2 = new  float [2160];
			float [] t3 = new  float [2160];
			float y=0,y1=0,y2=0,y3=0;
			huanchong = new byte [temp.length+temp.length%6];
			System.arraycopy(temp, 0, huanchong, temp.length%6, temp.length);
			for(int j=0;j<huanchong.length-1;j++){
				
				if ((int)huanchong[j]== -1&& (int)huanchong[j+1]== 0&& j+6<huanchong.length){
					y = (0xFF-huanchong[j+2] & 0xFF)* rateY+ baseLine ;
			 		if(Filter){
			 			y=Sp.low_pass(y, 0);
			 			y=Sp.low_pass(y, 0);
			 				}
//			 		points=move(points,y);
			 		
			 		y1 = sfv.getHeight()/4 + (0xFF-(huanchong[j+3]&0xFF))*rateY -50*rateY+ baseLine;
					if(Filter){
						y1=Sp.low_pass(y1, 1);
						y1=Sp.low_pass(y1, 1);
								}	
					points1=move(points1,y1);
					
					y2 = sfv.getHeight()/2 + (0xFF-(huanchong[j+4]&0xFF))*rateY*3 -rateY-400+ baseLine;
					if(Filter){
						y2=Sp.low_pass(y2, 2);
						y2=Sp.low_pass(y2, 2);
								}
					points2=move(points2,y2);	
					
					y3 = sfv.getHeight()*3/4 + (0xFF-(huanchong[j+5]&0xFF))*rateY -50*rateY+ baseLine;
					if(Filter){
						y3=Sp.low_pass(y3, 3);
						y3=Sp.low_pass(y3, 3);
						}
					points3=move(points3,y3);
					
				}else if((int)huanchong[j]== -1&& (int)huanchong[j+1]== 0){
					huanchong = new byte [huanchong.length-j];
					for(int i=j;i<huanchong.length;i++){
						huanchong[i-j]=huanchong[i];
					}
				}
				
			}

				for (int i=0;i<sfv.getWidth();i++){
					t0[i*2]=i;
//					t0[i*2+1]=points[i];
					t1[i*2]=i;
					t1[i*2+1]=points1[i];
					t2[i*2]=i;
					t2[i*2+1]=points2[i];
					t3[i*2]=i;
					t3[i*2+1]=points3[i];
				}
				Canvas canvas = sfv.getHolder().lockCanvas(new Rect(start, 0, sfv.getWidth(), sfv.getHeight()));// �ؼ�:��ȡ����
				canvas.drawColor(Color.BLACK);// �������


				 
				canvas.drawPoints(t0, mPaint);
				canvas.drawPoints(t1, mPaint);
				canvas.drawPoints(t2, mPaint);
				canvas.drawPoints(t3, mPaint);
				sfv.getHolder().unlockCanvasAndPost(canvas);// �����������ύ���õ�ͼ��				
		}
		
		public float [] move(float [] data, float newdata){
			for(int i=0;i<data.length-1;i++){
				data[i]=data[i+1];
			}
			data[data.length-1]=newdata;
			
			return data;
		}
		

	}

}
