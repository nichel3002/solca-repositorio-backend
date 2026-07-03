from pathlib import Path

from docx import Document
from docx.enum.section import WD_ORIENT, WD_SECTION
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_CELL_VERTICAL_ALIGNMENT
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.shared import Inches, Pt, RGBColor
from docx.oxml import OxmlElement
from docx.oxml.ns import qn


BASE = Path(__file__).resolve().parent
OUT = BASE / "documentacion_avance_solca_repositorio_clinico.docx"
IMAGES = [
    Path(r"C:\Users\nupia\Downloads\ChatGPT Image 3 jul 2026, 15_14_28.png"),
    Path(r"C:\Users\nupia\Downloads\ChatGPT Image 3 jul 2026, 15_13_41 (1).png"),
    Path(r"C:\Users\nupia\Downloads\ChatGPT Image 3 jul 2026, 15_14_40.png"),
]

BLUE = RGBColor(31, 77, 120)
MID_BLUE = RGBColor(46, 116, 181)
DARK = RGBColor(24, 36, 48)
GRAY_FILL = "F2F4F7"
BLUE_FILL = "E8EEF5"


def set_cell_shading(cell, fill):
    tc_pr = cell._tc.get_or_add_tcPr()
    shd = tc_pr.find(qn("w:shd"))
    if shd is None:
        shd = OxmlElement("w:shd")
        tc_pr.append(shd)
    shd.set(qn("w:fill"), fill)


def set_cell_width(cell, width_dxa):
    tc_pr = cell._tc.get_or_add_tcPr()
    tc_w = tc_pr.find(qn("w:tcW"))
    if tc_w is None:
        tc_w = OxmlElement("w:tcW")
        tc_pr.append(tc_w)
    tc_w.set(qn("w:w"), str(width_dxa))
    tc_w.set(qn("w:type"), "dxa")


def set_table_borders(table, color="D9E2F3"):
    tbl_pr = table._tbl.tblPr
    borders = tbl_pr.find(qn("w:tblBorders"))
    if borders is None:
        borders = OxmlElement("w:tblBorders")
        tbl_pr.append(borders)
    for name in ("top", "left", "bottom", "right", "insideH", "insideV"):
        border = borders.find(qn(f"w:{name}"))
        if border is None:
            border = OxmlElement(f"w:{name}")
            borders.append(border)
        border.set(qn("w:val"), "single")
        border.set(qn("w:sz"), "6")
        border.set(qn("w:space"), "0")
        border.set(qn("w:color"), color)


def set_table_width(table, width_dxa=9360, indent_dxa=120):
    tbl_pr = table._tbl.tblPr
    tbl_w = tbl_pr.find(qn("w:tblW"))
    if tbl_w is None:
        tbl_w = OxmlElement("w:tblW")
        tbl_pr.append(tbl_w)
    tbl_w.set(qn("w:w"), str(width_dxa))
    tbl_w.set(qn("w:type"), "dxa")
    tbl_ind = tbl_pr.find(qn("w:tblInd"))
    if tbl_ind is None:
        tbl_ind = OxmlElement("w:tblInd")
        tbl_pr.append(tbl_ind)
    tbl_ind.set(qn("w:w"), str(indent_dxa))
    tbl_ind.set(qn("w:type"), "dxa")


def set_cell_text(cell, text, bold=False, color=None):
    cell.text = ""
    p = cell.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    run = p.add_run(text)
    run.bold = bold
    run.font.size = Pt(10)
    if color:
        run.font.color.rgb = color
    cell.vertical_alignment = WD_CELL_VERTICAL_ALIGNMENT.CENTER


def add_table(doc, headers, rows, widths=None):
    table = doc.add_table(rows=1, cols=len(headers))
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = False
    set_table_width(table)
    set_table_borders(table)
    for idx, header in enumerate(headers):
        cell = table.rows[0].cells[idx]
        set_cell_shading(cell, GRAY_FILL)
        set_cell_text(cell, header, bold=True, color=BLUE)
        if widths:
            set_cell_width(cell, widths[idx])
    for row in rows:
        cells = table.add_row().cells
        for idx, value in enumerate(row):
            set_cell_text(cells[idx], str(value))
            if widths:
                set_cell_width(cells[idx], widths[idx])
    doc.add_paragraph()
    return table


