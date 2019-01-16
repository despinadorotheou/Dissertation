package example.unifood;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Entities.Product;

public class Login extends AppCompatActivity {

    EditText email,pass;
    TextView error;
    List<Product> products = new ArrayList<>();


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
                if (httpRequest.get().contains("ok")){
                    HttpRequest httpRequest1 = new HttpRequest();
                    httpRequest1.setLink("http://10.0.2.2:8080/rest/products");
                    httpRequest1.execute();
                    try {
                        products = extractProductsFromJson(httpRequest1.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, Main.class);
                    intent.putExtra("products", (Serializable) products);
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

        public List<Product> extractProductsFromJson(String productString) {
            //if the json string is empty or null, the return early.
            ObjectMapper mapper = new ObjectMapper();
            if (TextUtils.isEmpty(productString)) {
                return null;
            }
            try {
                List<Product> products = new ArrayList<>();
                products = mapper.readValue(productString, new TypeReference<List<Product>>() {
                });

                return products;
            } catch (Exception e) {
                System.out.println("Something wrong with the deserialisation of products ");
                e.printStackTrace();
                return null;
            }
        }

    public void signup(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}
