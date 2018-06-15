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

import java.util.ArrayList;
import java.util.List;

import es.dosxmil.partesexit.adapters.CursorAdapterParteCabecera;
import es.dosxmil.partesexit.mapeo.Articulo;
import es.dosxmil.partesexit.mapeo.Cliente;
import es.dosxmil.partesexit.mapeo.ParteCabecera;
import es.dosxmil.partesexit.mapeo.ParteLinea;

import es.dosxmil.partesexit.servicioweb.ApiService;
import es.dosxmil.partesexit.servicioweb.RetrofitClient;
import es.dosxmil.partesexit.servicioweb.StringResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

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

    private String fumSrv;

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

        cliente = RetrofitClient.getClient();
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

        // Implementa barra inferior de navegación
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
                // TODO
                break;
            case R.id.mi_main_toolbar_sinc:
                sincroniza();
                break;
        }

        return true;
    }

    private void sincroniza() {
        // Todo: aviso na barra de notificacións cando a sincronización esté rematada

        // De local a servidor: subida novos partes
        apiService.getFumSrv("Bearer " + sp.getString("TOKEN",""),codigoEmpresa).enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, retrofit2.Response<StringResponse> response) {
                Log.d("retrofit fum",response.code()+"" + ": " + response.message());
                fumSrv = response.body().getString();
                Log.d("retrofit fum", fumSrv);
                // os pc que teñan unha data superior a esta, súbense á rede, e as súas liñas tamén
                List<ParteCabecera> partesSinSubir = ParteCabecera.partesSinSubir(fumSrv);
                for (ParteCabecera pc : partesSinSubir) {

                    // TODO: Actualizar/insertar liñas
                    subirLineas(pc);

                    // Actualización / inserción de partes
                    apiService.updatePartesSrv("Bearer " + sp.getString("TOKEN",""),
                            pc.getMapValores()).enqueue(new Callback<StringResponse>() {
                            @Override
                            public void onResponse(Call<StringResponse> call, retrofit2.Response<StringResponse> resp) {
                                Log.d("retrofit_sincro","resp: " + resp.code()+"" + ": " + resp.message());
                                Log.d("retrofit_sincro",call.request().url().toString());
                                Log.d("retrofit_sincro",resp.body().getString());
                            }

                            @Override
                            public void onFailure(Call<StringResponse> call, Throwable t) {
                                Log.d("retrofit_sincro",t.getMessage());
                            }
                    });
                }


            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {
                Log.d("retrofit fum",t.getMessage());
            }
        });

        // De servidor a local: descarga novos partes
        descargaPC();
        // TODO: Liñas

        // Descarga artigos

        // Descarga clientes



        fillListView(this,lvPartesCabecera,ParteCabecera.getAllCursor());
    }

    private void subirLineas(ParteCabecera pc) {
        Cursor c = ParteLinea.getAllCursor(pc.getCodigoEmpresa(),pc.getEjercicioParte(),pc.getSerieParte(),pc.getNumeroParte());
        while (c.moveToNext()) {    // Inda que non foran modificadas todas as liñas, en principio subímolas todas
            ParteLinea pl = ParteLinea.getLineaPorID(c.getLong(0));
            Log.d("SINCRO", "pl - " + pl.getNumeroParte());
            Pair<String, String>[] valorespl = pl.getArrayValores();
            // TODO: descargaDatos(this, urlSubirPL, 0, valorespl);
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
        // Selección de partes para o seu borrado
        if (!seleccionados.contains(id)) {
            seleccionados.add(id);
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            seleccionados.remove(id);
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        return true;
    }

    private void descargaPC() {
        apiService.getPartesCabecera("Bearer " + sp.getString("TOKEN",""),codigoEmpresa, ParteCabecera.getMaxFUM())
                .enqueue(new Callback<List<ParteCabecera>>() {
            @Override
            public void onResponse(Call<List<ParteCabecera>> call, retrofit2.Response<List<ParteCabecera>> response) {
                Log.d("retrofit p",response.code()+"" + ": " + response.message());
                if (response.isSuccessful()) {
                    listaPartes = response.body();
                    Log.d("retrofit p",listaPartes.size()+"");
                    for (ParteCabecera pc : listaPartes)
                        if (ParteCabecera.getByID(pc.getId()) == null) {
                            ParteCabecera.guardar(pc);
                            Toast.makeText(MainActivity.this, "Parte guardado", Toast.LENGTH_SHORT).show();
                        } else {
                            ParteCabecera.actualizar(pc);
                            Toast.makeText(MainActivity.this, "Parte actualizado", Toast.LENGTH_SHORT).show();
                        }
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
                Log.d("retrofit c", response.code()+"" + ": " + response.message());
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
