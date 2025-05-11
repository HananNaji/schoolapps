package com.example.finalmobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    CheckBox rememberPassword;
    Button loginButton;
    TextView signUpText;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupview();

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(KEY_EMAIL, null);
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, null);

        if (savedEmail != null && savedPassword != null) {
            emailInput.setText(savedEmail);
            passwordInput.setText(savedPassword);
            rememberPassword.setChecked(true);
        }

        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest request = new StringRequest(Request.Method.POST, Constants.LOGIN_URL,
                    response -> {
                        String trimmedResponse = response.trim().replaceAll("\\s+", "").toLowerCase();
                        Log.d("LOGIN_RESPONSE", "Raw response: [" + response + "]");

                        if (!trimmedResponse.equals("invalidcredentials")) {
                            if (rememberPassword.isChecked()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_EMAIL, email);
                                editor.putString(KEY_PASSWORD, password);
                                editor.apply();
                            } else {
                                sharedPreferences.edit().clear().apply();
                            }

                            switch (trimmedResponse) {
                                case "student":
                                    startActivity(new Intent(this, StudentActivity.class));
                                    break;
                                case "teacher":
                                    startActivity(new Intent(this, TeacherActivity.class));
                                    break;
                                case "registrar":
                                    startActivity(new Intent(this, RegisterActivity.class));
                                    break;
                                default:
                                    Toast.makeText(this, "Unknown role: " + trimmedResponse, Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        } else {
                            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, "Server error: " + error.getMessage(), Toast.LENGTH_LONG).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email_or_username", email);
                    params.put("password", password);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        });

        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void setupview() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        rememberPassword = findViewById(R.id.rememberPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signUpText);
    }
}
