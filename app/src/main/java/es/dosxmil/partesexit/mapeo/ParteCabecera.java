package es.dosxmil.partesexit.mapeo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dosxmil.partesexit.MainActivity;
import es.dosxmil.partesexit.utils.Utils;

public class ParteCabecera implements Parcelable {

    // no subir
    private long id;

    private int CodigoEmpresa;
    private int EjercicioParte;
    private String SerieParte;
    private int NumeroParte;
    private String StatusParte;
    private String TipoParte;
    private String DescripcionTipoParte;
    private String CodigoArticulo;
    private String DescripcionArticulo;
    private String FechaParte;
    private String FechaEjecucion;
    private int CodigoEmpleado;
    private String NombreCompleto;
    private int CodigoProyecto;
    private String NombreProyecto;
    private String ComentarioCierre;
    private String ComentarioRecepcion;
    private String CodigoCliente;
    private String Nombre;
    private double Importe;
    private double ImporteGastos;
    private double ImporteFacturable;
    private int CodigoUsuario;
    private String FechaUltimaModificacion;

    // no subir
    private int plDescargado;

    private String FechaCierre;
    private double HoraCierre;
    private double TotalUnidades;
    private String NombreUsuarioCierre;
    private int CodigoUsuarioCierre;

    // no subir
    private int sincroMovil;

    private static String[] columnas = {"_id","CodigoEmpresa","EjercicioParte","SerieParte","NumeroParte"
            ,"StatusParte","TipoParte","DescripcionTipoParte","CodigoArticulo","DescripcionArticulo","FechaParte",
            "FechaEjecucion", "CodigoEmpleado","NombreCompleto","CodigoProyecto","NombreProyecto","ComentarioCierre",
            "ComentarioRecepcion","CodigoCliente", "Nombre","Importe","ImporteGastos","ImporteFacturable","CodigoUsuario",
            "FechaUltimaModificacion","PLDescargado", "FechaCierre","HoraCierre","TotalUnidades","NombreUsuarioCierre",
            "CodigoUsuarioCierre", "SincroMovil"};


    public ParteCabecera(int codigoEmpresa, int ejercicioParte, String serieParte, int numeroParte,
                         String statusParte, String tipoParte, String descripcionTipoParte, String codigoArticulo,
                         String descripcionArticulo, String fechaParte, String fechaEjecucion, int codigoEmpleado,
                         String nombreCompleto, int codigoProyecto, String nombreProyecto, String comentarioCierre,
                         String comentarioRecepcion, String codigoCliente, String nombre, double importe, double importeGastos,
                         double importeFacturable, int codigoUsuario, String fechaUltimaModificacion, int plDescargado,
                         String fechaCierre, double horaCierre, double totalUnidades, String nombreUsuarioCierre,
                         int codigoUsuarioCierre, int sincroMovil) {
        this.CodigoEmpresa = codigoEmpresa;
        this.EjercicioParte = ejercicioParte;
        this.SerieParte = serieParte;
        this.NumeroParte = numeroParte;
        this.StatusParte = statusParte;
        this.TipoParte = tipoParte;
        this.DescripcionTipoParte = descripcionTipoParte;
        this.CodigoArticulo = codigoArticulo;
        this.DescripcionArticulo = descripcionArticulo;
        this.FechaParte = fechaParte;
        this.FechaEjecucion = fechaEjecucion;
        this.CodigoEmpleado = codigoEmpleado;
        this.NombreCompleto = nombreCompleto;
        this.CodigoProyecto = codigoProyecto;
        this.NombreProyecto = nombreProyecto;
        this.ComentarioCierre = comentarioCierre;
        this.ComentarioRecepcion = comentarioRecepcion;
        this.CodigoCliente = codigoCliente;
        this.Nombre = nombre;
        this.Importe = importe;
        this.ImporteGastos = importeGastos;
        this.ImporteFacturable = importeFacturable;
        this.CodigoUsuario = codigoUsuario;
        this.FechaUltimaModificacion = fechaUltimaModificacion;
        this.plDescargado = plDescargado;
        this.FechaCierre = fechaCierre;
        this.HoraCierre = horaCierre;
        this.TotalUnidades = totalUnidades;
        this.NombreUsuarioCierre = nombreUsuarioCierre;
        this.CodigoUsuarioCierre = codigoUsuarioCierre;
        this.sincroMovil = sincroMovil;
    }


