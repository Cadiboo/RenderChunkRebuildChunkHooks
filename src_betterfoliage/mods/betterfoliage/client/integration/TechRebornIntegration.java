package mods.betterfoliage.client.integration;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import mods.betterfoliage.client.Client;
import mods.betterfoliage.client.render.LogRegistry;
import mods.octarinecore.metaprog.ClassRef;
import mods.octarinecore.metaprog.Reflection;
import mods.octarinecore.metaprog.Resolvable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
@Metadata(
   mv = {1, 1, 9},
   bv = {1, 0, 2},
   k = 1,
   d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"},
   d2 = {"Lmods/betterfoliage/client/integration/TechRebornIntegration;", "", "()V", "BlockRubberLog", "Lmods/octarinecore/metaprog/ClassRef;", "getBlockRubberLog", "()Lmods/octarinecore/metaprog/ClassRef;", "BetterFoliage-MC1.12"}
)
public final class TechRebornIntegration {
   @NotNull
   private static final ClassRef BlockRubberLog;
   public static final TechRebornIntegration INSTANCE;

   @NotNull
   public final ClassRef getBlockRubberLog() {
      return BlockRubberLog;
   }

   static {
      TechRebornIntegration var0 = new TechRebornIntegration();
      INSTANCE = var0;
      BlockRubberLog = new ClassRef("techreborn.blocks.BlockRubberLog");
      if (Loader.isModLoaded("techreborn") && Reflection.allAvailable((Resolvable)BlockRubberLog)) {
         Client var10000 = Client.INSTANCE;
         Level var10001 = Level.INFO;
         Intrinsics.checkExpressionValueIsNotNull(Level.INFO, "Level.INFO");
         var10000.log(var10001, "TechReborn support initialized");
         LogRegistry.INSTANCE.getSubRegistries().add(TechRebornLogSupport.INSTANCE);
      }

   }
}
