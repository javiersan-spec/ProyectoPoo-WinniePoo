public class Rut implements IdPersona {
    private int numero;
    private char dv;

    private Rut(int numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }

    public static Rut of(String rutConDv) {
        try {

            if (rutConDv == null || !rutConDv.contains("-")) return null;

            String[] partes = rutConDv.split("-");
            if (partes.length != 2 || partes[1].length() != 1) return null;

            // Eliminar puntos y convertir el numero
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
        if (!(otro instanceof Rut))
            return false;
        Rut r = (Rut) otro;
        return this.numero == r.numero && this.dv == r.dv;
    }
}
