package com.BBW.Myprj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class fifth_activity_subhealth extends Activity{
	public Fifthactivity_subhealthmain Fifth_mian = new Fifthactivity_subhealthmain();///定义全局变量的父类
	
	private TextView question = null;///缓存题目
	private TextView Theme = null;///缓存标题
	
	private Button Last = null;//“上一题”键
	private Button Next = null;//“下一题”键	
	private RadioGroup Answer=null;///定义 包含所有答案的按钮组
	private RadioButton Answer1=null;///第一题答案
	private RadioButton Answer2=null;///第二题答案
	private RadioButton  Answer3=null;///第三题答案
	private RadioButton Answer4=null;///第四题答案
	private RadioButton Answer5=null;///第五题答案
	private int  number_Qustion=75;///问题的总长度
	private int  i=0;///缓存的分数存储的位置
	private int  kk=0;///字表总分存贮位置
	private int  length=0;// 第length个问题
	private int  checked=0;///对应选项的ID
	private int  sub_score=0; //亚健康表总分,用来判断可疑亚健康类型
	
	private int []  temp= new int [number_Qustion];///缓存每道题得分
	private float []  Tran_score= new float [9]; ///缓存每个子表的得分	
	private  String [] Question_intent= null; ///缓存问题内容
	private String style=null; ////两种类型:"亚健康"和"中医体质"
	private Resources res=null; ///引用string文件
	private  String [] therapy_chinesemed = null;///缓存"中医体质调查的建议"
	private  String [] therapy_sub = null; /////缓存"亚健康评估"建议
	private  String [] options= null;///缓存选项内容
	private  String [] Habitus= null;//缓存体制种类内容
	private  String [] Subhealth_style= null;//缓存亚健康种类内容
	private  String Habitus_dispaly= ":"; //体质评估结果显示内容
	private  String therapy_dispaly= ":"; //体质对应的建议内容显示
	
	private int score = 0;////子表总分累计的过程变量
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subhealth14);// 设置要显示的布局文件
		
		temp[0]=1;////如果第一次不选，默认值为1；
		question= (TextView) findViewById(R.id.Question);
		question.setText(R.string.Question1);// 此处写入题目
		
		Theme = (TextView) findViewById(R.id.theme_subh);
		
		
		
		Last = (Button) findViewById(R.id.last);
		Last.setOnClickListener(new MyButtonListener());												
	
		Next = (Button) findViewById(R.id.next);
		Next.setOnClickListener(new MyButtonListener());	
		
		Answer =(RadioGroup) findViewById(R.id.answer);
		Answer1=(RadioButton) findViewById(R.id.A);
		Answer2=(RadioButton) findViewById(R.id.B);
		Answer3=(RadioButton) findViewById(R.id.C);
		Answer4=(RadioButton) findViewById(R.id.D);
		Answer5=(RadioButton) findViewById(R.id.E);
		Intent intent = getIntent();// 得到传递的 数据
		style= intent.getStringExtra("style");//获取进行测试的类型	
		
		res = getResources(); 		
		Habitus=res.getStringArray(R.array.chinesemed_habitus);
		Subhealth_style=res.getStringArray(R.array.subhealth_style);
		therapy_chinesemed =res.getStringArray(R.array.chinesemed_therapy);
		therapy_sub =res.getStringArray(R.array.sub_therapy);
		
		//////根据选择,读取相应的问题和选项
		if (style.equals("1")){
			Theme.setText(R.string.sub_health);
			Question_intent= res.getStringArray(R.array.Question_subhealth);
			options= res.getStringArray(R.array.sub_options0);
			}
		else{
			Theme.setText(R.string.chinesemed);
			Question_intent= res.getStringArray(R.array.Question_chinesemed);
			options= res.getStringArray(R.array.chinesemed_options0);

		}
		//////显示第一个问题和选项
		question.setText(Question_intent[0]);		
		Answer1.setText(options[0]);
		Answer2.setText(options[1]);
		Answer3.setText(options[2]);
		Answer4.setText(options[3]);
		Answer5.setText(options[4]);
		
		
		checked=Answer1.getId();////默认为第一个选项,等到其ID
		Answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){	
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				checked=checkedId;
			} 
		});
	}
	
	public void DisplayToast(String str)  ///Toast子函数
    {  
        Toast toast=Toast.makeText(this, str, Toast.LENGTH_SHORT);  
       toast.setGravity(Gravity.TOP,0,220);  
        toast.show();  
    }  
		

	
