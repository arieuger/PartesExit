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

public class CursorAdapterParteLineas extends SimpleCursorAdapter {
    public CursorAdapterParteLineas(Context context, Cursor c, int flags) {
        super(context, R.layout.item_parte_lineas, c, new String[]{}, new int[]{}, flags);

        LayoutInflater cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void bindView(View view, Context context, Cursor c) {

        TextView tvDescripcion = (TextView) view.findViewById(R.id.tvItemLineaDescripcion);
        TextView tvOrden = (TextView) view.findViewById(R.id.tvItemLineaOrden);
        TextView tvCodigoEmpleado = (TextView) view.findViewById(R.id.tvItemLineaCodigoEmpleado);
        TextView tvNombreEmpleado = (TextView) view.findViewById(R.id.tvItemLineaNombreEmpleado);
        TextView tvFechaRegistro = (TextView) view.findViewById(R.id.tvItemLineaFechaRegistro);
        TextView tvFechaEntrega = (TextView) view.findViewById(R.id.tvItemLineaFechaEntrega);
        ImageView ivGastos = (ImageView) view.findViewById(R.id.ivItemLineaGastos);
        ImageView ivFacturable = (ImageView) view.findViewById(R.id.ivItemLineaFacturable);
        TextView tvUnidades = (TextView) view.findViewById(R.id.tvItemLineaUnidades);
        TextView tvPrecio = (TextView) view.findViewById(R.id.tvItemLineaPrecio);
        TextView tvImporte = (TextView) view.findViewById(R.id.tvItemLineaImporte);


        String descripcion = c.getString(c.getColumnIndex("DescripcionArticulo"));
        int orden = c.getInt(c.getColumnIndex("Orden"));
        int codigoEmpleado = c.getInt(c.getColumnIndex("CodigoEmpleado"));
        String nombreEmpleado = c.getString(c.getColumnIndex("NombreCompleto"));
        String fechaRegistro = c.getString(c.getColumnIndex("FechaRegistro"));
        String fechaEntrega = c.getString(c.getColumnIndex("MIL_FechaEntrega"));
        int gastos = c.getInt(c.getColumnIndex("Gastos"));
        int facturable = c.getInt(c.getColumnIndex("Facturable"));
        double unidades = c.getDouble(c.getColumnIndex("Unidades"));
        double precio = c.getDouble(c.getColumnIndex("Precio"));
        double importe = c.getDouble(c.getColumnIndex("Importe"));


        tvDescripcion.setText(descripcion);
        tvOrden.setText(orden + "");
        tvCodigoEmpleado.setText(codigoEmpleado + "");
        tvNombreEmpleado.setText(nombreEmpleado);
        tvFechaRegistro.setText(Utils.FormatoFecha(fechaRegistro));
        tvFechaEntrega.setText(Utils.FormatoFecha(fechaEntrega));

        if (gastos == 0) ivGastos.setImageResource(R.drawable.ic_clear_black_24dp);
        else ivGastos.setImageResource(R.drawable.ic_done_black_24dp);

        if (facturable == 0) ivFacturable.setImageResource(R.drawable.ic_clear_black_24dp);
        else ivFacturable.setImageResource(R.drawable.ic_done_black_24dp);

        tvUnidades.setText(unidades + "");
        tvPrecio.setText(precio + "");
        tvImporte.setText(importe + "");

    }
}
