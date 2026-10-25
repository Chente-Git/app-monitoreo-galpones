# Arquitectura

## Visión general

Aplicación Android en Kotlin con Jetpack Compose. Arquitectura **MVVM** con inyección de dependencias manual mediante un `AppContainer`. La UI lee desde Room; las FakeApi solo alimentan la base cuando las tablas están vacías. Esto hace que la app funcione sin conexión con los datos ya persistidos.

## Capas

```
domain/           reglas de negocio puras, sin dependencias de Android
  model/          data class y enum
  repository/     interfaces de repositorio
  validation/     validadores de formulario con tests
  CalculadorEstado  cálculo del semáforo
  SesionActual      estado de la sesión en memoria

data/             implementación de las interfaces de domain
  fakeapi/        datos simulados con retardo
  local/          Room (entidades, DAO, AppDatabase)
  mapper/         conversión entre entidades Room y modelos de dominio
  repository/     implementaciones concretas
  red/            MonitorRed con ConnectivityManager

platform/         recursos nativos de Android
  notificaciones/ canal + NotificadorCritico
  vibracion/      Vibrador

di/               AppContainer: provee repositorios y servicios

ui/
  components/     piezas reutilizables (semáforo, campo de formulario, etc.)
  navigation/     AppNavHost y rutas
  permisos/       ManejadorPermiso + diálogo de rechazo
  theme/          Material 3
  <feature>/      <X>Screen + <X>ViewModel + <X>UiState por pantalla
```

## Flujo de datos típico

1. `GalponesApp.onCreate()` arma el `AppContainer` y lanza una siembra en background: `cargarInicialesSiVacio()` solo escribe si las tablas están vacías, protegido por un `Mutex`.
2. Cada ViewModel pide su repositorio al `contenedorApp()` (helper composable que lee el `GalponesApp` del `LocalContext`).
3. El ViewModel expone un `StateFlow<XUiState>` que la pantalla observa con `collectAsState()`.
4. Los eventos de UI llaman funciones del ViewModel. Las reglas de validación están en `domain/validation/`; el ViewModel invoca al validador y guarda los mensajes de error en el `UiState`. Los composables solo pintan ese estado.

## Regla del semáforo (CalculadorEstado)

- Medición dentro del rango y lejos de los bordes → **Normal**.
- Medición dentro del rango pero en el 10% más cercano a los extremos → **Advertencia**.
- Medición fuera del rango → **Crítico**.
- El estado del galpón es el máximo entre el estado de temperatura y el de humedad.

## Permisos y recursos nativos

- `POST_NOTIFICATIONS` (Android 13+): se pide en Inicio una vez por sesión mediante `PermisosSesion.notificacionesSolicitadas`. El rechazo permanente se detecta con `shouldShowRequestPermissionRationale`.
- `VIBRATE`: no requiere diálogo de ejecución; se usa junto con la notificación crítica.
- `ACCESS_NETWORK_STATE`: `MonitorRed` emite un `Flow<Boolean>` con la conectividad real y la UI muestra el banner "sin conexión".

## Decisiones conscientes

- **FakeApi + Room**: cumple el requisito académico y entrega lectura offline sin complicar la red real.
- **AppContainer manual**: evita arrastrar Hilt/Dagger en un proyecto de esta escala.
- **`SesionActual` como object**: simplifica el flujo para esta entrega. En una versión productiva pasaría a `SesionRepository` para poder testearlo y persistirlo.
- **Clave en texto plano**: solo en el modelo `Usuario` de la FakeApi. Marcado con TODO en `Usuario.kt`.
