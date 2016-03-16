package com.jlu.voicehome;

import android.content.Context;
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
public class UdpSend implements Runnable{
    private Context context;
    public Handler comHandler;
    private DatagramSocket comUDP = null;

    private String IP = "123.57.34.103";
    private String Port = "9001";
    private static int DATA_CODE=2;
    private boolean receive=false;

    //private boolean boolRefresh = false;

    //    private int type;    //
//    private byte[] address;
//    private int operation;
    byte[] buf_send;


    public void setCommand(byte t, String addr, byte[] op)
    {
        byte[] buf = new byte[(7 + op.length)];
        //byte[] buf = {0x40, 0x09,0x02, (byte)0x91, (byte)0xFE, 0x45, (byte)0x02, 0x01, 0x00};
        //             头    长度  类型     检验              地址      命令长度       命令
        int shAddr = Integer.parseInt(addr, 16);
        buf[1] = (byte)(7 + op.length);
        buf[2] = t;
        buf[3] = 0x00;
        buf[4] = (byte)(shAddr);
        buf[5] = (byte)(shAddr >> 8);
        buf[6] = (byte) op.length;
        for (int i = 0; i < op.length; i++)
        {
            buf[7+i] = op[i];
        }
        byte sum = 0;
        for (int i = 0; i < buf.length; i++)
        {
            sum += buf[i];
        }
        buf[3] = sum;
        this.buf_send = new byte[buf.length];
        System.arraycopy(buf, 0, this.buf_send, 0, buf.length);
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
            Log.i("com", "send");

            //Toast.makeText(MainActivity.this, "send:" + msg, Toast.LENGTH_SHORT).show();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Looper.prepare();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }


        comUDP.close();
    }

   /* public void receive()
    {
        this.buf_send = new byte[]{0x40, 0x05, 0x0E, 0x54, 0x01};

        receive=true;
    }*/

}
