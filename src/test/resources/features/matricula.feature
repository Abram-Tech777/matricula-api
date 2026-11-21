# language: es
Característica: Matrícula del alumno
  Como alumno de CIBERTEC
  Quiero cotizar, matricular y pagar mis cursos
  Para llevarlos en el ciclo

  Antecedentes:
    Dado que Luis consulta la API de matricula
    Cuando Luis inicia sesion como "i202610002" con clave "Cibertec2026"

  Escenario: Cotizar el curso de la fila
    Cuando cotiza el curso "BD5410" del alumno "i202610002"
    Entonces el codigo de respuesta es 200
    Y la cotizacion muestra 4 creditos, subtotal 480.0, descuento 10 y total 432.0
    Y la respuesta cumple el esquema "cotizacion.json"

  Escenario: Matricularse y pagar el curso de la fila
    Cuando se matricula en el curso "BD5410" con el alumno "i202610002"
    Entonces el codigo de respuesta es 201
    Y el estado de la matricula es "REGISTRADA"
    Y el monto de la matricula es 432.0
    Y la respuesta cumple el esquema "matricula.json"
    Cuando paga la matricula con "YAPE"
    Entonces el codigo de respuesta es 200
    Y el estado de la matricula es "PAGADA"
    Y la matricula tiene fecha de pago
    Cuando consulta los pagos de "i202610002" con metodo "YAPE"
    Entonces el codigo de respuesta es 200
    Y existe el pago de la matricula recordada con el metodo "YAPE"

  Escenario: Regla de negocio que debe fallar
    Cuando consulta la constancia de "i202610003"
    Entonces el codigo de respuesta es 403
    Y el error es "ACCESO_DENEGADO"
    Y la respuesta cumple el esquema "error.json"
