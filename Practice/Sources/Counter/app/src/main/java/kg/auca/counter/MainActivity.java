package kg.auca.counter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView counterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterTextView = (TextView) findViewById(R.id.counterTextView);
    }

    public void count(View view) {
        int value = Integer.parseInt(counterTextView.getText().toString()) + 1;
        counterTextView.setText(String.format(Locale.getDefault(), "%04d", value));
    }

}
