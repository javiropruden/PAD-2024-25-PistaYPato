package es.ucm.fdi.pistaypato;

import static org.mockito.Mockito.*;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import es.ucm.fdi.pistaypato.Registracion;

import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(manifest = "src/main/AndroidManifest.xml")
public class RegistracionUnitTest {

    private Registracion registracionActivity;
    private EditText correo;
    private EditText contrasenia;
    private EditText confirmarContrasenia;
    private EditText nombre;
    private EditText apellido;
    private Button registrarse;

    @Before
    public void setUp() {
        ActivityScenario<IniciaSesion> scenario = ActivityScenario.launch(IniciaSesion.class);
        scenario.onActivity(iniciarSesionActivity -> {
            Button registrarButton = iniciarSesionActivity.findViewById(R.id.registerButton);
            registrarButton.performClick();

            ActivityScenario<Registracion> registracionScenario = ActivityScenario.launch(Registracion.class);
            registracionScenario.onActivity(activity -> {
                this.registracionActivity = activity;
                correo = activity.findViewById(R.id.email);
                contrasenia = activity.findViewById(R.id.password);
                confirmarContrasenia = activity.findViewById(R.id.confirm_password);
                nombre = activity.findViewById(R.id.username);
                apellido = activity.findViewById(R.id.lastname);
                registrarse = activity.findViewById(R.id.registerButton);
            });
        });
    }

    @Test
    public void testValidRegistration() {
        // Set valid data
        nombre.setText("John");
        apellido.setText("Doe");
        correo.setText("john.doe@example.com");
        contrasenia.setText("password123");
        confirmarContrasenia.setText("password123");

        // Mock AlertDialog
        AlertDialog.Builder builder = Mockito.mock(AlertDialog.Builder.class);
        AlertDialog dialog = Mockito.mock(AlertDialog.class);
        when(builder.create()).thenReturn(dialog);
        doNothing().when(dialog).show();

        // Click register button
        registrarse.performClick();

        // Verify no error dialog is shown
        verify(dialog, never()).show();
    }
}