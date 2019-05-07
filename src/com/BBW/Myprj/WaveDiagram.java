package com.BBW.Myprj;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;

public class WaveDiagram extends Activity {
	ClsWaveDiagram clsWaveDiagram;
	SurfaceView sfvWave;
	public Read_Write File_R_W = new Read_Write();
	Paint mPaint;
	InputStream btInput;
    ZoomControls zctlX,zctlY;
    
	private static final String[] m = { "采集","存储", "滤波","分析","@!@"};
	private Spinner operation;
	private ArrayAdapter<String> adapter;
	
	public int Fs;
	public int ChannalNumber;
	public int k=0;

	static final int xMax = 5;//X轴缩小比例最大值,X轴数据量巨大，容易产生刷新延时
	static final int xMin = 1;//X轴缩小比例最小值
	static final int yMax = 5;//Y轴缩小比例最大值
	static final int yMin = 1;//Y轴缩小比例最小值
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signal);
		 this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		sfvWave = (SurfaceView) this.findViewById(R.id.sfvWave);
		sfvWave.setOnTouchListener(new TouchEvent());//触摸屏幕

		
		Intent intent = getIntent();// 得到传递的 数据
		Fs = Integer.valueOf(intent.getStringExtra("Fs"));//获取第一个电话号码
		ChannalNumber = Integer.valueOf(intent.getStringExtra("Chnalnmb"));
		
		Toast.makeText(WaveDiagram.this,ChannalNumber+ "路信号采集开始",Toast.LENGTH_SHORT).show();
		
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);// 画笔为绿色
		mPaint.setStrokeWidth(2);// 设置画笔粗细
		
		
		operation = (Spinner) findViewById(R.id.operationinput);
		adapter= new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, m);//数据操作下拉框
		adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
		operation .setAdapter(adapter);
		operation .setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0){
					    clsWaveDiagram.sampling = true;
				}
				   if (position == 1){
						clsWaveDiagram.Store = true;
						Toast.makeText(WaveDiagram.this, "信号已存储",Toast.LENGTH_SHORT).show();
				   }
				   else if (position ==2){
					   Toast.makeText(WaveDiagram.this, "开始信号滤波",Toast.LENGTH_SHORT).show();
					   clsWaveDiagram.Filter = true;
				   }
				   else if (position ==3){
					   Toast.makeText(WaveDiagram.this, "开始信号分析",Toast.LENGTH_SHORT).show();
							new AlertDialog.Builder(WaveDiagram.this)  
							.setTitle("请选择要查看的PRV分析结果！")  
							.setIcon(R.drawable.bk2)                  
							.setSingleChoiceItems(new String[] {"时域处理结果","频域处理结果", "非线性分析结果","疾病监测结果" }, 0,   
							  new DialogInterface.OnClickListener() {  
							                              
							     public void onClick(DialogInterface dialog, int which) {  
							    	 switch (which){
							    	   case 0:  clsWaveDiagram.PRV_Time=true;
							    	   		    clsWaveDiagram.PRV_Frequency=false;
							    	   		    clsWaveDiagram.PRV_nonlinear=false;
							    	   		    clsWaveDiagram.PRV_Result=false;
							    	   		    clsWaveDiagram.sampling=false;
							    	   		    break;
							    	   case 1:  clsWaveDiagram.PRV_Time=false;
							    	   			clsWaveDiagram.PRV_Frequency=true;
							    	   			clsWaveDiagram.PRV_nonlinear=false;
							    	   			clsWaveDiagram.PRV_Result=false;
							    	   			clsWaveDiagram.sampling=false;
							    	   			break;
							    	   case 2:  clsWaveDiagram.PRV_Time=false;
							    	   			clsWaveDiagram.PRV_Frequency=false;
							    	   			clsWaveDiagram.PRV_nonlinear=true;
							    	   			clsWaveDiagram.PRV_Result=false;
							    	   			clsWaveDiagram.sampling=false;
							    	   			break;
							    	   case 3:  clsWaveDiagram.PRV_Time=false;
					    	   		   	 		clsWaveDiagram.PRV_Frequency=false;
					    	   		   	 		clsWaveDiagram.PRV_nonlinear=false;
					    	   		   	 		clsWaveDiagram.PRV_Result=true;
					    	   		   	 		clsWaveDiagram.sampling=false;
					    	   		   	 		break;
							    	 }
							    	 operation.setSelection(4);
							         dialog.dismiss();  
							     }  
							  }  
							)  
							.setNegativeButton("取消",  new DialogInterface.OnClickListener()
						      {
						    	  public void onClick(DialogInterface dialoginterface, int i)   {
						    		  operation.setSelection(4);
						    	  }
						      					})  
							.show();
						}

				   
					
			}
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				operation.setSelection(3);
			}
			
		});
		operation.setVisibility(View.VISIBLE);
		zctlX = (ZoomControls)this.findViewById(R.id.zctlX);
		zctlX.setOnZoomOutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clsWaveDiagram.rateX>xMin)
					clsWaveDiagram.rateX--;
				setTitle("X轴缩小"+String.valueOf(clsWaveDiagram.rateX)+"倍"
						+","+"Y轴放大"+String.valueOf(clsWaveDiagram.rateY)+"倍");
			}
		});
		zctlX.setOnZoomInClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clsWaveDiagram.rateX<xMax)
					clsWaveDiagram.rateX++;	
				setTitle("X轴缩小"+String.valueOf(clsWaveDiagram.rateX)+"倍"
						+","+"Y轴放大"+String.valueOf(clsWaveDiagram.rateY)+"倍");
			}
		});
		zctlY = (ZoomControls)this.findViewById(R.id.zctlY);
		zctlY.setOnZoomOutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clsWaveDiagram.rateY>yMin)
					clsWaveDiagram.rateY--;
				setTitle("X轴缩小"+String.valueOf(clsWaveDiagram.rateX)+"倍"
						+","+"Y轴放大"+String.valueOf(clsWaveDiagram.rateY)+"倍");
			}
		});
		
		zctlY.setOnZoomInClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clsWaveDiagram.rateY<yMax)
					clsWaveDiagram.rateY++;	
				setTitle("X轴缩小"+String.valueOf(clsWaveDiagram.rateX)+"倍"
						+","+"Y轴放大"+String.valueOf(clsWaveDiagram.rateY)+"倍");
			}
		});
		try {
			if (third_activity.btSocket.getInputStream() != null) {
				clsWaveDiagram = new ClsWaveDiagram(
						third_activity.btSocket.getInputStream(), 1, 1,0);// 横向绘制波形图
				clsWaveDiagram.Start(WaveDiagram.this,sfvWave, mPaint,Fs,ChannalNumber);
				Log.e("clsWaveDiagram", "start");
			}
			else{
				 Toast.makeText(WaveDiagram.this, "没有数据输入，请重新连接蓝牙",Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		clsWaveDiagram.Stop();
		try {
			third_activity.btSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
////////////////触摸动态改变基线/////////////////
	class TouchEvent implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int  BaseLine= (int) event.getY();
			if (BaseLine<480){
				clsWaveDiagram.baseLine=clsWaveDiagram.baseLine-10;//触摸上半屏幕,每次触摸所有基线-10
			}else{
				clsWaveDiagram.baseLine=clsWaveDiagram.baseLine+10;//触摸下半屏幕,每次触摸所有基线+10
			}
			Log.e("baseLine", String.valueOf(clsWaveDiagram.baseLine));
			return true;
		}

	}
}
