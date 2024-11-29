package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class iniciaSesion extends AppCompatActivity {

    private Button iniciarSesion;
    private Button registrarse;
    private EditText contrasenia;
    private EditText correo;
    private PPAplication ppa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicia_sesion);

        iniciarSesion = findViewById(R.id.loginButton);
        registrarse = findViewById(R.id.registerButton);
        contrasenia = findViewById(R.id.password);
        correo = findViewById(R.id.useremail);

        ppa = (PPAplication) getApplication();

        iniciarSesion.setOnClickListener(v -> {
            String email = correo.getText().toString().trim();
            String password = contrasenia.getText().toString().trim();
            boolean valid = true;

            if (email.isEmpty()) {
                //mostar mensaje de error por no completar el campo correo
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("completa correo")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
                valid = false;
            }

            if (password.isEmpty()) {
                //mostar mensaje de error por no completar el campo contraseña
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("completa contraseña")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
                valid = false;

            }

            if (valid) {
                ppa.checkPassword(email, ppa.hashPassword(password), isValid -> {
                    if (isValid) {
                        Intent intent = new Intent(iniciaSesion.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Usuario o contraseña incorrectos")
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                                .show();
                    }
                });
            }
        });

        registrarse.setOnClickListener(v -> {
            Intent intent = new Intent(iniciaSesion.this, Registracion.class);
            startActivity(intent);
        });
    }
}
