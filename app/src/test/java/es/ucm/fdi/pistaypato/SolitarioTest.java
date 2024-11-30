package es.ucm.fdi.pistaypato;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SolitarioTest {

    private Solitario solitario;
    private List<User> usuarios;

    @Before
    public void setUp() {
        // Configuración inicial para las pruebas
        usuarios = new ArrayList<>();
        usuarios.add(new User("Alice", "Smith", "alice@example.com", "password123"));
        usuarios.add(new User("Bob", "Brown", "bob@example.com", "password456"));
        solitario = new Solitario("123", "Parque", usuarios, "30/12/2024");
    }

    @Test
    public void testConstructorConUsuarios() {
        // Verificar que el constructor con usuarios inicializa correctamente
        assertEquals("123", solitario.getId());
        assertEquals("Parque", solitario.getLugar());
        assertEquals(2, solitario.getNumPersonas());
        assertEquals("30/12/2024", solitario.getFecha());
        assertEquals(usuarios, solitario.getUsuarios());
    }

    @Test
    public void testConstructorSinUsuarios() {
        // Verificar que el constructor sin lista de usuarios inicializa correctamente
        Solitario solitarioSinUsuarios = new Solitario("456", "Piscina", 3, "30/12/2024");
        assertEquals("456", solitarioSinUsuarios.getId());
        assertEquals("Piscina", solitarioSinUsuarios.getLugar());
        assertEquals(3, solitarioSinUsuarios.getNumPersonas());
        assertEquals("30/12/2024", solitarioSinUsuarios.getFecha());
        assertNull(solitarioSinUsuarios.getUsuarios()); // Usuarios debería ser null
    }

    @Test
    public void testGettersYSetters() {
        // Probar setters y getters
        solitario.setId("789");
        assertEquals("789", solitario.getId());

        solitario.setLugar("Campo");
        assertEquals("Campo", solitario.getLugar());

        solitario.setFecha("30/12/2024");
        assertEquals("30/12/2024", solitario.getFecha());

        List<User> nuevosUsuarios = new ArrayList<>();
        nuevosUsuarios.add(new User("Charlie", "Davis", "charlie@example.com", "password789"));
        solitario.setUsuarios(nuevosUsuarios);
        assertEquals(nuevosUsuarios, solitario.getUsuarios());

        solitario.setNumPersonas(5);
        assertEquals(5, solitario.getNumPersonas());
    }

    @Test
    public void testAddUsuario() {
        // Verificar que addUsuario funciona correctamente
        User nuevoUsuario = new User("Charlie", "Davis", "charlie@example.com", "password789");
        solitario.addUsuario(nuevoUsuario);
        assertEquals(3, solitario.getUsuarios().size());
        assertEquals("Charlie", solitario.getUsuarios().get(2).getFirstName());
    }

    @Test
    public void testConstructorVacio() {
        // Verificar que el constructor vacío funciona correctamente
        Solitario solitarioVacio = new Solitario();
        assertNull(solitarioVacio.getId());
        assertNull(solitarioVacio.getLugar());
        assertNull(solitarioVacio.getFecha());
        assertNull(solitarioVacio.getUsuarios());
        assertEquals(0, solitarioVacio.getNumPersonas());
    }
}
