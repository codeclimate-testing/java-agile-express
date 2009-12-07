package com.express.service.dto;

/**
 *
 */

public class CSVRequest {
   public static final int TYPE_ITERATION_BACKLOG = 1;

   public static final int TYPE_PRODUCT_BACKLOG = 2;

   private Long id;

   private int type;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }
}
