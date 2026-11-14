package com.archivex.movil.util;

import java.util.regex.Pattern;

/**
 * Reglas de validación de los formularios. Son las mismas del escritorio
 * (controlador.Register_control) para que ambos clientes acepten los mismos datos.
 */
public final class Validador {

    /** Longitud mínima de la contraseña, igual que en Register_control. */
    public static final int LONGITUD_MINIMA_CONTRASENA = 4;

    private static final Pattern CORREO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern IPV4 = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$");
    private static final Pattern NOMBRE_HOST = Pattern.compile(
            "^(?=.{1,253}$)([a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)(\\.[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
    private static final Pattern TELEFONO = Pattern.compile("^\\+?[0-9 ]{7,15}$");

    private Validador() {
    }

    public static boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean esCorreoValido(String correo) {
        return !estaVacio(correo) && CORREO.matcher(correo.trim()).matches();
    }

    public static boolean esContrasenaValida(String contrasena) {
        return contrasena != null && contrasena.length() >= LONGITUD_MINIMA_CONTRASENA;
    }

    /** El teléfono es opcional; si se escribe, debe tener entre 7 y 15 dígitos. */
    public static boolean esTelefonoValido(String telefono) {
        return estaVacio(telefono) || TELEFONO.matcher(telefono.trim()).matches();
    }

    /** Entero mayor o igual que cero (cantidades, capacidades). */
    public static boolean esEnteroNoNegativo(String texto) {
        Integer valor = aEntero(texto);
        return valor != null && valor >= 0;
    }

    /** Entero mayor que cero. */
    public static boolean esEnteroPositivo(String texto) {
        Integer valor = aEntero(texto);
        return valor != null && valor > 0;
    }

    /** Número decimal mayor o igual que cero (precios). Acepta punto o coma decimal. */
    public static boolean esDecimalNoNegativo(String texto) {
        Double valor = aDecimal(texto);
        return valor != null && valor >= 0;
    }

    /** Dirección IPv4 o nombre de equipo válido para el servidor. */
    public static boolean esHostValido(String host) {
        if (estaVacio(host)) {
            return false;
        }
        String limpio = host.trim();
        if (limpio.matches("[0-9.]+")) {
            return IPV4.matcher(limpio).matches();
        }
        return NOMBRE_HOST.matcher(limpio).matches();
    }

    public static Integer aEntero(String texto) {
        if (estaVacio(texto)) {
            return null;
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double aDecimal(String texto) {
        if (estaVacio(texto)) {
            return null;
        }
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
