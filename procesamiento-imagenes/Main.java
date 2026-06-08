public class Main {

    public static void main(String[] args) {

        String nombreImagen = "dibujo.gif";

        if (args.length > 0) {
            nombreImagen = args[0];
        }

        System.out.println("Imagen cargada: " + nombreImagen);

        Imagen imagen = new Imagen(nombreImagen);

        int[][] matriz = imagen.getMatriz();

        Reconocedor reconocedor =
            new Reconocedor(matriz);

        Estado estado =
            reconocedor.construirEstado();

        if (estado == null) {
            System.out.println(
                "No se pudo construir el estado."
            );
            return;
        }

        System.out.println(
            "\n========================"
        );
        System.out.println(
            "ESTADO INICIAL"
        );
        System.out.println(
            "========================"
        );

        System.out.println(estado);

        Solucionador agente =
            new Solucionador(estado);

        System.out.println(
            "\nBuscando solucion..."
        );

        if (agente.solucionar()) {

            System.out.println(
                "\n========================"
            );
            System.out.println(
                "SOLUCION ENCONTRADA"
            );
            System.out.println(
                "========================\n"
            );

            for (int i = 0;
                 i < agente.getCMovimientos();
                 i++) {

                System.out.println(
                    (i + 1) + ". "
                    + agente.getMovimientos()[i]
                );
            }

            System.out.println(
                "\n========================"
            );
            System.out.println(
                "ESTADO FINAL"
            );
            System.out.println(
                "========================"
            );

            System.out.println(
                agente.getEstadoFinal()
            );

        } else {

            System.out.println(
                "\nNO TIENE SOLUCION"
            );
        }
    }
}