package br.com.correiam.checkmeta;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.correiam.checkmeta.dao.MetasDAO;
import br.com.correiam.checkmeta.dao.UserDAO;
import br.com.correiam.checkmeta.dominio.Meta;
import br.com.correiam.checkmeta.dominio.User;

public class MetaActivity extends ActionBarActivity {

    private EditText etIdMeta;
    private EditText etName;
    private EditText etDescription;
    private Button btnDueDate;
    private Button btnActualDate;
    private TextView tvState;
    private TextView tvActualDate;
    private Spinner spState;
    private AlertDialog delete_alert;
    static final int DUEDATE = 0;
    static final int ACTUALDATE = 1;
    private int year;
    private int month;
    private int day;

    private Meta oldMeta;

    int cur = 0;

    private MetasDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta);

        dao = new MetasDAO(this);

        etName = (EditText) findViewById(R.id.etName);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnDueDate = (Button) findViewById(R.id.etDueDate);

        btnDueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DUEDATE);
            }
        });

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        if(getIntent().hasExtra("metaId")){
            Bundle extras = getIntent().getExtras();
            String idMeta = extras.getString("metaId");

            btnActualDate = (Button) findViewById(R.id.etActualDate);
            etIdMeta = (EditText) findViewById(R.id.tvIdMetaActivity);
            tvActualDate = (TextView) findViewById(R.id.tvActualDate);
            tvState = (TextView) findViewById(R.id.tvState);
            spState = (Spinner) findViewById(R.id.spState);

            //Preenchendo os campos da meta
            oldMeta = dao.select(idMeta);
            etIdMeta.setText(oldMeta.getId().toString());
            etName.setText(oldMeta.getName());
            etDescription.setText(oldMeta.getDescription());
            btnDueDate.setText(oldMeta.getDueDate());
            spState.setSelection(selectSpinner(oldMeta.getState()));
            btnActualDate.setText(oldMeta.getActualDate());


            btnActualDate.setVisibility(View.VISIBLE);
            tvState.setVisibility(View.VISIBLE);
            tvActualDate.setVisibility(View.VISIBLE);
            spState.setVisibility(View.VISIBLE);

            btnActualDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showDialog(ACTUALDATE);
                }
            });

        }
    }

    private int selectSpinner(String text){
        if(text.equals("Pendente"))
            return 0;
        if(text.equals("Atrasada"))
            return 1;
        if(text.equals("Realizada"))
            return 2;
        if(text.equals("Despriorizada"))
            return 3;

        return 0;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String date = df.format(calendar.getTime());

            if(cur == DUEDATE){
                btnDueDate.setText(date);
            }
            else{
                btnActualDate.setText(date);
            }

        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DUEDATE:
                cur = DUEDATE;
                return new DatePickerDialog(this,
                        mDateSetListener,
                        year, month, day);
            case ACTUALDATE:
                cur = ACTUALDATE;
                return new DatePickerDialog(this,
                        mDateSetListener,
                        year, month, day);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meta, menu);

        if(getIntent().hasExtra("metaId")){
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        Boolean finished = false;

        switch (id) {
            case R.id.action_salvar:
                salvar();
                finished = true;
                break;
            case R.id.action_delete:
                delete();
                finished = true;
                break;
        }

        return finished;
    }

    public void delete(){
        final String idMeta = etIdMeta.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmação");
        builder.setMessage("Deseja excluir esta meta?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1){
                if(dao.delete(idMeta)){
                    Toast.makeText(MetaActivity.this, "Meta excluída com sucesso", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MetaActivity.this, "Erro ao excluir meta", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Log.d("DEBUG", "Usuário cancelou a deleção de uma meta");
            }
        });

        delete_alert = builder.create();
        delete_alert.show();
    }

    private void salvar(){
        if(etIdMeta != null)
        {
            editar();
        } else {
            cadastrar();
        }
    }

    public void cadastrar() {
        if(validationFields()) {
            Meta meta = new Meta();
            meta.setName(etName.getText().toString());
            meta.setDescription(etDescription.getText().toString());
            meta.setDueDate(btnDueDate.getText().toString());

            Long insertedId = dao.insert(meta);
            if (insertedId > -1) {
                Toast.makeText(MetaActivity.this, "Meta cadastrada com sucesso", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Log.d("ERROR", "Erro ao cadastrar meta");
                Toast.makeText(MetaActivity.this, "Erro ao cadastrar meta", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void editar(){

        if(validationFields()){
            Meta meta = new Meta();
            meta.setId(Long.parseLong(etIdMeta.getText().toString()));
            meta.setName(etName.getText().toString());
            meta.setDescription(etDescription.getText().toString());
            meta.setDueDate(btnDueDate.getText().toString());
            meta.setActualDate(btnActualDate.getText().toString());
            meta.setState(spState.getSelectedItem().toString());

            //Compara se o usuário fez alterações na Meta
            if(!oldMeta.equals(meta)) {
                boolean updatedId = dao.update(meta);
                if (updatedId) {
                    Toast.makeText(MetaActivity.this, "Meta editada com sucesso", Toast.LENGTH_LONG).show();
                    this.finish();
                } else {
                    Toast.makeText(MetaActivity.this, "Falha ao editar meta", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                this.finish();
            }
        }
    }

    private boolean validationFields(){
        if (etName.getText().toString().trim().length() >= 5) {
            if (etDescription.getText().toString().trim().length() >= 5) {
                if(!btnDueDate.getText().equals("")){
                    return true;
                } else {
                    Toast.makeText(this, "Campo 'Data prevista' deve ser preenchido", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Campo 'Descrição' deve conter no mínimo 5 caracteres", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Campo 'Nome da meta' deve conter no mínimo 5 caracteres", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}

