package org.iesinfantaelena.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.iesinfantaelena.utils.Utilidades;
import org.iesinfantaelena.modelo.AccesoDatosException;

/**
 * @descrition
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Cafes   {

    private static Connection con;
    private static ResultSet rs;
    private static PreparedStatement pStmt;
    private static Statement stmt;

    // Consultas a realizar en BD
    private static final String SELECT_CAFES_QUERY = "select CAF_NOMBRE, PROV_ID, PRECIO, VENTAS, TOTAL from CAFES";
    private static final String SEARCH_CAFE_QUERY = "select * from CAFES WHERE CAF_NOMBRE= ?";
    private static final String INSERT_CAFE_QUERY = "insert into CAFES values (?,?,?,?,?)";
    private static final String DELETE_CAFE_QUERY = "delete from CAFES WHERE CAF_NOMBRE = ?";
    private static final String SEARCH_CAFES_PROVEEDOR = "select * from CAFES,PROVEEDORES WHERE CAFES.PROV_ID= ? AND CAFES.PROV_ID=PROVEEDORES.PROV_ID";

    private static final String CREATE_TABLE_PROVEEDORES ="create table if not exists proveedores (PROV_ID integer NOT NULL, PROV_NOMBRE varchar(40) NOT NULL, CALLE varchar(40) NOT NULL, CIUDAD varchar(20) NOT NULL, PAIS varchar(2) NOT NULL, CP varchar(5), PRIMARY KEY (PROV_ID));";

    private static final String CREATE_TABLE_CAFES ="create table if not exists CAFES (CAF_NOMBRE varchar(32) NOT NULL, PROV_ID int NOT NULL, PRECIO numeric(10,2) NOT NULL, VENTAS integer NOT NULL, TOTAL integer NOT NULL, PRIMARY KEY (CAF_NOMBRE), FOREIGN KEY (PROV_ID) REFERENCES PROVEEDORES(PROV_ID));";

    /**
     * Constructor: inicializa conexión
     *
     * @throws AccesoDatosException
     */

    public Cafes() {
        /**
         * Los siguientes atributos los he comentado debido a que me parecia más eficiente llamar directamente
         * al método liberar, pero solo los he comentado dbeido a que en uno de los ejercios pedías que lo
         * hiciesemos de esta forma
         */
        con = null;
        liberar();
        /*
        rs = null;
        pStmt = null;
        stmt = null;

         */

        try {
            con = new Utilidades().getConnection();
            stmt = con.createStatement();

            stmt.executeUpdate(CREATE_TABLE_PROVEEDORES);

            stmt.executeUpdate(CREATE_TABLE_CAFES);

            stmt.executeUpdate("insert into proveedores values(49, 'PROVerior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460');");
            stmt.executeUpdate("insert into proveedores values(101, 'Acme, Inc.', '99 mercado CALLE', 'Groundsville', 'CA', '95199');");
            stmt.executeUpdate("insert into proveedores values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966');");

        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            //Utilidades.printSQLException(sqle);
            System.out.println(sqle.getMessage());


        } finally {
            liberar();
        }
    }

    /**
     * Metodo que muestra por pantalla los datos de la tabla cafes
     *
     * @throws SQLException
     */
    public void verTabla() throws AccesoDatosException {
        try {
            // Creación de la sentencia
            stmt = con.createStatement();
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = stmt.executeQuery(SELECT_CAFES_QUERY);

            // Recuperación de los datos del ResultSet
            while (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total);
            }


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }

    }

    /**
     * Mótodo que busca un cafe por nombre y muestra sus datos
     *
     * @param nombre
     */
    public void buscar(String nombre) throws AccesoDatosException {
        try {
            // Creación de la sentencia
            pStmt = con.prepareStatement(SEARCH_CAFE_QUERY);
            pStmt.setString(1, nombre);
            // Ejecución de la consulta y obtención de resultados en un
            // ResultSet
            rs = pStmt.executeQuery();

            // Recuperación de los datos del ResultSet
            if (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total);
            }


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }

    }

    /**
     * Mótodo para insertar una fila
     *
     * @param nombre
     * @param provid
     * @param precio
     * @param ventas
     * @param total
     * @return
     */
    public void insertar(String nombre, int provid, float precio, int ventas,
                         int total) throws AccesoDatosException {

        try {

            pStmt = con.prepareStatement(INSERT_CAFE_QUERY);
            pStmt.setString(1, nombre);
            pStmt.setInt(2, provid);
            pStmt.setFloat(3, precio);
            pStmt.setInt(4, ventas);
            pStmt.setInt(5, total);
            // Ejecución de la inserción
            pStmt.executeUpdate();


        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            liberar();
        }

    }

    /**
     * Mótodo para borrar una fila dado un nombre de cafó
     *
     * @param nombre
     * @return
     */
    public void borrar(String nombre) throws AccesoDatosException {

        try {
            // Creación de la sentencia
            pStmt = con.prepareStatement(DELETE_CAFE_QUERY);
            pStmt.setString(1, nombre);
            // Ejecución del borrado
            pStmt.executeUpdate();
            System.out.println("café "+nombre+ " ha sido borrado.");

        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            liberar();
        }

    }

    /**
     * Mótodo que busca un cafe por nombre y muestra sus datos
     *
     */
    public void cafesPorProveedor(int provid) throws AccesoDatosException {
        PreparedStatement var2 = null;
        ResultSet var3 = null;

        try {
            con = (new Utilidades()).getConnection();
            var2 = con.prepareStatement("select * from CAFES,PROVEEDORES WHERE CAFES.PROV_ID= ? AND CAFES.PROV_ID=PROVEEDORES.PROV_ID");
            var2.setInt(1, provid);
            var3 = var2.executeQuery();

            while(var3.next()) {
                String var4 = var3.getString("CAF_NOMBRE");
                int var5 = var3.getInt("PROV_ID");
                float var6 = var3.getFloat("PRECIO");
                int var7 = var3.getInt("VENTAS");
                int var8 = var3.getInt("TOTAL");
                String var9 = var3.getString("PROV_NOMBRE");
                String var10 = var3.getString("CALLE");
                String var11 = var3.getString("CIUDAD");
                String var12 = var3.getString("PAIS");
                int var13 = var3.getInt("CP");
                System.out.println(var4 + ", " + var5 + ", " + var6 + ", " + var7 + ", " + var8 + ",Y el proveedor es:" + var9 + "," + var10 + "," + var11 + "," + var12 + "," + var13);
            }
        } catch (IOException var22) {
            System.err.println(var22.getMessage());
            throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
        } catch (SQLException var23) {
            Utilidades.printSQLException(var23);
            throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
        } finally {
            try {
                if (var3 != null) {
                    var3.close();
                }

                if (var2 != null) {
                    var2.close();
                }
            } catch (SQLException var21) {
                Utilidades.printSQLException(var21);
            }

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
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (pStmt != null) {
                pStmt.close();
                pStmt = null;
            }
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }
    }
}
