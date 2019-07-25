package android.example.textrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView tv;
    Button back;
    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        back=findViewById(R.id.back);
        tv=findViewById(R.id.text);

        res=getIntent().getStringExtra("reco");
        tv.setText(res);
    }
}
