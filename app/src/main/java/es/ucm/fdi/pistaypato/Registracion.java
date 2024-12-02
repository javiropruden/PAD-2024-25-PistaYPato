package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Registracion extends AppCompatActivity {
    private Button registrarse;
    private EditText correo;
    private EditText contrasenia;
    private EditText confirmarContrasenia;
    private EditText nombre;
    private EditText apellido;
    private PPAplication ppa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracion);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        EdgeToEdge.enable(this);

        registrarse = findViewById(R.id.registerButton);
        correo = findViewById(R.id.email);
        contrasenia = findViewById(R.id.password);
        confirmarContrasenia = findViewById(R.id.confirm_password);
        nombre = findViewById(R.id.username);
        apellido = findViewById(R.id.lastname);

        ppa = (PPAplication) getApplication();

        registrarse.setOnClickListener(v -> {
            String email = correo.getText().toString().trim();
            String password = contrasenia.getText().toString().trim();
            String confirmPassword = confirmarContrasenia.getText().toString().trim();
            String name = nombre.getText().toString().trim();
            String lastName = apellido.getText().toString().trim();
            boolean valid = true;

            if (name.isEmpty()) {
                //mostar mensaje de error por no completar el campo correo
                showErrorMessage("Error", "Nombre no introducido");
                valid = false;
            }

            if (valid && email.isEmpty()) {
                //mostar mensaje de error por no completar el campo email
                showErrorMessage("Error", "Email no introducido");
                valid = false;
            }

            if (valid && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //mostar mensaje de error por correo no valido
                showErrorMessage("Error", "Correo no válido");
                valid = false;
            }

            if (valid && lastName.isEmpty()) {
                //mostar mensaje de error por no completar el campo nombre
                showErrorMessage("Error", "Apellido no introducido");
                valid = false;
            }

            if (valid && confirmPassword.isEmpty()) {
                //mostar mensaje de error por no completar el campo contraseña
                showErrorMessage("Error", "Introduce confirmar contraseña");
                valid = false;
            }

            if (valid && !password.equals(confirmPassword)) {
                //mostar mensaje de error por no completar el campo contraseña
                showErrorMessage("Error", "Las contraseñas no coinciden");
                valid = false;
            }

            if(valid) {
                //aqui se registra el usuario realiza el registro en firebase
                User usuario = new User(name, lastName, email, ppa.hashPassword(password));
                ppa.addUser(usuario);
                finish();
                Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorMessage(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }
}