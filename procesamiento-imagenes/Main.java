
/**
 * Write a description of class Main here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Main {
    public static void main(String[] args) {

        // Crear piezas
        Pieza rojo = new Pieza(-65536, false, 10);
        Pieza azul = new Pieza(-16776861, false, 10);

        // recipientes con capacidad 2
        Recipiente r1 = new Recipiente(2);
        Recipiente r2 = new Recipiente(2);
        Recipiente r3 = new Recipiente(2);

        // Llenar recipientes
        // Recipiente 1: [rojo, azul]
        r1.agregarPieza(rojo);
        r1.agregarPieza(azul);

        // Recipiente 2: [azul, rojo]
        r2.agregarPieza(azul);
        r2.agregarPieza(rojo);

        // Recipiente 3: vacío

        // Crear estado
        Recipiente[] recipientes = {r1, r2, r3};
        Estado estado = new Estado(recipientes);

        // Imprimir estado inicial
        System.out.println("ESTADO INICIAL:");
        System.out.println(estado.toString());

        // Resolver
        Solucionador agente = new Solucionador(estado);
        if(agente.solucionar()){
            System.out.println("SOLUCION ENCONTRADA:");
            for(int i = 0; i < agente.getCMovimientos(); i++){
                System.out.println(agente.getMovimientos()[i]);
            }
            System.out.println("ESTADO FINAL:");
            System.out.println(estado.toString());
        } else {
            System.out.println("NO TIENE SOLUCION");
        }
    }
}
