package es.dosxmil.partesexit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.dosxmil.partesexit.R;
import es.dosxmil.partesexit.mapeo.Empresa;

public class ArrayAdapterEmpresas extends ArrayAdapter<Empresa> {
    private final int resource;

    public ArrayAdapterEmpresas(@NonNull Context context, @NonNull List<Empresa> objects) {
        super(context, R.layout.item_empresa, objects);
        resource = R.layout.item_empresa;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View fila = inflater.inflate(resource, null);

        Empresa e = getItem(position);

        TextView tvCodigoEmpresa = (TextView) fila.findViewById(R.id.tvItemEmpresaCodigo);
        TextView tvNombreEmpresa = (TextView) fila.findViewById(R.id.tvItemEmpresaNombre);
        TextView tvNombreEmpleado = (TextView) fila.findViewById(R.id.tvItemEmpresaNombreEmpleado);

        tvCodigoEmpresa.setText(e.getCodigoEmpresa() + "");
        tvNombreEmpresa.setText(e.getEmpresa());
        tvNombreEmpleado.setText(e.getNombreEmpleado());


        return fila;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View fila = inflater.inflate(resource, null);

        Empresa e = getItem(position);

        TextView tvCodigoEmpresa = (TextView) fila.findViewById(R.id.tvItemEmpresaCodigo);
        TextView tvNombreEmpresa = (TextView) fila.findViewById(R.id.tvItemEmpresaNombre);
        TextView tvNombreEmpleado = (TextView) fila.findViewById(R.id.tvItemEmpresaNombreEmpleado);

        tvCodigoEmpresa.setText(e.getCodigoEmpresa() + "");
        tvNombreEmpresa.setText(e.getEmpresa());
        tvNombreEmpleado.setText(e.getNombreEmpleado());

        return fila;
    }
}