    // CURSORES
    public static Cursor getAllCursor() {
        Cursor c = MainActivity.getDb().query("ParteCabecera", columnas, null,null,null, null, "EjercicioParte DESC, NumeroParte DESC");
        return c;
    }

    public static Cursor getStatusCursor(String status) {
        Cursor c = MainActivity.getDb().query("ParteCabecera", columnas,  "StatusParte=?", new String[]{status},null, null, "EjercicioParte DESC, NumeroParte DESC");
        return c;
    }

    public static Cursor partesPorKeyword(String query) {
        Cursor c = MainActivity.getDb().query("ParteCabecera",columnas,"Nombre LIKE ?", new String[]{"%"+ query+ "%"},null,null,"EjercicioParte DESC, NumeroParte DESC");
        return c;
    }


    // BÚSQUEDAS
    public static ParteCabecera getByID(long idParte) {
        ParteCabecera pc = null;

        Cursor c = MainActivity.getDb().query("ParteCabecera",columnas, "_id = ?", new String[]{idParte+""}, null, null, null);
        if (c.moveToFirst()) {
            pc = new ParteCabecera(
                    c.getInt(1),
                    c.getInt(2),
                    c.getString(3),
                    c.getInt(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9),
                    c.getString(10),
                    c.getString(11),
                    c.getInt(12),
                    c.getString(13),
                    c.getInt(14),
                    c.getString(15),
                    c.getString(16),
                    c.getString(17),
                    c.getString(18),
                    c.getString(19),
                    c.getDouble(20),
                    c.getDouble(21),
                    c.getDouble(22),
                    c.getInt(23),
                    c.getString(24),
                    c.getInt(25),
                    c.getString(26),
                    c.getDouble(27),
                    c.getDouble(28),
                    c.getString(29),
                    c.getInt(30),
                    c.getInt(31)
            );
        }

        return pc;
    }

    public static int getLastNumeroParte() {
        int i = 0;
        Cursor c = MainActivity.getDb().rawQuery("SELECT max(NumeroParte) FROM ParteCabecera " +
                "WHERE CodigoEmpresa = 2 AND EjercicioParte = " +
                "(SELECT max(EjercicioParte) FROM ParteCabecera);", null);
        if (c.moveToNext())
            i = c.getInt(0);
        return i;
    }

    public static ArrayList<Pair<String, String>> buscarPorNombre(String pclave) {
        ArrayList<Pair<String, String>> resultado = new ArrayList<>();

        String sql = "SELECT CodigoCliente, Nombre FROM Clientes WHERE Nombre LIKE '%" + pclave + "%'";
        Cursor c = MainActivity.getDb().rawQuery(sql,null);
        while (c.moveToNext())
            resultado.add(new Pair<String, String>(c.getString(0),c.getString(1)));

        return resultado;
    }


    // GARDADO/MODIFICACIÓN
    public static void guardar(ParteCabecera pc) {

        ContentValues cv = new ContentValues();

        pc.putCv(cv);

        MainActivity.getDb().insert("ParteCabecera", null, cv);

    }

    public static void actualizar(ParteCabecera pc) {

        ContentValues cv = new ContentValues();
        pc.putCv(cv);

        MainActivity.getDb().update("ParteCabecera",cv,"_id=?",new String[]{pc.getId()+""});

    }

    public static void actualizaImportes(ParteCabecera pc) {
        Cursor c = ParteLinea.getAllCursor(pc.getCodigoEmpresa(),pc.getEjercicioParte(),pc.getSerieParte(),pc.getNumeroParte());

        double importe = 0, importeFacturable = 0, importeGastos = 0;

        while (c.moveToNext()) {
            ParteLinea pl = ParteLinea.getLineaPorID(c.getLong(0));
            importe += pl.getImporte();
            if (pl.getFacturable() != 0 ) importeFacturable += pl.getImporte();
            if (pl.getGastos() != 0 ) importeGastos += pl.getImporte();
        }

        pc.setImporte(importe);
        pc.setImporteFacturable(importeFacturable);
        pc.setImporteGastos(importeGastos);
        pc.setFechaUltimaModificacion(new Timestamp(new Date().getTime()).toString());
        ParteCabecera.actualizar(pc);
    }


