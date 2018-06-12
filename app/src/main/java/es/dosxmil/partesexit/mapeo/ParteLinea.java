package es.dosxmil.partesexit.mapeo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.ArrayList;

import es.dosxmil.partesexit.MainActivity;
import es.dosxmil.partesexit.utils.Utils;

public class ParteLinea {

    private long id;

    private int codigoEmpresa;
    private int ejercicioParte;
    private String serieParte;
    private int numeroParte;
    private int orden;
    private String codigoArticulo;
    private String descripcionArticulo;
    private String descripcionLinea;
    private String fechaParte;
    private String fechaRegistro;
    private String fechaEntrega;
    private int codigoEmpleado;
    private String nombreCompleto;
    private double precio;
    private double importe;
    private int gastos;
    private double unidades;
    private int facturable;

    private static String[] columnas = new String[]{"_id","CodigoEmpresa","EjercicioParte","SerieParte","NumeroParte",
                                            "Orden","CodigoArticulo","DescripcionArticulo", "DescripcionLinea","FechaParte",
                                            "FechaRegistro","MIL_FechaEntrega","CodigoEmpleado","NombreCompleto","Precio",
                                            "Importe","Gastos", "Unidades", "Facturable"};


    public ParteLinea(int codigoEmpresa, int ejercicioParte, String serieParte, int numeroParte, int orden,
                      String codigoArticulo, String descripcionArticulo, String descripcionLinea, String fechaParte,
                      String fechaRegistro, String fechaEntrega, int codigoEmpleado, String nombreCompleto,
                      double precio, double importe, int gastos, double unidades, int facturable) {
        this.codigoEmpresa = codigoEmpresa;
        this.ejercicioParte = ejercicioParte;
        this.serieParte = serieParte;
        this.numeroParte = numeroParte;
        this.orden = orden;
        this.codigoArticulo = codigoArticulo;
        this.descripcionArticulo = descripcionArticulo;
        this.descripcionLinea = descripcionLinea;
        this.fechaParte = fechaParte;
        this.fechaRegistro = fechaRegistro;
        this.fechaEntrega = fechaEntrega;
        this.codigoEmpleado = codigoEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.precio = precio;
        this.importe = importe;
        this.gastos = gastos;
        this.unidades = unidades;
        this.facturable = facturable;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public void setEjercicioParte(int ejercicioParte) {
        this.ejercicioParte = ejercicioParte;
    }

    public void setSerieParte(String serieParte) {
        this.serieParte = serieParte;
    }

    public void setNumeroParte(int numeroParte) {
        this.numeroParte = numeroParte;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.descripcionArticulo = descripcionArticulo;
    }

    public void setDescripcionLinea(String descripcionLinea) {
        this.descripcionLinea = descripcionLinea;
    }

    public void setFechaParte(String fechaParte) {
        this.fechaParte = fechaParte;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public void setGastos(int gastos) {
        this.gastos = gastos;
    }

    public void setUnidades(double unidades) {
        this.unidades = unidades;
    }

    public void setFacturable(int facturable) {
        this.facturable = facturable;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public int getEjercicioParte() {
        return ejercicioParte;
    }

    public String getSerieParte() {
        return serieParte;
    }

    public int getNumeroParte() {
        return numeroParte;
    }

    public int getOrden() {
        return orden;
    }

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }

    public String getDescripcionLinea() { return descripcionLinea; }

    public String getFechaParte() {
        return fechaParte;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public double getPrecio() {
        return precio;
    }

    public double getImporte() {
        return importe;
    }

    public int getGastos() {
        return gastos;
    }

    public double getUnidades() {
        return unidades;
    }

    public int getFacturable() {
        return facturable;
    }

    public long getId() {
        Cursor c = MainActivity.getDb().query("ParteLineas",columnas,
                "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND NumeroParte = ? AND Orden = ?",
                new String[]{codigoEmpresa+"",ejercicioParte+"",serieParte+"",numeroParte+"", orden+""}, null, null, null);
        if (c.moveToFirst())
            id = c.getLong(0);
        else
            id = 0;

        return id;
    }

    public static Cursor getAllCursor(int codigoEmpresa, int ejercicioParte, String serieParte, int numeroParte) {
        return MainActivity.getDb().query("ParteLineas",columnas,
                "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND numeroParte = ?",
                new String[]{codigoEmpresa+"",ejercicioParte+"",serieParte,numeroParte+"" },
                null,null,null);
    }

    public static void guardar(ParteLinea pl) {

        ContentValues cv = new ContentValues();

        pl.putCv(cv);

        MainActivity.getDb().insert("ParteLineas", null, cv);

    }

    public static void actualizar(ParteLinea pl) {
        ContentValues cv = new ContentValues();
        pl.putCv(cv);
        MainActivity.getDb().update("ParteLineas",cv,"_id=?", new String[]{String.valueOf(pl.getId())});
    }

    public static ParteLinea getLineaPorID(long id) {
        Cursor c = MainActivity.getDb().query(
                "ParteLineas",columnas,"_id=?",new String[]{id+""},
                null,null,null);

            if (c.moveToFirst()) return new ParteLinea(c.getInt(1), c.getInt(2),
                    c.getString(3), c.getInt(4), c.getInt(5),
                    c.getString(6), c.getString(7), c.getString(8),
                    c.getString(9), c.getString(10), c.getString(11),
                    c.getInt(12), c.getString(13), c.getDouble(14),
                    c.getDouble(15), c.getInt(16), c.getDouble(17),
                    c.getInt(18));

            else return null;
    }


    public static int getLastOrdenPl(ParteCabecera pc) {
        Cursor c = MainActivity.getDb().rawQuery("SELECT max(Orden) FROM ParteLineas pl" +
                " WHERE pl.NumeroParte = ? AND pl.EjercicioParte = ? AND pl.SerieParte = ? AND CodigoEmpresa = ?;",
                new String[]{pc.getNumeroParte()+"",pc.getEjercicioParte()+"",pc.getSerieParte(),pc.getCodigoEmpresa()+""});

        if (c.moveToNext()) return c.getInt(0);
        else return 0;

    }

    public static int borrarPartePorID(ArrayList<Long> lista) {
        int borrados = 0;
        for (long l:lista) {
            MainActivity.getDb().delete("ParteLineas","_id = ?",new String[]{l+""});
            borrados++;
        }
        return borrados;
    }

    private void putCv(ContentValues cv) {
        cv.put(columnas[1], this.codigoEmpresa);
        cv.put(columnas[2], this.ejercicioParte);
        cv.put(columnas[3], this.serieParte);
        cv.put(columnas[4], this.numeroParte);
        cv.put(columnas[5], this.orden);
        cv.put(columnas[6], this.codigoArticulo);
        cv.put(columnas[7], this.descripcionArticulo);
        cv.put(columnas[8], this.descripcionLinea);
        cv.put(columnas[9], this.fechaParte);
        cv.put(columnas[10], this.fechaRegistro);
        cv.put(columnas[11], this.fechaEntrega);
        cv.put(columnas[12], this.codigoEmpleado);
        cv.put(columnas[13], this.nombreCompleto);
        cv.put(columnas[14], this.precio);
        cv.put(columnas[15], this.importe);
        cv.put(columnas[16], this.gastos);
        cv.put(columnas[17], this.unidades);
        cv.put(columnas[18], this.facturable);
    }

    public Pair<String, String>[] getArrayValores() {

        Pair[] v = new Pair[]{
                new Pair<String, String>("CodigoEmpresa", this.codigoEmpresa + ""),
                new Pair<String, String>("EjercicioParte", this.ejercicioParte + ""),
                new Pair<String, String>("SerieParte", this.serieParte + ""),
                new Pair<String, String>("NumeroParte", this.numeroParte + ""),
                new Pair<String, String>("Orden", this.orden + ""),
                new Pair<String, String>("CodigoArticulo", this.codigoArticulo),
                new Pair<String, String>("DescripcionArticulo", this.descripcionArticulo),
                new Pair<String, String>("DescripcionLinea", this.descripcionLinea),
                new Pair<String, String>("FechaParte", Utils.FormatoSincronizacion(this.fechaParte)),
                new Pair<String, String>("FechaRegistro", Utils.FormatoSincronizacion(this.fechaRegistro)),
                new Pair<String, String>("CodigoEmpleado", this.codigoEmpleado + ""),
                new Pair<String, String>("NombreCompleto", this.nombreCompleto),
                new Pair<String, String>("Unidades", this.unidades + ""),
                new Pair<String, String>("Precio", this.precio + ""),
                new Pair<String, String>("Importe", this.importe + ""),
                new Pair<String, String>("Facturable", this.facturable + ""),
                new Pair<String, String>("Gastos", this.gastos + ""),
                new Pair<String, String>("Mil_FechaEntrega", Utils.FormatoSincronizacion(this.fechaEntrega))
        };

        return v;
    }
}


