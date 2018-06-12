package es.dosxmil.partesexit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends Activity {

    SharedPreferences sp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = this.getSharedPreferences("sp",MODE_PRIVATE);

        if (sp.getString("TOKEN","").length() > 0)
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        finish();
    }
}
