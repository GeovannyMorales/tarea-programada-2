/**
 * Clase que corresponde al estado de los recipientes
 * 
 * @author Bryan Morales, Maria Vargas, José Rojas 
 * @version 1.0
 */
public class Estado {
    private Recipiente[] recipientes;
    private int cRecipientes; // cantidad recipiente

    /**
     * Constructor
     */
    public Estado(Recipiente[] recipientes){
        this.recipientes = recipientes;
        this.cRecipientes = recipientes.length;
    }

    //revisar si todos los recipientes ya estan solucionados, si hay un recipiente sin resolver, retornar false
    public boolean estaSolucionado(){

        for(int i = 0; i < this.recipientes.length; i++){
            if(!recipientes[i].estaVacio()){
                if(!recipientes[i].estaLleno()){
                    return false;
                }
                
                if(!this.recipientes[i].estaResuelto()){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Crear una copia de estado de recipientes actual.
     * crea un nuevo arreglo de recipientes y recorre cada recipiente para copiar las piezas
     * @return copiaEstado, un arreglo de la copia del estado original
     */
    public Estado copiarEstado() {
        Recipiente[] copiaRecipientes = new Recipiente[cRecipientes];
        for (int i = 0; i < cRecipientes; i++) {
            Recipiente recipiente = new Recipiente(recipientes[i].getCapacidad());
            Pieza[] originales = recipientes[i].getPiezas();
            for (int j = 0; j < recipientes[i].getCPiezas(); j++) {
                Pieza pieza = originales[j];
                recipiente.agregarPieza(new Pieza(pieza.getColor(), pieza.getForma(), pieza.getTamaño()));
            }
            copiaRecipientes[i] = recipiente;
        }
        return new Estado(copiaRecipientes);
    }

    /**
     * Mover una pieza a otro recipiente
     * @param int o, el recipiente origen de la pieza a mover
     * @param int d, el recipiente destino de la pieza a mover
     * @return true, si el movimiento es valido.
     * @return false, si el movimiento no se realiza.
     */
    public boolean moverPieza(int o, int d) {
        if (!recipientes[o].estaVacio() &&
            !recipientes[d].estaLleno() &&
            (recipientes[d].estaVacio() ||
             recipientes[o].getPiezaSuperior().esIgual(recipientes[d].getPiezaSuperior()))) {
            recipientes[d].agregarPieza(recipientes[o].sacarPieza());
            return true;
        }
        return false;
    }
    
    public String toString() {
        String txt = "";
        for (int i = 0; i < cRecipientes; i++) {
            txt += "Recipiente " + (i + 1) + ": " + recipientes[i] + "\n";
        }
        return txt;
    }

    //setters
    public Recipiente[] getRecipientes(){return recipientes;}

    public int getCRecipientes(){return cRecipientes;}

    //getters
    public void setRecipientes(Recipiente[] recipientes){this.recipientes = recipientes;}

    public void setCRecipientes(int cRecipientes){this.cRecipientes = cRecipientes;}

}
