package com.example.givemepass.asynctaskexecutordemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ExecutorService mExecutors;
    private ProgressBar mTask1;
    private ProgressBar mTask2;
    private ProgressBar mTask3;
    private TextView result;
    private int taskCount;
    private Button startBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        mExecutors = Executors.newCachedThreadPool();
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strs = new String[100];
                for(int i = 0; i < 100; i++){
                    strs[i] = String.valueOf(i);
                }
                getTask(1).executeOnExecutor(mExecutors, strs);
                getTask(2).executeOnExecutor(mExecutors, strs);
                getTask(3).executeOnExecutor(mExecutors, strs);
                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                result.setText("任務尚未完成...");
                            }
                        });
                        while(taskCount < 3);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                result.setText("全部任務已完成!");
                            }
                        });
                    }
                });
            }
        });


    }

    private AsyncTask<String, Integer, String> getTask(final int num){
        return new AsyncTask<String, Integer, String>() {
            private int count;
            @Override
            protected String doInBackground(String... params) {
                while(count < params.length) {
                    int rand = (int) (Math.random() * 5);
                    try {
                        Thread.sleep(rand * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                    publishProgress(count);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                taskCount++;
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                taskCount++;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if(num == 1){
                    mTask1.setProgress(values[0]);
                } else if(num == 2){
                    mTask2.setProgress(values[0]);
                } else if(num == 3){
                    mTask3.setProgress(values[0]);
                }
            }
        };
    }

    private void initView() {
        mTask1 = (ProgressBar) findViewById(R.id.progress_task1);
        mTask2 = (ProgressBar) findViewById(R.id.progress_task2);
        mTask3 = (ProgressBar) findViewById(R.id.progress_task3);
        result = (TextView) findViewById(R.id.result);
        startBtn = (Button) findViewById(R.id.start_btn);
    }
}
