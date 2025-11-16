#include "Parser.h"
#include <algorithm>  // For std::string manipulation

// Static method to validate and parse the input string.
// It checks for valid characters (alphanumeric and space) and separates command and arguments.
std::pair<std::string, std::vector<unsigned long int>> Parser::checkInput(const std::string& input) {
    std::pair<std::string, std::vector<unsigned long int>> result;
    
    // Check if input contains invalid characters
    for (char c : input) {  
        if (!std::isalnum(c) && c != ' ') {
            return {};  // Invalid input
        }
    }

    std::vector<unsigned long int> args;  // Store arguments
    std::string word;  // Temporary string for each word in input

    std::istringstream stream(input);  // Create a string stream to split input by spaces

    // First word is the command (e.g., "POST")
    if (stream >> word) {
        result.first = word;
    }

    // If no command, return empty
    if (word.empty()) {
        return {};
    }

    // Process remaining words as arguments
    while (stream >> word) {
        try {
            size_t pos;
            long int number = std::stol(word, &pos);  // Convert string to long integer
            if (pos == word.length()) {  // Valid number
                result.second.push_back(number);  // Add to arguments list
            } else {
                return {};  // Invalid number format
            }
        } catch (const std::invalid_argument& e) {
            return {};  // Handle invalid argument exception
        }
    }

    return result;  // Return command and arguments
}