    // BORRADOS
    // Para o borrado de múltiples partes: Dende MainActivity
    public static int borrarPartePorID(ArrayList<Long> lista) {
        int borrados = 0;
        for (long l:lista) {
            ParteCabecera pc = ParteCabecera.getByID(l);
            int lineasBorradas = MainActivity.getDb().delete("ParteLineas",
                    "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND NumeroParte = ? ",
                    new String[]{pc.getCodigoEmpresa()+"",pc.getEjercicioParte()+"",pc.getSerieParte(),pc.getNumeroParte()+""});
            Log.d("BORRADOPC",lineasBorradas+"");

            MainActivity.getDb().delete("ParteCabecera","_id = ?",new String[]{l+""});
            borrados++;
        }
        return borrados;
    }

    // Para o borrado de un sólo parte: Dende DetallesParteActivity
    public static void borrarPartePorID(long parteID) {
        ParteCabecera pc = ParteCabecera.getByID(parteID);
        MainActivity.getDb().delete("ParteLineas",
                "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND NumeroParte = ? ",
                new String[]{pc.getCodigoEmpresa()+"",pc.getEjercicioParte()+"",pc.getSerieParte(),pc.getNumeroParte()+""});

        MainActivity.getDb().delete("ParteCabecera","_id = ?",new String[]{parteID+""});
    }


    // SINCRONIZACIÓN
    public static List<ParteCabecera> partesSinSubir(String fumSrv) {
        List<ParteCabecera> pcs = new ArrayList<>();

        String sql = "SELECT * FROM ParteCabecera WHERE FechaUltimaModificacion > '" + fumSrv + "';";
        // String sql = "SELECT * FROM ParteCabecera WHERE SincroMovil = 0;";
        Log.d("SINCCOMP", "SQL: " + sql);
        Cursor c = MainActivity.getDb().rawQuery(sql,null);
        while (c.moveToNext())
            pcs.add(ParteCabecera.getByID(c.getLong(0)));

        return pcs;
    }

    public static String getMaxFUM() {
        String sql = "SELECT max(FechaUltimaModificacion) FROM ParteCabecera;";
        String count = "SELECT EXISTS(SELECT 1 FROM ParteCabecera)";
        Cursor isEmpty = MainActivity.getDb().rawQuery(count, null);
        isEmpty.moveToNext();
        if (isEmpty.getInt(0) != 0) {
            Cursor c = MainActivity.getDb().rawQuery(sql,null);
            c.moveToNext();
            return Utils.FormatoSincronizacion(c.getString(0));

        } else return "1980-01-01T00:00:00.000";

    }




    // GETTERS
    public long getId() {
        Cursor c = MainActivity.getDb().query("ParteCabecera",columnas, "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND NumeroParte = ?", new String[]{CodigoEmpresa +"", EjercicioParte +"", SerieParte +"", NumeroParte +""}, null, null, null);
        if (c.moveToFirst())
            id = c.getLong(0);
        else
            id = 0;

        return id;
    }


