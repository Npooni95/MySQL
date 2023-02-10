package edu.csustan.cs3810.mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView lblMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblMsg = findViewById(R.id.lblMsg);
    }

    public void runQuery(View v) {
        // get query from input field
        EditText inputQuery = findViewById(R.id.inputQuery);
        String query = inputQuery.getText().toString();
        Log.i("MainActivity",query);

        // run query in background
        Container container = new Container(lblMsg);
        container.execute(query);
    }
}