
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
        DATE_OF_BIRTH date not null,
        SALUTATION varchar(10) not null,
        TITLE varchar(30),
        GENDER varchar(10) not null,
        ID_HOME_ADDRESS bigint not null,
        ID_PRIVATE_RESIDENTIAL_PHONE bigint,
        ID_PRIVATE_FAX bigint,
        ID_PRIVATE_MOBILE_PHONE bigint,
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
        ID_PERSON bigint not null unique,
        ID_INVOICE_ADDRESS bigint,
        ID_SHIPPING_ADDRESS bigint,
        primary key (id)
    );

    create table PEOPLE.TELEPHONE_NUMBER (
        ID_CONTACT_CHANNEL bigint not null,
        PHONE_TYPE varchar(20) not null,
        PHONE_NUMBER varchar(30) not null unique,
        primary key (ID_CONTACT_CHANNEL),
        unique (PHONE_NUMBER)
    );

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

    create sequence ID_SEQ_ACCOUNT;

    create sequence ID_SEQ_CONTACT_CHANNEL;
    
    create sequence PEOPLE.ID_SEQ_EMPLOYEE;

    create sequence ID_SEQ_PEOPLE;

    create sequence ID_SEQ_PRIVATE_CUSTOMER;
