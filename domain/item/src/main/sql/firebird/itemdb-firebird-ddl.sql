SET NAMES UTF8;

CONNECT 'E:\Var\Data\Firebird\PeopleDB\People.fdb' USER 'SYSDBA' PASSWORD 'masterkey';


    --------------------------------------------------
    ---       Table: ITEM                          ---
    --------------------------------------------------

    create table ITEM (
        id numeric(18,0) not null,
        ITEM_NUMBER varchar(30) not null,
        NAME varchar(60) not null,
        DESCRIPTION varchar(2000) not null,
        ID_ITEM_SUPPLIER numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table ITEM
        add constraint ITEM_PK
        primary key (ID);

    alter table ITEM
        add constraint ITEM_NUMBER_UNIQUE
        unique (ITEM_NUMBER);

    alter table ITEM
        add constraint ITEM_NAME_UNIQUE
        unique (NAME);

    --------------------------------------------------
    ---       Table: ITEM_OPTION                   ---
    --------------------------------------------------

    create table ITEM_OPTION (
        id numeric(18,0) not null,
        NAME varchar(60) not null,
        DESCRIPTION varchar(200) not null,
        TYPE varchar(30) not null,
        META_VERSION integer
    );

    alter table ITEM_OPTION
        add constraint ITEM_OPTION_PK
        primary key (ID);

    alter table ITEM_OPTION
        add constraint ITEM_OPTION_NAME_UNIQUE
        unique (NAME);

    --------------------------------------------------
    ---       Table: ITEM_OPTION_SPECIFICATION     ---
    --------------------------------------------------

    create table ITEM_OPTION_SPECIFICATION (
        id numeric(18,0) not null,
        NAME varchar(200) not null,
        ID_ITEM_OPTION numeric(18,0) not null,
        ID_ITEM_SPECIFICATION numeric(18,0) not null,
        META_VERSION integer
    );

    alter table ITEM_OPTION_SPECIFICATION
        add constraint ITEM_OPTION_SPECIFICATION_PK
        primary key (ID);

    --------------------------------------------------
    ---       Table: ITEM_OPTION_SPECIFICATION     ---
    --------------------------------------------------

    create table ITEM_SPECIFICATION (
        id numeric(18,0) not null,
        ITEM_SPECIFICATION_NUMBER varchar(30) not null,
        ID_ITEM numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table ITEM_SPECIFICATION
        add constraint ITEM_SPECIFICATION_PK
        primary key (ID);

    alter table ITEM_SPECIFICATION
        add constraint ITEM_SPEC_NUMBER_UNIQUE
        unique (ITEM_SPECIFICATION_NUMBER);

    --------------------------------------------------
    ---       Table: ITEM_OPTION_SPECIFICATION     ---
    --------------------------------------------------

    create table OFFER (
        id numeric(18,0) not null,
        NAME varchar(60) not null,
        START_DATE date not null,
        END_DATE date,
        PRICE_AMOUNT numeric(18,2),
        PRICE_CURRENCY varchar(255),
        ID_ITEM numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table OFFER
        add constraint OFFER_PK
        primary key (ID);

    alter table OFFER
        add constraint OFFER_NAME_UNIQUE
        unique (NAME);

    --------------------------------------------------
    ---       Table: ITEM_OPTION_SPECIFICATION     ---
    --------------------------------------------------

    create table PROMOTION (
        id numeric(18,0) not null,
        NAME varchar(60) not null,
        START_DATE date not null,
        END_DATE date,
        PROMOTIONAL_TEXT varchar(2000) not null,
        ID_OFFER numeric(18,0) not null,
        META_VERSION integer,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null
    );

    alter table PROMOTION
        add constraint PROMOTION_PK
        primary key (ID);

    alter table PROMOTION
        add constraint PROMOTION_NAME_UNIQUE
        unique (NAME);

    alter table PROMOTION
        add constraint ID_OFFER_UNIQUE
        unique (ID_OFFER);

    --------------------------------------------------
    ---       Table: ITEM_OPTION_SPECIFICATION     ---
    --------------------------------------------------

    create table ITEM_ITEM_OPTION_ASSOC (
        ID_ITEM numeric(18,0) not null,
        ID_ITEM_OPTION numeric(18,0) not null
    );

    alter table ITEM_ITEM_OPTION_ASSOC
        add constraint ITEM_ITEM_OPTION_ASSOC_PK
        primary key (ID_ITEM, ID_ITEM_OPTION);

    --------------------------------------------------
    ---       Foreign Key Constraints              ---
    --------------------------------------------------

    alter table ITEM
        add constraint ITEM_ITEM_SUPPLIER_FK 
        foreign key (ID_ITEM_SUPPLIER) 
        references ITEM_SUPPLIER;

    alter table ITEM_OPTION_SPECIFICATION
        add constraint ITEM_OPTION_SPEC_ITEM_OPTION_FK 
        foreign key (ID_ITEM_OPTION) 
        references ITEM_OPTION;

    alter table ITEM_OPTION_SPECIFICATION
        add constraint ITEM_OPTION_SPEC_ITEM_SPEC_FK 
        foreign key (ID_ITEM_SPECIFICATION) 
        references ITEM_SPECIFICATION
        on delete cascade;

    alter table ITEM_SPECIFICATION
        add constraint ITEM_SPECIFICATION_ITEM_FK 
        foreign key (ID_ITEM) 
        references ITEM
        on delete cascade;

    alter table OFFER
        add constraint OFFER_ITEM_FK 
        foreign key (ID_ITEM) 
        references ITEM;

    alter table PROMOTION
        add constraint PROMOTION_OFFER_FK 
        foreign key (ID_OFFER) 
        references OFFER;

    alter table ITEM_ITEM_OPTION_ASSOC 
        add constraint ITM_ITM_OPT_ASSOC_ID_ITM_OPT_FK
        foreign key (ID_ITEM_OPTION) 
        references ITEM_OPTION;

    alter table ITEM_ITEM_OPTION_ASSOC 
        add constraint ITM_ITM_OPT_ASSOC_ID_ITM_FK
        foreign key (ID_ITEM) 
        references ITEM;

    --------------------------------------------------
    ---       Sequences                            ---
    --------------------------------------------------

    create generator ID_SEQ_ITEM;

    create generator ID_SEQ_ITEM_OPTION;

    create generator ID_SEQ_ITEM_OPTION_SPEC;

    create generator ID_SEQ_ITEM_SPEC;

    create generator ID_SEQ_OFFER;

    create generator ID_SEQ_PROMOTION;
