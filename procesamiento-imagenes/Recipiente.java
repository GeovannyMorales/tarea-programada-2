/**
 * Clase que corresponde al recipiente
 * 
 * @author Bryan Morales, Maria Vargas, José Rojas 
 * @version 1.0
 */
public class Recipiente
{
    private Pieza piezas[];
    private int cPiezas; //cantidad piezas actuales
    private int capacidad;

    /**
     * Metodo constructor
     */
    public Recipiente(int capacidad){
        this.piezas = new Pieza[capacidad];
        this.cPiezas = 0;
        this.capacidad = capacidad;
    }

    /**
     * Metodo para agregar una pieza al recipiente, void.
     * @param pieza, pieza que se va agregar.
     */
    public void agregarPieza(Pieza pieza){
        if(cPiezas < capacidad){
            piezas[cPiezas] = pieza;

            cPiezas ++;           
        }
    }

    /**
     * Sacar una pieza del recipiente, actualizar las piezas del recipiente
     * @return pieza superior
     */
    public Pieza sacarPieza() {
        if (cPiezas > 0) {
            Pieza piezaTemp = piezas[cPiezas - 1];
            piezas[cPiezas - 1] = null;
            cPiezas--;
            return piezaTemp;
        }
        return null;
    }
    
    
    /**
     * verificar que el recipiente esté vacio
     * @return true si está vacio
     * @return false si contiene una pieza.
     */
    public boolean estaVacio(){
        return cPiezas == 0;
    }
    
    
    /**
     * verificar si el recipiente está lleno
     * @return true, si el recipiente llego a su capacidad maxima de piezas
     * @return false si faltan piezas
     */
    public boolean estaLleno(){
        return cPiezas == capacidad;
    }
    
    
    /**
     * Obtener la pieza que se encuentre en la posicion superior
     * @return la pieza superior
     * @return null si no hay pieza en el recipiente
     */
    public Pieza getPiezaSuperior(){
        if(!estaVacio()){
            return piezas[cPiezas-1];
        }
            return null;
        
    }
    
    /**
     * verificar si el recipiente ya está resuelto
     * @return true, si en la base no hay piezas, verifica que el recipiente está vacio(cuenta como resuelto)
     * @return false, si la siguiente ficha es diferente a la pieza base
     * @return true, si todas las fichas son iguales.
     */
    public boolean estaResuelto() {
        if (estaVacio()) return true;
        Pieza base = piezas[0];
        for (int i = 1; i < cPiezas; i++) {
            if (!base.esIgual(piezas[i])) return false;
        }
        return true;
    }

    
   public String toString() {
       
        String contenido = "";
        for (int i = 0; i < cPiezas; i++) {
            contenido += piezas[i] + " | ";
            
            
        }
        
        return "Capacidad: " + capacidad + ", Piezas: " + cPiezas + ", Contenido: [" + contenido + "]";
    }
    
    
    //getters
    public Pieza[] getPiezas(){return piezas;}

    public int getCPiezas(){return cPiezas;}

    public int getCapacidad(){return capacidad;}

    //setters
    public void setPiezas(Pieza[] piezas){ this.piezas = piezas;}

    public void setCPiezas(int cPiezas){this.cPiezas = cPiezas;}

    public void setCapacidad(int capacidad){this.capacidad = capacidad;}

}

