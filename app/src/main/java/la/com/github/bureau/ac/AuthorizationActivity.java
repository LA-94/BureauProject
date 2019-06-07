package la.com.github.bureau.ac;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import la.com.github.bureau.R;
import la.com.github.bureau.pojo.MenuActivities;

public class AuthorizationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public static final String EXTRA_KEY = "la.com.github.bureau.extra_key";
    private int mActivityCode;


    private Button btnSignUp;
    private Button btnSignIn;
    private EditText etLogin;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.reverse_alpha, R.anim.alpha);
        mActivityCode = getIntent().getIntExtra(EXTRA_KEY, 0);
        setContentView(R.layout.fr_authorization);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        addOnClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.sign_up_btn);
        btnSignIn = findViewById(R.id.sign_in_btn);
        etLogin = findViewById(R.id.login_et);
        etPassword = findViewById(R.id.password_et);
    }

    private void addOnClickListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegistrationActivity.getIntent(AuthorizationActivity.this, mActivityCode);
                startActivity(intent);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLogin.getText().toString();
                String password = etPassword.getText().toString();


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(AuthorizationActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AuthorizationActivity.this, "Success", Toast.LENGTH_LONG).show();
                                    Intent intent = MenuActivities.getIntent(AuthorizationActivity.this, mActivityCode);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(AuthorizationActivity.this, "Fail", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                /*
                if (Server.hasUser(login, password)) {
                    Intent intent = new Intent(AuthorizationActivity.this, AdListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.translate,R.anim. translate);
                } else {
                    String errorAuthorization = getString(R.string.errorAuthorization);
                    Toast.makeText(AuthorizationActivity.this, errorAuthorization, Toast.LENGTH_LONG)
                            .show();
                }*/
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }

            }
        };
    }

    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AuthorizationActivity.class);
        return intent;
    }

    public static Intent getIntent(Context packageContext, int activityCode) {
        Intent intent = new Intent(packageContext, AuthorizationActivity.class);
        intent.putExtra(EXTRA_KEY, activityCode);
        return intent;
    }
}
// todo использовать везде методы getIntent