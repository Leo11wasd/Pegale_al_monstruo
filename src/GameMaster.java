public class GameMaster {
    int sizeTablero;
    public GameMaster() {
        //tablero de 3x3
    }

    public int produce_coordenadas(){
        /*en el receptor, se obtiene columna y renglon como:
        r=val/3;
        c=val%3;
        * */
        return (int) (Math.random() * (8));
    }



}
