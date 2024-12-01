package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, manifest = Config.NONE)
public class RegistracionUnitTest {
    private Registracion registracion;
    private EditText correo;
    private EditText contrasenia;
    private EditText confirmarContrasenia;
    private EditText nombre;
    private EditText apellido;
    private Button registrarse;
    private DatabaseReference databaseReference;



    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.getApplication();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:183992745022:android:c9b3529347b6eb17aae56f") // Required for Analytics.
                .setApiKey("AIzaSyBhp_L2GRWjBkwZeL7mWClVR9Qlf6btu5s") // Required for Auth.
                .setDatabaseUrl("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/") // Required for RTDB.
                .build();

        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context, options);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Users");

        registracion = Mockito.spy(new Registracion());
        correo = mock(EditText.class);
        contrasenia = mock(EditText.class);
        confirmarContrasenia = mock(EditText.class);
        nombre = mock(EditText.class);
        apellido = mock(EditText.class);
        registrarse = mock(Button.class);

        doReturn(correo).when(registracion).findViewById(R.id.email);
        doReturn(contrasenia).when(registracion).findViewById(R.id.password);
        doReturn(confirmarContrasenia).when(registracion).findViewById(R.id.confirm_password);
        doReturn(nombre).when(registracion).findViewById(R.id.username);
        doReturn(apellido).when(registracion).findViewById(R.id.lastname);
        doReturn(registrarse).when(registracion).findViewById(R.id.registerButton);

        mockEditText(correo);
        mockEditText(contrasenia);
        mockEditText(confirmarContrasenia);
        mockEditText(nombre);
        mockEditText(apellido);

        doAnswer(invocation -> {
            View.OnClickListener listener = (View.OnClickListener) registrarse.getTag();
            if (listener != null) {
                listener.onClick(null);
            }
            return null;
        }).when(registrarse).performClick();
    }

    private void mockEditText(EditText editText) {
        Editable editable = mock(Editable.class);
        when(editText.getText()).thenReturn(editable);
        when(editable.toString()).thenReturn("");
    }

    @Test
    public void testRegisterAndVerifyInDatabase() {
        when(nombre.getText().toString()).thenReturn("Nombre");
        when(correo.getText().toString()).thenReturn("correo@valido.com");
        when(apellido.getText().toString()).thenReturn("Apellido");
        when(contrasenia.getText().toString()).thenReturn("password");
        when(confirmarContrasenia.getText().toString()).thenReturn("password");
        registrarse.performClick();

        // Verify AlertDialog is not shown
        AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
        assertNull(alert);

        // Verify data in Firebase Realtime Database
        databaseReference.child("correo@valido,com").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                assertNotNull(dataSnapshot);
                assertEquals("Nombre", dataSnapshot.child("firstName").getValue(String.class));
                assertEquals("Apellido", dataSnapshot.child("lastName").getValue(String.class));
            } else {
                fail("Failed to retrieve data from Firebase");
            }
        });
    }
}