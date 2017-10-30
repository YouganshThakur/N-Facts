package com.techrex.n_facts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class RandomFacts extends AppCompatActivity {

    String category;
    Context ctx;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_facts);
        ctx=this;
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading...");
        category=getIntent().getStringExtra("category");
        switch (category)
        {
            case "trivia":
                ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#ff0004"));
                ((Button)findViewById(R.id.categoryButton)).setText("TRIVIA");
                break;
            case "math":
                ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#0004ff"));
                ((Button)findViewById(R.id.categoryButton)).setText("MATH");
                break;
            case "date":
                ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#ffa200"));
                ((Button)findViewById(R.id.categoryButton)).setText("DATE");
                break;
            case "year":
                ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#47c100"));
                ((Button)findViewById(R.id.categoryButton)).setText("YEAR");
                break;

        }
        progressDialog.show();
        new Get_Fact().execute();
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
        alertDialog.dismiss();
        progressDialog.show();
        new Get_Fact().execute();
    }
    public void math(View view)
    {
        category="math";
        ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#0004ff"));
        ((Button)findViewById(R.id.categoryButton)).setText("MATH");
        alertDialog.dismiss();
        progressDialog.show();
        new Get_Fact().execute();
    }
    public void date(View view)
    {
        category="date";
        ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#ffa200"));
        ((Button)findViewById(R.id.categoryButton)).setText("DATE");
        alertDialog.dismiss();
        progressDialog.show();
        new Get_Fact().execute();
    }
    public void year(View view)
    {
        category="year";
        ((Button)findViewById(R.id.categoryButton)).setBackgroundColor(Color.parseColor("#47c100"));
        ((Button)findViewById(R.id.categoryButton)).setText("YEAR");
        alertDialog.dismiss();
        progressDialog.show();
        new Get_Fact().execute();
    }
    class Get_Fact extends AsyncTask<Void,Void,String> {
        String fullurl = "http://numbersapi.com/random/" + category;

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
