package es.dosxmil.partesexit.mapeo;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import es.dosxmil.partesexit.MainActivity;

public class Articulo {
    private String codigoArticulo;
    private String descripcionArticulo;
    private double precioNetoVenta;
    private static String [] columnas = new String[]{"_id","CodigoArticulo", "DescripcionArticulo", "PrecioNetoVenta"};

    public Articulo(String codigoArticulo, String descripcionArticulo, double precioNetoVenta) {
        this.codigoArticulo = codigoArticulo;
        this.descripcionArticulo = descripcionArticulo;
        this.precioNetoVenta = precioNetoVenta;
    }

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.descripcionArticulo = descripcionArticulo;
    }

    public double getPrecioNetoVenta() {
        return precioNetoVenta;
    }

    public void setPrecioNetoVenta(double precioNetoVenta) {
        this.precioNetoVenta = precioNetoVenta;
    }


    public static Cursor getAllCursor() {
        return MainActivity.getDb().query("Articulos",columnas,null,null,null,null,null);
    }

    public static void guardar(Articulo a) {
        ContentValues cv = new ContentValues();
        cv.put("CodigoArticulo", a.codigoArticulo);
        cv.put("DescripcionArticulo", a.descripcionArticulo);
        cv.put("PrecioNetoVenta", a.precioNetoVenta);

        MainActivity.getDb().insert("Articulos",null,cv);
    }

    public static Articulo buscarPorCodigo(String codigo) {
        Articulo a = null;
        Cursor c = MainActivity.getDb().query("Articulos",columnas, "CodigoArticulo = ?",new String[]{codigo},null,null,null);
        if (c.moveToFirst())
            a = new Articulo(c.getString(1),c.getString(2),c.getDouble(3));

        return a;
    }

    public static ArrayList<Articulo> buscarPorDesc(String desc) {
        ArrayList<Articulo> articulos = new ArrayList<>();
        Cursor c = MainActivity.getDb().rawQuery("SELECT * FROM Articulos WHERE DescripcionArticulo LIKE '%" + desc + "%'",null);
        while (c.moveToNext()) {
            articulos.add(new Articulo(c.getString(1),c.getString(2),c.getDouble(3)));
        }
        return articulos;
    }
}
