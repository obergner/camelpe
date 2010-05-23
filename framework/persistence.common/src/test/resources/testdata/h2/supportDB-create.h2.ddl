
    create schema if not exists SUPPORT authorization sa;
    
    create table SUPPORT.TEST_ENTITY (
        id bigint not null,
        NAME varchar(30) not null,
        primary key (id)
    );

    create sequence SUPPORT.ID_SEQ_TEST_ENTITY start with 100;
