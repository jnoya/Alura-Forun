CREATE TABLE respostas(

	id bigint not null auto_increment,
    mensagem varchar(1000) not null ,
    topicoId long not null ,
    dataCriacao Date not null,
    usuarioId long,
    soluçao boolean,
    primary key (id)
    
    );
	