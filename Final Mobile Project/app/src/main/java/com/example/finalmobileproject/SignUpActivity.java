package com.example.finalmobileproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
public class SignUpActivity extends AppCompatActivity {

    EditText email, username, password, confirmPassword;
    RadioGroup roleRadioGroup;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupviews();

        signUpBtn.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String usernameText = username.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            String confirmPasswordText = confirmPassword.getText().toString().trim();

            if (emailText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                Toast.makeText(this, "Please fill  all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!emailText.matches("^[A-Za-z0-9+_.-]+@gmail\\.com$")) {
                Toast.makeText(this, "Email must be a valid ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
            if (selectedRoleId == -1) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRoleBtn = findViewById(selectedRoleId);
            String role = selectedRoleBtn.getText().toString().toLowerCase();

            StringRequest request = new StringRequest(Request.Method.POST, Constants.SIGNUP_URL,
                    response -> {
                        Toast.makeText(this, response.trim(), Toast.LENGTH_LONG).show();
                    },
                    error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", emailText);
                    params.put("username", usernameText);
                    params.put("password", passwordText);
                    params.put("role", role);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        });
    }

    private void setupviews() {
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
        signUpBtn = findViewById(R.id.signUpBtn);
    }
}
