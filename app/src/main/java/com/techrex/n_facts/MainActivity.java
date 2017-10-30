package com.techrex.n_facts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Context ctx;
    TextView factTv;
    ProgressDialog progressDialog;
    String strDate;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ctx=this;
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading...");
        factTv=(TextView)findViewById(R.id.factOfTheDayTV);
        progressDialog.show();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        strDate = formatter.format(date);
        new Get_Fact().execute();
    }
    public void getFact(View view)
    {
      //  progressDialog.show();
        new Get_Fact().execute();
    }
    public void viewCatagoryRandom(View view)
    {
        type="random";
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.catagory, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        AlertDialog alertDialog;
        alertDialog=builder.create();
        alertDialog.setView(promptView);
        alertDialog.show();
    }
    public void viewcategoryQuest(View view)
    {
        type="quest";
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.catagory, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        AlertDialog alertDialog;
        alertDialog=builder.create();
        alertDialog.setView(promptView);
        alertDialog.show();
    }
    public void trivia(View view)
    {
        Intent intent;
        if(type.contains("random"))
            intent=new Intent(this,RandomFacts.class);
        else
            intent=new Intent(this,QuestFacts.class);
        intent.putExtra("category","trivia");
        startActivity(intent);
    }
    public void math(View view)
    {
        Intent intent;
        if(type.contains("random"))
            intent=new Intent(this,RandomFacts.class);
        else
            intent=new Intent(this,QuestFacts.class);
        intent.putExtra("category","math");
        startActivity(intent);
    }
    public void date(View view)
    {
        Intent intent;
        if(type.contains("random"))
            intent=new Intent(this,RandomFacts.class);
        else
            intent=new Intent(this,QuestFacts.class);
        intent.putExtra("category","date");
        startActivity(intent);
    }
    public void year(View view)
    {
        Intent intent;
        if(type.contains("random"))
            intent=new Intent(this,RandomFacts.class);
        else
            intent=new Intent(this,QuestFacts.class);
        intent.putExtra("category","year");
        startActivity(intent);
    }
    class Get_Fact extends AsyncTask<Void,Void,String>
    {
        String fullurl="http://numbersapi.com/"+strDate+"/date";

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpURLConnection con= (HttpURLConnection) new URL(fullurl).openConnection();
                con.setConnectTimeout(10000);
                InputStream in=con.getInputStream();
                Reader reader=new InputStreamReader(in,"UTF-8"); // char[] buffer=new char[];
                BufferedReader br = new BufferedReader(reader);
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append("\n");
                        line = br.readLine();
                    }
                    return sb.toString();
                } finally {
                    br.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "NFacts_E404";
        }

        @Override
        protected void onPostExecute(String s) {
            if(!((Activity) ctx).isFinishing()) {
                if(s.contains("NFacts_E404"))
                {
                    new AlertDialog.Builder(ctx)
                            .setTitle("Unable to connect")
                            .setMessage("Error: " + s + ", Please try again later")
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    }
                            )
                            .show();

                    factTv.setText("Unable to connect.\nPlease check your network conection!!");
                    progressDialog.dismiss();
                }
                else
                {
                    factTv.setText(s);
                    progressDialog.dismiss();
                }

            }
        }



    }
}
