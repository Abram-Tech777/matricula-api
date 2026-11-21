# language: es
Característica: Consulta de cursos
  Como alumno de CIBERTEC
  Quiero consultar los cursos disponibles
  Para decidir en cuales me matriculo


  Escenario: Consultar un cursos por codigo
    Dado que Ana consulta la API de matricula
    Cuando busca el curso "PS5511"
    Entonces el codigo de respuesta es 200
    Y el curso se llama "Pruebas de Software"
    Y la respuesta cumple el esquema "curso.json"

  Escenario: Consultar un curso que no existe
    Dado que Ana consulta la API de matricula
    Cuando busca el curso "XX9999"
    Entonces el codigo de respuesta es 404
    Y el codigo de error es "CURSO_NO_ENCONTRADO"

  Esquema del escenario: Filtrar cursos por modalidad
    Dado que Ana consulta la API de matricula
    Cuando lista los cursos con modalidad "<modalidad>"
    Entonces el codigo de respuesta es 200
    Y obtiene <cantidad> cursos
    Ejemplos:
      | modalidad  | cantidad |
      | PRESENCIAL | 5        |
      | VIRTUAL    | 4        |
      | HIBRIDO    | 2        |
