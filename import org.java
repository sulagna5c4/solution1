import org.json.JSONObject;
import java.util.Map;
import java.util.TreeMap;

public class ShamirSecretSharing {
    
    // Function to convert a number from any base to decimal
    public static int convertToDecimal(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Function to compute Lagrange Interpolation at x = 0 (finding constant term c)
    public static double lagrangeInterpolation(TreeMap<Integer, Integer> points) {
        double result = 0.0;

        for (Map.Entry<Integer, Integer> entry1 : points.entrySet()) {
            int x_i = entry1.getKey();
            int y_i = entry1.getValue();

            double term = y_i;

            for (Map.Entry<Integer, Integer> entry2 : points.entrySet()) {
                int x_j = entry2.getKey();
                if (x_j != x_i) {
                    term *= (0 - x_j) / (double) (x_i - x_j);
                }
            }
            result += term;
        }
        return result;
    }

    public static void main(String[] args) {
        // Example JSON input
        String jsonString = """
        {
            "keys": {
                "n": 4,
                "k": 3
            },
            "1": {
                "base": "10",
                "value": "4"
            },
            "2": {
                "base": "2",
                "value": "111"
            },
            "3": {
                "base": "10",
                "value": "12"
            },
            "6": {
                "base": "4",
                "value": "213"
            }
        }
        """;

        JSONObject json = new JSONObject(jsonString);
        JSONObject keys = json.getJSONObject("keys");
        int n = keys.getInt("n"); // Number of roots provided
        int k = keys.getInt("k"); // Minimum number of roots required

        TreeMap<Integer, Integer> points = new TreeMap<>();

        for (String key : json.keySet()) {
            if (!key.equals("keys")) {
                JSONObject root = json.getJSONObject(key);
                int x = Integer.parseInt(key);
                int base = root.getInt("base");
                String value = root.getString("value");
                int y = convertToDecimal(value, base);
                points.put(x, y);
            }
        }

        // Solve for constant term c using Lagrange Interpolation
        double secret = lagrangeInterpolation(points);
        System.out.println("The secret (constant term c) is: " + Math.round(secret));
    }
}
