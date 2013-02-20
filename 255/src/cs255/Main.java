/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255;

import javax.swing.JOptionPane;

/**
 *
 * @author Robert
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter UI version \n 0 = small \n 1 = large"));
        if(i == 0){
            smalluiGui g = new smalluiGui();
             g.setVisible(true);
        }else{
            Gui g = new Gui();
            g.setVisible(true);
        }
    }
}
