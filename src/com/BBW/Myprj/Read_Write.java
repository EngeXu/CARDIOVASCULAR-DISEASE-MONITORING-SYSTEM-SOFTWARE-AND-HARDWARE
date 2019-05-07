package com.BBW.Myprj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Read_Write  {
	 private String SDCardRoot;

//	 private String SDStateString ;
	 public Read_Write(){
	  //得到当前外部存储设备的目录
	

	  SDCardRoot = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	  // 获取扩展SD卡设备状态
	  System.out.println(SDCardRoot);
//	  SDStateString = Environment.getExternalStorageState();

	 }
	 public boolean isFileExist(String dir ,String fileName){
		  File file = new File(SDCardRoot + dir + File.separator + fileName);
		  return file.exists();
	 	}
	 public String getFilePath(String dir,String fileName){
		  return SDCardRoot + dir + File.separator + fileName;
		 }
	 
	 public byte [] readFromSD(String dir,String fileName,int bufferlength){
		 int l;
		  File file = new File(getFilePath(dir, fileName));
		  if( !file.exists() ){
			  System.out.println("The file is not exists");
		  }
		  FileInputStream inputStream = null;
		  try {
		   inputStream = new FileInputStream(file);
		   byte[] data = new byte[bufferlength];
		   while(true){
		    l = inputStream.read(data);
		    System.out.println(l);
			   return data;
		   }
		  } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		   
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }finally{
		   try {
		    if( inputStream != null ){
		     inputStream.close();
		    }
		   } catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		   }
		  }
		  return null;
		 }

	 public File creatSDDir(String dir) {
		  File dirFile = new File(SDCardRoot + dir + File.separator);
		  if( !dirFile.exists() ){
		   dirFile.mkdirs();
		  }
		  return dirFile;
		 }
	 public File createFileInSDCard(String dir ,String fileName) throws IOException {
		  File file = new File(SDCardRoot+ dir + File.separator + fileName);
		  file.createNewFile();
		  return file;
		 }
	 public boolean write2SD(String dir,String fileName,String bytes){
//		  if(bytes == null ){
//		   return false;
//		  }
		  FileOutputStream output = null;
		  OutputStreamWriter outStream =null;
		  try {
		    File file = null;
		    creatSDDir(dir);
		    file = createFileInSDCard(dir,fileName);
		    output  =new FileOutputStream(file,true);
		    outStream = new OutputStreamWriter(output);
		    //public FileOutputStream(String name,boolean append);append//为true说明为追加
		    outStream.write(bytes);
		    outStream.flush();
		    
		    return true;
		  // }
		  } catch (IOException e1) {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		  }finally{
		   try{
		    if( outStream!= null ){
		    	outStream.close();
		    }
		    if( output!= null ){
		    	output.close();
		    }
		   }
		   catch(Exception e){
		    e.printStackTrace();
		   }
		  }
		  return false;
		 }
//	 public void  write2SDFromInput(String R_dir,String R_fileName,String W_dir,String W_fileName){
//	     File W_file= null;
//	     OutputStream output = null;
//		   
//		  FileInputStream input= null;
//		  File R_file = new File(getFilePath(R_dir, R_fileName));
//	  try {
//	   // 拥有可读可写权限，并且有足够的容量
//
//	    creatSDDir(W_dir);
//	    W_file = createFileInSDCard(W_dir,W_fileName);
//	    output = new BufferedOutputStream(new FileOutputStream(W_file,true));
//	    
//
//		  if( !R_file.exists() ){
//			  System.out.println("The file"+R_fileName+" is not exists");
//		  }
//
//		   input = new FileInputStream(R_file);
//	    byte buffer [] = new byte[1024];
//	    int temp ;
//	    while((temp = input.read(buffer)) != -1){
//	     output.write(buffer,0,temp);
//	    }
//	    output.flush();
//	   
//	  } catch (IOException e1) {
//	   // TODO Auto-generated catch block
//	   e1.printStackTrace();
//	  }finally{
//	   try{
//	    if( output != null ){
//	     output.close();
//	     input.close();
//	    }
//	   }
//	   catch(Exception e){
//	    e.printStackTrace();
//	   }
//	  }
//	    }
	}

