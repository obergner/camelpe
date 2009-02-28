
    create table COMPANY.COMPANY (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        NAME varchar(60) not null unique,
        COMPANY_NUMBER varchar(30) not null unique,
        ID_MAIN_PHONE_NUMBER bigint not null unique,
        ID_MAIN_EMAIL_ADDRESS bigint not null unique,
        ID_MAIN_POSTAL_ADDRESS bigint not null unique,
        primary key (id)
    );

    create table COMPANY.CORPORATE_CLIENT (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        CORPORATE_CLIENT_NUMBER varchar(30) not null unique,
        ID_INVOICE_ADDRESS bigint not null unique,
        ID_COMPANY bigint not null unique,
        ID_DELIVERY_ADDRESS bigint not null unique,
        primary key (id)
    );

    create table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        CORPORATE_CLIENT_REP_NUMBER varchar(30) not null unique,
        POSITION varchar(20) not null,
        ID_WORK_ADDRESS bigint,
        ID_CORPORATE_CLIENT bigint,
        ID_WORK_PHONE_NUMBER bigint,
        ID_WORK_EMAIL_ADDRESS bigint not null,
        ID_PARENT_PERSON bigint not null unique,
        primary key (id)
    );

    create table COMPANY.ITEM_SUPPLIER (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        ITEM_SUPPLIER_NUMBER varchar(30) not null unique,
        ID_COMPANY bigint not null unique,
        primary key (id)
    );

    create table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        ITEM_SUPPLIER_REP_NUMBER varchar(30) not null unique,
        POSITION varchar(20) not null,
        ID_WORK_EMAIL_ADDRESS bigint not null,
        ID_ITEM_SUPPLIER bigint,
        ID_WORK_PHONE_NUMBER bigint,
        ID_WORK_ADDRESS bigint,
        ID_PARENT_PERSON bigint not null unique,
        primary key (id)
    );

    create table PEOPLE.CONTACT_CHANNEL (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        NAME varchar(30) not null,
        COMMENT varchar(200),
        primary key (id)
    );

    create table PEOPLE.EMAIL_ADDRESS (
        ID_CONTACT_CHANNEL bigint not null,
        EMAIL_ADDRESS varchar(70) not null unique,
        primary key (ID_CONTACT_CHANNEL),
        unique (EMAIL_ADDRESS)
    );

    create table PEOPLE.EMPLOYEE (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        EMPLOYEE_NUMBER varchar(30) not null unique,
        DEPARTMENT varchar(30) not null,
        ID_WORK_EMAIL_ADDRESS bigint not null,
        ID_PERSON bigint not null unique,
        primary key (id)
    );

    create table PEOPLE.PERSON (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        FIRST_NAME varchar(40) not null,
        MIDDLE_NAMES varchar(60),
        DATE_OF_BIRTH date not null check (DATE_OF_BIRTH < current_date),
        SALUTATION varchar(10) not null,
        TITLE varchar(30),
        GENDER varchar(10) not null,
        ID_PRIVATE_MOBILE_PHONE bigint,
        ID_PRIVATE_RESIDENTIAL_PHONE bigint,
        ID_HOME_ADDRESS bigint not null,
        ID_PRIVATE_FAX bigint,
        primary key (id)
    );

    create table PEOPLE.PERSON_ACCOUNT (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        USERNAME varchar(20) not null unique,
        PASSWORD varchar(30) not null,
        PASSWORD_LAST_CHANGED_ON timestamp not null,
        ACCOUNT_EXPIRES_ON date,
        SECRET_QUESTION varchar(200),
        SECRET_ANSWER varchar(100),
        ID_PERSON bigint not null unique,
        primary key (id)
    );

    create table PEOPLE.POSTAL_ADDRESS (
        ID_CONTACT_CHANNEL bigint not null,
        STREET varchar(50) not null,
        STREET_NUMBER varchar(10) not null,
        POSTAL_CODE varchar(10) not null,
        CITY varchar(40) not null,
        primary key (ID_CONTACT_CHANNEL),
        unique (STREET, STREET_NUMBER, POSTAL_CODE)
    );

    create table PEOPLE.PRIVATE_CUSTOMER (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        CUSTOMER_NUMBER varchar(30) not null unique,
        ID_INVOICE_ADDRESS bigint,
        ID_SHIPPING_ADDRESS bigint,
        ID_PERSON bigint not null unique,
        primary key (id)
    );

    create table PEOPLE.TELEPHONE_NUMBER (
        ID_CONTACT_CHANNEL bigint not null,
        PHONE_TYPE varchar(20) not null,
        PHONE_NUMBER varchar(30) not null unique,
        primary key (ID_CONTACT_CHANNEL),
        unique (PHONE_NUMBER)
    );

    alter table COMPANY.COMPANY 
        add constraint COMPANY_MAIN_EMAIL_FK 
        foreign key (ID_MAIN_EMAIL_ADDRESS) 
        references PEOPLE.EMAIL_ADDRESS;

    alter table COMPANY.COMPANY 
        add constraint COMPANY_MAIN_PHONE_FK 
        foreign key (ID_MAIN_PHONE_NUMBER) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table COMPANY.COMPANY 
        add constraint COMPANY_MAIN_ADDRESS_FK 
        foreign key (ID_MAIN_POSTAL_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table COMPANY.CORPORATE_CLIENT 
        add constraint CORP_CLIENT_INVOICE_ADDRESS_FK 
        foreign key (ID_INVOICE_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table COMPANY.CORPORATE_CLIENT 
        add constraint CORP_CLIENT_DELIVERY_ADDRESS_FK 
        foreign key (ID_DELIVERY_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table COMPANY.CORPORATE_CLIENT 
        add constraint CORP_CLIENT_COMPANY_FK 
        foreign key (ID_COMPANY) 
        references COMPANY.COMPANY;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_WORK_EMAIL_FK 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references PEOPLE.EMAIL_ADDRESS;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_CORP_CLIENT_FK 
        foreign key (ID_CORPORATE_CLIENT) 
        references COMPANY.CORPORATE_CLIENT 
        on delete cascade;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_PARENT_PERSON_FK 
        foreign key (ID_PARENT_PERSON) 
        references PEOPLE.PERSON;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_WORK_PHONE_FK 
        foreign key (ID_WORK_PHONE_NUMBER) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_WORK_ADDRESS_FK 
        foreign key (ID_WORK_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table COMPANY.ITEM_SUPPLIER 
        add constraint ITEM_SUPPLIER_COMPANY_FK 
        foreign key (ID_COMPANY) 
        references COMPANY.COMPANY;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_WORK_EMAIL_FK 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references PEOPLE.EMAIL_ADDRESS;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_PARENT_PERSON_FK 
        foreign key (ID_PARENT_PERSON) 
        references PEOPLE.PERSON;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_ITEM_SUPPLIER_FK 
        foreign key (ID_ITEM_SUPPLIER) 
        references COMPANY.ITEM_SUPPLIER 
        on delete cascade;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_WORK_ADDRESS_FK 
        foreign key (ID_WORK_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_WORK_PHONE_FK 
        foreign key (ID_WORK_PHONE_NUMBER) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table PEOPLE.EMAIL_ADDRESS 
        add constraint FK8805E611919EFB9C 
        foreign key (ID_CONTACT_CHANNEL) 
        references PEOPLE.CONTACT_CHANNEL;

    alter table PEOPLE.EMPLOYEE 
        add constraint FK75C8D6AED513DC58 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references PEOPLE.EMAIL_ADDRESS;

    alter table PEOPLE.EMPLOYEE 
        add constraint FK75C8D6AE767DCC5C 
        foreign key (ID_PERSON) 
        references PEOPLE.PERSON;

    alter table PEOPLE.PERSON 
        add constraint FK8C768F551C08D4A8 
        foreign key (ID_HOME_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table PEOPLE.PERSON 
        add constraint FK8C768F55DA78BC4F 
        foreign key (ID_PRIVATE_MOBILE_PHONE) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table PEOPLE.PERSON 
        add constraint FK8C768F553F40B171 
        foreign key (ID_PRIVATE_RESIDENTIAL_PHONE) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table PEOPLE.PERSON 
        add constraint FK8C768F5544D4A01B 
        foreign key (ID_PRIVATE_FAX) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table PEOPLE.PERSON_ACCOUNT 
        add constraint FK9C5F2723767DCC5C 
        foreign key (ID_PERSON) 
        references PEOPLE.PERSON;

    alter table PEOPLE.POSTAL_ADDRESS 
        add constraint FK74AC460919EFB9C 
        foreign key (ID_CONTACT_CHANNEL) 
        references PEOPLE.CONTACT_CHANNEL;

    alter table PEOPLE.PRIVATE_CUSTOMER 
        add constraint FKCD3DCF3A46A78C77 
        foreign key (ID_SHIPPING_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table PEOPLE.PRIVATE_CUSTOMER 
        add constraint FKCD3DCF3A616B012E 
        foreign key (ID_INVOICE_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table PEOPLE.PRIVATE_CUSTOMER 
        add constraint FKCD3DCF3A767DCC5C 
        foreign key (ID_PERSON) 
        references PEOPLE.PERSON;

    alter table PEOPLE.TELEPHONE_NUMBER 
        add constraint FK958FA004919EFB9C 
        foreign key (ID_CONTACT_CHANNEL) 
        references PEOPLE.CONTACT_CHANNEL;

    create sequence PEOPLE.ID_SEQ_ACCOUNT;

    create sequence PEOPLE.ID_SEQ_COMPANY;

    create sequence PEOPLE.ID_SEQ_CONTACT_CHANNEL;

    create sequence PEOPLE.ID_SEQ_CORPORATE_CLIENT;

    create sequence PEOPLE.ID_SEQ_CORP_CLIENT_REP;

    create sequence PEOPLE.ID_SEQ_EMPLOYEE;

    create sequence PEOPLE.ID_SEQ_ITEM_SUPPLIER;

    create sequence PEOPLE.ID_SEQ_ITEM_SUPPLIER_REP;

    create sequence PEOPLE.ID_SEQ_PEOPLE;

    create sequence PEOPLE.ID_SEQ_PRIVATE_CUSTOMER;
