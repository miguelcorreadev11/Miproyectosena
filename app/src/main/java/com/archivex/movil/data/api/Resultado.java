package com.archivex.movil.data.api;

/**
 * Callback que recibe la interfaz cuando termina una operación en segundo plano.
 * Ambos métodos se ejecutan en el hilo principal (patrón Observer).
 */
public interface Resultado<T> {

    void onExito(T datos);

    void onError(String mensaje);
}
