package Mensajes;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private String ip;
    private String url;
    private String subjectMosntruos;
    private String subjectGanador;
    private int puertoHit;
    private boolean status; //informa si se pudo hacer el lógin o ya existía un jugador con el id dado

    public LoginResponse(String url, String subjectMosntruos, String subjectGanador, int puertoHit, boolean status, String ip) {
        this.url = url;
        this.subjectMosntruos = subjectMosntruos;
        this.subjectGanador = subjectGanador;
        this.puertoHit = puertoHit;
        this.status = status;
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public String getSubjectMosntruos() {
        return subjectMosntruos;
    }

    public String getSubjectGanador() {
        return subjectGanador;
    }

    public int getPuertoHit() {
        return puertoHit;
    }

    public boolean isStatus() {
        return status;
    }

    public String getIp() {return ip;}
}
