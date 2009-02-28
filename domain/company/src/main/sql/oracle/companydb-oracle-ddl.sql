
    create table COMPANY.COMPANY (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        NAME varchar2(60 char) not null unique,
        COMPANY_NUMBER varchar2(30 char) not null unique,
        ID_MAIN_PHONE_NUMBER number(19,0) not null unique,
        ID_MAIN_EMAIL_ADDRESS number(19,0) not null unique,
        ID_MAIN_POSTAL_ADDRESS number(19,0) not null unique,
        primary key (id)
    );

    create table COMPANY.CORPORATE_CLIENT (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        CORPORATE_CLIENT_NUMBER varchar2(30 char) not null unique,
        ID_INVOICE_ADDRESS number(19,0) not null unique,
        ID_COMPANY number(19,0) not null unique,
        ID_DELIVERY_ADDRESS number(19,0) not null unique,
        primary key (id)
    );

    create table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        CORPORATE_CLIENT_REP_NUMBER varchar2(30 char) not null unique,
        POSITION varchar2(20 char) not null,
        ID_WORK_ADDRESS number(19,0),
        ID_CORPORATE_CLIENT number(19,0),
        ID_WORK_PHONE_NUMBER number(19,0),
        ID_WORK_EMAIL_ADDRESS number(19,0) not null,
        ID_PARENT_PERSON number(19,0) not null unique,
        primary key (id)
    );

    create table COMPANY.ITEM_SUPPLIER (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        ITEM_SUPPLIER_NUMBER varchar2(30 char) not null unique,
        ID_COMPANY number(19,0) not null unique,
        primary key (id)
    );

    create table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        ITEM_SUPPLIER_REP_NUMBER varchar2(30 char) not null unique,
        POSITION varchar2(20 char) not null,
        ID_WORK_EMAIL_ADDRESS number(19,0) not null,
        ID_ITEM_SUPPLIER number(19,0),
        ID_WORK_PHONE_NUMBER number(19,0),
        ID_WORK_ADDRESS number(19,0),
        ID_PARENT_PERSON number(19,0) not null unique,
        primary key (id)
    );

    create table PEOPLE.CONTACT_CHANNEL (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        NAME varchar2(30 char) not null,
        COMMENT varchar2(200 char),
        primary key (id)
    );

    create table PEOPLE.EMAIL_ADDRESS (
        ID_CONTACT_CHANNEL number(19,0) not null,
        EMAIL_ADDRESS varchar2(70 char) not null unique,
        primary key (ID_CONTACT_CHANNEL),
        unique (EMAIL_ADDRESS)
    );

    create table PEOPLE.EMPLOYEE (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        EMPLOYEE_NUMBER varchar2(30 char) not null unique,
        DEPARTMENT varchar2(30 char) not null,
        ID_WORK_EMAIL_ADDRESS number(19,0) not null,
        ID_PERSON number(19,0) not null unique,
        primary key (id)
    );

    create table PEOPLE.PERSON (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        FIRST_NAME varchar2(40 char) not null,
        MIDDLE_NAMES varchar2(60 char),
        DATE_OF_BIRTH date not null check (DATE_OF_BIRTH < current_date),
        SALUTATION varchar2(10 char) not null,
        TITLE varchar2(30 char),
        GENDER varchar2(10 char) not null,
        ID_PRIVATE_MOBILE_PHONE number(19,0),
        ID_PRIVATE_RESIDENTIAL_PHONE number(19,0),
        ID_HOME_ADDRESS number(19,0) not null,
        ID_PRIVATE_FAX number(19,0),
        primary key (id)
    );

    create table PEOPLE.PERSON_ACCOUNT (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        USERNAME varchar2(20 char) not null unique,
        PASSWORD varchar2(30 char) not null,
        PASSWORD_LAST_CHANGED_ON timestamp not null,
        ACCOUNT_EXPIRES_ON date,
        SECRET_QUESTION varchar2(200 char),
        SECRET_ANSWER varchar2(100 char),
        ID_PERSON number(19,0) not null unique,
        primary key (id)
    );

    create table PEOPLE.POSTAL_ADDRESS (
        ID_CONTACT_CHANNEL number(19,0) not null,
        STREET varchar2(50 char) not null,
        STREET_NUMBER varchar2(10 char) not null,
        POSTAL_CODE varchar2(10 char) not null,
        CITY varchar2(40 char) not null,
        primary key (ID_CONTACT_CHANNEL),
        unique (STREET, STREET_NUMBER, POSTAL_CODE)
    );

    create table PEOPLE.PRIVATE_CUSTOMER (
        id number(19,0) not null,
        META_VERSION number(10,0),
        AUDIT_CREATED_BY varchar2(15 char) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar2(15 char) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        CUSTOMER_NUMBER varchar2(30 char) not null unique,
        ID_INVOICE_ADDRESS number(19,0),
        ID_SHIPPING_ADDRESS number(19,0),
        ID_PERSON number(19,0) not null unique,
        primary key (id)
    );

    create table PEOPLE.TELEPHONE_NUMBER (
        ID_CONTACT_CHANNEL number(19,0) not null,
        PHONE_TYPE varchar2(20 char) not null,
        PHONE_NUMBER varchar2(30 char) not null unique,
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
