package dd186.unifood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    EditText email,pass;
    TextView error;
    CheckBox rememberMe;
    SharedPreferences userDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDetails = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.email_login);
        pass = (EditText) findViewById(R.id.pass_login);
        error = findViewById(R.id.error_login);
        rememberMe = findViewById(R.id.remember_me_btn);
        String savedEmail = userDetails.getString("email", "");
        assert savedEmail != null;
        if (!savedEmail.equals("")){
            rememberMe.setChecked(true);
            email.setText(savedEmail);
        }
        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor edit = userDetails.edit();
            if (isChecked){
                if (savedEmail.equals("")) {
                    if (!email.getText().toString().isEmpty()) {
                        edit.putString("email", email.getText().toString().trim());
                        edit.apply();
                    }
                }
            }else{
                if (!savedEmail.equals("")) {
                    edit.remove("email");
                    edit.apply();
                }
            }
        });

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
