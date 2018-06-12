package es.dosxmil.partesexit;

import java.util.List;

import es.dosxmil.partesexit.mapeo.Articulo;
import es.dosxmil.partesexit.mapeo.Cliente;
import es.dosxmil.partesexit.mapeo.ParteCabecera;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {
    public final static String URL = "http://10.0.2.2:8000/api/";

    @GET("ParteCabecera/{codigoEmpresa}")
    Call<List<ParteCabecera>> getPartesCabecera(
            @Header("Authorization") String token,
            @Path(value = "codigoEmpresa", encoded = true)int codigoEmpresa
    );

     @GET("Cliente/{codigoEmpresa}")
     Call<List<Cliente>> getClientes(
             @Header("Authorization") String token,
             @Path(value = "codigoEmpresa", encoded = true) int codigoEmpresa
     );

     @GET("Articulo/{codigoEmpresa}")
     Call<List<Articulo>> getArticulos(
             @Header("Authorization") String token,
             @Path(value = "codigoEmpresa", encoded = true) int codigoEmpresa
     );

}
