package dd186.unifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dd186.unifood.Entities.Product;
import dd186.unifood.Entities.User;

public class Login extends AppCompatActivity {

    EditText email,pass;
    TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.email_login);
        pass = (EditText) findViewById(R.id.pass_login);
        error = findViewById(R.id.error_login);

    }

    public void login(View view){
        HttpRequest httpRequest = new HttpRequest();
        String userEmail = email.getText().toString();
        String userPass = pass.getText().toString();
        httpRequest.setLink("http://10.0.2.2:8080/rest/login/"+userEmail+"/" +userPass);
        httpRequest.execute();
        if (userEmail.trim().equals("") || userPass.trim().equals("")){
            if (userEmail.trim().equals(""))
                email.setError("Email is required!");
            if (userPass.trim().equals(""))
                pass.setError("Password is required!");

        } else{
            try {
                String response = httpRequest.get();
                if (!response.contains("invalid")){
                    Intent intent = new Intent(this, Main.class);
                    intent.putExtra("user", response);
                    startActivity(intent);
                } else {
                    error.setText(R.string.error_login);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    public void signup(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}
