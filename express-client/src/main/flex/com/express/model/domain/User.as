package com.express.model.domain
{
import mx.collections.ArrayCollection;

[RemoteClass(alias="com.express.service.dto.UserDto")]
public class User
{
   public function User() {
      projects = new ArrayCollection();
   }

   public var id : Number;

   public var version : Number;

   public var email : String; //unique field, used as username.

   private var _fullName : String;

   public var firstName : String;

   public var lastName : String;

   public var password : String;

   public var passwordHint : String;

   public var phone1 : String;

   public var phone2 : String;

   [Bindable]
   public var colour : uint;

   public var projects : ArrayCollection;

   public var accessRequests : ArrayCollection;

   public var hasProjects : Boolean;

   public var storyWindowPreference : WindowMetrics;

   public function get fullName() : String {
      if(!firstName && !lastName) {
         return email.substr(0,email.indexOf("@"));
      }
      return firstName + " " + lastName;
   }

}
}