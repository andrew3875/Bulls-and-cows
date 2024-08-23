require: scripts/functions.js
theme: /
    
    state: Greeting
        q!: ($regex</start>|начать|играть|давай играть|сыграем|новая игра)
        script:
            $session.secretNumber = generateNumber();
            $session.attempts = 0;
        a: Привет! Предлагаю сыграть в игру "Быки и коровы". Введи 4-значное число, которое я загадал {{ $session.secretNumber }}.
        go: Guess

    state: Guess
        q: $regex<^\d{4}$>
        script:
            $session.attempts += 1;
            var secret = $session.secretNumber;
            var guess = $parseTree.text;
            var bulls = 0;
            var cows = 0;

            for (var i = 0; i < 4; i++) {
                if (secret[i] === guess[i]) {
                    bulls += 1;
                } else if (secret.indexOf(guess[i]) !== -1) {
                    cows += 1;
                }
            }

            if (bulls === 4) {
                $session.won = true;
            } else {
                $session.won = false;
            }

            $temp.bulls = bulls;
            $temp.cows = cows;
        if: $session.won
            a: Поздравляю! Ты угадал число {{ $session.secretNumber }} за {{ $session.attempts }} попыток. Введи "начать" для новой игры.
            go: Greeting
        else:
            a: Быки: {{ $temp.bulls }}, коровы: {{ $temp.cows }}. Попробуй еще раз!
            go: Guess

    state: Error
        q!: *
        a: Я тебя не понимаю. Введи 4-значное число.
        go: Guess