package com.express.model.domain
{
[RemoteClass(alias="com.express.service.dto.EffortRecordDto")]
public class EffortRecord {

   public function EffortRecord() {
   }

   public var id : Number;

   public var date : Date;

   public var effort : int;
}
}