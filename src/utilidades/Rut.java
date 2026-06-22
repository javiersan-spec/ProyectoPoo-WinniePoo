package utilidades;

import java.io.Serializable;
/**
 * @author Javier San Martin
 * @version Avance 3
 */
public class Rut implements IdPersona, Serializable {
    private int numero;
    private char dv;

    private Rut(int num, char dv) { //ahora el constructor es privado
        this.numero = num;
        this.dv = dv;
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }

    public static Rut of(String rutConDv) {
        if (rutConDv == null || !rutConDv.contains("-")) return null;

        String[] partes = rutConDv.split("-");
        if (partes.length != 2 || partes[1].length() != 1) return null;

        try {
            String numeroStr = partes[0].replace(".", "");
            int numero = Integer.parseInt(numeroStr);
            char dv = Character.toUpperCase(partes[1].charAt(0));

            return new Rut(numero, dv);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        String numeroStr = String.valueOf(numero);
        StringBuilder sb = new StringBuilder();
        int inicio = numeroStr.length() % 3;
        if (inicio > 0) sb.append(numeroStr, 0, inicio);
        for (int i = inicio; i < numeroStr.length(); i += 3) {
            if (sb.length() > 0) sb.append(".");
            sb.append(numeroStr, i, i + 3);
        }
        return sb.toString() + "-" + dv;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || getClass() != otro.getClass()) return false;
        Rut rut = (Rut) otro;
        return numero == rut.numero && dv == rut.dv;
    }
}


