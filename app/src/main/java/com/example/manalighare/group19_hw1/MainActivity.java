/*
************************ Assignment #HOMEWORK 01 *******************************************
*********************** File Name- activity_main.xml *************************************
************************ Full Name- 1. Manali Ghare 2. Anup Deshpande (Group 19) ***********

*/

package com.example.manalighare.group19_hw1;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView verdict;
    private TextView Percntage_label;
    private TextView result;

    private EditText weight;

    private SeekBar seekBar;
    private ProgressBar progressBar;

    private Switch gender;

    private Button save_btn;
    private Button add_drink;
    private Button reset_btn;

    private RadioGroup drink_size_group;


    private double wt=0;
    private double alco_perct=5;
    private  double r,A;
    private double BAC=0;

    private int gender_value;
    private int drink_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("BAC Calculator");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_foreground);

        verdict=(TextView)findViewById(R.id.verdict);
        Percntage_label=(TextView)findViewById(R.id.percent_label);
        result=(TextView)findViewById(R.id.result);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        gender=(Switch)findViewById(R.id.gender_switch);
        weight=(EditText)findViewById(R.id.weight);
        save_btn=(Button)findViewById(R.id.save_btn);
        add_drink=(Button)findViewById(R.id.add_btn);
        reset_btn=(Button)findViewById(R.id.reset_btn);
        drink_size_group=(RadioGroup)findViewById(R.id.RadioGroup);

        save_btn.setOnClickListener(this);
        add_drink.setOnClickListener(this);
        reset_btn.setOnClickListener(this);

        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setColor(getResources().getColor(R.color.Green));
        verdict.setTextColor(Color.WHITE);

        gradientDrawable.setCornerRadius(15.0f);
        verdict.setBackground(gradientDrawable);
        gender.setTextOff("F");
        gender.setTextOn("M");
        result.setText(result.getText()+" 0.00");
        seekBar.setProgress(1);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Percntage_label.setText(""+(progress)+"%");
                    alco_perct=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        drink_size_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected = (RadioButton) findViewById(checkedId);
                String tmp=selected.getText().toString();
                tmp=tmp.substring(0,tmp.indexOf(" "));
                drink_size=Integer.parseInt(tmp);

            }
        });

}


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.save_btn){


            if(gender.isChecked()){

                gender_value=1;
                r=0.68;
            }else{
                gender_value=0;
                r=0.55;
            }

            try{
                wt=Double.parseDouble(weight.getText().toString());

            }
            catch (Exception e){
                weight.setError("Enter the weight in lb");
            }

            reset_fun();

        } else if (v.getId() == R.id.add_btn){


            A = drink_size*alco_perct;

            if (wt!=0){

                BAC =BAC+ ((A*6.24)/(wt*r))/100;
                String tmp=result.getText().toString();
                tmp=tmp.substring(0,tmp.lastIndexOf(" "));

                DecimalFormat decimalFormat= new DecimalFormat("##.###");
                result.setText(tmp+" "+decimalFormat.format(BAC));

                int tmp2= (int) (BAC*100);
                progressBar.setProgress(tmp2);

                GradientDrawable gradientDrawable=new GradientDrawable();
                gradientDrawable.setCornerRadius(15.0f);
                String verdict_tmp=" ";

                if (BAC<0.08){

                    gradientDrawable.setColor(getResources().getColor(R.color.Green));
                    verdict_tmp="You're safe";
                }
                else if(BAC> 0.08  && BAC<=0.20){

                    gradientDrawable.setColor(getResources().getColor(R.color.Yellow));
                    verdict_tmp="Be Careful...";

                }else if(BAC>0.20 && BAC<0.25){
                    gradientDrawable.setColor(getResources().getColor(R.color.Red));
                    verdict_tmp="Over the limit!";
                }
                else if (BAC>=0.25){
                    gradientDrawable.setColor(getResources().getColor(R.color.Red));
                    verdict_tmp="Over the limit!";
                    save_btn.setEnabled(false);
                    add_drink.setEnabled(false);
                    Toast.makeText(MainActivity.this, "No more drinks for you!",Toast.LENGTH_LONG).show();

                }


                verdict.setBackground(gradientDrawable);
                verdict.setText(verdict_tmp);


                Log.d("tmp : ",""+tmp);
                Log.d("p_achl : ",""+alco_perct);
                Log.d("size : ",""+drink_size);
                Log.d("wt : ",""+wt);
                Log.d("A : ",""+A);
                Log.d("R : ",""+r);
                Log.d("msg", "BAC is: "+BAC);
                Log.d("Progress bar value : ",""+progressBar.getProgress());

            }
            else {
                weight.setError("Enter the weight in lb");
            }

        } else if(v.getId()== R.id.reset_btn){

            wt=0;
            weight.setText(" ");
            reset_fun();
        }

    }

    public void reset_fun(){
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setCornerRadius(15.0f);
        BAC=0;
        seekBar.setProgress(1);
        drink_size_group.check(R.id.r_btn1);
        Percntage_label.setText("5%");
        result.setText("BAC Level : 0.00");
        progressBar.setProgress(0);
        gradientDrawable.setColor(getResources().getColor(R.color.Green));
        verdict.setText("You're safe");
        verdict.setBackground(gradientDrawable);
        add_drink.setEnabled(true);
        save_btn.setEnabled(true);
    }
}
