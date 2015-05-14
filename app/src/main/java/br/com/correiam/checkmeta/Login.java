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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.correiam.checkmeta.dao.UserDAO;
import br.com.correiam.checkmeta.dominio.User;


public class Login extends Activity implements View.OnClickListener {


    private LoginButton loginButton;
    private TextView tvCadastrar;
    private EditText etEmail;
    private EditText etPassword;
    private UserDAO userDAO;
    private CallbackManager callbackManager;
    private Context context;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        context = this.getBaseContext();

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, mCallBack);

        tvCadastrar = (TextView) findViewById(R.id.tvCadastroLink);
        tvCadastrar.setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        if(loginButton.getText().equals("Log out"))
        {
            Intent i = new Intent(Login.this, Home.class);
            Login.this.startActivity(i);
        }

    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if(profile != null){
                Toast.makeText(context, "Bem vindo " + profile.getName(), Toast.LENGTH_SHORT).show();
                //TODO Salvar as informações do perfil
                goToHome();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(context, "Cancelou", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.tvCadastroLink:
                onClickCadastrar();
                break;
            case R.id.btnLogin:
                doLogin();
                break;
        }
    }



    //Chama a tela de cadastro quando clicado no TextView com a frase de cadastro
    public void onClickCadastrar()
    {
        Log.d("Teste", "entrei na tela de cadastro");
        Intent iCadastro  = new Intent(Login.this, CadastroActivity.class);
        Login.this.startActivity(iCadastro);
    }

    public void doLogin()
    {
        //TODO Inserir log
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
        Intent i = new Intent(Login.this, Home.class);
        Login.this.startActivity(i);
        this.finish();
    }

}