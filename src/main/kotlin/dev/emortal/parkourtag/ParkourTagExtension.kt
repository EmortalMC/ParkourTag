package dev.emortal.parkourtag

import dev.emortal.immortal.config.ConfigHelper
import dev.emortal.immortal.config.GameOptions
import dev.emortal.immortal.game.GameManager
import dev.emortal.immortal.game.WhenToRegisterEvents
import dev.emortal.parkourtag.game.ParkourTagGame
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.extensions.Extension
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.stream.Collectors
import kotlin.io.path.nameWithoutExtension

class ParkourTagExtension : Extension() {

    companion object {
        lateinit var config: ParkourConfig
    }

    override fun initialize() {
        val maps = Files.list(Path.of("./maps/parkourtag/")).map { it.nameWithoutExtension }.collect(Collectors.toSet())
        logger.info("Found ${maps.size} maps:\n- ${maps.joinToString("\n- ")}")

        val parkourConfig = ParkourConfig()
        val mapConfigMap = mutableMapOf<String, MapConfig>()

        maps.forEach {
            mapConfigMap[it] = MapConfig()
        }

        parkourConfig.mapSpawnPositions = mapConfigMap
        config = ConfigHelper.initConfigFile(Path.of("./parkour.json"), parkourConfig)

        GameManager.registerGame<ParkourTagGame>(
            "hideandseek",
            Component.text("Hide and Seek", NamedTextColor.GREEN, TextDecoration.BOLD),
            true,
            true,
            WhenToRegisterEvents.NEVER,
            GameOptions(
                minPlayers = 2,
                maxPlayers = 8,
                countdownSeconds = 15,
                canJoinDuringGame = false,
                showScoreboard = true,
                showsJoinLeaveMessages = true,
            )
        )

        logger.info("[ParkourTag] Initialized!")
    }

    override fun terminate() {
        logger.info("[ParkourTag] Terminated!")
    }

}