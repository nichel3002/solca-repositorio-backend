INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C00', 'Tumor maligno del labio', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C00');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C16', 'Tumor maligno del estomago', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C16');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C18', 'Tumor maligno del colon', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C18');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C20', 'Tumor maligno del recto', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C20');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C34', 'Tumor maligno de los bronquios y del pulmon', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C34');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C50', 'Tumor maligno de la mama', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C50');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C53', 'Tumor maligno del cuello del utero', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C53');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C56', 'Tumor maligno del ovario', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C56');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C61', 'Tumor maligno de la prostata', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C61');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C71', 'Tumor maligno del encefalo', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C71');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C91', 'Leucemia linfoide', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C91');

INSERT INTO cie10 (codigo, descripcion, categoria)
SELECT 'C92', 'Leucemia mieloide', 'Neoplasias'
WHERE NOT EXISTS (SELECT 1 FROM cie10 WHERE codigo = 'C92');

INSERT INTO historias_clinicas (
    id_historia_clinica, id_paciente_regional, fecha_apertura, sede_apertura, estado_historia,
    estado_civil, ocupacion, instruccion, nacionalidad, contacto_emergencia, telefono_emergencia,
    antecedentes_patologicos_personales, antecedentes_quirurgicos, antecedentes_alergicos,
    antecedentes_gineco_obstetricos, antecedentes_toxicos, medicacion_habitual,
    antecedentes_oncologicos_familiares, motivo_consulta, enfermedad_actual, fecha_inicio_sintomas,
    signos_sintomas_principales, tratamientos_previos, codigo_cie10, diagnostico_principal,
    tipo_cancer, localizacion_tumor, lateralidad, estadio_clinico, clasificacion_tnm,
    fecha_diagnostico, base_diagnostica, histologia, grado_tumoral, biomarcadores,
    peso, talla, imc, presion_arterial, frecuencia_cardiaca, frecuencia_respiratoria,
    temperatura, saturacion_oxigeno, estado_funcional_ecog, examen_fisico_general,
    examen_fisico_regional, intencion_tratamiento, plan_tratamiento, quimioterapia,
    radioterapia, fecha_inicio_tratamiento, medico_responsable, evolucion_clinica,
    respuesta_tratamiento, proxima_cita, recomendaciones, estado_actual_paciente,
    consentimiento_informado, fecha_consentimiento, creado_por, rol_creador,
    fecha_creacion, hora_creacion, actualizado_por, fecha_actualizacion, sede_registro
)
SELECT
    'HC-202607170001', 'REG-0001', '2026-07-17', 'SOLCA Cuenca', 'ACTIVA',
    'Casada', 'Docente', 'Superior', 'Ecuatoriana', 'Carlos Cordero', '0992223334',
    'Hipertension arterial controlada.', 'Cesarea previa.', 'Niega alergias medicamentosas.',
    'G2 P2 A0.', 'Niega tabaco y alcohol.', 'Losartan 50 mg cada dia.',
    'Madre con cancer de mama.', 'Apertura de historia clinica oncologica integral.',
    'Paciente con antecedente de lesion mamaria en seguimiento regional.', '2026-05-10',
    'Mastalgia intermitente y nodulo palpable.', 'Valoracion externa previa.',
    'C50', 'Tumor maligno de la mama', 'Carcinoma mamario', 'Mama', 'Izquierda',
    'IIA', 'T2N0M0', '2026-06-01', 'Biopsia', 'Carcinoma ductal infiltrante',
    'G2', 'RE positivo, RP positivo, HER2 negativo', '62 kg', '1.60 m', '24.2',
    '120/80', '78', '18', '36.5', '98%', '1',
    'Paciente consciente, orientada, hidratada.', 'Mama izquierda con nodulo palpable.',
    'Curativo', 'Comite oncologico para definir manejo multimodal.', 'Pendiente',
    'Pendiente', '2026-07-20', 'Dra. Ana Cardenas',
    'Historia clinica abierta y en seguimiento.', 'Pendiente de tratamiento inicial.',
    '2026-08-01', 'Completar estudios de extension.', 'En evaluacion',
    'SI', '2026-07-17', 'repositorio-service', 'ADMIN',
    '2026-07-17', '08:00:00', 'repositorio-service', '2026-07-17T08:00:00', 'SOLCA Cuenca'
WHERE NOT EXISTS (SELECT 1 FROM historias_clinicas WHERE id_historia_clinica = 'HC-202607170001');
