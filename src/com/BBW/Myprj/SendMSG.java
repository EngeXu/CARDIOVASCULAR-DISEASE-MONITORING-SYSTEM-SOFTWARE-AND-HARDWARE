package com.BBW.Myprj;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendMSG extends Activity{
	PendingIntent  sentIntent,deliverPI;
	SmsManager smsManager;
	Context context;
	   
	public SendMSG(Context context) {
		 sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);//取得 发送短信的 权限
		smsManager = SmsManager.getDefault();
		this.context=context;
	}
	public void send(String content, String phoneNumber){
		SmsManager smsmanager = SmsManager.getDefault();
		String message_content = content;		
		if(message_content.length()>70)//如果字数多于70个，拆分成两条短信
        {
			ArrayList<String> msgs = smsmanager.divideMessage(message_content);
			for(String msg:msgs){
				smsmanager.sendTextMessage(phoneNumber, null, msg,  sentIntent, deliverPI);
			}
        }else{
        	smsmanager.sendTextMessage(phoneNumber, null, message_content,  sentIntent, deliverPI);
        }
//		smsmanager.sendTextMessage(phoneNumber, null, message_content, sentIntent, null);//发送短信

		Toast.makeText(context, phoneNumber+"短消息发送成功",
				Toast.LENGTH_LONG).show();
		
	}
    
}
