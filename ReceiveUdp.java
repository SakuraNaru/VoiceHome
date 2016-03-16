package com.jlu.voicehome;

import android.os.Handler;

import java.net.DatagramSocket;

/**
 * Created by ErRa on 2016/3/6.
 */
public class ReceiveUdp {
    public ReceiveUdp(Handler mHandler)
    {
        UdpReceive receive=new UdpReceive(mHandler);
        new Thread(receive).start();
    }
}