def add_bullets(doc, items):
    for item in items:
        p = doc.add_paragraph(style="List Bullet")
        p.add_run(item)


def add_numbered(doc, items):
    for item in items:
        p = doc.add_paragraph(style="List Number")
        p.add_run(item)


def add_callout(doc, title, body):
    table = doc.add_table(rows=1, cols=1)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_width(table)
    set_table_borders(table, "B7C9E2")
    cell = table.cell(0, 0)
    set_cell_shading(cell, BLUE_FILL)
    p = cell.paragraphs[0]
    p.paragraph_format.space_after = Pt(4)
    r = p.add_run(title)
    r.bold = True
    r.font.color.rgb = BLUE
    r.font.size = Pt(11)
    p2 = cell.add_paragraph(body)
    p2.paragraph_format.space_after = Pt(0)
    doc.add_paragraph()


def style_document(doc):
    sec = doc.sections[0]
    sec.top_margin = Inches(1)
    sec.bottom_margin = Inches(1)
    sec.left_margin = Inches(1)
    sec.right_margin = Inches(1)
    sec.header_distance = Inches(0.492)
    sec.footer_distance = Inches(0.492)

    styles = doc.styles
    normal = styles["Normal"]
    normal.font.name = "Calibri"
    normal.font.size = Pt(11)
    normal.font.color.rgb = DARK
    normal.paragraph_format.space_after = Pt(6)
    normal.paragraph_format.line_spacing = 1.10

    title = styles["Title"]
    title.font.name = "Calibri"
    title.font.size = Pt(20)
    title.font.bold = True
    title.font.color.rgb = BLUE
    title.paragraph_format.space_after = Pt(8)

    for name, size, color, before, after in [
        ("Heading 1", 16, MID_BLUE, 16, 8),
        ("Heading 2", 13, MID_BLUE, 12, 6),
        ("Heading 3", 12, BLUE, 8, 4),
    ]:
        st = styles[name]
        st.font.name = "Calibri"
        st.font.size = Pt(size)
        st.font.bold = True
        st.font.color.rgb = color
        st.paragraph_format.space_before = Pt(before)
        st.paragraph_format.space_after = Pt(after)

    for list_name in ("List Bullet", "List Number"):
        st = styles[list_name]
        st.font.name = "Calibri"
        st.font.size = Pt(11)
        st.paragraph_format.space_after = Pt(8)
        st.paragraph_format.line_spacing = 1.167

    footer = sec.footer.paragraphs[0]
    footer.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    run = footer.add_run("Proyecto Reto SOLCA - Repositorio Clinico Regional")
    run.font.size = Pt(9)
    run.font.color.rgb = RGBColor(90, 100, 110)


def add_cover(doc):
    title = doc.add_paragraph(style="Title")
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    title.add_run("Documentacion del Proyecto Reto").bold = True
    subtitle = doc.add_paragraph()
    subtitle.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r = subtitle.add_run("Repositorio Clinico Regional SOLCA")
    r.font.size = Pt(16)
    r.font.color.rgb = BLUE
    r.bold = True
    meta = doc.add_paragraph()
    meta.alignment = WD_ALIGN_PARAGRAPH.CENTER
    meta.add_run("Avance 1 - Diseno inicial y microservicios base\n").bold = True
    meta.add_run("Avance 2 - Repositorio Clinico Regional e integracion\n")
    meta.add_run("Sedes consideradas: SOLCA Cuenca, SOLCA Manabi y SOLCA Quito")
    doc.add_paragraph()
    add_callout(
        doc,
        "Resumen ejecutivo",
        "El proyecto plantea una arquitectura basada en microservicios Spring Boot y frontend Angular no standalone para integrar informacion clinica regional. Cada microservicio mantiene su propia base PostgreSQL y el Repositorio Clinico Regional consolida datos mediante APIs REST protegidas con JWT, sin acceso directo entre bases de datos."
    )
    add_table(
        doc,
        ["Elemento", "Estado"],
        [
            ["Backend", "Cinco microservicios Spring Boot independientes"],
            ["Frontend", "Angular modular con login JWT"],
            ["Base de datos", "PostgreSQL con bases independientes por dominio"],
            ["Integracion", "REST entre servicios y respuesta JSON consolidada"],
            ["Evidencia", "GitHub, Docker, Postman, pgAdmin y capturas sugeridas"],
        ],
        [2200, 7160],
    )
    doc.add_page_break()


