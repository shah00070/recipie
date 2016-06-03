package in.creativebucket.recipesadda.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chandan Kumar on 9/13/2015.
 */
public class RecipeData implements Parcelable {

    public String recipeId;
    public String recipeName;
    public String imageUrl;
    public String videoId;
    public String ingredients;
    public String preparation;

    public RecipeData(String recipeId, String recipeName, String imageUrl, String videoId, String ingredients, String preparation) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.imageUrl = imageUrl;
        this.videoId = videoId;
        this.ingredients = ingredients;
        this.preparation = preparation;
    }

    public RecipeData(String recipeName, String imageUrl, String ingredients, String preparation) {
        this.recipeName = recipeName;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.preparation = preparation;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    // Parcelling part
    public RecipeData(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);

        this.recipeId = data[0];
        this.recipeName = data[1];
        this.imageUrl = data[2];
        this.videoId = data[3];
        this.ingredients = data[4];
        this.preparation = data[5];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.recipeId,
                this.recipeName, this.imageUrl, this.videoId, this.ingredients,
                this.preparation});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RecipeData createFromParcel(Parcel in) {
            return new RecipeData(in);
        }

        public RecipeData[] newArray(int size) {
            return new RecipeData[size];
        }
    };
}
