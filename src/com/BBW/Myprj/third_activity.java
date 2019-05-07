package com.BBW.Myprj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class third_activity extends Activity {

	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	Button btnSearch,PPG, btnExit,Alert,SignalSetting;//分别为蓝牙搜寻键，启动信号输入键，退出界面，关闭蓝牙
	ToggleButton tbtnSwitch;//蓝牙打开/关闭键
	ListView lvBTDevices;//搜寻到蓝牙可用设备列表
	ArrayAdapter<String> adtDevices;//蓝牙设备列表
	List<String> lstDevices = new ArrayList<String>();//搜索到的蓝牙设备
	BluetoothAdapter btAdapt;//蓝牙适配器

	public static BluetoothSocket btSocket;
	public String str=null;//全局变量,已选择的蓝牙接口
	
	public String Fs="250";
	public String Chnalnmb="1";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_care15);
		// Button 设置
		Alert = (Button)findViewById(R.id.Alert);
		Alert.setOnClickListener(new ClickEvent());
		SignalSetting = (Button)findViewById(R.id.Signal_Setting);
		SignalSetting.setOnClickListener(new ClickEvent());
		btnSearch = (Button) this.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new ClickEvent());
		btnExit = (Button) this.findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new ClickEvent());		
		PPG = (Button)findViewById(R.id.PPG);
		PPG.setOnClickListener(new ClickEvent());	
		// ToogleButton设置
		tbtnSwitch = (ToggleButton) this.findViewById(R.id.tbtnSwitch);
		tbtnSwitch.setOnClickListener(new ClickEvent());
		// Editext设置

		// ListView及其数据源 适配器
		lvBTDevices = (ListView) this.findViewById(R.id.lvDevices);
		adtDevices = new ArrayAdapter<String>(third_activity.this,
				android.R.layout.simple_list_item_1, lstDevices);
		lvBTDevices.setAdapter(adtDevices);
		lvBTDevices.setOnItemClickListener(new ItemClickEvent());

		btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能

		if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)// 读取蓝牙状态并显示
			tbtnSwitch.setChecked(false);
		else if (btAdapt.getState() == BluetoothAdapter.STATE_ON)
			tbtnSwitch.setChecked(true);

		// 注册Receiver来获取蓝牙设备相关的结果
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(searchDevices, intent);

	}
	//显示搜寻到的设备
	public BroadcastReceiver searchDevices = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
//			Bundle b = intent.getExtras();
//			Object[] lstName = b.keySet().toArray();

			// 显示所有收到的消息及其细节,调试用，以error的形式在eclipse中返回信息
