package kg.auca.greeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        messageTextView = findViewById(R.id.messageTextView);
    }

    public void greet(View view) {
        String name = nameEditText.getText().toString();
        String message = getString(R.string.messageTemplate, name);
        messageTextView.setText(message);
    }

}
