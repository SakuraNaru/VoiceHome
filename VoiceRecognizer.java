package com.jlu.voicehome;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ErRa on 2016/3/4.
 */
public class VoiceRecognizer {

    private String mResult="";
    private Boolean mIfFinish=false;
    //private Apply3d mApply;
    public void setIfFinish()
    {
        mIfFinish=false;
    }
    public boolean getIfFinish()
    {
        return mIfFinish;
    }
    public VoiceRecognizer (Context context)
    {
        //mApply=apply;
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(context , null);
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        mIat.startListening(mRecoListener);

    }

    public RecognizerListener mRecoListener = new RecognizerListener() {

        public void onResult(RecognizerResult results, boolean isLast) {
            //Log.d("Result:", results.getResultString());
            if(isLast == false){
/*
                if (test.equals(readJson(results.getResultString())))
                    sendOrder(0,listAddr[1][1]);
                if (test_close.equals(readJson(results.getResultString())))
                    sendOrder(1,listAddr[1][1]);
                if (test_up.equals(readJson(results.getResultString())))
                    sendOrder(2,listAddr[1][1]);
                if (test_down.equals(readJson(results.getResultString())))
                    sendOrder(3,listAddr[1][1]);
                if (open_curtain.equals(readJson(results.getResultString())))
                    sendOrder(4,listAddr[0][1]);
                if (close_curtain.equals(readJson(results.getResultString())))
                    sendOrder(5,listAddr[0][1]);
                if (stop_curtain.equals(readJson(results.getResultString())))
                    sendOrder(6,listAddr[0][1]);
                rtn_string.setText(readJson(results.getResultString()));*/
                mResult=readJson(results.getResultString());
                mIfFinish=true;
                Log.v("test",mResult);

            }
        }

        public void onError(SpeechError error) {
            error.getPlainDescription(true);
        }

        public void onBeginOfSpeech() {
            //rtn_string.setText("begin");
        }

        public void onVolumeChanged(int volume) {
        }

        public void onEndOfSpeech() {
        }

        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };
    public String readJson(String json)
    {
        String sum="";
        try {
            JSONObject root=new JSONObject(json);
            JSONArray array=root.getJSONArray("ws");
            for (int i=0;i<array.length();i++)
            {
                JSONObject lan=array.getJSONObject(i);
                JSONArray ch=lan.getJSONArray("cw");
                JSONObject w=ch.getJSONObject(0);
                //Log.v("JSON",ch.getString("w"));
                sum+=w.getString("w");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sum;
    }
    public String sendResult()
    {
        return mResult;
    }
}
