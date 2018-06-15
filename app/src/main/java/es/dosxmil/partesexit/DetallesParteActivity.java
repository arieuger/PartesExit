package es.dosxmil.partesexit;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import es.dosxmil.partesexit.mapeo.Cliente;
import es.dosxmil.partesexit.mapeo.ParteCabecera;
import es.dosxmil.partesexit.mapeo.Proyecto;
import es.dosxmil.partesexit.servicioweb.ApiService;
import es.dosxmil.partesexit.servicioweb.RetrofitClient;
import es.dosxmil.partesexit.utils.DatePickerFragment;
import es.dosxmil.partesexit.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class DetallesParteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private SimpleDateFormat sdf;
    private SharedPreferences sp;

    private ParteCabecera p;

    private List<Proyecto> proyectos = new ArrayList<>();
    private ArrayList<Pair<String, String>> busquedaClientes = new ArrayList<>();

    private TextView tvSerie;
    private TextView tvNumeroParte;
    private TextView tvEjercicio;
    private EditText etFecha;
    private EditText etFechaEjec;
    private TextView tvNombreEmpleado;
    private TextView tvNumeroEmpleado;
    private TextView tvNombreProyecto;
    private TextView tvNumeroProyecto;
    private ImageView ivEstadoParte;
    private TextView tvRSCliente;
    private TextView tvDomicilioCliente;
    private TextView tvNumeroCliente;
    private TextView tvCifDniCliente;
    private SearchView searchView;
    private EditText etCRecepcion;
    private EditText etCCierre;
    private TextView tvFacturable;
    private TextView tvGastos;
    private TextView tvImporte;
    private long parteID;
    private int codigoEmpresa;

    ApiService apiService;
    Retrofit cliente;

    private LinearLayout llADN1;
    private LinearLayout llADN2;
    private LinearLayout llADN3;
    private TextView tvNPDomicilioCliente;
    private TextView tvNPRSCliente;
    private Spinner spNPNombreProyecto;
    private EditText etNPCifDni;
    private EditText etNPNombre;
    private Cliente c;
    private Spinner spADBusquedaNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_parte);
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        parteID = getIntent().getLongExtra("idParte", 0);
        sp = this.getSharedPreferences("sp", MODE_PRIVATE);
        codigoEmpresa = sp.getInt("EMPRESA_DFLT",0);


        // FINDVIEWS
        tvEjercicio = (TextView) findViewById(R.id.tvDetallesParteEjercicio);
        tvNumeroParte = (TextView) findViewById(R.id.tvDetallesParteNumero);
        tvSerie = (TextView) findViewById(R.id.tvDetallesParteSerie);
        etFecha = (EditText) findViewById(R.id.etDetallesParteFecha);
        etFechaEjec = (EditText) findViewById(R.id.etDetallesParteFechaEjec);
        tvNumeroEmpleado = (TextView) findViewById(R.id.tvDetallesParteNumeroEmpleado);
        tvNombreEmpleado = (TextView) findViewById(R.id.tvDetallesParteNombreEmpleado);
        tvNumeroProyecto = (TextView) findViewById(R.id.tvDetallesParteNumeroProyecto);
        tvNombreProyecto = (TextView) findViewById(R.id.tvDetallesParteNombreProyecto);

        ivEstadoParte = (ImageView) findViewById(R.id.ivDetallesParteEstado);

        tvRSCliente = (TextView) findViewById(R.id.tvDetallesParteRazonSocialCliente);
        tvCifDniCliente = (TextView) findViewById(R.id.tvDetallesParteCifDniCliente);
        tvNumeroCliente = (TextView) findViewById(R.id.tvDetallesParteNumeroCliente);
        tvDomicilioCliente = (TextView) findViewById(R.id.tvDetallesParteDomicilioCliente);

        etCRecepcion = (EditText) findViewById(R.id.etDetallesParteCRecepcion);
        etCCierre = (EditText) findViewById(R.id.etDetallesParteCCierre);

        tvGastos = (TextView) findViewById(R.id.tvDetallesParteGastos);
        tvImporte = (TextView) findViewById(R.id.tvDetallesParteImporte);
        tvFacturable = (TextView) findViewById(R.id.tvDetallesParteFacturable);


        etFecha.setOnClickListener(this);
        etFechaEjec.setOnClickListener(this);
        ivEstadoParte.setOnClickListener(this);

        cliente = RetrofitClient.getClient();
        apiService = cliente.create(ApiService.class);

        // CAMBIOS PARTE XA CREADO / NOVO PARTE
        // Si non hai intent de id, entramos na pantalla para crear un parte novo. Así que non cargamos info
        if (parteID != 0) cargaInfo();
        else cargaNuevoParte();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar parteCabecera dende BD, para evitar duplicados por non estar PlDescargado actualizado
        // p = ParteCabecera.getByID(parteID);
        if (p != null) cargaInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalles_parte_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_detalles_parte_toolbar_lineas:
                Intent intent = new Intent(this, ParteLineasActivity.class);
                intent.putExtra("parteCabecera", p);
                startActivity(intent);
                break;
            case R.id.mi_detalles_parte_toolbar_descartar:
                if (parteID != 0) cargaInfo();
                else this.finish();
                break;
            case R.id.mi_detalles_parte_toolbar_guardar:
                p.setComentarioRecepcion(etCRecepcion.getText().toString());
                p.setComentarioCierre(etCCierre.getText().toString());
                try {
                    p.setSincroMovil(0);
                    p.setFechaEjecucion(Utils.FormatoATimestamp(etFechaEjec.getText().toString()).toString());
                    p.setFechaParte(Utils.FormatoATimestamp(etFecha.getText().toString()).toString());
                    p.setFechaUltimaModificacion(new Timestamp(new Date().getTime()).toString());
                    Log.d("SINC", "FUM - " + p.getFechaUltimaModificacion());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (parteID != 0) {
                    ParteCabecera.actualizar(p);
                    Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                    cargaInfo();
                }
                else {
                    AlertDialog.Builder adb = new AlertDialog.Builder(this);

                    adb.setMessage(R.string.ad_detalles_parte_guardar_msg);

                    adb.setCancelable(false);
                    adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParteCabecera.guardar(p);
                            Utils.finishStatic(DetallesParteActivity.this);
                        }
                    });
                    adb.setNegativeButton(android.R.string.cancel, null);
                    adb.show();

                }
                break;
            case R.id.mi_detalles_parte_toolbar_borrar:
                if (parteID != 0) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(this);
                    adb.setMessage(R.string.ad_detalles_parte_borrar_msg);

                    adb.setCancelable(false);
                    adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParteCabecera.borrarPartePorID(parteID);
                            Utils.finishStatic(DetallesParteActivity.this);
                        }
                    });
                    adb.setNegativeButton(android.R.string.cancel, null);
                    adb.show();

                }
                break;

        }
        return true;
    }

    private void cargaInfo() {
        p = ParteCabecera.getByID(parteID);

        // INSERIR A INFORMACIÓN
        tvEjercicio.setText(p.getEjercicioParte() + "");
        tvNumeroParte.setText(p.getNumeroParte() + "");
        tvSerie.setText(p.getSerieParte());

        etFecha.setText(Utils.FormatoFecha(p.getFechaParte()));

        String sFechaEjec = Utils.FormatoFecha(p.getFechaEjecucion());
        if (!sFechaEjec.equals("01/01/1900"))
            etFechaEjec.setText(sdf.format(Timestamp.valueOf(p.getFechaEjecucion())));
        else
            etFechaEjec.setText(sdf.format(new Date()));

        tvNumeroEmpleado.setText(p.getCodigoEmpleado() + "");
        tvNombreEmpleado.setText(p.getNombreCompleto());

        tvNumeroProyecto.setText(p.getCodigoProyecto() + "");
        if (p.getCodigoProyecto() != 0) tvNombreProyecto.setText(p.getNombreProyecto());

        switch (p.getStatusParte()) {
            case "A":
                ivEstadoParte.setImageResource(R.drawable.p_a);
                break;
            case "P":
                ivEstadoParte.setImageResource(R.drawable.p_p);
                break;
            case "C":
                ivEstadoParte.setImageResource(R.drawable.p_c);
                break;
        }

        cubrirDatos(Cliente.clientePorCodigo(p.getCodigoCliente()));

        etCRecepcion.setText(p.getComentarioRecepcion());
        etCCierre.setText(p.getComentarioCierre());

        tvImporte.setText(p.getImporte()+"");
        tvGastos.setText(p.getImporteGastos()+"");
        if (p.getImporteFacturable() >= 0) tvFacturable.setText(p.getImporteFacturable()+"");
        else tvFacturable.setText("0");
    }


    private void cargaNuevoParte() {
        // insertar usuario propio
        final int codigoEmpleado = Integer.valueOf(sp.getString("CODIGO_PLANTILLA","0"));
        final String nombreEmpleado = sp.getString("NOMBRE_COMPLETO", "Sin empleado");
        final int parteEjercicio = new GregorianCalendar().get(Calendar.YEAR);
        final int numeroParte = ParteCabecera.getLastNumeroParte()+1;
        final String serieParte = "A";

        // AlertDialog búsqueda cliente
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ad_nuevo_parte, null);
        adb.setView(dialogView);
        AlertDialog ad = adb.create();
        ad.setTitle(R.string.ad_nuevo_parte_rotulo);
        ad.setCancelable(false);

        final TextView tvNPEjercicio = (TextView) dialogView.findViewById(R.id.tvADNuevoParteEjercicio);
        TextView tvNPSerie = (TextView) dialogView.findViewById(R.id.tvADNuevoParteSerie);
        TextView tvNPNumero = (TextView) dialogView.findViewById(R.id.tvADNuevoParteNumero);
        etNPCifDni = (EditText) dialogView.findViewById(R.id.etADNuevoParteCifDniCliente);
        etNPNombre = (EditText) dialogView.findViewById(R.id.etADNuevoParteNombreCliente);
        ImageView ivNPBuscar = (ImageView) dialogView.findViewById(R.id.ivADNuevoParteBuscar);
        ImageView ivNPBuscarNombre = (ImageView) dialogView.findViewById(R.id.ivADNuevoParteBuscarNombre);

        final RadioButton rbBPNombre = (RadioButton) dialogView.findViewById(R.id.rbAdNuevoParteBuscarPNombre);
        final RadioButton rbBPCifDni = (RadioButton) dialogView.findViewById(R.id.rbAdNuevoParteBuscarPCifDni);
        RadioGroup rgBuscarParte = (RadioGroup) dialogView.findViewById(R.id.rgAdNuevoParteBuscar);

        final LinearLayout llADNBusquedaNombre = (LinearLayout) dialogView.findViewById(R.id.llADNBusquedaNombre);
        final LinearLayout llADNBusquedaCif = (LinearLayout) dialogView.findViewById(R.id.llADNBusquedaCif);
        spADBusquedaNombre = (Spinner) dialogView.findViewById(R.id.spADNuevoParteBusquedaNombre);

        llADN1 = (LinearLayout) dialogView.findViewById(R.id.llADN1);
        llADN2 = (LinearLayout) dialogView.findViewById(R.id.llADN2);
        llADN3 = (LinearLayout) dialogView.findViewById(R.id.llADN3);
        spNPNombreProyecto = (Spinner) dialogView.findViewById(R.id.spADNuevoParteNombreProyecto);

        tvNPRSCliente = (TextView) dialogView.findViewById(R.id.tvADNuevoParteRazonSocialCliente);
        tvNPDomicilioCliente = (TextView) dialogView.findViewById(R.id.tvADNuevoParteDomicilioCliente);

        tvNumeroEmpleado.setText(codigoEmpleado+"");
        tvNombreEmpleado.setText(nombreEmpleado);
        tvSerie.setText(serieParte);
        tvEjercicio.setText(parteEjercicio+"");
        tvNumeroParte.setText(numeroParte+"");
        etFecha.setText(Utils.FormatoFecha(new Date()));
        etFechaEjec.setText(Utils.FormatoFecha(new Date()));

        // Selección de tipo de búsqueda
        rgBuscarParte.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbBPNombre.isChecked()) {
                    llADNBusquedaNombre.setVisibility(View.VISIBLE);
                    llADNBusquedaCif.setVisibility(View.GONE);
                }
                if (rbBPCifDni.isChecked()) {
                    llADNBusquedaNombre.setVisibility(View.GONE);
                    llADNBusquedaCif.setVisibility(View.VISIBLE);
                }
            }
        });

        ivNPBuscarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDatos();
                busquedaClientes.clear();
                // Carga do array da búsqueda no sp
                String pclave = etNPNombre.getText().toString();
                busquedaClientes.add(new Pair<String, String>("0","Selecciona un cliente"));
                busquedaClientes.addAll(ParteCabecera.buscarPorNombre(pclave));

                if (busquedaClientes.size() > 1) {
                    fillBusquedaClientes();
                } else {
                    Toast.makeText(DetallesParteActivity.this, "Cliente no encontrado.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        ivNPBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Cliente.clientePorCifDni(etNPCifDni.getText().toString());
                proyectos.clear();
                if (c != null) cubrirDatos(c);
                else
                    Toast.makeText(DetallesParteActivity.this, "Cliente no encontrado.", Toast.LENGTH_SHORT).show();
            }
        });

        tvNPEjercicio.setText(parteEjercicio+"");
        tvNPSerie.setText(serieParte);
        tvNPNumero.setText(numeroParte+"");


        ad.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getText(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (spNPNombreProyecto.isEnabled()) {
                    String nproyecto = spNPNombreProyecto.getSelectedItem().toString();
                    tvNombreProyecto.setText(nproyecto);
                    tvNumeroProyecto.setText(proyectos.get(spNPNombreProyecto.getSelectedItemPosition()).getCodigoProyecto());
                }
                else {
                    tvNombreProyecto.setText("Sin proyecto asociado");
                    tvNumeroProyecto.setText("0");
                }
                try {
                    p = new ParteCabecera(codigoEmpresa,parteEjercicio,serieParte,numeroParte,"P",
                            "","","","",
                            Utils.FormatoATimestamp(etFecha.getText().toString()).toString(),
                            Utils.FormatoATimestamp(etFechaEjec.getText().toString()).toString(),
                            codigoEmpleado,nombreEmpleado,Integer.valueOf(tvNumeroProyecto.getText().toString()),
                            tvNombreProyecto.getText().toString(), etCCierre.getText().toString(),etCRecepcion.getText().toString(),
                            c.getCodigoCliente(),c.getNombre(), 0.0,0.0,0.0,
                            sp.getInt("CODIGO_USUARIO",0), new Timestamp(new Date().getTime()).toString(),1,
                            "",0,0,sp.getString("NOMBRE_CORTO",""),
                            sp.getInt("CODIGO_USUARIO",0),0);      // sincroMovil = 0 porque é unha inserción aínda sin subir
                    // todo: totalUnidades, importes

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        ad.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getText(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DetallesParteActivity.this.finish();
            }
        });


        ad.show();

    }

    private void fillBusquedaClientes() {
        if (busquedaClientes.size() == 2) {
            c = Cliente.clientePorCodigo(busquedaClientes.get(1).first);
            cubrirDatos(c);
        } else {
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < busquedaClientes.size(); i++) {
                strings.add(busquedaClientes.get(i).second);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetallesParteActivity.this,
                    android.R.layout.simple_spinner_item, strings);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spADBusquedaNombre.setAdapter(adapter);
            spADBusquedaNombre.setOnItemSelectedListener(DetallesParteActivity.this);

            spADBusquedaNombre.setVisibility(View.VISIBLE);

        }
    }

    private void cubrirDatos(Cliente c) {
        if (llADN1 != null) {   // si está null, a búsqueda non provén de un novo parte

            llADN1.setVisibility(View.VISIBLE);
            llADN2.setVisibility(View.VISIBLE);
            llADN3.setVisibility(View.VISIBLE);

            tvNPRSCliente.setText(c.getRazonSocial());
            tvNPDomicilioCliente.setText(c.getDomicilio());

            // PROYECTOS
            apiService.getProyectos("Bearer " + sp.getString("TOKEN",""), codigoEmpresa, c.getCodigoCliente())
                    .enqueue(new Callback<List<Proyecto>>() {
                        @Override
                        public void onResponse(Call<List<Proyecto>> call, retrofit2.Response<List<Proyecto>> response) {
                            Log.d("retrofit_proy","resp: " + response.code()+"" + ": " + response.message());
                            proyectos = response.body();

                            String[] strings = new String[proyectos.size()];
                            for (int i = 0; i < strings.length; i++) {
                                strings[i] = proyectos.get(i).getNombreProyecto();
                            }

                            if (strings.length>0) {
                                spNPNombreProyecto.setEnabled(true);
                                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(DetallesParteActivity.this,
                                        android.R.layout.simple_spinner_item, strings);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spNPNombreProyecto.setAdapter(adapter);
                            } else {
                                spNPNombreProyecto.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Proyecto>> call, Throwable t) {
                            Log.d("retrofit_proy",t.getMessage());
                        }
                    });


        }

        tvRSCliente.setText(c.getRazonSocial());
        tvNumeroCliente.setText(c.getCodigoCliente()+"");
        tvCifDniCliente.setText(c.getCifDni());
        tvDomicilioCliente.setText(c.getDomicilio());
    }

    private void resetDatos() {
        llADN1.setVisibility(View.GONE);
        llADN2.setVisibility(View.GONE);
        llADN3.setVisibility(View.GONE);
        spADBusquedaNombre.setVisibility(View.GONE);

        tvNPRSCliente.setText("");
        tvNPDomicilioCliente.setText("");
        tvRSCliente.setText("");
        tvNumeroCliente.setText("");
        tvCifDniCliente.setText("");
        tvDomicilioCliente.setText("");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivDetallesParteEstado) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            dialogBuilder.setItems(new String[]{"Pendiente","Abierto", "Cerrado"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            p.setStatusParte("P");
                            ivEstadoParte.setImageResource(R.drawable.p_p);
                            Toast.makeText(DetallesParteActivity.this, "Cambiado a pendiente", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            ivEstadoParte.setImageResource(R.drawable.p_a);
                            p.setStatusParte("A");
                            Toast.makeText(DetallesParteActivity.this, "Cambiado a abierto", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            ivEstadoParte.setImageResource(R.drawable.p_c);
                            p.setStatusParte("C");

                            // Si se cerra o parte, cambiar o nombre e código do empregado que o cerra, e a hora e data
                            p.setFechaCierre(new Timestamp(new Date().getTime()).toString());
                            p.setHoraCierre(new Date().getTime());
                            p.setCodigoUsuarioCierre(sp.getInt("CODIGO_USUARIO",0));
                            p.setNombreUsuarioCierre(sp.getString("NOMBRE_COMPLETO",""));

                            Toast.makeText(DetallesParteActivity.this, "Cambiado a cerrado", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            dialogBuilder.show();
        }

        if (v.getId() == R.id.etDetallesParteFecha) {
            showDatePickerDialog(etFecha);
        }

        if (v.getId() == R.id.etDetallesParteFechaEjec) {
            showDatePickerDialog(etFechaEjec);
        }

    }

    private void showDatePickerDialog(final EditText et) {
        DatePickerFragment dpf = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                final String selectedDate = Utils.twoDigits(day) + "/" + Utils.twoDigits((month+1)) + "/" + year;
                et.setText(selectedDate);
            }
        });
        dpf.show(this.getSupportFragmentManager(),"datePicker");
        try {
            p.setFechaParte(Utils.FormatoATimestamp(etFecha.getText().toString()).toString());
            p.setFechaEjecucion(Utils.FormatoATimestamp(etFechaEjec.getText().toString()).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spADNuevoParteBusquedaNombre) {
            proyectos.clear();
            if (position != 0) {
                c = Cliente.clientePorCodigo(busquedaClientes.get(position).first);
                cubrirDatos(c);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