//	
	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {//按确定按钮，进入second_activity
			if(arg0 == Last) {
				//////"上一题"按键响应内容
				if(length>0){////退回上一个问题,并显示以前选择的答案
					if (i>0){ 
					length=length-1;
					i=i-1;
						score=score-temp[i];//减去上次的得分
						question.setText(Question_intent[length]);//重新显示问题
						switch (temp[i]){ //重新显示以前选过的答案
						case 1: 	Answer1.setChecked(true);break;
						case 2: 	Answer2.setChecked(true);break;						
						case 3: 	Answer3.setChecked(true);break;
						case 4:		Answer4.setChecked(true);break;
						case 5: 	Answer5.setChecked(true);break;	
						}
					  }
					else {////子表第一题,不能再后退
						DisplayToast("亲，累死我了，不能再后退!!!!!!!");
						Answer1.setChecked(true);//重新初始化
						temp=new int [number_Qustion];
						temp[0]=1;
					}
				       }
				else {
					DisplayToast("亲，已是第一题，不要难为我了!!!!!!!");
					Answer1.setChecked(true);//重新初始化
					temp=new int [number_Qustion];
					temp[0]=1;
				}
			}
			else if (arg0 == Next){
				/////"下一题"按键响应内容
				if (style.equals("2")){
					///对于中医体质调查报告,从第二张子表开始调换答案顺序
					if (length>6){
					options= res.getStringArray(R.array.chinesemed_options1);
					Answer1.setText(options[0]);
					Answer2.setText(options[1]);
					Answer3.setText(options[2]);
					Answer4.setText(options[3]);
					Answer5.setText(options[4]);
					}
				}
				else {
					///对于亚健康调查报告,从第二张子表开始调换答案顺序
					if (length>5){
					options= res.getStringArray(R.array.sub_options1);
					Answer1.setText(options[0]);
					Answer2.setText(options[1]);
					Answer3.setText(options[2]);
					Answer4.setText(options[3]);
					Answer5.setText(options[4]);}					
				}
				length++;//指向下一个问题
				getvalue();//得到选项的答案
				score=score+temp[i];//计算子表的累计积分
				/////遇到"1",表示子表结束,开始计算子表分
				if (Question_intent[length].equals("1")){
					i=i+1;//加1代表第八题
					sub_score+=score;///仅供亚健康使用
					Computerscore();//计算子表总分
					/////重新初始化/////
					score=0;
					i=0;
					temp=new int [number_Qustion];
					temp[i]=1;
					///////直接显示下一组的下一题
					length++;					
					question.setText(Question_intent[length]);					
					Answer1.setChecked(true);
				}
				/////遇到"2",表示总表结束,开始显示最终结果
				else if (Question_intent[length].equals("2")){
					sub_score+=score;///仅供亚健康使用
					Computerscore();/////计算最后一种类型的总分
					if (style.equals("1")){///如果为亚健康,根据阈值判断结果
						Habitus_dispaly="您的亚健康测试结果为"+Habitus_dispaly;
						therapy_dispaly="\n具体的"+therapy_dispaly+"\n";						
						int []  threshold= {56,72,60};//三种亚健康的阈值,采用转换分
						int m=0;
						for (int j=0;j<3;j++)
						{
							if (Tran_score[j]<=threshold[j]){//根据对应的阈值,判断是否为相应类型的亚健康
								Habitus_dispaly=Habitus_dispaly+Subhealth_style[j]+";";
								therapy_dispaly+=therapy_sub[j];								
								m++;
							}
					
						}
						if(sub_score<=224&&m==0){///疑似亚健康的判断
							Habitus_dispaly+="对不起，亲！我怀疑您为亚健康，可能力有限，不知道属于什么类型。";
							therapy_dispaly="";							
						}
						else if((sub_score>224&&m==0)) {///正常健康状态
							Habitus_dispaly+="亲！您的心理、身体状态和社会关系良好，你太牛了，请继续保持奥！！！";
							therapy_dispaly="";
						}
						m=0;
						Fifth_mian.sub_content=Habitus_dispaly+therapy_dispaly;///全局变量,显示结果
					}
					else{
						Habitus_dispaly="您的体质测试结果为"+Habitus_dispaly;
						therapy_dispaly="\n具体的"+therapy_dispaly+"\n";
						int m=0;
						for (int j=0;j<9;j++)
						{
							if (Tran_score[j]>50){ //"是"这种体质的判断
								Habitus_dispaly=Habitus_dispaly+Habitus[j]+";";
								therapy_dispaly+=therapy_chinesemed[j];
								m++;
							}
							else if (Tran_score[j]>=30&&Tran_score[j]<=49){//"倾向是"这种体质的判断
								Habitus_dispaly=Habitus_dispaly+Habitus[j]+"（倾向）;";
								therapy_dispaly+=therapy_chinesemed[j];
								m++;
							}
						}
						if(m==0){//不符合任何一种类型体质的判断
							Habitus_dispaly+="您太淘气了,不好好答题,以为我不知道.";
							therapy_dispaly="";
						}	
						Fifth_mian.Chi_content=Habitus_dispaly+therapy_dispaly;
					}
					
					PopupAlertDialog();

				}
				else{
					i=i+1;//子表题目索引
					question.setText(Question_intent[length]);//显示子表题目
				}				
			
			}
		}
	}
	private void Computerscore() {//计算子表总分的转换分
		Tran_score[kk]=(float)(score-i)/(i*5-i)*100;
		kk++;
	}	
	private void getvalue(){//获取键值
		  if(Answer1.getId()==checked){
			temp[i]=1;
		 								}
			else if(Answer2.getId()==checked){
				temp[i]=2; 
												 }
			else if(Answer3.getId()==checked){
				temp[i]=3;
												}
			else if(Answer4.getId()==checked){
				temp[i]=4;
												}
			else if(Answer5.getId()==checked){
				temp[i]=5;
												}
			else {
				temp[i]=0;///两次输入相同，去上次的值为本次测量值		
				}
	}
	private void PopupAlertDialog() {//答题结束,探出判断结果对话框
		  new AlertDialog.Builder(fifth_activity_subhealth.this)
	      /*弹出窗口的最上头文字*/
	      .setTitle(R.string.sub_result)
	      /*设置弹出窗口的图式*/
	      .setIcon(R.drawable.bk2) 
	      /*设置弹出窗口的信息*/
	      .setMessage(Habitus_dispaly+"。")
	      .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
	      		{
	    	  		public void onClick(DialogInterface dialoginterface, int ii)
	    	  		{   
	    	  			finish();/*关闭窗口*/
	    	  		}
	      		}
	    		  			)
	      /*设置弹出窗口的返回事件*/
	      .setNegativeButton(R.string.sub_result_retry, new DialogInterface.OnClickListener()
	      {
	    	  public void onClick(DialogInterface dialoginterface, int ii)   {
					length=0;//重新初始化
					kk=0;
					question.setText(Question_intent[length]);
					Answer1.setChecked(true);
					temp=new int [number_Qustion];
					Habitus_dispaly=":";
					therapy_dispaly=":";
					temp[0]=1;
					score=0;
	    	  }
	      					}).show();
	}
}
