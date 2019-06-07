package la.com.github.bureau.ac;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import la.com.github.bureau.R;
import la.com.github.bureau.pojo.MenuActivities;
import la.com.github.bureau.pojo.ValidationModule;

public class RegistrationActivity extends AppCompatActivity {

    private static final String EXTRA_KEY = "la.com.github.bureau.pojo.extra_key";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private int mActivityCode;

    private Button btnDone;
    private EditText etPassword;
    private EditText etLogin;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_registration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivityCode = getIntent().getIntExtra(EXTRA_KEY, 0);

        init();
        addOnClickListener();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        btnDone = findViewById(R.id.done_btn);
        etLogin = findViewById(R.id.login_et);
        etPassword = findViewById(R.id.password_et);
        etName = findViewById(R.id.name_et);
    }

    private void addOnClickListener() {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                String name = etName.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_LONG).show();
                                    Intent intent = MenuActivities.getIntent(RegistrationActivity.this, mActivityCode);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegistrationActivity.this, "Fail", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                /*
                Intent intent;
                if (!Server.hasUser(email)) {
                    if (Server.addData(name, email, password)) {
                        intent = new Intent(RegistrationActivity.this, AdListActivity.class);
                    } else {
                        intent = new Intent(RegistrationActivity.this, DisconnectionActivity.class);
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.translate,R.anim. translate);
                } else {
                    String errorRegistration = getString(R.string.errorRegistrationString);
                    Toast.makeText(RegistrationActivity.this, errorRegistration, Toast.LENGTH_LONG)
                            .show();
                }
*/
            }
        });
        etLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String login = etLogin.getText().toString();
                if (ValidationModule.validLogin(login) == ValidationModule.LETTER_ERROR) {
                    Toast tMessage = Toast.makeText(getApplicationContext(),
                            getString(R.string.validIncorrectLoginLengthString),
                            Toast.LENGTH_LONG);
                    TextView tvMessage = tMessage.getView().findViewById(android.R.id.message);
                    tvMessage.setTextColor(Color.RED);
                    tMessage.show();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        etLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusMoved) {

            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_LONG).show();
                    int activityCode = getIntent().getIntExtra(EXTRA_KEY, 0);
                    Intent intent = MenuActivities.getIntent(RegistrationActivity.this, activityCode);
                    startActivity(intent);
                } else {

                }

            }
        };
    }


    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, RegistrationActivity.class);
        return intent;
    }

    public static Intent getIntent(Context packageContext, int activityCode) {
        Intent intent = new Intent(packageContext, RegistrationActivity.class);
        intent.putExtra(EXTRA_KEY, activityCode);
        return intent;
    }
}
