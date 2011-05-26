/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import bancoDados.CriarConexao;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logica.Sensor;

/**
 *
 * @author Leandro Honorato
 */
public class SensorDAO {
    private Connection conexao;

    public SensorDAO() throws SQLException
    {
        this.conexao = CriarConexao.getConexao();
    }

    public void adiciona(Sensor sensor) throws SQLException{
		//Prepara Conexão
		String sql = "insert into sensores (sh, sl, nome, localizacao) values (?, ?, ?, ?)";
		java.sql.PreparedStatement stmt = conexao.prepareStatement(sql);

		//Seta os valores
		stmt.setString(1, sensor.getSerialNumberHigh());
		stmt.setString(2, sensor.getSerialNumberLow());
		stmt.setString(3, sensor.getNome());
		stmt.setString(4, sensor.getLocalizacao());

		//Executa código SQL
		stmt.execute();
		stmt.close();
	}

	public List<Sensor> getLista() throws SQLException{
		String sql = "select * from sensores";
		PreparedStatement stmt = (PreparedStatement) this.conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		List<Sensor> minhaLista = new ArrayList<Sensor>();

		Sensor sensor;
		while(rs.next()){
			sensor = new Sensor();
			sensor.setNome(rs.getString("nome"));
			sensor.setLocalizacao(rs.getString("localizacao"));
			sensor.setSerialNumberHigh(rs.getString("sh"));
			sensor.setSerialNumberLow(rs.getString("sl"));
			
			minhaLista.add(sensor);
		}
		rs.close();
		stmt.close();
		return minhaLista;
	}

	
	public void altera(Sensor sensor) throws SQLException{
		String sql = "update sensores set nome=?, localizacao=?, dh=?, dl=? where (sh=? and sl=?)";
		PreparedStatement stmt = (PreparedStatement) conexao.prepareStatement(sql);

		stmt.setString(1, sensor.getNome());
		stmt.setString(2, sensor.getLocalizacao());
		stmt.setString(3, sensor.getSerialNumberHigh());
		stmt.setString(4, sensor.getSerialNumberLow());
                stmt.setString(5, sensor.getSerialNumberHigh());
		stmt.setString(6, sensor.getSerialNumberLow());
		
		stmt.execute();
		stmt.close();
	}

    public void remove (Sensor sensor) throws SQLException{
            String sql = "delete from sensores where (sh=? and sl=?)";
            
            PreparedStatement stmt = (PreparedStatement) conexao.prepareStatement(sql);

            stmt.setString(1, sensor.getSerialNumberHigh());
            stmt.setString(2, sensor.getSerialNumberLow());
            
            stmt.execute();
            stmt.close();
            //System.out.println("OK!");
    }
}
