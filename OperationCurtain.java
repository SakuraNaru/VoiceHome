package com.jlu.voicehome;

import android.os.Handler;

import java.net.DatagramSocket;

/**
 * Created by ErRa on 2016/3/6.
 */
public class OperationCurtain implements Operation{
    @Override
    public void mOpen( String addr) {
        UdpSend CurtainThread =new UdpSend();
        byte[] operation = {0x01, 0x00};
        CurtainThread.setCommand((byte)0x01, addr, operation);
        new Thread(CurtainThread).start();
    }

    @Override
    public void mOff( String addr) {
        UdpSend CurtainThread =new UdpSend();
        byte[] operation = {0x02, 0x00};
        CurtainThread.setCommand((byte)0x01, addr, operation);
        new Thread(CurtainThread).start();
    }

    @Override
    public void mUp( String addr, int start, int value) {

    }

    @Override
    public void mDown(String addr, int start, int value) {

    }

    @Override
    public void mStop(String addr) {
        UdpSend CurtainThread =new UdpSend();
        byte[] operation = {0x03, 0x00};
        CurtainThread.setCommand((byte)0x01, addr, operation);
        new Thread(CurtainThread).start();
    }
}
