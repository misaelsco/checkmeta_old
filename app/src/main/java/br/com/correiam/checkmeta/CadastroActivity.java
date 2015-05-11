package br.com.correiam.checkmeta;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.correiam.checkmeta.dao.UserDAO;
import br.com.correiam.checkmeta.dominio.User;


public class CadastroActivity extends ActionBarActivity implements View.OnClickListener  {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private Boolean validEmail = false;


    private UserDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dao = new UserDAO(this);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

        btnRegister = (Button) this.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        etEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (etEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && s.length() > 6)
                {
                    validEmail = !validEmail;
                }
                else{ validEmail = false;}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRegister:
                salvar();
                break;
        }
    }

    public void salvar(){
        if(etName.getText().toString().trim().length() >= 3){
            if(validEmail)
            {
                if(etPassword.getText().toString().trim().length() >= 6)
                {
                    if(etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                        if (!dao.isDuplicated(etEmail.getText().toString())) {
                            User user = new User();
                            user.setName(etName.getText().toString());
                            user.setEmail(etEmail.getText().toString());
                            user.setPassword(etPassword.getText().toString());

                            Long insertedId = dao.insert(user);

                            if (insertedId > -1) {
                                Toast.makeText(CadastroActivity.this, "Usuário registrado com sucesso", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(CadastroActivity.this, Home.class);
                                CadastroActivity.this.startActivity(intent);
                                this.finish();
                            } else {
                                Toast.makeText(CadastroActivity.this, "Falha ao registrar usuário", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this, "Este e-mail já existe em nosso banco de dados.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this,"A senha e confirmação de senha não são iguais", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"A senha deve conter no mínimo 6 caracteres", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"Digite um e-mail válido", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this,"Campo 'Nome' deve conter no mínimo 3 caracteres", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
