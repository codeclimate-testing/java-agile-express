package com.express.model {
import flash.utils.Dictionary;

import mx.managers.IBrowserManager;

import org.puremvc.as3.patterns.proxy.Proxy;

public class RequestParameterProxy extends Proxy{
   public static const NAME : String = "RequestParameterProxy";
   public static const PROJECT_ID_PARAM : String = "projectId";
   public static const ITERATION_ID_PARAM : String = "iterationId";
   public static const BACKLOG_ITEM_ID_PARAM : String = "backlogItemId";

   private var _initialParameters : Dictionary;
   private var _urlParameters : Dictionary;
   private var _browserManager : IBrowserManager;

   public function RequestParameterProxy(browserManager : IBrowserManager) {
      super(NAME);
      _browserManager = browserManager;
      _initialParameters = new Dictionary();
      _urlParameters = new Dictionary();
      var pairs:Array = _browserManager.fragment.split("&");
      for each(var pair : String in pairs) {
         var splitPair:Array = pair.split("=");
         _initialParameters[splitPair[0]] = splitPair[1];
      }
   }

   public function hasValue(key : String) : Boolean {
      return _initialParameters[key] != null;
   }

   public function getAndRemoveValue(key : String) : String {
      var value : String = _initialParameters[key];
      _initialParameters[key] = null;
      return value;
   }

   public function setParameter(key : String, value : String) : void {
      _urlParameters[key] = value;
      setParametersInUrl();
   }

   private function setParametersInUrl() : void {
      var fragment : String = "";
      for (var key : String in _urlParameters) {
         if(fragment.length > 0) {
            fragment += "&";
         }
         fragment += key + "=" + _urlParameters[key];
      }
      _browserManager.setFragment(fragment);
   }
}
}