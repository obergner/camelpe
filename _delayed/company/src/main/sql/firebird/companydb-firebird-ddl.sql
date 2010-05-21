SET NAMES UTF8;

CONNECT 'E:\Var\Data\Firebird\PeopleDB\PEOPLE.fdb' USER 'SYSDBA' PASSWORD 'masterkey';

    --------------------------------------------------
    ---       Table: COMPANY                       ---
    --------------------------------------------------

    create table COMPANY (
        id numeric(18,0) not null,
        NAME varchar(60) not null,
        COMPANY_NUMBER varchar(30) not null,
        ID_MAIN_PHONE_NUMBER numeric(18,0) not null,
        ID_MAIN_EMAIL_ADDRESS numeric(18,0) not null,
        ID_MAIN_POSTAL_ADDRESS numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table COMPANY
        add constraint COMPANY_PK
        primary key (ID);

    alter table COMPANY
        add constraint NAME_UNIQUE
        unique (NAME);

    alter table COMPANY
        add constraint COMPANY_NUMBER_UNIQUE
        unique (COMPANY_NUMBER);

    alter table COMPANY
        add constraint ID_MAIN_PHONE_NUMBER_UNIQUE
        unique (ID_MAIN_PHONE_NUMBER);

    alter table COMPANY
        add constraint ID_MAIN_EMAIL_ADDRESS_UNIQUE
        unique (ID_MAIN_EMAIL_ADDRESS);

    alter table COMPANY
        add constraint ID_MAIN_POSTAL_ADDRESS_UNIQUE
        unique (ID_MAIN_POSTAL_ADDRESS);

    --------------------------------------------------
    ---       Table: CORPORATE_CLIENT              ---
    --------------------------------------------------

    create table CORPORATE_CLIENT (
        id numeric(18,0) not null,
        CORPORATE_CLIENT_NUMBER varchar(30) not null,
        ID_INVOICE_ADDRESS numeric(18,0) not null,
        ID_COMPANY numeric(18,0) not null,
        ID_DELIVERY_ADDRESS numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table CORPORATE_CLIENT
        add constraint CORPORATE_CLIENT_PK
        primary key (ID);

    alter table CORPORATE_CLIENT
        add constraint CORPORATE_CLIENT_NUMBER_UNIQUE
        unique (CORPORATE_CLIENT_NUMBER);

    alter table CORPORATE_CLIENT
        add constraint ID_INVOICE_ADDRESS_UNIQUE
        unique (ID_INVOICE_ADDRESS);

    alter table CORPORATE_CLIENT
        add constraint ID_COMPANY_UNIQUE
        unique (ID_COMPANY);

    alter table CORPORATE_CLIENT
        add constraint ID_DELIVERY_ADDRESS_UNIQUE
        unique (ID_DELIVERY_ADDRESS);

    --------------------------------------------------
    ---  Table: CORPORATE_CLIENT_REPRESENTATIVE    ---
    --------------------------------------------------

    create table CORPORATE_CLIENT_REPRESENTATIVE (
        id numeric(18,0) not null,
        CORPORATE_CLIENT_REP_NUMBER varchar(30) not null,
        "POSITION" varchar(20) not null,
        ID_WORK_ADDRESS numeric(18,0),
        ID_CORPORATE_CLIENT numeric(18,0),
        ID_WORK_PHONE_NUMBER numeric(18,0),
        ID_WORK_EMAIL_ADDRESS numeric(18,0) not null,
        ID_PARENT_PERSON numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table CORPORATE_CLIENT_REPRESENTATIVE
        add constraint CORP_CLIENT_REP_PK
        primary key (ID);

    alter table CORPORATE_CLIENT_REPRESENTATIVE
        add constraint CORP_CLIENT_REP_NUMBER_UNIQUE
        unique (CORPORATE_CLIENT_REP_NUMBER);

    alter table CORPORATE_CLIENT_REPRESENTATIVE
        add constraint ID_PARENT_PERSON_UNIQUE
        unique (ID_PARENT_PERSON);

    --------------------------------------------------
    ---       Table: ITEM_SUPPLIER                 ---
    --------------------------------------------------

    create table ITEM_SUPPLIER (
        id numeric(18,0) not null,
        ITEM_SUPPLIER_NUMBER varchar(30) not null,
        ID_COMPANY numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table ITEM_SUPPLIER
        add constraint ITEM_SUPPLIER_PK
        primary key (ID);

    alter table ITEM_SUPPLIER
        add constraint ITEM_SUPPLIER_NUMBER_UNIQUE
        unique (ITEM_SUPPLIER_NUMBER);

    alter table ITEM_SUPPLIER
        add constraint ID_COMPANY_UNIQUE
        unique (ID_COMPANY);

    --------------------------------------------------
    ---   Table: ITEM_SUPPLIER_REPRESENTATIVE      ---
    --------------------------------------------------

    create table ITEM_SUPPLIER_REPRESENTATIVE (
        id numeric(18,0) not null,
        ITEM_SUPPLIER_REP_NUMBER varchar(30) not null,
        "POSITION" varchar(20) not null,
        ID_WORK_EMAIL_ADDRESS numeric(18,0) not null,
        ID_ITEM_SUPPLIER numeric(18,0),
        ID_WORK_PHONE_NUMBER numeric(18,0),
        ID_WORK_ADDRESS numeric(18,0),
        ID_PARENT_PERSON numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table ITEM_SUPPLIER_REPRESENTATIVE
        add constraint ITEM_SUPPLIER_REPRESENTATIVE_PK
        primary key (ID);

    alter table ITEM_SUPPLIER_REPRESENTATIVE
        add constraint ID_PARENT_PERSON_UNIQUE
        unique (ID_PARENT_PERSON);

    alter table ITEM_SUPPLIER_REPRESENTATIVE
        add constraint ITEM_SUPPLIER_REP_NUMBER_UNIQUE
        unique (ITEM_SUPPLIER_REP_NUMBER);

    --------------------------------------------------
    ---       Foreign Key Constraints              ---
    --------------------------------------------------

    alter table COMPANY
        add constraint COMPANY_MAIN_EMAIL_FK 
        foreign key (ID_MAIN_EMAIL_ADDRESS) 
        references EMAIL_ADDRESS;

    alter table COMPANY
        add constraint COMPANY_MAIN_PHONE_FK 
        foreign key (ID_MAIN_PHONE_NUMBER) 
        references TELEPHONE_NUMBER;

    alter table COMPANY
        add constraint COMPANY_MAIN_ADDRESS_FK 
        foreign key (ID_MAIN_POSTAL_ADDRESS) 
        references POSTAL_ADDRESS;

    alter table CORPORATE_CLIENT
        add constraint CORP_CLIENT_INVOICE_ADDRESS_FK 
        foreign key (ID_INVOICE_ADDRESS) 
        references POSTAL_ADDRESS;

    alter table CORPORATE_CLIENT
        add constraint CORP_CLIENT_DELIVERY_ADDRESS_FK 
        foreign key (ID_DELIVERY_ADDRESS) 
        references POSTAL_ADDRESS;

    alter table CORPORATE_CLIENT
        add constraint CORP_CLIENT_COMPANY_FK 
        foreign key (ID_COMPANY) 
        references COMPANY;

    alter table CORPORATE_CLIENT_REPRESENTATIVE
        add constraint CORP_CLIENT_REP_WORK_EMAIL_FK 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references EMAIL_ADDRESS;

    alter table CORPORATE_CLIENT_REPRESENTATIVE
        add constraint CORP_CLIENT_REP_CORP_CLIENT_FK 
        foreign key (ID_CORPORATE_CLIENT) 
        references CORPORATE_CLIENT
        on delete cascade;

    alter table CORPORATE_CLIENT_REPRESENTATIVE
        add constraint CORP_CLIENT_REP_PERSON_FK
        foreign key (ID_PARENT_PERSON) 
        references PERSON;

    alter table CORPORATE_CLIENT_REPRESENTATIVE
        add constraint CORP_CLIENT_REP_WORK_PHONE_FK 
        foreign key (ID_WORK_PHONE_NUMBER) 
        references TELEPHONE_NUMBER;

    alter table CORPORATE_CLIENT_REPRESENTATIVE
        add constraint CORP_CLIENT_REP_WORK_ADDRESS_FK 
        foreign key (ID_WORK_ADDRESS) 
        references POSTAL_ADDRESS;

    alter table ITEM_SUPPLIER
        add constraint ITEM_SUPPLIER_COMPANY_FK 
        foreign key (ID_COMPANY) 
        references COMPANY;

    alter table ITEM_SUPPLIER_REPRESENTATIVE
        add constraint ITEM_SUPPLIER_REP_WORK_EMAIL_FK 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references EMAIL_ADDRESS;

    alter table ITEM_SUPPLIER_REPRESENTATIVE
        add constraint ITEM_SUPPLIER_REP_PERSON_FK
        foreign key (ID_PARENT_PERSON) 
        references PERSON;

    alter table ITEM_SUPPLIER_REPRESENTATIVE
        add constraint ITEM_SUPPLIER_REP_SUPPLIER_FK
        foreign key (ID_ITEM_SUPPLIER) 
        references ITEM_SUPPLIER
        on delete cascade;

    alter table ITEM_SUPPLIER_REPRESENTATIVE
        add constraint ITEM_SUPPLIER_REP_ADDRESS_FK
        foreign key (ID_WORK_ADDRESS) 
        references POSTAL_ADDRESS;

    alter table ITEM_SUPPLIER_REPRESENTATIVE
        add constraint ITEM_SUPPLIER_REP_PHONE_FK
        foreign key (ID_WORK_PHONE_NUMBER) 
        references TELEPHONE_NUMBER;

    --------------------------------------------------
    ---       Sequences                            ---
    --------------------------------------------------

    create generator ID_SEQ_COMPANY;

    create generator ID_SEQ_CORPORATE_CLIENT;

    create generator ID_SEQ_CORP_CLIENT_REP;

    create generator ID_SEQ_ITEM_SUPPLIER;

    create generator ID_SEQ_ITEM_SUPPLIER_REP;

