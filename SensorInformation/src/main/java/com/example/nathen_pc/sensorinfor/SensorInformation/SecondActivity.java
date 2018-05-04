package com.example.nathen_pc.sensorinfor.SensorInformation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {

    private Button btnStart;
    private Button btnBack;
    private TextView view1;
    private TextView viewT;
    private TextView viewP;
    private TextView viewH1;
    private TextView viewH2;
    private TextView viewL;
    private TextView viewM;
    private ServerSocket serverSocket;
    private BufferedReader in;
    public String output;

    private static Handler hander = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            String s = (String)msg.obj;
        }

    };

    public static String numToHex8(int b) {
        return String.format("%02x", b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btnStart = (Button)findViewById(R.id.button11);
        view1=(TextView)findViewById(R.id.textView4);
        view1.setMovementMethod(ScrollingMovementMethod.getInstance());

        viewT=(TextView)findViewById(R.id.textView6);
        viewP=(TextView)findViewById(R.id.textView15);
        viewH1=(TextView)findViewById(R.id.textView12);
        viewH2=(TextView)findViewById(R.id.textView16);
        viewL=(TextView)findViewById(R.id.textView14);
        viewM=(TextView)findViewById(R.id.textView17);

        btnBack=(Button)findViewById(R.id.button12);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new ServerThread().start();//在新线程中启动SocketServer...
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.this.finish();
            }

        });

    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }

    private class ServerThread extends Thread {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            try {
                serverSocket = new ServerSocket(6000);
                while (true) {
                    Socket clientSocket = serverSocket.accept();//阻塞等待处理...
                    String remoteIP = clientSocket.getInetAddress().getHostAddress();
                    int remotePort = clientSocket.getLocalPort();

                    in = new BufferedReader(new InputStreamReader(
                            clientSocket.getInputStream()));

                    // 获得 client 端发送的数据
                    int  a=1;
                    int i=0;
                    String tmp="";
                    int Temperature=0,Pressure=0,Humidity=0,Horizon=0,Light=0,PM=0;
                    int n1=0,n2=0,n3=0,n4=0,n5=0,n6=0,n7=0,n8=0;
                    while (a!=1000) {
                        //打印出原始数据
                        a = in.read();
                        tmp= Integer.toHexString(a);
                        i=i+1;
                        if(tmp.equals("5")&&(i%4==1))
                            tmp="\n"+tmp;
                        if(i%2==1){
                            tmp=" "+tmp;
                        }
                        else
                            tmp=tmp+" ";
                        output = view1.getText() + tmp;


                        view1.setText(output);

                        //写入SD卡 原始数据
                        final FileHelper File1;
                        File1 = new FileHelper(getApplicationContext());


                        boolean truth=File1.isthereFile("data.txt");

                        if (truth==false){
                            File1.writeSDFile(tmp, "data.txt",truth);
                        }
                        else{
                            File1.writeSDFile(tmp, "data.txt",truth);
                        }

                        //温度显示
                        if ((i%40>=5)&&(i%40<=8)){
                            Temperature=Temperature*16+a;
                            if(i%40==8){
                                n1=Temperature/100;
                                n2=Temperature%100;
                                //viewT.setText("实时温度为："+Integer.toString(n1)+"."+Integer.toString(n2)+"℃");
                                viewT.setText("实时温度为："+Integer.toString(n1)+"."+Integer.toString(n2)+"℃");
                                //    Temperature=0;
                            }
                        }

                        //气压显示
                        if ((i%40>=9)&&(i%40<=16)){
                            Pressure=Pressure*16+a;
                            if(i%40==16){
                                n3=Pressure/100;
                                n4=Pressure%100;
                                viewP.setText("实时气压为："+Integer.toString(n3)+"."+Integer.toString(n4)+" Pa");
                                //    Pressure=0;
                            }
                        }

                        //湿度显示
                        if ((i%40>=17)&&(i%40<=20)){
                            Humidity=Humidity*16+a;
                            if(i%40==20){
                                // int n1,n2;
                                n5=Humidity/100;
                                n6=Humidity%100;
                                viewH1.setText("实时湿度为："+Integer.toString(n5)+"."+Integer.toString(n6)+"%");
                                //    Humidity=0;
                            }
                        }

                        //海拔显示
                        if ((i%40>=21)&&(i%40<=24)){
                            Horizon=Horizon*16+a;
                            if(i%40==24){
                                viewH2.setText("实时海拔为："+Integer.toString(Horizon)+" m");
                                //    Horizon=0;
                            }
                        }

                        //光强显示
                        if ((i%40>=25)&&(i%40<=32)){
                            Light=Light*16+a;
                            if(i%40==32){
                                //int n1,n2;
                                n7=Light/100;
                                n8=Light%100;
                                viewL.setText("实时光强为："+Integer.toString(n7)+"."+Integer.toString(n8)+" lux");
                                //    Light=0;
                            }
                        }

                        //颗粒物浓度显示
                        if ((i%40>=33)&&(i%40<=36)){
                            PM=PM*16+a;
                            if(i%40==36){
                                viewM.setText("实时颗粒物浓度为："+Integer.toString(PM)+" UG/m^3");
                                //    PM=0;
                            }
                        }
                        if (i%40==0){

                            SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");
                            Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
                            String    str    =    formatter.format(curDate);

                            str=str+"  "+"实时温度为："+Integer.toString(n1)+"."+Integer.toString(n2)+"℃";
                            str=str+"  "+"实时气压为："+Integer.toString(n3)+"."+Integer.toString(n4)+" Pa";
                            str=str+"  "+"实时湿度为："+Integer.toString(n5)+"."+Integer.toString(n6)+"%";
                            str=str+"  "+"实时海拔为："+Integer.toString(Horizon)+" m";
                            str=str+"  "+"实时光强为："+Integer.toString(n7)+"."+Integer.toString(n8)+" lux";
                            str=str+"  "+"实时颗粒物浓度为："+Integer.toString(PM)+" UG/m^3"+"\n";

                            final FileHelper File2;
                            File2 = new FileHelper(getApplicationContext());

                            boolean truth2=File1.isthereFile("data-real.txt");

                            File2.writeSDFile(str, "data-real.txt",truth2);

                            String Str1=Integer.toString(n1)+"."+Integer.toString(n2)+"  ";
                            Str1=Str1+Integer.toString(n3)+"."+Integer.toString(n4)+"  ";
                            Str1=Str1+Integer.toString(n5)+"."+Integer.toString(n6)+"  ";
                            Str1=Str1+Integer.toString(Horizon)+"  ";
                            Str1=Str1+Integer.toString(n7)+"."+Integer.toString(n8)+"  ";
                            Str1=Str1+Integer.toString(PM)+"\n";

                            final FileHelper File3;
                            File3 = new FileHelper(getApplicationContext());

                            boolean truth3=File1.isthereFile("data-pure.txt");

                            File3.writeSDFile(Str1, "data-pure.txt",truth3);

                            Temperature=0;
                            Pressure=0;
                            Humidity=0;
                            Horizon=0;
                            Light=0;
                            PM=0;
                        }


                    }
                    // 关闭各个流
                    //   out.close();
                    //  in.close();
                    Message message = hander.obtainMessage();
                    message.obj = tmp;
//                    hander.sendMessage(message);
                    //                   try {
                    //                       Thread.sleep(100);
                    //                   } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
//                        e.printStackTrace();
                    //                  }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}

