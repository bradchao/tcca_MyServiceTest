package tw.brad.android.games.myservicetest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private MyServiceConnection myServiceConnection;
    private SeekBar seekBar;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReceiver = new MyReceiver();
        seekBar = (SeekBar)findViewById(R.id.seekbar);

        IntentFilter filter = new IntentFilter("brad");
        registerReceiver(myReceiver, filter);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    Intent it = new Intent(MainActivity.this, MyService.class);
                    it.putExtra("progress", progress);
                    startService(it);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        Intent it = new Intent(MainActivity.this, MyService.class);
//        myServiceConnection = new MyServiceConnection();
//        bindService(it,myServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void finish() {
        unregisterReceiver(myReceiver);
        super.finish();
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int len = intent.getIntExtra("len", -1);
            int now = intent.getIntExtra("now", -1);
            if (len >= 0 ){
                seekBar.setMax(len);
            }
            if (now >= 0){
                seekBar.setProgress(now);
            }
        }
    }


    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    public void start(View view){
        Intent it = new Intent(MainActivity.this, MyService.class);
        it.putExtra("isStart", true);
        startService(it);
    }
    public void pause(View view){
        Intent it = new Intent(MainActivity.this, MyService.class);
        it.putExtra("isPause", true);
        startService(it);
    }
    public void stop(View view){
        Intent it = new Intent(MainActivity.this, MyService.class);
        stopService(it);
    }
    public void test(View view){
        unbindService(myServiceConnection);
    }


}
