package dd186.unifood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class Signup extends AppCompatActivity {
    EditText name, lastName, email, pass1, pass2;
    TextView error;
    public static MakeARequest makeARequest = new MakeARequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = (EditText) findViewById(R.id.name_signup);
        lastName= (EditText) findViewById(R.id.surname_signup);
        email = (EditText) findViewById(R.id.email_signup);
        pass1= (EditText) findViewById(R.id.pass_signup1);
        pass2= (EditText) findViewById(R.id.pass_signup2);
        error = findViewById(R.id.error_msg_signup);
    }

    public void Back(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void Creation(View view) {
        String userName = name.getText().toString();
        String last = lastName.getText().toString();
        String useremail = email.getText().toString();
        String password1 = pass1.getText().toString();
        String password2 = pass2.getText().toString();
        if (useremail.trim().equals("") || userName.trim().equals("")|| last.trim().equals("")|| password1.trim().equals("")|| password2.trim().equals("")){
            if (useremail.trim().equals(""))
                email.setError("Email is required!");
            if (userName.trim().equals(""))
                name.setError("Name is required!");
            if (last.trim().equals(""))
                lastName.setError("Last name is required!");
            if (password1.trim().equals(""))
                pass1.setError("Password is required!");
            if (password2.trim().equals(""))
                pass2.setError("Password is required!");

        } else if(!password1.contentEquals(password2)){
            error.setText("Passwords do not match!");
        } else if (!useremail.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
            email.setError("Invalid email address");
        else if (!userName.matches("^[a-zA-Z]+$"))
            name.setError("Invalid name!");
        else if (!last.matches("^[a-zA-Z]+$"))
            lastName.setError("Invalid last name!");
        else {
            if (makeARequest.get("http://10.0.2.2:8080/rest/signup/" + useremail + "/" + userName + "/" + last + "/" + password1).contains("ok")) {
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
            } else {
                error.setText("A user with this email address already exists!");
            }
        }

    }

}
