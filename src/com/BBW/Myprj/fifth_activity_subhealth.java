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
	public Fifthactivity_subhealthmain Fifth_mian = new Fifthactivity_subhealthmain();///����ȫ�ֱ����ĸ���
	
	private TextView question = null;///������Ŀ
	private TextView Theme = null;///�������
	
	private Button Last = null;//����һ�⡱��
	private Button Next = null;//����һ�⡱��	
	private RadioGroup Answer=null;///���� �������д𰸵İ�ť��
	private RadioButton Answer1=null;///��һ���
	private RadioButton Answer2=null;///�ڶ����
	private RadioButton  Answer3=null;///�������
	private RadioButton Answer4=null;///�������
	private RadioButton Answer5=null;///�������
	private int  number_Qustion=75;///������ܳ���
	private int  i=0;///����ķ����洢��λ��
	private int  kk=0;///�ֱ��ִܷ���λ��
	private int  length=0;// ��length������
	private int  checked=0;///��Ӧѡ���ID
	private int  sub_score=0; //�ǽ������ܷ�,�����жϿ����ǽ�������
	
	private int []  temp= new int [number_Qustion];///����ÿ����÷�
	private float []  Tran_score= new float [9]; ///����ÿ���ӱ�ĵ÷�	
	private  String [] Question_intent= null; ///������������
	private String style=null; ////��������:"�ǽ���"��"��ҽ����"
	private Resources res=null; ///����string�ļ�
	private  String [] therapy_chinesemed = null;///����"��ҽ���ʵ���Ľ���"
	private  String [] therapy_sub = null; /////����"�ǽ�������"����
	private  String [] options= null;///����ѡ������
	private  String [] Habitus= null;//����������������
	private  String [] Subhealth_style= null;//�����ǽ�����������
	private  String Habitus_dispaly= ":"; //�������������ʾ����
	private  String therapy_dispaly= ":"; //���ʶ�Ӧ�Ľ���������ʾ
	
	private int score = 0;////�ӱ��ܷ��ۼƵĹ��̱���
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subhealth14);// ����Ҫ��ʾ�Ĳ����ļ�
		
		temp[0]=1;////�����һ�β�ѡ��Ĭ��ֵΪ1��
		question= (TextView) findViewById(R.id.Question);
		question.setText(R.string.Question1);// �˴�д����Ŀ
		
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
		Intent intent = getIntent();// �õ����ݵ� ����
		style= intent.getStringExtra("style");//��ȡ���в��Ե�����	
		
		res = getResources(); 		
		Habitus=res.getStringArray(R.array.chinesemed_habitus);
		Subhealth_style=res.getStringArray(R.array.subhealth_style);
		therapy_chinesemed =res.getStringArray(R.array.chinesemed_therapy);
		therapy_sub =res.getStringArray(R.array.sub_therapy);
		
		//////����ѡ��,��ȡ��Ӧ�������ѡ��
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
		//////��ʾ��һ�������ѡ��
		question.setText(Question_intent[0]);		
		Answer1.setText(options[0]);
		Answer2.setText(options[1]);
		Answer3.setText(options[2]);
		Answer4.setText(options[3]);
		Answer5.setText(options[4]);
		
		
		checked=Answer1.getId();////Ĭ��Ϊ��һ��ѡ��,�ȵ���ID
		Answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){	
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				checked=checkedId;
			} 
		});
	}
	
	public void DisplayToast(String str)  ///Toast�Ӻ���
    {  
        Toast toast=Toast.makeText(this, str, Toast.LENGTH_SHORT);  
       toast.setGravity(Gravity.TOP,0,220);  
        toast.show();  
    }  
		

	
