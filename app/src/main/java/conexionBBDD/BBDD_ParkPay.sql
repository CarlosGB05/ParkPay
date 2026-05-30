create table usuarios (
	idUsuario SERIAL PRIMARY KEY,
	nombreCompleto VARCHAR(25),
	email VARCHAR(35) UNIQUE,
	telefono INT,
	dni VARCHAR(9),
	contrasena VARCHAR(20)
);

create table iconos_usuarios (
	idIcono SERIAL PRIMARY KEY,
	idUsuario_Fk INTEGER REFERENCES usuarios(idUsuario),
	nombreIcono VARCHAR(15)
);

create table reservas (
	idReserva SERIAL PRIMARY KEY,
	idUsuario_Fk INTEGER REFERENCES usuarios(idUsuario) NOT NULL,
	nombre VARCHAR(100),
	ubicacion VARCHAR(100),
	calificacion NUMERIC(5,2),
	matricula VARCHAR(10) NOT NULL,
	precioTotal NUMERIC(10,2),
	fechaReserva VARCHAR(30),
	inicioReserva VARCHAR(30),
	finalReserva VARCHAR(30),
	cocheElectrico BOOLEAN
);

alter table reservas add CONSTRAINT 
chk_horas_validas CHECK (finalReserva > inicioReserva);

create table codigos_qr (
	idCodigoQR SERIAL PRIMARY KEY,
    idReserva_FK INTEGER REFERENCES reservas(idReserva) NOT NULL,
    imagen BYTEA
);


INSERT INTO reservas (idUsuario_Fk, nombre, ubicacion, calificacion, 
matricula, precioTotal, fechaReserva, inicioReserva, finalReserva, cocheElectrico) 
values (2,'Hotel Osuna','Calle de Luis de la Mata, 18, Madrid',3.6,'0196 UDP',3.5,
'24 / 5 / 2026','04:00','05:00','FALSE')

Select * From reservas where idUsuario_Fk = 2 and fechaReserva = '24 / 5 / 2026'
and inicioReserva = '04:00' and finalReserva = '05:00';

SELECT COUNT(*) FROM usuarios WHERE email = 'email@gmail.es';

DELETE FROM codigos_qr where idReserva_FK = 10;
DELETE FROM reservas where idReserva = 10;