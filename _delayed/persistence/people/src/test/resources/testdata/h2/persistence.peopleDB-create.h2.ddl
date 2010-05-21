
    create schema if not exists PEOPLE authorization sa;
    
    create table PEOPLE.EMAIL_ADDRESS (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        COMMENT varchar(200),
        NAME varchar(30) not null,
        EMAIL_ADDRESS varchar(70) not null unique,
        primary key (id),
        unique (EMAIL_ADDRESS)
    );

    create table PEOPLE.EMPLOYEE (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        DEPARTMENT varchar(30) not null,
        EMPLOYEE_NUMBER varchar(30) not null unique,
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
        DATE_OF_BIRTH date not null check (DATE_OF_BIRTH < current_date),
        FIRST_NAME varchar(40) not null,
        GENDER varchar(10) not null,
        LAST_NAME varchar(40) not null,
        MIDDLE_NAMES varchar(60),
        SALUTATION varchar(10) not null,
        TITLE varchar(30),
        ID_PRIVATE_RESIDENTIAL_PHONE bigint,
        ID_HOME_ADDRESS bigint not null,
        ID_PRIVATE_MOBILE_PHONE bigint,
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
        ACCOUNT_EXPIRES_ON date,
        PASSWORD varchar(30) not null,
        PASSWORD_LAST_CHANGED_ON timestamp not null,
        SECRET_ANSWER varchar(100),
        SECRET_QUESTION varchar(200),
        USERNAME varchar(20) not null unique,
        ID_PERSON bigint not null unique,
        primary key (id)
    );

    create table PEOPLE.POSTAL_ADDRESS (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        COMMENT varchar(200),
        NAME varchar(30) not null,
        CITY varchar(40) not null,
        POSTAL_CODE varchar(10) not null,
        STREET varchar(50) not null,
        STREET_NUMBER varchar(10) not null,
        primary key (id),
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
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        COMMENT varchar(200),
        NAME varchar(30) not null,
        PHONE_NUMBER varchar(30) not null unique,
        PHONE_TYPE varchar(20) not null,
        primary key (id),
        unique (PHONE_NUMBER)
    );

    alter table PEOPLE.EMPLOYEE 
        add constraint FK_EMPLOYEE_WORKEMAILADDR 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references PEOPLE.EMAIL_ADDRESS;

    alter table PEOPLE.EMPLOYEE 
        add constraint FK_EMPLOYEE_PERSON 
        foreign key (ID_PERSON) 
        references PEOPLE.PERSON;

    alter table PEOPLE.PERSON 
        add constraint FK_PERSON_HOMEADDR 
        foreign key (ID_HOME_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table PEOPLE.PERSON 
        add constraint FK_PERSON_PRVMOBPHONE 
        foreign key (ID_PRIVATE_MOBILE_PHONE) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table PEOPLE.PERSON 
        add constraint FK_PERSON_PRVRESPHONE 
        foreign key (ID_PRIVATE_RESIDENTIAL_PHONE) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table PEOPLE.PERSON 
        add constraint FK_PERSON_PRVFAX 
        foreign key (ID_PRIVATE_FAX) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table PEOPLE.PERSON_ACCOUNT 
        add constraint FK_ACCOUNT_PERSON 
        foreign key (ID_PERSON) 
        references PEOPLE.PERSON;

    alter table PEOPLE.PRIVATE_CUSTOMER 
        add constraint FK_PRIVATECUST_SHIPPINGADDR 
        foreign key (ID_SHIPPING_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table PEOPLE.PRIVATE_CUSTOMER 
        add constraint FK_PRIVATECUST_INVOICEADDR 
        foreign key (ID_INVOICE_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table PEOPLE.PRIVATE_CUSTOMER 
        add constraint FK_PRIVATECUST_PERSON 
        foreign key (ID_PERSON) 
        references PEOPLE.PERSON;

    create sequence PEOPLE.ID_SEQ_ACCOUNT;

    create sequence PEOPLE.ID_SEQ_CONTACT_CHANNEL;

    create sequence PEOPLE.ID_SEQ_EMPLOYEE;

    create sequence PEOPLE.ID_SEQ_PERSON;

    create sequence PEOPLE.ID_SEQ_PRIVATE_CUSTOMER;
