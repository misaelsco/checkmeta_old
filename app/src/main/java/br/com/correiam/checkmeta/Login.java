package br.com.correiam.checkmeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import br.com.correiam.checkmeta.dao.UserDAO;
import br.com.correiam.checkmeta.dominio.User;


public class Login extends Activity implements View.OnClickListener {


    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AccessTokenTracker accessTokenTracker;
    //private ProfileTracker profileTracker;
    private LoginManager loginManager;
    private TextView tvCadastrar;
    private Boolean isLoggedFacebook = false;
    private String fbUserID;
    //private String fbProfileName;
    private String fbAuthToken;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private UserDAO userDAO;
    private User user;
    private static final String TAG = "FacebookLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("Teste", "onCreate inicio");

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        tvCadastrar = (TextView) findViewById(R.id.tvCadastroLink);
        tvCadastrar.setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.tvCadastroLink:
                onClickCadastrar();
                break;
            case R.id.login_button:
                onClickLoginFacebook();
                break;
            case R.id.btnLogin:
                doLogin();
                break;
        }
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("Teste", "onActivityResult");
    }

    //Chama a tela de cadastro quando clicado no TextView com a frase de cadastro
    public void onClickCadastrar()
    {
        Log.d("Teste", "entrei na tela de cadastro");
        Intent iCadastro  = new Intent(Login.this, CadastroActivity.class);
        Login.this.startActivity(iCadastro);
    }

    public void onClickLoginFacebook()
    {

        if(!isLoggedFacebook) {

            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();

            Log.d("Teste", "Passei pelo onClick()");

            loginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
            loginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Log.d(TAG, "Logged in with facebook");

                    getProfileTracker();

                    //accessTokenTracker = getAccessTokenTracker();
                    isLoggedFacebook = true;
                }

                @Override
                public void onCancel() {
                    // App code
                    Log.d("Teste", "onCancel inicio");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.d("Teste", "onError( " + exception.toString() + ")");
                }
            });
        }
    }

    public AccessTokenTracker getAccessTokenTracker(){
        return accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                fbAuthToken = currentAccessToken.getToken();
                fbUserID = currentAccessToken.getUserId();



                Log.d(TAG, "User id: " + fbUserID);
                Log.d(TAG, "Access token is: " + fbAuthToken);


                // Ensure that our profile is up to date
                Profile.fetchProfileForCurrentAccessToken();
            }
        };

    }

    public void getProfileTracker() {
         new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                userDAO = new UserDAO();
                user.setName(currentProfile.getName());
                user.setEmail("Usuario@gmail.com");
                Long insertedAt = userDAO.insert(user);
                if(insertedAt > -1)
                {
                    Toast.makeText(getApplicationContext(),"Logged as  = " + user.getName().toString() ,Toast.LENGTH_LONG).show();
                    Log.d("UsrFace", user.toString());

                    goToHome();
                }


/*
                fbProfileName = currentProfile.getName();

                Toast.makeText(getBaseContext(), fbProfileName.toString(), Toast.LENGTH_LONG).show();


                Log.d(TAG, "FirstName: " + currentProfile.getFirstName());

                Log.d(TAG, "LastName: " + currentProfile.getLastName());

                Log.d(TAG, " MiddleName: " + currentProfile.getMiddleName());

                Log.d(TAG, "LinkUri: " + currentProfile.getLinkUri());

                Log.d(TAG, "ProfilePictureUri: " + currentProfile.getProfilePictureUri(250, 250));*/


            }
        };
    }


    public void doLogin()
    {
        Log.d("Method", "doLogin()");

        userDAO = new UserDAO(this);
        //TODO Validar e-mail
        if(etEmail.getText().toString().length() > 6){
            if(etPassword.getText().toString().trim().length() >= 6)
            {
                User user = userDAO.isValidCredentials(etEmail.getText().toString(), etPassword.getText().toString());
                if(user != null){
                    goToHome();
                }
                else
                {
                    Toast.makeText(this, "E-mail e/ou Senha inválido", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, "A senha contém no mínimo 6 caracteres", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Digite um e-mail válido", Toast.LENGTH_LONG).show();
        }
    }

    public void goToHome(){
        Log.d("ChangeActivity", "Login > Home");
        Intent i = new Intent(Login.this, Home.class);
        Login.this.startActivity(i);
    }

}