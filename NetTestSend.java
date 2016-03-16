package com.jlu.voicehome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
 * Created by ErRa on 2016/3/8.
 */
public class NetTestSend implements Runnable{

    byte[] buf_recv;
    DatagramSocket udp = null;
    String Ip, Port;
    private static int TEST_CODE=3;
    public Handler comHandler;

    public void setIP(String ip, String port,Handler mHandler)
    {
        this.Ip = ip;
        this.Port = port;
        comHandler=mHandler;
    }
        @Override
        public void run() {
            //
            try
            {
                udp = new DatagramSocket();
                byte[] msg = {0x40, 0x04,0x00, 0x44};
                // remote host address
                //Inet4Address address = (Inet4Address) Inet4Address.getByName(getResources().getString(R.string.defaultIP));
                Inet4Address address = (Inet4Address) Inet4Address.getByName(Ip);
                // data package
                DatagramPacket packet = new DatagramPacket( msg, msg.length, address, Integer.parseInt(Port)); //remote addr, port
                // send data
                udp.send(packet);
                //Toast.makeText(MainActivity.this, "send:" + msg, Toast.LENGTH_SHORT).show();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                //Looper.prepare();
                //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                //Looper.loop();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                //Looper.prepare();
                //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                //Looper.loop();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //Looper.prepare();
                //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                //Looper.loop();
            }



            buf_recv = new byte[1024];
            DatagramPacket pack = new DatagramPacket(buf_recv,buf_recv.length);
            Message msg = comHandler.obtainMessage(TEST_CODE);
            Bundle data = new Bundle();
            while (true) {
                try {
                    udp.receive(pack); // 接收消息
                    CharsetDecoder decoder = Charset.forName("gb2312").newDecoder(); // gb2312
                    String str = decoder.decode(ByteBuffer.wrap(buf_recv)).toString();  // 转换的字符串附加到消息上
                    //Log.v("testre",str);
                    data.putString("Connected", str);
                    data.putString("Ip",this.Ip);
                    data.putString("Port", this.Port);
                    msg.setData(data);

                    //handler.removeMessages(0);
                    comHandler.sendMessage(msg); // 发送消息
                    //Arrays.fill(buf_recv, (byte) 0); // 清零
                    udp.close();
                    break;
                } catch (IOException e) {
                    //Looper.prepare();
                    //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //Looper.loop();
                }
            }
        }

}


