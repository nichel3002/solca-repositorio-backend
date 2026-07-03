INSERT INTO resultados_laboratorio (id_paciente_regional, sede, fecha_resultado, tipo_examen, resultado, unidad, rango_referencia)
SELECT 'REG-0001', 'SOLCA Cuenca', '2026-06-10', 'Hemoglobina', '12.8', 'g/dL', '12.0 - 16.0'
WHERE NOT EXISTS (SELECT 1 FROM resultados_laboratorio WHERE id_paciente_regional = 'REG-0001' AND fecha_resultado = '2026-06-10' AND tipo_examen = 'Hemoglobina');

INSERT INTO resultados_laboratorio (id_paciente_regional, sede, fecha_resultado, tipo_examen, resultado, unidad, rango_referencia)
SELECT 'REG-0002', 'SOLCA Manabi', '2026-06-11', 'Leucocitos', '6.4', '10^3/uL', '4.0 - 10.0'
WHERE NOT EXISTS (SELECT 1 FROM resultados_laboratorio WHERE id_paciente_regional = 'REG-0002' AND fecha_resultado = '2026-06-11' AND tipo_examen = 'Leucocitos');

INSERT INTO resultados_laboratorio (id_paciente_regional, sede, fecha_resultado, tipo_examen, resultado, unidad, rango_referencia)
SELECT 'REG-0003', 'SOLCA Quito', '2026-06-12', 'Plaquetas', '240', '10^3/uL', '150 - 450'
WHERE NOT EXISTS (SELECT 1 FROM resultados_laboratorio WHERE id_paciente_regional = 'REG-0003' AND fecha_resultado = '2026-06-12' AND tipo_examen = 'Plaquetas');
