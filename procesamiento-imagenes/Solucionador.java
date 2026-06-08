
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

    public boolean solucionar(){
        if(estadoActual.estaSolucionado()){
            return true;
        }

        if(yaVisitado(estadoActual.toString())){
            return false;
        }

        visitados[cVisitados] = estadoActual.toString();
        cVisitados++;

        for(int i = 0; i < estadoActual.getCRecipientes(); i++){
            
            for(int j = 0; j < estadoActual.getCRecipientes(); j++){
                
                if(i != j){
                    
                    if(estadoActual.moverPieza(i,j)){
                        movimientos[cMovimientos] = "Mover pieza de recipiente " + (i+1) + " a " + (j+1);
                        cMovimientos++;
                        if(solucionar()){
                            return true;
                        }
                        cMovimientos--;
                        estadoActual.moverPieza(j,i); //retroceder movimiento
                    
                    }
                }
            }
        }

        return false;
    }

    /**
     * Verificar que ya se visito un estado
     * @return true, si ya se visito el estado
     * @return false, si no se ha visitado
     */
    private boolean yaVisitado(String estado){
        for(int i = 0; i < cVisitados; i++){
            if(visitados[i].equalsIgnoreCase(estado)){
                return true;
            }
        }
        return false;
    }
    
    //getters
    public String[] getMovimientos(){return movimientos;}
    public int getCMovimientos(){return cMovimientos;}
    
    //setters
    public void setMovimientos(String[] movimientos){
        this.movimientos = movimientos;
    }
    
    public void setCMovimientos(int cMovimientos){
        this.movimientos = movimientos;
    }
}