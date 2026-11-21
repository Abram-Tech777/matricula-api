# language: es
Característica: Seguridad de la sesión
  Como alumno de CIBERTEC
  Quiero que mi sesión esté protegida
  Para evitar accesos indebidos

  Antecedentes:
    Dado que Luis consulta la API de matricula

  Escenario: Rotación del refresh token
    Cuando Luis inicia sesion como "i202610002" con clave "Cibertec2026"
    Y renueva tokens con el refresh guardado
    Entonces el codigo de respuesta es 200
    Y la respuesta cumple el esquema "token.json"
    Cuando renueva tokens con el refresh original
    Entonces el codigo de respuesta es 401
    Y el error es "REFRESH_REUTILIZADO"
    Y la respuesta cumple el esquema "error.json"
    Cuando consulta el perfil con el token nuevo
    Entonces el codigo de respuesta es 401
    Y el error es "TOKEN_INVALIDO"
    Y la respuesta cumple el esquema "error.json"

  Escenario: Cierre de sesión
    Cuando Luis inicia sesion como "i202610002" con clave "Cibertec2026"
    Y cierra la sesion actual
    Entonces el codigo de respuesta es 204
    Cuando consulta el perfil con el token actual
    Entonces el codigo de respuesta es 401
    Y el error es "TOKEN_INVALIDO"
    Y la respuesta cumple el esquema "error.json"

  Esquema del escenario: Bloqueo de cuenta por intentos fallidos
    Dado que Jorge consulta la API de matricula
    Cuando Jorge intenta iniciar sesion como "i202610004" con clave "ClaveIncorrecta1"
    Entonces el codigo de respuesta es <status>
    Y el error es "<codigo>"
    Y la respuesta cumple el esquema "error.json"
    Ejemplos:
      | status | codigo                 |
      | 401    | CREDENCIALES_INVALIDAS |
      | 401    | CREDENCIALES_INVALIDAS |
      | 401    | CREDENCIALES_INVALIDAS |
      | 401    | CREDENCIALES_INVALIDAS |
      | 423    | CUENTA_BLOQUEADA       |

  Escenario: Clave correcta después del bloqueo
    Dado que Jorge consulta la API de matricula
    Cuando Jorge intenta iniciar sesion como "i202610004" con clave "Cibertec2026"
    Entonces el codigo de respuesta es 423
    Y el error es "CUENTA_BLOQUEADA"
    Y la respuesta cumple el esquema "error.json"

  Escenario: Alumno sin permiso de administrador
    Cuando Luis inicia sesion como "i202610002" con clave "Cibertec2026"
    Y intenta listar todos los alumnos
    Entonces el codigo de respuesta es 403
    Y el error es "ACCESO_DENEGADO"
    Y la respuesta cumple el esquema "error.json"
