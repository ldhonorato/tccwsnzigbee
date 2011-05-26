/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;
import bancoDados.CriarConexao;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import logica.CanalInstalado;
/**
 *
 * @author Leandro Honorato
 */
public class CanaisInstaladosDAO {
    private Connection conexao;

    public CanaisInstaladosDAO() throws SQLException
    {
        this.conexao = CriarConexao.getConexao();
    }

    public void adiciona(CanalInstalado canalInstalado) throws SQLException{
        //Prepara Conexão
        String sql = "insert into canaisinstalados (fk_sh, fk_sl,idChanel) values (?, ?, ?)";
        java.sql.PreparedStatement stmt = conexao.prepareStatement(sql);

        //Seta os valores
        stmt.setString(1, canalInstalado.getSerialNumberHigh());
        stmt.setString(2, canalInstalado.getSerialNumberLow());
        stmt.setFloat(3, canalInstalado.getCanalLeitura());
        //Executa código SQL
        stmt.execute();
        stmt.close();
    }

    public List<CanalInstalado> getLista() throws SQLException{
            String sql = "select * from canaisinstalados";
            PreparedStatement stmt = (PreparedStatement) this.conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<CanalInstalado> minhaLista = new ArrayList<CanalInstalado>();

            CanalInstalado canalInstalado;
            while(rs.next()){
                    canalInstalado = new CanalInstalado();

                    canalInstalado.setSerialNumberHigh(rs.getString("fk_sh"));
                    canalInstalado.setSerialNumberLow(rs.getString("fk_sl"));
                    canalInstalado.setCanalLeitura(rs.getInt("idChanel"));
                    
                    minhaLista.add(canalInstalado);
            }
            rs.close();
            stmt.close();
            return minhaLista;
    }

    public void remove (CanalInstalado canalInstalado) throws SQLException{
            String sql = "delete from canaisinstalados where (fk_sh=? and fk_sl=? and idChanel=?)";

            PreparedStatement stmt = (PreparedStatement) conexao.prepareStatement(sql);

            stmt.setString(1, canalInstalado.getSerialNumberHigh());
            stmt.setString(2, canalInstalado.getSerialNumberLow());
            stmt.setInt(3, canalInstalado.getCanalLeitura());
            
            stmt.execute();
            stmt.close();
            //System.out.println("OK!");
    }
}
