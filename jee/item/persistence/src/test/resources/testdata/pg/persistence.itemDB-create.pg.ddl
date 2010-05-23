
	create schema ITEM authorization postgres;
	
    create table ITEM.ITEM (
        id int8 not null,
        META_VERSION int4,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        DESCRIPTION varchar(2000) not null,
        ITEM_NUMBER varchar(30) not null unique,
        NAME varchar(60) not null unique,
        primary key (id)
    );

    create table ITEM.ITEM_ITEM_OPTION_ASSOC (
        ID_ITEM int8 not null,
        ID_ITEM_OPTION int8 not null,
        primary key (ID_ITEM, ID_ITEM_OPTION)
    );

    create table ITEM.ITEM_OPTION (
        id int8 not null,
        META_VERSION int4,
        DESCRIPTION varchar(200) not null,
        NAME varchar(60) not null unique,
        TYPE varchar(30) not null,
        primary key (id)
    );

    create table ITEM.ITEM_OPTION_SPECIFICATION (
        id int8 not null,
        META_VERSION int4,
        NAME varchar(200) not null,
        ID_ITEM_SPECIFICATION int8 not null,
        ID_ITEM_OPTION int8 not null,
        primary key (id)
    );

    create table ITEM.ITEM_SPECIFICATION (
        id int8 not null,
        META_VERSION int4,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        ITEM_SPECIFICATION_NUMBER varchar(30) not null unique,
        ID_ITEM int8 not null,
        primary key (id)
    );

    create table ITEM.OFFER (
        id int8 not null,
        META_VERSION int4,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        END_DATE date,
        NAME varchar(60) not null unique,
        PRICE_AMOUNT numeric(19, 2),
        PRICE_CURRENCY varchar(255),
        START_DATE date not null,
        ID_ITEM int8 not null,
        primary key (id)
    );

    create table ITEM.PROMOTION (
        id int8 not null,
        META_VERSION int4,
        AUDIT_CREATED_BY varchar(15) not null,
        AUDIT_CREATED_ON timestamp not null,
        AUDIT_LAST_UPDATED_BY varchar(15) not null,
        AUDIT_LAST_UPDATED_ON timestamp not null,
        END_DATE date,
        NAME varchar(60) not null unique,
        PROMOTIONAL_TEXT varchar(2000) not null,
        START_DATE date not null,
        ID_OFFER int8 not null unique,
        primary key (id)
    );

    alter table ITEM.ITEM_ITEM_OPTION_ASSOC 
        add constraint FK_ITEMOPTION_ITEM 
        foreign key (ID_ITEM_OPTION) 
        references ITEM.ITEM_OPTION;

    alter table ITEM.ITEM_ITEM_OPTION_ASSOC 
        add constraint FK_ITEM_ITEMOPTION 
        foreign key (ID_ITEM) 
        references ITEM.ITEM;

    alter table ITEM.ITEM_OPTION_SPECIFICATION 
        add constraint FK_ITEMOPTIONSPEC_ITEMOPTION 
        foreign key (ID_ITEM_OPTION) 
        references ITEM.ITEM_OPTION;

    alter table ITEM.ITEM_OPTION_SPECIFICATION 
        add constraint FK_ITEMOPTIONSPEC_ITEMSPEC 
        foreign key (ID_ITEM_SPECIFICATION) 
        references ITEM.ITEM_SPECIFICATION 
        on delete cascade;

    alter table ITEM.ITEM_SPECIFICATION 
        add constraint FK_ITEMSPEC_ITEM 
        foreign key (ID_ITEM) 
        references ITEM.ITEM 
        on delete cascade;

    alter table ITEM.OFFER 
        add constraint FK_OFFER_ITEM 
        foreign key (ID_ITEM) 
        references ITEM.ITEM;

    alter table ITEM.PROMOTION 
        add constraint FK_PROMOTION_OFFER 
        foreign key (ID_OFFER) 
        references ITEM.OFFER;

    create sequence ITEM.ID_SEQ_ITEM_OPTION;

    create sequence ITEM.ID_SEQ_ITEM_OPTION_SPEC;

    create sequence ITEM.ID_SEQ_ITEM_SPEC;

    create sequence ITEM.ID_SEQ_OFFER;

    create sequence ITEM.ID_SEQ_PROMOTION;

    create sequence ITEM.ID_SEQ_ITEM;
