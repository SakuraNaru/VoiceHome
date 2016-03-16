package com.jlu.voicehome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private ImageView light_one_before;
    private ImageView light_one_after;
    private ImageView light_one_bright;

    private ImageView light_two_before;
    private ImageView light_two_after;
    private ImageView light_two_bright;

    private ImageView curtain_one_before;
    private ImageView curtain_one_after;
    private ImageView curtain_one_bright;

    private ImageView curtain_two_before;
    private ImageView curtain_two_after;
    private ImageView curtain_two_bright;

    private TextView talk;
    private Apply3d mApply1;
    private Apply3d mApply2;
    private Apply3d mApply3;
    private Apply3d mApply4;


    View mContainer1 = null;
    View mContainer2 = null;
    View mContainer3 = null;
    View mContainer4 = null;

    public String mResult;
    private String test1="把灯打开";
    private String test2="把灯关闭";
    private String test_up="把灯调亮";
    private String test_down="把灯调暗";
    private String test3="把窗帘打开";
    private String test4="把窗帘关闭";

    private static int MSG_CODE=1;
    private static int DATA_CODE=2;
    private static int TEST_CODE=3;

    private String mAddressLight;
    private String mAddressCurtain;
    private String[][] listAddr= new String[5][];
    private String[][] listName = new String[5][];
    private String[][] listValue = new String[5][5];
    private String[] strNames;
    private String[] strData;
    private int level;
    private int deep=0;
    private int state=0;

    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==MSG_CODE)//收到语音识别完成的消息
            {
                //change local code
                //afterRecognizeVoice();
                afterRecognize();
            }
            if (msg.what==DATA_CODE)//收到灯状态识别完的消息
            {
                receiveState(msg);
                //afterRecognizeVoice();

                mApply2.DeeperImage(Integer.parseInt(listValue[0][1]));
            }
            if (msg.what==TEST_CODE)//收到连接成功的消息
            {
                recognize(msg);
                //afterRecognizeVoice();
                //Log.v("address",listAddr[1][1]);
                //Log.v("address", listAddr[0][1]);
            }
        }
    };
    private void recognize(Message msg)
    {

        Bundle data = msg.getData();
        String val = data.getString("Connected");
        if (val.getBytes()[0] == 0x40)
        {

            Bundle bNamebundle = new Bundle();
            strNames = val.split("\u0000");
            byte count;
            byte type = 0;
            for(int i = 1; i < strNames.length; )
            {
                count = strNames[i].getBytes()[0];
                //Log.e("TEST",count+"0000");
                strNames[i] = strNames[i].substring(1);
                listName[type] = new String[count];
                listAddr[type] = new String[count];
                for(int j = 0; j < count; j++)
                {
                    listName[type][j] = (strNames[i]);
                    i++;
                }
                for(int j = 0; j < count; j++)
                {
                    listAddr[type][j] = (strNames[i]);
                    i++;
                }

                type++;

            }
        }

    }
    private void receiveState(Message msg)
    {

        Bundle data = msg.getData();
        String val = data.getString("State");
        strData = val.split("\u0000");
        int k=0;
        /*for (int i=0;i<strData.length;i++)
        {
            Log.v("test",i+"  "+strData[i]);
        }*/

        for (int i=3;i<strData.length;i=i+3)
        {
            listValue[0][k]=(strData[i]);
            //Log.v("test",i+"");
            k++;
        }
        level=Integer.parseInt(listValue[0][1]);
        //Log.v("test",listValue[0][1]);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences login = getSharedPreferences("Date", Activity.MODE_PRIVATE);
        editor=login.edit();
        if (login.getBoolean("First",true))
        {
            editor.putString("User","admin");
            editor.putString("Password","root");
            editor.putBoolean("First",false);
            editor.commit();
        }
        if (!login.getBoolean("LoginState",false))
        {
            Intent intent=new Intent(this,LoginActivity.class);
            startActivityForResult(intent, 0);
        }

        light_one_before=(ImageView)findViewById(R.id.light_one_before);
        light_one_after=(ImageView)findViewById(R.id.light_one_after);
        light_one_bright=(ImageView)findViewById(R.id.light_one_bright);
        mContainer1=findViewById(R.id.continer_light_one);

        light_two_before=(ImageView)findViewById(R.id.light_two_before);
        light_two_after=(ImageView)findViewById(R.id.light_two_after);
        light_two_bright=(ImageView)findViewById(R.id.light_two_bright);
        mContainer2=findViewById(R.id.continer_light_one);

        curtain_one_before=(ImageView)findViewById(R.id.curtain_one_before);
        curtain_one_after=(ImageView)findViewById(R.id.curtain_one_after);
        curtain_one_bright=(ImageView)findViewById(R.id.curtain_one_bright);
        mContainer3=findViewById(R.id.continer_light_one);

        curtain_two_before=(ImageView)findViewById(R.id.curtain_two_before);
        curtain_two_after=(ImageView)findViewById(R.id.curtain_two_after);
        curtain_two_bright=(ImageView)findViewById(R.id.curtain_two_bright);
        mContainer4=findViewById(R.id.continer_light_one);

        talk=(TextView)findViewById(R.id.talk);
        //mStartAnimView = mImageView1;
        mApply1=new Apply3d(mContainer1,light_one_before,light_one_after,light_one_bright);
        mApply2=new Apply3d(mContainer2,light_two_before,light_two_after,light_two_bright);
        mApply3=new Apply3d(mContainer3,curtain_one_before,curtain_one_after,curtain_one_bright);
        mApply4=new Apply3d(mContainer4,curtain_two_before,curtain_two_after,curtain_two_bright);


        //light_two_after.setBackgroundColor(Color.rgb(255,255,255));
        //创建链接
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=556d4a86");
        NetTestSend mNetTest=new NetTestSend();
        mNetTest.setIP("123.57.34.103", "9001", mHandler);
        new Thread(mNetTest).start();


        findViewById(R.id.voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                //new OperationLight().mOpen(listAddr[1][1]);
                //new ReceiveUdp(mHandler);
                //new OperationLight().mOff(listAddr[1][1]);
                final VoiceRecognizer mVoiceRecognizer = new VoiceRecognizer(MainActivity.this);


                new Thread() {
                    @Override
                    public void run() {
                        while (!mVoiceRecognizer.getIfFinish()) {

                        }
                        mResult = mVoiceRecognizer.sendResult();
                        Message msg = mHandler.obtainMessage(MSG_CODE);
                        msg.sendToTarget();
                        //Log.v("haha", mResult);
                        mVoiceRecognizer.setIfFinish();
                        // afterRecognize();
                    }
                }.start();*/
/*
                if (deep<10)
                    deep++;
                refreshUi(1, 3, deep);
*/
                refreshUi(1,1,0);
            }
        });
        findViewById(R.id.deep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                if (deep>0)
                    deep--;
                refreshUi(1,2,deep);
*/
                //Log.v("DEEP",deep+"");
                refreshUi(1,0,10);
            }
        });
        /*findViewById(R.id.receive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReceiveUdp(mHandler);//获取当前灯状态

            }
        });*/

    }
    private void afterRecognize()
    {
        talk.setText(mResult);//关灯状态 可以开灯和调亮
        if (test1.equals(mResult))
            mApply1.applyRotation(light_one_before, 0, 90);
        if (test2.equals(mResult))
            mApply1.applyRotation(light_one_before, 0, 90);
    }
    private void refreshUi(int num,int type,int now)//num为操作设备的编号,type为操作类型  0关 1开 2调暗 3调亮
    {                                               //now为设备当前状态 0关---10最亮
        if (now==0)
        {
            if (type==1)
            {
                switch (num)
                {
                    case 1:
                        mApply1.applyRotation(light_one_before, 0, 90);  //打开1号设备
                        break;
                    case 2:
                        mApply2.applyRotation(light_two_before, 0, 90);  //打开2号设备
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
            }
            else if (type==3)
            {
                switch (num)
                {
                    case 1:
                        mApply1.applyRotation(light_one_before, 0, 90);  //打开1号设备
                        mApply1.DeeperImage(now);
                        break;
                    case 2:
                        mApply2.applyRotation(light_two_before, 0, 90);  //打开2号设备
                        mApply2.DeeperImage(now);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
            }
            else
            {
                Toast.makeText(this,"灯已经关了",Toast.LENGTH_SHORT).show();
            }
        }
        else if (now==10)
        {
            if (type==0)
            {
                switch (num)
                {
                    case 1:
                        mApply1.applyRotation(light_one_after, 0, 90);  //关闭1号设备
                        break;
                    case 2:
                        mApply2.applyRotation(light_two_after, 0, 90);  //关闭2号设备
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
            }
            else if (type==2)
            {
                switch (num)
                {
                    case 1:
                        mApply1.DeeperImage(now); //调暗1号设备
                        break;
                    case 2:
                        mApply1.DeeperImage(now); //调暗2号设备
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
            }
            else
            {
                Toast.makeText(this,"灯已经是最亮的了",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (type==0)
            {
                switch (num)
                {
                    case 1:
                        mApply1.applyRotation(light_one_after, 0, 90);  //关闭1号设备
                        break;
                    case 2:
                        mApply2.applyRotation(light_two_after, 0, 90);  //关闭1号设备
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
            }
            else if (type==2)
            {
                switch (num)
                {
                    case 1:
                        mApply1.DeeperImage(now); //调暗1号设备
                        break;
                    case 2:
                        mApply2.DeeperImage(now); //调暗1号设备
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
            }
            else if (type==3)
            {
                switch (num)
                {
                    case 1:
                        mApply1.DeeperImage(now); //调亮1号设备
                        break;
                    case 2:
                        mApply2.DeeperImage(now); //调亮2号设备
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
            }
            else
            {
                Toast.makeText(this,"灯已经打开了",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void afterRecognizeVoice()
    {
        if (!mApply1.getmIfOn())//关灯状态 可以开灯和调亮
        {
            if (test1.equals(mResult))
            {
                new OperationLight().mOpen(listAddr[1][1]);
                new ReceiveUdp(mHandler);
            }

            if (test_up.equals(mResult)){
                new OperationLight().mUp(listAddr[1][1],level,1);
                new ReceiveUdp(mHandler);
            }
            if (test_down.equals(mResult))
            {
                new OperationLight().mDown(listAddr[1][1], level, 1);
                new ReceiveUdp(mHandler);
            }
        }
        else //开灯状态 可以关灯和调暗
        {
            if (test2.equals(mResult))
            {
                new OperationLight().mOff(listAddr[1][1]);
                new ReceiveUdp(mHandler);
            }
            if (test_up.equals(mResult)){
                new OperationLight().mUp(listAddr[1][1],level,1);
                new ReceiveUdp(mHandler);
            }
            if (test_down.equals(mResult))
            {
                new OperationLight().mDown(listAddr[1][1], level, 1);
                new ReceiveUdp(mHandler);
            }

        }
        if (!mApply3.getmIfOn())//关闭窗帘状态
        {
            if (test3.equals(mResult))
                new OperationCurtain().mOpen(listAddr[0][1]);

        }
        else //拉开窗帘状态
        {
            if (test4.equals(mResult))
                new OperationCurtain().mOff(listAddr[0][1]);
        }
    }


}
