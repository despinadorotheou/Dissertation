package dd186.unifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

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
        HttpGetRequest httpGetRequest = new HttpGetRequest();
        String userEmail = email.getText().toString();
        String userPass = pass.getText().toString();
        httpGetRequest.setLink("http://10.0.2.2:8080/rest/login/"+userEmail+"/" +userPass);
        httpGetRequest.execute();
        if (userEmail.trim().equals("") || userPass.trim().equals("")){
            if (userEmail.trim().equals(""))
                email.setError("Email is required!");
            if (userPass.trim().equals(""))
                pass.setError("Password is required!");

        } else{
            try {
                String response = httpGetRequest.get();
                if (!response.contains("invalid") && !response.isEmpty()){
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
