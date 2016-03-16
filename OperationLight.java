package com.jlu.voicehome;

import android.os.Handler;
import android.util.Log;

import java.net.DatagramSocket;

/**
 * Created by ErRa on 2016/3/6.
 */
public class OperationLight implements Operation{

    @Override
    public void mOpen(String addr) {


        UdpSend LightThread = new UdpSend();
        byte[] operation = {0x01, 0x00};
        LightThread.setCommand((byte) 0x02, addr, operation);
        new Thread(LightThread).start();


    }

    @Override
    public void mOff( String addr) {
        UdpSend LightThread = new UdpSend();
        byte[] operation = {0x00, 0x00};
        LightThread.setCommand((byte)0x02, addr, operation);
        new Thread(LightThread).start();
    }

    @Override
    public void mUp( String addr,int start,int value) {
        if (start>10)
        {
            //灯已经是最亮的了
        }
        else
        {
            UdpSend LightThread = new UdpSend();
            byte tmp=(byte)(start+value);
            if (tmp>0x0A)
                tmp=0x0A;
            byte[] operation = {0x02, tmp};
            LightThread.setCommand((byte)0x02, addr, operation);
            new Thread(LightThread).start();
        }

    }

    @Override
    public void mDown( String addr,int start,int value) {
        if (start<=0)
        {
            //灯已经关了
        }
        else
        {
            UdpSend LightThread = new UdpSend();
            byte tmp=(byte)(start-value);
            if (tmp<0)
                tmp=0;
            byte[] operation = {0x02, tmp};
            LightThread.setCommand((byte)0x02, addr, operation);
            new Thread(LightThread).start();
        }
    }

    @Override
    public void mStop(String addr) {

    }
}
