package com.jlu.voicehome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

/**
 * Created by ErRa on 2016/3/3.
 */
public class UdpReceive implements Runnable{
    private DatagramSocket comUDP = null;
    public Handler comHandler;
    private String IP = "123.57.34.103";
    private String Port = "9001";
    private static int DATA_CODE=2;
    private byte []buf_send ={0x40, 0x05, 0x0E, 0x54, 0x01};
    //获得主线程handler
    public UdpReceive(Handler mHandler)
    {
        comHandler=mHandler;

    }
    @Override
    public void run() {
        try
        {

            comUDP = new DatagramSocket();
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //byte[] buf_send = {0x40, 0x09,0x02, (byte)0x91, (byte)0xFE, 0x45, (byte)0x02, 0x01, 0x00};
            // remote host address
            Inet4Address address = (Inet4Address) Inet4Address.getByName(IP);

            // data package
            DatagramPacket packet = new DatagramPacket( buf_send, buf_send.length, address, Integer.parseInt(Port)); //remote addr, port
            // send data
            comUDP.send(packet);


            //Toast.makeText(MainActivity.this, "send:" + msg, Toast.LENGTH_SHORT).show();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }


            byte[] buf_recv = new byte[1024];
            DatagramPacket pack = new DatagramPacket(buf_recv, buf_recv.length);
            Message comMsg = comHandler.obtainMessage(DATA_CODE);
            Bundle data = new Bundle();
            while (true) {
                try {
                    Log.d("Refresh", "Receiving!");
                    comUDP.receive(pack); // 接收消息
                    Log.d("Refresh", "Receive!");
                    CharsetDecoder decoder = Charset.forName("gb2312").newDecoder(); // gb2312
                    String str = decoder.decode(ByteBuffer.wrap(buf_recv)).toString();  // 转换的字符串附加到消息上
                    Log.v("hehe", str);
                    data.putString("State", str);
                    comMsg.setData(data);
                    //comHandler.removeMessages(0);
                    comHandler.sendMessage(comMsg); // 发送消息

                    //Arrays.fill(buf_recv, (byte) 0); // 清零

                    break;

                } catch (IOException e) {
                    Log.e("UDP", "Receive error");
                }
            }

        comUDP.close();
    }
}
