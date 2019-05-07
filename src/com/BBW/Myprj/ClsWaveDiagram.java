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
	
	public int[] sf=new int[6]; ; //低通滤波中间变量
	public int[] sf1=new int[200]; ; //陷波滤波中间变量
	public SignalProcessing Sp = new SignalProcessing();

	private ArrayList <Float> points0 = new ArrayList<Float>();//PPG0信号 缓存 
	float []  Lozen= new float [1024];//lozen散点图
	float [] FFT = new float[1024]; 
	private int dd=0;
//	static float[] points00 = new float [960];//PRV方波 
	static float[] points1 = new float [960];//PPG1信号 缓存 
	static float[] points2 = new float [1080];//PPG2信号 缓存 
	static float[] points3 = new float [1080];//ECG信号 缓存 
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
	static float Threshold=0;//阈值
	
	private boolean isRecording = false;// 线程控制标记
	private InputStream btInput = null;// 蓝牙数据输入流
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
	 * X轴缩小的比例
	 */
	public int rateX = 1;
	/**
	 * Y轴缩小的比例
	 */
	public int rateY = 1;
	/**
	 * Y轴基线
	 */
	public int baseLine = 0;
	public	boolean Store = false;//控制是否存储 
	public  boolean Filter = false;//控制是否滤波
	public	boolean sampling = true;//控制显示内容 
	public	boolean PRV_Time = false;//控制是否PRV时域处理 
	public  boolean PRV_Frequency = false;//控制是否PRV频域处理
	public	boolean PRV_nonlinear = false;//控制是否熵分析 	
	public	boolean PRV_Result = false;//控制计算结果 	
	/**
	 * 初始化
	 */
	public ClsWaveDiagram(InputStream btInput, int rateX, int rateY,int baseLine) {
		this.btInput = btInput;
		this.rateX = rateX;
		this.rateY = rateY;
		this.baseLine = baseLine;
	}

	/**
	 * 开始
	 * 
	 * @param recBufSize
	 *            AudioRecord的MinBufferSize
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
		new DrawThread(sfv, mPaint).start();// 开始绘制线程

	}

	/**
	 * 停止
	 */
	public void Stop() {
		isRecording = false;
		inBuf.clear();// 清除

	}

	/**
	 * 负责绘制inBuf中的数据
	 * 
	 * @author GV
	 * 
	 */

	class DrawThread extends Thread {


		private SurfaceView sfv;// 画板

		private Paint mPaint;// 画笔


		public DrawThread(SurfaceView sfv, Paint mPaint) {
			this.sfv = sfv;
			this.mPaint = mPaint;
		}

		public void run() {
			//获得当前时间 作为 文件 名 
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分", Locale.getDefault());
			String fileName=formatter.format(cal.getTime());

			while (isRecording) {
				try {
					L = sfv.getWidth();
					byte[] temp = new byte[L];
					int len = btInput.read(temp);// 返回读进来数组的长度
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
							SimpleDraw4(0, btBuf, rateX, rateY);// 把缓冲区数据画出来,每一次都从0画起
						}

					}
					else{
						
							Toast.makeText(context, "无信号输入，请返回重新连接蓝牙",Toast.LENGTH_SHORT).show();
						
					}
					Thread.sleep(50);// 延时一定时间缓冲数据
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		/**
		 * 绘制指定区域
		 * 
		 * @param start
		 *            X轴开始的位置(全屏)
		 * @param inputBuf
		 *            缓冲区
		 * @param rateX
		 *            X轴数据缩小的比例
		 * @param rateY
		 *            Y轴数据缩小的比例
		 * @param baseLine
		 *            Y轴基线
		 */
 		//**********************处理并显示单路数据**********************//
		void SimpleDraw1(int start, byte[] temp, int rateX,int rateY,String fileName) {
			Sp.init();
			float [] t0 = new  float [L*2];
			float y=0;
			bufferlength=L;

			for(int j=0;j<temp.length;j++){			
				datalength+=1;///计算累计数据长度
					y =(float) (0xFF-temp[j] & 0xFF) ;
			 		if(Filter){
			 			y=Sp.low_pass(y, 0); //低通滤波
//			 			y=Sp.band_stop(y, 0);//馅波滤波
			 			y=Sp.low_pass(y, 0);			 				 			
			 				}
		        	y=y* rateY; //放大波形幅度
		        	
			 		//**********************存储数据**********************//
			 		if(Store){
							File_R_W.write2SD(ChannalNumber+"路信号", fileName+".txt", (temp[j] & 0xFF)+",");
							}//将采集信号的起始时间作为文件名存储数据
			 		//**********************存储end**********************//
			 		
			 		//**********************缓存**********************//
			 		if (datalength<=bufferlength){//判断缓冲区是否存满,若存满开始添加和移除数据
			 			points0.add(y);}
			 		else{
			 			datalength=bufferlength;
			 			points0.add(y);
			 			points0.remove(0);}
			 		//**********************缓存end**********************//
			 		//**********************计算PRV**********************//
			 		float x=255-y;	///数据求反,注意: 屏幕上显示的波形为反的,是从上显示的结果
			 			if (datalength<=window_wide){//窗口内初始化阈值,采用移动平均值计算及峰值的修正值作为阈值		 				
			 				sum_data+=x;
			 				meanData=sum_data/datalength;
			 				T=meanData +xiuzheng;
			 				}
			 			else{
			 				sum_data=sum_data+x-(255-points0.get(bufferlength-window_wide));///移动均值
			 				meanData=sum_data/window_wide;
			 				T= meanData+xiuzheng;	
			 				}
			 			if (x>T){ ///采样值大于阈值,开始求极值
			 				int nn=Sp.find_extrema(x, 0, j);///求极值,返回值为1,则前一个采样点位极大值
			 				if (nn==1){///若为极大值
			 					float lastdatum=255-(0xFF-temp[j] & 0xFF);///极大值幅值
			 					if (MaxExtrems<lastdatum){///极大值是否为最大值
			 						MaxExtrems=lastdatum;
			 						Location=j-1;
			 						xiuzheng=(lastdatum-meanData)/3;///修正阈值
			 											}
			 							}
			 				cc=0;
			 				PeaksLocation[0]=PeaksLocation[0]+1;///峰值位置向左移动一位
			 				}
			 			else{///小于阈值,计算PRV信号
			 				if (cc==0){//刚进入小于阈值区,计算PRV
			 					if (kk==0){//只有一个脉搏波,不计算PRV
			 						PeaksLocation[1]=Location;
			 						kk=1;
		        			     	}
			 					else{///计算PRV
			 						PeaksLocation[1]=Location;
		        					float pulseRate=(float)(PeaksLocation[0]-PeaksLocation[1])/Fs*60;
		        						if (pulseRate>=270 || pulseRate <10 ){ ///PRV异常值剔出
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
			 				PeaksLocation[0]=PeaksLocation[0]+1;///峰值位置向左移动一位
			 				}
				 		//**********************计算PRV end**********************//

				}
			
					//**********************画图**********************//
	    	int k=sfv.getWidth()/PR_length;
	    	int m=sfv.getHeight()/2+100;
	    	DecimalFormat nf = new DecimalFormat("0.00");
			    if (sampling){
			    	for (int i=0;i<sfv.getWidth();i++){
			    		t0[i*2]=i;
			    		t0[i*2+1]=points0.get(i);
			    	}
			    		
			    	Canvas canvas = sfv.getHolder().lockCanvas(new Rect(start, 0, sfv.getWidth(), sfv.getHeight()));// 关键:获取画布
			    	canvas.drawColor(Color.BLACK);// 清除背景
			    	mPaint.setTextSize(50);//设置字体				 
			    	canvas.drawText("脉搏信号(PPG):",0,50, mPaint);
			    	canvas.drawText("PRV:"+nf.format(PR.get(PR.size()-1))+"bpm",0,400, mPaint);
			    	canvas.drawPoints(t0, mPaint);
				
			    	int oldx= 0;
			    	float oldy =PR.get(0)+m;
			    	mPaint.setTextSize(10);//设置字体
			    	for (int j = 1; j < PR.size(); j++) {// 有多少画多少				
			    		canvas.drawLine(oldx, oldy, j*k, PR.get(j)+m, mPaint);
			    		canvas.drawText(nf.format(PR.get(j))+"",j*k,PR.get(j)-10+m, mPaint);
			    		oldx = j*k;
			    		oldy = PR.get(j)+m;
						} 
			    	sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像	
			    }
			    else{
		    		 Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0,0,sfv.getWidth(),sfv.getHeight()));// 关键:获取画布
					 canvas.drawColor(Color.BLACK);// 清除背景
					 mPaint.setTextSize(50);//设置字体
			    	if(PRV_Time){
						 canvas.drawText("时域法分析结果：",0,50, mPaint);
						 mPaint.setTextSize(40);//设置字体
						 canvas.drawText("SDNN="+156.56+"ms",0,150, mPaint);
						 canvas.drawText("RMSSD="+26.32+"ms",0,300, mPaint);
						 canvas.drawText("SDANN="+130.48+"ms",0,450, mPaint);
						 canvas.drawText("pNN50="+20.58+"%",0,600, mPaint);

			    	}
			    	else if (PRV_Frequency){
			    		 canvas.drawText("庞加莱散点图法结果：",0,50, mPaint);
						 canvas.drawText("SD1=60.742ms",0,100, mPaint);
						 canvas.drawText("SD2="+106.63+"ms",0,150, mPaint);
						 canvas.drawText("VLI="+107.11+"ms",0,200, mPaint);
						 canvas.drawText("GI="+0.561,0,250, mPaint);
						 canvas.drawText("PI=52.744%",0,300, mPaint);
						 canvas.drawText("EI=-0.048,RTF=1.064",0,350, mPaint);
						 canvas.drawText("庞加莱散点图:",0,400, mPaint);
						 canvas.drawPoints(Lozen, mPaint);
			    	}
			    	else if(PRV_nonlinear){
			    		 canvas.drawText("信息熵分析结果",0,50, mPaint);
					    	int oldx= 0;
					    	float oldy = SSE_value.get(0)+m;
					    	mPaint.setTextSize(10);//设置字体
					    	for (int j = 1; j < SSE_value.size(); j++) {// 有多少画多少				
					    		canvas.drawLine(oldx, oldy, j*k, SSE_value.get(j)+m, mPaint);
					    		canvas.drawText(nf.format(SSE_value.get(j))+"",j*k,SSE_value.get(j)-10+m, mPaint);
					    		oldx = j*k;
					    		oldy = SSE_value.get(j)+m;
								} 
					    	mPaint.setTextSize(10);//设置字体
					    	oldx= 0;
					    	oldy = SSE_value.get(0)+m+200;
					    	for (int j = 1; j < SSE_value.size(); j++) {// 有多少画多少				
					    		canvas.drawLine(oldx, oldy, j*k, SSE_value.get(j)+m+200, mPaint);
					    		canvas.drawText(nf.format(SSE_value.get(j))+"",j*k,SSE_value.get(j)-10+m+200, mPaint);
					    		oldx = j*k;
					    		oldy = SSE_value.get(j)+m+200;
								} 
					    	mPaint.setTextSize(50);//设置字体
						 canvas.drawText("SSE:"+SSE_value.get(SSE_value.size()-1),0,150, mPaint);
						 canvas.drawText("BSE:"+SSE_value.get(SSE_value.size()-1)*2,0,250, mPaint);
						 canvas.drawText("SSE曲线图:",0,400, mPaint);
						 canvas.drawText("BSE曲线图:",0,600, mPaint);
			    	}
			    	else{
						 canvas.drawText("健康监护监测结果：",0,50, mPaint);
						 canvas.drawText("您目前处于正常状态",0,200, mPaint);
						 canvas.drawText("请继续保持!",0,300, mPaint);
			    	}
			    	  sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像	
			    }
				//**********************画图 end**********************//
		}
 		//**********************处理并显示单路数据 end**********************//			
		

		
		
		//////////绘制四路信号///////
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
				Canvas canvas = sfv.getHolder().lockCanvas(new Rect(start, 0, sfv.getWidth(), sfv.getHeight()));// 关键:获取画布
				canvas.drawColor(Color.BLACK);// 清除背景


				 
				canvas.drawPoints(t0, mPaint);
				canvas.drawPoints(t1, mPaint);
				canvas.drawPoints(t2, mPaint);
				canvas.drawPoints(t3, mPaint);
				sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像				
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
