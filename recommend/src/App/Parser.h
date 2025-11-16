#ifndef PARSER_H
#define PARSER_H

#include <string>
#include <vector>
#include <sstream>
#include <stdexcept>

class Parser {
public:
    // Static method to validate and parse the input string
    // This method will return a vector with the command and arguments, or an empty vector for invalid input.
    static std::pair<std::string, std::vector<unsigned long int>> checkInput(const std::string& input);
};

#endif // PARSER_H