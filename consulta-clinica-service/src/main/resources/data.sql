INSERT INTO consultas_clinicas (id_paciente_regional, sede, fecha_consulta, especialidad, diagnostico, medico_tratante, observaciones)
SELECT 'REG-0001', 'SOLCA Cuenca', '2026-06-10', 'Oncologia Clinica', 'Control de neoplasia mamaria', 'Dra. Ana Cardenas', 'Paciente estable, continua protocolo.'
WHERE NOT EXISTS (SELECT 1 FROM consultas_clinicas WHERE id_paciente_regional = 'REG-0001' AND fecha_consulta = '2026-06-10');

INSERT INTO consultas_clinicas (id_paciente_regional, sede, fecha_consulta, especialidad, diagnostico, medico_tratante, observaciones)
SELECT 'REG-0002', 'SOLCA Manabi', '2026-06-11', 'Cirugia Oncologica', 'Evaluacion prequirurgica', 'Dr. Luis Zambrano', 'Se solicita laboratorio preoperatorio.'
WHERE NOT EXISTS (SELECT 1 FROM consultas_clinicas WHERE id_paciente_regional = 'REG-0002' AND fecha_consulta = '2026-06-11');

INSERT INTO consultas_clinicas (id_paciente_regional, sede, fecha_consulta, especialidad, diagnostico, medico_tratante, observaciones)
SELECT 'REG-0003', 'SOLCA Quito', '2026-06-12', 'Hematologia', 'Seguimiento de linfoma', 'Dra. Paulina Rivas', 'Pendiente imagen de control.'
WHERE NOT EXISTS (SELECT 1 FROM consultas_clinicas WHERE id_paciente_regional = 'REG-0003' AND fecha_consulta = '2026-06-12');
