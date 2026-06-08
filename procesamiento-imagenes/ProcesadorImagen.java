import java.util.ArrayList;

/**
 * Clase que procesa una imagen para construir el estado inicial del problema.
 * Lee la matriz de píxeles de un objeto Imagen, identifica los recipientes
 * y las piezas dentro de ellos, y construye un Estado con Recipientes y Piezas.
 *
 * LÓGICA GENERAL:
 *  1. Obtener la matriz de píxeles con imagen.getMatriz()
 *  2. Identificar el color de fondo (píxel [0][0])
 *  3. Detectar columnas que corresponden a recipientes (columnas con píxeles
 *     que no son fondo y no son borde)
 *  4. Dentro de cada recipiente, identificar las piezas de abajo hacia arriba
 *  5. Asignar un número entero a cada tipo de pieza (mismo color+forma+tamaño)
 *  6. Construir objetos Pieza y Recipiente y armar el Estado
 * Le pedi a chat que haga comentarios estilo javadoc

 */
public class ProcesadorImagen {

    private Imagen imagen;
    private int[][] matriz;
    private int colorFondo;
    private int colorBorde;
    private int ancho;
    private int alto;

    /**
     * Constructor: recibe el objeto Imagen ya cargado.
     * @param imagen objeto Imagen con la imagen del problema cargada.
     */
    public ProcesadorImagen(Imagen imagen) {
        this.imagen = imagen;
        this.matriz = imagen.getMatriz();
        this.ancho = imagen.getAncho();
        this.alto = imagen.getAltura();
        this.colorFondo = matriz[0][0]; // esquina superior izquierda = fondo
    }

    /**
     * Método principal: procesa la imagen y devuelve el Estado inicial.
     * @return Estado con todos los recipientes y piezas reconocidos.
     */
    public Estado procesar() {
        // Paso 1: detectar el color de borde (primer color distinto al fondo)
        colorBorde = detectarColorBorde();

        // Paso 2: encontrar los límites (columnas x) de cada recipiente
        ArrayList<int[]> limitesRecipientes = encontrarLimitesRecipientes();

        // Paso 3: para cada recipiente, encontrar sus piezas
        // Primero pasada: recolectar todos los colores de pieza para asignarles IDs
        ArrayList<int[]> todasPiezas = new ArrayList<>(); // [color, forma, tamaño]
        ArrayList<ArrayList<int[]>> piezasPorRecipiente = new ArrayList<>();

        for (int[] limites : limitesRecipientes) {
            ArrayList<int[]> piezas = extraerPiezasDeRecipiente(limites[0], limites[1]);
            piezasPorRecipiente.add(piezas);
            for (int[] pieza : piezas) {
                if (!existeTipo(todasPiezas, pieza)) {
                    todasPiezas.add(pieza);
                }
            }
        }

        // Paso 4: determinar capacidad máxima
        int capacidadMax = 0;
        for (int[] limites : limitesRecipientes) {
            int piezasEnEste = contarPiezasEnRecipiente(limites[0], limites[1]);
            if (piezasEnEste > capacidadMax) {
                capacidadMax = piezasEnEste;
            }
        }
        if (capacidadMax == 0) capacidadMax = 4; // valor por defecto si imagen vacía

        // Paso 5: construir Recipiente[] con Pieza[]
        Recipiente[] recipientes = new Recipiente[limitesRecipientes.size()];

        for (int i = 0; i < limitesRecipientes.size(); i++) {
            recipientes[i] = new Recipiente(capacidadMax);
            ArrayList<int[]> piezas = piezasPorRecipiente.get(i);
            for (int[] tipoPieza : piezas) {
                int idColor = buscarIdTipo(todasPiezas, tipoPieza);
                // color = id numérico, forma = tipoPieza[1]==1 (círculo), tamaño = tipoPieza[2]
                Pieza p = new Pieza(idColor, tipoPieza[1] == 1, tipoPieza[2]);
                recipientes[i].agregarPieza(p);
            }
        }

        // Imprimir leyenda de colores
        System.out.println("=== LEYENDA DE PIEZAS ===");
        for (int i = 0; i < todasPiezas.size(); i++) {
            int[] t = todasPiezas.get(i);
            System.out.println((i + 1) + " = color RGB " + t[0] +
                " forma=" + (t[1] == 1 ? "circulo" : "cuadrado") +
                " tamaño=" + t[2]);
        }

        return new Estado(recipientes);
    }

