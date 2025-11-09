package entidades;
import java.io.*;
import java.time.LocalTime;
import aeds3.Registro;

public class Horario implements Registro {
    private int ID;
    private LocalTime horaPartida;
    private LocalTime horaChegada;

    public Horario() {
        this(-1, LocalTime.MIN, LocalTime.MIN);
    }

    public Horario(LocalTime horaPartida, LocalTime horaChegada) {
        this(-1, horaPartida, horaChegada);
    }

    public Horario(int ID, LocalTime horaPartida, LocalTime horaChegada) {
        this.ID = ID;
        this.horaPartida = horaPartida;
        this.horaChegada = horaChegada;
    }

    @Override
    public int getID() { return ID; }
    @Override
    public void setID(int ID) { this.ID = ID; }
    public LocalTime getHoraPartida() { return horaPartida; }
    public void setHoraPartida(LocalTime horaPartida) { this.horaPartida = horaPartida; }
    public LocalTime getHoraChegada() { return horaChegada; }
    public void setHoraChegada(LocalTime horaChegada) { this.horaChegada = horaChegada; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeLong(this.horaPartida.toNanoOfDay());
        dos.writeLong(this.horaChegada.toNanoOfDay());
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.horaPartida = LocalTime.ofNanoOfDay(dis.readLong());
        this.horaChegada = LocalTime.ofNanoOfDay(dis.readLong());
    }

    @Override
    public String toString() {
        return "\nID...........: " + this.ID +
               "\nHora Partida.: " + this.horaPartida +
               "\nHora Chegada.: " + this.horaChegada;
    }

    public int compareTo(Object b) {
        return this.getID() - ((Horario) b).getID();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}