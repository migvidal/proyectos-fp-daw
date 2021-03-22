/* MIGUEL VIDAL FUNCIA *** DAW_M03II_ACT03_EJERCICIO 2 *** LinkiaFP */


import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {

    private static GestionBDD gestionBDD;//objeto BDD

    public static void main(String[] args) {

        gestionBDD = new GestionBDD();
        gestionBDD.abrirConexion();//abrir conexion

        // Borrar tablas si ya existen
        gestionBDD.dropTabla("pelicula");
        gestionBDD.dropTabla("director");

        gestionBDD.crearBDD();
        gestionBDD.crearTablaDirector();
        gestionBDD.crearTablaPelicula();

        // Insertar valores
        gestionBDD.insertDirector("Christopher Nolan");
        gestionBDD.insertDirector("James Cameron");
        gestionBDD.insertDirector("Ridley Scott");

        gestionBDD.insertPelicula("Interstellar", 1, "EEUU", 160, "Ciencia ficcion");
        gestionBDD.insertPelicula("El caballero oscuro", 1, "EEUU", 152, "Thriller");
        gestionBDD.insertPelicula("Titanic", 2, "EEUU", 195, "Drama");
        gestionBDD.insertPelicula("Avatar", 2, "EEUU", 162, "Ciencia ficcion");
        gestionBDD.insertPelicula("Blade Runner", 3, "EEUU", 117, "Ciencia ficcion - Cyberpunk");
        gestionBDD.insertPelicula("Alien", 3, "EEUU, Reino Unido", 116, "Terror");



        // Crear form y frame

        Formulario formulario = new Formulario();
        JFrame frame = new JFrame();
        formulario.setGestionBDD(gestionBDD);//Formulario se encargará de llamar a las operaciones
        formulario.iniciarDatos();

        // Sobrecargar la [X] para que cierre resultset y connection
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                gestionBDD.cerrarResultSet();
                gestionBDD.cerrarConexion();
            }
        });


        // Mostrar frame
        frame.setContentPane(formulario.getPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
