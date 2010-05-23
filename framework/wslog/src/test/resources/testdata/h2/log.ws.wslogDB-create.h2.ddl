
	create schema if not exists LOG authorization sa;
	
        create table LOG.WEBSERVICE_HEADER (
        ID bigint not null,
        NAME varchar(100) not null,
        VALUE varchar(200),
        ID_WEBSERVICE_REQUEST bigint not null,
        primary key (ID)
    );

    create table LOG.WEBSERVICE_OPERATION (
        ID integer not null,
        IDEMPOTENT boolean not null,
        LOGGED boolean not null,
        NAME varchar(200) not null,
        SINCE date not null,
        primary key (ID)
    );

    create table LOG.WEBSERVICE_REQUEST (
        ID bigint not null,
        CONTENT clob not null,
        RECEIVED_ON timestamp not null,
        SOURCE_IP varchar(15) not null,
        ID_WEBSERVICE_OPERATION integer not null,
        primary key (ID)
    );

    create table LOG.WEBSERVICE_RESPONSE (
        ID bigint not null,
        CONTENT clob not null,
        SENT_ON timestamp not null,
        ID_WEBSERVICE_REQUEST bigint,
        primary key (ID)
    );

    alter table LOG.WEBSERVICE_HEADER 
        add constraint FK_WSHEADER_WSREQUEST 
        foreign key (ID_WEBSERVICE_REQUEST) 
        references LOG.WEBSERVICE_REQUEST;

    alter table LOG.WEBSERVICE_REQUEST 
        add constraint FK_WSREQUEST_WSOPERATION 
        foreign key (ID_WEBSERVICE_OPERATION) 
        references LOG.WEBSERVICE_OPERATION;

    alter table LOG.WEBSERVICE_RESPONSE 
        add constraint FK_WSRESPONSE_WSREQUEST 
        foreign key (ID_WEBSERVICE_REQUEST) 
        references LOG.WEBSERVICE_REQUEST;

    create sequence LOG.ID_SEQ_WEBSERVICE_HEADER;

    create sequence LOG.ID_SEQ_WEBSERVICE_OPERATION;

    create sequence LOG.ID_SEQ_WEBSERVICE_REQUEST;

    create sequence LOG.ID_SEQ_WEBSERVICE_RESPONSE;
