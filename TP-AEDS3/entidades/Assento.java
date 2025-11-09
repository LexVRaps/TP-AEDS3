package entidades;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aeds3.Registro;

public class Assento implements Registro {
    private int ID;
    private String numeroAssento;
    private boolean disponivel;

    public Assento() {
        this(-1, "", true);
    }

    public Assento(String numeroAssento, boolean disponivel) {
        this(-1, numeroAssento, disponivel);
    }

    public Assento(int ID, String numeroAssento, boolean disponivel) {
        this.ID = ID;
        this.numeroAssento = numeroAssento;
        this.disponivel = disponivel;
    }

    @Override
    public int getID() { return ID; }
    @Override
    public void setID(int ID) { this.ID = ID; }
    public String getNumeroAssento() { return numeroAssento; }
    public void setNumeroAssento(String numeroAssento) { this.numeroAssento = numeroAssento; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeUTF(this.numeroAssento);
        dos.writeBoolean(this.disponivel);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.numeroAssento = dis.readUTF();
        this.disponivel = dis.readBoolean();
    }

    @Override
    public String toString() {
        return "\nID.............: " + this.ID +
               "\nNúmero Assento.: " + this.numeroAssento +
               "\nDisponível.....: " + (disponivel ? "Sim" : "Não");
    }
    public int compareTo(Object b) {
    return this.getID() - ((Assento) b).getID();
  }

    @Override
    public Object clone() throws CloneNotSupportedException {
    return super.clone();
    }
}