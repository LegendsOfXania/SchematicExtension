package fr.legendsofxania.structure.interaction.content

import com.typewritermc.core.entries.Query
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.ContentMode
import com.typewritermc.engine.paper.content.components.bossBar
import com.typewritermc.engine.paper.content.components.exit
import com.typewritermc.engine.paper.content.entryId
import fr.legendsofxania.structure.entry.static.template.StructureTemplateEntry
import fr.legendsofxania.structure.util.BoundingBoxViewer
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import java.time.Duration

class StructureTemplateContentMode(
    context: ContentContext,
    player: Player
) : ContentMode(context, player) {
    private var boundingBoxViewer: BoundingBoxViewer? = null
    private var tickCounter = 0

    override suspend fun setup(): Result<Unit> {
        bossBar {
            title = "<aqua>Structure Template Mode</aqua>"
            color = BossBar.Color.RED
            progress = 1.0f
        }

        val entryId = context.entryId
            ?: return failure("Entry ID not found in context.")
        val entry = Query.findById<StructureTemplateEntry>(entryId)
            ?: return failure("RoomTemplateEntry not found for ID: $entryId")

        val selectionTool = StructureSelectionTool(entry) { corner1, corner2 ->
            updateBoundingBoxViewer(corner1, corner2)
        }
        +selectionTool

        exit()
        return ok(Unit)
    }

    override suspend fun tick(deltaTime: Duration) {
        tickCounter++

        if (tickCounter == 10) {
            boundingBoxViewer?.drawnBox()
            tickCounter = 0
        }
    }

    private fun updateBoundingBoxViewer(corner1: Location?, corner2: Location?) {
        if (corner1 != null && corner2 != null) {
            val minX = minOf(corner1.x, corner2.x)
            val minY = minOf(corner1.y, corner2.y)
            val minZ = minOf(corner1.z, corner2.z)
            val maxX = maxOf(corner1.x, corner2.x) + 1
            val maxY = maxOf(corner1.y, corner2.y) + 1
            val maxZ = maxOf(corner1.z, corner2.z) + 1

            val boundingBox = BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
            boundingBoxViewer = BoundingBoxViewer(player, boundingBox)
        } else {
            boundingBoxViewer = null
        }
    }
}