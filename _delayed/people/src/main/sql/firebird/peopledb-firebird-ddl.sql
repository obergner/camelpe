    SET NAMES UTF8;

    CONNECT 'E:\Var\Data\Firebird\PeopleDB\People.fdb' USER 'SYSDBA' PASSWORD 'masterkey';

    --------------------------------------------------
    ---       Table: CONTACT_CHANNEL               ---
    --------------------------------------------------
    create table CONTACT_CHANNEL (
        ID numeric(18,0) not null,
        NAME varchar(30) not null,
        "COMMENT" varchar(200),
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        primary key (ID)
    );

    --------------------------------------------------
    ---       Table: EMAIL_ADDRESS                 ---
    --------------------------------------------------

    create table EMAIL_ADDRESS (
        ID_CONTACT_CHANNEL numeric(18,0) not null,
        EMAIL_ADDRESS varchar(70) not null,
        primary key (ID_CONTACT_CHANNEL)
    );

    alter table EMAIL_ADDRESS
    add constraint EMAIL_ADDRESS_ADDR_UNIQUE
    unique (EMAIL_ADDRESS);

    --------------------------------------------------
    ---       Table: PERSON                        ---
    --------------------------------------------------

    create table PERSON (
        ID numeric(18,0) not null,
        FIRST_NAME varchar(40) not null,
        MIDDLE_NAMES varchar(60),
        DATE_OF_BIRTH date not null,
        SALUTATION varchar(10) not null,
        TITLE varchar(30),
        GENDER varchar(10) not null,
        ID_HOME_ADDRESS numeric(18,0) not null,
        ID_PRIVATE_RESIDENTIAL_PHONE numeric(18,0),
        ID_PRIVATE_FAX numeric(18,0),
        ID_PRIVATE_MOBILE_PHONE numeric(18,0),
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        primary key (ID)
    );

    --------------------------------------------------
    ---       Table: PERSON_ACCOUNT                ---
    --------------------------------------------------

    create table PERSON_ACCOUNT (
        ID numeric(18,0) not null,
        USERNAME varchar(20) not null,
        "PASSWORD" varchar(30) not null,
        PASSWORD_LAST_CHANGED_ON timestamp not null,
        ACCOUNT_EXPIRES_ON date,
        SECRET_QUESTION varchar(200),
        SECRET_ANSWER varchar(100),
        ID_PERSON numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        primary key (ID)
    );

    alter table PERSON_ACCOUNT
    add constraint PERSON_ACCOUNT_USERNAME_UNIQUE
    unique (USERNAME);

    alter table PERSON_ACCOUNT
    add constraint PERSON_ACCOUNT_ID_PERSON_UNIQUE
    unique (ID_PERSON);

    --------------------------------------------------
    ---       Table: POSTAL_ADDRESS                ---
    --------------------------------------------------

    create table POSTAL_ADDRESS (
        ID_CONTACT_CHANNEL numeric(18,0) not null,
        STREET varchar(50) not null,
        STREET_NUMBER varchar(10) not null,
        POSTAL_CODE varchar(10) not null,
        CITY varchar(40) not null,
        primary key (ID_CONTACT_CHANNEL)
    );

    alter table POSTAL_ADDRESS
    add constraint POSTAL_ADDRESS_STR_NO_ZIP_UNIQUE
    unique (STREET, STREET_NUMBER, POSTAL_CODE);

    --------------------------------------------------
    ---       Table: PRIVATE_CUSTOMER              ---
    --------------------------------------------------

    create table PRIVATE_CUSTOMER (
        ID numeric(18,0) not null,
        CUSTOMER_NUMBER varchar(30) not null,
        ID_SHIPPING_ADDRESS numeric(18,0),
        ID_INVOICE_ADDRESS numeric(18,0),
        ID_PERSON numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        primary key (ID)
    );

    alter table PRIVATE_CUSTOMER
    add constraint PRIVATE_CUSTOMER_CUSTOMER_NO_UNIQUE
    unique (CUSTOMER_NUMBER);

    alter table PRIVATE_CUSTOMER
    add constraint PRIVATE_CUSTOMER_ID_PERSON_UNIQUE
    unique (ID_PERSON);
    
    --------------------------------------------------
    ---       Table: EMPLOYEE              ---
    --------------------------------------------------

    create table EMPLOYEE (
        ID numeric(18,0) not null,
        EMPLOYEE_NUMBER varchar(30) not null,
        DEPARTMENT varchar(30) not null,
        ID_WORK_EMAIL_ADDRESS numeric(18,0) not null,
        ID_PERSON numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );
    
    alter table EMPLOYEE
    add constraint EMPLOYEE_PK primary key (ID);
    
    alter table EMPLOYEE
    add constraint EMPLOYEE_EMPLOYEE_NO_UNIQUE
    unique (EMPLOYEE_NUMBER);

    alter table EMPLOYEE
    add constraint EMPLOYEE_ID_PERSON_UNIQUE
    unique (ID_PERSON);

    --------------------------------------------------
    ---       Table: TELEPHONE_NUMBER              ---
    --------------------------------------------------

    create table TELEPHONE_NUMBER (
        ID_CONTACT_CHANNEL numeric(18,0) not null,
        PHONE_TYPE varchar(20) not null,
        PHONE_NUMBER varchar(30) not null,
        primary key (ID_CONTACT_CHANNEL)
    );

    alter table TELEPHONE_NUMBER
    add constraint TELEPHONE_NUMBER_NUMBER_UNIQUE
    unique (PHONE_NUMBER);

    --------------------------------------------------
    ---       Foreign Key Constraints              ---
    --------------------------------------------------

    alter table EMAIL_ADDRESS 
        add constraint EMAIL_ADDR_CONTACT_CH_FK
        foreign key (ID_CONTACT_CHANNEL) 
        references CONTACT_CHANNEL;
        
    alter table EMPLOYEE 
        add constraint EMPLOYEE_WORK_EMAIL_ADDR_FK 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references EMAIL_ADDRESS;

    alter table EMPLOYEE 
        add constraint EMPLOYEE_PERSON_FK 
        foreign key (ID_PERSON) 
        references PERSON;

    alter table PERSON 
        add constraint PERSON_POSTAL_ADDR_FK
        foreign key (ID_HOME_ADDRESS) 
        references POSTAL_ADDRESS;

    alter table PERSON 
        add constraint PERSON_MOBILE_PHONE_NO_FK
        foreign key (ID_PRIVATE_MOBILE_PHONE) 
        references TELEPHONE_NUMBER;

    alter table PERSON 
        add constraint PERSON_RESIDENT_PHONE_FK
        foreign key (ID_PRIVATE_RESIDENTIAL_PHONE) 
        references TELEPHONE_NUMBER;

    alter table PERSON 
        add constraint PERSON_FAX_FK
        foreign key (ID_PRIVATE_FAX) 
        references TELEPHONE_NUMBER;

    alter table PERSON_ACCOUNT 
        add constraint PERSON_ACCOUNT_PERSON_FK
        foreign key (ID_PERSON) 
        references PERSON;

    alter table POSTAL_ADDRESS 
        add constraint POSTAL_ADDR_CONTACT_CH_FK
        foreign key (ID_CONTACT_CHANNEL) 
        references CONTACT_CHANNEL;

    alter table PRIVATE_CUSTOMER 
        add constraint PRIV_CUSTOMER_SHIPPING_ADDR_FK
        foreign key (ID_SHIPPING_ADDRESS) 
        references POSTAL_ADDRESS;

    alter table PRIVATE_CUSTOMER 
        add constraint PRIV_CUSTOMER_INVOICE_ADDR_FK
        foreign key (ID_INVOICE_ADDRESS) 
        references POSTAL_ADDRESS;

    alter table PRIVATE_CUSTOMER 
        add constraint PRIV_CUSTOMER_PERSON_FK
        foreign key (ID_PERSON) 
        references PERSON;

    alter table TELEPHONE_NUMBER 
        add constraint TELEPHONE_NUMBER_CONTACT_CH_FK
        foreign key (ID_CONTACT_CHANNEL) 
        references CONTACT_CHANNEL;

    --------------------------------------------------
    ---       Sequences                            ---
    --------------------------------------------------

    create generator ID_SEQ_ACCOUNT;

    create generator ID_SEQ_CONTACT_CHANNEL;
    
    create generator ID_SEQ_EMPLOYEE;

    create generator ID_SEQ_PEOPLE;

    create generator ID_SEQ_PRIVATE_CUSTOMER;
