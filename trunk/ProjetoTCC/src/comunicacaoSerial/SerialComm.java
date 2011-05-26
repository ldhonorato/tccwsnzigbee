/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunicacaoSerial;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;

/**
 *
 * @author Leandro Honorato
 */
public class SerialComm {

    private String[] portas;
    private Enumeration listaDePortas;

    public SerialComm(){
        listaDePortas = CommPortIdentifier.getPortIdentifiers();
    }

    public String[] getPortas(){
        listarPortas();
        return portas;
    }

    private void listarPortas(){
        int i = 0;
        portas = new String[20];
        while (listaDePortas.hasMoreElements()) {
            CommPortIdentifier ips =
            (CommPortIdentifier)listaDePortas.nextElement();
            portas[i] = ips.getName();
            i++;
        }
    }

    public boolean existePorta(String COMp){
        String temp;
        boolean e = false;
        while (listaDePortas.hasMoreElements()) {
            CommPortIdentifier ips = (CommPortIdentifier)listaDePortas.nextElement();
            temp = ips.getName();
            if (temp.equals(COMp)== true) {
                e = true;
                return e;
            }
        }
        return e;
    }
}
