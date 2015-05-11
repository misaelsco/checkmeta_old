package br.com.correiam.checkmeta;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.DatagramChannel;
import java.util.List;

import br.com.correiam.checkmeta.adapters.AdapterMeta;
import br.com.correiam.checkmeta.dao.MetasDAO;
import br.com.correiam.checkmeta.dominio.Meta;


public class Home extends ActionBarActivity{

    private ListView listMetas;
    private MetasDAO daoMeta;
    private Long idSelecionado = Long.valueOf(-1);
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this.getBaseContext();

        daoMeta = new MetasDAO(this);
        listMetas = (ListView) findViewById(R.id.listMetas);

        listMetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listMetas.getItemAtPosition(position);
                TextView itemID = (TextView) view.findViewById(R.id.tvIdMeta);
                //                Toast.makeText(context, "Id da meta: " + itemID.getText().toString(), Toast.LENGTH_SHORT).show();

                Log.d("ChangeActivity", "Home > Meta");
                Intent i = new Intent(Home.this, MetaActivity.class);
                i.putExtra("metaId", itemID.getText().toString());
                Home.this.startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarMetas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_new_meta:
                Log.d("ChangeActivity", "Login > Home");
                Intent i = new Intent(Home.this, MetaActivity.class);
                Home.this.startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregarMetas(){
        List<Meta> metas = daoMeta.selectAll();
        AdapterMeta adapterMeta = new AdapterMeta(Home.this, R.layout.list_meta_row, metas);
        listMetas.setAdapter(adapterMeta);
        adapterMeta.notifyDataSetChanged();
    }
}
