package net.optifine.shaders.config;

public class ShaderMacro {
   private String name;
   private String value;

   public ShaderMacro(String name, String value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }
}
