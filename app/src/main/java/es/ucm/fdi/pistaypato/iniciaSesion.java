package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

            if(valid) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                //inicia sesion correctamente
                                FirebaseUser user = mAuth.getCurrentUser();
                                //se dirige a la pantalla principal
                            } else {
                                //si falla el inicio de sesion
                                new AlertDialog.Builder(this)
                                        .setTitle("Error")
                                        .setMessage("Error al iniciar sesión")
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