package es.dosxmil.partesexit.mapeo;

import com.google.gson.annotations.SerializedName;

public class Proyecto {
    @SerializedName("CodigoProyecto")
    private String CodigoProyecto;

    @SerializedName("NombreProyecto")
    private String NombreProyecto;

    public Proyecto(String CodigoProyecto, String CodigoCliente) {
        this.CodigoProyecto = CodigoProyecto;
        this.NombreProyecto = NombreProyecto;
    }

    public String getNombreProyecto() {
        return NombreProyecto;
    }

    public String getCodigoProyecto() {
        return CodigoProyecto;
    }
}
