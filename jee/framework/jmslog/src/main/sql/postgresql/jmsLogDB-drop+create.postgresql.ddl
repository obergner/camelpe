
    alter table LOG.JMS_HEADER 
        drop constraint FK_JMSHEADER_JMSMESSAGE;

    alter table LOG.JMS_MESSAGE 
        drop constraint FK_JMSMESSAGE_JMSMESSAGETYPE;

    drop table LOG.JMS_HEADER;

    drop table LOG.JMS_MESSAGE;

    drop table LOG.JMS_MESSAGE_TYPE;

    drop sequence LOG.ID_SEQ_JMS_HEADER;

    drop sequence LOG.ID_SEQ_JMS_MESSAGE;

    drop sequence LOG.ID_SEQ_JMS_MESSAGE_TYPE;

    create table LOG.JMS_HEADER (
        ID int8 not null,
        ID_JMS_MESSAGE int8 not null,
        NAME varchar(100) not null,
        VALUE varchar(200),
        primary key (ID)
    );

    create table LOG.JMS_MESSAGE (
        ID int8 not null,
        GUID varchar(50) not null unique,
        ID_JMSMESSAGETYPE int4 not null,
        PROCESSING_STATE varchar(15) not null,
        RECEIVED_ON timestamp not null,
        COMPLETED_ON timestamp,
        CONTENT text not null,
        ERROR_TYPE varchar(100),
        ERROR_MESSAGE varchar(1000),
        ERROR_STACKTRACE text,
        primary key (ID),
        unique (GUID)
    );

    create table LOG.JMS_MESSAGE_TYPE (
        ID int4 not null,
        IDEMPOTENT bool not null,
        LOGGED bool not null,
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
