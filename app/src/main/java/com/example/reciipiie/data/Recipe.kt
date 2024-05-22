package com.example.reciipiie.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: Int = 0,
    var favourite: Boolean = false,
    val title: String = "",
    val image: String = "",
    val imageType: String = "",
    val servings: Int = 0,
    val readyInMinutes: Int = 0,
    val healthScore: Double = 0.0,
    val pricePerServing: Double = 0.0,
    val analyzedInstructions: List<Instruction> = emptyList(), // Include analyzedInstructions here
    val cheap: Boolean = false,
    val creditsText: String = "",
    val cuisines: List<String> = emptyList(),
    val dairyFree: Boolean = false,
    val diets: List<String> = emptyList(),
    val gaps: String = "",
    val glutenFree: Boolean = false,
    val instructions: String = "",
    val ketogenic: Boolean = false,
    val vegan: Boolean = false,
    val vegetarian: Boolean = false,
    val veryHealthy: Boolean = false,
    val veryPopular: Boolean = false,
    val dishTypes: List<String> = emptyList(),
    val extendedIngredients: List<ExtendedIngredient> = emptyList(),
    val summary: String = "",
    val winePairing: WinePairing = WinePairing()
) : Parcelable {
    @Parcelize
    data class ExtendedIngredient(
        val aisle: String = "",
        val amount: Double = 0.0,
        val consistency: String = "",
        val id: Int = 0,
        val image: String = "",
        val measures: Measures = Measures(),
        val meta: List<String> = emptyList(),
        val name: String = "",
        val original: String = "",
        val originalName: String = "",
        val unit: String = ""
    ) : Parcelable {
        @Parcelize
        data class Measures(
            val metric: Measure = Measure(),
            val us: Measure = Measure()
        ) : Parcelable {
            @Parcelize
            data class Measure(
                val amount: Double = 0.0,
                val unitLong: String = "",
                val unitShort: String = ""
            ) : Parcelable
        }
    }

    @Parcelize
    data class Instruction(
        val name: String? = null,
        val steps: List<Step> = emptyList()
    ) : Parcelable {
        @Parcelize
        data class Step(
            val number: Int = 0,
            val step: String = "",
            val ingredients: List<Ingredient> = emptyList(),
            val equipment: List<Equipment> = emptyList(),
            val length: Length? = null
        ) : Parcelable {
            @Parcelize
            data class Ingredient(
                val id: Int = 0,
                val name: String = "",
                val localizedName: String = "",
                val image: String = ""
            ) : Parcelable

            @Parcelize
            data class Equipment(
                val id: Int = 0,
                val name: String = "",
                val localizedName: String = "",
                val image: String = ""
            ) : Parcelable

            @Parcelize
            data class Length(
                val number: Int = 0,
                val unit: String = ""
            ) : Parcelable
        }
    }

    @Parcelize
    data class WinePairing(
        val pairedWines: List<String> = emptyList(),
        val pairingText: String = "",
        val productMatches: List<ProductMatch> = emptyList()
    ) : Parcelable {
        @Parcelize
        data class ProductMatch(
            val id: Int = 0,
            val title: String = "",
            val description: String = "",
            val price: String = "",
            val imageUrl: String = "",
            val averageRating: Double = 0.0,
            val ratingCount: Int = 0,
            val score: Double = 0.0,
            val link: String = ""
        ) : Parcelable
    }
}

data class RecipeResponse(
    val recipes: List<Recipe>?
)

@Parcelize
data class SearchResult(
    val id: Int,
    val title: String,
    val imageType: String
) : Parcelable
