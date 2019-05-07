package com.BBW.Myprj;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


public class TextReader_Write extends Activity{
	public ArrayList<String> DATA = new ArrayList<String>();
	
	public  void save(Context context, String fileName,ArrayList<String> data){
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		try{
			fOut = context.openFileOutput(fileName, 0);
			osw = new OutputStreamWriter(fOut);
			for( int i=0;i<data.size();i++){
			osw.append(data.get(i));
			osw.append("\n");
//			System.out.println("in1:"+data.get(i));
			}
			//osw.write(data);
			osw.flush();
			Toast.makeText(context, "信息已存储",Toast.LENGTH_SHORT).show();
		}
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(context, "信息存储失败或 文件不存在",Toast.LENGTH_SHORT).show();			
		}
		finally{
			try{
				osw.close();
				fOut.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	public ArrayList<String>   Read(Context context,String filename){
		DATA.clear();

		FileInputStream fIn = null;
		InputStreamReader isr = null;
		
		String  data = null;
		try{
			fIn = context.openFileInput(filename);
			isr = new InputStreamReader(fIn);

			BufferedReader in = new BufferedReader(isr);

			while( (data = in.readLine())!=null){
				DATA.add(data);
			}		
//			isr.read(inputBuffer);
//			data = new String(inputBuffer);
//			System.out.println("1:"+DATA.get(0));
//			System.out.println("2:"+DATA.get(1));
//			System.out.println("3:"+DATA.get(2));
//			System.out.println("4:"+DATA.get(3));
//			System.out.println("5:"+DATA.get(4));
//			System.out.println("6:"+DATA.get(5));
//			System.out.println("7:"+DATA.get(6));
			Toast.makeText(context, "信息已读取 ", Toast.LENGTH_SHORT).show();
			fIn.close();
			isr.close();		
		}
		catch(Exception e){
			DATA.add("false");
			System.out.println(DATA.get(0));

			Toast.makeText(context, "信息读取失败或 文件不存在 ", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return DATA;
		}


		return DATA;
	}

//public String  Reader(Context context,String filename){
//		
//
//		FileInputStream fIn = null;
//		InputStreamReader isr = null;
//		char [] inputBuffer = new char[2048]; 
//		String  data = null;
//		try{
//			fIn = context.openFileInput(filename);
//			isr = new InputStreamReader(fIn);
//
//			
//			isr.read(inputBuffer);
//			data = new String(inputBuffer);
//			Toast.makeText(context, "信息已读取 ", Toast.LENGTH_LONG).show();
//		}
//		catch(Exception e){
//			Toast.makeText(context, "信息读取失败或 文件不存在 ", Toast.LENGTH_LONG).show();
//			e.printStackTrace();
//			
//		}
//		finally{
//			try{
//				isr.close();
//				fIn.close();
//			}
//			catch(IOException e){
//				e.printStackTrace();
//			}
//		}
//		return data;
//	}
	
	
}


