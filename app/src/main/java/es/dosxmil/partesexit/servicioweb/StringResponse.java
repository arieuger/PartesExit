package es.dosxmil.partesexit.servicioweb;

import com.google.gson.annotations.SerializedName;

/**
 * Para mapear respostas dende o servidor que son sólo unha cadea de texto
 */

public class StringResponse {
    @SerializedName("string")
    private String string;

    public String getString() {
        return string;
    }
}
