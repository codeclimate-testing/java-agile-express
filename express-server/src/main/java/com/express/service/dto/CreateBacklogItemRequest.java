package com.express.service.dto;

import java.io.Serializable;


public class CreateBacklogItemRequest implements Serializable {

   private static final long serialVersionUID = -6979146785438719380L;
   
   public static final Integer UNCOMMITED_STORY  = 0;
   public static final Integer STORY = 1;
   public static final Integer TASK = 2;
   
   private Integer type;
   
   private Long parentId;
   
   private  BacklogItemDto backlogItem;

   
   public Integer getType() {
      return type;
   }

   
   public void setType(Integer type) {
      this.type = type;
   }

   
   public Long getParentId() {
      return parentId;
   }

   
   public void setParentId(Long parentId) {
      this.parentId = parentId;
   }

   
   public BacklogItemDto getBacklogItem() {
      return backlogItem;
   }

   
   public void setBacklogItem(BacklogItemDto backlogItem) {
      this.backlogItem = backlogItem;
   }

}
