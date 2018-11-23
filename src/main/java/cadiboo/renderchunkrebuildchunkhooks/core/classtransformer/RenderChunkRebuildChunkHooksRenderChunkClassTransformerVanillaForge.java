package cadiboo.renderchunkrebuildchunkhooks.core.classtransformer;

import cadiboo.renderchunkrebuildchunkhooks.core.util.InjectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import static cadiboo.renderchunkrebuildchunkhooks.core.util.ObfuscationHelper.ObfuscationMethod.*;
import static cadiboo.renderchunkrebuildchunkhooks.core.util.ObfuscationHelper.ObfuscationField.*;

/**
 * @author Cadiboo
 * @see <a href="http://www.egtry.com/java/bytecode/asm/tree_transform">http://www.egtry.com/java/bytecode/asm/tree_transform</a>
 */
// useful links:
// https://text-compare.com
// http://www.minecraftforge.net/forum/topic/32600-1710-strange-error-with-custom-event-amp-event-handler/?do=findComment&comment=172480
public class RenderChunkRebuildChunkHooksRenderChunkClassTransformerVanillaForge extends RenderChunkRebuildChunkHooksRenderChunkClassTransformer {

	@Override
	public byte[] transform(final String unTransformedName, final String transformedName, final byte[] basicClass) {

		return super.transform(unTransformedName, transformedName, basicClass);

	}

	public void injectHooks(InsnList instructions) {

		super.injectHooks(instructions);

	}

