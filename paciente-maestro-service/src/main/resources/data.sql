INSERT INTO pacientes (id_paciente_regional, cedula, nombres, apellidos, sede_origen, fecha_nacimiento, sexo, direccion, telefono)
VALUES
('REG-0001', '0102030405', 'Maria Fernanda', 'Cordero Vega', 'SOLCA Cuenca', '1984-05-12', 'Femenino', 'Av. Loja y Remigio Crespo', '0991112223'),
('REG-0002', '1304050607', 'Jose Antonio', 'Mendoza Vera', 'SOLCA Manabi', '1975-09-21', 'Masculino', 'Calle 12 de Marzo, Portoviejo', '0982223334'),
('REG-0003', '1712345678', 'Carla Estefania', 'Paredes Molina', 'SOLCA Quito', '1991-02-03', 'Femenino', 'Av. Eloy Alfaro, Quito', '0973334445')
ON CONFLICT (id_paciente_regional) DO NOTHING;