    public Pair<String, String>[] getArrayValores() {

        Log.d("SINC", "FechaCierre = " + FechaCierre);
        if (FechaCierre.equals("") || FechaCierre == null) FechaCierre = "1900-01-01 00:00:00";
        Log.d("SINC", FechaCierre);

        String estadoEjecucion = "";
        switch (this.StatusParte) {
            case "A":
                estadoEjecucion= "En ejecucion";
                break;
            case "C":
                estadoEjecucion = "Finalizado";
                break;
        }

        Pair[] v = new Pair[]{
                new Pair<String, String>("CodigoEmpresa", this.CodigoEmpresa +""),
                new Pair<String, String>("EjercicioParte",this.EjercicioParte +""),
                new Pair<String, String>("SerieParte",this.SerieParte +""),
                new Pair<String, String>("NumeroParte",this.NumeroParte +""),
                new Pair<String, String>("StatusParte",this.StatusParte),
                new Pair<String, String>("CodigoArticulo",this.CodigoArticulo),
                new Pair<String, String>("DescripcionArticulo",this.DescripcionArticulo),
                new Pair<String, String>("FechaParte",Utils.FormatoSincronizacion(this.FechaParte)),
                new Pair<String, String>("CodigoEmpleado",this.CodigoEmpleado +""),
                new Pair<String, String>("NombreCompleto",this.NombreCompleto),
                new Pair<String, String>("CodigoProyecto",this.CodigoProyecto +""),
                new Pair<String, String>("NombreProyecto",this.NombreProyecto),
                new Pair<String, String>("ComentarioCierre",this.ComentarioCierre),
                new Pair<String, String>("ComentarioRecepcion",this.ComentarioRecepcion),
                new Pair<String, String>("CodigoCliente",this.CodigoCliente),
                new Pair<String, String>("Importe",this.Importe +""),
                new Pair<String, String>("ImporteGastos",this.ImporteGastos +""),
                new Pair<String, String>("ImporteFacturable",this.ImporteFacturable +""),
                new Pair<String, String>("FechaUltimaModificacion",Utils.FormatoSincronizacion(this.FechaUltimaModificacion)),
                new Pair<String, String>("FechaEjecucion",Utils.FormatoSincronizacion(this.FechaEjecucion)),
                new Pair<String, String>("EstadoEjecucion",estadoEjecucion),
                new Pair<String, String>("CodigoUsuarioCierre",this.CodigoUsuarioCierre +""),
                new Pair<String, String>("FechaCierre",Utils.FormatoSincronizacion(FechaCierre)),
                new Pair<String, String>("HoraCierre",this.HoraCierre +"")};

        return v;
    }

    public Map<String, String> getMapValores() {

        if (FechaCierre.equals("") || FechaCierre == null) FechaCierre = "1900-01-01 00:00:00";
        Log.d("SINC", FechaCierre);

        String estadoEjecucion = "";
        switch (this.StatusParte) {
            case "A":
                estadoEjecucion= "En ejecucion";
                break;
            case "C":
                estadoEjecucion = "Finalizado";
                break;
        }

        Map<String, String> v = new HashMap<String, String>();
                v.put("CodigoEmpresa", this.CodigoEmpresa +"");
                v.put("EjercicioParte",this.EjercicioParte +"");
                v.put("SerieParte",this.SerieParte +"");
                v.put("NumeroParte",this.NumeroParte +"");
                v.put("StatusParte",this.StatusParte);
                v.put("CodigoArticulo",this.CodigoArticulo);
                v.put("DescripcionArticulo",this.DescripcionArticulo);
                v.put("FechaParte",Utils.FormatoSincronizacion(this.FechaParte));
                v.put("CodigoEmpleado",this.CodigoEmpleado +"");
                v.put("NombreCompleto",this.NombreCompleto);
                v.put("CodigoProyecto",this.CodigoProyecto +"");
                v.put("NombreProyecto",this.NombreProyecto);
                v.put("ComentarioCierre",this.ComentarioCierre);
                v.put("ComentarioRecepcion",this.ComentarioRecepcion);
                v.put("CodigoCliente",this.CodigoCliente);
                v.put("Importe",this.Importe +"");
                v.put("ImporteGastos",this.ImporteGastos +"");
                v.put("ImporteFacturable",this.ImporteFacturable +"");
                v.put("FechaUltimaModificacion",Utils.FormatoSincronizacion(this.FechaUltimaModificacion));
                v.put("FechaEjecucion",Utils.FormatoSincronizacion(this.FechaEjecucion));
                v.put("EstadoEjecucion",estadoEjecucion);
                v.put("CodigoUsuarioCierre",this.CodigoUsuarioCierre +"");
                v.put("FechaCierre",Utils.FormatoSincronizacion(FechaCierre));
                v.put("HoraCierre",this.HoraCierre +"");

        return v;
    }

