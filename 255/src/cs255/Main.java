/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255;

/**
 *
 * @author Robert
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 0;
        if(i == 0){
            smalluiGui g = new smalluiGui();
             g.setVisible(true);
        }else{
            Gui g = new Gui();
            g.setVisible(true);
        }
    }
}
