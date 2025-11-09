package entidades;
import java.io.*;
import java.time.LocalDate;
import aeds3.Registro;

public class Passagem implements Registro {
    private int ID;
    private LocalDate dataViagem;
    private float preco;
    private int rota_id; 
    private int horario_id;
    private int assento_id;
    private int usuario_id;

    public Passagem() {
        this(-1, LocalDate.now(), 0.0F, -1, -1, -1, -1);
    }

    public Passagem(LocalDate dataViagem, float preco) {
        this(-1, dataViagem, preco, -1, -1, -1, -1);
    }

    public Passagem(LocalDate dataViagem, float preco, int rota_id, int horario_id, int assento_id, int usuario_id) {
        this(-1, dataViagem, preco, rota_id, horario_id, assento_id, usuario_id);
    }

    public Passagem(int ID, LocalDate dataViagem, float preco) {
        this(ID, dataViagem, preco, -1, -1, -1, -1);
    }

    public Passagem(int ID, LocalDate dataViagem, float preco, int rota_id, int horario_id, int assento_id, int usuario_id) {
        this.ID = ID;
        this.dataViagem = dataViagem;
        this.preco = preco;
        this.rota_id = rota_id;
        this.horario_id = horario_id;
        this.assento_id = assento_id;
        this.usuario_id = usuario_id;
    }

    @Override
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }
    public LocalDate getDataViagem() { return dataViagem; }
    public void setDataViagem(LocalDate dataViagem) { this.dataViagem = dataViagem; }
    public float getPreco() { return preco; }
    public void setPreco(float preco) { this.preco = preco; }
    public int getRotaId() { return rota_id; }
    public void setRotaId(int rota_id) { this.rota_id = rota_id; }
    public int getHorarioId() { return horario_id; }
    public void setHorarioId(int horario_id) { this.horario_id = horario_id; }
    public int getAssentoId() { return assento_id; }
    public void setAssentoId(int assento_id) { this.assento_id = assento_id; }
    public int getUsuarioId() { return usuario_id; } 
    public void setUsuarioId(int usuario_id) { this.usuario_id = usuario_id; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeLong(this.dataViagem.toEpochDay());
        dos.writeFloat(this.preco);
        dos.writeInt(this.rota_id);
        dos.writeInt(this.horario_id);
        dos.writeInt(this.assento_id);
        dos.writeInt(this.usuario_id); 
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.dataViagem = LocalDate.ofEpochDay(dis.readLong());
        this.preco = dis.readFloat();
        this.rota_id = dis.readInt();
        this.horario_id = dis.readInt();
        this.assento_id = dis.readInt();
        this.usuario_id = dis.readInt(); 
    }

    @Override
    public String toString() {
        return "\nID...........: " + this.ID +
               "\nData Viagem..: " + this.dataViagem +
               "\nPreço........: " + String.format("%.2f", this.preco) +
               "\nRota ID......: " + this.rota_id +
               "\nHorário ID...: " + this.horario_id +
               "\nAssento ID...: " + this.assento_id +
               "\nUsuário ID...: " + this.usuario_id; 
    }

    public int compareTo(Object b) {
        return this.getID() - ((Passagem) b).getID();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}