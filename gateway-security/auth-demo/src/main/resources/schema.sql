create table if not exists oauth_client_details
(
    client_id               varchar(255) primary key,
    client_secret           varchar(255),
    resource_ids            varchar(255),
    scope                   varchar(255),
    authorized_grant_types  varchar(255),
    web_server_redirect_uri varchar(255),
    authorities             varchar(255),
    access_token_validity   int,
    refresh_token_validity  int,
    additional_information  varchar(255) default '',
    autoapprove             varchar(255) default 'true'
);


create table if not exists oauth_user_details
(
    username varchar(255) primary key,
    password varchar(255),
    roles    varchar(255),
    enabled  char(1) default '1'
);


