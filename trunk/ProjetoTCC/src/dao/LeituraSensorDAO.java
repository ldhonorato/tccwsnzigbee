/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import bancoDados.CriarConexao;
import com.mysql.jdbc.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import logica.LeituraSensor;

/**
 *
 * @author Leandro Honorato
 */
public class LeituraSensorDAO {

    private Connection conexao;

    public LeituraSensorDAO() throws SQLException
    {
        this.conexao = CriarConexao.getConexao();
    }

    public void adiciona(LeituraSensor leituraSensor) throws SQLException{
        //Prepara Conexão
        String sql = "insert into leiturasensor (fk_sh, fk_sl, valorleituraS1, chanel_ADC ,datahora) values (?, ?, ?, ?, NOW())";
        java.sql.PreparedStatement stmt = conexao.prepareStatement(sql);

        //Seta os valores
        stmt.setString(1, leituraSensor.getSerialNumberHigh());
        stmt.setString(2, leituraSensor.getSerialNumberLow());
        stmt.setFloat(3, leituraSensor.getValorLeitura());
        stmt.setFloat(4, leituraSensor.getCanalLeitura());
        //Executa código SQL
        stmt.execute();
        stmt.close();
    }

    public List<LeituraSensor> getLista() throws SQLException{
            String sql = "select * from leiturasensor";
            PreparedStatement stmt = (PreparedStatement) this.conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<LeituraSensor> minhaLista = new ArrayList<LeituraSensor>();

            LeituraSensor leituraSensor;
            while(rs.next()){
                    leituraSensor = new LeituraSensor();

                    leituraSensor.setSerialNumberHigh(rs.getString("fk_sh"));
                    leituraSensor.setSerialNumberLow(rs.getString("fk_sl"));
                    leituraSensor.setDataLeitura(rs.getDate("datahora"));
                    leituraSensor.setValorLeitura(rs.getFloat("valorleituraS1"));
                    leituraSensor.setCanalLeitura(rs.getInt("chanel_ADC"));
                    
                    minhaLista.add(leituraSensor);
            }
            rs.close();
            stmt.close();
            return minhaLista;
    }

    public void remove (LeituraSensor leituraSensor) throws SQLException{
            String sql = "delete from leiturasensor where (fk_sh=? and fk_sl=? and datahora=?  and chanel_ADC=?)";

            PreparedStatement stmt = (PreparedStatement) conexao.prepareStatement(sql);

            stmt.setString(1, leituraSensor.getSerialNumberHigh());
            stmt.setString(2, leituraSensor.getSerialNumberLow());
            stmt.setDate(3, (Date) leituraSensor.getDataLeitura());
            stmt.setInt(4, leituraSensor.getCanalLeitura());

            stmt.execute();
            stmt.close();
            //System.out.println("OK!");
    }

    public LeituraSensor getLastInsertion() throws SQLException
    {
        String sql = "SELECT * FROM leituraSensor where (fk_SH = \"13A200\" and fk_SL = \"402d0198\" and chanel_ADC = 1) ORDER BY dataHora DESC LIMIT 0,1";
        PreparedStatement stmt = (PreparedStatement) this.conexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        LeituraSensor leituraSensor;
        leituraSensor = new LeituraSensor();
        leituraSensor.setSerialNumberHigh(rs.getString("fk_sh"));
        leituraSensor.setSerialNumberLow(rs.getString("fk_sl"));
        leituraSensor.setDataLeitura(rs.getDate("datahora"));
        leituraSensor.setValorLeitura(rs.getFloat("valorleituraS1"));
        leituraSensor.setCanalLeitura(rs.getInt("chanel_ADC"));
        rs.close();
        stmt.close();
        return leituraSensor;
    }
    
}
