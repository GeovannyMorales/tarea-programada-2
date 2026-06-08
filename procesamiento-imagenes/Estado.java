
/**
 * Clase que corresponde al estado de los recipientes
 * 
 * @author Bryan Morales, Maria Vargas, José Rojas 
 * @version 1.0
 */
public class Estado
{
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

    public String toString() {
        String recipientesTxt = "";
        for(int i = 0; i < cRecipientes; i++){
            recipientesTxt += "Recipiente:" + (i+1) +" " + recipientes[i] + "\n";
        }

        return "Cantidad de recipientes: " + cRecipientes + ", recipientes:"+ recipientesTxt;

    }

    /**
     * Crear una copia de estado de recipientes actual.
     * crea un nuevo arreglo de recipientes y recorre cada recipiente para copiar las piezas
     * @return copiaEstado, un arreglo de la copia del estado original
     */
    public Estado copiarEstado(){
        Recipiente[] copiaRecipientes = new Recipiente[cRecipientes]; // crear arreglo de recipientes

        for(int i = 0; i < copiaRecipientes.length; i++){
            Recipiente nuevoRecipiente = new Recipiente(recipientes[i].getCapacidad()); // crear nuevo recipiente copia
            for(int j = 0; j < recipientes[i].getCPiezas(); j++){
                nuevoRecipiente.agregarPieza(recipientes[i].getPiezas()[j]); //agregar piezas originales en cada recipiente copia
            }
            copiaRecipientes[i] = nuevoRecipiente; //referenciar la copia al recipiente original
        }
        Estado copiaEstado = new Estado(copiaRecipientes); // crear la copia de los recipientes
        return copiaEstado;
    }

    /**
     * Mover una pieza a otro recipiente
     * @param int o, el recipiente origen de la pieza a mover
     * @param int d, el recipiente destino de la pieza a mover
     * @return true, si el movimiento es valido.
     * @return false, si el movimiento no se realiza.
     */
    public boolean moverPieza(int o,int d){
        //comparar que el recipiente destino no esté lleno y el origen no esté vacio, que el recipiente destino este vacio 
        // o la pieza superior sea igual a la del destino
        if(!recipientes[o].estaVacio() && !recipientes[d].estaLleno() &&
        (recipientes[d].estaVacio() || recipientes[o].getPiezaSuperior().esIgual(recipientes[d].getPiezaSuperior()))){
            recipientes[d].agregarPieza(recipientes[o].sacarPieza());
            return true;
        }
        return false;
    }

    //setters
    public Recipiente[] getRecipientes(){return recipientes;}

    public int getCRecipientes(){return cRecipientes;}

    //getters
    public void setRecipientes(Recipiente[] recipientes){this.recipientes = recipientes;}

    public void setCRecipientes(int cRecipientes){this.recipientes = recipientes;}

}