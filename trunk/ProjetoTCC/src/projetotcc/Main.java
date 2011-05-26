/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package projetotcc;

import bancoDados.CriarConexao;
import com.mysql.jdbc.Connection;
import comunicacaoSerial.ComunicacaoZigBee;
import comunicacaoSerial.SerialComLeituraEscrita;
import comunicacaoSerial.SerialComm;
import dao.LeituraSensorDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import logica.LeituraSensor;
/**
 *
 * @author Leandro Honorato
 */
public class Main extends SerialComm{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
            int intervaloEntreRequisicoes;
            int intervaloEntreCiclos;
            int taxaAtualizacaoListaSensores;
            SerialComm serial = new SerialComm();
            String[] portas = serial.getPortas();
            String index = (String)JOptionPane.showInputDialog(null,"Escolha a COM na qual está conectado o módulo ZigBee.",
                                                                "Escolha a COM",
                                                                JOptionPane.INFORMATION_MESSAGE,
                                                                null,portas, portas[0]);

            String tempoEntreRequisicoes = JOptionPane.showInputDialog("Digite o valor do tempo entre requisições (em ms)","2000");
            intervaloEntreRequisicoes = Integer.parseInt(tempoEntreRequisicoes);

            String tempoEntreCiclos = JOptionPane.showInputDialog("Digite o valor do tempo entre ciclos (em ms)","60000");
            intervaloEntreCiclos = Integer.parseInt(tempoEntreCiclos);

            String taxaAtualizacao = JOptionPane.showInputDialog("Digite o a taxa de atualização da lista de sensores", "100");
            taxaAtualizacaoListaSensores = Integer.parseInt(taxaAtualizacao);
            
            SerialComLeituraEscrita portaSerial = new SerialComLeituraEscrita(index, 9600, 0);
            portaSerial.HabilitarLeitura();
            portaSerial.ObterIdDaPorta();
            portaSerial.AbrirPorta();
            portaSerial.LerDados();
            try {
            ComunicacaoZigBee comunicacao = new ComunicacaoZigBee(portaSerial, intervaloEntreRequisicoes, intervaloEntreRequisicoes, taxaAtualizacaoListaSensores);
            Thread threadComunicacaoZigBee = new Thread(comunicacao);
            threadComunicacaoZigBee.start();
            } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

      
        try {
            ThreadInterface interfaceThread = new ThreadInterface(40);
            Thread threadinterface = new Thread(interfaceThread);
            threadinterface.start();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
