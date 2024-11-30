package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class IniciaSesion extends AppCompatActivity {

    private Button iniciarSesion;
    private Button registrarse;
    private EditText contrasenia;
    private EditText correo;
    private PPAplication ppa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inicia_sesion);

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

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //mostar mensaje de error por correo no valido
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("correo no valido")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
                valid = false;
            }

            if (valid) {
                ppa.checkPassword(email, ppa.hashPassword(password), isValid -> {
                    if (isValid) {
                        Intent intent = new Intent(IniciaSesion.this, MainActivity.class);
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
            Intent intent = new Intent(IniciaSesion.this, Registracion.class);
            startActivity(intent);
        });
    }
}
