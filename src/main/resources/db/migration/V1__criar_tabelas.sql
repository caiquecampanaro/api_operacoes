create sequence conta_sequence start 1;

create table conta (
	id int default NEXTVAL ('conta_sequence') primary key,
	saldo int,
	numero VARCHAR (20),
	digito VARCHAR (10),
	tipo_conta VARCHAR (20),
	limite_credito int
	
); 

create sequence	transferencia_sequence start 1;

create table transferencia(
	id int default nextval ('transferencia_sequence')  primary key, 
	valor int, 
	quantidade_parcelas int, 
	valor_parcela int,
	ultima_parcela int,
	operacoes VARCHAR(30),
    id_origem int,
	constraint fk_id_origem
		foreign key(id_origem) 
			references conta(id),
	id_destino int,		
	constraint fk_id_destino
		foreign key(id_destino)
			references conta(id)
);