import java.sql.*;
import java.util.ArrayList;

public class GestionBDD {

    // Vars conexion
    private static String datosConexion = "jdbc:mysql://localhost/";
    private static String baseDatos = "cine";
    private static String parametros = "?useServerPrepStmts=true";
    private static String usuario = "12miguel34";
    private static String password = "12miguel34";
    private Connection con;

    // Vars datos
    Statement stmt = null;
    ResultSet rs = null;
    ArrayList<String> lista = new ArrayList<>();

    // Métodos conexión
    public Connection abrirConexion() {
        try {
            con = DriverManager.getConnection (datosConexion + baseDatos + parametros, usuario, password);
        } catch (SQLException exception) {
            System.out.println("Error al abrir conexion");
            exception.printStackTrace();
        }
        return con;
    }
    public void cerrarConexion() {
        try {
            con.close();
            System.out.println("Conexion cerrada");
        } catch (SQLException exception) {
            System.out.println("Error al cerrar conexion");
            exception.printStackTrace();
        }
    }

    // Métodos inicio
    public void dropTabla(String tabla) {
        String sql = "DROP TABLE IF EXISTS " + tabla + ";";
        Statement stmt = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/cine", "miguel", "miguel");
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Tabla '" + tabla + "' eliminada");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void crearBDD() {
        String sql = "CREATE DATABASE IF NOT EXISTS " + baseDatos + ";";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            con = DriverManager.getConnection("jdbc:mysql://localhost/cine", "miguel", "miguel");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void crearTablaDirector() {
        String sql = "CREATE TABLE IF NOT EXISTS director " +
                "(" +
                        "id              INT         NOT NULL		AUTO_INCREMENT,"+
                        "nombre    		VARCHAR(80)	NOT NULL,"+
                        "PRIMARY KEY (id)"+
                ");";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void crearTablaPelicula() {
        String sql = "CREATE TABLE IF NOT EXISTS pelicula"+
            "("+
            "id              INT                NOT NULL		AUTO_INCREMENT,"+
            "titulo          VARCHAR(80)        NOT NULL,"+
            "director        INT,"+
            "pais            VARCHAR(50),"+
            "duracion        INT,"+
            "genero          VARCHAR(50),"+
            "PRIMARY KEY (id),"+
            "FOREIGN KEY (director) REFERENCES director (id)"+
            ");";
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodos rs
    public ArrayList <String> recorrerPeliculas() {
        lista.clear();
        try {
            if (rs.next()) {
                lista.add (String.valueOf(rs.getInt(1)));
                lista.add (rs.getString(2));
                lista.add (rs.getString(3));
                lista.add (rs.getString(4));
                lista.add (String.valueOf(rs.getInt(5)));
                lista.add (rs.getString(6));
            } else
                lista.clear(); //vacía la lista para indicar el final
        } catch (SQLException exception) {
            System.out.println("Error al recorrer peliculas");
            exception.printStackTrace();
        }
        return lista;
    }
    public ArrayList <String> recorrerDirectores() {
        lista.clear();
        try {
            if (rs.next()) {
                lista.add (String.valueOf(rs.getInt(1)));
                lista.add (rs.getString(2));
            } else
                lista.clear(); //vacía la lista para indicar el final
        } catch (SQLException exception) {
            System.out.println("Error al recorrer directores");
            exception.printStackTrace();
        }
        return lista;
    }
    public void cerrarResultSet() {
        try {
            rs.close();
            System.out.println("ResultSet cerrado");
        } catch (SQLException exception) {
            System.out.println("Error al cerrar result set");
            exception.printStackTrace();
        }

    }


    // Métodos SQL

    // Select
    public void selectPeliculas(int id,  int director, String genero) {
        String sql = "SELECT * FROM pelicula";
        //añadir condiciones
        if (id != 0) {
            sql += " WHERE id=" + id;
        } else {
            if (director != 0) {
                sql += " WHERE director= " + director;
            }
            if (genero != null) {
                if (director != 0) {
                    sql += " AND";
                } else
                    sql += " WHERE";
                sql += " genero=" + "'" + genero + "'";
            }
        }
        sql += ";";
        System.out.println(sql);
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            System.out.println(sql);

        } catch (Exception e) {
            System.out.println("Error al seleccionar películas");
            e.printStackTrace();
        }
    }
    public void selectDirectores() {
        String sql = "SELECT * FROM director;";
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
        } catch (SQLException exception) {
            System.out.println("Error al seleccionar directores");
            exception.printStackTrace();
        }
    }
    public int selectIDDirector(String nombre) {
        String sql = "SELECT id FROM director WHERE nombre='" + nombre + "';";
        int idDirector = 0;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            if (rs.next())
                idDirector = rs.getInt(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            return idDirector;
        }
    }
    public String selectNombreDirector(int id) {
        String sql = "SELECT nombre FROM director WHERE id='" + id + "';";
        String nombreDirector = "";
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            if (rs.next())
                nombreDirector = rs.getString(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            return nombreDirector;
        }
    }

    // Insert
    public void insertDirector(String nombre) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("INSERT INTO director (nombre) VALUES (?);");
            ps.setString(1, nombre);
            System.out.println(ps.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException exception) {
            System.out.println("Error en inserción");
            exception.printStackTrace();
        }
    }
    public void insertPelicula(String titulo, int director, String pais, int duracion, String genero) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("INSERT INTO pelicula (titulo, director, pais, duracion, genero) VALUES (?, ?, ?, ?, ?);");
            ps.setString(1, titulo);
            ps.setInt(2, director);
            ps.setString(3, pais);
            ps.setInt(4, duracion);
            ps.setString(5, genero);
            System.out.println(ps.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException exception) {
            System.out.println("Error en inserción");
            exception.printStackTrace();
        }
    }

    // Delete
    public void borrar(String from, int id) {
        PreparedStatement ps = null;
        String sql = "DELETE FROM ";
        sql += from;
        sql += " WHERE id=?;";
        System.out.println(sql);
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            System.out.println(ps.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException exception) {
            System.out.println("Error en borrado");
            exception.printStackTrace();
        }

    }

    // Modificar
    public void modificarPelicula(int id, String titulo, int director, String pais, int duracion, String genero) {
        // Crear SQL
        String sql = "UPDATE pelicula SET";
        if (titulo != null)
            sql += " titulo='" + titulo + "',";
        if (director != 0)
            sql += " director=" + director + ",";
        if (pais != null)
            sql += " pais='" + pais + "',";
        if (duracion != 0)
            sql += " duracion='" + duracion + "',";
        if (genero != null)
            sql += " genero='" + genero + "',";
        sql = sql.substring(0, sql.length()-1); //quitar última coma
        sql += " WHERE id='" + id + "';";
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.executeUpdate(sql);
            System.out.println(sql);
        } catch (SQLException exception) {
            System.out.println("Error al modificar");
            exception.printStackTrace();
        }
    }
    public void modificarDirector(int id, String nombre) {
        String sql = "UPDATE director SET";
        sql += " nombre='" + nombre + "'";
        sql += " WHERE id=" + id + ";";
        System.out.println(sql);
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.executeUpdate(sql);
        } catch (SQLException exception) {
            System.out.println("Error al modificar");
            exception.printStackTrace();
        }
    }



    public GestionBDD() {

    }

}
