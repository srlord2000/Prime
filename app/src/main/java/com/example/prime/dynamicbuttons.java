package com.example.prime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class dynamicbuttons extends AppCompatActivity implements View.OnClickListener {
    LinearLayout parent;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicbuttons);
        String[] btn_name = {"Button1","Button2","Button3","Button4"};
        parent = (LinearLayout) findViewById(R.id.ll_parent);

        for(int i=0;i<4;i++)
        {
            b1 = new Button(dynamicbuttons.this);
            b1.setId(i+1);
            b1.setText(btn_name[i]);
            b1.setTag(i);
            parent.addView(b1);
            b1.setOnClickListener(dynamicbuttons.this);

        }
    }

    @Override
    public void onClick(View view) {
        String str = view.getTag().toString();
        if(str.equals(("0")))
        {
            Toast.makeText(getApplicationContext(),"Click Button1", Toast.LENGTH_SHORT).show();
        }
        else if(str.equals("1"))
        {
            Toast.makeText(getApplicationContext(),"Click Button2", Toast.LENGTH_SHORT).show();
        }
        else if(str.equals("2"))
        {
            Toast.makeText(getApplicationContext(),"Click Button3", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Click Button5", Toast.LENGTH_SHORT).show();
        }
    }
}