//			for (int i = 0; i < lstName.length; i++) {
//				String keyName = lstName[i].toString();
////				Log.e(keyName, String.valueOf(b.get(keyName)));
//			}
			// 搜索设备时，取得设备的MAC地址
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String str = device.getName() + "|" + device.getAddress();
				if (lstDevices.indexOf(str) == -1)// 防止重复添加
					lstDevices.add(str); // 获取设备名称和mac地址
				adtDevices.notifyDataSetChanged();
			}
		}
	};

	protected void onDestroy() {
		this.unregisterReceiver(searchDevices);
		super.onDestroy();
//		android.os.Process.killProcess(android.os.Process.myPid());//该条指令使整个程序直接退出,故注释
	}
	//显示已选择的设备
	class ItemClickEvent implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			btAdapt.cancelDiscovery();
			str = lstDevices.get(arg2);
			setTitle("已选择：" + str );
		}

	}

	@SuppressLint("ShowToast")
	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			if (v == btnSearch)// 搜索蓝牙设备，在BroadcastReceiver显示结果
			{  
				if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
					Toast.makeText(third_activity.this, "请先打开蓝牙", 1000).show();
					return;
				}			
				setTitle("本机蓝牙地址：" + btAdapt.getAddress());
				lstDevices.clear();
				btAdapt.startDiscovery();
			} 
			else if (v == tbtnSwitch) {// 本机蓝牙启动/关闭
				if (tbtnSwitch.isChecked() == false){
					// 本机可以被搜索
					Intent discoverableIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					discoverableIntent.putExtra(
							BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
					startActivity(discoverableIntent);
					btAdapt.enable();
				}

				else if (tbtnSwitch.isChecked() == true){
					btAdapt.disable();}
			} 
			else if(v == SignalSetting){
//				Intent intent=new Intent();
//	            intent.setClass(third_activity.this, Setting_signal.class);
//	            third_activity.this.startActivity(intent);	   
				LayoutInflater factory = LayoutInflater.from(third_activity.this); 
		        final View myView = factory.inflate(R.layout.signal_setting, null); 
		        final EditText fs = (EditText) myView.findViewById(R.id.Fs); 
		        final EditText CHNN = (EditText) myView.findViewById(R.id.channalnumber); 
				new AlertDialog.Builder(third_activity.this)
			      /*弹出窗口的最上头文字*/
				.setView(myView)
			      .setTitle(R.string.SignalSetting_Title)
			      /*设置弹出窗口的图式*/
			      .setIcon(R.drawable.bk2) 
			      .setPositiveButton(R.string.ok,
			      new DialogInterface.OnClickListener()
			    {
			     public void onClick(DialogInterface dialoginterface, int i)
			     {         
			    	String F=fs.getText().toString(); 
			    	String CN=CHNN.getText().toString(); 	
			    	if(F.equals("")){
						Fs="250";
					}
			    	else{
			    		Fs = F;
			    	}
			    	if(CN.equals("")){
			    		Chnalnmb="1";
					}
			    	else{
			    		Chnalnmb= CN;
			    	}
			    	Toast.makeText(third_activity.this, "参数设置成功", 200).show();
			     }

			     }
			    )
			      /*设置弹出窗口的返回事件*/
			      .setNegativeButton(R.string.cancel,
			       new DialogInterface.OnClickListener()
			      {
			     public void onClick(DialogInterface dialoginterface, int i)   
			    {
			    }
			      }).show();
				
			}
			else if(v == PPG){
				if(str==null){
					Toast.makeText(third_activity.this, "请连接蓝牙！", 1000).show();
				}
				else
				{
				String[] values = str.split("\\|");//将字符串从“|”处分开，前面部分索引为0，后面为1
				String address = values[1];//取出数字形式的蓝牙IP地址。
				Log.e("address", values[1]);
				UUID uuid = UUID.fromString(SPP_UUID);
				BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
				try {
					btSocket = btDev.createRfcommSocketToServiceRecord(uuid);//创建一个客户端
					btSocket.connect();
					
					// 打开波形图实例
					setTitle("采集脉搏信号中：........");
					Intent intent = new Intent();
					intent.putExtra("Fs", Fs);///不同activity之间 传递 键值对
					intent.putExtra("Chnalnmb", Chnalnmb);///不同activity之间 传递 键值对
					intent.setClass(third_activity.this, WaveDiagram.class);
					third_activity.this.startActivity(intent);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(third_activity.this, "请重新连接蓝牙", 1000).show();
				}
				}

			}	
			else if (v == btnExit) {
				try {
					if (btSocket != null)
						btSocket.close();
					btAdapt.disable();

				} catch (IOException e) {
					e.printStackTrace();
				}
				third_activity.this.finish();
			}
			else if(v == Alert){	
				Intent intent = getIntent();// 得到传递的 数据
				String Name = intent.getStringExtra("Name");
				String PhoneNumber1 = intent.getStringExtra("PhoneNumber1");
				String PhoneNumber2 = intent.getStringExtra("PhoneNumber2");
				Intent intent1 = new Intent();
				intent1.putExtra("Name", Name);///不同activity之间 传递 键值对
				intent1.putExtra("PhoneNumber1", PhoneNumber1 );///不同activity之间 传递 键值对
				intent1.putExtra("PhoneNumber2", PhoneNumber2 );
				intent1.setClass(third_activity.this, Alert_activity.class);
				third_activity.this.startActivity(intent1);
			}
		}
		}
	

	

}
