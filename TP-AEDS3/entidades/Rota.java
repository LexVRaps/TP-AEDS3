package entidades;

import java.io.*;
import aeds3.Registro;

public class Rota implements Registro {
    private int ID;
    private String origem;
    private String destino;
    private float distancia;
    
    public Rota() {
        this(-1, "", "", 0.0f);
    }

    public Rota(String origem, String destino, float distancia) {
        this(-1, origem, destino, distancia);
    }

    public Rota(int ID, String origem, String destino, float distancia) {
        this.ID = ID;
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
    }

    @Override
    public int getID() { return ID; }
    @Override
    public void setID(int ID) { this.ID = ID; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public float getDistancia() { return distancia; }
    public void setDistancia(float distancia) { this.distancia = distancia; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeUTF(this.origem);
        dos.writeUTF(this.destino);
        dos.writeFloat(this.distancia);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.origem = dis.readUTF();
        this.destino = dis.readUTF();
        this.distancia = dis.readFloat();
    }

    @Override
    public String toString() {
        return "\nID........: " + this.ID +
               "\nOrigem....: " + this.origem +
               "\nDestino...: " + this.destino +
               "\nDistância...: " + this.distancia + " km";
    }

    public int compareTo(Object b) {
    return this.getID() - ((Rota) b).getID();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
    return super.clone();
    }
}
