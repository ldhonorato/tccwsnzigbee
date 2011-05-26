/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package projetotcc;

import dao.LeituraSensorDAO;
import java.sql.SQLException;
import logica.LeituraSensor;

/**
 *
 * @author Leandro Honorato
 */
public class ThreadInterface implements Runnable{

    private FrameLDR frame;
    private LeituraSensorDAO leituraSensorDAO;
    private int tempoBusca;

    public ThreadInterface(int tempoBusca) throws SQLException {
        frame = new FrameLDR();
        leituraSensorDAO = new LeituraSensorDAO();
        this.tempoBusca = tempoBusca;
    }

    public void run() {
        LeituraSensor leitura;
        //System.out.println("Rodando...");
        this.frame.setVisible(true);
        int valor;
        while(true)
         {
            try {
                if(this.leituraSensorDAO == null)
                {
                    this.leituraSensorDAO = new LeituraSensorDAO();
                }
                leitura = this.leituraSensorDAO.getLastInsertion();
                //valor = (int) ((leitura.getValorLeitura() / 1024) * 255);
                //System.out.println("Valor da leitura é: " + leitura.getValorLeitura());
                //System.out.println("Valor é:" + (int)valor);
                frame.setColor((int)leitura.getValorLeitura());
                //frame.setColor(0);
                Thread.sleep(tempoBusca);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

         }
    }

}
