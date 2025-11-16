const parseServerResponse = (response) => {
    // erase server res
    const trimmedResponse = response.trim();

    // the recommrndation starts after 200 ok\n\n
    const parts = trimmedResponse.split('\n\n');
     if (parts.length < 2) {
        return [];
    }

    const numbersLine = parts[1].trim();

    //split recommend movies by ','
    const movieIds = numbersLine
        .split(',')
        .filter((id) => !isNaN(id)) 
        .map((id) => id.trim()); 

    return movieIds;
};

module.exports = parseServerResponse;
