function generateNumber() {
    var digits = [];
    while (digits.length < 4) {
        var digit = Math.floor(Math.random() * 10).toString();
        if (digits.indexOf(digit) === -1) {
            digits.push(digit);
        }
    }
    return digits.join('');
}
