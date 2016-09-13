package com.example.soil.biogi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soil.biogi.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private String TAG= LoginActivity.class.getSimpleName() ;
    Button register ;
    EditText inputEmail, inputPassword;
    private TextInputLayout inputLayoutPassword, inputLayoutEmail ;
    private SessionManger session;
    private SaveText db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        LinearLayout layout = (LinearLayout) findViewById(R.id.loginLayout);
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        register =(Button)findViewById(R.id.btnLinkToRegisterScreen) ;
        session = new SessionManger(getApplicationContext());
        db = new SaveText(getApplicationContext()) ;

        if (session.isLoggedIn()) {
            startActivity( new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void Login(View view) {
        Dialog.Dialog(this);
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        // Check for empty data in the form
        if (!validateEmail()) {
            Dialog.dissDialog();
            return;
        }

        if (!validatePassword()) {
            Dialog.dissDialog();
            return;
        }

        StringRequest checkreq = new StringRequest(Request.Method.POST,
                AllUrl.Loginurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);

                            if (!jObj.getBoolean("error")) {

                                session.setLogin(true);
                                JSONObject user = jObj.getJSONObject("user");

                                Log.d(TAG, "NAME=" + user.getString("id")) ;
                                db.addUser(user.getString("id"), user.getString("address"), user.getString("date"),
                                        password, user.getString("name"), user.getString("sex"), user.getString("birthday"),
                                        user.getString("phone"), user.getString("cellphone"), user.getString("mail"));
                                Dialog.dissDialog();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class)) ;
                                finish();


                        }else {
                                Toast.makeText(LoginActivity.this, "請輸入正確的帳號或密碼", Toast.LENGTH_LONG).show();
                            }

                        }catch (JSONException e) {
                            // JSON error

                            Log.e(TAG, "json parsing error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Dialog.dissDialog();
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String,String> getParams()  {
                Map<String,String> parmater = new HashMap<>() ;
                parmater.put("email", email) ;
                parmater.put("password", password) ;

                Log.d(TAG, "params: " + parmater.toString());
                return  parmater ;
            }


        };

        ApplicationController.getInstance().addToRequestQueue(checkreq);
        Dialog.dissDialog();
    }

    public void Registater(View view) {
        startActivity(new Intent(LoginActivity.this,
                Register.class));
        finish();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    // Validating name
    private boolean validateEmail() {
        if (inputEmail.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError(getString(R.string.err_msg_name));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    // Validating email
    private boolean validatePassword() {
        String password = inputPassword.getText().toString().trim();

        if (password.isEmpty() ) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

/*    private static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && android.util.Patterns.EMAIL_ADDRESS.matcher(password).matches();
    }
*/
    private class MyTextWatcher implements TextWatcher {

        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }
}
