package com.self_ignition.cabbage2;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.os.PatternMatcher;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignUpActivity extends AppCompatActivity {

    EditText user;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        user = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        user.setFilters(new InputFilter[]{filter});
        email.setFilters(new InputFilter[]{filter});
        password.setFilters(new InputFilter[]{filter});
    }


    public void clickFunction(View v)
    {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String characterSet = "@ .";
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)))
                {
                    if(characterSet.contains("" + source.charAt(i)))
                    {
                        return "@";
                    }
                    else if(characterSet.contains("" + source.charAt(i)))
                    {
                        return ".";
                    }
                    else
                    {
                        return "";
                    }
                }
            }
            return null;
        }
    };

    public void buttonFunction(View v)
    {
        signup();
    }

    public void signup()
    {
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            validate();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public boolean validate() throws UnsupportedEncodingException {
        boolean valid = true;

        String _user = user.getText().toString();
        String _email = email.getText().toString();
        String _password = password.getText().toString();

        if (_user.isEmpty())
        {
            user.setError("Please enter a valid username");
            valid = false;
        }

        else if (_email.isEmpty())
        {
            email.setError("Please enter a valid email address");
            valid = false;
        }

        else if (_password.isEmpty() || _password.length() < 6 || _password.length() > 12)
        {
            password.setError("Please enter a valid password");
            valid = false;
        }
        else
        {
            password.setError(null);

            String data = URLEncoder.encode("user", "UTF-8")
                    + "=" + URLEncoder.encode(_user, "UTF-8");

            data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                    + URLEncoder.encode(_email, "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8")
                    + "=" + URLEncoder.encode(_password, "UTF-8");


            try
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url = new URL("http://computing.derby.ac.uk/~cabbage/signup.php");
                URLConnection client = url.openConnection();
                client.setDoOutput(true);

                OutputStreamWriter outputPost = new OutputStreamWriter(client.getOutputStream());
                outputPost.write(data);
                outputPost.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                valid = true;
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
            }
        }
        return valid;
    }
}

