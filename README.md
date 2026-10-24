# App Monitoreo de Galpones

App Android para monitorear galpones y gestionar alertas y acciones. Desarrollada en Kotlin con Jetpack Compose y Material 3 para el ramo DSY1105.

## Pasos de ejecución

1. Clonar el repositorio:
   ```
   git clone https://github.com/Chente-Git/app-monitoreo-galpones.git
   ```
2. Abrir la carpeta clonada en Android Studio (File > Open).
3. Esperar a que termine la sincronización de Gradle.
4. Crear o elegir un emulador (se probó con Pixel 8) desde Device Manager.
5. Presionar Run (el botón verde de play) para instalar y abrir la app.
6. Iniciar sesión con uno de los usuarios de prueba. El perfil elegido en la pantalla debe coincidir con el rol del usuario.

### Usuarios de prueba

| Rol | Correo | Contraseña |
|---|---|---|
| Operario | operario@galpones.cl | Operario123 |
| Supervisor | supervisor@galpones.cl | Supervisor123 |
| Jefatura | jefatura@galpones.cl | Jefatura123 |

## Estructura de carpetas

Paquete base: `com.duoc.galpones`

```
galpones
├── GalponesApp.kt
├── MainActivity.kt
├── data
│   ├── fakeapi      Datos simulados de alertas y usuarios
│   ├── local        Base de datos Room (AppDatabase, dao, entity)
│   ├── red          MonitorRed: estado de la conexión
│   └── repository   Implementaciones de los repositorios
├── di               AppContainer: inyección de dependencias manual
├── domain           Reglas de negocio y validaciones
│   ├── model        Modelos de dominio
│   └── repository   Interfaces de los repositorios
└── ui
    ├── accion       Registro de acciones
    ├── alertas      Lista de alertas
    ├── components   Componentes reutilizables
    ├── login        Inicio de sesión
    ├── navigation   AppNavHost y rutas
    ├── permisos     Manejo de permisos
    ├── resumen      Resumen de galpones
    └── theme        Colores, tipografía y tema
```

## Arquitectura

MVVM con AppContainer manual. Las reglas de validación están en `domain/` y los composables solo muestran el estado que entrega cada ViewModel.