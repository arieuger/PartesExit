package es.dosxmil.partesexit.servicioweb;

import java.util.List;
import java.util.Map;

import es.dosxmil.partesexit.mapeo.Articulo;
import es.dosxmil.partesexit.mapeo.Cliente;
import es.dosxmil.partesexit.mapeo.ParteCabecera;
import es.dosxmil.partesexit.mapeo.ParteLinea;
import es.dosxmil.partesexit.mapeo.Proyecto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiService {
    String URL = "http://10.0.2.2:8000/api/";

    // PARTECABECERA
    @GET("ParteCabecera/{codigoEmpresa}/{fumLocal}")
    Call<List<ParteCabecera>> getPartesCabecera(
            @Header("Authorization") String token,
            @Path(value = "codigoEmpresa", encoded = true) int codigoEmpresa,
            @Path(value = "fumLocal", encoded = true) String fumLocal
    );

    @GET("ParteCabecera/FechaUltimaModificacion/{codigoEmpresa}")
    Call<StringResponse> getFumSrv(
            @Header("Authorization") String token,
            @Path(value = "codigoEmpresa", encoded = true) int codigoEmpresa
    );

    @GET("ParteCabecera/update")
    Call<StringResponse> updatePartesSrv(
            @Header("Authorization") String token,
            @QueryMap(encoded = true) Map<String, String> params
    );

    // CLIENTE
    @GET("Cliente/{codigoEmpresa}")
    Call<List<Cliente>> getClientes(
            @Header("Authorization") String token,
            @Path(value = "codigoEmpresa", encoded = true) int codigoEmpresa
    );

    @GET("Cliente/Proyectos/{codigoEmpresa}/{codigoCliente}")
    Call<List<Proyecto>> getProyectos(
            @Header("Authorization") String token,
            @Path(value = "codigoEmpresa", encoded = true) int codigoEmpresa,
            @Path(value = "codigoCliente", encoded = true) String codigoCliente
    );

    // ARTICULO
    @GET("Articulo/{codigoEmpresa}")
    Call<List<Articulo>> getArticulos(
            @Header("Authorization") String token,
            @Path(value = "codigoEmpresa", encoded = true) int codigoEmpresa
    );

    // PARTELINEAS
    @GET("ParteLineas/{codigoEmpresa}/{ejercicioParte}/{serieParte}/{numeroParte}")
    Call<List<ParteLinea>> getParteLineas(
            @Header("Authorization") String token,
            @Path(value = "codigoEmpresa", encoded = true) int codigoEmpresa,
            @Path(value = "ejercicioParte", encoded = true) int ejercicioParte,
            @Path(value = "serieParte", encoded = true) String serieParte,
            @Path(value = "numeroParte", encoded = true) int numeroParte
    );

}
