
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
        ID_DELIVERY_ADDRESS bigint not null unique,
        ID_COMPANY bigint not null unique,
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
        ID_WORK_PHONE_NUMBER bigint,
        ID_WORK_EMAIL_ADDRESS bigint not null,
        ID_PARENT_PERSON bigint not null unique,
        ID_CORPORATE_CLIENT bigint,
        ID_WORK_ADDRESS bigint,
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
        ID_PARENT_PERSON bigint not null unique,
        ID_WORK_ADDRESS bigint,
        ID_WORK_PHONE_NUMBER bigint,
        ID_WORK_EMAIL_ADDRESS bigint not null,
        ID_ITEM_SUPPLIER bigint,
        primary key (id)
    );

    create table ITEM.ITEM (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        ITEM_NUMBER varchar(30) not null unique,
        NAME varchar(60) not null unique,
        DESCRIPTION varchar(2000) not null,
        ID_ITEM_SUPPLIER bigint not null,
        primary key (id)
    );

    create table ITEM.ITEM_OPTION (
        id bigint not null,
        META_VERSION integer,
        NAME varchar(60) not null unique,
        DESCRIPTION varchar(200) not null,
        TYPE varchar(30) not null,
        primary key (id)
    );

    create table ITEM.ITEM_OPTION_SPECIFICATION (
        id bigint not null,
        META_VERSION integer,
        NAME varchar(200) not null,
        ID_ITEM_SPECIFICATION bigint not null,
        ID_ITEM_OPTION bigint not null,
        primary key (id)
    );

    create table ITEM.ITEM_SPECIFICATION (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        ITEM_SPECIFICATION_NUMBER varchar(30) not null unique,
        ID_ITEM bigint not null,
        primary key (id)
    );

    create table ITEM.OFFER (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        NAME varchar(60) not null unique,
        START_DATE date not null,
        END_DATE date,
        PRICE_AMOUNT numeric,
        PRICE_CURRENCY varchar(255),
        ID_ITEM bigint not null,
        primary key (id)
    );

    create table ITEM.PROMOTION (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        NAME varchar(60) not null unique,
        START_DATE date not null,
        END_DATE date,
        PROMOTIONAL_TEXT varchar(2000) not null,
        ID_OFFER bigint not null unique,
        primary key (id)
    );

    create table PEOPLE.ITEM_ITEM_OPTION_ASSOC (
        ID_ITEM bigint not null,
        ID_ITEM_OPTION bigint not null,
        primary key (ID_ITEM, ID_ITEM_OPTION)
    );

    create table ORDER.CORPORATE_CLIENT_ORDER (
        ID_ORDER bigint not null,
        ID_CORPORATE_ORDERER bigint not null,
        ID_CONTACT_PERSON bigint not null,
        primary key (ID_ORDER)
    );

    create table ORDER.CORPORATE_CLIENT_PAYMENT (
        ID_PAYMENT bigint not null,
        ID_CORPORATE_PAYER bigint not null,
        ID_PAID_ORDER bigint not null,
        primary key (ID_PAYMENT)
    );

    create table ORDER.ORDER (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        ORDER_NUMBER varchar(30) not null unique,
        ORDERED_ON timestamp not null,
        FULFILLED_ON timestamp,
        ID_SHIPPING_ADDRESS bigint not null,
        ID_BILLING_ADDRESS bigint,
        primary key (id),
        unique (ORDER_NUMBER)
    );

    create table ORDER.ORDER_POSITION (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        POSITION integer not null,
        COUNT_ORDERED_OFFERS integer not null,
        ID_OFFER bigint not null,
        ID_ORDER bigint not null,
        primary key (id),
        unique (ID_ORDER, POSITION)
    );

    create table ORDER.PAYMENT (
        id bigint not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        PAID_AMOUNT_AMOUNT numeric,
        PAID_AMOUNT_CURRENCY varchar(255),
        OBTAINED_ON timestamp not null,
        BOOKED_ON timestamp,
        ID_PAYMENT_TYPE bigint not null,
        primary key (id)
    );

    create table ORDER.PAYMENT_TYPE (
        id bigint not null,
        META_VERSION integer,
        NAME varchar(30) not null unique,
        DESCRIPTION varchar(200),
        primary key (id),
        unique (NAME)
    );

    create table ORDER.PRIVATE_CUSTOMER_ORDER (
        ID_ORDER bigint not null,
        ID_PRIVATE_ORDERER bigint not null,
        primary key (ID_ORDER)
    );

    create table ORDER.PRIVATE_CUSTOMER_PAYMENT (
        ID_PAYMENT bigint not null,
        ID_PRIVATE_PAYER bigint not null,
        ID_PAID_ORDER bigint not null,
        primary key (ID_PAYMENT)
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
        ID_PERSON bigint not null unique,
        ID_WORK_EMAIL_ADDRESS bigint not null,
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
        ID_PRIVATE_FAX bigint,
        ID_PRIVATE_RESIDENTIAL_PHONE bigint,
        ID_PRIVATE_MOBILE_PHONE bigint,
        ID_HOME_ADDRESS bigint not null,
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
        add constraint CORP_CLIENT_COMPANY_FK 
        foreign key (ID_COMPANY) 
        references COMPANY.COMPANY;

    alter table COMPANY.CORPORATE_CLIENT 
        add constraint CORP_CLIENT_DELIVERY_ADDRESS_FK 
        foreign key (ID_DELIVERY_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_CORP_CLIENT_FK 
        foreign key (ID_CORPORATE_CLIENT) 
        references COMPANY.CORPORATE_CLIENT 
        on delete cascade;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_WORK_EMAIL_FK 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references PEOPLE.EMAIL_ADDRESS;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_PARENT_PERSON_FK 
        foreign key (ID_PARENT_PERSON) 
        references PEOPLE.PERSON;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_WORK_ADDRESS_FK 
        foreign key (ID_WORK_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table COMPANY.CORPORATE_CLIENT_REPRESENTATIVE 
        add constraint CORP_CLIENT_REP_WORK_PHONE_FK 
        foreign key (ID_WORK_PHONE_NUMBER) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table COMPANY.ITEM_SUPPLIER 
        add constraint ITEM_SUPPLIER_COMPANY_FK 
        foreign key (ID_COMPANY) 
        references COMPANY.COMPANY;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_WORK_EMAIL_FK 
        foreign key (ID_WORK_EMAIL_ADDRESS) 
        references PEOPLE.EMAIL_ADDRESS;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_ITEM_SUPPLIER_FK 
        foreign key (ID_ITEM_SUPPLIER) 
        references COMPANY.ITEM_SUPPLIER 
        on delete cascade;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_PARENT_PERSON_FK 
        foreign key (ID_PARENT_PERSON) 
        references PEOPLE.PERSON;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_WORK_PHONE_FK 
        foreign key (ID_WORK_PHONE_NUMBER) 
        references PEOPLE.TELEPHONE_NUMBER;

    alter table COMPANY.ITEM_SUPPLIER_REPRESENTATIVE 
        add constraint ITEM_SUPPLIER_REP_WORK_ADDRESS_FK 
        foreign key (ID_WORK_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table ITEM.ITEM 
        add constraint ITEM_ITEM_SUPPLIER_FK 
        foreign key (ID_ITEM_SUPPLIER) 
        references COMPANY.ITEM_SUPPLIER;

    alter table ITEM.ITEM_OPTION_SPECIFICATION 
        add constraint ITEM_OPTION_SPEC_ITEM_OPTION_FK 
        foreign key (ID_ITEM_OPTION) 
        references ITEM.ITEM_OPTION;

    alter table ITEM.ITEM_OPTION_SPECIFICATION 
        add constraint ITEM_OPTION_SPEC_ITEM_SPEC_FK 
        foreign key (ID_ITEM_SPECIFICATION) 
        references ITEM.ITEM_SPECIFICATION 
        on delete cascade;

    alter table ITEM.ITEM_SPECIFICATION 
        add constraint ITEM_SPECIFICATION_ITEM_FK 
        foreign key (ID_ITEM) 
        references ITEM.ITEM 
        on delete cascade;

    alter table ITEM.OFFER 
        add constraint OFFER_ITEM_FK 
        foreign key (ID_ITEM) 
        references ITEM.ITEM;

    alter table ITEM.PROMOTION 
        add constraint PROMOTION_OFFER_FK 
        foreign key (ID_OFFER) 
        references ITEM.OFFER;

    alter table PEOPLE.ITEM_ITEM_OPTION_ASSOC 
        add constraint FK937A932B9A752A6C 
        foreign key (ID_ITEM_OPTION) 
        references ITEM.ITEM_OPTION;

    alter table PEOPLE.ITEM_ITEM_OPTION_ASSOC 
        add constraint FK937A932B9053A711 
        foreign key (ID_ITEM) 
        references ITEM.ITEM;

    alter table ORDER.CORPORATE_CLIENT_ORDER 
        add constraint CORP_ORDER_CONTACT_PERSON_FK 
        foreign key (ID_CONTACT_PERSON) 
        references COMPANY.CORPORATE_CLIENT_REPRESENTATIVE;

    alter table ORDER.CORPORATE_CLIENT_ORDER 
        add constraint CORP_ORDER_ORDERER_FK 
        foreign key (ID_CORPORATE_ORDERER) 
        references COMPANY.CORPORATE_CLIENT;

    alter table ORDER.CORPORATE_CLIENT_ORDER 
        add constraint FKB3AF3F7497155AE4 
        foreign key (ID_ORDER) 
        references ORDER.ORDER;

    alter table ORDER.CORPORATE_CLIENT_PAYMENT 
        add constraint FK9DEC74ACC3BEB289 
        foreign key (ID_CORPORATE_PAYER) 
        references ORDER.CORPORATE_CLIENT_ORDER 
        on delete cascade;

    alter table ORDER.CORPORATE_CLIENT_PAYMENT 
        add constraint FK9DEC74ACF3274E6C 
        foreign key (ID_PAYMENT) 
        references ORDER.PAYMENT;

    alter table ORDER.CORPORATE_CLIENT_PAYMENT 
        add constraint CORP_PAYMENT_PAID_ORDER_FK 
        foreign key (ID_PAID_ORDER) 
        references ORDER.CORPORATE_CLIENT_ORDER;

    alter table ORDER.CORPORATE_CLIENT_PAYMENT 
        add constraint CORP_PAYMENT_PAYER_FK 
        foreign key (ID_CORPORATE_PAYER) 
        references COMPANY.CORPORATE_CLIENT;

    alter table ORDER.ORDER 
        add constraint FK47F8F2E46A78C77 
        foreign key (ID_SHIPPING_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table ORDER.ORDER 
        add constraint FK47F8F2EC293B27C 
        foreign key (ID_BILLING_ADDRESS) 
        references PEOPLE.POSTAL_ADDRESS;

    alter table ORDER.ORDER_POSITION 
        add constraint ORDER_DETAIL_OFFER_FK 
        foreign key (ID_OFFER) 
        references ITEM.OFFER;

    alter table ORDER.ORDER_POSITION 
        add constraint ORDER_DETAIL_ORDER_FK 
        foreign key (ID_ORDER) 
        references ORDER.ORDER 
        on delete cascade;

    alter table ORDER.PAYMENT 
        add constraint PAYMENT_PAYMENT_TYPE_FK 
        foreign key (ID_PAYMENT_TYPE) 
        references ORDER.PAYMENT_TYPE;

    alter table ORDER.PRIVATE_CUSTOMER_ORDER 
        add constraint PRIV_ORDER_ORDERER_FK 
        foreign key (ID_PRIVATE_ORDERER) 
        references PEOPLE.PRIVATE_CUSTOMER;

    alter table ORDER.PRIVATE_CUSTOMER_ORDER 
        add constraint FKF82BEDE997155AE4 
        foreign key (ID_ORDER) 
        references ORDER.ORDER;

    alter table ORDER.PRIVATE_CUSTOMER_PAYMENT 
        add constraint FKB5F759E17A0108D6 
        foreign key (ID_PRIVATE_PAYER) 
        references ORDER.PRIVATE_CUSTOMER_ORDER 
        on delete cascade;

    alter table ORDER.PRIVATE_CUSTOMER_PAYMENT 
        add constraint PRIV_PAYMENT_PAYER_FK 
        foreign key (ID_PRIVATE_PAYER) 
        references PEOPLE.PRIVATE_CUSTOMER;

    alter table ORDER.PRIVATE_CUSTOMER_PAYMENT 
        add constraint FKB5F759E1F3274E6C 
        foreign key (ID_PAYMENT) 
        references ORDER.PAYMENT;

    alter table ORDER.PRIVATE_CUSTOMER_PAYMENT 
        add constraint PRIV_PAYMENT_PAID_ORDER_FK 
        foreign key (ID_PAID_ORDER) 
        references ORDER.PRIVATE_CUSTOMER_ORDER;

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

    create sequence PEOPLE.ID_SEQ_ITEM;

    create sequence PEOPLE.ID_SEQ_ITEM_OPTION;

    create sequence PEOPLE.ID_SEQ_ITEM_OPTION_SPEC;

    create sequence PEOPLE.ID_SEQ_ITEM_SPEC;

    create sequence PEOPLE.ID_SEQ_ITEM_SUPPLIER;

    create sequence PEOPLE.ID_SEQ_ITEM_SUPPLIER_REP;

    create sequence PEOPLE.ID_SEQ_OFFER;

    create sequence PEOPLE.ID_SEQ_ORDER;

    create sequence PEOPLE.ID_SEQ_ORDER_POSITION;

    create sequence PEOPLE.ID_SEQ_PAYMENT;

    create sequence PEOPLE.ID_SEQ_PAYMENT_TYPE;

    create sequence PEOPLE.ID_SEQ_PEOPLE;

    create sequence PEOPLE.ID_SEQ_PRIVATE_CUSTOMER;

    create sequence PEOPLE.ID_SEQ_PROMOTION;
