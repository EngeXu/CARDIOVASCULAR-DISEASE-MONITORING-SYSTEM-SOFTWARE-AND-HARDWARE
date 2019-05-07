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
    
	private static final String[] m = { "�ɼ�","�洢", "�˲�","����","@!@"};
	private Spinner operation;
	private ArrayAdapter<String> adapter;
	
	public int Fs;
	public int ChannalNumber;
	public int k=0;

	static final int xMax = 5;//X����С�������ֵ,X���������޴����ײ���ˢ����ʱ
	static final int xMin = 1;//X����С������Сֵ
	static final int yMax = 5;//Y����С�������ֵ
	static final int yMin = 1;//Y����С������Сֵ
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signal);
		 this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		sfvWave = (SurfaceView) this.findViewById(R.id.sfvWave);
		sfvWave.setOnTouchListener(new TouchEvent());//������Ļ

		
		Intent intent = getIntent();// �õ����ݵ� ����
		Fs = Integer.valueOf(intent.getStringExtra("Fs"));//��ȡ��һ���绰����
		ChannalNumber = Integer.valueOf(intent.getStringExtra("Chnalnmb"));
		
		Toast.makeText(WaveDiagram.this,ChannalNumber+ "·�źŲɼ���ʼ",Toast.LENGTH_SHORT).show();
		
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);// ����Ϊ��ɫ
		mPaint.setStrokeWidth(2);// ���û��ʴ�ϸ
		
		
		operation = (Spinner) findViewById(R.id.operationinput);
		adapter= new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, m);//���ݲ���������
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
						Toast.makeText(WaveDiagram.this, "�ź��Ѵ洢",Toast.LENGTH_SHORT).show();
				   }
				   else if (position ==2){
					   Toast.makeText(WaveDiagram.this, "��ʼ�ź��˲�",Toast.LENGTH_SHORT).show();
					   clsWaveDiagram.Filter = true;
				   }
				   else if (position ==3){
					   Toast.makeText(WaveDiagram.this, "��ʼ�źŷ���",Toast.LENGTH_SHORT).show();
							new AlertDialog.Builder(WaveDiagram.this)  
							.setTitle("��ѡ��Ҫ�鿴��PRV���������")  
							.setIcon(R.drawable.bk2)                  
							.setSingleChoiceItems(new String[] {"ʱ������","Ƶ������", "�����Է������","���������" }, 0,   
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
							.setNegativeButton("ȡ��",  new DialogInterface.OnClickListener()
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
				setTitle("X����С"+String.valueOf(clsWaveDiagram.rateX)+"��"
						+","+"Y��Ŵ�"+String.valueOf(clsWaveDiagram.rateY)+"��");
			}
		});
		zctlX.setOnZoomInClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clsWaveDiagram.rateX<xMax)
					clsWaveDiagram.rateX++;	
				setTitle("X����С"+String.valueOf(clsWaveDiagram.rateX)+"��"
						+","+"Y��Ŵ�"+String.valueOf(clsWaveDiagram.rateY)+"��");
			}
		});
		zctlY = (ZoomControls)this.findViewById(R.id.zctlY);
		zctlY.setOnZoomOutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clsWaveDiagram.rateY>yMin)
					clsWaveDiagram.rateY--;
				setTitle("X����С"+String.valueOf(clsWaveDiagram.rateX)+"��"
						+","+"Y��Ŵ�"+String.valueOf(clsWaveDiagram.rateY)+"��");
			}
		});
		
		zctlY.setOnZoomInClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clsWaveDiagram.rateY<yMax)
					clsWaveDiagram.rateY++;	
				setTitle("X����С"+String.valueOf(clsWaveDiagram.rateX)+"��"
						+","+"Y��Ŵ�"+String.valueOf(clsWaveDiagram.rateY)+"��");
			}
		});
		try {
			if (third_activity.btSocket.getInputStream() != null) {
				clsWaveDiagram = new ClsWaveDiagram(
						third_activity.btSocket.getInputStream(), 1, 1,0);// ������Ʋ���ͼ
				clsWaveDiagram.Start(WaveDiagram.this,sfvWave, mPaint,Fs,ChannalNumber);
				Log.e("clsWaveDiagram", "start");
			}
			else{
				 Toast.makeText(WaveDiagram.this, "û���������룬��������������",Toast.LENGTH_SHORT).show();
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
////////////////������̬�ı����/////////////////
	class TouchEvent implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int  BaseLine= (int) event.getY();
			if (BaseLine<480){
				clsWaveDiagram.baseLine=clsWaveDiagram.baseLine-10;//�����ϰ���Ļ,ÿ�δ������л���-10
			}else{
				clsWaveDiagram.baseLine=clsWaveDiagram.baseLine+10;//�����°���Ļ,ÿ�δ������л���+10
			}
			Log.e("baseLine", String.valueOf(clsWaveDiagram.baseLine));
			return true;
		}

	}
}
