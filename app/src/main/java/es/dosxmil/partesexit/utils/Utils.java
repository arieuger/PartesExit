package es.dosxmil.partesexit.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());;

    public static String FormatoFecha(String entrada) {
        return sdf.format(Timestamp.valueOf(entrada));
    }

    public static String FormatoFecha (Date d) {
        return sdf.format(d);
    }

    public static Timestamp FormatoATimestamp (String s) throws ParseException {
        return new Timestamp(sdf.parse(s).getTime());
    }

    public static String FormatoSincronizacion(String s) {
        // SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
        // return sdf2.format(new Date(Timestamp.valueOf(s).getTime()));
        return s.split(" ")[0]+"T"+s.split(" ")[1];
    }

    // Para que mes e día teñan sempre dous díxitos
    public static String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    public static boolean isDecimal(String str) {
        try {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static void finishStatic(Activity a) {
        a.finish();
    }
}
