import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {
    // ===== INTERFACES & ABSTRACT CLASSES =====
    interface DietPlan {
        double[] getMacroTargets();
        String getPlanName();
        String getDescription();
    }
    
    interface Trackable {
        void addEntry(LocalDate date, NutritionInfo info);
        NutritionInfo getEntry(LocalDate date);
        Map<LocalDate, NutritionInfo> getAllEntries();
    }
    
    interface Reportable {
        String generateReport();
    }
    
    // Food with nutrition info (per 100g)
    static class Food {
        private String name;
        private double calories, protein, carbs, fat;
        private String category;
        private boolean isVegetarian;
        
        Food(String name, double calories, double protein, double carbs, double fat, String category, boolean isVeg) {
            this.name = name;
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
            this.category = category;
            this.isVegetarian = isVeg;
        }
        
        // Getters
        String getName() { return name; }
        double getCalories() { return calories; }
        double getProtein() { return protein; }
        double getCarbs() { return carbs; }
        double getFat() { return fat; }
        String getCategory() { return category; }
        boolean isVegetarian() { return isVegetarian; }
    }
    
    // Food item with quantity
    static class FoodItem {
        private Food food;
        private double grams;
        private LocalDate dateConsumed;
        
        FoodItem(Food food, double grams, LocalDate date) {
            this.food = food;
            this.grams = grams;
            this.dateConsumed = date;
        }
        
        double getCalories() { return food.getCalories() * grams / 100; }
        double getProtein() { return food.getProtein() * grams / 100; }
        double getCarbs() { return food.getCarbs() * grams / 100; }
        double getFat() { return food.getFat() * grams / 100; }
        
        Food getFood() { return food; }
        double getGrams() { return grams; }
        LocalDate getDateConsumed() { return dateConsumed; }
    }
    
    // Nutrition totals
    static class NutritionInfo implements Comparable<NutritionInfo> {
        private double calories, protein, carbs, fat;
        
        NutritionInfo(double cal, double prot, double carb, double fat) {
            this.calories = cal;
            this.protein = prot;
            this.carbs = carb;
            this.fat = fat;
        }
        
        // Getters
        double getCalories() { return calories; }
        double getProtein() { return protein; }
        double getCarbs() { return carbs; }
        double getFat() { return fat; }
        
        // Calculate macro percentages
        double getProteinPercentage() { return (protein * 4) / calories * 100; }
        double getCarbsPercentage() { return (carbs * 4) / calories * 100; }
        double getFatPercentage() { return (fat * 9) / calories * 100; }
        
        double getTotalMacroGrams() { return protein + carbs + fat; }
        
        @Override
        public int compareTo(NutritionInfo other) {
            return Double.compare(this.calories, other.calories);
        }
    
    // USER PROFILE WITH PERSONALIZATION
    static class UserProfile {
        private String name;
        private double height, weight, age;
        private String gender;
        private String activityLevel;
        private DietPlan dietPlan;
        private Set<String> allergies;
        private List<String> preferences;
        
        UserProfile(String name, double height, double weight, double age, String gender, String activityLevel) {
            this.name = name;
            this.height = height;
            this.weight = weight;
            this.age = age;
            this.gender = gender;
            this.activityLevel = activityLevel;
            this.allergies = new HashSet<>();
            this.preferences = new ArrayList<>();
            this.dietPlan = new BalancedDiet();
        }
        
        double getBMI() {
            return weight / (height * height);
        }
        
        double getCalorieRequirement() {
            double bmr = gender.equalsIgnoreCase("M") ? 
                88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age) :
                447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
            
            double multiplier = switch(activityLevel.toLowerCase()) {
                case "sedentary" -> 1.2;
                case "light" -> 1.375;
                case "moderate" -> 1.55;
                case "very_active" -> 1.725;
                case "extra_active" -> 1.9;
                default -> 1.55;
            };
            return bmr * multiplier;
        }
        
        void addAllergy(String allergy) { allergies.add(allergy.toLowerCase()); }
        void addPreference(String pref) { preferences.add(pref.toLowerCase()); }
        void setDietPlan(DietPlan plan) { this.dietPlan = plan; }
        
        String getName() { return name; }
        double getWeight() { return weight; }
        DietPlan getDietPlan() { return dietPlan; }
        Set<String> getAllergies() { return allergies; }
        List<String> getPreferences() { return preferences; }
    }
    
    // DIET PLAN IMPLEMENTATIONS
    static class BalancedDiet implements DietPlan {
        @Override
        public double[] getMacroTargets() {
            return new double[]{0.3, 0.4, 0.3}; // 30% protein, 40% carbs, 30% fat
        }
        
        @Override
        public String getPlanName() { return "Balanced Diet"; }
        
        @Override
        public String getDescription() { return "Equal macronutrient distribution for overall health"; }
    }
    
    static class LowCarbDiet implements DietPlan {
        @Override
        public double[] getMacroTargets() {
            return new double[]{0.4, 0.2, 0.4}; // 40% protein, 20% carbs, 40% fat
        }
        
        @Override
        public String getPlanName() { return "Low-Carb Diet"; }
        
        @Override
        public String getDescription() { return "Reduced carbohydrates for weight management"; }
    }
    
    static class HighProteinDiet implements DietPlan {
        @Override
        public double[] getMacroTargets() {
            return new double[]{0.4, 0.35, 0.25}; // 40% protein, 35% carbs, 25% fat
        }
        
        @Override
        public String getPlanName() { return "High-Protein Diet"; }
        
        @Override
        public String getDescription() { return "Enhanced protein for muscle building"; }
    }
    
    static class VegetarianDiet implements DietPlan {
        @Override
        public double[] getMacroTargets() {
            return new double[]{0.25, 0.5, 0.25}; // 25% protein, 50% carbs, 25% fat
        }
        
        @Override
        public String getPlanName() { return "Vegetarian Diet"; }
        
        @Override
        public String getDescription() { return "Plant-based nutrition plan"; }
    }
    
    // Single meal
    static class Meal implements Reportable {
        private String name;
        private List<FoodItem> foods;
        
        Meal(String name) {
            this.name = name;
            this.foods = new ArrayList<>();
        }
        
        void addFood(Food food, double grams, LocalDate date) {
            foods.add(new FoodItem(food, grams, date));
        }
        
        NutritionInfo getTotalNutrition() {
            double cal = 0, prot = 0, carb = 0, fat = 0;
            for(FoodItem fi : foods) {
                cal += fi.getCalories();
                prot += fi.getProtein();
                carb += fi.getCarbs();
                fat += fi.getFat();
            }
            return new NutritionInfo(cal, prot, carb, fat);
        }
        
        void removeFood(int index) {
            if(index >= 0 && index < foods.size()) {
                foods.remove(index);
            }
        }
        
        String getMealName() { return name; }
        List<FoodItem> getFoods() { return new ArrayList<>(foods); }
        
        @Override
        public String generateReport() {
            NutritionInfo info = getTotalNutrition();
            return String.format("%s: %.0f cal | %.1fg prot | %.1fg carbs | %.1fg fat", 
                name, info.getCalories(), info.getProtein(), info.getCarbs(), info.getFat());
        }
    }
    
    // Daily tracker for all meals
    static class DailyTracker implements Trackable, Reportable {
        private Map<String, Meal> meals;
        private LocalDate date;
        private UserProfile userProfile;
        
        DailyTracker(LocalDate date, UserProfile profile) {
            this.date = date;
            this.userProfile = profile;
            this.meals = new LinkedHashMap<>();
            meals.put("breakfast", new Meal("Breakfast"));
            meals.put("lunch", new Meal("Lunch"));
            meals.put("dinner", new Meal("Dinner"));
            meals.put("snacks", new Meal("Snacks"));
        }
        
        void addFoodToMeal(String mealType, Food food, double grams) {
            if(meals.containsKey(mealType)) {
                meals.get(mealType).addFood(food, grams, date);
            }
        }
        
        NutritionInfo getDailyTotal() {
            double cal = 0, prot = 0, carb = 0, fat = 0;
            for(Meal meal : meals.values()) {
                NutritionInfo info = meal.getTotalNutrition();
                cal += info.getCalories();
                prot += info.getProtein();
                carb += info.getCarbs();
                fat += info.getFat();
            }
            return new NutritionInfo(cal, prot, carb, fat);
        }
        
        @Override
        public void addEntry(LocalDate date, NutritionInfo info) {
            // Entry already managed through meals
        }
        
        @Override
        public NutritionInfo getEntry(LocalDate date) {
            return this.date.equals(date) ? getDailyTotal() : null;
        }
        
        @Override
        public Map<LocalDate, NutritionInfo> getAllEntries() {
            Map<LocalDate, NutritionInfo> entries = new HashMap<>();
            entries.put(date, getDailyTotal());
            return entries;
        }
        
        @Override
        public String generateReport() {
            StringBuilder report = new StringBuilder();
            report.append("\n========== DAILY REPORT (").append(date).append(") ==========\n");
            
            for(String mealType : new String[]{"breakfast", "lunch", "dinner", "snacks"}) {
                Meal meal = meals.get(mealType);
                if(!meal.getFoods().isEmpty()) {
                    report.append(meal.generateReport()).append("\n");
                }
            }
            
            NutritionInfo daily = getDailyTotal();
            report.append(String.format("\nDAILY TOTAL: %.0f cal | %.1fg protein | %.1fg carbs | %.1fg fat\n",
                daily.getCalories(), daily.getProtein(), daily.getCarbs(), daily.getFat()));
            
            return report.toString();
        }
        
        LocalDate getDate() { return date; }
        Map<String, Meal> getMeals() { return meals; }
    }
    
    // Advanced Nutrition Analyzer with personalization
    static class NutritionAnalyzer {
        static String analyzeNutrition(NutritionInfo info, UserProfile user) {
            double calTarget = user.getCalorieRequirement();
            double[] macroTargets = user.getDietPlan().getMacroTargets();
            
            double proteinTarget = (calTarget * macroTargets[0]) / 4;
            double carbTarget = (calTarget * macroTargets[1]) / 4;
            double fatTarget = (calTarget * macroTargets[2]) / 9;
            
            StringBuilder analysis = new StringBuilder();
            analysis.append("\n========== PERSONALIZED NUTRITION ANALYSIS ==========\n");
            analysis.append("Diet Plan: ").append(user.getDietPlan().getPlanName()).append("\n");
            analysis.append(user.getDietPlan().getDescription()).append("\n\n");
            
            analysis.append(String.format("Calories: %.0f / %.0f kcal (%.0f%%)\n", 
                info.getCalories(), calTarget, (info.getCalories()/calTarget)*100));
            analysis.append(String.format("Protein: %.1fg / %.1fg g (%.0f%%)\n",
                info.getProtein(), proteinTarget, (info.getProtein()/proteinTarget)*100));
            analysis.append(String.format("Carbs: %.1fg / %.1fg g (%.0f%%)\n",
                info.getCarbs(), carbTarget, (info.getCarbs()/carbTarget)*100));
            analysis.append(String.format("Fat: %.1fg / %.1fg g (%.0f%%)\n",
                info.getFat(), fatTarget, (info.getFat()/fatTarget)*100));
            
            analysis.append(String.format("\nMacro Distribution: P:%.1f%% C:%.1f%% F:%.1f%%\n",
                info.getProteinPercentage(), info.getCarbsPercentage(), info.getFatPercentage()));
            
            return analysis.toString();
        }
    }
    
    // Enhanced Diet Planner with diet-specific recommendations
    static class DietPlanner {
        static String suggestPlan(NutritionInfo info, UserProfile user) {
            DietPlan plan = user.getDietPlan();
            double calTarget = user.getCalorieRequirement();
            double[] macroTargets = plan.getMacroTargets();
            
            double proteinTarget = (calTarget * macroTargets[0]) / 4;
            double carbTarget = (calTarget * macroTargets[1]) / 4;
            double fatTarget = (calTarget * macroTargets[2]) / 9;
            
            StringBuilder recommendations = new StringBuilder();
            recommendations.append("\n========== PERSONALIZED DIET RECOMMENDATIONS ==========\n");
            recommendations.append("Plan: ").append(plan.getPlanName()).append("\n\n");
            
            if(info.getCalories() > calTarget * 1.15) {
                recommendations.append("⚠ CALORIE ALERT: Reduce portion sizes - intake is high\n");
            } else if(info.getCalories() < calTarget * 0.85) {
                recommendations.append("⚠ CALORIE ALERT: Increase meals - intake is insufficient\n");
            } else {
                recommendations.append("✓ Calorie intake is on target\n");
            }
            
            if(info.getProtein() < proteinTarget * 0.85) {
                recommendations.append("✓ Add protein sources based on your diet:\n");
                if(user.getPreferences().contains("vegetarian")) {
                    recommendations.append("  - Paneer, Dal, Tofu, Legumes, Nuts\n");
                } else {
                    recommendations.append("  - Chicken, Fish, Eggs, Legumes, Dairy\n");
                }
            }
            
            if(info.getCarbs() > carbTarget * 1.15) {
                recommendations.append("✓ Switch to complex carbs: whole grains, vegetables, oats\n");
            } else if(info.getCarbs() < carbTarget * 0.85) {
                recommendations.append("✓ Add carbohydrates: rice, bread, fruits, vegetables\n");
            }
            
            if(info.getFat() > fatTarget * 1.15) {
                recommendations.append("✓ Reduce oil and fatty foods\n");
            } else if(info.getFat() < fatTarget * 0.85) {
                recommendations.append("✓ Add healthy fats: nuts, olive oil, avocado, ghee\n");
            }
            
            return recommendations.toString();
        }
    }
    
    // Weekly Nutrition Tracker
    static class WeeklyTracker {
        private Map<LocalDate, DailyTracker> dailyTrackers;
        private UserProfile userProfile;
        
        WeeklyTracker(UserProfile profile) {
            this.userProfile = profile;
            this.dailyTrackers = new HashMap<>();
        }
        
        void addDailyTracker(LocalDate date, DailyTracker tracker) {
            dailyTrackers.put(date, tracker);
        }
        
        NutritionInfo getWeeklyAverage() {
            if(dailyTrackers.isEmpty()) return new NutritionInfo(0, 0, 0, 0);
            
            double totalCal = 0, totalProt = 0, totalCarb = 0, totalFat = 0;
            for(DailyTracker tracker : dailyTrackers.values()) {
                NutritionInfo info = tracker.getDailyTotal();
                totalCal += info.getCalories();
                totalProt += info.getProtein();
                totalCarb += info.getCarbs();
                totalFat += info.getFat();
            }
            
            int days = dailyTrackers.size();
            return new NutritionInfo(totalCal/days, totalProt/days, totalCarb/days, totalFat/days);
        }
        
        String generateWeeklyReport() {
            StringBuilder report = new StringBuilder();
            report.append("\n========== WEEKLY REPORT ==========\n");
            
            List<LocalDate> dates = new ArrayList<>(dailyTrackers.keySet());
            Collections.sort(dates);
            
            for(LocalDate date : dates) {
                NutritionInfo info = dailyTrackers.get(date).getDailyTotal();
                report.append(String.format("%s: %.0f cal | P:%.1fg C:%.1fg F:%.1fg\n",
                    date, info.getCalories(), info.getProtein(), info.getCarbs(), info.getFat()));
            }
            
            NutritionInfo avg = getWeeklyAverage();
            report.append(String.format("\nWeekly Avg: %.0f cal | P:%.1fg C:%.1fg F:%.1fg\n",
                avg.getCalories(), avg.getProtein(), avg.getCarbs(), avg.getFat()));
            
            return report.toString();
        }
    }
    
    // Enhanced Food Database with categories and filtering
    static class FoodDatabase {
        private Map<String, Food> foods;
        
        FoodDatabase() {
            this.foods = new HashMap<>();
            initializeFoods();
        }
        
        private void initializeFoods() {
            // Proteins
            addFood("chicken", "Chicken Breast", 165, 31, 0, 3.6, "Protein", false);
            addFood("fish", "Salmon", 208, 25, 0, 13, "Protein", false);
            addFood("egg", "Egg", 155, 13, 1.1, 11, "Protein", false);
            addFood("paneer", "Paneer", 265, 26, 3.6, 17, "Protein", true);
            addFood("tofu", "Tofu", 76, 8, 1.9, 4.8, "Protein", true);
            addFood("dal", "Dal (cooked)", 130, 9, 20, 0.3, "Protein", true);
            
            // Grains and Carbs
            addFood("rice", "Brown Rice", 112, 2.6, 24, 0.9, "Carbs", true);
            addFood("bread", "Whole Wheat Bread", 247, 13, 43, 2.4, "Carbs", true);
            addFood("pasta", "Pasta", 131, 5, 25, 1.1, "Carbs", true);
            addFood("oats", "Oats", 389, 17, 66, 7, "Carbs", true);
            addFood("roti", "Roti", 104, 3.5, 20, 1.3, "Carbs", true);
            
            // Vegetables
            addFood("broccoli", "Broccoli", 34, 2.8, 7, 0.4, "Vegetables", true);
            addFood("spinach", "Spinach", 23, 2.9, 3.6, 0.4, "Vegetables", true);
            addFood("carrot", "Carrot", 41, 0.9, 10, 0.2, "Vegetables", true);
            addFood("tomato", "Tomato", 18, 0.9, 3.9, 0.2, "Vegetables", true);
            
            // Fruits
            addFood("apple", "Apple", 52, 0.3, 14, 0.2, "Fruits", true);
            addFood("banana", "Banana", 89, 1.1, 23, 0.3, "Fruits", true);
            addFood("orange", "Orange", 47, 0.9, 12, 0.3, "Fruits", true);
            
            // Healthy Fats
            addFood("almonds", "Almonds", 579, 21, 22, 50, "Fats", true);
            addFood("olive_oil", "Olive Oil", 884, 0, 0, 100, "Fats", true);
            addFood("avocado", "Avocado", 160, 2, 9, 15, "Fats", true);
            
            // Dairy
            addFood("milk", "Milk", 42, 3.4, 5, 1, "Dairy", true);
            addFood("yogurt", "Yogurt", 59, 10, 3.3, 0.4, "Dairy", true);
            
            // Indian Foods
            addFood("dal_tadka", "Dal Tadka", 119, 9, 19, 2, "Indian", true);
            addFood("samosa", "Samosa", 251, 5, 32, 12, "Indian", true);
            addFood("dosa", "Dosa", 168, 3.5, 32, 2.5, "Indian", true);
            addFood("biryani", "Biryani (1 serving)", 487, 24, 52, 18, "Indian", false);
        }
        
        private void addFood(String key, String name, double cal, double prot, double carbs, double fat, String category, boolean veg) {
            foods.put(key, new Food(name, cal, prot, carbs, fat, category, veg));
        }
        
        Food getFood(String key) { return foods.get(key); }
        
        List<Food> getFoodsByCategory(String category) {
            return foods.values().stream()
                .filter(f -> f.getCategory().equalsIgnoreCase(category))
                .toList();
        }
        
        List<Food> getVegetarianFoods() {
            return foods.values().stream()
                .filter(Food::isVegetarian)
                .toList();
        }
        
        List<String> getAllFoodNames() {
            return new ArrayList<>(foods.keySet());
        }
        
        String displayAll() {
            StringBuilder display = new StringBuilder();
            Set<String> categories = foods.values().stream()
                .map(Food::getCategory)
                .collect(java.util.stream.Collectors.toSet());
            
            for(String cat : categories) {
                display.append("\n").append(cat).append(":\n");
                getFoodsByCategory(cat).forEach(f -> 
                    display.append("  - ").append(f.getName()).append("\n")
                );
            }
            return display.toString();
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize Food Database
        FoodDatabase foodDb = new FoodDatabase();
        
        System.out.println("\n========== NUTRITRACK - Advanced Nutrition Monitor ==========");
        System.out.println("Welcome! Let's create your profile for personalized tracking.\n");
        
        // User Profile Setup
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter your height (in meters): ");
        double height = scanner.nextDouble();
        
        System.out.print("Enter your weight (in kg): ");
        double weight = scanner.nextDouble();
        
        System.out.print("Enter your age: ");
        double age = scanner.nextDouble();
        
        scanner.nextLine(); // consume newline
        
        System.out.print("Enter your gender (M/F): ");
        String gender = scanner.nextLine();
        
        System.out.print("Enter activity level (sedentary/light/moderate/very_active/extra_active): ");
        String activity = scanner.nextLine();
        
        UserProfile user = new UserProfile(name, height, weight, age, gender, activity);
        
        // Diet Plan Selection
        System.out.println("\nChoose your diet plan:");
        System.out.println("1. Balanced Diet (default)");
        System.out.println("2. Low-Carb Diet");
        System.out.println("3. High-Protein Diet");
        System.out.println("4. Vegetarian Diet");
        System.out.print("Enter choice (1-4): ");
        
        int planChoice = scanner.nextInt();
        switch(planChoice) {
            case 2: user.setDietPlan(new LowCarbDiet()); break;
            case 3: user.setDietPlan(new HighProteinDiet()); break;
            case 4: user.setDietPlan(new VegetarianDiet()); break;
            default: user.setDietPlan(new BalancedDiet());
        }
        
        scanner.nextLine();
        
        // Add preferences
        System.out.print("Do you have any food allergies? (comma-separated, or press Enter): ");
        String allergiesInput = scanner.nextLine();
        if(!allergiesInput.isEmpty()) {
            for(String allergy : allergiesInput.split(",")) {
                user.addAllergy(allergy.trim());
            }
        }
        
        // Display user info
        System.out.printf("\n✓ Profile Created!\n");
        System.out.printf("Name: %s | BMI: %.1f | Daily Calorie Requirement: %.0f kcal\n",
            user.getName(), user.getBMI(), user.getCalorieRequirement());
        System.out.printf("Diet Plan: %s\n\n", user.getDietPlan().getPlanName());
        
        // Display food database
        System.out.println("========== AVAILABLE FOODS ==========");
        System.out.println(foodDb.displayAll());
        
        // Daily tracking
        DailyTracker dailyTracker = new DailyTracker(LocalDate.now(), user);
        WeeklyTracker weeklyTracker = new WeeklyTracker(user);
        
        System.out.println("\n========== TRACK YOUR MEALS ==========");
        System.out.println("Format: [meal] [food] [grams]");
        System.out.println("Meals: breakfast, lunch, dinner, snacks");
        System.out.println("Example: breakfast chicken 150");
        System.out.println("Commands: 'done' - finish, 'list' - show foods, 'report' - daily report\n");
        
        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if(input.equals("done")) break;
            if(input.equals("list")) {
                System.out.println(foodDb.displayAll());
                continue;
            }
            if(input.equals("report")) {
                System.out.println(dailyTracker.generateReport());
                NutritionInfo daily = dailyTracker.getDailyTotal();
                System.out.println(NutritionAnalyzer.analyzeNutrition(daily, user));
                System.out.println(DietPlanner.suggestPlan(daily, user));
                continue;
            }
            
            String[] parts = input.split("\\s+");
            if(parts.length != 3) {
                System.out.println("Invalid! Use: [meal] [food] [grams]");
                continue;
            }
            
            String meal = parts[0];
            String foodKey = parts[1];
            
            try {
                double grams = Double.parseDouble(parts[2]);
                Food food = foodDb.getFood(foodKey);
                
                if(food == null) {
                    System.out.println("❌ Food not found!");
                    continue;
                }
                if(!dailyTracker.getMeals().containsKey(meal)) {
                    System.out.println("❌ Meal type not found!");
                    continue;
                }
                
                // Check allergies
                if(user.getAllergies().stream().anyMatch(a -> food.getName().toLowerCase().contains(a))) {
                    System.out.println("⚠ WARNING: This food contains an ingredient you're allergic to!");
                    continue;
                }
                
                dailyTracker.addFoodToMeal(meal, food, grams);
                System.out.println("✓ Added " + food.getName() + " (" + grams + "g) to " + meal);
                
            } catch(NumberFormatException e) {
                System.out.println("❌ Invalid grams value!");
            }
        }
        
        // Final Reports
        weeklyTracker.addDailyTracker(LocalDate.now(), dailyTracker);
        
        System.out.println(dailyTracker.generateReport());
        NutritionInfo daily = dailyTracker.getDailyTotal();
        System.out.println(NutritionAnalyzer.analyzeNutrition(daily, user));
        System.out.println(DietPlanner.suggestPlan(daily, user));
        System.out.println(weeklyTracker.generateWeeklyReport());
        
        scanner.close();
    }
}
