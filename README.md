# ArchiveX Móvil 2.0

Cliente Android del sistema de inventario **ArchiveX** (evidencia AA2-EV02, SENA).
Consume la API REST del proyecto de escritorio (Java + MySQL).

## Módulos
| Módulo | Paquete | Funciones |
|---|---|---|
| Autenticación | `ui.login` | Inicio de sesión (`POST /usuarios/login`), registro, IP del servidor |
| Menú | `ui.menu` | Acceso a los módulos según la sesión |
| Productos | `ui.productos` | Listar, buscar, detalle, crear, editar, eliminar |
| Catálogos | `ui.catalogos` | CRUD de categorías, almacenes y proveedores |
| Movimientos | `ui.movimientos` | Historial y registro de entradas, salidas, ajustes y devoluciones |
| Compras | `ui.compras` | Consulta de órdenes de compra |
| Usuarios | `ui.usuarios` | Consulta de usuarios |

## Arquitectura
- `modelo`: entidades y reglas de negocio.
- `data.api`: cliente HTTP (`ApiClient`, Singleton) y configuración (`ApiConfig`).
- `data.mapper`: conversión JSON ↔ modelo (Strategy `JsonMapper<T>`).
- `data.repositorio`: Repository genérico `Repositorio<T>` y fábrica `Repositorios`.
- `ui.comun`: componentes reutilizables (`ListaActivity<T>`, `ListaAdapter<T>`, `FormularioDialog`, `SelectorLista<T>`, `Dialogos`).
- `util`: validación, formato, filtro de texto y sesión.

## Requisitos
- Android Studio (AGP 8.7.3, Gradle 8.9, JDK 17+), Android 7.0 (API 24) o superior.
- Servidor: ejecutar `api.ApiServidor` del proyecto de escritorio con MySQL activo (puertos 8080–8086).
- Desde el emulador el computador es `10.0.2.2`; en un teléfono físico usar la IP LAN del computador.

## Pruebas
```
gradlew testDebugUnitTest
```
## Compilar el APK
```
gradlew assembleDebug   # app/build/outputs/apk/debug/app-debug.apk
```
