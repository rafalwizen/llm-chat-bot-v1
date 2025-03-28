import java.io.IOException;
import java.util.Scanner;

public class Chatbot {
    public static void main(String[] args) {
        HuggingFaceClient client = new HuggingFaceClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Chatbot is ready! Type your message (type 'exit' to quit):");
        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            try {
                String response = client.sendRequest(input);
                System.out.println("AI: " + response);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
