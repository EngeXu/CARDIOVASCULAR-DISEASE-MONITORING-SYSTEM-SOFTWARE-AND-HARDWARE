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
	Button btnSearch,PPG, btnExit,Alert,SignalSetting;//�ֱ�Ϊ������Ѱ���������ź���������˳����棬�ر�����
	ToggleButton tbtnSwitch;//������/�رռ�
	ListView lvBTDevices;//��Ѱ�����������豸�б�
	ArrayAdapter<String> adtDevices;//�����豸�б�
	List<String> lstDevices = new ArrayList<String>();//�������������豸
	BluetoothAdapter btAdapt;//����������

	public static BluetoothSocket btSocket;
	public String str=null;//ȫ�ֱ���,��ѡ��������ӿ�
	
	public String Fs="250";
	public String Chnalnmb="1";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_care15);
		// Button ����
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
		// ToogleButton����
		tbtnSwitch = (ToggleButton) this.findViewById(R.id.tbtnSwitch);
		tbtnSwitch.setOnClickListener(new ClickEvent());
		// Editext����

		// ListView��������Դ ������
		lvBTDevices = (ListView) this.findViewById(R.id.lvDevices);
		adtDevices = new ArrayAdapter<String>(third_activity.this,
				android.R.layout.simple_list_item_1, lstDevices);
		lvBTDevices.setAdapter(adtDevices);
		lvBTDevices.setOnItemClickListener(new ItemClickEvent());

		btAdapt = BluetoothAdapter.getDefaultAdapter();// ��ʼ��������������

		if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)// ��ȡ����״̬����ʾ
			tbtnSwitch.setChecked(false);
		else if (btAdapt.getState() == BluetoothAdapter.STATE_ON)
			tbtnSwitch.setChecked(true);

		// ע��Receiver����ȡ�����豸��صĽ��
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// ��BroadcastReceiver��ȡ���������
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(searchDevices, intent);

	}
	//��ʾ��Ѱ�����豸
	public BroadcastReceiver searchDevices = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
//			Bundle b = intent.getExtras();
//			Object[] lstName = b.keySet().toArray();

			// ��ʾ�����յ�����Ϣ����ϸ��,�����ã���error����ʽ��eclipse�з�����Ϣ
//			for (int i = 0; i < lstName.length; i++) {
//				String keyName = lstName[i].toString();
////				Log.e(keyName, String.valueOf(b.get(keyName)));
//			}
			// �����豸ʱ��ȡ���豸��MAC��ַ
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String str = device.getName() + "|" + device.getAddress();
				if (lstDevices.indexOf(str) == -1)// ��ֹ�ظ����
					lstDevices.add(str); // ��ȡ�豸���ƺ�mac��ַ
				adtDevices.notifyDataSetChanged();
			}
		}
	};

	protected void onDestroy() {
		this.unregisterReceiver(searchDevices);
		super.onDestroy();
//		android.os.Process.killProcess(android.os.Process.myPid());//����ָ��ʹ��������ֱ���˳�,��ע��
	}
	//��ʾ��ѡ����豸
	class ItemClickEvent implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			btAdapt.cancelDiscovery();
			str = lstDevices.get(arg2);
			setTitle("��ѡ��" + str );
		}

	}

	@SuppressLint("ShowToast")
	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			if (v == btnSearch)// ���������豸����BroadcastReceiver��ʾ���
			{  
				if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// ���������û����
					Toast.makeText(third_activity.this, "���ȴ�����", 1000).show();
					return;
				}			
				setTitle("����������ַ��" + btAdapt.getAddress());
				lstDevices.clear();
				btAdapt.startDiscovery();
			} 
			else if (v == tbtnSwitch) {// ������������/�ر�
				if (tbtnSwitch.isChecked() == false){
					// �������Ա�����
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
			      /*�������ڵ�����ͷ����*/
				.setView(myView)
			      .setTitle(R.string.SignalSetting_Title)
			      /*���õ������ڵ�ͼʽ*/
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
			    	Toast.makeText(third_activity.this, "�������óɹ�", 200).show();
			     }

			     }
			    )
			      /*���õ������ڵķ����¼�*/
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
					Toast.makeText(third_activity.this, "������������", 1000).show();
				}
				else
				{
				String[] values = str.split("\\|");//���ַ����ӡ�|�����ֿ���ǰ�沿������Ϊ0������Ϊ1
				String address = values[1];//ȡ��������ʽ������IP��ַ��
				Log.e("address", values[1]);
				UUID uuid = UUID.fromString(SPP_UUID);
				BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
				try {
					btSocket = btDev.createRfcommSocketToServiceRecord(uuid);//����һ���ͻ���
					btSocket.connect();
					
					// �򿪲���ͼʵ��
					setTitle("�ɼ������ź��У�........");
					Intent intent = new Intent();
					intent.putExtra("Fs", Fs);///��ͬactivity֮�� ���� ��ֵ��
					intent.putExtra("Chnalnmb", Chnalnmb);///��ͬactivity֮�� ���� ��ֵ��
					intent.setClass(third_activity.this, WaveDiagram.class);
					third_activity.this.startActivity(intent);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(third_activity.this, "��������������", 1000).show();
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
				Intent intent = getIntent();// �õ����ݵ� ����
				String Name = intent.getStringExtra("Name");
				String PhoneNumber1 = intent.getStringExtra("PhoneNumber1");
				String PhoneNumber2 = intent.getStringExtra("PhoneNumber2");
				Intent intent1 = new Intent();
				intent1.putExtra("Name", Name);///��ͬactivity֮�� ���� ��ֵ��
				intent1.putExtra("PhoneNumber1", PhoneNumber1 );///��ͬactivity֮�� ���� ��ֵ��
				intent1.putExtra("PhoneNumber2", PhoneNumber2 );
				intent1.setClass(third_activity.this, Alert_activity.class);
				third_activity.this.startActivity(intent1);
			}
		}
		}
	

	

}
