# language: es
Característica: Calificación de matrículas
  Como docente de CIBERTEC
  Quiero registrar notas y consultar el historial
  Para reflejar el rendimiento académico

  Esquema del escenario: El docente registra la nota y el historial se actualiza
    Dado que Rosa consulta la API de matricula
    Cuando Rosa inicia sesion como "i202610005" con clave "Cibertec2026"
    Y Rosa guarda el id de su matricula pagada de "PI5620"
    Dado que D0001 consulta la API de matricula
    Cuando D0001 inicia sesion como "D0001" con clave "Cibertec2026"
    Y D0001 registra la nota <nota> en la matricula pagada de Rosa
    Entonces el codigo de respuesta es 200
    Cuando Rosa vuelve a iniciar sesion como "i202610005" con clave "Cibertec2026"
    Y Rosa consulta su historial
    Entonces el codigo de respuesta es 200
    Y la respuesta cumple el esquema "historial.json"
    Y el historial muestra la condicion "<condicion>" para "PI5620", <creditos> creditos aprobados y promedio <promedio>
    Ejemplos:
      | nota | condicion   | creditos | promedio |
      | 18   | APROBADO    | 13       | 16.62    |
      | 12.5 | DESAPROBADO | 7        | 14.08    |

  Escenario: Un docente que no dicta el curso intenta registrar la nota
    Dado que Rosa consulta la API de matricula
    Cuando Rosa inicia sesion como "i202610005" con clave "Cibertec2026"
    Y Rosa guarda el id de su matricula pagada de "PI5620"
    Dado que D0002 consulta la API de matricula
    Cuando D0002 inicia sesion como "D0002" con clave "Cibertec2026"
    Y D0002 registra la nota 15 en la matricula pagada de Rosa
    Entonces el codigo de respuesta es 403
    Y el error es "ACCESO_DENEGADO"
    Y la respuesta cumple el esquema "error.json"

  Escenario: No se puede calificar una matricula que no esta pagada
    Dado que Rosa consulta la API de matricula
    Cuando Rosa inicia sesion como "i202610005" con clave "Cibertec2026"
    Y Rosa guarda el id de su matricula pagada de "PI5620"
    Cuando Rosa guarda el id de su matricula registrada de "PS5511"
    Dado que D0001 consulta la API de matricula
    Cuando D0001 inicia sesion como "D0001" con clave "Cibertec2026"
    Y D0001 registra la nota 16 en la matricula registrada de Rosa
    Entonces el codigo de respuesta es 409
    Y el error es "MATRICULA_NO_PAGADA"
    Y la respuesta cumple el esquema "error.json"
