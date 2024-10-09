package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

import data.RecipeFileHandler;

public class RecipeUI {
    private BufferedReader reader;
    private RecipeFileHandler fileHandler;

    public RecipeUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileHandler = new RecipeFileHandler();
    }

    public RecipeUI(BufferedReader reader, RecipeFileHandler fileHandler) {
        this.reader = reader;
        this.fileHandler = fileHandler;
    }

    public void displayMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        // 設問1: 一覧表示機能
                        this.displayRecipes();
                        break;
                    case "2":
                        // 設問2: 新規登録機能
                        this.addNewRecipe();
                        break;
                    case "3":
                        // 設問3: 検索機能
                        this.searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exit the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    /**
     * 設問1: 一覧表示機能
     * RecipeFileHandlerから読み込んだレシピデータを整形してコンソールに表示します。
     */
    private void displayRecipes() {
        // RecipeFileHandler を使用してレシピデータを読み込み
        ArrayList<String> recipes = fileHandler.readRecipes();

        System.out.println();
        // レシピが空の場合
        if (recipes.isEmpty()) {
            System.out.println("No recipes available.");
        } else {
            // レシピ出力
            System.out.println("Recipes:");
            System.out.println("-----------------------------------");
            for (String recipe : recipes) {
                String[] keyValue = recipe.split(",", 2);
                System.out.println("Recipe Name: " + keyValue[0]);
                System.out.println("Main Ingredients: " + keyValue[1]);
                System.out.println("-----------------------------------");
            }
        }
    }

    /**
     * 設問2: 新規登録機能
     * ユーザーからレシピ名と主な材料を入力させ、RecipeFileHandlerを使用してrecipes.txtに新しいレシピを追加します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void addNewRecipe() throws IOException {

        String recipeName;
        String ingredients;

        // レシピ名入力
        System.out.print("Enter recipe name: ");
        recipeName = reader.readLine();

        // 材料入力
        System.out.print("Enter main ingredients (comma separated): ");
        ingredients = reader.readLine();

        // ファイルにレシピデータを追加
        fileHandler.addRecipe(recipeName, ingredients);

        System.out.println("Recipe added successfully.");
    }

    /**
     * 設問3: 検索機能
     * ユーザーから検索クエリを入力させ、そのクエリに基づいてレシピを検索し、一致するレシピをコンソールに表示します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void searchRecipe() throws IOException {
        // クエリ入力受付
        System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'): ");
        String query = reader.readLine();

        String queryName = "";
        String queryIngredient = "";
        // クエリから検索ワードを抽出
        if (query.contains("&")) {
            queryName = query.split("&")[0].split("=")[1];
            queryIngredient = query.split("&")[1].split("=")[1];
        } else {
            // クエリが name or ingredient のみの場合
            String[] keyValue = query.split("=");
            switch (keyValue[0]) {
                case "name":
                    queryName = keyValue[1];
                    break;
                case "ingredient":
                    queryIngredient = keyValue[1];
            }
        }

        // レシピデータ読み込み
        ArrayList<String> recipes = fileHandler.readRecipes();
        // 一致するレシピを格納
        ArrayList<String> matchRecipes = new ArrayList<>();

        // 一致するレシピがあるか検索
        for (String recipe : recipes) {
            String[] keyValue = recipe.split(",", 2);
            // レシピ名が一致
            if (keyValue[0].contains(queryName)) {
                // 食材が一致するか判定
                String[] ingredients = keyValue[1].split(", ");
                for (String ingredient : ingredients) {
                    // 食材が一致
                    if (ingredient.contains(queryIngredient)) {
                        matchRecipes.add(recipe);
                    }
                }
            }
        }

        // 検索結果表示
        System.out.println("Search Results:");
        // 一致するレシピがない場合
        if (matchRecipes.isEmpty()) {
            System.out.println("No recipes found matching the criteria.");
        } else {
            // 一致するレシピがある場合
            for (String matchRecipe : matchRecipes) {
                System.out.println(matchRecipe);
            }
        }

    }

}
