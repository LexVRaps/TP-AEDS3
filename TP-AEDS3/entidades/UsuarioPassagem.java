package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import aeds3.Registro;

public class UsuarioPassagem implements Registro {
    private int ID;
    private int usuarioId; 
    private int passagemId;

    public UsuarioPassagem() {
        this(-1, -1, -1);
    }

    public UsuarioPassagem(int usuarioId, int passagemId) {
        this(-1, usuarioId, passagemId);
    }

    public UsuarioPassagem(int ID, int usuarioId, int passagemId) {
        this.ID = ID;
        this.usuarioId = usuarioId;
        this.passagemId = passagemId;
    }

    @Override
    public int getID() { 
        return ID; 
    }

    @Override
    public void setID(int ID) { 
        this.ID = ID; 
    }

    public int getUsuarioId() { 
        return usuarioId; 
    }

    public void setUsuarioId(int usuarioId) { 
        this.usuarioId = usuarioId; 
    }

    public int getPassagemId() { 
        return passagemId; 
    }

    public void setPassagemId(int passagemId) { 
        this.passagemId = passagemId; 
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeInt(this.usuarioId);
        dos.writeInt(this.passagemId);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.usuarioId = dis.readInt();
        this.passagemId = dis.readInt();
    }

    @Override
    public String toString() {
        return "\n--- Associação Usuario-Passagem ---" +
               "\nID da Associação: " + this.ID +
               "\nID do Usuário...: " + this.usuarioId +
               "\nID da Passagem..: " + this.passagemId;
    }

    @Override
    public int compareTo(Object b) {
        UsuarioPassagem outro = (UsuarioPassagem) b;
        if (this.usuarioId != outro.usuarioId) {
            return this.usuarioId - outro.usuarioId;
        }
        return this.passagemId - outro.passagemId;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UsuarioPassagem outro = (UsuarioPassagem) obj;
        return this.usuarioId == outro.usuarioId && 
               this.passagemId == outro.passagemId;
    }

    @Override
    public int hashCode() {
        return 31 * usuarioId + passagemId;
    }
}
