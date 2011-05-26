/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bancoDados;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Leandro Honorato
 */
public class CriarConexao {
    public static Connection getConexao() throws SQLException {

            try {
                    Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Classe não encontrada", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
            }
            //System.out.println("Conectando ao Banco");
            return (Connection) DriverManager.getConnection("jdbc:mysql://localhost/wsn_zigbee","root", "root");
    }
}
