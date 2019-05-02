package dd186.unifood;

import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;


import java.util.concurrent.ExecutionException;

import static dd186.unifood.Login.makeARequest;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
public class LoginUnitTest {

    private Login tested;

    private EditText email,pass;
    private TextView error;
    private CheckBox rememberMe;
    private Button loginBtn, signupBtn;



    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tested = spy(Robolectric.setupActivity(Login.class));
        makeARequest = mock(MakeARequest.class);
        email = tested.findViewById(R.id.email_login);
        pass = tested.findViewById(R.id.pass_login);
        error =  tested.findViewById(R.id.error_login);
        rememberMe = tested.findViewById(R.id.remember_me_btn);
        loginBtn = tested.findViewById(R.id.login_btn);
        signupBtn = tested.findViewById(R.id.signup_page_btn);
    }

    @Test
    public void invalidUser() {
        //setup
        email.setText("dd186@student.le.ac.uk");
        pass.setText("123456");
        when(makeARequest.get(anyString())).thenReturn("invalid");


        //test
        loginBtn.performClick();

        //verify
        assertEquals("Invalid Email or Password!",error.getText());

    }

    @Test
    public void invalidUser1() {
        //setup
        email.setText("");
        pass.setText("");
        when(makeARequest.get(anyString())).thenReturn("");


        //test
        loginBtn.performClick();

        //verify
        assertEquals("Email is required!",email.getError());
        assertEquals("Password is required!",pass.getError());


    }

    @Test
    public void validUser() throws ExecutionException, InterruptedException {
        //setup
        email.setText("dd186@student.le.ac.uk");
        pass.setText("12345");

        String mockUserJson = "{\"id\":6,\"name\":\"Despina\",\"lastName\":\"Dorotheou\",\"email\":\"dd186@student.le.ac.uk\",\"password\":\"$2a$10$P6ENyTd74quDcQSsKiAqV.U3hcsvn.d/79pw3UyRvjLKCdechChyq\",\"favouriteProducts\":[25,26,2,9],\"orders\":[{\"id\":200,\"date\":\"Sat Apr 20 13:42:25 BST 2019\",\"value\":0.8,\"products\":{\"25\":1}},{\"id\":193,\"date\":\"Sat Apr 20 13:16:46 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":198,\"date\":\"Sat Apr 20 13:25:50 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":209,\"date\":\"Sat Apr 20 15:53:11 BST 2019\",\"value\":2.5,\"products\":{\"18\":1}},{\"id\":196,\"date\":\"Sat Apr 20 13:19:39 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":207,\"date\":\"Sat Apr 20 15:08:05 BST 2019\",\"value\":2.5,\"products\":{\"27\":1}},{\"id\":192,\"date\":\"Sat Apr 20 13:07:57 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":195,\"date\":\"Sat Apr 20 13:19:36 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":199,\"date\":\"Sat Apr 20 13:34:04 BST 2019\",\"value\":1.5,\"products\":{\"19\":1}},{\"id\":208,\"date\":\"Sat Apr 20 15:35:01 BST 2019\",\"value\":0.8,\"products\":{\"11\":1}},{\"id\":204,\"date\":\"Sat Apr 20 14:26:24 BST 2019\",\"value\":0.8,\"products\":{\"13\":1}},{\"id\":194,\"date\":\"Sat Apr 20 13:17:07 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":203,\"date\":\"Sat Apr 20 14:19:43 BST 2019\",\"value\":1.5,\"products\":{\"19\":1}},{\"id\":201,\"date\":\"Sat Apr 20 13:52:43 BST 2019\",\"value\":2.5,\"products\":{\"27\":1}},{\"id\":197,\"date\":\"Sat Apr 20 13:20:20 BST 2019\",\"value\":1.55,\"products\":{\"2\":1}},{\"id\":202,\"date\":\"Sat Apr 20 14:03:50 BST 2019\",\"value\":0.8,\"products\":{\"25\":1}},{\"id\":205,\"date\":\"Sat Apr 20 14:38:15 BST 2019\",\"value\":0.8,\"products\":{\"25\":1}},{\"id\":206,\"date\":\"Sat Apr 20 14:56:10 BST 2019\",\"value\":0.8,\"products\":{\"9\":1}},{\"id\":210,\"date\":\"Sat Apr 20 19:24:07 BST 2019\",\"value\":1.0,\"products\":{\"15\":1}}]}";
        when(makeARequest.get(anyString())).thenReturn(mockUserJson);


        //test
        loginBtn.performClick();


        //verify
        ShadowActivity shadowActivity = shadowOf(tested);
        // 9
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        // 10
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        // 11
        assertThat(shadowIntent.getIntentClass().getName(), equalTo(Main.class.getName()));

    }

    @Test
    public void signUpPage(){
        //test
        signupBtn.performClick();

        //verify
        ShadowActivity shadowActivity = shadowOf(tested);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getIntentClass().getName(), equalTo(Signup.class.getName()));

    }



}
