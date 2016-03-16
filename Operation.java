package com.jlu.voicehome;

import android.os.Handler;

import java.net.DatagramSocket;

/**
 * Created by ErRa on 2016/3/6.
 */
public interface Operation {
    public void mOpen(String addr);
    public void mOff(String addr);
    public void mUp(String addr,int start,int value);//给出起始值和增长量
    public void mDown(String addr,int start,int value);
    public void mStop(String addr);
}
