package com.example.nathen_pc.sensorinfor.SensorInformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;

import java.io.IOException;


public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text2=(TextView)findViewById(R.id.textView2);

        text2.setMovementMethod(ScrollingMovementMethod.getInstance());

        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        final FileHelper helper1;

        helper1 = new FileHelper(getApplicationContext());


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setText("\nSD卡是否存在:" + helper1.hasSD()+"\nSD卡路径:" + helper1.getSDPATH()+"\n");

                boolean truth=helper1.isthereFile("data.txt");

                if (truth==false){
                    try {
                        String b=text2.getText().toString();
                        text2.setText(b+"创建文件："+ helper1.createSDFile("data.txt").getAbsolutePath()+"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    helper1.writeSDFile("以下为测试内容!"+"\n", "data.txt",truth);
                }
                else{
                    String c=text2.getText().toString();
                    text2.setText(c+"续写文件测试开始！"+"\n");
                    helper1.writeSDFile("以下为续写文件测试内容！"+"\n", "data.txt",truth);
                }
            }

        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=text2.getText().toString();
                text2.setText(a+"删除文件是否成功:"+ helper1.deleteSDFile("data.txt")+"\n");
            }

        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,SecondActivity.class);
                MainActivity.this.startActivity(intent);
            }

        });


    }


}