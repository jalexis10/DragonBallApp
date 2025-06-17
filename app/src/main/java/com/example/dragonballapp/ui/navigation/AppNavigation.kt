package com.example.dragonballapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dragonballapp.ui.screen.*

object Routes {
    const val CHARACTERS = "characters"
    const val CHARACTER_DETAIL = "character_detail"

    const val CHARACTER_ARG_ID = "characterId"

    const val PLANETS = "planets"
    const val PLANET_DETAIL = "planet_detail"
    const val PLANET_ARG_ID = "planetId"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.CHARACTERS) {

        // Lista de personajes
        composable(Routes.CHARACTERS) {
            CharacterListScreen(
                onCharacterClick = { characterId ->

                    navController.navigate("${Routes.CHARACTER_DETAIL}/$characterId")
                },
                onPlanetSectionClick = {
                    navController.navigate(Routes.PLANETS)
                }
            )
        }


        composable(
            route = "${Routes.CHARACTER_DETAIL}/{${Routes.CHARACTER_ARG_ID}}",
            arguments = listOf(navArgument(Routes.CHARACTER_ARG_ID) { type = NavType.IntType })
        ) { backStackEntry ->

            val characterId = backStackEntry.arguments?.getInt(Routes.CHARACTER_ARG_ID)



            if (characterId != null) {
                CharacterDetailScreen(characterId = characterId)
            } else {

            }
        }


        composable(Routes.PLANETS) {
            PlanetListScreen(
                onPlanetClick = { planet ->
                    navController.navigate("${Routes.PLANET_DETAIL}/${planet.id}")
                }
            )
        }


        composable(
            route = "${Routes.PLANET_DETAIL}/{${Routes.PLANET_ARG_ID}}",
            arguments = listOf(navArgument(Routes.PLANET_ARG_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val planetId = backStackEntry.arguments?.getInt(Routes.PLANET_ARG_ID)
            if (planetId != null) {
                PlanetDetailScreen(
                    planetId = planetId,
                    onCharacterClick = { characterId ->
                        navController.navigate("${Routes.CHARACTER_DETAIL}/$characterId")
                    }
                )
            }
        }
    }
}