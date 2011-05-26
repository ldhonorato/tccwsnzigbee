/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logica;

/**
 *
 * @author Leandro Honorato
 */
public class CanalInstalado {
    private String serialNumberHigh;
    private String serialNumberLow;
    private int canalLeitura;

    public CanalInstalado() {
    }

    public CanalInstalado(String serialNumberHigh, String serialNumberLow, int canalLeitura) {
        this.serialNumberHigh = serialNumberHigh;
        this.serialNumberLow = serialNumberLow;
        this.canalLeitura = canalLeitura;
    }

    public int getCanalLeitura() {
        return canalLeitura;
    }

    public void setCanalLeitura(int canalLeitura) {
        this.canalLeitura = canalLeitura;
    }

    public String getSerialNumberHigh() {
        return serialNumberHigh;
    }

    public void setSerialNumberHigh(String serialNumberHigh) {
        this.serialNumberHigh = serialNumberHigh;
    }

    public String getSerialNumberLow() {
        return serialNumberLow;
    }

    public void setSerialNumberLow(String serialNumberLow) {
        this.serialNumberLow = serialNumberLow;
    }

    
}
