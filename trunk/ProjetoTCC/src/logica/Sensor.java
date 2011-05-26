/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logica;

/**
 *
 * @author Leandro Honorato
 */
public class Sensor {
    private String serialNumberHigh;
    private String serialNumberLow;
    private String nome;
    private String localizacao;

    public Sensor(String serialNumberHigh, String serialNumberLow) {
        this.serialNumberHigh = serialNumberHigh;
        this.serialNumberLow = serialNumberLow;
    }

    public Sensor() {
    }

    public Sensor(String serialNumberHigh, String serialNumberLow, String nome, String localizacao) {
        this.serialNumberHigh = serialNumberHigh;
        this.serialNumberLow = serialNumberLow;
        this.nome = nome;
        this.localizacao = localizacao;
    }

    public Sensor(String serialNumberHigh, String serialNumberLow, String nome) {
        this.serialNumberHigh = serialNumberHigh;
        this.serialNumberLow = serialNumberLow;
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
