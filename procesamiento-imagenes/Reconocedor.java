public class Reconocedor {

    private int[][] pixeles;
    private int colorFondo;
    private int alto;
    private int ancho;

    public Reconocedor(int[][] pixeles) {
        this.pixeles = pixeles;
        this.alto = pixeles.length;
        this.ancho = pixeles[0].length;
        this.colorFondo = pixeles[0][0];
    }

    public Estado construirEstado() {

        int[] inicios = detectarRecipientes();

        System.out.println("=== RECIPIENTES DETECTADOS ===");

        for (int i = 0; i < inicios.length; i++) {
            System.out.println("inicios[" + i + "] = " + inicios[i]);
        }

        if (inicios == null || inicios[0] == 0) {
            System.out.println("No se detectaron recipientes.");
            return null;
        }

        int numRecipientes = contarRecipientes(inicios);

        System.out.println("Cantidad de recipientes: " + numRecipientes);

        Recipiente[] recipientes = new Recipiente[numRecipientes];
        int capacidadMaxima = 1;

        for (int r = 0; r < numRecipientes; r++) {

            int xInicio = inicios[r];
            int xFin;

            if (r + 1 < numRecipientes) {
                xFin = inicios[r + 1];
            } else {
                xFin = ancho;
            }

            Pieza[] piezas = extraerPiezas(xInicio, xFin);
            int numPiezas = contarPiezas(piezas);

            System.out.println(
                "Recipiente " + (r + 1)
                + " -> piezas detectadas: "
                + numPiezas
            );

            if (numPiezas > capacidadMaxima) {
                capacidadMaxima = numPiezas;
            }

            Recipiente recipiente =
                new Recipiente(Math.max(1, numPiezas));

            for (int p = 0; p < numPiezas; p++) {
                recipiente.agregarPieza(piezas[p]);
            }

            recipientes[r] = recipiente;
        }

        Recipiente[] finales =
            new Recipiente[numRecipientes];

        for (int r = 0; r < numRecipientes; r++) {

            Recipiente original = recipientes[r];

            Recipiente nuevo =
                new Recipiente(capacidadMaxima);

            for (int p = 0;
                 p < original.getCPiezas();
                 p++) {

                nuevo.agregarPieza(
                    original.getPiezas()[p]
                );
            }

            finales[r] = nuevo;
        }

        return new Estado(finales);
    }

    private int[] detectarRecipientes() {

        int[] inicios = new int[ancho];

        int cantidad = 0;
        boolean dentro = false;

        for (int x = 0; x < ancho; x++) {

            boolean tieneContenido = false;

            for (int y = 0; y < alto; y++) {

                if (pixeles[y][x] != colorFondo) {
                    tieneContenido = true;
                    break;
                }
            }

            if (tieneContenido && !dentro) {

                inicios[cantidad] = x;
                cantidad++;
                dentro = true;

            } else if (!tieneContenido) {

                dentro = false;
            }
        }

        return inicios;
    }

    private int contarRecipientes(int[] inicios) {

        int count = 0;

        for (int i = 0; i < inicios.length; i++) {

            if (inicios[i] > 0) {
                count++;
            }
        }

        return count;
    }

    private Pieza[] extraerPiezas(int xInicio, int xFin) {

        Pieza[] piezas = new Pieza[20];
        int cantidad = 0;

        int colorBorde =
            detectarColorBorde(xInicio);

        boolean[] visitada =
            new boolean[alto];

        for (int y = alto - 1; y >= 0; y--) {

            if (visitada[y]) {
                continue;
            }

            int colorEncontrado = colorFondo;
            int xPieza = -1;

            for (int x = xInicio + 1;
                 x < xFin - 1;
                 x++) {

                int color = pixeles[y][x];

                if (color != colorFondo &&
                    color != colorBorde) {

                    colorEncontrado = color;
                    xPieza = x;
                    break;
                }
            }

            if (xPieza == -1) {
                continue;
            }

            int altoPieza = 0;
            int yTemp = y;

            while (yTemp >= 0 &&
                   pixeles[yTemp][xPieza]
                   == colorEncontrado) {

                visitada[yTemp] = true;
                altoPieza++;
                yTemp--;
            }

            int anchoPieza = 0;

            for (int x = xInicio;
                 x < xFin;
                 x++) {

                if (pixeles[y][x]
                    == colorEncontrado) {

                    anchoPieza++;
                }
            }

            boolean esCirculo =
                Math.abs(
                    altoPieza - anchoPieza
                ) < 3;

            int tamaño =
                altoPieza * anchoPieza;

            piezas[cantidad] =
                new Pieza(
                    colorEncontrado,
                    esCirculo,
                    tamaño
                );

            System.out.println(
                "Pieza detectada -> color: "
                + colorEncontrado
                + " tamaño: "
                + tamaño
            );

            cantidad++;
        }

        return piezas;
    }

    private int detectarColorBorde(int xInicio) {

        for (int y = 0; y < alto; y++) {

            int color =
                pixeles[y][xInicio];

            if (color != colorFondo) {
                return color;
            }
        }

        return colorFondo;
    }

    private int detectarFondoRecipiente(
        int xInicio,
        int xFin,
        int colorBorde) {

        int xCentro =
            (xInicio + xFin) / 2;

        for (int y = 0; y < alto; y++) {

            int color =
                pixeles[y][xCentro];

            if (color != colorFondo &&
                color != colorBorde) {

                return color;
            }
        }

        return colorFondo;
    }

    private int contarPiezas(Pieza[] piezas) {

        int count = 0;

        for (int i = 0;
             i < piezas.length;
             i++) {

            if (piezas[i] != null) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }
}
