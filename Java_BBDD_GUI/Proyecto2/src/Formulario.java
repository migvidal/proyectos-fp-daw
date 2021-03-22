import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Formulario {

    private JPanel panel;
    private JButton buscarButton;
    private JTable tablaPeliculas;
    private JComboBox directorCombo;
    private JComboBox generoCombo;
    private JButton eliminarPeliculaButton;
    private JButton modificarPeliculaButton;
    private JButton nuevaPeliculaButton;
    private JTable tablaDirectores;
    private JButton eliminarDirectorButton;
    private JButton modificarDirectorButton;
    private JButton nuevoDirectorButton;

    //Modelos tablas y comboBox
    private DefaultTableModel dtmPeliculas = new DefaultTableModel();
    private DefaultTableModel dtmDirectores = new DefaultTableModel();

    private DefaultComboBoxModel modeloComboDirector = new DefaultComboBoxModel();
    private DefaultComboBoxModel modeloComboGenero = new DefaultComboBoxModel<>();

    //Instancia de gestionBDD
    private GestionBDD gestionBDD;

    //Listas datos
    private ArrayList<String> listaFila;//resultset BDD
    //private ArrayList<String> filaSeleccionada;//selección del usuario en la JTable

    //Métodos BDD
    public void actualizarTablaPeliculas(int director, String genero) {
        dtmPeliculas.setRowCount(0);//limpiar tabla
        gestionBDD.selectPeliculas(0, director, genero);//hacer select
        //guardar resultSet
        do {
            listaFila = gestionBDD.recorrerPeliculas();
            dtmPeliculas.addRow(listaFila.toArray());
        } while (!listaFila.isEmpty());
        dtmPeliculas.removeRow(dtmPeliculas.getRowCount()-1);
        tablaPeliculas.setVisible(true);
    }

    public void actualizarTablaDirectores() {
        dtmDirectores.setRowCount(0);//limpiar tabla
        gestionBDD.selectDirectores();//hacer select
        //guardar resultSet
        do {
            listaFila = gestionBDD.recorrerDirectores();
            dtmDirectores.addRow(listaFila.toArray());
        } while (!listaFila.isEmpty());
        dtmDirectores.removeRow(dtmDirectores.getRowCount()-1);
        tablaDirectores.setVisible(true);
    }

    //Métodos comboBox
    public void actualizarComboDirector() {
        modeloComboDirector.removeAllElements();
        gestionBDD.selectDirectores();
        do {
            listaFila = gestionBDD.recorrerDirectores();
            if (listaFila.isEmpty())
                break;
            //añadir al modelo, comprobando que no se repiten
            boolean repetido = false;
            String elemento = listaFila.get(1);
            for (int i=0; i < modeloComboDirector.getSize(); i++) {
                if (elemento.equals(modeloComboDirector.getElementAt(i)))
                    repetido = true;
            }
            if (!repetido)
                modeloComboDirector.addElement(elemento);
        } while (!listaFila.isEmpty());
        modeloComboDirector.insertElementAt("<Director>", 0);
    }
    public void actualizarComboGenero() {
        modeloComboGenero.removeAllElements();
        gestionBDD.selectPeliculas(0, 0, null);
        do {
            listaFila = gestionBDD.recorrerPeliculas();
            if (listaFila.isEmpty())
                break;
            //añadir al modelo, comprobando que no se repiten
            boolean repetido = false;
            String elemento = listaFila.get(5);
            for (int i=0; i < modeloComboGenero.getSize(); i++) {
                if (elemento.equals(modeloComboGenero.getElementAt(i)))
                    repetido = true;
            }
            if (!repetido)
                modeloComboGenero.addElement(elemento);
        } while (!listaFila.isEmpty());
        modeloComboGenero.insertElementAt("<Género>", 0);
    }

    /* Método película:
    recibe el dialogo y el ID.
    Si ID > 0, actualiza el registro del ID.
    Si ID == 0, inserta nuevo registro.*/
    public void comprobarModificarPelicula(DialogoPelicula dialogoPelicula, int id) {
        //Comprobar
        if (dialogoPelicula.isOkClick()) {
            //Obtener datos
            String[] valores = dialogoPelicula.getValoresTxt();

            //comprobar inputs
            boolean valoresCorrectos = true;
            for (String v : valores) {
                if (v.equals("") || dialogoPelicula.getIndexDirectorCombo() == 0)
                    valoresCorrectos = false;
            }
            //compobar que la duración es un número
            int nuevaDuracion;
            try {
                nuevaDuracion = Integer.parseInt(valores[2]);
            } catch (NumberFormatException nfe) {
                valoresCorrectos = false;
                nuevaDuracion = -1;
            }
            //Modificar
            if (valoresCorrectos) {
                String nombreDirector = dialogoPelicula.getTxtDirectorCombo();
                int idDirector = gestionBDD.selectIDDirector(nombreDirector);//obtiene el id del director
                //Operación BDD
                if (id == 0)
                    gestionBDD.insertPelicula(valores[0], idDirector, valores[1], nuevaDuracion, valores[3]);
                else
                    gestionBDD.modificarPelicula(id, valores[0], idDirector, valores[1], nuevaDuracion, valores[3]);
            } else {
                JOptionPane.showMessageDialog(panel, "Error: algún dato es incorrecto", "", JOptionPane.WARNING_MESSAGE);
            }

            //Actualizar campos
            actualizarTablaPeliculas(0, null);
            actualizarComboGenero();
        }
    }

    public void iniciarDatos() {
        //rellenar comboBoxes
        actualizarComboDirector();
        actualizarComboGenero();

        //mostrar directores
        actualizarTablaDirectores();
    }


    //CONSTRUCTOR

    public Formulario() {

        //INICIO

        //Establecer modelos
        dtmPeliculas.setColumnIdentifiers(new String[]{"ID", "Título", "Director", "País", "Duración", "Género"});
        dtmDirectores.setColumnIdentifiers(new String[]{"ID", "Nombre"});
        tablaPeliculas.setModel(dtmPeliculas);
        tablaDirectores.setModel(dtmDirectores);
        generoCombo.setModel(modeloComboGenero);
        directorCombo.setModel(modeloComboDirector);

        //ACTION LISTENERS

        //Peliculas
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Obtener selección de las ComboBox
                int director;
                if (directorCombo.getSelectedIndex() == 0) {
                    director = 0; //si no se selecciona opcion, es null
                } else {
                    String nombreDirector = (String) directorCombo.getSelectedItem();
                    director = gestionBDD.selectIDDirector(nombreDirector);//obtener id
                }

                String genero;
                if (generoCombo.getSelectedIndex() == 0) {
                    genero = null;
                } else
                    genero = (String) generoCombo.getSelectedItem();

                //Mostrar peliculas
                actualizarTablaPeliculas(director, genero);
            }
        });

        eliminarPeliculaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaPeliculas.getSelectedRow();
                int id = Integer.parseInt((String) dtmPeliculas.getValueAt(filaSeleccionada, 0));
                //Borrar registro
                gestionBDD.borrar("pelicula", id);
                //Actualizar tabla
                dtmPeliculas.removeRow(filaSeleccionada);
                actualizarTablaPeliculas(0, null);
            }
        });

        nuevaPeliculaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Crear dialog
                DialogoPelicula dialogoPelicula = new DialogoPelicula();

                //Rellenar combo
                dialogoPelicula.setDirectorCombo(modeloComboDirector, 0);
                dialogoPelicula.setVisible(true);

                //Obtener datos
                comprobarModificarPelicula(dialogoPelicula, 0);
            }
        });

        modificarPeliculaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tablaPeliculas.getSelectedRow() > -1) {
                    int fila = tablaPeliculas.getSelectedRow();
                    int id = Integer.valueOf((String) dtmPeliculas.getValueAt(fila, 0));

                    //Guardar datos
                    String titulo = (String) dtmPeliculas.getValueAt(fila, 1);
                    String pais = (String) dtmPeliculas.getValueAt(fila, 3);
                    String duracion = String.valueOf(dtmPeliculas.getValueAt(fila, 4));
                    String genero = (String) dtmPeliculas.getValueAt(fila, 5);

                    int idDirector = Integer.parseInt((String) dtmPeliculas.getValueAt(fila, 2));
                    int indiceCombo = modeloComboDirector.getIndexOf(gestionBDD.selectNombreDirector(idDirector));

                    //Crear dialog
                    DialogoPelicula dialogoPelicula = new DialogoPelicula();

                    //Rellenar inputs
                    dialogoPelicula.setDirectorCombo(modeloComboDirector, indiceCombo);
                    String[] valoresTxt = {titulo, pais, duracion, genero};
                    dialogoPelicula.setTextFieldsTexto(valoresTxt);
                    dialogoPelicula.setVisible(true);

                    comprobarModificarPelicula(dialogoPelicula, id);
                }
            }
        });

        //Directores
        eliminarDirectorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Obtener id de la fila
                int filaSeleccionada = tablaDirectores.getSelectedRow();

                //si ok clicado
                if (filaSeleccionada > -1 && filaSeleccionada <= dtmDirectores.getRowCount()) {
                    int id = Integer.parseInt((String) dtmDirectores.getValueAt(filaSeleccionada, 0));
                    int director = Integer.parseInt((String) dtmDirectores.getValueAt(filaSeleccionada, 0));

                    //Comprobar si tiene películas
                    gestionBDD.selectPeliculas(0, director, null);
                    boolean tienePeliculas = false;
                    do {
                        listaFila = gestionBDD.recorrerPeliculas();
                        if (!listaFila.isEmpty()) {
                            tienePeliculas = true;
                        }
                    } while (!listaFila.isEmpty());
                    if (!tienePeliculas) {
                        gestionBDD.borrar("director", id);
                        dtmDirectores.removeRow(filaSeleccionada);//actualizar tabla
                        //Actualizar campos
                        actualizarTablaPeliculas(0, null);
                        actualizarComboDirector();
                    } else {
                        JOptionPane.showMessageDialog(panel, "No se puede eliminar un director si tiene películas", "", JOptionPane.WARNING_MESSAGE);
                    }
                }



            }
        });

        modificarDirectorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tablaDirectores.getSelectedRow() > -1) {
                    int fila = tablaDirectores.getSelectedRow();

                    //Guardar datos en lista
                    String nombre;
                    nombre = (String) dtmDirectores.getValueAt(fila, 1);

                    //Crear dialogo
                    DialogoDirector dialogoDirector = new DialogoDirector();
                    dialogoDirector.setTextNombreTxt(nombre);
                    dialogoDirector.setVisible(true);

                    //Guardar valores
                    int id = Integer.valueOf((String) dtmDirectores.getValueAt(fila, 0));
                    String nuevoNombre = dialogoDirector.getNombre();

                    if (dialogoDirector.isOkClick() && !nuevoNombre.equals("")) {
                        gestionBDD.modificarDirector(id, nuevoNombre);
                    }
                    actualizarTablaDirectores();
                }
            }
        });

        nuevoDirectorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogoDirector dialogoDirector = new DialogoDirector();
                dialogoDirector.setVisible(true);
                String nombreDirector = dialogoDirector.getNombre();
                if (dialogoDirector.isOkClick() && !nombreDirector.equals("")) {
                    gestionBDD.insertDirector(nombreDirector);
                    actualizarTablaDirectores();
                    actualizarTablaPeliculas(0, null);
                    actualizarComboDirector();
                    tablaPeliculas.setVisible(true);
                    tablaDirectores.setVisible(true);
                }

            }
        });
    }

    //GETTERS Y SETTERS
    public JPanel getPanel() {
        return panel;
    }
    public void setGestionBDD(GestionBDD gestionBDD) {
        this.gestionBDD = gestionBDD;
    }
}
