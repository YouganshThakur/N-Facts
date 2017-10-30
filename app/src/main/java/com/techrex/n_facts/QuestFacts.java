package com.techrex.n_facts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QuestFacts extends AppCompatActivity {

    String category;
    Context ctx;
    ProgressDialog progressDialog;
    String number;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_facts);
        ctx=this;
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading...");
        category=getIntent().getStringExtra("category");
        ((EditText)findViewById(R.id.numberEditText)).setHint("");
        switch (category)
        {
            case "trivia":
                ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#ff0004"));
                ((Button)findViewById(R.id.categoryButton)).setText("TRIVIA");
                ((EditText)findViewById(R.id.numberEditText)).setHint("Enter your number");
                break;
            case "math":
                ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#0004ff"));
                ((Button)findViewById(R.id.categoryButton)).setText("MATH");
                ((EditText)findViewById(R.id.numberEditText)).setHint("Enter your number");
                break;
            case "date":
                ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#ffa200"));
                ((Button)findViewById(R.id.categoryButton)).setText("DATE");
                ((EditText)findViewById(R.id.numberEditText)).setHint("Enter MM/YY");
                break;
            case "year":
                ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#47c100"));
                ((Button)findViewById(R.id.categoryButton)).setText("YEAR");
                ((EditText)findViewById(R.id.numberEditText)).setHint("Enter YEAR");
                break;

        }
    }
    public void getFact(View view)
    {
        EditText nEditText=(EditText)findViewById(R.id.numberEditText);
        number=nEditText.getText().toString();
        if(number.length()==0)
        {
            new AlertDialog.Builder(ctx)
                    .setTitle("Empty")
                    .setMessage("Please enter number")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }
                    )
                    .show();
        }
        else {
            if(category.contains("date"))
            {
                if(number.contains("/")) {
                    progressDialog.show();
                    new Get_Fact().execute();
                }
                else
                {
                    new AlertDialog.Builder(ctx)
                            .setTitle("Invalid Format")
                            .setMessage("Date should be in MM/YY format.")
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    }
                            )
                            .show();
                }
            }
            else {

                progressDialog.show();
                new Get_Fact().execute();
            }
        }
    }
    public void changeCategory(View view)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.catagory, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        alertDialog=builder.create();
        alertDialog.setView(promptView);
        alertDialog.show();
    }
    public void trivia(View view)
    {
        category="trivia";
        ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#ff0004"));
        ((Button)findViewById(R.id.categoryButton)).setText("TRIVIA");
        ((EditText)findViewById(R.id.numberEditText)).setText("");
        ((EditText)findViewById(R.id.numberEditText)).setHint("Enter your number");
        alertDialog.dismiss();
    }
    public void math(View view)
    {
        category="math";
        ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#0004ff"));
        ((Button)findViewById(R.id.categoryButton)).setText("MATH");
        ((EditText)findViewById(R.id.numberEditText)).setText("");
        ((EditText)findViewById(R.id.numberEditText)).setHint("Enter your number");
        alertDialog.dismiss();
    }
    public void date(View view)
    {
        category="date";
        ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#ffa200"));
        ((Button)findViewById(R.id.categoryButton)).setText("DATE");
        ((EditText)findViewById(R.id.numberEditText)).setText("");
        ((EditText)findViewById(R.id.numberEditText)).setHint("Enter MM/YY");
        alertDialog.dismiss();
    }
    public void year(View view)
    {
        category="year";
        ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#47c100"));
        ((Button)findViewById(R.id.categoryButton)).setText("YEAR");
        ((EditText)findViewById(R.id.numberEditText)).setText("");
        ((EditText)findViewById(R.id.numberEditText)).setHint("Enter YEAR");
        alertDialog.dismiss();
    }
    class Get_Fact extends AsyncTask<Void,Void,String> {
        String fullurl = "http://numbersapi.com/"+number+"/"+category;

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL(fullurl).openConnection();
                con.setConnectTimeout(10000);
                InputStream in = con.getInputStream();
                Reader reader = new InputStreamReader(in, "UTF-8"); // char[] buffer=new char[];
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
            if (!((Activity) ctx).isFinishing()) {
                if (s.contains("NFacts_E404")) {
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

                    ((TextView) findViewById(R.id.randomFactTv2)).setText("Unable to connect.\nPlease check your network conection!!");
                    progressDialog.dismiss();
                } else {
                    String s1[]=s.split(" ");
                    if(category.contains("date"))
                    {
                        ((TextView) findViewById(R.id.randomFactTv1)).setText(s1[1]+" "+s1[0]);
                    }
                    else
                        ((TextView) findViewById(R.id.randomFactTv1)).setText(s1[0]);

                    ((TextView) findViewById(R.id.randomFactTv2)).setText(s);
                    progressDialog.dismiss();
                }

            }
        }
    }
}