def add_design_sections(doc):
    doc.add_heading("1. Analisis del problema actual", level=1)
    doc.add_paragraph(
        "SOLCA Cuenca, Manabi y Quito atienden pacientes oncologicos que pueden requerir continuidad clinica entre sedes. El problema principal es la fragmentacion de informacion: datos demograficos, consultas, resultados de laboratorio e imagenologia pueden quedar distribuidos en sistemas locales, dificultando una vision regional del paciente."
    )
    add_bullets(doc, [
        "Riesgo de duplicidad de registros de paciente cuando cada sede administra su propia informacion.",
        "Demoras en la consulta de antecedentes clinicos, resultados de laboratorio e informes de imagenologia.",
        "Necesidad de integrar informacion estructurada y no estructurada sin mezclar directamente las bases de datos.",
        "Requerimiento de trazabilidad, seguridad y control de acceso para informacion clinica sensible.",
        "Necesidad de una arquitectura escalable que pueda operar en nube y permitir despliegues independientes.",
    ])

    doc.add_heading("2. Diagrama preliminar de arquitectura cloud", level=1)
    add_callout(
        doc,
        "Vista conceptual",
        "La arquitectura se organiza en una aplicacion frontend Angular, cinco microservicios Spring Boot, bases PostgreSQL independientes y un repositorio regional que actua como agregador REST. Cada sede conserva su sistema local, mientras el repositorio regional consulta los dominios clinicos mediante APIs."
    )
    add_table(
        doc,
        ["Capa", "Componentes", "Funcion"],
        [
            ["Presentacion", "Angular no standalone", "Permite login JWT y consulta de historia regional."],
            ["Servicios", "Paciente, Consulta, Laboratorio, Imagenologia, Repositorio", "Expone APIs REST por dominio clinico."],
            ["Datos", "PostgreSQL por microservicio", "Mantiene independencia de esquemas y evita acoplamiento directo."],
            ["Seguridad", "JWT", "Protege endpoints y permite autorizacion por token."],
            ["Operacion", "Docker / Railway", "Facilita despliegue por servicio y escalabilidad."],
        ],
        [1500, 3100, 4760],
    )

    doc.add_heading("3. Modelo del Paciente Maestro Regional", level=1)
    doc.add_paragraph(
        "El Paciente Maestro Regional unifica la identidad minima del paciente para que las sedes puedan relacionar consultas, examenes e imagenes con un mismo identificador regional."
    )
    add_table(
        doc,
        ["Campo", "Descripcion"],
        [
            ["idPacienteRegional", "Identificador regional unico, por ejemplo REG-0001."],
            ["cedula", "Documento de identidad del paciente."],
            ["nombres / apellidos", "Datos personales principales."],
            ["sedeOrigen", "SOLCA Cuenca, SOLCA Manabi o SOLCA Quito."],
            ["fechaNacimiento / sexo", "Datos demograficos."],
            ["direccion / telefono", "Datos de contacto."],
        ],
        [2500, 6860],
    )

    doc.add_heading("4. Bases de datos independientes", level=1)
    add_table(
        doc,
        ["Microservicio", "Base de datos", "Tabla principal"],
        [
            ["Paciente Maestro", "paciente_maestro_db", "pacientes"],
            ["Consulta Clinica", "consulta_clinica_db", "consultas_clinicas"],
            ["Laboratorio Clinico", "laboratorio_clinico_db", "resultados_laboratorio"],
            ["Imagenologia / PACS", "imagenologia_db", "estudios_imagenologia"],
            ["Repositorio Regional", "repositorio_clinico_regional_db", "registro_consultas_repositorio"],
        ],
        [2700, 3300, 3360],
    )

    doc.add_heading("5. Componentes cloud identificados", level=1)
    add_table(
        doc,
        ["Categoria", "Aplicacion en el proyecto"],
        [
            ["IaaS", "Maquinas virtuales o contenedores para ejecutar servicios backend y base de datos si se requiere control de infraestructura."],
            ["PaaS", "Railway o plataforma similar para desplegar microservicios sin administrar servidores directamente."],
            ["SaaS", "GitHub para repositorios, Docker Hub para imagenes y Postman para pruebas documentadas."],
            ["DBaaS", "PostgreSQL administrado por la plataforma cloud para cada base independiente."],
            ["Storage cloud", "Almacenamiento de imagenes DICOM/PACS o documentos clinicos no estructurados."],
        ],
        [1800, 7560],
    )


