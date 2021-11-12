import org.iesinfantaelena.dao.Cafes;
import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

public class Main {

    public static void main(String[] args) {


        try {
            Cafes cafes = new Cafes();
            cafes.insertar("Cafetito", 150, 1.0f, 100,1000);
            cafes.insertar("Cafe tacilla", 150, 2.0f, 100,1000);
            cafes.verTabla();
            cafes.buscar("tacilla");
            cafes.cafesPorProveedor(150);
            cafes.borrar("Cafe tacilla");
            cafes.verTabla();

            Libros libros = new Libros();

            Libro libro_1 = new Libro(12345,"Sistemas Operativos","Tanembaun","Informatica",156,3);
            Libro libro_2 = new Libro(12453,"Minix","Stallings","Informatica",345,4);
            Libro libro_3 = new Libro(1325,"Linux","Richard Stallman","FSF",168,10);
            Libro libro_4 = new Libro(1725,"Java","Juan Garcia","Programacion",245,9);

            libros.anadirLibro(libro_1);
            libros.anadirLibro(libro_2);
            libros.anadirLibro(libro_3);
            libros.anadirLibro(libro_4);

        } catch (AccesoDatosException e) {
            e.printStackTrace();
        }
    }

}
