
    create schema LOG authorization DBA;
 
    create table LOG.JMS_HEADER (
        ID bigint not null,
        NAME varchar(100) not null,
        VALUE varchar(200),
        ID_JMS_MESSAGE bigint not null,
        primary key (ID)
    );

    create table LOG.JMS_MESSAGE (
        ID bigint not null,
        CONTENT longvarchar not null,
        GUID varchar(15) not null,
        PROCESSING_STATE varchar(36) not null,
        RECEIVED_ON timestamp not null,
        ID_JMSMESSAGETYPE integer not null,
        primary key (ID),
        unique (GUID)
    );

    create table LOG.JMS_MESSAGE_TYPE (
        ID integer not null,
        IDEMPOTENT bit not null,
        LOGGED bit not null,
        NAME varchar(200) not null,
        SINCE date not null,
        primary key (ID)
    );

    alter table LOG.JMS_HEADER 
        add constraint FK_JMSHEADER_JMSMESSAGE 
        foreign key (ID_JMS_MESSAGE) 
        references LOG.JMS_MESSAGE;

    alter table LOG.JMS_MESSAGE 
        add constraint FK_JMSMESSAGE_JMSMESSAGETYPE 
        foreign key (ID_JMSMESSAGETYPE) 
        references LOG.JMS_MESSAGE_TYPE;

    create sequence LOG.ID_SEQ_JMS_HEADER;

    create sequence LOG.ID_SEQ_JMS_MESSAGE;

    create sequence LOG.ID_SEQ_JMS_MESSAGE_TYPE;

