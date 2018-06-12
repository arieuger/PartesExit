package es.dosxmil.partesexit.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import es.dosxmil.partesexit.R;
import es.dosxmil.partesexit.utils.Utils;

public class CursorAdapterParteCabecera extends SimpleCursorAdapter {
    private final int resource;
    private LayoutInflater cursorInflater;

    public CursorAdapterParteCabecera(Context context, Cursor c, int flags) {

        super(context, R.layout.item_parte_cabecera, c, new String[]{}, new int[]{}, flags);

        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        resource = R.layout.item_parte_cabecera;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvPCNombre = (TextView) view.findViewById(R.id.tvItemParteCabeceraNombre);
        TextView tvPCEjercicio = (TextView) view.findViewById(R.id.tvItemParteCabeceraEjercicio);
        TextView tvPCEmpleado = (TextView) view.findViewById(R.id.tvItemParteCabeceraEmpleado);
        TextView tvPCFecha = (TextView) view.findViewById(R.id.tvItemParteCabeceraFecha);
        TextView tvPCImporte = (TextView) view.findViewById(R.id.tvItemParteCabeceraImporte);
        TextView tvPCNumero = (TextView) view.findViewById(R.id.tvItemParteCabeceraNumero);
        TextView tvPCSerie = (TextView) view.findViewById(R.id.tvItemParteCabeceraSerie);
        ImageView ivPCStatus = (ImageView) view.findViewById(R.id.ivItemParteCabeceraEstado);

        String nombre = cursor.getString(cursor.getColumnIndex("Nombre"));
        String ejercicio = cursor.getString(cursor.getColumnIndex("EjercicioParte"));
        String empleado = cursor.getString(cursor.getColumnIndex("CodigoEmpleado"));
        String fecha = cursor.getString(cursor.getColumnIndex("FechaParte"));
        String importe = cursor.getString(cursor.getColumnIndex("Importe"));
        String numero = cursor.getString(cursor.getColumnIndex("NumeroParte"));
        String serie = cursor.getString(cursor.getColumnIndex("SerieParte"));

        String status = cursor.getString(cursor.getColumnIndex("StatusParte"));
        switch (status) {
            case "P":
                ivPCStatus.setImageResource(R.drawable.p_p);
                break;
            case "A":
                ivPCStatus.setImageResource(R.drawable.p_a);
                break;
            case "C":
                ivPCStatus.setImageResource(R.drawable.p_c);
                break;

        }


        tvPCNombre.setText(nombre);
        tvPCEjercicio.setText(ejercicio);
        tvPCEmpleado.setText(empleado);
        tvPCFecha.setText(Utils.FormatoFecha(fecha));
        tvPCImporte.setText(importe);
        tvPCNumero.setText(numero);
        tvPCSerie.setText(serie);

        

    }





}
