/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comunicacaoSerial;

import dao.CanaisInstaladosDAO;
import dao.LeituraSensorDAO;
import dao.SensorDAO;
import java.security.Timestamp;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.CanalInstalado;
import logica.LeituraSensor;
import logica.Sensor;

/**
 *
 * @author Leandro Honorato
 */
public class ComunicacaoZigBee implements Runnable{
    
    private static LeituraSensorDAO leituraSensorDao;
    private static CanaisInstaladosDAO canaisInstaladosDAO;
    private static int intervaloEntrePools;
    private static int intervaloEntrePoolsPorSensor;
    private static int taxaAtualizacaoListaSensores;
    private SerialComLeituraEscrita portaSerial;

    private enum TipoPacote{
        NENHUM, OK, ERROR, DADO_LEITURA
    };

    private static TipoPacote ultimoPacoteRecebido;

    public ComunicacaoZigBee(SerialComLeituraEscrita portaSerial, int intervaloEntrePools, int intervaloEntrePoolsPorSensor,
                            int taxaAtualizacaoListaSensores) throws SQLException {
        if(leituraSensorDao == null)
        {
            leituraSensorDao = new LeituraSensorDAO();
            canaisInstaladosDAO = new CanaisInstaladosDAO();
        }
        this.portaSerial = portaSerial;
        ultimoPacoteRecebido = TipoPacote.NENHUM;
        this.intervaloEntrePools = intervaloEntrePools;
        this.intervaloEntrePoolsPorSensor = intervaloEntrePoolsPorSensor;
        this.taxaAtualizacaoListaSensores = taxaAtualizacaoListaSensores;
    }

    public void run() {
        int contador = 0;
        List<CanalInstalado> canaisInstalados = null;
        Iterator<CanalInstalado> iteratorCanaisInstalados;
        while(true)
        {
            try {
                if(contador == 0)
                {
                    canaisInstalados = canaisInstaladosDAO.getLista();
                }
                iteratorCanaisInstalados = canaisInstalados.iterator();
                while(iteratorCanaisInstalados.hasNext())
                {
                    enviarComandoLeitura(iteratorCanaisInstalados.next());
                    Thread.sleep(intervaloEntrePoolsPorSensor);//Aguarda 2s antes de fazer a próxima rodada de leitura
                }
                contador = (contador+1)%taxaAtualizacaoListaSensores;
                Thread.sleep(intervaloEntrePools);//Aguarda 50s antes de fazer a próxima rodada de leitura
            } catch (InterruptedException ex) {
                Logger.getLogger(ComunicacaoZigBee.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                System.out.println("Mais Um erro para LOG");
                Logger.getLogger(ComunicacaoZigBee.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void tratarPacote(String pacote)
    {
        System.out.println("Tratar pacote: " + pacote );
        if(leituraSensorDao != null){
            StringTokenizer tokenData = new StringTokenizer(pacote,"*");
            String[] tokensPacote = new String[6];
            int i = 0;
            while(tokenData.hasMoreTokens()){
                tokensPacote[i] = tokenData.nextToken();
                i++;
            }
           
            if(tokensPacote[0].equalsIgnoreCase("#READV")){ //Alta probabilidade de o pacote ter chegado Correto
                System.out.println("Entei no if");
                ultimoPacoteRecebido = TipoPacote.DADO_LEITURA;
                //System.out.println("Vou inserir");
                LeituraSensor leitura = new LeituraSensor(tokensPacote[1], tokensPacote[2], Integer.parseInt(tokensPacote[3]), Float.parseFloat(tokensPacote[4]), null);

                try {
                    leituraSensorDao.adiciona(leitura);
                    System.out.println("Chegou nova leitura e foi adicionada com Sucesso!");
                } catch (SQLException ex) {
                    Logger.getLogger(ComunicacaoZigBee.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Deu Erro na insersão da leitura! - Salvar num log futuramente");
                }
            }else if(tokensPacote[0].equalsIgnoreCase("OK"))
            {
                ultimoPacoteRecebido = TipoPacote.OK;
                
            }else if(tokensPacote[0].equalsIgnoreCase("ERROR"))
            {
                ultimoPacoteRecebido = TipoPacote.ERROR;
                
            }
        }
        //System.out.println(data);
    }

    private boolean enviarComandoLeitura(CanalInstalado canalInstalado) throws SQLException {
        portaSerial.HabilitarEscrita();
        portaSerial.EnviarUmaString("+++");
        boolean chegouOK = esperaComandoOK();
        if(chegouOK == false)
        {
            return false;
        }
        portaSerial.EnviarUmaString("AT DH " + canalInstalado.getSerialNumberHigh() + "\r");
        chegouOK = esperaComandoOK();
        if(chegouOK == false)
        {
            return false;
        }
        portaSerial.EnviarUmaString("AT DL " + canalInstalado.getSerialNumberLow() + "\r");
        chegouOK = esperaComandoOK();
        if(chegouOK == false)
        {
            return false;
        }
        portaSerial.EnviarUmaString("ATCN\r");
        chegouOK = esperaComandoOK();
        if(chegouOK == false)
        {
            return false;
        }
        portaSerial.EnviarUmaString("#READR*"+ canalInstalado.getCanalLeitura() + "\r");
        return true;
    }

    private boolean esperaComandoOK()
    {
        long tempoInicial = System.currentTimeMillis();
        long tempoEspera = 0;
        while((tempoEspera < 5000))
        {
            if(ultimoPacoteRecebido == TipoPacote.OK)
            {
                System.out.println("Chegou OK!");
                ultimoPacoteRecebido = TipoPacote.NENHUM;
                return true;
            }
            tempoEspera = System.currentTimeMillis() - tempoInicial;
        }
        return false;
    }
}
