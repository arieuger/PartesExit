package es.dosxmil.partesexit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dosxmil.partesexit.adapters.CursorAdapterParteLineas;
import es.dosxmil.partesexit.mapeo.Articulo;
import es.dosxmil.partesexit.mapeo.ParteCabecera;
import es.dosxmil.partesexit.mapeo.ParteLinea;
import es.dosxmil.partesexit.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ParteLineasActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ApiService apiService;
    Retrofit cliente;

    private ParteCabecera pc;
    private ListView lvParteLineas;
    private TextView tvFacturable;
    private TextView tvGastos;
    private TextView tvImporte;

    private ArrayList<Long> seleccionados = new ArrayList<>();
    private ArrayList<Articulo> busquedaArticulos = new ArrayList<>();

    private SharedPreferences sp;
    private ParteLinea pl;
    private int codigoEmpresa;
    private int numeroParte;
    private int ejercicioParte;
    private String serieParte;
    private TextView tvADPrecio;
    private EditText etADDescripcionArticulo;
    private TextView tvADImporte;
    private Spinner spADSeleccionArticuloPDesc;
    private EditText etADCodigoArticulo;
    private TextView tvADCodigoEmpleado;
    private TextView tvADNombreEmpleado;
    private EditText etADFechaRegistro;
    private EditText etADFechaEjecucion;
    private EditText etADUnidades;
    private CheckBox cbADFacturable;
    private CheckBox cbADGastos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parte_lineas);

        pc = getIntent().getParcelableExtra("parteCabecera");

        codigoEmpresa = pc.getCodigoEmpresa();
        ejercicioParte = pc.getEjercicioParte();
        serieParte = pc.getSerieParte();
        numeroParte = pc.getNumeroParte();

        sp = this.getSharedPreferences("sp", MODE_PRIVATE);

        lvParteLineas = (ListView) findViewById(R.id.lvParteLineasActivity);
        tvFacturable = (TextView) findViewById(R.id.tvParteLineasFacturable);
        tvImporte = (TextView) findViewById(R.id.tvParteLineasImporte);
        tvGastos = (TextView) findViewById(R.id.tvParteLineasGastos);

        cliente = RetrofitClient.getClient();
        apiService = cliente.create(ApiService.class);

        if (pc.getPlDescargado() == 0) {
            apiService.getParteLineas("Bearer " + sp.getString("TOKEN",""),
                                        pc.getCodigoEmpresa(),
                                        pc.getEjercicioParte(),
                                        pc.getSerieParte(),
                                        pc.getNumeroParte())
                    .enqueue(new Callback<List<ParteLinea>>() {
                        @Override
                        public void onResponse(Call<List<ParteLinea>> call, retrofit2.Response<List<ParteLinea>> response) {
                            List<ParteLinea> listaLineas = response.body();
                            Log.d("retrofit pl " , listaLineas.size() + "");
                            for (ParteLinea pl : listaLineas) {

                                ParteLinea.guardar(pl);
                            }
                            pc.setPlDescargado(1);
                            ParteCabecera.actualizar(pc);
                            fillLvParteLineas();
                        }

                        @Override
                        public void onFailure(Call<List<ParteLinea>> call, Throwable t) {
                            Log.d("retrofit pl",t.getMessage());
                        }
                    });
        }

        else fillLvParteLineas();

        cargaInfoPc();

        lvParteLineas.setOnItemClickListener(this);
        lvParteLineas.setOnItemLongClickListener(this);
    }

    private void cargaInfoPc() {
        tvGastos.setText(pc.getImporteGastos() + "");
        tvImporte.setText(pc.getImporte() + "");
        if (pc.getImporteFacturable() >= 0) tvFacturable.setText(pc.getImporteFacturable() + "");
        else tvFacturable.setText("0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_parte_lineas_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_parte_lineas_toolbar_borrar:
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                final String plural = seleccionados.size() == 1 ? " ":"s ";
                String s = getResources().getString(R.string.ad_parte_lineas_confirmar_borrado_desc,seleccionados.size(), plural);
                adb.setMessage(s);
                adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ParteLineasActivity.this,
                                "Borrado" + plural + ParteLinea.borrarPartePorID(seleccionados) + " elemento" + plural,
                                Toast.LENGTH_SHORT).show();
                        seleccionados.clear();
                        ParteCabecera.actualizaImportes(pc);
                        fillLvParteLineas();
                        cargaInfoPc();
                    }
                });
                adb.setNegativeButton(android.R.string.cancel,null);
                adb.setCancelable(false);
                adb.show();
                break;

            case R.id.mi_parte_lineas_toolbar_editar:
                if (seleccionados.size() > 1)
                    Toast.makeText(this, R.string.ad_parte_lineas_seleccion_solo_una, Toast.LENGTH_SHORT).show();
                else if (seleccionados.size() == 0)
                    Toast.makeText(this, R.string.ad_parte_lineas_seleccion_una, Toast.LENGTH_SHORT).show();
                else {
                    pl = ParteLinea.getLineaPorID(seleccionados.get(0));
                    cargaADEdicionPL(pl);
                }
                break;
            case R.id.mi_parte_lineas_toolbar_anadir:
                pl = null;
                cargaADEdicionPL(pl);
                break;
        }
        return true;
    }

    private void cargaADEdicionPL(final ParteLinea pl) {

        seleccionados.clear();

        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ad_edicion_pl, null);
        adb.setView(dialogView);
        AlertDialog ad = adb.create();
        ad.setCancelable(false);

        // findviewbyids
        etADCodigoArticulo = (EditText) dialogView.findViewById(R.id.etEdicionPlCodigoArticulo);
        tvADCodigoEmpleado = (TextView) dialogView.findViewById(R.id.tvEdicionPlCodigoEmpleado);
        tvADNombreEmpleado = (TextView) dialogView.findViewById(R.id.tvEdicionPlNombreEmpleado);
        etADFechaRegistro = (EditText) dialogView.findViewById(R.id.etEdicionPlFecha);
        etADFechaEjecucion = (EditText) dialogView.findViewById(R.id.etEdicionPlFechaEjec);
        cbADGastos = (CheckBox) dialogView.findViewById(R.id.cbEdicionPLGastos);
        cbADFacturable = (CheckBox) dialogView.findViewById(R.id.cbEdicionPLFacturable);
        etADUnidades = (EditText) dialogView.findViewById(R.id.etEdicionPLUnidades);

        final ImageView ivADBuscar = (ImageView) dialogView.findViewById(R.id.ivEdicionPlBuscarArticulo);
        final ImageView ivADBuscarPDesc = (ImageView) dialogView.findViewById(R.id.ivEdicionPlBuscarArticuloPDesc);

        etADDescripcionArticulo = (EditText) dialogView.findViewById(R.id.etEdicionPlDescripcionArticulo);
        tvADPrecio = (TextView) dialogView.findViewById(R.id.tvEdicionPLPrecio);
        tvADImporte = (TextView) dialogView.findViewById(R.id.tvEdicionPLImporte);
        spADSeleccionArticuloPDesc = (Spinner) dialogView.findViewById(R.id.spEdicionPlSeleccionArticuloPDesc);

        ivADBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etADCodigoArticulo.getText().toString() != "") {
                    Articulo a = Articulo.buscarPorCodigo(etADCodigoArticulo.getText().toString());
                    etADDescripcionArticulo.setText(a.getDescripcionArticulo());
                    tvADPrecio.setText(a.getPrecioNetoVenta() +"");
                    tvADImporte.setText(a.getPrecioNetoVenta() * Double.valueOf(etADUnidades.getText().toString()) +"");
                }
            }
        });

        ivADBuscarPDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spADSeleccionArticuloPDesc.setVisibility(View.GONE);

                busquedaArticulos.clear();
                busquedaArticulos.add(new Articulo("0","Selecciona un artículo",0));
                busquedaArticulos.addAll(Articulo.buscarPorDesc(etADDescripcionArticulo.getText().toString()));

                fillSeleccionArticulo();

            }
        });


        cargaDatosAD();

        // Cando cambia o texto de unidades, actualízase o importe total
        etADUnidades.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utils.isDecimal(s.toString())) {
                    Double importe = Double.parseDouble(s.toString()) * Double.parseDouble(tvADPrecio.getText().toString());
                    tvADImporte.setText(importe + "");
                }
            }

        });

        ad.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getText(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int orden;
                String descripcionLinea;
                ParteLinea plGuardar = null;
                pc.setSincroMovil(0);

                // Gardar os novos valores no obxeto
                if (pl == null) {
                    // nova liña
                    orden = ParteLinea.getLastOrdenPl(pc) + 5;
                    descripcionLinea = "";
                } else {
                    orden = pl.getOrden();
                    descripcionLinea = pl.getDescripcionLinea();
                }
                try {
                    plGuardar = new ParteLinea(codigoEmpresa, ejercicioParte, serieParte, numeroParte, orden,
                            etADCodigoArticulo.getText().toString(), etADDescripcionArticulo.getText().toString(),
                            descripcionLinea, pc.getFechaParte(), Utils.FormatoATimestamp(etADFechaRegistro.getText().toString()).toString(),
                            Utils.FormatoATimestamp(etADFechaEjecucion.getText().toString()).toString(),
                            Integer.parseInt(tvADCodigoEmpleado.getText().toString()), tvADNombreEmpleado.getText().toString(),
                            Double.parseDouble(tvADPrecio.getText().toString()), Double.parseDouble(tvADImporte.getText().toString()),
                            cbADGastos.isChecked() ? -1 : 0, Double.parseDouble(etADUnidades.getText().toString()), cbADFacturable.isChecked() ? -1 : 0);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (pl == null) ParteLinea.guardar(plGuardar);
                else ParteLinea.actualizar(plGuardar);

                ParteCabecera.actualizaImportes(pc);

                // Actualizar visualización de info
                cargaInfoPc();
                fillLvParteLineas();
            }
        });

        ad.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getText(android.R.string.cancel), null, null);

        ad.show();

    }


    private void fillSeleccionArticulo() {
        if (busquedaArticulos.size() == 2) {
            Articulo a = busquedaArticulos.get(1);
            etADCodigoArticulo.setText(a.getCodigoArticulo());
            etADDescripcionArticulo.setText(a.getDescripcionArticulo());
            tvADPrecio.setText(a.getPrecioNetoVenta()+"");
            tvADImporte.setText(a.getPrecioNetoVenta() * Double.valueOf(etADUnidades.getText().toString()) +"");

        }

        if (busquedaArticulos.size() > 2) {

            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < busquedaArticulos.size(); i++) {
                strings.add(busquedaArticulos.get(i).getDescripcionArticulo());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ParteLineasActivity.this,
                    android.R.layout.simple_spinner_item, strings);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spADSeleccionArticuloPDesc.setAdapter(adapter);
            spADSeleccionArticuloPDesc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(ParteLineasActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    if (position != 0) {
                        Articulo a = busquedaArticulos.get(position);

                        etADCodigoArticulo.setText(a.getCodigoArticulo());
                        etADDescripcionArticulo.setText(a.getDescripcionArticulo());
                        tvADPrecio.setText(a.getPrecioNetoVenta()+"");
                        tvADImporte.setText(a.getPrecioNetoVenta() * Double.valueOf(etADUnidades.getText().toString()) +"");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            spADSeleccionArticuloPDesc.setVisibility(View.VISIBLE);
        }
    }

    private void cargaDatosAD() {

        if (pl != null) {   // Si pl é distinto de null, significará que estamos editando un parte, non creándoo
            etADCodigoArticulo.setText(pl.getCodigoArticulo());
            etADDescripcionArticulo.setText(pl.getDescripcionArticulo());
            tvADCodigoEmpleado.setText(pl.getCodigoEmpleado() + "");
            tvADNombreEmpleado.setText(pl.getNombreCompleto());
            etADFechaRegistro.setText(Utils.FormatoFecha(pl.getFechaParte()));
            etADFechaEjecucion.setText(Utils.FormatoFecha(pl.getFechaEntrega()));
            cbADGastos.setChecked(pl.getGastos() != 0);
            cbADFacturable.setChecked(pl.getFacturable() != 0);
            etADUnidades.setText(pl.getUnidades() + "");
            tvADPrecio.setText(pl.getPrecio() + "");
            tvADImporte.setText(pl.getImporte() + "");

        } else {    // Si é null, crearemos liña nova
            tvADCodigoEmpleado.setText(sp.getString("CODIGO_PLANTILLA", "0"));
            tvADNombreEmpleado.setText(sp.getString("NOMBRE_COMPLETO", ""));
            etADFechaRegistro.setText(Utils.FormatoFecha(new Date()));
            etADFechaEjecucion.setText(Utils.FormatoFecha(new Date()));
        }


    }


    private void fillLvParteLineas() {
        Cursor c = ParteLinea.getAllCursor(pc.getCodigoEmpresa(), pc.getEjercicioParte(), pc.getSerieParte(), pc.getNumeroParte());
        CursorAdapterParteLineas capl = new CursorAdapterParteLineas(this, c, 0);

        lvParteLineas.setAdapter(capl);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ParteLineasActivity.this);
        final EditText input = new EditText(ParteLineasActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(50, 50, 50, 50);
        input.setPadding(50, 50, 50, 50);

        input.setLayoutParams(lp);
        input.setText(ParteLinea.getLineaPorID(id).getDescripcionLinea());
        builder.setView(input);

        pl = ParteLinea.getLineaPorID(id);

        builder.setTitle(R.string.ad_parte_lineas_descripcion);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pl.setDescripcionLinea(input.getText().toString());
                ParteLinea.actualizar(pl);
                fillLvParteLineas();
                Toast.makeText(ParteLineasActivity.this, R.string.toast_parte_lineas_descripcion_guardada, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!seleccionados.contains(id)) {
            seleccionados.add(id);
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            String s = "";
            for (long l : seleccionados) {
                s += l + " ";
            }
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        } else {
            seleccionados.remove(id);
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        return true;
    }

}
