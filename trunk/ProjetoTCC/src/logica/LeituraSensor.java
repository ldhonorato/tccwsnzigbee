/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logica;

import java.util.Date;

/**
 *
 * @author Leandro Honorato
 */
public class LeituraSensor {
    private String serialNumberHigh;
    private String serialNumberLow;
    private Date dataLeitura;
    private float valorLeitura;
    private int canalLeitura;

    public LeituraSensor(String serialNumberHigh, String serialNumberLow, int canalLeitura, float valorLeitura, Date dataLeitura) {
        this.serialNumberHigh = serialNumberHigh;
        this.serialNumberLow = serialNumberLow;
        this.dataLeitura = dataLeitura;
        this.valorLeitura = valorLeitura;
        this.canalLeitura = canalLeitura;;
    }

    public LeituraSensor() {
    }

    public Date getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
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

    public float getValorLeitura() {
        return valorLeitura;
    }

    public void setValorLeitura(float valorLeitura) {
        this.valorLeitura = valorLeitura;
    }

    public int getCanalLeitura() {
        return canalLeitura;
    }

    public void setCanalLeitura(int canalLeitura) {
        this.canalLeitura = canalLeitura;
    }

    
}
