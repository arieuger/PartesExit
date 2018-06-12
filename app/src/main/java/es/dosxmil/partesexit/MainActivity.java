package es.dosxmil.partesexit;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dosxmil.partesexit.adapters.CursorAdapterParteCabecera;
import es.dosxmil.partesexit.mapeo.Articulo;
import es.dosxmil.partesexit.mapeo.Cliente;
import es.dosxmil.partesexit.mapeo.ParteCabecera;
import es.dosxmil.partesexit.mapeo.ParteLinea;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final int DPARTES = 10;
    private static final int DCLIENTES = 20;
    private static final int DARTICULOS = 30;
    private static final int DFUSSRV = 40;
    private static final int DSINCRO = 50;
    private static final int DBAJARP = 60;

    private final String url = "http://10.0.2.2:8000/api/";
    private final String urlClientes = "http://10.0.2.2/proba_2xmil/clientes_por_empresa.php";
    private final String urlArticulos = "http://10.0.2.2/proba_2xmil/articulos_por_empresa.php";
    private final String urlFUSSrv = "http://10.0.2.2/proba_2xmil/fecha_ultimo_parte_srv.php";
    private final String urlSubirP = "http://10.0.2.2/proba_2xmil/actualizacion_partes.php";
    private String urlSubirPL = "http://10.0.2.2/proba_2xmil/actualizacion_lineas.php";

    // TODO: Mellorar búsqueda

    private BottomNavigationView bottomNavigationView;

    private ArrayList<Long> seleccionados = new ArrayList<>();

    ApiService apiService;
    Retrofit cliente;
    List<ParteCabecera> listaPartes;

    private static SQLiteDatabase db;
    private static SharedPreferences sp;
    private ListView lvPartesCabecera;
    private int codigoEmpresa;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private ProgressBar pbMain;

    private String sUltimaModSrv;
    private ArrayList<String[]> pkPartesLocal = new ArrayList<>();


    public static SQLiteDatabase getDb() {
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainBottomNavigationWeb);
        pbMain = (ProgressBar) findViewById(R.id.pbMain);
        lvPartesCabecera = (ListView)findViewById(R.id.lvMainActivityPartesCabecera);

        lvPartesCabecera.setOnItemClickListener(this);
        lvPartesCabecera.setOnItemLongClickListener(this);


        // configuración de gson
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().equals("id") || f.getName().equals("plDescargado") || f.getName().equals("sincroMovil");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();

        cliente = new Retrofit.Builder().baseUrl(ApiService.URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        apiService = cliente.create(ApiService.class);


        // Na primeira entrada, creación da base de datos
        if (db == null) db = new ExitDBOpenHelper(this).getWritableDatabase();
        // Chamada ás sharedPreferences para coller o código da empresa
        sp = this.getSharedPreferences("sp", MODE_PRIVATE);

        // Descarga a local das cabeceiras de partes
        codigoEmpresa = sp.getInt("EMPRESA_DFLT", -1);

        Cursor c = ParteCabecera.getAllCursor();

        // DESCARGAS INICIALES
        if (c.getCount() == 0) {
            pbMain.setVisibility(View.VISIBLE);
            descargaPC();
        }
        if (Cliente.getAllCursor().getCount() == 0) descargaC();
        if (Articulo.getAllCursor().getCount() == 0) {
            descargaA();
            pbMain.setVisibility(View.GONE);
            fillListView(this,lvPartesCabecera,ParteCabecera.getAllCursor());
        }

        else fillListView(this, lvPartesCabecera, c);

        // Implementa barar inferior de navegación
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()) {
                    case R.id.navigation_parte_a:
                        fillListView(MainActivity.this,lvPartesCabecera,ParteCabecera.getStatusCursor("A"));
                        break;
                    case R.id.navigation_parte_c:
                        fillListView(MainActivity.this, lvPartesCabecera, ParteCabecera.getStatusCursor("C"));
                        break;
                    case R.id.navigation_parte_p:
                        fillListView(MainActivity.this, lvPartesCabecera, ParteCabecera.getStatusCursor("P"));
                        break;
                    case R.id.navigation_parte_ultimos:
                        fillListView(MainActivity.this, lvPartesCabecera, ParteCabecera.getAllCursor());
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c = seleccionaCursor();

        fillListView(this,lvPartesCabecera,c);
    }

    private Cursor seleccionaCursor() {
        Cursor c = null;
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_parte_ultimos)
            c = ParteCabecera.getAllCursor();
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_parte_a)
            c = ParteCabecera.getStatusCursor("A");
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_parte_c)
            c = ParteCabecera.getStatusCursor("C");
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_parte_p)
            c = ParteCabecera.getStatusCursor("P");

        return c;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar,menu);

        // Implementación de búsqueda
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.mi_main_toolbar_buscar);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        // Submenú de opción usuario
        // TODO

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_main_toolbar_borrar:

                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                final String plural = seleccionados.size() == 1 ? " ":"s ";
                String s = getResources().getString(R.string.ad_parte_cabeceras_confirmar_borrado, seleccionados.size(), plural);
                adb.setMessage(s);
                adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Borrados " + ParteCabecera.borrarPartePorID(seleccionados) + " elementos.", Toast.LENGTH_SHORT).show();
                        seleccionados.clear();
                        fillListView(MainActivity.this,lvPartesCabecera,seleccionaCursor());
                    }
                });
                adb.setNegativeButton(android.R.string.cancel, null);
                adb.show();
                break;
            case R.id.mi_main_toolbar_anadir:
                startActivity(new Intent(this, DetallesParteActivity.class));
                break;
            case R.id.mi_main_toolbar_usr:
                break;
            case R.id.mi_main_toolbar_sinc:
                descargaDatos(this,urlFUSSrv,DFUSSRV,new Pair<String, String>("codigoEmpresa",codigoEmpresa+""));
                break;
        }

        return true;
    }

    private ParteCabecera pc;
    private void sincroniza() {
        // Todo: aviso na barra de notificacións cando a sincronización esté rematada

        // De local a servidor: subida novos partes
        ArrayList<ParteCabecera> pcs = ParteCabecera.partesSinSubir(sUltimaModSrv);
        Log.d("SINC", "UMS: " + sUltimaModSrv);
        for (ParteCabecera pc : pcs) {
            this.pc = pc;
            Log.d("SINCCOMP","Parte: " + pc.getNumeroParte() + " - " + pc.getNombre() +": " + pc.getFechaUltimaModificacion());
            // no momento que se sincroniza, hai que cambiar o valor de sincroMovil a 1, en local e no srv
            Pair<String, String>[] valores = pc.getArrayValores();
            pc.setSincroMovil(1);
            ParteCabecera.actualizar(pc);
            descargaDatos(this, urlSubirP, DSINCRO, valores);

            // TODO: Trigger en BD do servidor. Cando se modifica unha fila, a columna SincroMovil tén que volver a 0

        }


        // De servidor a local: descarga novos partes
        descargaDatos(this,url,DBAJARP, new Pair<String, String>("codigoEmpresa",codigoEmpresa+""));
               // new Pair<String, String>"maxFUM",);

        // TODO: Sincronizar borrados


        // Descarga artigos
        descargaDatos(this, urlArticulos, DARTICULOS, new Pair<String, String>("codigoEmpresa",codigoEmpresa+""));


        // Descarga clientes
        descargaDatos(MainActivity.this, urlClientes, DCLIENTES,
                new Pair<String, String>("codigoEmpresa",codigoEmpresa+""));

        // Borrar clientes

    }

    private void sincronizaLineas(ParteCabecera pc) {
        Cursor c = ParteLinea.getAllCursor(pc.getCodigoEmpresa(),pc.getEjercicioParte(),pc.getSerieParte(),pc.getNumeroParte());
        while (c.moveToNext()) {    // Inda que non foran modificadas todas as liñas, en principio subímolas todas
            ParteLinea pl = ParteLinea.getLineaPorID(c.getLong(0));
            Log.d("SINCRO", "pl - " + pl.getNumeroParte());
            Pair<String, String>[] valorespl = pl.getArrayValores();
            descargaDatos(this, urlSubirPL, 0, valorespl);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetallesParteActivity.class);
        intent.putExtra("idParte", id);
        startActivity(intent);
    }

    private void fillListView(Context context, ListView lv, Cursor c) {
        CursorAdapterParteCabecera cap = new CursorAdapterParteCabecera(context,c, 0);
        lv.setAdapter(cap);
        seleccionados.clear();
    }


    public void descargaDatos(final Context context, String url, final int tipo, final Pair<String, String>... params) {

        // TODO: Barra de carga
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = null;
                    if (tipo != DFUSSRV && tipo != DSINCRO && tipo != 0) jsonArray = new JSONArray(response);

                    if (tipo == DPARTES || tipo == DBAJARP) {
                        Toast.makeText(context, "Descargando partes...", Toast.LENGTH_SHORT).show();
                        Log.d("DESCARGA PARTES", "Iniciada");
                        // Descarga partes
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.d("descarga", " - " + i);

                            JSONObject o = jsonArray.getJSONObject(i);

                            int codigoEmpresa = o.getInt("CodigoEmpresa");
                            int ejercicioParte = o.getInt("EjercicioParte");
                            String serieParte = o.getString("SerieParte");
                            int numeroParte = o.getInt("NumeroParte");
                            String statusParte = o.getString("StatusParte");
                            String tipoParte = o.getString("TipoParte");
                            String descripcionTipoParte = o.getString("DescripcionTipoParte");
                            String codigoArticulo = o.getString("CodigoArticulo");
                            String descripcionArticulo = o.getString("DescripcionArticulo");
                            String fechaParte = o.getJSONObject("FechaParte").getString("date");
                            String fechaEjecucion = o.getJSONObject("FechaEjecucion").getString("date");
                            int codigoEmpleado = o.getInt("CodigoEmpleado");
                            String nombreCompleto = o.getString("NombreCompleto");
                            int codigoProyecto = o.getInt("CodigoProyecto");
                            String nombreProyecto = o.getString("NombreProyecto");
                            String comentarioCierre = o.getString("ComentarioCierre");
                            String comentarioRecepcion = o.getString("ComentarioRecepcion");
                            String codigoCliente = o.getString("CodigoCliente");
                            String nombre = o.getString("Nombre");
                            double importe = o.getDouble("Importe");
                            double importeGastos = o.getDouble("ImporteGastos");
                            double importeFacturable = o.getDouble("ImporteFacturable");
                            int codigoUsuario = o.getInt("CodigoUsuario");
                            String fechaUltimaModificacion = o.getJSONObject("FechaUltimaModificacion").getString("date");
                            int plDescargado = 0;
                            String fechaCierre = o.getJSONObject("FechaCierre").getString("date");
                            double horaCierre = o.getDouble("HoraCierre");
                            double totalUnidades = o.getDouble("TotalUnidades");
                            String nombreUsuarioCierre = o.getString("NombreUsuarioCierre");
                            int codigoUsuarioCierre = o.getInt("CodigoUsuarioCierre");

                            ParteCabecera pc = new ParteCabecera(codigoEmpresa, ejercicioParte, serieParte, numeroParte, statusParte, tipoParte, descripcionTipoParte,
                                    codigoArticulo, descripcionArticulo, fechaParte, fechaEjecucion, codigoEmpleado, nombreCompleto, codigoProyecto, nombreProyecto, comentarioCierre,
                                    comentarioRecepcion, codigoCliente, nombre, importe, importeGastos, importeFacturable, codigoUsuario, fechaUltimaModificacion, plDescargado,
                                    fechaCierre, horaCierre, totalUnidades, nombreUsuarioCierre, codigoUsuarioCierre, 1);
                            // sincroMovil = 1 porque é unha descarga do servidor - os partes estarán sincronizados

                            ParteCabecera.guardar(pc);

                            if (tipo == DBAJARP) fillListView(MainActivity.this,lvPartesCabecera,ParteCabecera.getAllCursor());
                        }
                        Log.d("DESCARGA PARTES", "Finalizada");
                    }

                    // Descarga clientes
                    // A búsqueda inicial de clientes no servidor está limitada a aquelas empresas
                    // que non están marcadas como clientes potenciais.
                    if (tipo == DCLIENTES) { // Descarga de clientes
                        Log.d("DESCARGA CLIENTES","Iniciada");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            Cliente c = new Cliente(json.getInt("CodigoEmpresa"),
                                    json.getString("CodigoCliente"),
                                    json.getString("SiglaNacion"),
                                    json.getString("CifDni"),
                                    json.getString("CifEuropeo"),
                                    json.getString("RazonSocial"),
                                    json.getString("Nombre"),
                                    json.getString("Domicilio"),
                                    json.getInt("CodigoCondiciones"));

                            Cliente.guardar(c);
                        }

                        Log.d("DESCARGA CLIENTES", "Finalizada");
                        Cursor c = ParteCabecera.getAllCursor();
                        Log.d("DESCARGA CARGALV", "Iniciada");
                        fillListView(context, lvPartesCabecera, c);
                        Log.d("DESCARGA CARGALV", "Finalizada");
                        pbMain.setVisibility(View.GONE);
                    }

                    if (tipo == DARTICULOS) {
                        Log.d("DESCARGA ARTÍCULOS", "Iniciada");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            Articulo a = new Articulo(
                                    o.getString("CodigoArticulo"),
                                    o.getString("DescripcionArticulo"),
                                    o.getDouble("PrecioNetoVenta")
                            );

                            Articulo.guardar(a);
                        }

                        Log.d("DESCARGA ARTÍCULOS", "Finalizada");
                    }


                    // SINCRONIZACIÓN
                    if (tipo == DFUSSRV) {
                        sUltimaModSrv = response.trim();
                        sincroniza();

                    }

                    if (tipo == DSINCRO) {
                        Log.d("SINC", response);
                        sincronizaLineas(MainActivity.this.pc);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "VolleyError", Toast.LENGTH_SHORT).show();
                pbMain.setVisibility(View.GONE);
                error.printStackTrace();

            }
        })

        {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                for (int i = 0; i < params.length; i++) {
                    param.put(params[i].first, params[i].second);
                }
                return param;
            }
        };

        int socketTimeout = 100000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Cursor c = ParteCabecera.partesPorKeyword(query);
        if (c == null) {
            Toast.makeText(this, "No hay resultados", Toast.LENGTH_SHORT).show();
        } else {
            fillListView(this,lvPartesCabecera,c);
        }
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!seleccionados.contains(id)) {
            seleccionados.add(id);
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            String s = "";
            for (long l:seleccionados) {
                s += l+" ";
            }
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        } else {
            seleccionados.remove(id);
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        return true;
    }

    private void descargaPC() {
        apiService.getPartesCabecera("Bearer " + sp.getString("TOKEN",""),codigoEmpresa).enqueue(new Callback<List<ParteCabecera>>() {
            @Override
            public void onResponse(Call<List<ParteCabecera>> call, retrofit2.Response<List<ParteCabecera>> response) {
                Log.d("retrofit p",response.code()+"" + ": " + response.message());
                if (response.isSuccessful()) {
                    listaPartes = response.body();
                    Log.d("retrofit p",listaPartes.size()+"");
                    for (ParteCabecera pc : listaPartes)
                        ParteCabecera.guardar(pc);
                }
            }

            @Override
            public void onFailure(Call<List<ParteCabecera>> call, Throwable t) {
                Log.d("retrofit p",t.getMessage());
            }
        });
    }

    private void descargaC() {
        apiService.getClientes("Bearer " + sp.getString("TOKEN",""),codigoEmpresa).enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, retrofit2.Response<List<Cliente>> response) {
                List<Cliente> listaClientes = response.body();
                Log.d("retrofit c", listaClientes.size()+"");
                for (Cliente c : listaClientes) {
                    Log.d("retrofit c", c.getCifDni());
                    Cliente.guardar(c);
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Log.d("retrofit c",t.getMessage());
            }
        });
    }

    private void descargaA() {
        apiService.getArticulos("Bearer " + sp.getString("TOKEN",""), codigoEmpresa).enqueue(new Callback<List<Articulo>>() {
            @Override
            public void onResponse(Call<List<Articulo>> call, retrofit2.Response<List<Articulo>> response) {
                List<Articulo> listaArticulos = response.body();
                Log.d("retrofit a", listaArticulos.size()+"");
                for (Articulo a : listaArticulos) {
                    Log.d("retrofit a", a.getCodigoArticulo());
                    Articulo.guardar(a);
                }
            }

            @Override
            public void onFailure(Call<List<Articulo>> call, Throwable t) {
                Log.d("retrofit a", t.getMessage());
            }
        });
    }
}
