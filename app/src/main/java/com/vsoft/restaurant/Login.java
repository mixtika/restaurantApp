package com.vsoft.restaurant;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import android.annotation.TargetApi;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;


/*import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
*/

public class Login extends AppCompatActivity {
    private AutoCompleteTextView usr;
    private EditText pwd;
    private View mProgressView;
    private View mLoginFormView;
    private UserLoginTask mAuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usr=(AutoCompleteTextView)findViewById(R.id.usuario);
        pwd=(EditText)findViewById(R.id.pasword);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void btnLogin(View vi)
    {
        if(!usr.getText().toString().isEmpty()) {
            showProgress(true);
            mAuthTask = new UserLoginTask(vi,usr.getText().toString(), pwd.getText().toString());
            mAuthTask.execute((Void) null);
        }
        else {
            usr.setError("Ingrese un usuario");
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String cad;
        View vi;
        UserLoginTask(View vi,String email, String password) {
            this.vi=vi;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                GetServer ob = new GetServer();
                cad = ob.stringQuery("/login?username=" + usr.getText() + "&password=" + pwd.getText(),"POST");
                JSONObject json=new JSONObject(cad);
                JSONArray array=json.optJSONArray("users");
                if(array.length()>0)
                    cad="1";
                else
                    cad="0";
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "NO Valido", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if(success)
            {
                //finish();
                int v = Integer.parseInt(cad);
                if (v == 1) {
                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "NO Valido", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "FAIL!!!", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }

}
