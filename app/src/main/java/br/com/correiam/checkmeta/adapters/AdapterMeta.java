package br.com.correiam.checkmeta.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.correiam.checkmeta.R;
import br.com.correiam.checkmeta.dominio.Meta;
import br.com.correiam.checkmeta.dominio.User;

/**
 * Created by Misael Correia on 04/05/15.
 * misaelsco@gmail.com
 */
public class AdapterMeta extends ArrayAdapter<Meta>{

    private int resource;
    private LayoutInflater inflater;

    public AdapterMeta(Context context, int resourceId, List<Meta> metas){
        super(context,resourceId,metas);
        resource = resourceId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(convertView == null){
            view = inflater.inflate(resource,null);
        }

        Meta meta = getItem(position);

        TextView tvIdMeta = (TextView) view.findViewById(R.id.tvIdMeta);
        tvIdMeta.setText(meta.getId().toString());

        TextView tvNome = (TextView)view.findViewById(R.id.tvName);
        tvNome.setText(meta.getName().toString());

        TextView tvDescription = (TextView)view.findViewById(R.id.tvDescription);
        tvDescription.setText(meta.getDescription().toString());

        TextView tvDueDate = (TextView)view.findViewById(R.id.tvMetaDueDate);
        tvDueDate.setText(meta.getDueDate().toString());

        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatusMeta);
        tvStatus.setText(meta.getState().toString());
        if(meta.getState().toString().equals("Atrasada"))
            tvStatus.setTextColor(Color.RED);
        if(meta.getState().toString().equals("Pendente"))
            tvStatus.setTextColor(Color.BLUE);
        if(meta.getState().toString().equals("Realizada"))
            tvStatus.setTextColor(Color.BLACK);
        if(meta.getState().toString().equals("Despriorizada"))
            tvStatus.setTextColor(Color.BLACK);

        return view;

    }

}
