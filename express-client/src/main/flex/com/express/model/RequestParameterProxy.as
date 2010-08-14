package com.express.model {
import flash.utils.Dictionary;

import org.puremvc.as3.patterns.proxy.Proxy;

public class RequestParameterProxy extends Proxy{
   public static const NAME : String = "RequestParameterProxy";

   private var _parameters : Dictionary;

   public function RequestParameterProxy(urlFragment : String) {
      super(NAME)
      _parameters = new Dictionary();
      var pairs:Array = urlFragment.split("&");
      for each(var pair : String in pairs) {
         var splitPair:Array = pair.split("=");
         _parameters[splitPair[0]] = splitPair[1];
      }
   }

   public function hasValue(key : String) : Boolean {
      return _parameters[key] != null;
   }

   public function getValue(key : String) : String {
      return _parameters[key]
   }
}
}