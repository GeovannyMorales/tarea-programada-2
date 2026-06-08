
/**
 * Clase que implementa solucion 
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Solucionador
{
    Estado estadoActual;
    String[] visitados;
    String[] movimientos;
    int cMovimientos;
    int cVisitados;

    public Solucionador(Estado estadoInicial){
        this.estadoActual = estadoInicial;
        this.visitados = new String[1000];
        this.movimientos = new String[1000];
        this.cMovimientos = 0;
        this.cVisitados = 0;
    }    

    public boolean solucionar() {
        return backtrack(estadoActual);
    }

    private boolean backtrack(Estado estado) {
        if (estado.estaSolucionado()) {
            estadoActual = estado;
            return true;
        }

        String llave = estado.toString();
        if (yaVisitado(llave)) return false;

        visitados[cVisitados] = llave;
        cVisitados++;

        int n = estado.getCRecipientes();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                Estado estadoCopia = estado.copiarEstado();

                if (estadoCopia.moverPieza(i, j)) {
                    movimientos[cMovimientos] = "Mover pieza de recipiente " + (i + 1) + " al recipiente " + (j + 1);
                    cMovimientos++;

                    if (backtrack(estadoCopia)) return true;

                    cMovimientos--;
                }
            }
        }

        return false;
    }

    private boolean yaVisitado(String estado) {
        for (int i = 0; i < cVisitados; i++) {
            if (visitados[i].equals(estado)) return true;
        }
        return false;
    }
    
    //getters

    public String[] getMovimientos() { return movimientos; }
    public int getCMovimientos() { return cMovimientos; }
    public Estado getEstadoFinal() { return estadoActual; }
    
    //setters

    public void setMovimientos(String[] movimientos) { this.movimientos = movimientos; }
    public void setCMovimientos(int cMovimientos) { this.cMovimientos = cMovimientos; }
}