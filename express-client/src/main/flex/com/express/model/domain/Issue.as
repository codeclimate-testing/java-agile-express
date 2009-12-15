package com.express.model.domain
{
public class Issue {
   public static const STATUS_NEW : String = "NEW";

   public static const STATUS_PROGRESS : String = "IN PROGRESS";
   public static const STATUS_TEST : String = "IN TEST";
   public static const STATUS_COMPLETE : String = "COMPLETE";

   public static const STATUS_REJECTED : String = "REJECTED";

   public function Issue() {
   }

   public var id : Number;

   public var version : Number;

   public var reference : String;

   public var title : String;

   public var description : String;

   public var status : String;

   public var effort : int;

   public var assignedTo : User;

   public var createdBy : User;

   public var createdDate : Date;

}
}