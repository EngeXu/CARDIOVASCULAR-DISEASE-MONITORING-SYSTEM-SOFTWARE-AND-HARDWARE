package com.BBW.Myprj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignalProcessing {
	private ArrayList<float[]> SF = new ArrayList<float[]>();

	private int degree=50;//低通阶数
	private ArrayList<Float> SF1 = new ArrayList<Float>();
	private ArrayList<Float> SF2 = new ArrayList<Float>();
	private ArrayList<Float> SF3 = new ArrayList<Float>();
	private ArrayList<Float> SF4 = new ArrayList<Float>();
	static float PRV =0f;
	
	public static int m=3;
	private static float lastdatum;
	private static int lastdiff;
	private ArrayList <Integer> x = new ArrayList<Integer>();
	private static int [] S = new int [(int)Math.pow(3, m)];
	private static float  lastPRV=0;
	private static float  LastSSE=0;
	
	public void init(){
		SF.add(new float [200]);//初始化陷波滤波器,SF添加四组系数
//		SF.add(new int [200]);
//		SF.add(new int [200]);
//		SF.add(new int [200]);
		for (int i=0;i<degree;i++){//初始化低通滤波器,SF添加四组系数
		SF1.add(0f);
		SF2.add(0f);
		SF3.add(0f);
		SF4.add(0f);
		}
	}
	


	public float band_stop(float data, int flag){
		float[] sf1 = new float[200];	
			sf1 = SF.get(flag);
		float bandstop_R;
		float sf4=sf1[3]*2;
		float wnew=data-sf4-sf1[7];
		float sf100=sf1[99]*2;
		float Out=(wnew+sf100+sf1[199])/625;
	    bandstop_R=data-Out;
	    for (int i=199;i>0;i--)
	     {
		  sf1[i]=sf1[i-1]; 	 
		   }
	    sf1[0]=wnew;
	    SF.set(flag, sf1);
		return bandstop_R;
	}
	
	public float low_pass(float data,int flag){//支持四路信号同步采集
		float Low_R=0f;
		if (flag==0){		
			float wn = data+SF1.get(0);
			Low_R =(float) (wn-SF1.get(degree-1))/degree;
			SF1.remove(degree-1);
			SF1.add(0, wn);
		}
		else if(flag==1){
			float wn = data+SF2.get(0);
			Low_R = (wn-SF2.get(degree-1))/degree;
			SF2.remove(degree-1);
			SF2.add(wn);
		}
		else if(flag==2){
			float wn = data+SF3.get(0);
			Low_R = (wn-SF3.get(degree-1))/degree;
			SF3.remove(degree-1);
			SF3.add(wn);
		}
		else{
			float wn = data+SF4.get(0);
			Low_R = (wn-SF4.get(degree-1))/degree;
			SF4.remove(degree-1);
			SF4.add(wn);
		}
		return Low_R;
	}
	/*************** SSE end******************/
    public float  SSE(float datum,int m,int w,int i) {
    	//datum:数据点
    	//m:向量长度
    	//w:窗宽
    	//i：数据累计长度，用来控制处于初始化还是迭代过程
    	float  SSE_new=0;
    	if (i==0){
    		lastPRV=datum;
    	}
    	else{
    		float  diffpp=Math.round(datum-lastPRV);//ok
    		
    		if(i<=w){
    			x.add(Signification(diffpp));//ok
    			lastPRV=datum;
    			if (i>=m){    				
    				int mode=0;
    				for (int j=0;j<m;j++){
    					mode+=x.get(i-j-1)*((int)Math.pow(3, j));
    				}

    				S[mode]+=1;  				
    				float  A=(float )1/(w-m+1);
    				if (S[mode]==1){
    					SSE_new=LastSSE-(float)A*log2(A); 
    				}else{
    					SSE_new=LastSSE+(float )(S[mode]-1)*A*log2((float )(S[mode]-1)*A)-(float )S[mode]*A*log2((float )S[mode]*A);
    				}
    				LastSSE=SSE_new;
    			}else{
    				SSE_new=0;
    			}
    		}else{////大于窗口时,迭代  			
    			i=w;
    			x.add(Signification(diffpp));
    			lastPRV=datum;
				int mode1=0;
				for (int j=0;j<m;j++){
					mode1+=x.get(m-j-1)*((int)Math.pow(3, j));
				}    			
    			S[mode1]-=1;
    			x.remove(0);
				int mode_last=0;
				float  A=(float )1/(w-m+1);
				for (int j=0;j<m;j++){
					mode_last+=x.get(i-j-1)*((int)Math.pow(3, j));
				}    			
    			S[mode_last]+=1;
    			if(S[mode1]!=0 && S[mode_last]!=1 && mode1!=mode_last){
    			SSE_new=LastSSE+S[mode1]*A*log2(1+(float )1/S[mode1])+A*log2((float )(S[mode1]+1)/(S[mode_last]-1))+S[mode_last]*A*log2(1-(float )1/S[mode_last]);	
    			LastSSE=SSE_new;	
    			}
    			else if(S[mode1]==0 && S[mode_last]==1 || mode1==mode_last){
    			SSE_new=LastSSE;
    				}
    			else if(S[mode1]==0 && S[mode_last]!=1){
    			SSE_new=LastSSE+S[mode_last]*A*log2(1-(float )1/S[mode_last])-A*log2(S[mode_last]-1);	
    			LastSSE=SSE_new;	
    				}
    			else if(S[mode1]!=0 && S[mode_last]==1){
    			SSE_new=LastSSE+S[mode1]*A*log2(1+(float )1/(S[mode1]))+A*log2(S[mode1]+1);	
    			LastSSE=SSE_new;	
    			}
 			
    		}
    	}
    	
    	
    	return SSE_new;
    }
    public float  log2(float  B){
    	float  Duishu2;
    	Duishu2=(float )Math.log(B)/(float )Math.log(2);
    	return Duishu2;  	
    }
    public int Signification(float  PP){
    	 if (PP<0){
    		 return 0;
    	 }else if(PP==0){
    		 return 1;
    	 }else{
    		 return 2;
    	 }
    }
	/*************** SSE end******************/
	
	
	///提取极值子函数
    public int find_extrema(float datum,int flag,int i) {
	///datum：输入数据点
    ///falg：极值类型选择标示，flag=0：极大值；flag=1：极小值；flag=2：极值。
	int n=0;//初始化返回值
	if (flag == 0){///极大值
		if(i == 0){///第0个数据点，不计算，只初始化
			lastdatum =datum;
			n=0;
		}else{
			if(i==1){///第1个数据点，初始化差分值
				double d=datum-lastdatum;
				int diff;
				if (d>0){
					diff=1;
				}else{ diff=0;}	///对差分信号进行编码			
				lastdatum =datum;
				lastdiff=diff;
				n=0;
			}else{///第1个数据点，开始计算
				double d=datum-lastdatum;
				int diff;
				if (d>0){
					diff=1;
				}else{ diff=0;}	
				if(diff-lastdiff <0){ ///二阶差分，<0为极大值,>0极小值,!=0为极值
					n=1;
				}else{
				 n=0;
				}
				lastdatum =datum;
				lastdiff=diff;
			}
		}
		return n;
	}
	else if (flag==1){///极小值
		if(i == 0){
			lastdatum =datum;
			n=0;
		}else{
			if(i==1){
				double d=datum-lastdatum;
				int diff;
				if (d>0){
					diff=1;
				}else{ diff=0;}				
				lastdatum =datum;
				lastdiff=diff;
				n=0;
			}else{
				double d=datum-lastdatum;
				int diff;
				if (d>0){
					diff=1;
				}else{ diff=0;}	
				if(diff-lastdiff >0){
					n=1;
				}else{
				 n=0;
				}
				lastdatum =datum;
				lastdiff=diff;
			}
		}
		return n;
	}
	else{///极值
		if(i == 0){
			lastdatum =datum;
			n=0;
		}else{
			if(i==1){
				double d=datum-lastdatum;
				int diff;
				if (d>0){
					diff=1;
				}else{ diff=0;}				
				lastdatum =datum;
				lastdiff=diff;
				n=0;
			}else{
				double d=datum-lastdatum;
				int diff;
				if (d>0){
					diff=1;
				}else{ diff=0;}	
				if(diff-lastdiff!=0){
					n=1;
				}else{
				 n=0;
				}
				lastdatum =datum;
				lastdiff=diff;
			}
		}
		return n;
	}
   }	

	
	
	
	//计算PRV信号//
	public float PRV(float []data){
		float max = 0f;
		int p=0;
		max=data[1];
		for (int i=2;i<data.length;i++){
			if(max<data[i]){
				max=data[i];
				p=i-1;
			}
		}
		PRV = p*0.244f*60;
		return PRV;
	}
	public float [] MaxMin(float []data){
		float [] maxmin = new float[4];//分别返回一段数据的最大值maxmin[0]、最大值位置maxmin[1]、最小值maxmin[2]、最小值位置maxmin[3]

		maxmin[0]=data[1];
		maxmin[2]=data[1];
		for (int i=2;i<data.length;i++){
			if(maxmin[0]<data[i]){
				maxmin[0]=data[i];
				maxmin[1]=i;
			}
			else if(maxmin[2]>data[i]){
				maxmin[2]=data[i];
				maxmin[3]=i;
			}
		}
		return maxmin;
	}
	
	
	////////////////FFT////////////////
	//*输入信号长度即为变换的点数*//
	public float [] fft(float [] INPUT){
		/////初始化 
		int input_length=INPUT.length;
		int dianshu =input_length;
		float [] cos_tab = new float[input_length] ;
		float [] sin_tab = new float[input_length] ;
		float [] fWaveR = new float[input_length] ;
		float [] fWaveI = new float[input_length] ;
		float [] w = new float[input_length] ;
		
		for ( int i=0;i<input_length;i++ ){
			sin_tab[i]=(float)Math.sin(Math.PI*2*i/input_length);
			cos_tab[i]=(float)Math.cos(Math.PI*2*i/input_length);
			//********** following code invert sequence ************/
			int x0,x1,x2,x3,x4,x5,x6,x7,x8,x9;
			x0=i&0x01; 
			x1=(i/2)&0x01; 
			x2=(i/4)&0x01;
			x3=(i/8)&0x01;
			x4=(i/16)&0x01; 
			x5=(i/32)&0x01; 
			x6=(i/64)&0x01;
			x7=(i/128)&0x01;
			x8=(i/256)&0x01;
			x9=(i/512)&0x01;
			int xx=x0*512+x1*256+x2*128+x3*64+x4*32+x5*16+x6*8+x7*4+x8*2+x9;
			fWaveR[xx]=INPUT[i];
		}			
			

			//************** following code FFT *******************/
			for (int L=1;L<=10;L++ ){ 
				int b=1, i=L-1;
				while ( i>0 ) {
					b=b*2; i--;
				} 
				for ( int j=0;j<=b-1;j++ ){
					int p=1; i=10-L;
					while ( i>0 ) {
						p=p*2; i--;
						}
					p=p*j;
					for ( int k=j;k<dianshu;k=k+2*b ) {
						float TR=fWaveR[k], TI=fWaveI[k], temp=fWaveR[k+b];
						fWaveR[k]=fWaveR[k]+fWaveR[k+b]*cos_tab[p]+fWaveI[k+b]*sin_tab[p];
						fWaveI[k]=fWaveI[k]-fWaveR[k+b]*sin_tab[p]+fWaveI[k+b]*cos_tab[p];
						fWaveR[k+b]=TR-fWaveR[k+b]*cos_tab[p]-fWaveI[k+b]*sin_tab[p];
						fWaveI[k+b]=TI+temp*sin_tab[p]-fWaveI[k+b]*cos_tab[p];
						w[k]=(float)Math.sqrt(fWaveR[k]*fWaveR[k]+fWaveI[k]*fWaveI[k]);
						w[k+b]=(float)Math.sqrt(fWaveR[k+b]*fWaveR[k+b]+fWaveI[k+b]*fWaveI[k+b]);}
				}
			} 

		return w;
	}

}
