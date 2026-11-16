# Cambios en la API del escritorio (ArchiveX)
Archivos que se reemplazan/añaden en el proyecto NetBeans `archivexjava54`:
- `api/UsuarioApiServer.java`: nueva ruta `POST /usuarios/login` (compara hash SHA-256).
- `api/MovimientoApiServer.java`: nueva ruta `POST /movimientos` (registra el movimiento y actualiza el stock).
- `modelo/MovimientoInventario.java`: método `registrarMovimientoApi(...)` transaccional y sin ventanas emergentes.
- `api/ApiServidor.java`: arranca los 7 servidores (puertos 8080–8086) con un solo `main`.