def add_implementation_sections(doc):
    doc.add_heading("6. Implementacion del Avance 1", level=1)
    add_table(
        doc,
        ["Microservicio", "Puerto", "Endpoints minimos"],
        [
            ["Paciente Maestro Regional", "8081", "GET /pacientes, GET /pacientes/{id}, POST /pacientes"],
            ["Consulta Clinica", "8082", "GET /consultas, GET /consultas/paciente/{id}, POST /consultas"],
            ["Laboratorio Clinico", "8083", "GET /laboratorio, GET /laboratorio/paciente/{id}, POST /laboratorio"],
            ["Imagenologia / PACS", "8084", "GET /imagenes, GET /imagenes/paciente/{id}, POST /imagenes"],
            ["Repositorio Clinico Regional", "8085", "GET /repositorio/paciente/{id}, GET /repositorio/registros"],
        ],
        [2700, 900, 5760],
    )
    add_callout(
        doc,
        "Autenticacion",
        "Todos los endpoints principales estan protegidos con JWT. Para las pruebas se utiliza POST /auth/login con usuario admin y clave admin123. El token se envia como Authorization: Bearer TOKEN."
    )

    doc.add_heading("7. Implementacion del Avance 2", level=1)
    doc.add_paragraph(
        "El Repositorio Clinico Regional consume los microservicios de Paciente Maestro, Consulta Clinica, Laboratorio e Imagenologia mediante REST. La respuesta consolidada cumple el contrato solicitado."
    )
    add_table(
        doc,
        ["Endpoint obligatorio", "Respuesta esperada"],
        [["GET /repositorio/paciente/{idPacienteRegional}", '{"paciente": {}, "consultas": [], "laboratorio": [], "imagenes": []}']],
        [3900, 5460],
    )
    add_bullets(doc, [
        "Las llamadas entre servicios se realizan por HTTP REST.",
        "El repositorio regional no consulta directamente las bases de datos de otros microservicios.",
        "Si un servicio no responde, el repositorio devuelve la seccion vacia y registra el error en el objeto errores.",
        "La prueba principal se realiza con REG-0001, REG-0002 y REG-0003.",
    ])

    doc.add_heading("8. Infraestructura de datos", level=1)
    add_table(
        doc,
        ["Tipo", "Uso"],
        [
            ["Datos estructurados", "Pacientes, consultas, resultados de laboratorio, metadatos de imagenes y registros de consulta."],
            ["Datos no estructurados", "Imagenes diagnosticas, informes radiologicos extendidos, documentos clinicos y archivos PACS."],
            ["DBaaS", "PostgreSQL administrado para cada microservicio."],
            ["Storage cloud", "Repositorio para imagenes o documentos clinicos asociados a PACS."],
        ],
        [2400, 6960],
    )

    doc.add_page_break()
    doc.add_heading("9. Matriz preliminar de riesgos", level=1)
    add_table(
        doc,
        ["Riesgo", "Impacto", "Mitigacion"],
        [
            ["Microservicio no disponible", "Respuesta parcial del repositorio", "Timeouts, manejo de errores y monitoreo."],
            ["Duplicidad de pacientes", "Historia clinica fragmentada", "Paciente Maestro Regional y validacion por identificador."],
            ["Acceso no autorizado", "Exposicion de informacion sensible", "JWT, HTTPS en despliegue y control de roles futuro."],
            ["Falla de base de datos", "Perdida de disponibilidad del dominio", "Backups, DBaaS y replicas."],
            ["Archivos de imagen muy pesados", "Lentitud en consulta de PACS", "Storage cloud y metadatos en base estructurada."],
            ["Configuracion incorrecta entre servicios", "Errores de integracion REST", "Variables de entorno y pruebas Postman por endpoint."],
        ],
        [2600, 2600, 4160],
    )


