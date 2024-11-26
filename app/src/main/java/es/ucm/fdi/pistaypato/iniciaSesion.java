package es.ucm.fdi.pistaypato;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class iniciaSesion extends AppCompatActivity {
    Button iniciarSesion;
    Button registrarse;
    EditText contrasenia;
    EditText correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicia_sesion);

        iniciarSesion = findViewById(R.id.loginButton);
        registrarse = findViewById(R.id.registerButton);
        contrasenia = findViewById(R.id.password);
        correo = findViewById(R.id.useremail);

        iniciarSesion.setOnClickListener(v -> {
            //aqui se coge el correo y la contraseña y se comprueba si son correctos

        });

        registrarse.setOnClickListener(v -> {
            Intent intent = new Intent(iniciaSesion.this, Registracion.class);
            startActivity(intent);
        });
    }
}