    public static String[] getColumnas() {
        return columnas;
    }
    public int getCodigoEmpresa() {
        return CodigoEmpresa;
    }
    public int getEjercicioParte() {
        return EjercicioParte;
    }
    public String getSerieParte() {
        return SerieParte;
    }
    public int getNumeroParte() {
        return NumeroParte;
    }
    public String getStatusParte() {
        return StatusParte;
    }
    public String getTipoParte() {
        return TipoParte;
    }
    public String getDescripcionTipoParte() {
        return DescripcionTipoParte;
    }
    public String getCodigoArticulo() {
        return CodigoArticulo;
    }
    public String getDescripcionArticulo() {
        return DescripcionArticulo;
    }
    public String getFechaParte() {
        return FechaParte;
    }
    public String getFechaEjecucion() {
        return FechaEjecucion;
    }
    public int getCodigoEmpleado() {
        return CodigoEmpleado;
    }
    public String getNombreCompleto() {
        return NombreCompleto;
    }
    public int getCodigoProyecto() {
        return CodigoProyecto;
    }
    public String getNombreProyecto() {
        return NombreProyecto;
    }
    public String getComentarioCierre() {
        return ComentarioCierre;
    }
    public String getComentarioRecepcion() {
        return ComentarioRecepcion;
    }
    public String getCodigoCliente() {
        return CodigoCliente;
    }
    public String getNombre() {
        return Nombre;
    }
    public double getImporte() {
        return Importe;
    }
    public double getImporteGastos() {
        return ImporteGastos;
    }
    public double getImporteFacturable() {
        return ImporteFacturable;
    }
    public int getCodigoUsuario() {
        return CodigoUsuario;
    }
    public String getFechaUltimaModificacion() {
        return FechaUltimaModificacion;
    }
    public int getPlDescargado() {
        return plDescargado;
    }
    public double getHoraCierre() {
        return HoraCierre;
    }
    public double getTotalUnidades() {
        return TotalUnidades;
    }
    public String getNombreUsuarioCierre() {
        return NombreUsuarioCierre;
    }
    public int getCodigoUsuarioCierre() {
        return CodigoUsuarioCierre;
    }
    public int getSincroMovil() {
            return sincroMovil;
    }

    // SETTERS
    public void setPlDescargado(int plDescargado) {
        this.plDescargado = plDescargado;
    }
    public void setStatusParte(String statusParte) {
        this.StatusParte = statusParte;
    }
    public void setFechaParte(String fechaParte) {
        this.FechaParte = fechaParte;
    }
    public void setFechaEjecucion(String fechaEjecucion) {
        this.FechaEjecucion = fechaEjecucion;
    }
    public void setComentarioCierre(String comentarioCierre) {
        this.ComentarioCierre = comentarioCierre;
    }
    public void setComentarioRecepcion(String comentarioRecepcion) {
        this.ComentarioRecepcion = comentarioRecepcion;
    }
    public void setFechaUltimaModificacion(String fechaUltimaModificacion) {
        this.FechaUltimaModificacion = fechaUltimaModificacion;
    }
    public void setImporteFacturable(double importeFacturable) {
        this.ImporteFacturable = importeFacturable;
    }
    public void setImporteGastos(double importeGastos) {
        this.ImporteGastos = importeGastos;
    }
    public void setImporte(double importe) {
        this.Importe = importe;
    }
    public String getFechaCierre() {
        return FechaCierre;
    }
    public void setFechaCierre(String fechaCierre) {
        this.FechaCierre = fechaCierre;
    }
    public void setHoraCierre(double horaCierre) {
        this.HoraCierre = horaCierre;
    }
    public void setTotalUnidades(double totalUnidades) {
        this.TotalUnidades = totalUnidades;
    }
    public void setNombreUsuarioCierre(String nombreUsuarioCierre) {
        this.NombreUsuarioCierre = nombreUsuarioCierre;
    }
    public void setCodigoUsuarioCierre(int codigoUsuarioCierre) {
        this.CodigoUsuarioCierre = codigoUsuarioCierre;
    }
    public void setSincroMovil(int sincroMovil) {
        this.sincroMovil = sincroMovil;
    }

    // SERIALIZACIÓN
    protected ParteCabecera(Parcel in) {
        CodigoEmpresa = in.readInt();
        EjercicioParte = in.readInt();
        SerieParte = in.readString();
        NumeroParte = in.readInt();
        StatusParte = in.readString();
        TipoParte = in.readString();
        DescripcionTipoParte = in.readString();
        CodigoArticulo = in.readString();
        DescripcionArticulo = in.readString();
        FechaParte = in.readString();
        FechaEjecucion = in.readString();
        CodigoEmpleado = in.readInt();
        NombreCompleto = in.readString();
        CodigoProyecto = in.readInt();
        NombreProyecto = in.readString();
        ComentarioCierre = in.readString();
        ComentarioRecepcion = in.readString();
        CodigoCliente = in.readString();
        Nombre = in.readString();
        Importe = in.readDouble();
        ImporteGastos = in.readDouble();
        ImporteFacturable = in.readDouble();
        CodigoUsuario = in.readInt();
        FechaUltimaModificacion = in.readString();
        plDescargado = in.readInt();
        FechaCierre = in.readString();
        HoraCierre = in.readDouble();
        TotalUnidades = in.readDouble();
        NombreUsuarioCierre = in.readString();
        CodigoUsuarioCierre = in.readInt();
        sincroMovil = in.readInt();
    }

