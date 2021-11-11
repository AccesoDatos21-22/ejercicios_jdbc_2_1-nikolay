# Ejercicios JDBC 2.1 de Nikolay Krasenov
###***2.f ¿Dónde llamas a cerrar y liberar? ¿Por qué?*** 
    -Cerrar
    Al método cerrar solo lo podemos llamar en el main debido a que si lo
    llamamos en alguno de los métodos de la clase este no se podrá volver
    a abrir debido a que solo se abre en el constructor de la clase.

    -Liberar
    Al método liberar() lo llamo en el bloque finally de todos los métodos 
    debido a que los atributos a los cuales voy a liberar estan declarados
    en al principio de la clase, por lo cual si llamo al método borrar
    y después al método insertar, cuando se inicialice el método insertar
    los atributos contendrán información del anterior método ejecutado.

###***2.g Piensa en las ventas e inconvenientes de esta nueva versión de Cafes.java***
    -Ventajas
    La principal ventaja que encuentro en esta nueva versión es que el
    código es mucho más eficiente debido a que nos ahorramos muchas líneas
    mediante el método liberar().

    -Inconvenientes
    Otro de los principales inconvenientes que veo es que veo la conexión
    a la base de datos siempre permanece conectada hasta que no la cerremos
    de forma manual en la clase main mediante el método cerrar de la clase
    libros.
    