/*
Esta classe representa um objeto para uma entidade
que será armazenado em um Hash Extensível

Neste caso em particular, este objeto é representado
por dois números inteiros para que possa conter
relacionamentos entre dois IDs de entidades quaisquer
*/

package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIntIntHash implements RegistroHashExtensivel {

  private int chave;  // Chave primária para o hash
  private int valor;  // Valor associado
  private short TAMANHO = 8;

  public ParIntIntHash() {
    this(-1, -1);
  }

  public ParIntIntHash(int chave) {
    this(chave, -1);
  }

  public ParIntIntHash(int chave, int valor) {
    this.chave = chave;
    this.valor = valor;
  }

  public int getChave() {
    return chave;
  }

  public int getValor() {
    return valor;
  }

  public void setChave(int chave) {
    this.chave = chave;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }

  @Override
  public int hashCode() {
    return this.chave;
  }

  @Override
  public short size() {
    return this.TAMANHO;
  }

  public String toString() {
    return String.format("%3d", this.chave) + ";" + String.format("%-3d", this.valor);
  }

  @Override
  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.chave);
    dos.writeInt(this.valor);
    return baos.toByteArray();
  }

  @Override
  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.chave = dis.readInt();
    this.valor = dis.readInt();
  }
}
