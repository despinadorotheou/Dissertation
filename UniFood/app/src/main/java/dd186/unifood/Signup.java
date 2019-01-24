package dd186.unifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

public class Signup extends AppCompatActivity {
    EditText name, lastName, email, pass1, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = (EditText) findViewById(R.id.name_signup);
        lastName= (EditText) findViewById(R.id.surname_signup);
        email = (EditText) findViewById(R.id.email_signup);
        pass1= (EditText) findViewById(R.id.pass_signup1);
        pass2= (EditText) findViewById(R.id.pass_signup2);
    }

    public void Back(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void Creation(View view) {
        HttpRequest httpRequest = new HttpRequest();
        String userName = name.getText().toString();
        String last = lastName.getText().toString();
        String useremail = email.getText().toString();
        String password1 = pass1.getText().toString();
        String password2 = pass2.getText().toString();
        if (useremail.trim().equals("") || userName.trim().equals("")|| last.trim().equals("")|| password1.trim().equals("")|| password2.trim().equals("")){
            //todo
        } else if(password1.contentEquals(password2)){
            httpRequest.setLink("http://10.0.2.2:8080/rest/signup/"+useremail+"/" +userName+"/" +last+"/" +password1);
            httpRequest.execute();
            try {
                if (httpRequest.get().contains("ok")){
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                } else {
                    //todo pass or username wrong
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
