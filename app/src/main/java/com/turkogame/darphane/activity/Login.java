package com.turkogame.darphane.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.utils.Tools;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private View parent_view;
    private Button btn_login,btn_facebook,btn_google;
    private LoginButton facebook_loginButton;
    private TextInputEditText eposta, parola;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int code=100;
    int RC_SIGN_IN=0;
    CallbackManager callbackManager;
    String user_id,email,name,last_name;
    TextView register;
    String image_url;
    int kontrol=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

        parent_view = findViewById(android.R.id.content);
        btn_facebook =  findViewById(R.id.btn_facebook);
        btn_google = findViewById(R.id.btn_google);
        btn_login = findViewById(R.id.btn_giris);
        eposta = findViewById(R.id.eposta);
        parola = findViewById(R.id.parola);
        register = findViewById(R.id.sign_up);
      //  facebook_loginButton=findViewById(R.id.login_button);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager=CallbackManager.Factory.create();



            LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("mesaj", "Login");


                        String facebook_user_id=loginResult.getAccessToken().getUserId();
                        image_url="https://graph.facebook.com/"+loginResult.getAccessToken().getUserId()+"/picture?return_ssl_resources=1";
                        //Picasso.get().load(image_url).into(image_view nesnesi);

                        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                                        try {
                                            Log.d("mesaj", jsonObject.toString());
                                            Log.d("mesaj", jsonObject.getString("id"));
                                            Log.d("mesaj", jsonObject.getString("email"));
                                            Log.d("mesaj", jsonObject.getString("first_name"));
                                            Log.d("mesaj", jsonObject.getString("last_name"));
                                            Log.d("mesaj", image_url);

                                            user_id=jsonObject.getString("id");
                                            email=jsonObject.getString("email");
                                            name=  jsonObject.getString("first_name");
                                            last_name=jsonObject.getString("last_name");


                                            Facebook_Login_Json();

                                            Intent intent = new Intent(Login.this, MainMenu.class);
                                            startActivity(intent);
                                            finishAffinity();

                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            Bundle bundle = new Bundle();
                        bundle.putString(
                                "fields",
                                "id,name,link,email,gender,last_name,first_name,locale,timezone,updated_time,verified"
                        );



                            graphRequest.setParameters(bundle);
                            graphRequest.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Login.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            //setContentView(R.layout.login);


            ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Sign Up", Snackbar.LENGTH_SHORT).show();
            }
        });

            btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login_Post_Json();
            }
        });

             register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


            btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_google:
                        signIn();
                        break;
                    // ...
                }

            }
        });

            btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("user_status","public_profile", "user_friends","email"));


            }
        });





        Tools.setSystemBarColor(this, R.color.colorBanner);



        //Kullan??c?? login olduysa otomatik giri?? yap
        String login_kontrol = sharedPreferences.getString("login","0");

        Log.d("mesaj", "login kontrol?? ??al??????yor");

        Log.d("mesaj", login_kontrol);

        if (login_kontrol.equals("1")){


            Intent intent = new Intent(Login.this, MainMenu.class);
            startActivity(intent);

            finishAffinity();
        }


    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);//bu facebook login i??in

        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Intent intent = new Intent(Login.this, MainMenu.class);
            startActivity(intent);
            finishAffinity();

        } catch (ApiException e) {

            Log.w( "mesaj", "signInResult:failed code=" + e.getStatusCode());

        }
    }


    public void Facebook_Login_Json() {

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String md5=AppConfig.md5(email+"loginPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("email", email);
            object.put("sifre", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/login.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){
                                JSONObject bilgiler = new JSONObject(kontrol.getString("uye-bilgileri"));

                                sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email",bilgiler.getString("EMAIL"));
                                editor.putString("user_id",bilgiler.getString("KULLANICI_ID"));
                                editor.putString("adi",bilgiler.getString("ADI"));
                                editor.putString("soyadi",bilgiler.getString("SOYADI"));
                                editor.putString("resim",image_url);
                                editor.putString("login","1");
                                Log.d("mesaj", "login ba??ar??l??");
                                editor.commit();



                            } else {

                                kullanici_kayit();

                                /*
                                Toast toast = Toast.makeText(getApplicationContext(), kontrol.getString("hataMesaj"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();*/

                            }


                        } catch (Exception e) {

                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mesaj", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void kullanici_kayit() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String md5= AppConfig.md5(email+"kullaniciPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("kayit_tipi", "2");
            object.put("ad", name);
            object.put("soyad", last_name);
            object.put("email", email);
            object.put("sifre", user_id);
            object.put("facebook_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/kullanici.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Veri", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){


                                Log.d("snow", kontrol.getString("mesaj"));

                                sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email",email);
                                editor.putString("user_id",kontrol.getString("user_id"));
                                editor.putString("adi",name);
                                editor.putString("soyadi",last_name);
                                editor.putString("login","1");
                                editor.commit();

                                Facebook_Login_Json();


                            } else {

                                Log.d("snow", kontrol.getString("hataMesaj"));

                                /*Toast toast = Toast.makeText(getApplicationContext(), kontrol.getString("hataMesaj"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();*/

                            }


                        } catch (Exception e) {

                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("snow", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void login_Post_Json() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String md5=AppConfig.md5(eposta.getText().toString()+"loginPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("email", eposta.getText().toString());
            object.put("sifre", parola.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/login.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                      //  Log.d("snow", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){
                                JSONObject bilgiler = new JSONObject(kontrol.getString("uye-bilgileri"));

                                sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email",bilgiler.getString("EMAIL"));
                                editor.putString("user_id",bilgiler.getString("KULLANICI_ID"));
                                editor.putString("adi",bilgiler.getString("ADI"));
                                editor.putString("soyadi",bilgiler.getString("SOYADI"));
                                editor.putString("login","1");
                                editor.commit();


                                Intent intent = new Intent(Login.this, MainMenu.class);
                                startActivity(intent);
                                finishAffinity();


                               /* Log.d("KULLANICI_ID",  bilgiler.getString("KULLANICI_ID"));
                                Log.d("ADI",  bilgiler.getString("ADI"));
                                Log.d("SOYADI",  bilgiler.getString("SOYADI"));
                                Log.d("EMAIL",  bilgiler.getString("EMAIL"));
                                Log.d("PAROLA",  bilgiler.getString("PAROLA"));
                                Log.d("EMAIL_ONAY",  bilgiler.getString("EMAIL_ONAY"));
                                Log.d("AKTIF",  bilgiler.getString("AKTIF"));*/

                            } else {

                                Toast toast = Toast.makeText(getApplicationContext(), kontrol.getString("hataMesaj"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

                            }


                        } catch (Exception e) {

                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("snow", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        try {
            String url = AppConfig.URL + "/kullanici.php?user_id=2&kontrol_key=7C16FC7EC78B50FD1E751B81E908091F";


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("snow", response.toString());

                   // Toast.makeText(getApplicationContext(), "I am OK !" + response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }



            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void login_post() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL + "/login1.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("snow", response.toString());
                        try {
                            JSONObject kontrol = new JSONObject(response);

                            if (kontrol.getString("hata")=="false"){
                                JSONObject bilgiler = new JSONObject(kontrol.getString("uye-bilgileri"));

                                Log.d("KULLANICI_ID",  bilgiler.getString("KULLANICI_ID"));
                                Log.d("ADI",  bilgiler.getString("ADI"));
                                Log.d("SOYADI",  bilgiler.getString("SOYADI"));
                                Log.d("EMAIL",  bilgiler.getString("EMAIL"));
                                Log.d("PAROLA",  bilgiler.getString("PAROLA"));
                                Log.d("EMAIL_ONAY",  bilgiler.getString("EMAIL_ONAY"));
                                Log.d("AKTIF",  bilgiler.getString("AKTIF"));

                            }




                        } catch (Exception e) {

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("snow", "onErrorResponse: " + error.getMessage().toString());
            }
        }) {

            @Override
            protected Map<String,String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("kontrol_key", "85EF6B2C8E0D8D20A950E8B345A46843");
                params.put("email", "syrize@hotmail.com");
                params.put("sifre", "1234");


                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }




}
