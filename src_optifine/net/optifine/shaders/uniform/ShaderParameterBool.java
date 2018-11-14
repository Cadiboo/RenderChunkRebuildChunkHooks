package net.optifine.shaders.uniform;

import net.optifine.expr.IExpressionBool;

public enum ShaderParameterBool implements IExpressionBool {
   IS_ALIVE("is_alive"),
   IS_BURNING("is_burning"),
   IS_CHILD("is_child"),
   IS_GLOWING("is_glowing"),
   IS_HURT("is_hurt"),
   IS_IN_LAVA("is_in_lava"),
   IS_IN_WATER("is_in_water"),
   IS_INVISIBLE("is_invisible"),
   IS_ON_GROUND("is_on_ground"),
   IS_RIDDEN("is_ridden"),
   IS_RIDING("is_riding"),
   IS_SNEAKING("is_sneaking"),
   IS_SPRINTING("is_sprinting"),
   IS_WET("is_wet");

   private String name;
   private bzf renderManager;
   private static final ShaderParameterBool[] VALUES = values();

   private ShaderParameterBool(String name) {
      this.name = name;
      this.renderManager = bib.z().ac();
   }

   public String getName() {
      return this.name;
   }

   public boolean eval() {
      vg entityGeneral = bib.z().aa();
      if (entityGeneral instanceof vp) {
         vp entity = (vp)entityGeneral;
         switch(this) {
         case IS_ALIVE:
            return entity.aC();
         case IS_BURNING:
            return entity.aR();
         case IS_CHILD:
            return entity.l_();
         case IS_GLOWING:
            return entity.aW();
         case IS_HURT:
            return entity.ay > 0;
         case IS_IN_LAVA:
            return entity.au();
         case IS_IN_WATER:
            return entity.ao();
         case IS_INVISIBLE:
            return entity.aX();
         case IS_ON_GROUND:
            return entity.z;
         case IS_RIDDEN:
            return entity.aT();
         case IS_RIDING:
            return entity.aS();
         case IS_SNEAKING:
            return entity.aU();
         case IS_SPRINTING:
            return entity.aV();
         case IS_WET:
            return entity.an();
         }
      }

      return false;
   }

   public static ShaderParameterBool parse(String str) {
      if (str == null) {
         return null;
      } else {
         for(int i = 0; i < VALUES.length; ++i) {
            ShaderParameterBool type = VALUES[i];
            if (type.getName().equals(str)) {
               return type;
            }
         }

         return null;
      }
   }
}
