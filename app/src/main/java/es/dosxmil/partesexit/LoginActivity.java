package es.dosxmil.partesexit;

// TODO: Implementar encriptación
// TODO: Se non recorda o usuario borrar sharedpreferences ao saír

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dosxmil.partesexit.adapters.ArrayAdapterEmpresas;
import es.dosxmil.partesexit.mapeo.Empresa;

public class LoginActivity extends Activity {

    private final String URLDLOGIN = "http://10.0.2.2:8000/api/user/login";
    private EditText etMail;
    private EditText etPwd;
    private SharedPreferences sp;

    private Spinner adLoginSpEmpresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etMail = (EditText) findViewById(R.id.etLoginMail);
        etPwd = (EditText) findViewById(R.id.etLoginPwd);
        sp = this.getSharedPreferences("sp", MODE_PRIVATE);
    }

    public void loginOnClick(View view) {
        String email = etMail.getText().toString();
        String password = etPwd.getText().toString();

        descargaData(URLDLOGIN, new Pair<>("email", email), new Pair<>("password", password));

    }

    private void seleccionEmpresa() {

        // ShowDialog selecciona empresa
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ad_seleccion_empresa, null);
        dialogBuilder.setView(dialogView);

        adLoginSpEmpresas = dialogView.findViewById(R.id.adLoginSpEmpresas);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle(R.string.ad_seleccion_empresa_rotulo);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tvcodigo = (TextView) alertDialog.findViewById(R.id.tvItemEmpresaCodigo);
                TextView tvnombree = (TextView) alertDialog.findViewById(R.id.tvItemEmpresaNombreEmpleado);
                int codigoEmpresa = Integer.parseInt(tvcodigo.getText().toString());
                String nombreEmpleado = tvnombree.getText().toString();
                sp.edit().putInt("EMPRESA_DFLT", codigoEmpresa)
                        .putString("NOMBRE_COMPLETO", nombreEmpleado)
                        .apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        alertDialog.show();
    }

    private void descargaData(String url, final Pair<String, String>... params) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONArray(response).getJSONObject(0);

                    //if (jsonObject.getBoolean("login")) {
                    if (!jsonObject.has("error")) {
                        //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        sp.edit().putString("TOKEN", jsonObject.getString("token"))
                                .putString("CODIGO_PLANTILLA", jsonObject.getString("codigoPlantilla"))
                                .putInt("CODIGO_USUARIO", jsonObject.getInt("codigoUsuario"))
                                .putString("NOMBRE_CORTO", jsonObject.getString("nombreCorto"))
                                .apply();


                        ArrayList<Empresa> listaEmpresas = new ArrayList<Empresa>();
                        JSONArray jempresas = jsonObject.getJSONArray("empresas");
                        for (int i = 0; i < jempresas.length(); i++) {
                            Empresa e = new Empresa(jempresas.getJSONObject(i).getInt("CodigoEmpresa"),
                                    jempresas.getJSONObject(i).getString("Empresa"),
                                    jempresas.getJSONObject(i).getString("NombreCompleto"));

                            listaEmpresas.add(e);
                        }

                        ArrayAdapterEmpresas aae = new ArrayAdapterEmpresas(LoginActivity.this, listaEmpresas);
                        seleccionEmpresa();
                        adLoginSpEmpresas.setAdapter(aae);


                    } else
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "VolleyError", Toast.LENGTH_SHORT).show();
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

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

}