//	
	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {//��ȷ����ť������second_activity
			if(arg0 == Last) {
				//////"��һ��"������Ӧ����
				if(length>0){////�˻���һ������,����ʾ��ǰѡ��Ĵ�
					if (i>0){ 
					length=length-1;
					i=i-1;
						score=score-temp[i];//��ȥ�ϴεĵ÷�
						question.setText(Question_intent[length]);//������ʾ����
						switch (temp[i]){ //������ʾ��ǰѡ���Ĵ�
						case 1: 	Answer1.setChecked(true);break;
						case 2: 	Answer2.setChecked(true);break;						
						case 3: 	Answer3.setChecked(true);break;
						case 4:		Answer4.setChecked(true);break;
						case 5: 	Answer5.setChecked(true);break;	
						}
					  }
					else {////�ӱ��һ��,�����ٺ���
						DisplayToast("�ף��������ˣ������ٺ���!!!!!!!");
						Answer1.setChecked(true);//���³�ʼ��
						temp=new int [number_Qustion];
						temp[0]=1;
					}
				       }
				else {
					DisplayToast("�ף����ǵ�һ�⣬��Ҫ��Ϊ����!!!!!!!");
					Answer1.setChecked(true);//���³�ʼ��
					temp=new int [number_Qustion];
					temp[0]=1;
				}
			}
			else if (arg0 == Next){
				/////"��һ��"������Ӧ����
				if (style.equals("2")){
					///������ҽ���ʵ��鱨��,�ӵڶ����ӱ�ʼ������˳��
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
					///�����ǽ������鱨��,�ӵڶ����ӱ�ʼ������˳��
					if (length>5){
					options= res.getStringArray(R.array.sub_options1);
					Answer1.setText(options[0]);
					Answer2.setText(options[1]);
					Answer3.setText(options[2]);
					Answer4.setText(options[3]);
					Answer5.setText(options[4]);}					
				}
				length++;//ָ����һ������
				getvalue();//�õ�ѡ��Ĵ�
				score=score+temp[i];//�����ӱ���ۼƻ���
				/////����"1",��ʾ�ӱ����,��ʼ�����ӱ��
				if (Question_intent[length].equals("1")){
					i=i+1;//��1����ڰ���
					sub_score+=score;///�����ǽ���ʹ��
					Computerscore();//�����ӱ��ܷ�
					/////���³�ʼ��/////
					score=0;
					i=0;
					temp=new int [number_Qustion];
					temp[i]=1;
					///////ֱ����ʾ��һ�����һ��
					length++;					
					question.setText(Question_intent[length]);					
					Answer1.setChecked(true);
				}
				/////����"2",��ʾ�ܱ����,��ʼ��ʾ���ս��
				else if (Question_intent[length].equals("2")){
					sub_score+=score;///�����ǽ���ʹ��
					Computerscore();/////�������һ�����͵��ܷ�
					if (style.equals("1")){///���Ϊ�ǽ���,������ֵ�жϽ��
						Habitus_dispaly="�����ǽ������Խ��Ϊ"+Habitus_dispaly;
						therapy_dispaly="\n�����"+therapy_dispaly+"\n";						
						int []  threshold= {56,72,60};//�����ǽ�������ֵ,����ת����
						int m=0;
						for (int j=0;j<3;j++)
						{
							if (Tran_score[j]<=threshold[j]){//���ݶ�Ӧ����ֵ,�ж��Ƿ�Ϊ��Ӧ���͵��ǽ���
								Habitus_dispaly=Habitus_dispaly+Subhealth_style[j]+";";
								therapy_dispaly+=therapy_sub[j];								
								m++;
							}
					
						}
						if(sub_score<=224&&m==0){///�����ǽ������ж�
							Habitus_dispaly+="�Բ����ף��һ�����Ϊ�ǽ��������������ޣ���֪������ʲô���͡�";
							therapy_dispaly="";							
						}
						else if((sub_score>224&&m==0)) {///��������״̬
							Habitus_dispaly+="�ף�������������״̬������ϵ���ã���̫ţ�ˣ���������ְ£�����";
							therapy_dispaly="";
						}
						m=0;
						Fifth_mian.sub_content=Habitus_dispaly+therapy_dispaly;///ȫ�ֱ���,��ʾ���
					}
					else{
						Habitus_dispaly="�������ʲ��Խ��Ϊ"+Habitus_dispaly;
						therapy_dispaly="\n�����"+therapy_dispaly+"\n";
						int m=0;
						for (int j=0;j<9;j++)
						{
							if (Tran_score[j]>50){ //"��"�������ʵ��ж�
								Habitus_dispaly=Habitus_dispaly+Habitus[j]+";";
								therapy_dispaly+=therapy_chinesemed[j];
								m++;
							}
							else if (Tran_score[j]>=30&&Tran_score[j]<=49){//"������"�������ʵ��ж�
								Habitus_dispaly=Habitus_dispaly+Habitus[j]+"������;";
								therapy_dispaly+=therapy_chinesemed[j];
								m++;
							}
						}
						if(m==0){//�������κ�һ���������ʵ��ж�
							Habitus_dispaly+="��̫������,���úô���,��Ϊ�Ҳ�֪��.";
							therapy_dispaly="";
						}	
						Fifth_mian.Chi_content=Habitus_dispaly+therapy_dispaly;
					}
					
					PopupAlertDialog();

				}
				else{
					i=i+1;//�ӱ���Ŀ����
					question.setText(Question_intent[length]);//��ʾ�ӱ���Ŀ
				}				
			
			}
		}
	}
	private void Computerscore() {//�����ӱ��ֵܷ�ת����
		Tran_score[kk]=(float)(score-i)/(i*5-i)*100;
		kk++;
	}	
	private void getvalue(){//��ȡ��ֵ
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
				temp[i]=0;///����������ͬ��ȥ�ϴε�ֵΪ���β���ֵ		
				}
	}
	private void PopupAlertDialog() {//�������,̽���жϽ���Ի���
		  new AlertDialog.Builder(fifth_activity_subhealth.this)
	      /*�������ڵ�����ͷ����*/
	      .setTitle(R.string.sub_result)
	      /*���õ������ڵ�ͼʽ*/
	      .setIcon(R.drawable.bk2) 
	      /*���õ������ڵ���Ϣ*/
	      .setMessage(Habitus_dispaly+"��")
	      .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
	      		{
	    	  		public void onClick(DialogInterface dialoginterface, int ii)
	    	  		{   
	    	  			finish();/*�رմ���*/
	    	  		}
	      		}
	    		  			)
	      /*���õ������ڵķ����¼�*/
	      .setNegativeButton(R.string.sub_result_retry, new DialogInterface.OnClickListener()
	      {
	    	  public void onClick(DialogInterface dialoginterface, int ii)   {
					length=0;//���³�ʼ��
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