    /**
     * Detecta el color de borde recorriendo la imagen hasta encontrar
     * el primer color que no es el fondo.
     * @return color de borde como int
     */
    private int detectarColorBorde() {
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = matriz[y][x];
                if (pixel != colorFondo) {
                    return pixel;
                }
            }
        }
        return colorFondo; // fallback (imagen toda de un color)
    }

    /**
     * Encuentra los límites izquierdo y derecho (en x) de cada recipiente.
     * Un recipiente es una región vertical delimitada por píxeles de borde.
     * @return lista de int[]{xIzquierdo, xDerecho} para cada recipiente
     */
    private ArrayList<int[]> encontrarLimitesRecipientes() {
        ArrayList<int[]> limites = new ArrayList<>();
        boolean dentroRecipiente = false;
        int xInicio = 0;

        for (int x = 0; x < ancho; x++) {
            boolean columnaEsBorde = esColumnaDeBorde(x);
            if (columnaEsBorde && !dentroRecipiente) {
                xInicio = x;
                dentroRecipiente = true;
            } else if (!columnaEsBorde && dentroRecipiente) {
                // Verificar que la columna anterior tenía borde (cierre del recipiente)
                if (esColumnaDeBorde(x - 1)) {
                    limites.add(new int[]{xInicio, x - 1});
                    dentroRecipiente = false;
                }
            }
        }
        return limites;
    }

    /**
     * Verifica si una columna x contiene al menos un píxel de borde.
     * @param x columna a verificar
     * @return true si hay píxel de borde en esa columna
     */
    private boolean esColumnaDeBorde(int x) {
        for (int y = 0; y < alto; y++) {
            if (matriz[y][x] == colorBorde) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extrae las piezas de un recipiente identificado por sus límites x.
     * Recorre de abajo hacia arriba buscando regiones de color de pieza
     * (ni fondo, ni borde, ni fondo del recipiente).
     *
     * @param xIzq columna izquierda del recipiente
     * @param xDer columna derecha del recipiente
     * @return lista de int[]{colorId, forma, tamaño} de abajo hacia arriba
     */
    private ArrayList<int[]> extraerPiezasDeRecipiente(int xIzq, int xDer) {
        ArrayList<int[]> piezas = new ArrayList<>();

        // El fondo del recipiente es el color predominante interior (distinto al borde y al fondo global)
        int colorFondoRecipiente = detectarFondoInterior(xIzq, xDer);

        // Rastrear grupos de píxeles de pieza de abajo hacia arriba
        int yActual = alto - 1;
        while (yActual >= 0) {
            // Saltar filas que no tienen pieza
            int colorPieza = colorPiezaEnFila(yActual, xIzq, xDer, colorFondoRecipiente);
            if (colorPieza == -1) {
                yActual--;
                continue;
            }
            // Encontramos una fila de pieza: calcular extensión vertical
            int yInicio = yActual;
            while (yActual >= 0 &&
                   colorPiezaEnFila(yActual, xIzq, xDer, colorFondoRecipiente) != -1) {
                yActual--;
            }
            int yFin = yActual + 1;
            int altura = yInicio - yFin + 1;
            // Determinar forma: si la pieza es más "redonda" es círculo, si cuadrada es cuadrado
            // Simplificación: calculamos el ancho real de la pieza en la fila central
            int yMedio = (yInicio + yFin) / 2;
            int anchoPieza = calcularAnchoPieza(yMedio, xIzq, xDer, colorFondoRecipiente);
            boolean esCirculo = Math.abs(altura - anchoPieza) <= 2; // aprox cuadrado bounding box
            int tamaño = Math.max(altura, anchoPieza);
            piezas.add(0, new int[]{colorPieza, esCirculo ? 1 : 0, tamaño});
        }
        return piezas;
    }

    /**
     * Cuenta cuántas piezas hay en un recipiente (para determinar capacidad máxima).
     */
    private int contarPiezasEnRecipiente(int xIzq, int xDer) {
        return extraerPiezasDeRecipiente(xIzq, xDer).size();
    }

    /**
     * Detecta el color de fondo interior del recipiente (el color más frecuente
     * que no es fondo global ni borde).
     */
    private int detectarFondoInterior(int xIzq, int xDer) {
        int[] colores = new int[1000];
        int[] conteos = new int[1000];
        int nColores = 0;

        for (int y = 0; y < alto; y++) {
            for (int x = xIzq; x <= xDer; x++) {
                int pixel = matriz[y][x];
                if (pixel == colorFondo || pixel == colorBorde) continue;
                boolean encontrado = false;
                for (int i = 0; i < nColores; i++) {
                    if (colores[i] == pixel) {
                        conteos[i]++;
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado && nColores < 1000) {
                    colores[nColores] = pixel;
                    conteos[nColores] = 1;
                    nColores++;
                }
            }
        }

        // El color más frecuente es el fondo interior
        int maxConteo = 0;
        int colorMasFrecuente = colorFondo;
        for (int i = 0; i < nColores; i++) {
            if (conteos[i] > maxConteo) {
                maxConteo = conteos[i];
                colorMasFrecuente = colores[i];
            }
        }
        return colorMasFrecuente;
    }

    /**
     * Devuelve el color de pieza encontrado en una fila dentro del recipiente,
     * o -1 si no hay pieza (solo fondo o borde).
     */
    private int colorPiezaEnFila(int y, int xIzq, int xDer,
                                  int colorFondoRecipiente) {
        for (int x = xIzq; x <= xDer; x++) {
            int pixel = matriz[y][x];
            if (pixel != colorFondo && pixel != colorBorde
                    && pixel != colorFondoRecipiente) {
                return pixel;
            }
        }
        return -1;
    }

    /**
     * Calcula el ancho de una pieza en una fila dada.
     */
    private int calcularAnchoPieza(int y, int xIzq, int xDer,
                                    int colorFondoRecipiente) {
        int conteo = 0;
        for (int x = xIzq; x <= xDer; x++) {
            int pixel = matriz[y][x];
            if (pixel != colorFondo && pixel != colorBorde
                    && pixel != colorFondoRecipiente) {
                conteo++;
            }
        }
        return conteo;
    }

    /**
     * Verifica si un tipo de pieza ya existe en la lista de tipos conocidos.
     * Se compara por color solamente (como principal diferenciador).
     */
    private boolean existeTipo(ArrayList<int[]> tipos, int[] nuevo) {
        for (int[] t : tipos) {
            if (t[0] == nuevo[0]) return true;
        }
        return false;
    }

    /**
     * Busca el ID (posición 1-based) de un tipo de pieza en la lista.
     */
    private int buscarIdTipo(ArrayList<int[]> tipos, int[] buscar) {
        for (int i = 0; i < tipos.size(); i++) {
            if (tipos.get(i)[0] == buscar[0]) return i + 1;
        }
        return 0;
    }
}