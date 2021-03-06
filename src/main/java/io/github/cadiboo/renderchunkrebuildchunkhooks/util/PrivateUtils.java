package io.github.cadiboo.renderchunkrebuildchunkhooks.util;

import com.google.common.annotations.Beta;
import io.github.cadiboo.renderchunkrebuildchunkhooks.RenderChunkRebuildChunkHooks;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.VersionChecker;

import javax.annotation.Nonnull;

import static net.minecraftforge.fml.VersionChecker.getResult;

/**
 * @author Cadiboo
 */
@Deprecated
@Beta
public final class PrivateUtils {

	/**
	 * Ew
	 *
	 * @param modContainer the {@link ModContainer} for {@link RenderChunkRebuildChunkHooks}
	 */
	@Deprecated
	@Beta
	public static void launchUpdateDaemon(ModContainer modContainer) {

		new Thread(() -> {
			WHILE:
			while (true) {

				final VersionChecker.CheckResult checkResult = getResult(modContainer.getModInfo());

				switch (checkResult.status) {
					default:
					case PENDING:
						break;
					case OUTDATED:
						try {
							BadAutoUpdater.update(modContainer.getModInfo().getVersion(), checkResult.target.toString());
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						//fallthrough
					case FAILED:
					case UP_TO_DATE:
					case AHEAD:
					case BETA:
					case BETA_OUTDATED:
						break WHILE;
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

		}, "RenderChunk rebuildChunk Hooks Update Daemon").start();

	}

	@Nonnull
	public static <T> T _null() {
		return null;
	}

}
