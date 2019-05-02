package dd186.unifood;

import android.content.Intent;
import android.widget.Button;
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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static dd186.unifood.Signup.makeARequest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
public class SignupUnitTest {

    private Signup tested;
    private EditText name, lastName, email, pass1, pass2;
    private TextView error;
    private Button back, signupBtn;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tested = spy(Robolectric.setupActivity(Signup.class));
        makeARequest = mock(MakeARequest.class);
        email = tested.findViewById(R.id.email_signup);
        pass1 = tested.findViewById(R.id.pass_signup1);
        pass2 = tested.findViewById(R.id.pass_signup2);
        error =  tested.findViewById(R.id.error_msg_signup);
        name = tested.findViewById(R.id.name_signup);
        lastName = tested.findViewById(R.id.surname_signup);
        signupBtn = tested.findViewById(R.id.confirm_signup_btn);
        back = tested.findViewById(R.id.back_signup_btn);
    }

    @Test
    public void backToMain(){
        //test
        back.performClick();

        //verify
        ShadowActivity shadowActivity = shadowOf(tested);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getIntentClass().getName(), equalTo(Login.class.getName()));

    }

    @Test
    public void createAccount(){
        //setup
        email.setText("test123@test.com");
        name.setText("Despina");
        lastName.setText("Dorotheou");
        pass1.setText("12345");
        pass2.setText("12345");
        when(makeARequest.get(anyString())).thenReturn("ok");

        //test
        signupBtn.performClick();

        //verify
        ShadowActivity shadowActivity = shadowOf(tested);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getIntentClass().getName(), equalTo(Login.class.getName()));
    }

    @Test
    public void failedCreateAccount1() {
        //setup
        email.setText("test123test.com");
        name.setText("Despina");
        lastName.setText("Dorotheou");
        pass1.setText("12345");
        pass2.setText("12345");
        when(makeARequest.get(anyString())).thenReturn("");

        //test
        signupBtn.performClick();

        //verify
        assertEquals("Invalid email address", email.getError());
    }

    @Test
    public void failedCreateAccount2() {
        //setup
        email.setText("test123@test.com");
        name.setText("Despi£na");
        lastName.setText("Dorotheou");
        pass1.setText("12345");
        pass2.setText("12345");
        when(makeARequest.get(anyString())).thenReturn("");

        //test
        signupBtn.performClick();

        //verify
        assertEquals("Invalid name!", name.getError());
    }

    @Test
    public void failedCreateAccount3()  {
        //setup
        email.setText("test123@test.com");
        name.setText("Despina");
        lastName.setText("Dorot8heou");
        pass1.setText("12345");
        pass2.setText("12345");
        when(makeARequest.get(anyString())).thenReturn("");

        //test
        signupBtn.performClick();

        //verify
        assertEquals("Invalid last name!", lastName.getError());
    }

    @Test
    public void failedCreateAccount4() {
        //setup
        email.setText("test123@test.com");
        name.setText("Despina");
        lastName.setText("Dorotheou");
        pass1.setText("12345");
        pass2.setText("1345");
        when(makeARequest.get(anyString())).thenReturn("");

        //test
        signupBtn.performClick();

        //verify
        assertEquals("Passwords do not match!", error.getText());
    }

    @Test
    public void failedCreateAccount5() {
        //setup
        email.setText("");
        name.setText("");
        lastName.setText("");
        pass1.setText("");
        pass2.setText("");
        when(makeARequest.get(anyString())).thenReturn("");

        //test
        signupBtn.performClick();

        //verify
        assertEquals("Email is required!", email.getError());
        assertEquals("Name is required!", name.getError());
        assertEquals("Last name is required!", lastName.getError());
        assertEquals("Password is required!", pass1.getError());
        assertEquals("Password is required!", pass2.getError());

    }
}
