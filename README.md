📅 Agenda Profesional
Aplicación de escritorio para gestión de agenda y recordatorios desarrollada en Java con interfaz gráfica Swing y base de datos SQLite.

✨ Características
Sistema de autenticación - Login y registro de usuarios

Roles diferenciados - Administradores y usuarios normales

Gestión de recordatorios - Crear, ver y eliminar eventos

Calendario visual - Visualización mensual con tooltips informativos

Filtros - Visualización de recordatorios por mes

Persistencia - Almacenamiento local mediante SQLite

👥 Roles
Rol	Permisos
👑 Administrador	Crear recordatorios, ver todos, eliminar cualquier recordatorio
👤 Usuario	Ver recordatorios, eliminar cualquier recordatorio
Nota: Los usuarios normales no pueden crear recordatorios, pero pueden eliminar cualquier tarea existente (incluyendo las de administradores).

🛠️ Tecnologías
Java (Swing para interfaz gráfica)

SQLite (Base de datos embebida)

JDBC (Conexión a base de datos)

📋 Requisitos
Java 8 o superior

SQLite JDBC driver (incluido)

🚀 Instalación y ejecución
Desde el código fuente
Clonar el repositorio:

bash
git clone https://github.com/tu-usuario/agenda-profesional.git
cd agenda-profesional
Compilar el proyecto:

bash
javac -cp "lib/*" src/org/example/*.java -d out
Ejecutar la aplicación:

bash
java -cp "out;lib/*" org.example.Main
🔐 Credenciales por defecto
Tipo	Usuario	Contraseña
Administrador	admin	admin123
📁 Estructura del proyecto
text
agenda-profesional/
├── src/org/example/
│   ├── Main.java              # Punto de entrada
│   ├── LoginView.java         # Vista de login
│   ├── LoginController.java   # Controlador de login
│   ├── AgendaView.java        # Vista principal de agenda
│   ├── AgendaController.java  # Controlador de agenda
│   ├── AgendaModel.java       # Modelo de datos
│   ├── DatabaseManager.java   # Gestor de base de datos
│   ├── Usuario.java           # Entidad usuario
│   └── Recordatorio.java      # Entidad recordatorio
├── lib/                       # Dependencias (SQLite JDBC)
└── README.md
🖼️ Capturas de pantalla
Pantalla de login
Interfaz con gradiente y formulario de autenticación/registro.

Agenda principal
Calendario mensual con días destacados (azul = con tareas)

Tooltips con detalles de recordatorios

Botones de acción (Nuevo, Ver, Eliminar)

Ver recordatorios
Tabla con todos los recordatorios y filtro por mes.

🗄️ Base de datos
El sistema genera automáticamente un archivo agenda.db con dos tablas:

usuarios

id (PK)

nombre (UNIQUE)

contraseña

esAdmin (0/1)

recordatorios

id (PK)

usuario_id (FK)

fecha

hora_inicio

hora_fin

descripcion

📝 Mejoras futuras
Editar recordatorios existentes

Notificaciones y recordatorios automáticos

Exportar agenda a PDF/CSV

Modo oscuro

Categorías/colores para diferentes tipos de tareas

Recordatorios recurrentes

👨‍💻 Autor
Desarrollado como proyecto de demostración de Java Swing con arquitectura MVC.

📄 Licencia
MIT License - Libre para uso educativo y personal.
