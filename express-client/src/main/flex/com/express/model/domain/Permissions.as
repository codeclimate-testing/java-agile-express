package com.express.model.domain {

[RemoteClass(alias="com.express.service.dto.PermissionsDto")]
public class Permissions {
   
   public var id : Number;

   public var version : Number;

   public var iterationAdmin : Boolean;

   public var projectAdmin : Boolean;
   
}
}