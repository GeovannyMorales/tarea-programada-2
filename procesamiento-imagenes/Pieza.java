/**
 * Clase que corresponde a la pieza
 * 
 * @author Bryan Morales, Maria Vargas, José Rojas
 * @version 1.0
 */
public class Pieza
{
   private int color;
   private int tamaño;
   private boolean forma;
   
   public Pieza(int color, boolean forma, int tamaño){
       this.color = color;
       this.forma = forma;
       this.tamaño = tamaño;
   }
   
   
   /**
    * Comparar si una pieza es igual a otra mediante boolean.
    * 
    * @param recibe una pieza diferente a la actual.
    * @return true, si tienen el mismo color, forma y tamaño;
    * @return false, si si al menos un atributo es distinto del otro.
    */
   public boolean esIgual(Pieza pieza) {
        return this.color == pieza.getColor() &&
               this.tamaño == pieza.getTamaño() &&
               this.forma == pieza.getForma();
    }
   

   public String toString() {
       String nombreForma = forma ? "circulo" : "cuadrado";
       return "Color: " + color + ", Tamaño: " + tamaño + ", Forma: " + nombreForma;
   }
    
   //getters
   public int getColor(){return color;}
   public int getTamaño(){return tamaño;}
   public boolean getForma(){return forma;}
   
   //setters
   public void setColor(int color){this.color = color;}
   public void setTamaño(int tamaño){this.tamaño = tamaño;;}
   public void setForma(boolean forma){this.forma = forma;}
      }