def add_evidence(doc):
    doc.add_heading("10. Evidencia tecnica sugerida", level=1)
    add_table(
        doc,
        ["Evidencia", "Detalle"],
        [
            ["Repositorio backend", "https://github.com/nichel3002/solca-repositorio-backend.git"],
            ["Repositorio frontend", "https://github.com/nichel3002/solca-repositorio-frontend.git"],
            ["Docker Hub", "Imagenes publicadas como nichel030/solca-*-service:latest"],
            ["Postman", "Coleccion SOLCA-Repositorio-Clinico-Regional.postman_collection.json"],
            ["pgAdmin", "Host 127.0.0.1, puerto 5432, usuario solca, clave solca"],
            ["Frontend local", "http://127.0.0.1:4200"],
        ],
        [2600, 6760],
    )
    add_numbered(doc, [
        "Capturar POST /auth/login y guardar el token JWT.",
        "Capturar GET /pacientes y GET /pacientes/REG-0001.",
        "Capturar GET /consultas/paciente/REG-0001.",
        "Capturar GET /laboratorio/paciente/REG-0001.",
        "Capturar GET /imagenes/paciente/REG-0001.",
        "Capturar GET /repositorio/paciente/REG-0001 con respuesta consolidada.",
        "Capturar en pgAdmin las tablas principales de cada base independiente.",
    ])


def add_process_annex(doc):
    doc.add_section(WD_SECTION.NEW_PAGE)
    sec = doc.sections[-1]
    sec.orientation = WD_ORIENT.LANDSCAPE
    sec.page_width = Inches(11)
    sec.page_height = Inches(8.5)
    sec.left_margin = Inches(0.55)
    sec.right_margin = Inches(0.55)
    sec.top_margin = Inches(0.5)
    sec.bottom_margin = Inches(0.5)

    doc.add_heading("Anexo A. Diagramas de proceso institucional", level=1)
    doc.add_paragraph(
        "Los siguientes diagramas se utilizan como soporte metodologico para evidenciar el analisis de procesos hospitalarios relacionados con Imagenologia/PACS. El caso de adquisicion e implementacion de un tomografo permite vincular necesidades clinicas, departamentos participantes, riesgos operativos y habilitacion del servicio."
    )
    captions = [
        "Figura 1. SIPOC de adquisicion e implementacion de tomografo hospitalario.",
        "Figura 2. Mapa de procesos estrategicos, misionales y de apoyo para la adquisicion del tomografo.",
        "Figura 3. Flujo detallado del proceso de adquisicion e implementacion del tomografo.",
    ]
    notes = [
        "Este SIPOC identifica proveedores, entradas, proceso, salidas y clientes. Sirve para justificar que Imagenologia no es un servicio aislado, sino un flujo institucional que requiere integracion tecnica, financiera, normativa y clinica.",
        "El mapa de procesos diferencia actividades estrategicas, misionales y de apoyo. Para el proyecto, refuerza la necesidad de un repositorio regional que permita consultar informacion clinica generada por servicios como Imagenologia.",
        "El flujo detalla aprobacion, compras, infraestructura, instalacion, pruebas, capacitacion y puesta en marcha. Sus puntos de decision ayudan a identificar riesgos de integracion, retrasos y fallas de configuracion.",
    ]
    add_table(
        doc,
        ["Diagrama", "Uso en la documentacion"],
        [
            ["SIPOC", notes[0]],
            ["Mapa de procesos", notes[1]],
            ["Flujo detallado", notes[2]],
        ],
        [2100, 7260],
    )
    for idx, image in enumerate(IMAGES):
        doc.add_section(WD_SECTION.NEW_PAGE)
        sec = doc.sections[-1]
        sec.orientation = WD_ORIENT.LANDSCAPE
        sec.page_width = Inches(11)
        sec.page_height = Inches(8.5)
        sec.left_margin = Inches(0.55)
        sec.right_margin = Inches(0.55)
        sec.top_margin = Inches(0.5)
        sec.bottom_margin = Inches(0.5)
        doc.add_paragraph(captions[idx]).runs[0].bold = True
        if image.exists():
            p = doc.add_paragraph()
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p.add_run().add_picture(str(image), width=Inches(9.05))
        else:
            doc.add_paragraph(f"No se encontro la imagen: {image}")


def build():
    doc = Document()
    style_document(doc)
    add_cover(doc)
    add_design_sections(doc)
    add_implementation_sections(doc)
    add_evidence(doc)
    add_process_annex(doc)
    doc.save(OUT)
    print(OUT)


if __name__ == "__main__":
    build()
