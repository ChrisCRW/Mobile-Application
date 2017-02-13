package com.self_ignition.cabbage2;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    //Textbox variables
    EditText username;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        username = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        username.setFilters(new InputFilter[]{filter});
        email.setFilters(new InputFilter[]{filter});
        password.setFilters(new InputFilter[]{filter});
    }

    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String blockCharacterSet = " !#$%&()*+,-.:;<=>?@[]^_{|}~";
            for (int i = start; i < end; i++) {
                if (source != null && !Character.isLetterOrDigit(source.charAt(i)) && !blockCharacterSet.contains("" + source)) {
                    return "";
                }
            }
            return null;
        }
    };

    public void signup() {
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        validate();

                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void validate(){

        final String _user = username.getText().toString();
        final String _email = email.getText().toString();
        final  String _password = password.getText().toString();

        if (_user.isEmpty()) {
            username.setError("Please enter a valid username");
        } else if (_email.isEmpty()) {
            email.setError("Please enter a valid email address");
        } else if (_password.isEmpty() || _password.length() < 6 || _password.length() > 12) {
            password.setError("Please enter a valid password");
        } else {
            volleyMethod();
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }
    }

    public void volleyMethod() {
        Map<String, String> mParams;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://computing.derby.ac.uk/~cabbage/signup.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley", "Response is: " + response);
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
            }
        }
                //Some touchy-feely with body to add post payload
        ) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();

                //Finding variables because of this inner class
                EditText username = (EditText) findViewById(R.id.input_name);
                EditText email = (EditText) findViewById(R.id.input_email);
                EditText password = (EditText) findViewById(R.id.input_password);

                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                params.put("email", email.getText().toString());
                return params;
            }
        };
        //add the request to the queue
        queue.add(request);
    }

    public void clickFunction(View v) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public void buttonFunction(View v) {
        signup();
    }
}