	/**
	 * get "++renderChunksUpdated;"
	 * inject after
	 * get line number for nice debug
	 *
	 * @param instructions the instructions for the method
	 */
	public boolean injectRebuildChunkPreEventHook(InsnList instructions) {

		FieldInsnNode PUTSTATIC_renderChunksUpdated_Node = null;

		// Find the bytecode instruction for "++renderChunksUpdated" ("PUTSTATIC net/minecraft/client/renderer/chunk/RenderChunk.renderChunksUpdated : I")
		for (AbstractInsnNode instruction : instructions.toArray()) {

			if (instruction.getOpcode() == PUTSTATIC) {
				if (instruction.getType() == AbstractInsnNode.FIELD_INSN) {
					final FieldInsnNode fieldInsnNode = (FieldInsnNode) instruction;
					if (fieldInsnNode.desc.equals(RENDER_CHUNK_RENDER_CHUNKS_UPDATED.getDescriptor())) {
						if (fieldInsnNode.name.equals(RENDER_CHUNK_RENDER_CHUNKS_UPDATED.getName())) {
							PUTSTATIC_renderChunksUpdated_Node = fieldInsnNode;
							break;
						}
					}
				}
			}
		}

		if (PUTSTATIC_renderChunksUpdated_Node == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		LabelNode preExistingLabelNode = null;

		// go down the instructions until we find the next Label
		for (int i = instructions.indexOf(PUTSTATIC_renderChunksUpdated_Node); i < instructions.size() - 1; i++) {
			if (instructions.get(i).getType() != AbstractInsnNode.LABEL) {
				continue;
			}
			preExistingLabelNode = (LabelNode) instructions.get(i);
			break;
		}

		if (preExistingLabelNode == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		LineNumberNode preExistingLineNumberNode = null;

		// go back up the instructions until we find the Line Number Node
		for (int i = instructions.indexOf(PUTSTATIC_renderChunksUpdated_Node) - 1; i >= 0; i--) {
			if (instructions.get(i).getType() != AbstractInsnNode.LINE) {
				continue;
			}
			preExistingLineNumberNode = (LineNumberNode) instructions.get(i);
			break;
		}

		if (preExistingLineNumberNode == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		final InsnList tempInstructionList = new InsnList();

		LabelNode ourLabel = new LabelNode(new Label());
		tempInstructionList.add(ourLabel);
		tempInstructionList.add(new LineNumberNode(preExistingLineNumberNode.line, ourLabel));
		/**
		 * @param renderChunk         the instance of {@link RenderChunk} the event is being fired for
		 * @param renderGlobal        the {@link RenderGlobal} passed in from RenderChunk#rebuildChunk
		 * @param worldView           the {@link ChunkCache} passed in from RenderChunk#rebuildChunk
		 * @param generator           the {@link ChunkCompileTaskGenerator} passed in from RenderChunk#rebuildChunk
		 * @param compiledChunk       the {@link CompiledChunk} passed in from RenderChunk#rebuildChunk
		 * @param renderChunkPosition the {@link BlockPos.MutableBlockPos position} passed in from RenderChunk#rebuildChunk
		 * @param x                   the translation X passed in from RenderChunk#rebuildChunk
		 * @param y                   the translation Y passed in from RenderChunk#rebuildChunk
		 * @param z                   the translation Z passed in from RenderChunk#rebuildChunk
		 *
		 * @return If vanilla rendering should be stopped
		 *
		 * @see cadiboo.renderchunkrebuildchunkhooks.core.util.rebuildChunk_diff and cadiboo.renderchunkrebuildchunkhooks.core.util.rebuildChunkOptifine_diff
		 */
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // this
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // renderGlobal
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_RENDER_GLOBAL.getName(), "Lnet/minecraft/client/renderer/RenderGlobal;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // worldView
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_WORLD_VIEW.getName(), "Lnet/minecraft/world/ChunkCache;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_generator)); // generator
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_compiledchunk)); // compiledchunk
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // position
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_POSITION.getName(), "Lnet/minecraft/util/math/BlockPos$MutableBlockPos;"));
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_x)); // x
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_y)); // y
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_z)); // z
		tempInstructionList.add(new MethodInsnNode(INVOKESTATIC, "cadiboo/renderchunkrebuildchunkhooks/hooks/RenderChunkRebuildChunkHooksHooks", "onRebuildChunkPreEvent", "(Lnet/minecraft/client/renderer/chunk/RenderChunk;Lnet/minecraft/client/renderer/RenderGlobal;Lnet/minecraft/world/ChunkCache;Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;Lnet/minecraft/client/renderer/chunk/CompiledChunk;Lnet/minecraft/util/math/BlockPos$MutableBlockPos;FFF)Z", false));
		tempInstructionList.add(new LabelNode(new Label()));
		tempInstructionList.add(new JumpInsnNode(IFEQ, preExistingLabelNode));
		tempInstructionList.add(new InsnNode(RETURN));

		instructions.insertBefore(preExistingLabelNode, tempInstructionList);

		return true;
	}

	/**
	 * get "block.canRenderInLayer(iblockstate, blockrenderlayer);"<br>
	 * "INVOKEVIRTUAL net/minecraft/block/Block.canRenderInLayer (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockRenderLayer;)Z"<br>
	 * replace it with our hook and inject before & after (let the existing ALOADs stay because we use the same variables)<br>
	 *
	 * @param instructions the instructions for the method
	 */
	public boolean injectRebuildChunkBlockRenderInLayerEventHook(InsnList instructions) {

		final AbstractInsnNode INVOKEVIRTUAL_Block_canRenderInLayer_Node = InjectionHelper.getCanRenderInBlockInjectionPoint(instructions);

		if (INVOKEVIRTUAL_Block_canRenderInLayer_Node == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		// ALOAD 17: block
		// ALOAD 16: iblockstate
		// ALOAD 18: blockrenderlayer1
		// INVOKEVIRTUAL net/minecraft/block/Block.canRenderInLayer(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockRenderLayer;)Z

		// cast to be notified early (by crashing) if they arent the variables we want
		final VarInsnNode ALOAD_blockrenderlayer1_Node = (VarInsnNode) INVOKEVIRTUAL_Block_canRenderInLayer_Node.getPrevious();
		final VarInsnNode ALOAD_iblockstate_Node = (VarInsnNode) ALOAD_blockrenderlayer1_Node.getPrevious();
		final VarInsnNode ALOAD_block_Node = (VarInsnNode) ALOAD_iblockstate_Node.getPrevious();

		final InsnList tempInstructionList = new InsnList();

		// TODO find ALOAD refrences instead of hardcoding them

		// Inject a call to RenderChunkRebuildChunkHooksHooks#canBlockRenderInLayer()} AFTER the call to block.canRenderInLayer(iblockstate, blockrenderlayer1)
		// add our hook

		/**
		 * @param renderChunk               the instance of {@link RenderChunk} the event is being fired for
		 * @param worldView                 the {@link ChunkCache} passed in from RenderChunk#rebuildChunk
		 * @param chunkCompileTaskGenerator the {@link ChunkCompileTaskGenerator} passed in from RenderChunk#rebuildChunk
		 * @param compiledChunk             the {@link CompiledChunk} passed in from RenderChunk#rebuildChunk
		 * @param blockRendererDispatcher   the {@link BlockRendererDispatcher} passed in from RenderChunk#rebuildChunk
		 * @param renderChunkPosition       the {@link BlockPos.MutableBlockPos position} passed in from RenderChunk#rebuildChunk
		 * @param visGraph                  the {@link VisGraph} passed in from RenderChunk#rebuildChunk
		 * @param blockPos                  the {@link BlockPos.MutableBlockPos position} of the block being assessed
		 * @param block                     the {@link Block block} being assessed
		 * @param blockState                the {@link IBlockState state} of the block being assessed
		 * @param blockRenderLayer          the {@link BlockRenderLayer} of the block being assessed
		 *
		 * @return If the block can render in the layer
		 *
		 * @see cadiboo.renderchunkrebuildchunkhooks.core.util.rebuildChunk_diff and cadiboo.renderchunkrebuildchunkhooks.core.util.rebuildChunkOptifine_diff
		 */

		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // RenderChunk - renderChunk
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // ChunkCache - worldView
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_WORLD_VIEW.getName(), "Lnet/minecraft/world/ChunkCache;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_generator)); // generator
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_compiledchunk)); // compiledchunk
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_blockrendererdispatcher)); // blockRendererDispatcher
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // MutableBlockPos - position
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_POSITION.getName(), "Lnet/minecraft/util/math/BlockPos$MutableBlockPos;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_lvt_9_1_visGraph)); // visGraph
		// tempInstructionList.add(new VarInsnNode(ALOAD, 13)); // blockPos
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_blockpos$mutableblockpos)); // blockPos

		// Inject our instructions right BEFORE the Label for the "ALOAD ?: block" instruction
		instructions.insertBefore(ALOAD_block_Node, tempInstructionList);

		tempInstructionList.add(new MethodInsnNode(INVOKESTATIC, "cadiboo/renderchunkrebuildchunkhooks/hooks/RenderChunkRebuildChunkHooksHooks", "canBlockRenderInLayer",
			"(Lnet/minecraft/client/renderer/chunk/RenderChunk;Lnet/minecraft/world/ChunkCache;Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;Lnet/minecraft/client/renderer/chunk/CompiledChunk;Lnet/minecraft/client/renderer/BlockRendererDispatcher;Lnet/minecraft/util/math/BlockPos$MutableBlockPos;Lnet/minecraft/client/renderer/chunk/VisGraph;Lnet/minecraft/util/math/BlockPos$MutableBlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockRenderLayer;)Z",
			false));

		// Inject our instructions right AFTER the Label for the "ALOAD ?: blockrenderlayer1" instruction
		instructions.insert(ALOAD_blockrenderlayer1_Node, tempInstructionList);

		// Remove the call to block.canRenderInLayer()
		instructions.remove(INVOKEVIRTUAL_Block_canRenderInLayer_Node);

		return true;
	}

	/**
	 * find "blockrendererdispatcher.renderBlock(iblockstate, blockpos$mutableblockpos, this.worldView, bufferbuilder);"<br>
	 * "INVOKEVIRTUAL net/minecraft/client/renderer/BlockRendererDispatcher.renderBlock (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z"<br>
	 * get return label<br>
	 * get line number for nice debug<br>
	 * inject before<br>
	 *
	 * @param instructions the instructions for the method
	 */
	public boolean injectRebuildChunkBlockEventHook(InsnList instructions) {

		MethodInsnNode INVOKEVIRTUAL_BlockRendererDispatcher_renderBlock_Node = null;

		// Find the bytecode instruction for "BlockRendererDispatcher.renderBlock" ("INVOKEVIRTUAL net/minecraft/client/renderer/BlockRendererDispatcher.renderBlock (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z")
		for (AbstractInsnNode instruction : instructions.toArray()) {

			if (instruction.getOpcode() == INVOKEVIRTUAL) {
				if (instruction.getType() == AbstractInsnNode.METHOD_INSN) {
					//todo check desc etc.
					if (((MethodInsnNode) instruction).name.equals(BLOCK_RENDERER_DISPATCHER_RENDER_BLOCK.getName())) {
						INVOKEVIRTUAL_BlockRendererDispatcher_renderBlock_Node = (MethodInsnNode) instruction;
						break;
					}
				}
			}

		}

		if (INVOKEVIRTUAL_BlockRendererDispatcher_renderBlock_Node == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		LabelNode preExistingLabelNode = null;

		// go back up the instructions until we find the Label for the "INVOKEVIRTUAL net/minecraft/client/renderer/BlockRendererDispatcher.renderBlock (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z" instruction
		for (int i = instructions.indexOf(INVOKEVIRTUAL_BlockRendererDispatcher_renderBlock_Node) - 1; i >= 0; i--) {
			if (instructions.get(i).getType() != AbstractInsnNode.LABEL) {
				continue;
			}
			preExistingLabelNode = (LabelNode) instructions.get(i);
			break;
		}

		if (preExistingLabelNode == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		VarInsnNode preExistingVarInsNode = null;

		// go back down the instructions until we find the first VarInsnNode instruction
		for (int i = instructions.indexOf(preExistingLabelNode); i < instructions.indexOf(INVOKEVIRTUAL_BlockRendererDispatcher_renderBlock_Node); i++) {
			if (instructions.get(i).getType() != AbstractInsnNode.VAR_INSN) {
				continue;
			}
			preExistingVarInsNode = (VarInsnNode) instructions.get(i);
			break;
		}

		if (preExistingVarInsNode == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		LabelNode returnLabel = null;

		// go back down the instructions until we find the LabelNode for all the following instructions
		for (int i = instructions.indexOf(INVOKEVIRTUAL_BlockRendererDispatcher_renderBlock_Node); i < (instructions.indexOf(INVOKEVIRTUAL_BlockRendererDispatcher_renderBlock_Node) + 25); i++) {
			if (instructions.get(i).getType() != AbstractInsnNode.LABEL) {
				continue;
			}
			returnLabel = (LabelNode) instructions.get(i);
			break;
		}

		if (returnLabel == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		final InsnList tempInstructionList = new InsnList();

		/**
		 * @param renderChunk                     the instance of {@link RenderChunk} the event is being fired for
		 * @param renderGlobal                    the {@link RenderGlobal} passed in from RenderChunk#rebuildChunk
		 * @param worldView                       the {@link ChunkCache} passed in from RenderChunk#rebuildChunk
		 * @param generator                       the {@link ChunkCompileTaskGenerator} passed in from RenderChunk#rebuildChunk
		 * @param compiledChunk                   the {@link CompiledChunk} passed in from RenderChunk#rebuildChunk
		 * @param blockRendererDispatcher         the {@link BlockRendererDispatcher} passed in from RenderChunk#rebuildChunk
		 * @param blockState                      the {@link IBlockState state} of the block being rendered
		 * @param blockPos                        the {@link BlockPos.MutableBlockPos position} of the block being rendered
		 * @param bufferBuilder                   the {@link BufferBuilder} for the BlockRenderLayer
		 * @param renderChunkPosition             the {@link BlockPos.MutableBlockPos position} passed in from RenderChunk#rebuildChunk
		 * @param usedBlockRenderLayers           the array of {@link BlockRenderLayer} that are being used
		 * @param blockRenderLayer                the {@link BlockRenderLayer} of the block being rendered
		 * @param x                               the translation X passed in from RenderChunk#rebuildChunk
		 * @param y                               the translation Y passed in from RenderChunk#rebuildChunk
		 * @param z                               the translation Z passed in from RenderChunk#rebuildChunk
		 * @param tileEntitiesWithGlobalRenderers the {@link HashSet} of {@link TileEntity TileEntities} with global renderers passed in from RenderChunk#rebuildChunk
		 * @param visGraph                        the {@link VisGraph} passed in from RenderChunk#rebuildChunk
		 *
		 * @return If the block should NOT be rebuilt to the chunk by vanilla
		 *
		 * @see cadiboo.renderchunkrebuildchunkhooks.core.util.rebuildChunk_diff and cadiboo.renderchunkrebuildchunkhooks.core.util.rebuildChunkOptifine_diff
		 */

		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // this
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // renderGlobal
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_RENDER_GLOBAL.getName(), "Lnet/minecraft/client/renderer/RenderGlobal;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // worldView
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_WORLD_VIEW.getName(), "Lnet/minecraft/world/ChunkCache;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_generator)); // generator
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_compiledchunk)); // compiledchunk
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_blockrendererdispatcher)); // blockrendererdispatcher
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_iblockstate)); // iblockstate
		//		tempInstructionList.add(new VarInsnNode(ALOAD, 13)); // blockpos$mutableblockpos (currentBlockPos)
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_blockpos$mutableblockpos)); // blockpos$mutableblockpos (currentBlockPos)
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_bufferbuilder)); // bufferbuilder
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // position
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_POSITION.getName(), "Lnet/minecraft/util/math/BlockPos$MutableBlockPos;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_aboolean)); // aboolean
		//		tempInstructionList.add(new VarInsnNode(ALOAD, 17)); // blockrenderlayer1
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_blockrenderlayer1)); // blockrenderlayer1
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_x)); // x
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_y)); // y
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_z)); // z
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_lvt_10_1_tileEntitiesWithGlobalRenderers)); // lvt_10_1_ (tileEntitiesWithGlobalRenderers)
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_lvt_9_1_visGraph)); // lvt_9_1_ (visGraph)
		tempInstructionList.add(new MethodInsnNode(INVOKESTATIC, "cadiboo/renderchunkrebuildchunkhooks/hooks/RenderChunkRebuildChunkHooksHooks", "onRebuildChunkBlockEvent",
			"(Lnet/minecraft/client/renderer/chunk/RenderChunk;Lnet/minecraft/client/renderer/RenderGlobal;Lnet/minecraft/world/ChunkCache;Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;Lnet/minecraft/client/renderer/chunk/CompiledChunk;Lnet/minecraft/client/renderer/BlockRendererDispatcher;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos$MutableBlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/util/math/BlockPos$MutableBlockPos;[ZLnet/minecraft/util/BlockRenderLayer;FFFLjava/util/HashSet;Lnet/minecraft/client/renderer/chunk/VisGraph;)Z",
			false));
		tempInstructionList.add(new JumpInsnNode(IFNE, returnLabel));

		// Inject our instructions right BEFORE first VarsInsNode
		instructions.insertBefore(preExistingVarInsNode, tempInstructionList);

		return true;
	}

	/**
	 * find last return statement in method<br>
	 * inject before<br>
	 *
	 * @param instructions the instructions for the method
	 */
	@Override
	public boolean injectRebuildChunkPostEventHook(InsnList instructions) {

		InsnNode RETURN_Node = null;

		//Iterate backwards over the instructions to get the last return statement in the method
		for (int i = instructions.size() - 1; i > 0; i--) {
			AbstractInsnNode instruction = instructions.get(i);

			if (instruction.getOpcode() == RETURN) {
				if (instruction.getType() == AbstractInsnNode.INSN) {
					RETURN_Node = (InsnNode) instruction;
					break;
				}
			}

		}

		if (RETURN_Node == null) {
			new RuntimeException("Couldn't find injection point!").printStackTrace();
			return false;
		}

		final InsnList tempInstructionList = new InsnList();

		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // this
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_x));
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_y));
		tempInstructionList.add(new VarInsnNode(FLOAD, ALOAD_z));
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_generator));
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_compiledchunk));
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // position
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_POSITION.getName(), "Lnet/minecraft/util/math/BlockPos$MutableBlockPos;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // renderGlobal
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_RENDER_GLOBAL.getName(), "Lnet/minecraft/client/renderer/RenderGlobal;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // worldView
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_WORLD_VIEW.getName(), "Lnet/minecraft/world/ChunkCache;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, ALOAD_lvt_9_1_visGraph));
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // setTileEntities
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_SET_TILE_ENTITIES.getName(), "Ljava/util/Set;"));
		tempInstructionList.add(new VarInsnNode(ALOAD, 0)); // lockCompileTask
		tempInstructionList.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/chunk/RenderChunk", RENDER_CHUNK_LOCK_COMPILE_TASK.getName(), "Ljava/util/concurrent/locks/ReentrantLock;"));
		tempInstructionList.add(new MethodInsnNode(INVOKESTATIC, "cadiboo/renderchunkrebuildchunkhooks/hooks/RenderChunkRebuildChunkHooksHooks", "onRebuildChunkPostEvent",
			"(Lnet/minecraft/client/renderer/chunk/RenderChunk;FFFLnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;Lnet/minecraft/client/renderer/chunk/CompiledChunk;Lnet/minecraft/util/math/BlockPos$MutableBlockPos;Lnet/minecraft/client/renderer/RenderGlobal;Lnet/minecraft/world/ChunkCache;Lnet/minecraft/client/renderer/chunk/VisGraph;Ljava/util/Set;Ljava/util/concurrent/locks/ReentrantLock;)V", false));

		// Inject our instructions right BEFORE the RETURN instruction
		instructions.insertBefore(RETURN_Node, tempInstructionList);

		return true;
	}

}