    public static final Creator<ParteCabecera> CREATOR = new Creator<ParteCabecera>() {
        @Override
        public ParteCabecera createFromParcel(Parcel in) {
            return new ParteCabecera(in);
        }

        @Override
        public ParteCabecera[] newArray(int size) {
            return new ParteCabecera[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CodigoEmpresa);
        dest.writeInt(EjercicioParte);
        dest.writeString(SerieParte);
        dest.writeInt(NumeroParte);
        dest.writeString(StatusParte);
        dest.writeString(TipoParte);
        dest.writeString(DescripcionTipoParte);
        dest.writeString(CodigoArticulo);
        dest.writeString(DescripcionArticulo);
        dest.writeString(FechaParte);
        dest.writeString(FechaEjecucion);
        dest.writeInt(CodigoEmpleado);
        dest.writeString(NombreCompleto);
        dest.writeInt(CodigoProyecto);
        dest.writeString(NombreProyecto);
        dest.writeString(ComentarioCierre);
        dest.writeString(ComentarioRecepcion);
        dest.writeString(CodigoCliente);
        dest.writeString(Nombre);
        dest.writeDouble(Importe);
        dest.writeDouble(ImporteGastos);
        dest.writeDouble(ImporteFacturable);
        dest.writeInt(CodigoUsuario);
        dest.writeString(FechaUltimaModificacion);
        dest.writeInt(plDescargado);
        dest.writeString(FechaCierre);
        dest.writeDouble(HoraCierre);
        dest.writeDouble(TotalUnidades);
        dest.writeString(NombreUsuarioCierre);
        dest.writeInt(CodigoUsuarioCierre);
        dest.writeInt(sincroMovil);
    }


    // MÉTODOS PRIVADOS
    private void putCv(ContentValues cv) {
        cv.put(columnas[1], this.CodigoEmpresa);
        cv.put(columnas[2], this.EjercicioParte);
        cv.put(columnas[3], this.SerieParte);
        cv.put(columnas[4], this.NumeroParte);
        cv.put(columnas[5], this.StatusParte);
        cv.put(columnas[6], this.TipoParte);
        cv.put(columnas[7], this.DescripcionTipoParte);
        cv.put(columnas[8], this.CodigoArticulo);
        cv.put(columnas[9], this.DescripcionArticulo);
        cv.put(columnas[10], this.FechaParte);
        cv.put(columnas[11], this.FechaEjecucion);
        cv.put(columnas[12], this.CodigoEmpleado);
        cv.put(columnas[13], this.NombreCompleto);
        cv.put(columnas[14], this.CodigoProyecto);
        cv.put(columnas[15], this.NombreProyecto);
        cv.put(columnas[16], this.ComentarioCierre);
        cv.put(columnas[17], this.ComentarioRecepcion);
        cv.put(columnas[18], this.CodigoCliente);
        cv.put(columnas[19], this.Nombre);
        cv.put(columnas[20], this.Importe);
        cv.put(columnas[21], this.ImporteGastos);
        cv.put(columnas[22], this.ImporteFacturable);
        cv.put(columnas[23], this.CodigoUsuario);
        cv.put(columnas[24], this.FechaUltimaModificacion);
        cv.put(columnas[25], this.plDescargado);
        cv.put(columnas[26], this.FechaCierre);
        cv.put(columnas[27], this.HoraCierre);
        cv.put(columnas[28], this.TotalUnidades);
        cv.put(columnas[29], this.NombreUsuarioCierre);
        cv.put(columnas[30], this.CodigoUsuarioCierre);
        cv.put(columnas[31], this.sincroMovil);
    }
}
