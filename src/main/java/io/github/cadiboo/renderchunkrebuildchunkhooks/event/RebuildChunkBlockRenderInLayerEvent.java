package io.github.cadiboo.renderchunkrebuildchunkhooks.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;

/**
 * Called when a {@link net.minecraft.client.renderer.chunk.RenderChunk#rebuildChunk RenderChunk.rebuildChunk} is called.<br>
 * This event is fired on the {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS EVENT_BUS} for every block inside the chunk to be rebuilt and for every {@link net.minecraft.util.BlockRenderLayer BlockRenderLayer} the block could render in.<br>
 * Setting the result of this event to {@link net.minecraftforge.fml.common.eventhandler.Event.Result#DENY} prevents the parts of the block in this {@link net.minecraft.util.BlockRenderLayer BlockRenderLayer} from being rebuilt to the chunk (and therefore rendered).<br>
 * You should not perform your own rendering in this event. Perform your rendering in the {@link RebuildChunkBlockEvent}<br>
 * Cancel the event to stop mods further down the listener list from receiving the event
 *
 * @author Cadiboo
 * @see net.minecraft.client.renderer.chunk.RenderChunk#rebuildChunk(float, float, float, ChunkCompileTaskGenerator)
 */
@HasResult
@Cancelable
public class RebuildChunkBlockRenderInLayerEvent extends Event {

	private final RenderChunk renderChunk;
	private final ChunkCache chunkCache;
	private final ChunkCompileTaskGenerator generator;
	private final CompiledChunk compiledchunk;
	private final BlockRendererDispatcher blockRendererDispatcher;
	private final MutableBlockPos renderChunkPosition;
	private final VisGraph visGraph;
	private final MutableBlockPos blockPos;
	private final IBlockState blockState;
	private final BlockRenderLayer blockRenderLayer;

	/**
	 * @param renderChunk               the instance of {@link RenderChunk} the event is being fired for
	 * @param chunkCache                the {@link ChunkCache} passed in from RenderChunk#rebuildChunk
	 * @param chunkCompileTaskGenerator the {@link ChunkCompileTaskGenerator} passed in from RenderChunk#rebuildChunk
	 * @param compiledchunk             the {@link CompiledChunk} passed in from RenderChunk#rebuildChunk
	 * @param blockRendererDispatcher   the {@link BlockRendererDispatcher} passed in from RenderChunk#rebuildChunk
	 * @param renderChunkPosition       the {@link MutableBlockPos position} passed in from RenderChunk#rebuildChunk
	 * @param visGraph                  the {@link VisGraph} passed in from RenderChunk#rebuildChunk
	 * @param blockPos                  the {@link MutableBlockPos position} of the block being assessed
	 * @param blockState                the {@link IBlockState state} of the block being assessed
	 * @param blockRenderLayer          the {@link BlockRenderLayer} of the block being assessed
	 */
	public RebuildChunkBlockRenderInLayerEvent(final RenderChunk renderChunk, final ChunkCache chunkCache, final ChunkCompileTaskGenerator chunkCompileTaskGenerator, final CompiledChunk compiledchunk, final BlockRendererDispatcher blockRendererDispatcher, final MutableBlockPos renderChunkPosition, final VisGraph visGraph, final MutableBlockPos blockPos, final IBlockState blockState, final BlockRenderLayer blockRenderLayer) {
		this.renderChunk = renderChunk;
		this.chunkCache = chunkCache;
		this.generator = chunkCompileTaskGenerator;
		this.compiledchunk = compiledchunk;
		this.blockRendererDispatcher = blockRendererDispatcher;
		this.renderChunkPosition = renderChunkPosition;
		this.visGraph = visGraph;
		this.blockPos = blockPos;
		this.blockState = blockState;
		this.blockRenderLayer = blockRenderLayer;
	}

	/**
	 * @return the instance of {@link RenderChunk} the event is being fired for
	 */
	public RenderChunk getRenderChunk() {
		return this.renderChunk;
	}

	/**
	 * @return the {@link ChunkCache} passed in
	 */
	public ChunkCache getChunkCache() {
		return this.chunkCache;
	}

	/**
	 * @return the {@link ChunkCompileTaskGenerator} passed in
	 */
	public ChunkCompileTaskGenerator getGenerator() {
		return this.generator;
	}

	/**
	 * @return the {@link CompiledChunk} passed in
	 */
	public CompiledChunk getCompiledChunk() {
		return this.compiledchunk;
	}

	/**
	 * @return the {@link BlockRendererDispatcher} passed in
	 */
	public BlockRendererDispatcher getBlockRendererDispatcher() {
		return this.blockRendererDispatcher;
	}

	/**
	 * @return the position passed in
	 */
	public MutableBlockPos getRenderChunkPosition() {
		return this.renderChunkPosition;
	}

	/**
	 * @return the {@link VisGraph} passed in
	 */
	public VisGraph getVisGraph() {
		return this.visGraph;
	}

	/**
	 * @return the position of the block passed in
	 */
	public MutableBlockPos getBlockPos() {
		return this.blockPos;
	}

	/**
	 * @return the {@link IBlockState state} of the block passed in
	 */
	public IBlockState getBlockState() {
		return this.blockState;
	}

	/**
	 * @return the {@link BlockRenderLayer} of the block passed in
	 */
	public BlockRenderLayer getBlockRenderLayer() {
		return this.blockRenderLayer;
	}

}