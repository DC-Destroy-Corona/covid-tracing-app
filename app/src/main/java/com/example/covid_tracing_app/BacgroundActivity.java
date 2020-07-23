package com.example.covid_tracing_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeoutException;

public class BacgroundActivity extends AppCompatActivity {
    String QUEUE_NAME = "user.position.queue";
    ConnectionFactory factory = new ConnectionFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacground);
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {


        public void NetworkTask() {

        }

        @Override
        protected String doInBackground(Void... params) {
            factory.setHost("203.250.32.29");
            factory.setPort(54326);
            factory.setUsername("covid");
            factory.setPassword("covid");
            try {
                Connection connection;
                Channel channel;
                connection = factory.newConnection();
                channel = connection.createChannel();
                for(int i=0;i<=100000;i++){
                    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    String message = "Message" + (int) (Math.random() * 100);
                    channel.basicPublish("user.position", "user.position.route", null, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");
                    Thread.sleep(10);
                    //channel.close();
                    //connection.close();
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}

