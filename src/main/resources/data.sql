insert into curso (codigo, nombre, ciclo, creditos, vacantes, modalidad, dia, hora_inicio, hora_fin) values
('PS5511', 'Pruebas de Software', 6, 3, 30, 'PRESENCIAL', 'LUNES', '08:00', '11:00'),
('DM5512', 'Desarrollo de Aplicaciones Móviles', 6, 4, 25, 'HIBRIDO', 'MARTES', '08:00', '12:00'),
('IA5513', 'Inteligencia Artificial Aplicada', 6, 3, 1, 'VIRTUAL', 'MIERCOLES', '08:00', '11:00'),
('TS5514', 'Taller de Software', 6, 4, 30, 'HIBRIDO', 'JUEVES', '08:00', '12:00'),
('QA5517', 'Gestión de Calidad de Software', 6, 3, 30, 'VIRTUAL', 'LUNES', '09:00', '12:00'),
('PI5620', 'Proyecto Integrador', 6, 6, 20, 'PRESENCIAL', 'VIERNES', '08:00', '14:00'),
('SA5516', 'Seguridad de Aplicaciones', 5, 4, 30, 'VIRTUAL', 'LUNES', '14:00', '18:00'),
('BD5410', 'Base de Datos Avanzado', 5, 4, 30, 'PRESENCIAL', 'MARTES', '14:00', '18:00');

insert into alumno (codigo, nombres, apellidos, email, promedio, tiene_deuda) values
('i202610001', 'Ana', 'Torres', 'ana.torres@cibertec.edu.pe', 18.0, false),
('i202610002', 'Luis', 'Ramos', 'luis.ramos@cibertec.edu.pe', 16.0, false),
('i202610003', 'Carla', 'Quispe', 'carla.quispe@cibertec.edu.pe', 12.0, false),
('i202610004', 'Jorge', 'Huamán', 'jorge.huaman@cibertec.edu.pe', 14.0, true),
('i202610005', 'Rosa', 'Medina', 'rosa.medina@cibertec.edu.pe', 15.5, false);

insert into matricula (alumno_id, curso_id, monto, estado, fecha_registro)
select a.id, c.id, 360.0, 'REGISTRADA', current_timestamp from alumno a, curso c where a.codigo = 'i202610003' and c.codigo = 'IA5513';
insert into matricula (alumno_id, curso_id, monto, estado, fecha_registro, fecha_pago)
select a.id, c.id, 648.0, 'PAGADA', current_timestamp, current_timestamp from alumno a, curso c where a.codigo = 'i202610005' and c.codigo = 'PI5620';
insert into matricula (alumno_id, curso_id, monto, estado, fecha_registro)
select a.id, c.id, 324.0, 'REGISTRADA', current_timestamp from alumno a, curso c where a.codigo = 'i202610005' and c.codigo = 'PS5511';
insert into matricula (alumno_id, curso_id, monto, estado, fecha_registro)
select a.id, c.id, 432.0, 'REGISTRADA', current_timestamp from alumno a, curso c where a.codigo = 'i202610005' and c.codigo = 'DM5512';
insert into matricula (alumno_id, curso_id, monto, estado, fecha_registro)
select a.id, c.id, 432.0, 'REGISTRADA', current_timestamp from alumno a, curso c where a.codigo = 'i202610005' and c.codigo = 'SA5516';
insert into matricula (alumno_id, curso_id, monto, estado, fecha_registro)
select a.id, c.id, 432.0, 'REGISTRADA', current_timestamp from alumno a, curso c where a.codigo = 'i202610005' and c.codigo = 'BD5410';
insert into matricula (alumno_id, curso_id, monto, estado, fecha_registro)
select a.id, c.id, 324.0, 'REGISTRADA', current_timestamp from alumno a, curso c where a.codigo = 'i202610002' and c.codigo = 'PS5511';
insert into matricula (alumno_id, curso_id, monto, estado, fecha_registro)
select a.id, c.id, 384.0, 'ANULADA', current_timestamp from alumno a, curso c where a.codigo = 'i202610001' and c.codigo = 'DM5512';
