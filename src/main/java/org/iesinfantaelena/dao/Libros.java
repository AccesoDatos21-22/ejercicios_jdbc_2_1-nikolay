package org.iesinfantaelena.dao;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import org.iesinfantaelena.modelo.Libro;
import org.iesinfantaelena.utils.Utilidades;
import org.iesinfantaelena.modelo.AccesoDatosException;


/**
 * @descrition
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Libros {

    // Consultas a realizar en BD


    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pstmt;


    private static final String SELECT_LIBROS_QUERY = "select isbn, titulo, autor, editorial, paginas, copias from libros";
    private static final String DELETE_LIBRO_QUERY = "delete from LIBROS WHERE isbn = ?";
    private static final String SEARCH_LIBRO_QUERY = "select * from libros WHERE isbn = ?";
    private static final String UPDATE_LIBRO_QUERY = "update libros from set isbn = ?, titulo = ?, autor = ?, editorial = ?, paginas = ?, copias = ?";

    /**
     * Constructor: inicializa conexión
     *
     * @throws AccesoDatosException
     */

    public Libros() throws AccesoDatosException {
        try {
            // Obtenemos la conexión
            this.con = new Utilidades().getConnection();
            this.stmt = null;
            this.rs = null;
            this.pstmt = null;
        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        }
    }


    /**
     * Método para cerrar la conexión
     *
     * @throws AccesoDatosException
     */
    public void cerrar() {

        if (con != null) {
            Utilidades.closeConnection(con);
        }

    }


    /**
     * Método para liberar recursos
     *
     * @throws AccesoDatosException
     */
    private void liberar() {
        try {
            // Liberamos todos los recursos pase lo que pase
            //Al cerrar un stmt se cierran los resultset asociados. Podíamos omitir el primer if. Lo dejamos por claridad.
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }
    }

    /**
     * Metodo que muestra por pantalla los datos de la tabla cafes
     * @throws SQLException
     */

    public List<Libro> verCatalogo() throws AccesoDatosException {

        /* Sentencia sql */
        Statement stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;
        try {

            // Creación de la sentencia
            stmt = con.createStatement();
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
        return (List<Libro>) rs;

    }

    /**
     * Actualiza el numero de copias para un libro
     * @throws AccesoDatosException
     */

    public void actualizarCopias(Libro libro) throws AccesoDatosException {
        /* Sentencia sql */
        PreparedStatement stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;
        try {
            // Creación de la sentencia
            stmt = con.prepareStatement(SEARCH_LIBRO_QUERY);
            stmt.setInt(1, libro.getISBN());
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery();

            // Recuperación de los datos del ResultSet
            if (rs != null) {
                stmt = con.prepareStatement(UPDATE_LIBRO_QUERY);
                stmt.executeQuery();
            }


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }


    /**
     * Añade un nuevo libro a la BD
     * @throws AccesoDatosException
     */
    public void anadirLibro(Libro libro) throws AccesoDatosException {


    }

    /**
     * Borra un libro por ISBN
     * @throws AccesoDatosException
     */

    public void borrar(Libro libro) throws AccesoDatosException {
        /* Sentencia sql */
        PreparedStatement stmt = null;

        try {
            // Creación de la sentencia
            stmt = con.prepareStatement(DELETE_LIBRO_QUERY);
            stmt.setInt(1, libro.getISBN());
            // Ejecución del borrado
            stmt.executeUpdate();
            System.out.println("El libro " + libro.getTitulo() + " ha sido borrado.");

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }

        }

    }

    /**
     * Devulve los nombres de los campos de BD
     * @return
     * @throws AccesoDatosException
     */

    public String[] getCamposLibro() throws AccesoDatosException {
        /* Sentencia sql */
        PreparedStatement stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;

        String[] columnas = null;

        try {
            DatabaseMetaData dbmd = con.getMetaData();
            rs = dbmd.getColumns(null, null,null,null);

            // Recuperación de los datos del ResultSet
            int cont = 0;

            while (rs.next()) {
                String nombre = rs.getString("COLUMN_NAME");
                columnas[cont] = nombre;
                cont++;
            }

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
        return columnas;
    }


    public void obtenerLibro(int ISBN) throws AccesoDatosException {
        /* Sentencia sql */
        PreparedStatement stmt = null;
        /* Conjunto de Resultados a obtener de la sentencia sql */
        ResultSet rs = null;
        try {
            // Creación de la sentencia
            stmt = con.prepareStatement(SEARCH_LIBRO_QUERY);
            stmt.setInt(1, ISBN);
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery();

            // Recuperación de los datos del ResultSet
            if (rs.next()) {
                int isbn = rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas= rs.getInt("paginas");
                int copias = rs.getInt("copias");
                System.out.println(isbn + ", " + titulo + ", "
                        + autor + ", " + editorial + ", " + paginas + ", " + copias);
            }


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
    }
}
